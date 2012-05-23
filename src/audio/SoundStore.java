package audio;

import java.util.HashMap;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;

/**
 * A basic singleton to store audio data
 * @author TANGUY Arnaud
 *
 * @param <KeyType>
 */
public class SoundStore<KeyType> {
	protected static SoundStore instance;
	protected AssetManager assetManager;
	
	protected HashMap<KeyType, AudioData> sounds;
	

	public static SoundStore getInstance() {
		if (null == instance) { // Premier appel
			instance = new SoundStore();
		}
		return instance;
	}
	
	protected SoundStore() {
		this.assetManager = null; 
		sounds = new HashMap<KeyType, AudioData>();
	}
	
	public void setAssetManager(AssetManager assetMgr) {
		this.assetManager = assetMgr;
	}
	

	public void addSound(KeyType id, String path) throws Exception {
		if(assetManager != null) {
			sounds.put(id, assetManager.loadAudio(path));
		} else {
			throw new Exception("Error in AbstractSoundStore : no asset manager defined !");
		}
	}
	
	public HashMap<KeyType, AudioData> getSounds() {
		return sounds;
	}
	
	public AudioData getSound(KeyType key) {
		return sounds.get(key);
	}
}
