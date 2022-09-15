package bot.spaz;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.logging.Logger;

public class SpeechRecording extends ListenerAdapter {

    public static StreamSpeechRecognizer recognizer;
    private static HashMap<Long, UserVoiceObject> userVoiceObjects = new HashMap<>();

    public void run(MessageReceivedEvent event) {

        if (!event.getMessage().getContentRaw().equalsIgnoreCase("-join")) return;
        VoiceChannel userVoiceChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
        // Sphinx configuration
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("src/main/resources/model.dic");
        configuration.setLanguageModelPath("src/main/resources/model.lm");

        // prevent logging of Sphinx4 spam in console
        Logger cmRootLogger = Logger.getLogger("default.config");
        cmRootLogger.setLevel(java.util.logging.Level.OFF);
        String conFile = System.getProperty("java.util.logging.config.file");
        if (conFile == null) {
            System.setProperty("java.util.logging.config.file", "ignoreAllSphinx4LoggingOutput");
        }
        try {
            userVoiceChannel.getGuild().getAudioManager().openAudioConnection(userVoiceChannel);
        } catch (Exception e) {
            System.out.println("Error while Joining " + e.getMessage());
        }

        try {
            recognizer = new StreamSpeechRecognizer(configuration);
            userVoiceChannel.getGuild().getAudioManager().setReceivingHandler(new AudioReceiveHandler() {
                @Override
                public boolean canReceiveCombined() {
                    return false;
                }

                @Override
                public boolean canReceiveUser() {
                    return true;
                }

                @Override
                public void handleCombinedAudio(@NotNull CombinedAudio combinedAudio) {
                }

                @Override
                public void handleUserAudio(@NotNull UserAudio userAudio) {
                    if (userVoiceObjects.containsKey(userAudio.getUser().getIdLong())) {
                        final UserVoiceObject userVoiceObject = userVoiceObjects.get(userAudio.getUser().getIdLong());
                        userVoiceObject.addToPacket(userAudio.getAudioData(1));
                    } else {
                        final UserVoiceObject userVoiceObject = new UserVoiceObject(userAudio.getUser(), userAudio.getAudioData(1));
                        userVoiceObjects.put(userAudio.getUser().getIdLong(), userVoiceObject);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
