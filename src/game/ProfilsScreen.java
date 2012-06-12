package game;

import ia.IALevel;

import java.util.ArrayList;

import physics.CarProperties;
import physics.TypeCarProperties;

import save.Comptes;
import save.Profil;
import save.ProfilCurrent;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.ImageSelect;
import de.lessvoid.nifty.controls.ImageSelectSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextField;

public class ProfilsScreen extends AbstractScreenController {
	
	private InputManager inputManager;
	private final String ALLJOUEUR = "allJoueur";
	private final String IMGCAR = "imgcar";
	private ArrayList<Profil> dataAllJoueur;
	private ArrayList<CarProperties> dataAllCar;
	private ArrayList<CarProperties> carsPlayer;
	private DropDown<String> allJoueurDropDown;
	private TextField demi;
	private TextField quart;
	private TextField free;
	private TextField deadcarfree;
	private TextField monnaie;
	private String logchoose;
	
	private TextField typeCar;
	private TextField weight;
	private TextField puis;
	private TextField nitro;
	
	private ImageSelect imgCar;

	private DropDown<String> niveau;
	
	private final String NIVEAU = "niveau";
	
	public ProfilsScreen() {
		super();
		dataAllJoueur = Comptes.getListProfil();
		dataAllCar = Comptes.getListCar();
		carsPlayer = new ArrayList<CarProperties>();
	}

	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);
		
		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);
	
		this.inputManager = app.getInputManager();
		
		/* keyboard mappings */
		setInputMappings();
		
		allJoueurDropDown = screen.findNiftyControl(ALLJOUEUR, DropDown.class);
		demi = screen.findNiftyControl("demi", TextField.class);
		quart = screen.findNiftyControl("quart", TextField.class);
		free = screen.findNiftyControl("free", TextField.class);
		deadcarfree = screen.findNiftyControl("cardead", TextField.class);
		monnaie = screen.findNiftyControl("monnaie", TextField.class);
		
		imgCar = screen.findNiftyControl(IMGCAR, ImageSelect.class);
		typeCar = screen.findNiftyControl("typeCar", TextField.class);
		
		niveau = screen.findNiftyControl(NIVEAU, DropDown.class);
		niveau.clear();
		allJoueurDropDown.clear();
		fillLevelDropDown();
		
		demi.setEnabled(false);
		quart.setEnabled(false);
		free.setEnabled(false);
		deadcarfree.setEnabled(false);
		monnaie.setEnabled(false);
		typeCar.setEnabled(false);
		
		weight = screen.findNiftyControl("weight", TextField.class);
		puis = screen.findNiftyControl("puis", TextField.class);
		nitro = screen.findNiftyControl("nitro", TextField.class);
		
		AffBase();
		
		typeCar.setEnabled(false);
		weight.setEnabled(false);
		puis.setEnabled(false);
		nitro.setEnabled(false);
	}
	
	/**
	 * Met en place les évènements clavier.
	 */
	private void setInputMappings() {
		inputManager.addMapping("return", new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addListener(new ActionListener() {
			@Override
			public void onAction(String arg0, boolean arg1, float arg2) {
				gotoMainMenu();
			}
		}, "return");
		
		inputManager.addMapping("save", new KeyTrigger(KeyInput.KEY_LCONTROL));
		inputManager.addListener(new ActionListener() {
			@Override
			public void onAction(String arg0, boolean arg1, float arg2) {
				Enregistrer();
			}
		}, "save");
	}
	
	@Override
	public void onEndScreen() {
		inputManager.clearMappings();
		super.onEndScreen();
	}

	private void fillLevelDropDown() {
		for (IALevel level : IALevel.values()) {
			niveau.addItem(level.toString());
		}
	}
	
	private void AffBase() {
		for (int i = 0; i < dataAllJoueur.size(); ++i) {
			allJoueurDropDown.addItem(dataAllJoueur.get(i).getLogin());
		}
        if (ProfilCurrent.getInstance() == null) {
        	if (Comptes.getListProfil().size() != 0) {
        		AffDataPlayer(0);
        	}
        } else {
    		for (int i = 0; i < dataAllJoueur.size(); ++i) {
    			if (dataAllJoueur.get(i).getId() == ProfilCurrent.getInstance().getId()) {
    				allJoueurDropDown.selectItemByIndex(i);
    				break;
    			}
    		}
        }
	}
	
	private void forAllCar(TypeCarProperties typeCarChoose) {
		
		for (int j = 0; j < carsPlayer.size(); ++j) {
			if (carsPlayer.get(j).getTypeCar().equals(typeCarChoose)) {
				typeCar.setText(carsPlayer.get(j).getTypeCar().toString());
				puis.setText(Integer.toString(carsPlayer.get(j).getPuissance()));
				weight.setText(Integer.toString(carsPlayer.get(j).getWeight()));
				nitro.setText((carsPlayer.get(j).isImprovenitro())?"Oui" : "Non");
				break;
			}
		}
	}
	
	public void AffDataPlayer(int rangProfil) {

		typeCar.setText("");
		weight.setText("");
		puis.setText("");
		nitro.setText("");
		
		demi.setText(dataAllJoueur.get(rangProfil).getTimeDemi());
		quart.setText(dataAllJoueur.get(rangProfil).getTimeQuart());
		free.setText(dataAllJoueur.get(rangProfil).getTimefree());
		deadcarfree.setText(Integer.toString(dataAllJoueur.get(rangProfil).getCardead()));
		monnaie.setText(Integer.toString(dataAllJoueur.get(rangProfil).getMonnaie()));
		niveau.selectItem(dataAllJoueur.get(rangProfil).getLevel().toString());
		carsPlayer = dataAllJoueur.get(rangProfil).getCar();
		int numchoixcar = dataAllJoueur.get(rangProfil).getChoixCar();

		if (carsPlayer.get(numchoixcar).getTypeCar().equals(TypeCarProperties.CHARGER)) {
			imgCar.setSelectedImageIndex(1);
			imgCar.backClick();
			imgCar.setSelectedImageIndex(0);
			forAllCar(TypeCarProperties.CHARGER);
		} else if (carsPlayer.get(numchoixcar).getTypeCar().equals(TypeCarProperties.CORVETTE)) {
			imgCar.setSelectedImageIndex(2);
			imgCar.backClick();
			imgCar.setSelectedImageIndex(1);
			forAllCar(TypeCarProperties.CORVETTE);
		} else if (carsPlayer.get(numchoixcar).getTypeCar().equals(TypeCarProperties.BMWM3)) {
			imgCar.setSelectedImageIndex(3);
			imgCar.backClick();
			imgCar.setSelectedImageIndex(2);
			forAllCar(TypeCarProperties.BMWM3);
		} else if (carsPlayer.get(numchoixcar).getTypeCar().equals(TypeCarProperties.FERRARI)) {
			imgCar.setSelectedImageIndex(2);
			imgCar.forwardClick();
			imgCar.setSelectedImageIndex(3);
			forAllCar(TypeCarProperties.FERRARI);
		}
		
	}
	
	@NiftyEventSubscriber(id=ALLJOUEUR)
	public void onResolutionChange(final String id, final DropDownSelectionChangedEvent<String> event) {
		logchoose = event.getSelection();
		for (int i = 0; i < dataAllJoueur.size(); ++i) {
			if (logchoose.equals(dataAllJoueur.get(i).getLogin())) {
				AffDataPlayer(i);
				break;
			}
		}
	}
	
	@NiftyEventSubscriber(id=IMGCAR)
	public void onChangePhoto(final String id, final ImageSelectSelectionChangedEvent event) {
		changeDataPhoto();
	}
	
	public void changePhoto() {
		int maxphoto = 3;
		if (imgCar.getSelectedImageIndex() == maxphoto) {
			imgCar.setSelectedImageIndex(1);
			imgCar.backClick();
		} else if (imgCar.getSelectedImageIndex() >= 0 && imgCar.getSelectedImageIndex() < maxphoto){
			imgCar.forwardClick();
		}
		
		changeDataPhoto();
	}
    
	public void changeDataPhoto() {
		typeCar.setText("");
		weight.setText("");
		puis.setText("");
		nitro.setText("");
		if (imgCar.getSelectedImageIndex() == 0) {
			forAllCar(TypeCarProperties.CHARGER);
		} else if (imgCar.getSelectedImageIndex() == 1) {
			forAllCar(TypeCarProperties.CORVETTE);
		} else if (imgCar.getSelectedImageIndex() == 2) {
			forAllCar(TypeCarProperties.BMWM3);
		} else if (imgCar.getSelectedImageIndex() == 3){
			forAllCar(TypeCarProperties.FERRARI);
		}
	}
	
	public int forCarPlayer (TypeCarProperties typeCarSelect, int rangprofil) {
		ArrayList<CarProperties> cars = dataAllJoueur.get(rangprofil).getCar();
		for (int j = 0; j < cars.size(); ++j) {
			if (cars.get(j).getTypeCar().equals(typeCarSelect)) {
				return j;
			}
		}
		return -1;
	}
	
	public void savemodif(int rangprofil) {
		int choixCar = -1;
		
		if (imgCar.getSelectedImageIndex() == 1) {
			choixCar = forCarPlayer (TypeCarProperties.CORVETTE, rangprofil);
		} else if (imgCar.getSelectedImageIndex() == 2) {
			choixCar = forCarPlayer (TypeCarProperties.BMWM3, rangprofil);
		} else if (imgCar.getSelectedImageIndex() == 3) {
			choixCar = forCarPlayer (TypeCarProperties.FERRARI, rangprofil);
		} else if (imgCar.getSelectedImageIndex() == 0) {
			choixCar = forCarPlayer (TypeCarProperties.CHARGER, rangprofil);
		}
		
		if (choixCar == -1)
			choixCar = dataAllJoueur.get(rangprofil).getChoixCar();
		
		if (ProfilCurrent.getInstance() != null) {
			ProfilCurrent.getInstance().setLastchoose(false);
			Comptes.modifier(ProfilCurrent.getInstance());
		}
		
		ProfilCurrent.setInstance(Comptes.getListProfil().get(rangprofil));
		ProfilCurrent.getInstance().setChoixCar(choixCar);
		ProfilCurrent.getInstance().setLastchoose(true);
		for (IALevel level : IALevel.values()) {
			if(niveau.getSelection().equals(level.toString())) {
				ProfilCurrent.getInstance().setLevel(level);
			}
		}
		Comptes.modifier(ProfilCurrent.getInstance());
		Comptes.Enregistrer();
	}
	
	public void Enregistrer() {
		for (int i = 0; i < dataAllJoueur.size(); ++i) {
			if (logchoose.equals(dataAllJoueur.get(i).getLogin())) {
				savemodif(i);
				break;
			} //if
		}
		gotoMainMenu();
	}
	
	public void Remove() {
		for (int i = 0; i < dataAllJoueur.size(); ++i) {
			if (logchoose.equals(dataAllJoueur.get(i).getLogin())) {
				dataAllJoueur.remove(i);
				Comptes.Enregistrer();
				break;
			}
		}
		allJoueurDropDown = null;
		nifty.addXml("Interface/Nifty/ChooseProfil.xml");
        nifty.gotoScreen("chooseprofil");
        ProfilsScreen pfscreen = (ProfilsScreen) nifty.getCurrentScreen().getScreenController();
        stateManager.attach(pfscreen);
	}
	
	public void Achat() {
		for (int i = 0; i < dataAllJoueur.size(); ++i) {
			if (logchoose.equals(dataAllJoueur.get(i).getLogin())) {
				savemodif(i);
				break;
			}
		}
		app.gotoAchat();
	}
	
	public void Garage() {
		for (int i = 0; i < dataAllJoueur.size(); ++i) {
			if (logchoose.equals(dataAllJoueur.get(i).getLogin())) {
				savemodif(i);
				break;
			}
		}
		app.gotoGarage();
	}
	
	public void gotoMainMenu() {
		app.gotoStart();
	}
}
