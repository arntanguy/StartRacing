package game;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.Nifty;

public class App extends SimpleApplication {
	
	private Nifty nifty;
	private NiftyJmeDisplay niftyDisplay;
		
	@Override
	public void simpleInitApp() {
		
		AppSettings set = new AppSettings(true);
		
		niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager,
				audioRenderer, guiViewPort);
		nifty = niftyDisplay.getNifty();
		
		validateXML();
		
		set.setHeight(760);
		set.setWidth(1024);
		set.setTitle(StringStore.APP_TITLE);
		this.setSettings(set);
        
		gotoStart();

		// disable the fly cam
		flyCam.setEnabled(false);
		 	
    	// attach the nifty display to the gui view port as a processor
		guiViewPort.addProcessor(niftyDisplay);
		inputManager.setCursorVisible(true);
	}
	
	public void gotoGame() {
		nifty.addXml("Interface/Nifty/GameScreen.xml");
        nifty.gotoScreen("hud");
        GameScreenState gameScreenController = (GameScreenState) nifty.getCurrentScreen().getScreenController();
        stateManager.attach(gameScreenController);
 
    }
 
    public void gotoStart() {
//    	nifty.addXml("Interface/Nifty/DevTest.xml");
    	nifty.addXml("Interface/Nifty/StartScreen.xml");
        nifty.gotoScreen("start");
        StartScreenState startScreenController = (StartScreenState) nifty.getCurrentScreen().getScreenController();
        stateManager.attach(startScreenController);
    }
    
    public boolean validateXML() {
    	try {
    		nifty.validateXml("Interface/Nifty/StartScreen.xml");
//    		nifty.validateXml("Interface/Nifty/GameScreen.xml");
//    		nifty.validateXml("Interface/Nifty/DevTest.xml");
    		return true;
    	} catch(Exception e) {
    		System.out.println("XML Exception: " + e.getMessage());
    		return false;
    	}
    }
}
