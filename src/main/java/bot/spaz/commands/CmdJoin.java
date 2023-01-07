package bot.spaz.commands;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CmdJoin extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }

        VoiceChannel userVoiceChannel;
        String[] message = event.getMessage().getContentRaw().split(" ");

        if (message[0].equalsIgnoreCase("-join")) {
            try {
                userVoiceChannel = (VoiceChannel) event.getMessage().getMember().getVoiceState().getChannel();
                if (userVoiceChannel == null) {
                    event.getChannel().sendMessage("You must be in a voice channel to use commands.").queue();
                    return;
                }
                if (userVoiceChannel.getGuild().getSelfMember().getVoiceState().inAudioChannel()) {
                    event.getChannel().sendMessage("I'm already in a channel BRUH.").queue();
                    return;
                }
                userVoiceChannel.getGuild().getAudioManager().openAudioConnection(userVoiceChannel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
