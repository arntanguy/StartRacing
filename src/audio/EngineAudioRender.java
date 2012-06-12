package audio;

import java.util.Iterator;

import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

public class EngineAudioRender extends AudioRender<Integer> {

	private AudioNode prevLow;
	private AudioNode prevHigh;

	public EngineAudioRender(Node rootNode, EngineSoundStore soundStore) {
		super(rootNode, soundStore, true);

	}

	public void mute() {
		try {
			for (Integer id : soundNodes.keySet()) {
				soundNodes.get(id).setVolume(0f);
			}

			System.out.println("mute engine");
			if (prevHigh != null) {
				prevHigh.setVolume(0);
			}

			if (prevLow != null) {
				prevLow.setVolume(0);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public void setRPM(int rpm) {
		Iterator<Integer> it = soundNodes.keySet().iterator();

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

		AudioNode low = soundNodes.get(rpmLow);
		AudioNode high = soundNodes.get(rpmHigh);
		low.setLooping(true);
		high.setLooping(true);

		// Muter le son précédent
		if (prevLow != null && prevLow != low) {
			prevLow.setVolume(0);
		}
		if (prevHigh != null && prevHigh != high) {
			prevHigh.setVolume(0);
		}

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
	}
}
