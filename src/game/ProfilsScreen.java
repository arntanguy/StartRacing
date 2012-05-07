package game;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;

public class ProfilsScreen extends AbstractScreenController {
	
	public ProfilsScreen() {
		super();
	}

	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		/*super.initialize(stateManager, a);
		
		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);
	
		this.inputManager = app.getInputManager();*/
	}
	
	public void gotoMainMenu() {
		app.gotoStart();
	}
}
