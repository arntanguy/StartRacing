package audio;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

public class AudioRender {
	private LinkedHashMap<Integer, AudioNode> channels;
	private HashMap<String, AudioNode> extraChan;
	private AssetManager assetM;
	private Node rootNode;

	private AudioNode prevLow;
	private AudioNode prevHigh;

	public AudioRender(AssetManager asset, Node player) {
		channels = new LinkedHashMap<Integer, AudioNode>();
		extraChan = new HashMap<String, AudioNode>();

		this.rootNode = player;
		this.assetM = asset;
	}

	public void init(LinkedHashMap<Integer, String> soundPaths,
			HashMap<String, String> extraSound) {
		// Charger les sons
		Iterator<Integer> it = soundPaths.keySet().iterator();

		while (it.hasNext()) {
			int key = it.next();
			AudioNode sample = new AudioNode(assetM, soundPaths.get(key), false);
			sample.setLooping(true);
			sample.setPositional(true);
			sample.setVolume(0);
			sample.play();
			rootNode.attachChild(sample);
			channels.put(key, sample);
		}

		Iterator<String> itr = extraSound.keySet().iterator();

		while (itr.hasNext()) {
			String key = itr.next();
			AudioNode sample = new AudioNode(assetM, extraSound.get(key), false);
			rootNode.attachChild(sample);
			extraChan.put(key, sample);
		}
	}

	public void playStartSound() {
		AudioNode sample = extraChan.get("start");
		sample.setVolume(0.6f);
		sample.playInstance();
	}

	public void gearUp() {
		AudioNode sample = extraChan.get("up");
		sample.setVolume(1f);
		sample.playInstance();
	}

	public void playWin() {
		AudioNode sample = extraChan.get("win");
		sample.setVolume(1f);
		sample.playInstance();
	}

	public void playLost() {
		AudioNode sample = extraChan.get("lost");
		sample.setVolume(1f);
		sample.playInstance();
	}

	public void playStartBeepLow() {
		AudioNode sample = extraChan.get("start_low");
		sample.setVolume(1f);
		sample.playInstance();
	}

	public void playStartBeepHigh() {
		AudioNode sample = extraChan.get("start_high");
		sample.setVolume(1f);
		sample.playInstance();
	}

	public void playBurst() {
		AudioNode sample = extraChan.get("burst");
		sample.setVolume(1f);
		sample.playInstance();
	}

	public void mute() {
		prevHigh.setVolume(0);
		prevLow.setVolume(0);
	}

	public void setRPM(int rpm) {
		Iterator<Integer> it = channels.keySet().iterator();

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

		AudioNode low = channels.get(rpmLow);
		AudioNode high = channels.get(rpmHigh);

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
