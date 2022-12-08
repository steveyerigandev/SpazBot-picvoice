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
    AudioInputStream storedInputStream;

    public VoiceFileSaver() {
    }

    // This runs every 20ms, basically every single time the JDA handleUserAudio method fires
    public void newStream(UserAudio userAudio) {

        // TODO Figure out how to play back audio data within IntelliJ THEN figure out how to store it temporarily as a full
//      // TODO consolidated sound file
        // TODO First take the userAudio.getAudioData(1) original stereo byte[] and convert it into the target mono byte[]
        // TODO If a mono byte[] exists, figure out a way to concatenate the byte[]s together, either raw, or by switching
        // TODO to an InputStream or saving as a WAVE file
        // TODO Once Audio reaches 3 - 5 seconds long, have voice Recognition read the file, then reset file
        // TODO file reset may need to happen in the AudioRecieveHandler that way each tile it's reset, it is
        // TODO not recording and adding the the end of a preexisting audio file

        int totalFramesRead = 0;
        byte[] audioArray = userAudio.getAudioData(1);
        User user = userAudio.getUser();
        AudioFormat sourceFormat = new AudioFormat(48000, 16, 2, true, true);

        try {
            AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(audioArray), sourceFormat, audioArray.length);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error combining source audio: " + e.getMessage());
        }
    }

//    public AudioInputStream convertToMono(UserAudio userAudio) {
//        byte[] audio = userAudio.getAudioData(1);
//        AudioFormat sourceFormat = new AudioFormat(48000, 16, 2, true, true);
//        AudioFormat targetFormat = new AudioFormat(16000f, 16, 1, true, false);
//        return new AudioInputStream(new ByteArrayInputStream(audio), sourceFormat, audio.length);
//    }
}
