package game;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;

import de.lessvoid.nifty.Nifty;

public class App extends SimpleApplication {
	private AppStateManager appStateMgr;
	private AbstractAppState appState;
	
	private Nifty nifty;
	private NiftyJmeDisplay niftyDisplay;

	@Override
	public void simpleInitApp() {
		niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager,
				audioRenderer, guiViewPort);
		nifty = niftyDisplay.getNifty();
		// nifty.fromXml("Interface/Nifty/HelloJme.xml", "start", this);
		// nifty.fromXml("Interface/Nifty/StartScreen.xml", "start", this);

		// attach the nifty display to the gui view port as a processor
		guiViewPort.addProcessor(niftyDisplay);

		// disable the fly cam
		// flyCam.setEnabled(false);
		// flyCam.setDragToRotate(true);
		inputManager.setCursorVisible(true);
		
		appStateMgr = this.getStateManager();
		appState = new StartScreenState(this);
		appStateMgr.attach(appState);
		//appStateMgr.attach(new GameScreenState(this));

		appState.setEnabled(true);
	}
	
	public void switchView(String view) {
		if(view.equals("game")) {
			nifty.gotoScreen("hud");
		} else {
			nifty.gotoScreen("start");
		}
	}
}
