package bot.spaz;

import ignored.Token;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import edu.cmu.sphinx.api.Configuration;
import net.dv8tion.jda.api.OnlineStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Queue;
import java.nio.ByteBuffer;
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
        Message message = event.getMessage();
        User user = message.getAuthor();
        String content = message.getContentRaw();
        Guild guild = event.getGuild();

        if (user.isBot()) return;

        if (!content.startsWith("-")) return;

        if (content.startsWith("-spaztest")) {
            event.getChannel().sendMessage("You said \"" + message.getContentRaw().replace("-spaztest", "").trim() + "\"").queue();
        }

        if (message.getContentRaw().startsWith("-join")) {
            VoiceChannel voiceChannel = (VoiceChannel) message.getMember().getVoiceState().getChannel();
            AudioManager audioManager = guild.getAudioManager();
            audioManager.openAudioConnection(voiceChannel);
        }

        if (message.getContentRaw().startsWith("-leave")) {
            AudioManager audioManager = guild.getAudioManager();
            audioManager.closeAudioConnection();
        }
    }

    private void connectTo(AudioChannel channel){
        Guild guild = channel.getGuild();
        AudioManager audioManager = guild.getAudioManager();
        AudioHandler handler = new AudioHandler();
        audioManager.setSendingHandler(handler);
        audioManager.setReceivingHandler(handler);
        audioManager.openAudioConnection(channel);
    }

    public class AudioHandler implements AudioReceiveHandler, AudioSendHandler {

        private final Queue<byte[]> queue = new ConcurrentLinkedQueue<>();

        @Override
        public boolean canReceiveCombined() {
            return queue.size() < 10;
        }

        @Override
        public void handleCombinedAudio(@NotNull CombinedAudio combinedAudio) {
            if (combinedAudio.getUsers().isEmpty()) {
                return;
            }
            byte[] data = combinedAudio.getAudioData(1.0f); // 1.0 sets volume to 100%
            queue.add(data);
        }

//        @Override
//        public boolean canReceiveUser() {
//            return AudioReceiveHandler.super.canReceiveUser();
//        }
//
//        @Override
//        public void handleUserAudio(@NotNull UserAudio userAudio) {
//            AudioReceiveHandler.super.handleUserAudio(userAudio);
//        }

        @Override
        public boolean canProvide() {
            return !queue.isEmpty();
        }

        @Nullable
        @Override
        public ByteBuffer provide20MsAudio() {
            byte[] data = queue.poll();
            return data == null ? null : ByteBuffer.wrap(data);
        }

        @Override
        public boolean isOpus() {
            return false;
        }
    }
}