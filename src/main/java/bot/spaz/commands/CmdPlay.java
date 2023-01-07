package bot.spaz.commands;

import bot.spaz.lavaplayer.PlayerManager;
import bot.spaz.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;

public class CmdPlay extends ListenerAdapter{

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
                userVoiceChannel = (VoiceChannel) event.getMessage().getMember().getVoiceState().getChannel();
                if (userVoiceChannel == null) {
                    event.getChannel().sendMessage("You must be in a voice channel to use music player.").queue();
                    return;
                }
                if (userVoiceChannel.getGuild().getSelfMember().getVoiceState().getChannel() != userVoiceChannel) {
                    event.getChannel().sendMessage("I'm in a different channel BRUH.").queue();
                    return;
                }

                if(!isURL(link)){
                    play(link, userVoiceChannel, userTextChannel);
                }
                userVoiceChannel.getGuild().getAudioManager().openAudioConnection(userVoiceChannel);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void play(String link, VoiceChannel voiceChannel, TextChannel textChannel) {
        if(voiceChannel == null){
            textChannel.sendMessage("Unable to queue song, user must be in a voice channel").queue();
        }
        if (link.trim().equals("")) {
            textChannel.sendMessage("Empty request.").queue();
        } else {
            if (!isURL(link)) {
                link = "ytsearch:" + link + " audio";
            }
            // play music
            PlayerManager.getINSTANCE().loadAndPlay(textChannel, link);
        }
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
