package bot.spaz;

import ignored.Token;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import edu.cmu.sphinx.api.Configuration;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Queue;
import java.nio.ByteBuffer;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import java.util.concurrent.ConcurrentLinkedQueue;


public class SpazBot extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {

        Configuration sphinxConfig = new Configuration();
        sphinxConfig.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        sphinxConfig.setDictionaryPath("src\\main\\resources\\3678.dic");
        sphinxConfig.setLanguageModelPath("src\\main\\resources\\3678.lm");

        JDABuilder.createDefault(new Token().getToken())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                .setActivity(Activity.playing("the dangerous game of figuring out how to code a bot"))  // sets default bot activity
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .enableCache(CacheFlag.VOICE_STATE)
                .addEventListeners(new SpazBot())
                .build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        StreamSpeechRecognizer recognizer;
        Guild guild = event.getGuild();
        Message message = event.getMessage();
        User user = message.getAuthor();
        String content = message.getContentRaw();

        if (user.isBot()) return;

        if (!content.startsWith("-")) return;

        if (content.startsWith("-spaztest")) {
            event.getChannel().sendMessage("You said \"" + message.getContentRaw().replace("-spaztest", "").trim() + "\"").queue();
        }

        if (message.getContentRaw().startsWith("-join")) {
            VoiceChannel voiceChannel = (VoiceChannel) message.getMember().getVoiceState().getChannel();
            AudioManager audioManager = guild.getAudioManager();

            // sphinx voice recognition configuration, acoustic model, vocab dictionary, pronunciation
            Configuration configuration = new Configuration();
            configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            configuration.setDictionaryPath("resources/3678.dic");
            configuration.setLanguageModelPath("resources/3678.lm");

            // prevents sphinx from logging spam
            Logger cmRootLogger = Logger.getLogger("default.config");
            cmRootLogger.setLevel(java.util.logging.Level.OFF);
            String conFile = System.getProperty("java.util.logging.config.file");

            if (conFile == null) {
                System.setProperty("java.util.logging.config.file", "ignoreAllSphinx4LoggingOutput");
            }

            try {
                audioManager.openAudioConnection(voiceChannel);
            } catch (Exception e) {
                System.out.println("Error connecting too channel " + e.getMessage());
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

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (message.getContentRaw().startsWith("-leave")) {
            AudioManager audioManager = guild.getAudioManager();
            audioManager.closeAudioConnection();
        }
    }
}