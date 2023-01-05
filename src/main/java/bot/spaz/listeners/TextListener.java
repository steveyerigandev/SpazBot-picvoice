package bot.spaz.listeners;

import bot.spaz.commands.CmdJoin;
import bot.spaz.commands.CmdLeave;
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
        VoiceChannel userVoiceChannel = null;
        TextChannel userTextChannel = event.getGuildChannel().asTextChannel();
        AudioManager audioManager = guild.getAudioManager();
        CmdJoin join = new CmdJoin();
        CmdLeave leave = new CmdLeave();


        // ENSURES THE COMMAND STARTS WITH '-' OR IT'S NOT FROM A BOT
        if (user.isBot() || !content.startsWith("-")) return;

        // ATTEMPTS TO GRAB A VOICE CHANNEL IF ANY
        try {
            userVoiceChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
        } catch (Exception e) {
            userTextChannel.sendMessage("Error I don't understand wtf is going on bro").queue();
        }

        // JUST TEST NONSENSE
        if (content.startsWith("-spaztest")) {
            event.getChannel().sendMessage("You said \"" + message.getContentRaw().replace("-spaztest", "").trim() + "\"").queue();
        }

        // LEAVE VOICE CHANNEL
        if (message.getContentRaw().equalsIgnoreCase("-leave")) {
            leave.leave(audioManager);
        }

        // JOIN VOICE CHANNEL
        if (message.getContentRaw().equalsIgnoreCase("-join")) {
            if(userVoiceChannel != null){
                join.setVoiceChannel(userVoiceChannel);
                join.joinChannel();
            } else {
                userTextChannel.sendMessage("Unable to join, user must be in a voice channel").queue();
            }
        }

        // WORK IN PROGRESS PLAY LAVAPLAYER
        if (message.getContentRaw().equalsIgnoreCase("-play")) {
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
