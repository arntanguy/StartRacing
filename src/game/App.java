package game;

import java.awt.Toolkit;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;

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
		
		/* ******* DEBUG ******* */
		if (!validateXML()) {
			this.destroy();
		}
		
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
    
    public boolean validateXML() {
    	try {
    		nifty.validateXml("Interface/Nifty/StartScreen.xml");
    		
    		return true;
    	} catch(Exception e) {
    		System.out.println("XML Exception: " + e.getMessage());
    		return false;
    	}
    }
}
