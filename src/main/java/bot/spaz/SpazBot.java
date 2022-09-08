package bot.spaz;

import ignored.Token;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class SpazBot extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {

        String token = new Token().getToken();

        JDA bot = JDABuilder.createDefault(token)
                .setActivity(Activity.playing("with your mom"))
                .addEventListeners(new SpazBot())
                .build();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        System.out.println("testing");
        String messageSent = event.getMessage().getContentStripped();
        if (event.getChannel().toString().equals("spaz-bot-testing-ground")) {
            event.getChannel().sendMessage("You said: " + messageSent).queue();
        }
    }
}
