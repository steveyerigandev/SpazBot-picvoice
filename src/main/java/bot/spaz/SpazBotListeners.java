package bot.spaz;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceRequestToSpeakEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

public class SpazBotListeners extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.getMessage().getContentRaw().startsWith("-")) return;

        Message message = event.getMessage();
        Guild guild = event.getGuild();

        if (message.getContentRaw().startsWith("-spaztest")) {
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

    @Override
    public void onGuildVoiceRequestToSpeak(@NotNull GuildVoiceRequestToSpeakEvent event) {
        // How to implement voice activity, speech too incorporate it with Sphinx
    }
}