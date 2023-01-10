package bot.spaz.listeners;

import ai.picovoice.porcupine.Porcupine;
import ai.picovoice.porcupine.PorcupineException;
import ignored.PicoToken;

public class WakeUp {

    Porcupine porcupine = new Porcupine.Builder()
            .setAccessKey(PicoToken.getToken())
            .setBuiltInKeywords(new Porcupine.BuiltInKeyword[]{Porcupine.BuiltInKeyword.PICOVOICE, Porcupine.BuiltInKeyword.BUMBLEBEE})
            .build();

    public WakeUp() throws PorcupineException {

    }
}
