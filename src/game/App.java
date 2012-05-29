package game;

import xml.OptionXMLParser;
import xml.XMLFileStore;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
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
		set.setFullscreen(OptionXMLParser.fullScreen);
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
//		nifty.setDebugOptionPanelColors(true);
		addXMLFiles();
		/****** FIN DEBUG *****/
		
		/* Keyboard Mappings */
		setInputMapping();

		gotoStart();
//		gotoOptions();
//		gotoCrtProfil();
//		gotoAffProfil();
		
		
		// disable the fly cam
		flyCam.setEnabled(false);

		// attach the nifty display to the gui view port as a processor
		guiViewPort.addProcessor(niftyDisplay);
		inputManager.setCursorVisible(false);
	}
	
	void setInputMapping() {
		inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
		inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_HIDE_STATS);
		
		inputManager.addMapping("quitGame", new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean keyPressed, float arg2) {
				System.out.println("Current Screen : " + nifty.getCurrentScreen().getScreenId());
				if (nifty.getCurrentScreen().getScreenId().equals("start") && keyPressed) {
					stop();
				} else {
					gotoStart();
				}
			}
		}, "quitGame");
		
		inputManager.addMapping("quickPlay", new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				gotoFreeForAll();
			}
		}, "quickPlay");
		
		inputManager.addMapping("halfGame", new KeyTrigger(KeyInput.KEY_H));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				gotoGame("half");
			}
		}, "halfGame");
		
		inputManager.addMapping("quarterGame", new KeyTrigger(KeyInput.KEY_H));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				gotoGame("");
			}
		}, "halfGame");
		
		inputManager.addMapping("options", new KeyTrigger(KeyInput.KEY_O));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				gotoOptions();
			}
		}, "options");
		
		inputManager.addMapping("createProfil", new KeyTrigger(KeyInput.KEY_C));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				gotoCrtProfil();
			}
		}, "createProfil");
		
		inputManager.addMapping("chooseProfil", new KeyTrigger(KeyInput.KEY_P));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				gotoAffProfil();
			}
		}, "chooseProfil");
		
		inputManager.addMapping("achat", new KeyTrigger(KeyInput.KEY_B));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				gotoAchat();
			}
		}, "achat");
		
		inputManager.addMapping("garage", new KeyTrigger(KeyInput.KEY_G));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				gotoGarage();
			}
		}, "garage");
	}
	
	@Override
	public void stop() {
		super.stop();
	}
	
	public void addXMLFiles() {
		nifty.addXml(XMLFileStore.START_SCREEN_FILE);
		nifty.addXml(XMLFileStore.OPTION_SCREEN_FILE);
		nifty.addXml(XMLFileStore.FREE_4_ALL_FILE);
		nifty.addXml(XMLFileStore.HALF_GAME_FILE);
		nifty.addXml(XMLFileStore.QUARTER_GAME_FILE);
		nifty.addXml(XMLFileStore.CREATE_PROFIL_FILE);
		nifty.addXml(XMLFileStore.CHOOSE_PROFIL_FILE);
		nifty.addXml(XMLFileStore.ACHAT_FILE);
		nifty.addXml(XMLFileStore.GARAGE_FILE);
	}
	
	public void gotoGame(String mode) {
		if(mode.equals("half")) {
			nifty.gotoScreen("halfScreen");
			AbstractGameScreenState gameScreenController = (AbstractGameScreenState) nifty
					.getScreen("halfScreen").getScreenController();
			stateManager.attach(gameScreenController);
		} else {
			nifty.gotoScreen("quarterScreen");
			AbstractGameScreenState gameScreenController = (AbstractGameScreenState) nifty
					.getScreen("quarterScreen").getScreenController();
			stateManager.attach(gameScreenController);
		}
	}

	public void gotoFreeForAll() {
		nifty.gotoScreen("freeForAllScreen");
		AbstractGameScreenState gameScreenController = (AbstractGameScreenState) nifty
				.getScreen("freeForAllScreen").getScreenController();
		stateManager.attach(gameScreenController);
	}
	
	public void gotoOptions() {
        nifty.gotoScreen("options");
        OptionScreenController gameScreenController = (OptionScreenController) nifty.getScreen("options").getScreenController();
        stateManager.attach(gameScreenController);
	}

	public void gotoStart() {
		nifty.gotoScreen("start");
		StartScreenState startScreenController = (StartScreenState) nifty.getScreen("start").getScreenController();
		//StartScreenState startScreenController = (StartScreenState) nifty
		//		.getCurrentScreen().getScreenController();
		stateManager.attach(startScreenController);
	}

	public void gotoCrtProfil() {
		nifty.gotoScreen("createprofil");
        CreateProfilScreen cpfscreen = (CreateProfilScreen) nifty.getScreen("createprofil").getScreenController();
        stateManager.attach(cpfscreen);
	}
	
	public void gotoAffProfil() {
        nifty.gotoScreen("chooseprofil");
        ProfilsScreen pfscreen = (ProfilsScreen) nifty.getScreen("chooseprofil").getScreenController();
        stateManager.attach(pfscreen);
	}
	
	public void gotoAchat() {
        nifty.gotoScreen("achat");
        Achat achat = (Achat) nifty.getScreen("achat").getScreenController();
        stateManager.attach(achat);
	}
	
	public void gotoGarage() {
        nifty.gotoScreen("garage");
        Garage garage = (Garage) nifty.getScreen("garage").getScreenController();
        stateManager.attach(garage);
	}
}
