package bot.spaz;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SpazTextListener extends ListenerAdapter {

    // Checks text channel activity and responds to specific commands
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

        if (message.getContentRaw().equalsIgnoreCase("-leave")) {
            AudioManager audioManager = guild.getAudioManager();
            audioManager.closeAudioConnection();
        }
        if (message.getContentRaw().equalsIgnoreCase("-join")) {
            SpazVoiceListener voiceListener = new SpazVoiceListener();
            voiceListener.run(event);
        }
        if (message.getContentRaw().equalsIgnoreCase("-play")) {
            // music player commands
        }
    }
}
