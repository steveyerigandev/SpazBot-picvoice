package bot.spaz;

import ignored.Token;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class SpazBot {

    public static void main(String[] args) throws LoginException {

        JDABuilder spazBot =
                JDABuilder.createDefault(new Token().getToken());
        spazBot.setActivity(Activity.playing("the dangerous game of figuring out how to code a bot"))  // sets default bot activity
                .addEventListeners(new SpazBotListeners())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
    }
}