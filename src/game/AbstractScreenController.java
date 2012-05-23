package game;

import audio.SoundStore;
import audio.VoiceRender;
import audio.VoiceSoundStore;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * A basic ScreenState
 * 
 * @author TANGUY arnaud
 * 
 */
public class AbstractScreenController extends AbstractAppState implements
		ScreenController {

	protected Nifty nifty;
	protected Screen screen;
	protected App app;
	protected AppStateManager stateManager;

	protected VoiceRender voiceRender;

	public AbstractScreenController() {
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		System.err.println("INITIALIZE");
		this.stateManager = stateManager;
		this.app = (App) app;

		initAudioVoices();
	}

	@Override
	public void update(float tpf) {
	}

	@Override
	public void stateAttached(AppStateManager stateManager) {
	}

	@Override
	public void stateDetached(AppStateManager stateManager) {
	}

	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
	}

	public void onStartScreen() {
	}

	public void onEndScreen() {
		stateManager.detach(this);
	}

	public void gotoscreen(String nextscreen) {
		nifty.gotoScreen(nextscreen);
	}

	public void quitGame() {
		System.out.println("quitGame");
		app.stop();
	}

	protected void initAudioVoices() {
		VoiceSoundStore soundStore = VoiceSoundStore.getInstance();
		soundStore.setAssetManager(app.getAssetManager());
		try {
			soundStore.addSound("freeforall", "Sound/win.wav");
			soundStore.addSound("halfmile", "Sound/car_crash.wav");
			soundStore.addSound("quartermile", "Sound/lost.wav");
		} catch (Exception e) {
			e.printStackTrace();
		}
		voiceRender = new VoiceRender(app.getGuiNode(), soundStore);
	}
}