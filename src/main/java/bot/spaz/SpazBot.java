package bot.spaz;

import edu.cmu.sphinx.api.Configuration;
import ignored.Token;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class SpazBot {

    public static void main(String[] args) throws LoginException {

        Configuration sphinxConfig = new Configuration();
        sphinxConfig.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        sphinxConfig.setDictionaryPath("src\\main\\resources\\3678.dic");
        sphinxConfig.setLanguageModelPath("src\\main\\resources\\3678.lm");

        JDABuilder spazBot =
                JDABuilder.createDefault(new Token().getToken());

        spazBot.setActivity(Activity.playing("the dangerous game of figuring out how to code a bot"))  // sets default bot activity
                .addEventListeners(new SpazBotListeners())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                .build();
    }
}