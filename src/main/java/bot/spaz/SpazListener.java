package bot.spaz;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;
import java.util.logging.Logger;

public class SpazListener extends ListenerAdapter {

    public static StreamSpeechRecognizer recognizer;
    private static final HashMap<Long, UserVoiceObject> voiceObjects = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        Guild guild = event.getGuild();
        Message message = event.getMessage();
        User user = message.getAuthor();
        String content = message.getContentRaw();

        if (user.isBot()) return;

        if (!content.startsWith("-")) return;

        if (content.startsWith("-spaztest")) {
            event.getChannel().sendMessage("You said \"" + message.getContentRaw().replace("-spaztest", "").trim() + "\"").queue();
        }

        if (message.getContentRaw().startsWith("-leave")) {
            AudioManager audioManager = guild.getAudioManager();
            audioManager.closeAudioConnection();
        }

        if (message.getContentRaw().startsWith("-join")) {
            VoiceChannel voiceChannel = (VoiceChannel) message.getMember().getVoiceState().getChannel();

            // sphinx voice recognition configuration, acoustic model, vocab dictionary, pronunciation
            Configuration configuration = new Configuration();
            configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            configuration.setDictionaryPath("src/main/resources/3678.dic");
            configuration.setLanguageModelPath("src/main/resources/3678.lm");

            // prevents sphinx from logging spam
            Logger cmRootLogger = Logger.getLogger("default.config");
            cmRootLogger.setLevel(java.util.logging.Level.OFF);
            String conFile = System.getProperty("java.util.logging.config.file");

            if (conFile == null) {
                System.setProperty("java.util.logging.config.file", "ignoreAllSphinx4LoggingOutput");
            }

            try {
                voiceChannel.getGuild().getAudioManager().openAudioConnection(voiceChannel);
            } catch (Exception e) {
                System.out.println("Error connecting to channel " + e.getMessage());
            }

            try {
                recognizer = new StreamSpeechRecognizer(configuration);
                voiceChannel.getGuild().getAudioManager().setReceivingHandler(new AudioReceiveHandler() {
                    @Override
                    public boolean canReceiveCombined() {
                        return false;
                    }

                    @Override
                    public boolean canReceiveUser() {
                        return true;
                    }

                    @Override
                    public void handleCombinedAudio(CombinedAudio combinedAudio) {
                    }

                    @Override
                    public void handleUserAudio(UserAudio userAudio) {
                        if(voiceObjects.containsKey(userAudio.getUser().getIdLong())){
                            final UserVoiceObject voiceObject = voiceObjects.get(userAudio.getUser().getIdLong());
                            voiceObject.addToPacket(userAudio.getAudioData(1));
                        } else {
                            final UserVoiceObject voiceObject = new UserVoiceObject(userAudio.getUser(), userAudio.getAudioData(1));
                            voiceObjects.put(userAudio.getUser().getIdLong(), voiceObject);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
