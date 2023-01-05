package bot.spaz.commands;

import bot.spaz.listeners.VoiceListener;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CmdJoin {

    VoiceChannel voiceChannel;

    public CmdJoin(Event event) {
        //
    }

    public void joinChannel(Event e){
        VoiceChannel userVoiceChannel = null;
        try {
            userVoiceChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
            if (userVoiceChannel == null) {
                event.getChannel().sendMessage("Unable to join, must be in a voice channel").queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
