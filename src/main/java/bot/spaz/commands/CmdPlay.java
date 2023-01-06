package bot.spaz.commands;

import bot.spaz.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.net.URI;
import java.net.URISyntaxException;

public class CmdPlay extends ListenerAdapter{

    public CmdPlay() {
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
            textChannel.sendMessage("(TEST) You want to listen to **" + link + "**").queue();
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
