package bot.spaz;

import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.User;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

public class VoiceInputConvertor {

    public VoiceInputConvertor (UserAudio userAudio){
        // audioData Provides 20 milliseconds of combined audio data in 48khz 16bit stereo signed BigEndian PCM
        byte[] audioData = userAudio.getAudioData(1);
        User user = userAudio.getUser();

        //TODO Save audio file, convert it to the proper Sphinx encoding/format then Call Sphinx constructor with file
//        AudioSystem.write(userAudio, AudioFileFormat.Type.WAVE, "src/main/resources/tmp" + user.getId() + ".wav");

    }
}
