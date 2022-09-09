package bot.spaz;

import ignored.Token;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import javax.security.auth.login.LoginException;

public class SpazBot {

    public static void main(String[] args) throws LoginException {

        String token = new Token().getToken();

        JDA bot = JDABuilder.createDefault(token)
                .setActivity(Activity.playing("with your mom"))
                .addEventListeners(new SpazBotListeners())
                .build();
    }
}
