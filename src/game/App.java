package game;

import xml.OptionXMLParser;
import xml.XMLFileStore;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.Nifty;

public class App extends SimpleApplication {

	private Nifty nifty;
	private NiftyJmeDisplay niftyDisplay;

	public App() {
		AppSettings set = new AppSettings(true);
		
		/* Options */
		OptionXMLParser.loadAppOptions(XMLFileStore.OPTION_SAVE_FILE);
		set.setResolution(OptionXMLParser.screenResolution.width, OptionXMLParser.screenResolution.height);
		set.setTitle(StringStore.APP_TITLE);
		this.setSettings(set);
		this.setShowSettings(false);
	}
	
	@Override
	public void simpleInitApp() {

		/* Lancement application */
		niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager,
				audioRenderer, guiViewPort);
		nifty = niftyDisplay.getNifty();

		/******* DEBUG ********/
		XMLFileStore.validateXMLFiles(nifty);
		nifty.setDebugOptionPanelColors(true);
		addXMLFiles();		
		/****** FIN DEBUG *****/

//		gotoStart();
		gotoOptions();
		//gotoCrtProfil();
		//gotoAffProfil();
		
		
		// disable the fly cam
		flyCam.setEnabled(false);

		// attach the nifty display to the gui view port as a processor
		guiViewPort.addProcessor(niftyDisplay);
		inputManager.setCursorVisible(false);
	}
	
	@Override
	public void stop() {
		super.stop();
		audioRenderer.cleanup();
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
		nifty.addXml("Interface/Nifty/StartScreen.xml");
		nifty.gotoScreen("start");
		StartScreenState startScreenController = (StartScreenState) nifty
				.getCurrentScreen().getScreenController();
		stateManager.attach(startScreenController);
	}

	public void gotoCrtProfil() {
		nifty.addXml("Interface/Nifty/CreateProfil.xml");
		nifty.gotoScreen("createprofil");
        CreateProfilScreen cpfscreen = (CreateProfilScreen) nifty.getCurrentScreen().getScreenController();
        stateManager.attach(cpfscreen);
	}
	
	public void gotoAffProfil() {
		nifty.addXml("Interface/Nifty/ChooseProfil.xml");
        nifty.gotoScreen("chooseprofil");
        ProfilsScreen pfscreen = (ProfilsScreen) nifty.getCurrentScreen().getScreenController();
        stateManager.attach(pfscreen);
	}
	
	public void gotoAchat() {
		nifty.addXml("Interface/Nifty/Achat.xml");
        nifty.gotoScreen("achat");
        Achat achat = (Achat) nifty.getCurrentScreen().getScreenController();
        stateManager.attach(achat);
	}
}
