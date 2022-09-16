package bot.spaz;

import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.User;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;

public class VoiceInputConverter {

    byte[] audio;
    User user;

    public VoiceInputConverter(UserAudio userAudio) {
        // audioData Provides 20 milliseconds of combined audio data in 48khz 16bit stereo signed BigEndian PCM
        this.audio = userAudio.getAudioData(1);
        this.user = userAudio.getUser();
    }

    public void saveAudioFile() {
        AudioFormat sphinxTargetFormat = new AudioFormat(16000f, 16, 1, true, false);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audio);
        AudioInputStream ais = new AudioInputStream(byteArrayInputStream, AudioReceiveHandler.OUTPUT_FORMAT, audio.length);

        try {
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File("src/main/resources/tmp" + user.getIdLong() + ".wav"));
            System.out.println("file written");
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
    //TODO Save audio file, convert it to the proper Sphinx encoding/format then Call Sphinx constructor with file
//        AudioSystem.write(userAudio, AudioFileFormat.Type.WAVE, "src/main/resources/tmp" + user.getId() + ".wav");

}

