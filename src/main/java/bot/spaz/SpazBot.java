package bot.spaz;

import javax.security.auth.login.LoginException;

public class DiscordBot {

    public static void main(String[] args) throws LoginException {

        String token = new Tokens().getBotToken();
        System.out.println(token);

//        JDA bot = JDABuilder.createDefault(token)
//                .setActivity(Activity.playing("with your mom"))
//                .build();
    }

}
