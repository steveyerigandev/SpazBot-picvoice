package bot.spaz.commands;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CmdJoin extends ListenerAdapter {

    VoiceChannel userVoiceChannel;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        String[] message = event.getMessage().getContentRaw().split(" ");

        if (message[0].equalsIgnoreCase("-join")) {
            try {
                userVoiceChannel = (VoiceChannel) event.getMessage().getMember().getVoiceState().getChannel();
                if (userVoiceChannel == null) {
                    event.getChannel().sendMessage("You must be in a voice channel.").queue();
                } else {
                    userVoiceChannel.getGuild().getAudioManager().openAudioConnection(userVoiceChannel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
