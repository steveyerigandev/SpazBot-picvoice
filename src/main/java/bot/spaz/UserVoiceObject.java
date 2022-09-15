package bot.spaz;

import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.entities.User;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;

public class UserVoiceObject {

    private byte[] packet;
    private User user;

    public UserVoiceObject(User user, byte[] packet) {
        this.user = user;
        this.packet = packet;
    }

    public void addToPacket(byte[] toAdd) {
        int length = (packet != null ? packet.length : toAdd.length);
        if (length >= 576000) {
            try {
                getResult(SpeechRecording.recognizer);
            } catch (Exception e) {
                e.printStackTrace();
            }
            packet = toAdd; // reset the packet
            return;
        }
        byte[] newPacket = new byte[length + 3840];
        // copy old packet onto new temp array
        System.arraycopy(packet, 0, newPacket, 0, length);
        // copy toAdd packet onto new temp array
        System.arraycopy(toAdd, 0, newPacket, 3840, toAdd.length);
        // overwrite the old packet with the newly resized packet
        packet = newPacket;
    }

    public void getResult(StreamSpeechRecognizer recognizer) throws FileNotFoundException {
        // specify the output format you want
        AudioFormat target = new AudioFormat(16000f, 16, 1, true, false);
        // get the audio stream ready and pass the raw byte[]
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet);
        final AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, AudioReceiveHandler.OUTPUT_FORMAT, packet.length);
        AudioInputStream is = AudioSystem.getAudioInputStream(target, audioInputStream);
        // write a temporary file to othe computer somewhere, this method will return an InputStream that can be used for recognition
        try {
            AudioSystem.write(is, AudioFileFormat.Type.WAVE, new File("src/main/resources/tmp/" + user.getId() + ".wav"));
            InputStream stream = new FileInputStream("src/main/resources/tmp/" + user.getId() + ".wav");
            byte[] isArray = new byte[is.available()];
            byte[] streamArray = new byte[stream.available()];
            stream.read(streamArray);
            is.read(isArray);
            stream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        InputStream stream = new FileInputStream("src/main/resources/tmp/" + user.getId() + ".wav");
        try {
            stream = new ByteArrayInputStream(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        recognizer.startRecognition(stream);
        SpeechResult result;
        while ((result = recognizer.getResult()) != null) {
            System.out.format("Hypothesis: %s\n", result.getHypothesis());
        }
        recognizer.stopRecognition();
    }
}
