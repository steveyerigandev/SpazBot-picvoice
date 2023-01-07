package bot.spaz.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CmdSkip extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }

        String[] message = event.getMessage().getContentRaw().split(" ");
        String song = "TEST SONG TITLE";
        if(message[0].equalsIgnoreCase("-skip")){
            event.getChannel().sendMessage("(TEST) Skipping **" + song + "**.").queue();
        }
    }
}
