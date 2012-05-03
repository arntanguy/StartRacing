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
		inputManager.setCursorVisible(false);
	}
	
	public void addXMLFiles() {
		nifty.addXml(XMLFileStore.START_SCREEN_FILE);
		nifty.addXml(XMLFileStore.OPTION_SCREEN_FILE);
		nifty.addXml(XMLFileStore.GAME_SCREEN_FILE);
	}
	
	public void gotoGame(String mode) {
		
		if(mode.equals("half")) {
			nifty.addXml("Interface/Nifty/HalfGameScreen.xml");
			nifty.gotoScreen("hud");

			AbstractGameScreenState gameScreenController = (AbstractGameScreenState) nifty
					.getCurrentScreen().getScreenController();
			stateManager.attach(gameScreenController);
		} else {
			nifty.addXml("Interface/Nifty/QuarterGameScreen.xml");
			nifty.gotoScreen("hud");

			AbstractGameScreenState gameScreenController = (AbstractGameScreenState) nifty
					.getCurrentScreen().getScreenController();
			stateManager.attach(gameScreenController);
		}

	}

	public void gotoFreeForAll() {
		nifty.addXml("Interface/Nifty/FreeForAllScreen.xml");
		nifty.gotoScreen("hud");
		AbstractGameScreenState gameScreenController = (AbstractGameScreenState) nifty
				.getCurrentScreen().getScreenController();
		stateManager.attach(gameScreenController);
	}
	
	public void gotoOptions() {
        nifty.gotoScreen("options");
        OptionScreenState gameScreenController = (OptionScreenState) nifty.getCurrentScreen().getScreenController();
        stateManager.attach(gameScreenController);
	}

	public void gotoStart() {
		// nifty.addXml("Interface/Nifty/DevTest.xml");
		nifty.addXml("Interface/Nifty/StartScreen.xml");
		nifty.gotoScreen("start");
		StartScreenState startScreenController = (StartScreenState) nifty
				.getCurrentScreen().getScreenController();
		stateManager.attach(startScreenController);
	}

	public void gotoCrtProfil() {
		nifty.addXml("Interface/Nifty/CreateProfil.xml");
		nifty.gotoScreen("createprofil");
		
        //stateManager.attach(gameScreenController);
	}
	
	public void gotoAffProfil() {
		
	}
	
	public boolean validateXML() {
		try {
			nifty.validateXml("Interface/Nifty/StartScreen.xml");
			// nifty.validateXml("Interface/Nifty/GameScreen.xml");
			// nifty.validateXml("Interface/Nifty/DevTest.xml");
			return true;
		} catch (Exception e) {
			System.out.println("XML Exception: " + e.getMessage());
			return false;
		}
	}

}
