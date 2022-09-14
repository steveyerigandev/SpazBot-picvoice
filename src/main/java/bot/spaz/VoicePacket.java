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

public class VoicePacket {

    private byte[] packet;
    private User user;

    public VoicePacket(User user, byte[] packet) {
        this.user = user;
        this.packet = packet;
    }

    public void addToPacket(byte[] toAdd) {
        int length = (packet != null ? packet.length : toAdd.length);
        if (length >= 576000) {
            try {
                getResult(SpazListener.recognizer);
            } catch (Exception e) {
                e.printStackTrace();
            }
            packet = toAdd;
            return;
        }

        byte[] newPacket = new byte[length + 3840];
        System.arraycopy(packet, 0, newPacket, 0, length);
        System.arraycopy(toAdd, 0, newPacket, 3840, toAdd.length);
        packet = newPacket;
    }

    public void getResult(StreamSpeechRecognizer recognizer) throws FileNotFoundException {
        // Audio output format specification
        AudioFormat target = new AudioFormat(16000f, 16, 1, true, false);
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet);
        final AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, AudioReceiveHandler.OUTPUT_FORMAT, packet.length);
        AudioInputStream AIS = AudioSystem.getAudioInputStream(target, audioInputStream);

        try {

            AudioSystem.write(AIS, AudioFileFormat.Type.WAVE, new File("src/main/resources/tmp/" + user.getId() + ".wav"));
            InputStream stream = new FileInputStream("src/main/resources/tmp/" + user.getId() + ".wav");

            byte[] isArray = new byte[AIS.available()];
            byte[] streamArray = new byte[stream.available()];

            stream.read(streamArray);
            AIS.read(isArray);
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
