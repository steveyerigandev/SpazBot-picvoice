package bot.spaz;

import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;

public class VoiceFileSaver {

    HashMap<User, byte[]> usersAudioData = new HashMap<>();

    public VoiceFileSaver() {
    }

    public void updateStream(UserAudio userAudio) {
        byte[] newAudio = userAudio.getAudioData(1);
        User user = userAudio.getUser();
        newAudio = convertFromStereoToMono(newAudio);

        // If user key exists, the new byte[] is added to the existing byte[]
        if (usersAudioData.containsKey(user)) {
            byte[] existingAudioData = usersAudioData.get(user);
            usersAudioData.put(user, createMetaAudio(existingAudioData, newAudio));
        } else {

            // If user key does not exist, creates new hashmap with new user key and byte[]
            usersAudioData.put(userAudio.getUser(), userAudio.getAudioData(1));
        }
    }

    public byte[] convertFromStereoToMono(byte[] newAudio){
        //TODO method needs logic for converting stereo to mono, update return statement
        return newAudio;
    }

    public byte[] createMetaAudio(byte[] existingAudio, byte[] newAudio) {
        //TODO method needs logic for combining byte[]s, update return statement
        byte[] combinedArray = existingAudio;
        return combinedArray;
    }

    public void saveAudio(){
        //TODO method needs logic for saving or overwriting audio file, based on user ID, must be WAVE format
    }
}
