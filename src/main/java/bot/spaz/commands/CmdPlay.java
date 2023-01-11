package bot.spaz.commands;

import bot.spaz.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;

public class CmdPlay extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }

        VoiceChannel userVoiceChannel;
        TextChannel userTextChannel = event.getChannel().asTextChannel();
        String[] message = event.getMessage().getContentRaw().split(" ");
        String link = event.getMessage().getContentRaw().substring(5).trim();

        if (message[0].equalsIgnoreCase("-play")) {
            try {
                // Grabs the user's voice state, if the user isn't in a voice channel then
                // userVoiceChannel will be null.
                userVoiceChannel = (VoiceChannel) event.getMessage().getMember().getVoiceState().getChannel();
                if (userVoiceChannel == null) {
                    event.getChannel().sendMessage("You must be in a voice channel to use music player.").queue();
                    return;
                }
                // If userVoiceChannel is not the same as the bot's AND the bot's current
                // channel isn't null.  Prevents bot from switching channels when already
                // being used.
                if (userVoiceChannel.getGuild().getSelfMember().getVoiceState().getChannel() != userVoiceChannel && userVoiceChannel.getGuild().getSelfMember().getVoiceState().getChannel() != null) {
                    event.getChannel().sendMessage("I'm literlerlerlally in a different channel, BRUH!").queue();
                    return;
                }
                // Accounts for a case where the bot's voice channel is null.
                if (userVoiceChannel.getGuild().getSelfMember().getVoiceState().getChannel() == null) {
                    event.getChannel().sendMessage("I'm not in a voice channel, use '**-join**' My G").queue();
                    return;
                }
                // Accounts for '-play' with empty request after
                if (link.equals("")) {
                    event.getChannel().sendMessage("Homie you stoooopid or sumtin'? You sent an empty request. Use '**-play [artist name] [song name]**'").queue();
                    return;
                }
                // Method called to check if user input is a URL or not
                if (!isURL(link)) {
                    play(link, userVoiceChannel, userTextChannel);
                }
                userVoiceChannel.getGuild().getAudioManager().openAudioConnection(userVoiceChannel);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void play(String link, VoiceChannel voiceChannel, TextChannel textChannel) {
        if (!isURL(link)) {
            link = "ytsearch:" + link + " audio";
        }
        // play music
        PlayerManager.getINSTANCE().loadAndPlay(textChannel, link);
    }

    public boolean isURL(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
