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
		
		XMLFileStore.validateXMLFiles(nifty);
		addXMLFiles();
		
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
	
	public void addXMLFiles() {
		nifty.addXml(XMLFileStore.GAME_SCREEN_FILE);
		nifty.addXml(XMLFileStore.OPTION_SCREEN_FILE);
		nifty.addXml(XMLFileStore.START_SCREEN_FILE);
	}
	
	public void gotoGame() {
        nifty.gotoScreen("hud");
        GameScreenState gameScreenController = (GameScreenState) nifty.getCurrentScreen().getScreenController();
        stateManager.attach(gameScreenController);
 
    }
 
	public void gotoOptions() {
        nifty.gotoScreen("options");
        GameScreenState gameScreenController = (GameScreenState) nifty.getCurrentScreen().getScreenController();
        stateManager.attach(gameScreenController);
	}
	
    public void gotoStart() {
        nifty.gotoScreen("start");
        StartScreenState startScreenController = (StartScreenState) nifty.getCurrentScreen().getScreenController();
        stateManager.attach(startScreenController);
    }
}
