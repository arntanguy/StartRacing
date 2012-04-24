package audio;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

public class AudioRender {
	private LinkedHashMap<Integer, AudioNode> engineSoundNodes;
	private HashMap<String, AudioNode> extraSoundNodes;
	private Node rootNode;

	private AudioNode prevLow;
	private AudioNode prevHigh;

	private SoundStore soundStore;

	public AudioRender(Node player, SoundStore soundStore) {
		engineSoundNodes = new LinkedHashMap<Integer, AudioNode>();
		extraSoundNodes = new HashMap<String, AudioNode>();

		this.rootNode = player;
		init(soundStore);
	}

	private void init(SoundStore soundStore) {
		this.soundStore = soundStore;

		LinkedHashMap<Integer, AudioData> engineSounds = soundStore
				.getEngineSounds();
		// Charger les sons
		Iterator<Integer> it = engineSounds.keySet().iterator();

		while (it.hasNext()) {
			int key = it.next();
			AudioNode sample = new AudioNode();
			sample.setAudioData(engineSounds.get(key), null);
			sample.setLooping(true);
			sample.setPositional(true);
			sample.setVolume(0);
			sample.play();
			engineSoundNodes.put(key, sample);
			rootNode.attachChild(sample);
		}

		HashMap<String, AudioData> extraSounds = soundStore.getExtraSounds();
		Iterator<String> itr = extraSounds.keySet().iterator();

		while (itr.hasNext()) {
			String key = itr.next();
			AudioNode sample = new AudioNode();
			sample.setAudioData(extraSounds.get(key), null);
			extraSoundNodes.put(key, sample);
			rootNode.attachChild(sample);
		}
	}

	public void playStartSound() {
		AudioNode sample = extraSoundNodes.get("start");
		sample.setVolume(0.6f);
		sample.playInstance();
	}

	public void gearUp() {
		AudioNode sample = extraSoundNodes.get("up");
		sample.setVolume(1f);
		sample.playInstance();
	}

	public void playWin() {
		AudioNode sample = extraSoundNodes.get("win");
		sample.setVolume(1f);
		sample.playInstance();
	}

	public void playLost() {
		AudioNode sample = extraSoundNodes.get("lost");
		sample.setVolume(1f);
		sample.playInstance();
	}

	public void playStartBeepLow() {
		AudioNode sample = extraSoundNodes.get("start_low");
		sample.setVolume(1f);
		sample.playInstance();
	}

	public void playStartBeepHigh() {
		AudioNode sample = extraSoundNodes.get("start_high");
		sample.setVolume(1f);
		sample.playInstance();
	}

	public void playBurst() {
		AudioNode sample = extraSoundNodes.get("burst");
		sample.setVolume(1f);
		sample.playInstance();
	}

	public void mute() {
		prevHigh.setVolume(0);
		prevLow.setVolume(0);
	}

	public void setRPM(int rpm) {
		Iterator<Integer> it = engineSoundNodes.keySet().iterator();

		int rpmLow = 0;
		int rpmHigh = 0;

		int rpmTmp = 0;
		while (it.hasNext() && (rpm - rpmHigh >= 0)) {
			rpmTmp = rpmHigh;
			rpmHigh = it.next();
		}
		rpmLow = rpmTmp;

		float denum = (float) (rpmHigh - rpmLow);

		float volumLow = (float) (rpm - rpmLow) / denum;
		float volumHigh = (float) (rpmHigh - rpm) / denum;

		AudioNode low = engineSoundNodes.get(rpmLow);
		AudioNode high = engineSoundNodes.get(rpmHigh);

		// Muter le son précédent
		if (prevLow != null && prevLow != low) {
			prevLow.setVolume(0);
		}
		if (prevHigh != null && prevHigh != high) {
			prevHigh.setVolume(0);
		}

		// float pourcentLow = ((float)rpm/(float)rpmLow);
		// float pourcentHigh = ((float)rpm/(float)rpmHigh);

		float pourcentLow = (((float) rpm - rpmLow) / (float) (rpmHigh - rpmLow));
		float pourcentHigh = ((((float) rpmHigh - rpm) / (float) (rpmHigh - rpmLow))) / 2.f;

		try {
			low.setPitch(pourcentLow + 1.f);
		} catch (Exception e1) {
			System.err.println("val du pich bas = " + pourcentLow);
		}

		float vol = (volumHigh) * 2.f;
		low.setVolume((vol > 0) ? vol : 0.f);

		try {
			high.setPitch(1 - pourcentHigh);
		} catch (Exception e) {
			System.err.println("valu du pich haut = " + pourcentHigh);
		}
		high.setVolume(volumLow * 2.f);

		prevLow = low;
		prevHigh = high;
	}
}
