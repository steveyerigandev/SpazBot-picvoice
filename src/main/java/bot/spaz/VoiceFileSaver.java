package bot.spaz;

import edu.cmu.sphinx.tools.audio.AudioPlayer;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.User;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.spi.AudioFileReader;
import java.io.*;
import java.util.HashMap;
import java.util.zip.Adler32;

public class VoiceFileSaver {

    HashMap<User, String> usersAudioData = new HashMap<>();

    public VoiceFileSaver() {
    }

    public void newStream(UserAudio userAudio) {

        try {
            AudioInputStream newClip = convertStereoToMono(userAudio.getAudioData(1));
            // If user key exists, the new audio file is added to the existing audio file
            if (usersAudioData.containsKey(userAudio.getUser())) {
                try {
                    AudioSystem.write(newClip, AudioFileFormat.Type.WAVE, new File("src/main/resources/tmp/" + userAudio.getUser().getIdLong() + "TEMP.wav"));
                    AudioInputStream convertedClip = AudioSystem.getAudioInputStream(new File("src/main/resources/tmp/" + userAudio.getUser().getIdLong() + "TEMP.wav"));
                    AudioInputStream existingClip = AudioSystem.getAudioInputStream(new File("src/main/resources/tmp/" + userAudio.getUser().getIdLong() + ".wav"));
                    System.out.println(existingClip.getFormat());
                    System.out.println(convertedClip.getFormat());
                    AudioInputStream appendedAudio = new AudioInputStream(new SequenceInputStream(existingClip, convertedClip), existingClip.getFormat(), existingClip.getFrameLength() + convertedClip.getFrameLength());
                    AudioSystem.write(appendedAudio, AudioFileFormat.Type.WAVE, new File("src/main/resources/tmp/" + userAudio.getUser().getIdLong() + ".wav"));
                } catch (Exception e) {
                    System.out.println("Error appending audio files:" + e.getMessage());
                }
            } else {
                // If user key does not exist, creates new hashmap with new user key and file name as a String
                usersAudioData.put(userAudio.getUser(), "src/main/resources/tmp/" + userAudio.getUser().getIdLong() + ".wav");
                AudioSystem.write(newClip, AudioFileFormat.Type.WAVE, new File("src/main/resources/tmp/" + userAudio.getUser().getIdLong() + ".wav"));
            }
        } catch (Exception e) {
            System.out.println("Error converting from stereo to mono: " + e.getMessage());
        }
    }

    public AudioInputStream convertStereoToMono(byte[] audio) {
        // Target format required by CMU Sphinx 4 for voice recognition
        AudioFormat target = new AudioFormat(16000, 16, 1, true, false);
        AudioFormat testTarget = new AudioFormat(48000, 16, 2, true, true);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audio);
        return new AudioInputStream(byteArrayInputStream, testTarget, audio.length);
    }
}
