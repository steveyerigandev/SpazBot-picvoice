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

    HashMap<User, byte[]> usersAudioData = new HashMap<>();

    public VoiceFileSaver() {
    }

    public void newStream(UserAudio userAudio) {
        User user = userAudio.getUser();
        byte[] audio = userAudio.getAudioData(1);

        try {
            convertThenWrite(audio, user);
//            addToExistingAudio(user);
            // If user key exists, the newly converted byte[] is added to the existing byte[]
            if (usersAudioData.containsKey(user)) {
                byte[] existingAudioData = usersAudioData.get(user);
                usersAudioData.put(user, createMetaAudio(existingAudioData, audio));
            } else {
                // If user key does not exist, creates new hashmap with new user key and converted byte[]
                usersAudioData.put(userAudio.getUser(), audio);
            }
        } catch (Exception e) {
            System.out.println("Error with updateStream(): " + e.getMessage());
        }
    }

    public void addToExistingAudio(){

    }

    public void convertThenWrite(byte[] audio, User user) {
        // Target format required by CMU Sphinx 4 for voice recognition
        AudioFormat target = new AudioFormat(16000, 16, 1, true, false);
        AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(audio), target, audio.length);
        try {
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File("src/main/resources/tmp/" + user.getIdLong() + "temp.wav"));
        } catch (Exception e) {
            System.out.println("Error converting, saving or combining files" + e.getMessage());
        }
    }

    public byte[] createMetaAudio(byte[] existingAudio, byte[] newAudio) {
        //TODO method needs logic for combining byte[]s, update return statement
        byte[] combinedArray = existingAudio;
        return combinedArray;
    }

    public void saveAudio() {
        //TODO method needs logic for saving or overwriting audio file, based on user ID, must be WAVE format
    }
}
