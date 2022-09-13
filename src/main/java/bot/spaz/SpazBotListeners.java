package bot.spaz;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class SpazBotListeners extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.getMessage().getContentRaw().startsWith(".spaz")) return;

        Message message = event.getMessage();
        Guild guild = event.getGuild();

        if (message.getContentRaw().startsWith(".spaztest")) {
            event.getChannel().sendMessage("You said \"" + message.getContentRaw().replace(".spaztest", "").trim() + "\"").queue();
        }

        if (message.getContentRaw().startsWith(".join")) {
            VoiceChannel voiceChannel = (VoiceChannel) message.getMember().getVoiceState().getChannel();
            AudioManager audioManager = guild.getAudioManager();
            audioManager.openAudioConnection(voiceChannel);
        }

        if (message.getContentRaw().startsWith(".leave")) {
            AudioManager audioManager = guild.getAudioManager();
            audioManager.closeAudioConnection();
        }

        if(message.getContentRaw().startsWith(".tts")){
            TTS tts = new TTS(message.getContentRaw().replace(".tts", "").trim());
        }
    }
}