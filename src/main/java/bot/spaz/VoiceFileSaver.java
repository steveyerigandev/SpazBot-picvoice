package bot.spaz;

import edu.cmu.sphinx.tools.audio.AudioPlayer;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.User;

import javax.sound.sampled.*;
import javax.sound.sampled.spi.AudioFileReader;
import java.io.*;
import java.util.HashMap;
import java.util.zip.Adler32;

public class VoiceFileSaver {

    HashMap<User, String> usersAudioData = new HashMap<>();

    public VoiceFileSaver() {
    }

    public void newStream(UserAudio userAudio) {

        // TODO First take the userAudio.getAudioData(1) original stereo byte[] and convert it into the target mono byte[]
        // TODO If a mono byte[] exists, figure out a way to concatenate the byte[]s together, either raw, or by switching
        // TODO to an InputStream or saving as a WAVE file
        // TODO Once Audio reaches 3 - 5 seconds long, have voice Recognition read the file, then reset file
        // TODO file reset may need to happen in the AudioRecieveHandler that way each tile it's reset, it is
        // TODO not recording and adding the the end of a preexisting audio file
        try {
            AudioInputStream newAudio = convertToMono(userAudio.getAudioData(1));
            // If user key exists, the new audio file is added to the existing audio file
            if (usersAudioData.containsKey(userAudio.getUser())) {
                try {
                    AudioSystem.write(newAudio, AudioFileFormat.Type.WAVE, new File("src/main/resources/tmp/" + userAudio.getUser().getIdLong() + "TEMP.wav"));
                    AudioInputStream convertedClip = AudioSystem.getAudioInputStream(new File("src/main/resources/tmp/" + userAudio.getUser().getIdLong() + "TEMP.wav"));
                    AudioInputStream existingClip = AudioSystem.getAudioInputStream(new File("src/main/resources/tmp/" + userAudio.getUser().getIdLong() + ".wav"));
                    AudioInputStream appendedAudio = new AudioInputStream(new SequenceInputStream(existingClip, convertedClip), existingClip.getFormat(), existingClip.getFrameLength() + convertedClip.getFrameLength());
                    AudioSystem.write(appendedAudio, AudioFileFormat.Type.WAVE, new File("src/main/resources/tmp/" + userAudio.getUser().getIdLong() + ".wav"));
                } catch (Exception e) {
                    System.out.println("Error appending audio files:" + e.getMessage());
                }
            } else {
                // If user key does not exist, creates new hashmap with new user key and file name as a String
                usersAudioData.put(userAudio.getUser(), "src/main/resources/tmp/" + userAudio.getUser().getIdLong() + ".wav");
                AudioSystem.write(newAudio, AudioFileFormat.Type.WAVE, new File("src/main/resources/tmp/" + userAudio.getUser().getIdLong() + ".wav"));
            }
        } catch (Exception e) {
            System.out.println("Error converting from stereo to mono: " + e.getMessage());
        }
    }

    public AudioInputStream convertToMono(byte[] audio) {
//        AudioFormat sourceFormat = new AudioFormat(48000, 16, 2, true, true);
        AudioFormat targetFormat = new AudioFormat(16000f, 16, 1,  true, false);
        return new AudioInputStream(new ByteArrayInputStream(audio), targetFormat, audio.length);
    }
}
