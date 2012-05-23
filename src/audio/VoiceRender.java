package audio;

import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

/**
 * Play the sounds for the vocal help.
 * It plays only one sound at a time, preventing sounds to overlap
 * @author TANGUY Arnaud
 *
 */
public class VoiceRender extends AudioRender<String> {

	private AudioNode playedNode;

	public VoiceRender(Node rootNode, SoundStore<String> soundStore) {
		super(rootNode, soundStore);
		playedNode = null;
	}
	/**
	 * Plays the voice corresponding to the given id, only if it is not already playing.
	 * If it is a different id than the one being played, stop the current playing, and play the new one.
	 * @param sound
	 * 		The string id of the sound
	 */
	public void playVoice(String sound) {
		System.out.println("play " + sound);
		if (playedNode == null) {
			playedNode = soundNodes.get(sound);
			play(sound, false);
			System.out.println(playedNode.getStatus());
		} else {
			if (!playedNode.getName().equals(sound)) {
				if(playedNode.getStatus() == AudioNode.Status.Playing) {
					System.out.println("Sound "+playedNode.getName()+" is playing, stopping it...");
					stopVoice();
				}
				System.out.println("Playing " + sound);
				playedNode = soundNodes.get(sound);
				play(sound, false);
			} else {
				System.out.println("Same name, do nothing");
			}
		}
	}
	/**
	 * Stops the current playing track
	 */
	public void stopVoice() {
		System.out.println("VoiceRender :: Stop voice "+playedNode.getName());
		playedNode.stop();
	}

	/**
	 * Stops the current playing track, and reset the lastPlayed node to null.
	 */
	public void stopAndReset() {
		stopVoice();
		playedNode = null;
	}
}
