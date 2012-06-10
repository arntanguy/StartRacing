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
		//System.err.println("INITIALIZE");
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
		voiceRender.stopVoice();
		voiceRender.close();
		stateManager.detach(this);
	}

	public void gotoscreen(String nextscreen) {
		nifty.gotoScreen(nextscreen);
	}

	public void quitGame() {
		System.out.println("quitGame");
		voiceRender.close();
		
		
		app.stop();
	}

	protected void initAudioVoices() {
		VoiceSoundStore s = VoiceSoundStore.getInstance();
		s.setAssetManager(app.getAssetManager());
		try {
			s.addSound("freeforall", "Sound/Vocal/course_libre.ogg");
			s.addSound("halfmile", "Sound/Vocal/course_demi.ogg");
			s.addSound("quartermile", "Sound/Vocal/course_quart.ogg");
			s.addSound("optionButton", "Sound/Vocal/option.ogg");
			s.addSound("newProfil", "Sound/Vocal/nouveau_profil.ogg");
			s.addSound("oldProfil", "Sound/Vocal/choisir_gerer_profil.ogg");
			s.addSound("quitButton", "Sound/Vocal/quitter.ogg");
			s.addSound("tutorielButton", "Sound/Vocal/tutoriel.ogg");
			
			s.addSound("applyButton", "Sound/Vocal/appliquer.ogg");
			s.addSound("returnButton", "Sound/Vocal/retour_main.ogg");
			s.addSound("valider", "Sound/Vocal/valider.ogg");
			s.addSound("retour", "Sound/Vocal/retour.ogg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		voiceRender = new VoiceRender(app.getGuiNode(), s);
	}
}