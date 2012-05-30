package game;

import java.util.ArrayList;

import physics.BMWM3Properties;
import physics.CarProperties;
import physics.DodgeViperProperties;
import physics.SkylineProperties;
import physics.TypeCarProperties;

import save.Comptes;
import save.Profil;
import save.ProfilCurrent;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.niftygui.RenderDeviceJme;
import com.jme3.texture.Image;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.ImageSelect;
import de.lessvoid.nifty.controls.ImageSelectSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.imageselect.builder.ImageSelectBuilder;
import de.lessvoid.nifty.nulldevice.NullRenderDevice;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyImageManager;
import de.lessvoid.nifty.render.NiftyRenderEngineImpl;
import de.lessvoid.nifty.render.image.ImageMode;

public class ProfilsScreen extends AbstractScreenController {
	
	private InputManager inputManager;
	private final String ALLJOUEUR = "allJoueur";
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
		
		allJoueurDropDown = screen.findNiftyControl(ALLJOUEUR, DropDown.class);
		demi = screen.findNiftyControl("demi", TextField.class);
		quart = screen.findNiftyControl("quart", TextField.class);
		free = screen.findNiftyControl("free", TextField.class);
		deadcarfree = screen.findNiftyControl("cardead", TextField.class);
		monnaie = screen.findNiftyControl("monnaie", TextField.class);
		
		imgCar = screen.findNiftyControl("imgcar", ImageSelect.class);
		typeCar = screen.findNiftyControl("typeCar", TextField.class);
		
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
		carsPlayer = dataAllJoueur.get(rangProfil).getCar();
		int numchoixcar = dataAllJoueur.get(rangProfil).getChoixCar();
		
		typeCar.setText(carsPlayer.get(numchoixcar).getTypeCar().toString());
		puis.setText(Integer.toString(carsPlayer.get(numchoixcar).getPuissance()));
		weight.setText(Integer.toString(carsPlayer.get(numchoixcar).getWeight()));
		nitro.setText(Boolean.toString(carsPlayer.get(numchoixcar).isImprovenitro()));
		
		if (carsPlayer.get(numchoixcar).getTypeCar().equals(TypeCarProperties.SKYLINE)) {
			imgCar.setSelectedImageIndex(1);
			imgCar.backClick();
		} else if (carsPlayer.get(numchoixcar).getTypeCar().equals(TypeCarProperties.DODGEVIPER)) {
			imgCar.setSelectedImageIndex(2);
			imgCar.backClick();
		} else if (carsPlayer.get(numchoixcar).getTypeCar().equals(TypeCarProperties.BMWM3)) {
			imgCar.setSelectedImageIndex(3);
			imgCar.backClick();
		} else if (carsPlayer.get(numchoixcar).getTypeCar().equals(TypeCarProperties.FERRARI)) {
			imgCar.setSelectedImageIndex(2);
			imgCar.forwardClick();
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
	
	public void changePhoto() {
		int maxphoto = 3;
		if (imgCar.getSelectedImageIndex() == maxphoto) {
			imgCar.setSelectedImageIndex(1);
			imgCar.backClick();
		} else if (imgCar.getSelectedImageIndex() >= 0 && imgCar.getSelectedImageIndex() < maxphoto){
			imgCar.forwardClick();
		}
		
		typeCar.setText("");
		weight.setText("");
		puis.setText("");
		nitro.setText("");
		
		if (imgCar.getSelectedImageIndex() == 0) {
			for (int i = 0; i < carsPlayer.size(); ++i) {
				if (carsPlayer.get(i).getTypeCar().equals(TypeCarProperties.SKYLINE)) {
					typeCar.setText(carsPlayer.get(i).getTypeCar().toString());
					puis.setText(Integer.toString(carsPlayer.get(i).getPuissance()));
					weight.setText(Integer.toString(carsPlayer.get(i).getWeight()));
					nitro.setText(Boolean.toString(carsPlayer.get(i).isImprovenitro()));
					break;
				}						
			}
		} else if (imgCar.getSelectedImageIndex() == 1) {
			for (int i = 0; i < carsPlayer.size(); ++i) {
				if (carsPlayer.get(i).getTypeCar().equals(TypeCarProperties.DODGEVIPER)) {
					typeCar.setText(carsPlayer.get(i).getTypeCar().toString());
					puis.setText(Integer.toString(carsPlayer.get(i).getPuissance()));
					weight.setText(Integer.toString(carsPlayer.get(i).getWeight()));
					nitro.setText(Boolean.toString(carsPlayer.get(i).isImprovenitro()));
					break;
				}						
			}
		} else if (imgCar.getSelectedImageIndex() == 2) {
			for (int i = 0; i < carsPlayer.size(); ++i) {
				if (carsPlayer.get(i).getTypeCar().equals(TypeCarProperties.BMWM3)) {
					typeCar.setText(carsPlayer.get(i).getTypeCar().toString());
					puis.setText(Integer.toString(carsPlayer.get(i).getPuissance()));
					weight.setText(Integer.toString(carsPlayer.get(i).getWeight()));
					nitro.setText(Boolean.toString(carsPlayer.get(i).isImprovenitro()));
					break;
				}						
			}
		} else {
			for (int i = 0; i < carsPlayer.size(); ++i) {
				if (carsPlayer.get(i).getTypeCar().equals(TypeCarProperties.FERRARI)) {
					typeCar.setText(carsPlayer.get(i).getTypeCar().toString());
					puis.setText(Integer.toString(carsPlayer.get(i).getPuissance()));
					weight.setText(Integer.toString(carsPlayer.get(i).getWeight()));
					nitro.setText(Boolean.toString(carsPlayer.get(i).isImprovenitro()));
					break;
				}						
			}
		}
	}
    
	public void savemodif(int rangprofil) {
		int choixCar = dataAllJoueur.get(rangprofil).getChoixCar();
		ArrayList<CarProperties> cars = dataAllJoueur.get(rangprofil).getCar();
		if (imgCar.getSelectedImageIndex() == 1) {
			for (int j = 0; j < cars.size(); ++j) {
				if (cars.get(j).getTypeCar().equals(TypeCarProperties.DODGEVIPER)) {
					choixCar = j;
					break;
				}
			}
		} else if (imgCar.getSelectedImageIndex() == 2) {
			for (int j = 0; j < cars.size(); ++j) {
				if (cars.get(j).getTypeCar().equals(TypeCarProperties.BMWM3)) {
					choixCar = j;
					break;
				}
			}
		} else if (imgCar.getSelectedImageIndex() == 3) {
			for (int j = 0; j < cars.size(); ++j) {
				if (cars.get(j).getTypeCar().equals(TypeCarProperties.FERRARI)) {
					choixCar = j;
					break;
				}
			}
		} else if (imgCar.getSelectedImageIndex() == 0) {
			for (int j = 0; j < cars.size(); ++j) {
				if (cars.get(j).getTypeCar().equals(TypeCarProperties.SKYLINE)) {
					choixCar = j;
					break;
				}
			}
		}
		ProfilCurrent pc = new ProfilCurrent(Comptes.getListProfil().get(rangprofil));
		pc.getInstance().setChoixCar(choixCar);
		Comptes.modifier(pc.getInstance());
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
