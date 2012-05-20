package audio;

import java.util.LinkedHashMap;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;

public class EngineSoundStore extends SoundStore {
	protected static EngineSoundStore instance;
	protected AssetManager assetManager;
	
	protected LinkedHashMap<Integer, AudioData> sounds;
	

	public static EngineSoundStore getInstance() {
		if (null == instance) { // Premier appel
			instance = new EngineSoundStore();
		}
		return instance;
	}
	
	protected EngineSoundStore() {
		this.assetManager = null; 
		sounds = new LinkedHashMap<Integer, AudioData>();
	}
	
	public void setAssetManager(AssetManager assetMgr) {
		this.assetManager = assetMgr;
	}
	

	public void addSound(Integer id, String path) throws Exception {
		if(assetManager != null) {
			sounds.put(id, assetManager.loadAudio(path));
		} else {
			throw new Exception("Error in AbstractSoundStore : no asset manager defined !");
		}
	}
	
	public LinkedHashMap<Integer, AudioData> getSounds() {
		return sounds;
	}
	
	public AudioData getSound(Integer key) {
		return sounds.get(key);
	}
}
