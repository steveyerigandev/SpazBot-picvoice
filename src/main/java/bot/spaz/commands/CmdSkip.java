package bot.spaz.commands;

import bot.spaz.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CmdSkip extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }

        VoiceChannel userVoiceChannel;
        TextChannel userTextChannel = event.getChannel().asTextChannel();
        String[] message = event.getMessage().getContentRaw().split(" ");
        String link = event.getMessage().getContentRaw().substring(5).trim();

        if (message[0].equalsIgnoreCase("-skip")) {
            try {
                userVoiceChannel = (VoiceChannel) event.getMessage().getMember().getVoiceState().getChannel();
                if (userVoiceChannel == null) {
                    event.getChannel().sendMessage("You must be in a voice channel to use music player.").queue();
                    return;
                }
                if (userVoiceChannel.getGuild().getSelfMember().getVoiceState().getChannel() != userVoiceChannel) {
                    event.getChannel().sendMessage("I'm in a different channel BRUH.").queue();
                    return;
                }

                PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).scheduler.nextTrack();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
