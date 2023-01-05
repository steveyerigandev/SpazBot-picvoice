package bot.spaz.listeners;

import bot.spaz.VoiceFileSaver;
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

        VoiceChannel userVoiceChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
        VoiceFileSaver voiceFileSaver = new VoiceFileSaver();

        try {
            userVoiceChannel.getGuild().getAudioManager().openAudioConnection(userVoiceChannel);
        } catch (Exception e) {
            System.out.println("Error connecting to voice channel: " + e.getMessage());
        }

        try {
            userVoiceChannel.getGuild().getAudioManager().setReceivingHandler(new AudioReceiveHandler() {
                @Override
                public boolean canReceiveCombined() {
                    return false;
                }

                @Override
                public boolean canReceiveUser() {
                    return true;
                }

                @Override
                public void handleCombinedAudio(@NotNull CombinedAudio combinedAudio) {
                }

                @Override
                public void handleUserAudio(@NotNull UserAudio userAudio) {
                    if (!userAudio.getUser().isBot()) {
                        voiceFileSaver.newStream(userAudio);
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Error setting Audio Receive Handler: " + e.getMessage());
        }
    }
}