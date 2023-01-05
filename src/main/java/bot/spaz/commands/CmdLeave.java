package bot.spaz.commands;

import net.dv8tion.jda.api.managers.AudioManager;

public class CmdLeave {

    public CmdLeave() {
    }

    public void leave(AudioManager audioManager){
        audioManager = audioManager.getGuild().getAudioManager();
        audioManager.closeAudioConnection();
    }
}
