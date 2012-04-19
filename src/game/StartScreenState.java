package game;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.elements.Element;

public class StartScreenState extends AbstractScreenController {

	private InputManager inputManager;

	public StartScreenState() {
		super();
	}

	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);

		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);

		this.inputManager = app.getInputManager();
	}

	@Override
	public void update(float tpf) {
		/** any main loop action happens here */
	}

	@Override
	public void stateAttached(AppStateManager stateManager) {
	}

	@Override
	public void stateDetached(AppStateManager stateManager) {
	}

	@Override
	public void bind(Nifty nifty, Screen screen) {
		super.bind(nifty, screen);
	}

	@Override
	public void onEndScreen() {
		stateManager.detach(this);
	}

	@Override
	public void onStartScreen() {
	}

	public void startGame(String nextScreen) {
		app.gotoGame(nextScreen);
	}

	public void startFreeForAll() {
		System.out.println("START free for all");
		app.gotoFreeForAll();
	}

	public void showQuitPopup(String s) {
		Element pop = nifty.createPopup("exitPopup");
		nifty.showPopup(nifty.getCurrentScreen(), pop.getId(), null);
	}

	public void quitGame() {
		app.stop();
	}

	/**
	 * Permet d'interfacer avec le format XML.
	 * 
	 * @return Le titre de l'application
	 */
	public static String getTitle() {
		return StringStore.APP_TITLE;
	}

}