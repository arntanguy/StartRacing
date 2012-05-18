package audio;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;

public class SoundStore {
	private static SoundStore instance;

	private AssetManager assetManager;
	private LinkedHashMap<Integer, AudioData> engineSounds;
	private HashMap<String, AudioData> extraSounds;
	private HashMap<String, AudioData> voiceSounds;

	public static SoundStore getInstance() {
		if (null == instance) { // Premier appel
			instance = new SoundStore();
		}
		return instance;
	}

	/**
	 * Redefine constructor as private to disable access
	 */
	private SoundStore() {
		this.assetManager = null;
		engineSounds = new LinkedHashMap<Integer, AudioData>();
		extraSounds = new HashMap<String, AudioData>();
		voiceSounds = new HashMap<String, AudioData>();
	}

	public void setAssetManager(AssetManager assetMgr) {
		this.assetManager = assetMgr;
		engineSounds = new LinkedHashMap<Integer, AudioData>();
	}

	public void addEngineSound(Integer key, String path) {
		engineSounds.put(key, assetManager.loadAudio(path));
	}

	public void addExtraSound(String key, String path) {
		extraSounds.put(key, assetManager.loadAudio(path));
	}

	public AudioData getEngineSound(int rpm) {
		return engineSounds.get(rpm);
	}

	public AudioData getExtraSound(String key) {
		return extraSounds.get(key);
	}

	public LinkedHashMap<Integer, AudioData> getEngineSounds() {
		return engineSounds;
	}

	public HashMap<String, AudioData> getExtraSounds() {
		return extraSounds;
	}
	
	
	public void addVoiceSound(String key, String path) {
		voiceSounds.put(key, assetManager.loadAudio(path));
	}
	
	public AudioData getVoiceSound(String key) {
		return voiceSounds.get(key);
	}
	
	public HashMap<String, AudioData> getVoiceSounds() {
		return voiceSounds;
	}
}
