package bot.spaz.commands;

import bot.spaz.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class CmdVoiceCommands {

    public CmdVoiceCommands() {
    }

    public static void transcriptParse(TextChannel textChannel, String transcript, VoiceChannel voiceChannel) {
        if (transcript.length() < 4) {
            textChannel.sendMessage("Empty request").queue();
            return;
        }
        String[] transcriptWords = transcript.split(" ");
        if(transcriptWords[0].equalsIgnoreCase("play") || transcriptWords[0].equalsIgnoreCase("plain")){
            System.out.println(transcript.substring(5));
            PlayerManager.getINSTANCE().loadAndPlay(textChannel, transcript.substring(5));
        }
        if (transcriptWords[0].equalsIgnoreCase("skip") || transcriptWords[0].equalsIgnoreCase("skep")) {
            PlayerManager.getINSTANCE().getMusicManager(voiceChannel.getGuild()).scheduler.nextTrack();
        }
    }
}