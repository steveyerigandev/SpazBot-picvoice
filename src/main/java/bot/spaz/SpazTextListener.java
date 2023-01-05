package bot.spaz;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SpazTextListener extends ListenerAdapter {

    // Checks text channel activity and responds to specific commands
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        Guild guild = event.getGuild();
        Message message = event.getMessage();
        User user = message.getAuthor();
        String content = message.getContentRaw();

        if (user.isBot()) return;

        if (!content.startsWith("-")) return;

        if (content.startsWith("-spaztest")) {
            event.getChannel().sendMessage("You said \"" + message.getContentRaw().replace("-spaztest", "").trim() + "\"").queue();
        }

        if (message.getContentRaw().equalsIgnoreCase("-leave")) {
            AudioManager audioManager = guild.getAudioManager();
            audioManager.closeAudioConnection();
        }
        if (message.getContentRaw().equalsIgnoreCase("-join")) {
            SpazVoiceListener voiceListener = new SpazVoiceListener();
            voiceListener.run(event);
        }
        if (message.getContentRaw().equalsIgnoreCase("-play")) {
            final int BUFFER_SIZE = 128000;
            File soundFile = null;
            AudioInputStream audioStream = null;
            AudioFormat audioFormat;
            SourceDataLine sourceLine = null;
            String fileName = "/Users/chasemartinez/IdeaProjects/java-discord-bot/src/main/resources/soundclips/BRUH.mp3";
            try {
                try {
                    soundFile = new File(fileName);
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Error creating soundFile");
                    System.exit(1);
                }

                try {
                    audioStream = AudioSystem.getAudioInputStream(soundFile);
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Error creating audioStream");
                    System.exit(1);
                }

                audioFormat = audioStream.getFormat();

                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                try {
                    sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                    sourceLine.open();
                } catch (LineUnavailableException e){
                    e.printStackTrace();
                    System.out.println("Error with sourceLine creation - Line Unavailable Exception");
                    System.exit(1);
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Error with sourceLine creation - general error");
                    System.exit(1);
                }

                sourceLine.start();

                int nBytesRead = 0;
                byte[] abData = new byte[BUFFER_SIZE];
                while (nBytesRead != -1) {
                    try {
                        nBytesRead = audioStream.read(abData, 0, abData.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (nBytesRead >= 0) {
                        @SuppressWarnings("unused")
                        int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                    }
                }

                sourceLine.drain();
                sourceLine.close();

            } catch (Exception e) {
                System.out.println("Error playing sound clip (text listener class)");
            }
        }
    }
}
