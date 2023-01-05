package bot.spaz.commands;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class CmdJoin {

    VoiceChannel voiceChannel;

    public void setVoiceChannel(VoiceChannel voiceChannel) {
        this.voiceChannel = voiceChannel;
    }

    public void joinChannel(){
        try {
            voiceChannel.getGuild().getAudioManager().openAudioConnection(voiceChannel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
