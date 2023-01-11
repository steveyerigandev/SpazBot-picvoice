package bot.spaz.listeners;

import ai.picovoice.porcupine.Porcupine;
import ai.picovoice.porcupine.PorcupineException;
import bot.spaz.lavaplayer.PlayerManager;
import ignored.PicoToken;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class WakeUpWordListener extends ListenerAdapter implements AudioReceiveHandler {

    private static Porcupine porcupineINSTANCE;
    int keyword;

    public void Listen(TextChannel textChannel) throws PorcupineException {
        porcupineINSTANCE = new Porcupine.Builder()
                .setAccessKey(PicoToken.getToken())
                .setBuiltInKeywords(new Porcupine.BuiltInKeyword[]{Porcupine.BuiltInKeyword.PICOVOICE, Porcupine.BuiltInKeyword.BUMBLEBEE})
                .build();

        AudioReceiveHandler audioReceiveHandler = new AudioReceiveHandler() {

            @Override
            public boolean canReceiveUser() {
                return true;
            }

            @Override
            public void handleUserAudio(@NotNull UserAudio userAudio) {
                try {
                    keyword = porcupineINSTANCE.process(convertToShortArray(userAudio.getAudioData(1)));
                    if (keyword == 0) {
                        textChannel.sendMessage("TESTING - 1").queue();
                    } else if (keyword == 1) {
                        textChannel.sendMessage("TESTING - 2").queue();
                    }
                } catch (PorcupineException e) {
                    throw new RuntimeException(e);
                }
            }

            public short[] convertToShortArray(byte[] bytes) {
                int size = bytes.length;
                short[] shorts = new short[size];
                for (int i = 0; i < size; i++) {
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
