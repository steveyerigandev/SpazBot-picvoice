package bot.spaz.commands;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class CmdLeave extends ListenerAdapter {

    public CmdLeave() {
    }

    public void leave(AudioManager audioManager){
        audioManager = audioManager.getGuild().getAudioManager();
        audioManager.closeAudioConnection();
    }
}
