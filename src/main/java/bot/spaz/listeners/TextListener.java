package bot.spaz.listeners;

import bot.spaz.commands.CmdJoin;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class TextListener extends ListenerAdapter {

    // Checks text channel activity and responds to specific commands
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        Guild guild = event.getGuild();
        Message message = event.getMessage();
        User user = message.getAuthor();
        String content = message.getContentRaw();

        if (user.isBot() || !content.startsWith("-")) return;

        if (content.startsWith("-spaztest")) {
            event.getChannel().sendMessage("You said \"" + message.getContentRaw().replace("-spaztest", "").trim() + "\"").queue();
        }

        if (message.getContentRaw().equalsIgnoreCase("-leave")) {
            AudioManager audioManager = guild.getAudioManager();
            audioManager.closeAudioConnection();
        }

        if (message.getContentRaw().equalsIgnoreCase("-join")) {
//            CmdJoin join = new CmdJoin(event);
//            join.joinChannel();
            VoiceChannel userVoiceChannel;
            try {
                userVoiceChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
                if (userVoiceChannel == null) {
                    event.getChannel().sendMessage("Unable to join, must be in a voice channel").queue();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            VoiceListener voiceListener = new VoiceListener();
            voiceListener.run(event);
        }

        if (message.getContentRaw().equalsIgnoreCase("-play")) {
            VoiceChannel userVoiceChannel;
            String songRequest = message.getContentRaw().substring(5);
            try {
                userVoiceChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
                if (userVoiceChannel == null) {
                    event.getChannel().sendMessage("Unable to queue song, must be in voice channel").queue();
                } else if (songRequest.trim().equals("")) {
                    event.getChannel().sendMessage("Empty request.").queue();
                } else {
                    event.getChannel().sendMessage("Adding " + songRequest + "------ music player works here").queue();
                    // play music
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            VoiceListener voiceListener = new VoiceListener();
            voiceListener.run(event);
        }
    }
}
