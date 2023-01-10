package bot.spaz.listeners;

import ai.picovoice.porcupine.Porcupine;
import ai.picovoice.porcupine.PorcupineException;
import ignored.PicoToken;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class WakeUpWordListener extends ListenerAdapter implements AudioReceiveHandler {

    private static Porcupine porcupineINSTANCE;
    AudioReceiveHandler audioReceiveHandler;
    ArrayList<short[]> userAudioShorts = new ArrayList<>();
    int keyword;

    public void Listen() throws PorcupineException {
        porcupineINSTANCE = new Porcupine.Builder()
                .setAccessKey(PicoToken.getToken())
                .setBuiltInKeywords(new Porcupine.BuiltInKeyword[]{Porcupine.BuiltInKeyword.PICOVOICE, Porcupine.BuiltInKeyword.BUMBLEBEE})
                .build();
        this.audioReceiveHandler = new AudioReceiveHandler() {

            @Override
            public boolean canReceiveUser() {
                return true;
            }

            @Override
            public void handleUserAudio(@NotNull UserAudio userAudio) {
                userAudioShorts.add(convertToShortArray(userAudio.getAudioData(1)));
            }

            public short[] convertToShortArray(byte[] bytes) {
                int size = bytes.length;
                short[] shorts = new short[size];
                for(int i = 0;i < size;i++){
                    shorts[i] = bytes[i];
                }
                return shorts;
            }
        };
    }

    public static Porcupine getPorcupineINSTANCE() {
        return porcupineINSTANCE;
    }
}


//    @Override
//    public boolean canReceiveUser() {
//        try {
//            while (userAudioShorts.size() > 0) {
//                int keyword = porcupineINSTANCE.process(userAudioShorts.get(0));
//                userAudioShorts.remove(0);
//                if (keyword == 0) {
//                    System.out.println("PORCUPINE");
//                } else if (keyword == 1) {
//                    System.out.println("NOTHING........");
//                }
//            }
//        } catch (PorcupineException e) {
//            throw new RuntimeException(e);
//        }
//        return AudioReceiveHandler.super.canReceiveUser();
//    }
