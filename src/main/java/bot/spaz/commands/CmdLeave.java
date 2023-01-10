package bot.spaz.commands;

import bot.spaz.listeners.WakeUpWordListener;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CmdLeave extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }

        VoiceChannel userVoiceChannel;
        String[] message = event.getMessage().getContentRaw().split(" ");

        if (message[0].equalsIgnoreCase("-leave")) {
            try {
                userVoiceChannel = (VoiceChannel) event.getMessage().getMember().getVoiceState().getChannel();
                if (userVoiceChannel == null) {
                    event.getChannel().sendMessage("You must be in a voice channel to use commands.").queue();
                    return;
                }
                event.getChannel().sendMessage("NOIGHT! NOIGHT!!").queue();
                userVoiceChannel.getGuild().getAudioManager().closeAudioConnection();
                WakeUpWordListener.getPorcupineINSTANCE().delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
