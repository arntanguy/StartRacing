package audio;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

/**
 * The audioRenderer creates sound nodes from audiodata, and then provide basic
 * playback functions such as play, stop
 * 
 * @author TANGUY Arnaud
 * 
 * @param <KeyType>
 *            The type of the key used to find the audio nodes.
 */
public class AudioRender<KeyType> {
	protected Node rootNode;
	protected LinkedHashMap<KeyType, AudioNode> soundNodes;

	public AudioRender(Node rootNode, SoundStore<KeyType> soundStore) {
		soundNodes = new LinkedHashMap<KeyType, AudioNode>();
		this.rootNode = rootNode;
		createSoundNodes(soundStore);
	}

	private void createSoundNodes(SoundStore<KeyType> soundStore) {
		HashMap<KeyType, AudioData> sounds = soundStore.getSounds();
		// Charger les sons
		Iterator<KeyType> it = sounds.keySet().iterator();

		while (it.hasNext()) {
			KeyType key = it.next();
			AudioNode sample = new AudioNode();
			sample.setAudioData(sounds.get(key), null);
			sample.setName(key.toString());
			sample.setLooping(true);
			sample.setPositional(true);
			sample.setVolume(0);
			sample.play();
			soundNodes.put(key, sample);
			rootNode.attachChild(sample);
		}
	}

	public void close() {
		Iterator<KeyType> it = soundNodes.keySet().iterator();
		AudioNode aud;
		while (it.hasNext()) {
			aud = soundNodes.get(it.next());
			aud.stop();
			aud.removeFromParent();
		}
	}

	public void setRootNode(Node node) {
		/**
		 * reassign all nodes to rootNode
		 */
		for (KeyType key : soundNodes.keySet()) {
			rootNode.detachChild(soundNodes.get(key));
			node.attachChild(soundNodes.get(key));
		}
		this.rootNode = node;
	}

	public void play(KeyType sound) {
		play(sound, 1.f);
	}

	public void play(KeyType sound, boolean looping) {
		play(sound);
		soundNodes.get(sound).setLooping(looping);
	}

	public void play(KeyType sound, float volume) {
		play(sound, volume, false);
	}

	public void play(KeyType sound, float volume, boolean looping) {
		//System.out.println("AudioRender :: play " + sound);
		AudioNode sample = soundNodes.get(sound);
		if(sample == null) {
			//System.out.println("The audio node "+sound+ " doesn't exist, abort playing!!");
			return;
		}
 		//System.out.println("Sound node : " + sample.getName());
		sample.setVolume(volume);
		sample.setLooping(looping);
		sample.play();
	}

	public void stop(KeyType sound) {
		System.out.println("AudioRender :: Stop sound " + sound);
		AudioNode soundNode = soundNodes.get(sound);
		soundNode.stop();
	}

	public void mute() {
		for (KeyType key : soundNodes.keySet()) {
			soundNodes.get(key).setVolume(0.f);
		}
	}
}
