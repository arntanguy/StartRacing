package audio;

import java.util.HashMap;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;

public class VoiceSoundStore extends SoundStore<String> {
	protected static VoiceSoundStore instance;
	protected AssetManager assetManager;
	
	protected HashMap<String, AudioData> voices;

	public static VoiceSoundStore getInstance() {
		if (null == instance) { // First call
			instance = new VoiceSoundStore();
		}
		return instance;
	}
	
	protected VoiceSoundStore() {
		this.assetManager = null; 
		voices = new HashMap<String, AudioData>();
	}
	
	public void setAssetManager(AssetManager assetMgr) {
		this.assetManager = assetMgr;
	}
	

	public void addSound(String id, String path) throws Exception {
		if(assetManager != null) {
			voices.put(id, assetManager.loadAudio(path));
		} else {
			throw new Exception("Error in AbstractSoundStore : no asset manager defined !");
		}
	}
	
	public HashMap<String, AudioData> getSounds() {
		return this.voices;
	}
	
	public AudioData getSound(String key) {
		return voices.get(key);
	}
}
