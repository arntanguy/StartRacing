package audio;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

public class audioRender {
	private LinkedHashMap<Integer, AudioNode> channels;
	private HashMap<String, AudioNode> extraChan;
	private AssetManager assetM;
	private Node rootNode;
	
	private AudioNode prevLow;
	private AudioNode prevHigh;
	
	public audioRender(AssetManager asset, Node player) {
		channels = new LinkedHashMap<Integer, AudioNode>();
		extraChan = new HashMap<String, AudioNode>();
		
		this.rootNode = player;
		this.assetM = asset;
	}
	
	public void init(LinkedHashMap<Integer, String>	soundPaths, HashMap<String, String>	extraSound)	{
		// Charger les sons
		Iterator<Integer> it = soundPaths.keySet().iterator();
		
		while (it.hasNext())	{
			int key = it.next();
			AudioNode sample = new AudioNode(assetM, soundPaths.get(key), false);
			sample.setLooping(true);
			sample.setVolume(0);
			sample.play();
			rootNode.attachChild(sample);
			channels.put(key, sample);
		}
		
		Iterator<String> itr = extraSound.keySet().iterator();
		
		while (itr.hasNext())	{
			String key = itr.next();
			AudioNode sample = new AudioNode(assetM, extraSound.get(key), false);
			rootNode.attachChild(sample);
			extraChan.put(key, sample);
		}
	}
	
	public void playStartSound()	{
		AudioNode sample = extraChan.get("start");
		sample.playInstance();
	}
	
	public void gearUp()	{
		AudioNode sample = extraChan.get("up");
		sample.playInstance();
	}
	
	public void setRPM(int rpm)	{
		
		Iterator<Integer> it = channels.keySet().iterator();
		
		int rpmLow = 0;
		int rpmHigh = 0;
		
		int rpmTmp = 0;
		while (it.hasNext() && rpm - rpmHigh >= 0) {
			rpmTmp = rpmHigh;
			rpmHigh = it.next();
		}
		rpmLow = rpmHigh;
		rpmHigh = rpmTmp;
		
		float num = (rpm - rpmLow);
		float pourcentLow = num / (float)(rpmHigh - rpmLow);
		System.out.println(pourcentLow);
		
		AudioNode low = channels.get(rpmLow);
		AudioNode high = channels.get(rpmHigh);
		
		// Muter le son précédent
		if (prevLow != null)	{
			prevLow.setVolume(0);
		}
		if (prevHigh != null)	{
			prevHigh.setVolume(0);
		}
		
		low.setPitch(1.f + pourcentLow);
		low.setVolume(1 - pourcentLow);
		
		high.setPitch(1.f - pourcentLow/2);
		high.setVolume(1 - pourcentLow);
		
		prevLow = low;
		prevHigh = high;
	}
}
