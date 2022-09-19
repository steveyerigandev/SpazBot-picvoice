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

public class VoiceFileSaver {

    HashMap<User, byte[]> usersAudioData = new HashMap<>();

    public VoiceFileSaver() {
    }

    public void updateStream(UserAudio userAudio) {
        // Capture the newly streamed 20 millisecond audio blip and convert it to CMU Sphinx 4 format
        try {
            User user = userAudio.getUser();
            byte[] newAudio = userAudio.getAudioData(1);
            newAudio = convertFromStereoToMono(newAudio, user);
            // If user key exists, the newly converted byte[] is added to the existing byte[]
            if (usersAudioData.containsKey(user)) {
                byte[] existingAudioData = usersAudioData.get(user);
                usersAudioData.put(user, createMetaAudio(existingAudioData, newAudio));
            } else {
                // If user key does not exist, creates new hashmap with new user key and converted byte[]
                usersAudioData.put(userAudio.getUser(), newAudio);
            }
        } catch(Exception e){
            System.out.println("Error with updateStream(): " + e.getMessage());
        }
    }

    public byte[] convertFromStereoToMono(byte[] newAudio, User user){
        // Target format required by CMU Sphinx 4 for voice recognition
        byte[] convertedAudio = null;
        AudioFormat target = new AudioFormat(16000, 16, 1, true, false);
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(target, new AudioInputStream(new ByteArrayInputStream(newAudio), AudioReceiveHandler.OUTPUT_FORMAT, newAudio.length));
        try {
            convertedAudio = inputStream.readAllBytes();
        } catch(IOException e){
            System.out.println("Error saving new audio data" + e.getMessage());
        }
        //TODO figure out how to save the input stream as a new byte[]
        //TODO method needs logic for converting stereo to mono, update return statement
//        return inputStream;
        return convertedAudio;
    }

    public byte[] createMetaAudio(byte[] existingAudio, byte[] newAudio) {
        //TODO method needs logic for combining byte[]s, update return statement
        byte[] combinedArray = existingAudio;
        return combinedArray;
    }

    public void saveAudio(){
        //TODO method needs logic for saving or overwriting audio file, based on user ID, must be WAVE format
    }
}
