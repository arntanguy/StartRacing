package audio;

import java.util.Iterator;
import java.util.LinkedHashMap;

import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

public class AudioRender<KeyType> {
	protected Node rootNode;
	protected LinkedHashMap<KeyType, AudioNode> soundNodes;

	public AudioRender(Node rootNode, SoundStore soundStore) {
		soundNodes = new LinkedHashMap<KeyType, AudioNode>();
		this.rootNode = rootNode;
		createSoundNodes(soundStore);
	}

	private void createSoundNodes(SoundStore soundStore) {
		LinkedHashMap<KeyType, AudioData> sounds = soundStore.getSounds();
		// Charger les sons
		Iterator<KeyType> it = sounds.keySet().iterator();

		while (it.hasNext()) {
			KeyType key = it.next();
			AudioNode sample = new AudioNode();
			sample.setAudioData(sounds.get(key), null);
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
		AudioNode sample = soundNodes.get(sound);
		sample.setVolume(volume);
		sample.setLooping(looping);
		sample.playInstance();
	}

	public void stop(KeyType sound) {
		AudioNode soundNode = soundNodes.get(sound);
		soundNode.stop();
	}

	public void mute() {
		for (KeyType key : soundNodes.keySet()) {
			soundNodes.get(key).setVolume(0.f);
		}
	}
}
