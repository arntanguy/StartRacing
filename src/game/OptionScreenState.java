package game;

import java.awt.Dimension;
import java.util.Comparator;
import java.util.TreeMap;

import xml.OptionXMLParser;
import xml.XMLFileStore;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Label;

public class OptionScreenState extends AbstractScreenController {

	private InputManager inputManager;
	private TreeMap<String, Dimension> normalResolutions;
	private TreeMap<String, Dimension> wideResolutions;
	private DropDown<String> resolutionDropDown;			/* Menu déroulant des résolutions */
	private CheckBox soundCheckbox;
	private CheckBox ratioCheckbox;
	
	private final String RESOLUTION_DROP_ID = "resolutionDropDown";
	private final String RATIO_CHECKBOX_ID = "wideScreen";
	private final String SOUND_CHECKBOX_ID = "activateSound";
	private final String ON_SAVE_ID = "saveMessage";

	/* Comparateur personnalisé pour la liste */
	private Comparator<String> myComp = new Comparator<String>() {
		public int compare(String arg0, String arg1) {
			if (arg0.length() == arg1.length()) {
				return arg0.compareTo(arg1);
			} else {
				return (arg0.length() > arg1.length()) ? 1 : -1;
			}
		}
	};
	
	public OptionScreenState() {
		super();
		
		/* Résolutions */
		normalResolutions = new TreeMap<String, Dimension>(myComp);
		wideResolutions = new TreeMap<String, Dimension>(myComp);
		normalResolutions.put("800 x 600", new Dimension(800, 600));
		normalResolutions.put("1024 x 768", new Dimension(1024, 768));
		normalResolutions.put("1600 x 1200", new Dimension(1600, 1200));
		
		wideResolutions.put("1280 x 720", new Dimension(1280, 720));
		wideResolutions.put("1600 x 900", new Dimension(1600, 900));
		wideResolutions.put("1920 x 1080", new Dimension(1920, 1080));
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);
		
		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);
	
		this.inputManager = app.getInputManager();
		
		/* Objet nifty */
		resolutionDropDown = screen.findNiftyControl(RESOLUTION_DROP_ID, DropDown.class);
		soundCheckbox = screen.findNiftyControl(SOUND_CHECKBOX_ID, CheckBox.class);
		ratioCheckbox = screen.findNiftyControl(RATIO_CHECKBOX_ID, CheckBox.class);
		
		before();
	}
	
	/**
	 * Modifie l'interface graphique pour convenir aux options
	 */
	private void before() {
		soundCheckbox.setChecked(OptionXMLParser.sound);
		ratioCheckbox.setChecked(OptionXMLParser.wideScreen);
		resolutionDropDown.clear();
		if (OptionXMLParser.wideScreen) {
			fillResolutionDropDown(wideResolutions);
		} else {
			fillResolutionDropDown(normalResolutions);
		}
		resolutionDropDown.selectItem(OptionXMLParser.screenResolution.width + " x " + OptionXMLParser.screenResolution.height);
		System.out.println("OK on commence avec " + OptionXMLParser.screenResolution.width + " x " + OptionXMLParser.screenResolution.height);
	}
	
	/**
	 * Ajouter les différentes résolution au menu déroulant.
	 * @author Alexandre GILLE
	 */
	private void fillResolutionDropDown(TreeMap<String, Dimension> hash) {
		for (String r : hash.keySet()) {
			resolutionDropDown.addItem(r);
		}
	}
	
	/**
	 * Fonction callBack appelée lorsque le ratio est changé.
	 * @param id
	 * @param event
	 * @author Alexandre GILLE
	 */
	@NiftyEventSubscriber(id=RATIO_CHECKBOX_ID)
	public void onRatioChange(final String id, final CheckBoxStateChangedEvent event) {
		resolutionDropDown.clear();
		if (event.isChecked()) {
			fillResolutionDropDown(wideResolutions);
		} else {
			fillResolutionDropDown(normalResolutions);
		}
	}
	
	/**
	 * Retourne la résolution de l'écran qui est utilisée
	 * @return	Dimension équivalent à la résolution
	 */
	public Dimension getCurrentScreenResolution() {
		System.out.println("La résolution change (" + resolutionDropDown.getSelection() + ")");
		if (ratioCheckbox.isChecked()) {
			return wideResolutions.get(resolutionDropDown.getSelection());
		} else {
			return normalResolutions.get(resolutionDropDown.getSelection());
		}
	}
	
	@Override
	public void onEndScreen() {
		stateManager.detach(this);
	}
	
	public void gotoMainMenu() {
		app.gotoStart();
	}
	
	/**
	 * Sauvegarde les options contenues dans les variables tampons dans le fichier de sauvegarde
	 * @author Alexandre GILLE
	 * Sauvegarde les options contenues dans les variables tampons puis dans le fichier de sauvegarde.
	 */
	public void applyOptions() {
		System.out.println("Option saved");
		AppSettings set = new AppSettings(true);
		Dimension resolution = this.getCurrentScreenResolution();
		
		OptionXMLParser.sound = soundCheckbox.isChecked();
		OptionXMLParser.wideScreen = ratioCheckbox.isChecked();
		OptionXMLParser.screenResolution = resolution;
		OptionXMLParser.saveAppOptions(XMLFileStore.OPTION_SAVE_FILE);
	}
	
	public void showMessage() {
		screen.findNiftyControl(ON_SAVE_ID, Label.class).setText(StringStore.ON_SAVE_OPTION_MESSAGE);
	}
	
	public String getMenuTitle() {
		return StringStore.OPTION_MENU_TITLE;
	}
}
