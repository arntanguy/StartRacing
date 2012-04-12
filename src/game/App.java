package game;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;

import de.lessvoid.nifty.Nifty;

public class App extends SimpleApplication {
	
	private Nifty nifty;
	private NiftyJmeDisplay niftyDisplay;
	
	@Override
	public void simpleInitApp() {

		niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager,
				audioRenderer, guiViewPort);
		nifty = niftyDisplay.getNifty();
		nifty.addXml("Interface/Nifty/StartScreen.xml");
		gotoStart();
		
		// disable the fly cam
		flyCam.setEnabled(false);
		 	
    	// attach the nifty display to the gui view port as a processor
		guiViewPort.addProcessor(niftyDisplay);
		inputManager.setCursorVisible(true);
	}
	
	public void gotoGame() {
        nifty.gotoScreen("hud");
        GameScreenState gameScreenController = (GameScreenState) nifty.getCurrentScreen().getScreenController();
        stateManager.attach(gameScreenController);
 
    }
 
    public void gotoStart() {
        nifty.gotoScreen("start");
        StartScreenState startScreenController = (StartScreenState) nifty.getCurrentScreen().getScreenController();
        stateManager.attach(startScreenController);
    }
}
