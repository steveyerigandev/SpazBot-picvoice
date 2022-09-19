package bot.spaz;

import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;

public class VoiceFileSaver {

    HashMap<User, byte[]> usersAudioData = new HashMap<>();

    public VoiceFileSaver() {
    }

    public void updateStream(UserAudio userAudio) {
        // If user key exists, the new byte[] is added to the existing byte[]
        if (usersAudioData.containsKey(userAudio.getUser())) {
            byte[] existingAudioData = usersAudioData.get(userAudio.getUser());
            //TODO insert method that combines byte[] data
            addNewByteArrayTo(existingAudioData, userAudio.getAudioData(1));
            usersAudioData.put(userAudio.getUser(), existingAudioData);
        } else {
            // If user key does not exist, creates new hashmap with new user key and byte[]
            usersAudioData.put(userAudio.getUser(), userAudio.getAudioData(1));
        }
    }

    public void addNewByteArrayTo(byte[] existingByteArray, byte[] newByteArray) {

    }
}
