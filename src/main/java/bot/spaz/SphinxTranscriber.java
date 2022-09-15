package bot.spaz;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.User;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class SphinxTranscriber {
    // Sphinx configuration

    public SphinxTranscriber(UserAudio userAudio) throws IOException {
        User user = userAudio.getUser();

        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("src/main/resources/model.dic");
        configuration.setLanguageModelPath("src/main/resources/model.lm");

        // ignores Sphinx spam in console
        Logger cmRootLogger = Logger.getLogger("default.config");
        cmRootLogger.setLevel(java.util.logging.Level.OFF);
        String conFile = System.getProperty("java.util.logging.config.file");
        if (conFile == null) {
            System.setProperty("java.util.logging.config.file", "ignoreAllSphinx4LoggingOutput");
        }

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
    }
}
