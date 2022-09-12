package bot.spaz;

import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SpazBotListeners extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.getAuthor().isBot()) {
            String botCommand = event.getMessage().getContentRaw().substring(0, 6).trim(); // Pulls first 6 characters from all chats
            if (botCommand.equals(".spaz")) {
                String messageSent = event.getMessage().getContentRaw().replace("!spaz", "").trim();
                event.getChannel().sendMessage("You said \"" + messageSent + "\"").queue();
            }
            botCommand = event.getMessage().getContentRaw().substring(0, 9).trim();
            if (botCommand.equals(".spaz")) {
                AudioChannel currentChannel = event.getMember().getVoiceState().getChannel();
                System.out.println(currentChannel);
            }
        }
    }
}