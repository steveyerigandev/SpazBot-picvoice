package bot.spaz.commands;

import bot.spaz.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;

public class CmdVoiceCommands {

    public CmdVoiceCommands() {
    }

    public void transcriptParse(TextChannel textChannel, String transcript){
        if(transcript.length() < 4){
            textChannel.sendMessage("Empty request").queue();
            return;
        }
        String[] transcriptWords = transcript.split(" ");
        if(transcriptWords[0].equalsIgnoreCase("play") || transcriptWords[0].equalsIgnoreCase("plain")){
            System.out.println(transcript.substring(4));
            PlayerManager.getINSTANCE().loadAndPlay(textChannel, transcript.substring(4));
        }
    }
}
