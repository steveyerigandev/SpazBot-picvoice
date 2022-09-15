package bot.spaz;

import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SpazVoiceListener extends ListenerAdapter {

    // joins channel of user that types "-join", audio from each user is sent to the convertor
    public void run(MessageReceivedEvent event) {

        if (!event.getMessage().getContentRaw().equalsIgnoreCase("-join")) return;
        VoiceChannel userVoiceChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

        try {
            userVoiceChannel.getGuild().getAudioManager().openAudioConnection(userVoiceChannel);
        } catch (Exception e) {
            System.out.println("Error while Joining " + e.getMessage());
        }

        try {
            userVoiceChannel.getGuild().getAudioManager().setReceivingHandler(new AudioReceiveHandler() {
                @Override
                public boolean canReceiveCombined() {
                    return false;
                }

                @Override
                public boolean canReceiveUser() {
                    return AudioReceiveHandler.super.canReceiveUser();
                }

                @Override
                public void handleCombinedAudio(@NotNull CombinedAudio combinedAudio) {
                }

                @Override
                public void handleUserAudio(@NotNull UserAudio userAudio) {
                    VoiceInputConverter converter = new VoiceInputConverter(userAudio); // creates new converter
                    // Create generic class and run converter method from converter class instead??
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
