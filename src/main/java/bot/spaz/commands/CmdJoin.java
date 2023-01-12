package bot.spaz.commands;

import ai.picovoice.porcupine.Porcupine;
import ai.picovoice.porcupine.PorcupineException;
import bot.spaz.listeners.WakeUpWordListener;
import ignored.PicoToken;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

public class CmdJoin extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }

        TextChannel textChannel = event.getChannel().asTextChannel();
        VoiceChannel userVoiceChannel;
        String[] message = event.getMessage().getContentRaw().split(" ");

        if (message[0].equalsIgnoreCase("-join")) {
            try {
                userVoiceChannel = (VoiceChannel) event.getMessage().getMember().getVoiceState().getChannel();
                if (userVoiceChannel == null) {
                    event.getChannel().sendMessage("You must be in a voice channel to use commands.").queue();
                    return;
                }
                if (userVoiceChannel.getGuild().getSelfMember().getVoiceState().inAudioChannel()) {
                    event.getChannel().sendMessage("I'm already in a different channel BRUH.").queue();
                    return;
                }
                userVoiceChannel.getGuild().getAudioManager().openAudioConnection(userVoiceChannel);
                WakeUpWordListener spazListener = new WakeUpWordListener(userVoiceChannel, textChannel);
                spazListener.Run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
