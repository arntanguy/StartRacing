package game;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

import de.lessvoid.nifty.Nifty;

public class App extends SimpleApplication {
	private AppStateManager appStateMgr;
	private AbstractAppState appState;
	
	
	@Override
	public void simpleInitApp() {
	
		// disable the fly cam
		flyCam.setEnabled(false);

		inputManager.setCursorVisible(true);
		
		appStateMgr = this.getStateManager();
		appState = new StartScreenState(this);
		appStateMgr.attach(appState);
		appState.setEnabled(true);
	}
	
	public void switchView(View view, Nifty nifty) {
		switch(view) {
		case START:
			appStateMgr.attach(appState);
			break;
		case GAME:
			appStateMgr.detach(appState);
			appState.setEnabled(false);
			appStateMgr.attach(new GameScreenState(this));
			break;
		} 
	}
}
