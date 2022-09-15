package bot.spaz;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.User;


import java.io.IOException;
import java.util.logging.Logger;

public class SphinxTranscriber {

    public SphinxTranscriber() throws IOException {

        // sets configuration acoustic model, language, and dictionary
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setLanguageModelPath("src/main/resources/model.lm");
        configuration.setDictionaryPath("src/main/resources/model.dic");

        // ignores Sphinx spam in console
        Logger cmRootLogger = Logger.getLogger("default.config");
        cmRootLogger.setLevel(java.util.logging.Level.OFF);
        String conFile = System.getProperty("java.util.logging.config.file");
        if (conFile == null) {
            System.setProperty("java.util.logging.config.file", "ignoreAllSphinx4LoggingOutput");
        }

        // Stream speech recognizer uses InputStream as the source, this can be from a file
        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
    }
}
