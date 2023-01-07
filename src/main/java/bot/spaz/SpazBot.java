package bot.spaz;

import bot.spaz.commands.CmdJoin;
import bot.spaz.commands.CmdLeave;
import bot.spaz.commands.CmdPlay;
import ignored.Token;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.requests.GatewayIntent;
import javax.security.auth.login.LoginException;

public class SpazBot extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {

        // Create bot
        JDABuilder.createDefault(new Token().getToken())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                .setActivity(Activity.playing("the dangerous game of figuring out how to code a bot"))  // sets default bot activity
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .enableCache(CacheFlag.VOICE_STATE)
                .addEventListeners(new CmdJoin())
                .addEventListeners(new CmdLeave())
                .addEventListeners(new CmdPlay())
                .build();
    }
}