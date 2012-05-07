package game;

import java.awt.Dimension;
import java.util.Comparator;
import java.util.TreeMap;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;

public class OptionScreenState extends AbstractScreenController {

	private InputManager inputManager;
	private TreeMap<String, Dimension> normalResolutions;
	private TreeMap<String, Dimension> wideResolutions;
	private DropDown<String> resolutionDropDown;			/* Menu déroulant des résolutions */
	
	private final String RESOLUTION_DROP_ID = "resolutionDropDown";
	private final String RATIO_CHECKBOX_ID = "wideScreen";

	/* Comparateur personnalisé pour la liste */
	private Comparator<String> myComp = new Comparator<String>() {
		public int compare(String arg0, String arg1) {
			if (arg0.length() == arg1.length()) {
				return arg0.compareTo(arg1);
			} else {
				return (arg0.length() > arg1.length()) ? 1 : 0;
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
		normalResolutions.put("1480 x 1260", new Dimension(1480, 1260));
		
		wideResolutions.put("1280 x 720", new Dimension(1280, 720));
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
		
		fillResolutionDropDown(normalResolutions);
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
	 * Fonction callBack appelée lorsque la résolution est changée dans le menu déroulant.
	 * @param id
	 * @param event
	 * @author Alexandre GILLE
	 */
	@NiftyEventSubscriber(id=RESOLUTION_DROP_ID)
	public void onResolutionChange(final String id, final DropDownSelectionChangedEvent<String> event) {
		System.out.println("resolutionDropDown selection: " + event.getSelection());
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
		return null;
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
	 */
	public void applyOptions() {
		System.out.println("Option saved");
	}
	
	public String getMenuTitle() {
		return StringStore.OPTION_MENU_TITLE;
	}
}
