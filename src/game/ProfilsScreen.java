package game;

import java.util.ArrayList;

import physics.BMWM3Properties;
import physics.CarProperties;
import physics.DodgeViperProperties;
import physics.SkylineProperties;

import save.Comptes;
import save.Profil;
import save.ProfilCurrent;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextField;

public class ProfilsScreen extends AbstractScreenController {
	
	private InputManager inputManager;
	private final String ALLJOUEUR = "allJoueur";
	private ArrayList<Profil> dataAllJoueur;
	private DropDown<String> allJoueurDropDown;
	private TextField demi;
	private TextField quart;
	private TextField free;
	private TextField deadcarfree;
	private TextField monnaie;
	private String logchoose;
	
	private CheckBox checkboxstand;
	private CheckBox checkboxbmw;
	private CheckBox checkboxdv;
	private CheckBox checkboxsl;
	
	private TextField standtireHeight;
	private TextField standfinalGearRatio;
	private TextField standtireRadius;
	private TextField standweight;
	private TextField standstiffness;
	private TextField standmass;
	private TextField standidleRpm;
	private TextField standredline;
	private TextField BmwtireHeight;
	private TextField BmwfinalGearRatio;
	private TextField BmwtireRadius;
	private TextField Bmwweight;
	private TextField Bmwstiffness;
	private TextField Bmwmass;
	private TextField BmwidleRpm;
	private TextField Bmwredline;
	private TextField DVtireHeight;
	private TextField DVfinalGearRatio;
	private TextField DVtireRadius;
	private TextField DVweight;
	private TextField DVstiffness;
	private TextField DVmass;
	private TextField DVidleRpm;
	private TextField DVredline;
	private TextField SLtireHeight;
	private TextField SLfinalGearRatio;
	private TextField SLtireRadius;
	private TextField SLweight;
	private TextField SLstiffness;
	private TextField SLmass;
	private TextField SLidleRpm;
	private TextField SLredline;
	
	public ProfilsScreen() {
		super();
		dataAllJoueur = Comptes.getListProfil();
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
		
		demi.setEnabled(false);
		quart.setEnabled(false);
		free.setEnabled(false);
		deadcarfree.setEnabled(false);
		monnaie.setEnabled(false);
		
		checkboxstand = screen.findNiftyControl("Stand", CheckBox.class);
		checkboxbmw = screen.findNiftyControl("Bmw", CheckBox.class);
		checkboxdv = screen.findNiftyControl("Dodge", CheckBox.class);
		checkboxsl = screen.findNiftyControl("Skyline", CheckBox.class);
		
		standtireHeight = screen.findNiftyControl("StandtireHeight", TextField.class);
		standfinalGearRatio = screen.findNiftyControl("StandfinalGearRatio", TextField.class);
		standtireRadius = screen.findNiftyControl("StandtireRadius", TextField.class);
		standweight = screen.findNiftyControl("Standweight", TextField.class);
		standstiffness = screen.findNiftyControl("Standstiffness", TextField.class);
		standmass = screen.findNiftyControl("Standmass", TextField.class);
		standidleRpm = screen.findNiftyControl("StandidleRpm", TextField.class);
		standredline = screen.findNiftyControl("Standredline", TextField.class);
		BmwtireHeight = screen.findNiftyControl("BmwtireHeight", TextField.class);
		BmwfinalGearRatio = screen.findNiftyControl("BmwfinalGearRatio", TextField.class);
		BmwtireRadius = screen.findNiftyControl("BmwtireRadius", TextField.class);
		Bmwweight = screen.findNiftyControl("Bmwweight", TextField.class);
		Bmwstiffness = screen.findNiftyControl("Bmwstiffness", TextField.class);
		Bmwmass = screen.findNiftyControl("Bmwmass", TextField.class);
		BmwidleRpm = screen.findNiftyControl("BmwidleRpm", TextField.class);
		Bmwredline = screen.findNiftyControl("Bmwredline", TextField.class);
		DVtireHeight = screen.findNiftyControl("DVtireHeight", TextField.class);
		DVfinalGearRatio = screen.findNiftyControl("DVfinalGearRatio", TextField.class);
		DVtireRadius = screen.findNiftyControl("DVtireRadius", TextField.class);
		DVweight = screen.findNiftyControl("DVweight", TextField.class);
		DVstiffness = screen.findNiftyControl("DVstiffness", TextField.class);
		DVmass = screen.findNiftyControl("DVmass", TextField.class);
		DVidleRpm = screen.findNiftyControl("DVidleRpm", TextField.class);
		DVredline = screen.findNiftyControl("DVredline", TextField.class);
		SLtireHeight = screen.findNiftyControl("SLtireHeight", TextField.class);
		SLfinalGearRatio = screen.findNiftyControl("SLfinalGearRatio", TextField.class);
		SLtireRadius = screen.findNiftyControl("SLtireRadius", TextField.class);
		SLweight = screen.findNiftyControl("SLweight", TextField.class);
		SLstiffness = screen.findNiftyControl("SLstiffness", TextField.class);
		SLmass = screen.findNiftyControl("SLmass", TextField.class);
		SLidleRpm = screen.findNiftyControl("SLidleRpm", TextField.class);
		SLredline = screen.findNiftyControl("SLredline", TextField.class);
		
		AffBase();
		
		BmwtireHeight.setEnabled(false);
		BmwfinalGearRatio.setEnabled(false);
		BmwtireRadius.setEnabled(false);
		Bmwweight.setEnabled(false);
		Bmwstiffness.setEnabled(false);
		Bmwmass.setEnabled(false);
		BmwidleRpm.setEnabled(false);
		Bmwredline.setEnabled(false);
		DVtireHeight.setEnabled(false);
		DVfinalGearRatio.setEnabled(false);
		DVtireRadius.setEnabled(false);
		DVweight.setEnabled(false);
		DVstiffness.setEnabled(false);
		DVmass.setEnabled(false);
		DVidleRpm.setEnabled(false);
		DVredline.setEnabled(false);
		SLtireHeight.setEnabled(false);
		SLfinalGearRatio.setEnabled(false);
		SLtireRadius.setEnabled(false);
		SLweight.setEnabled(false);
		SLstiffness.setEnabled(false);
		SLmass.setEnabled(false);
		SLidleRpm.setEnabled(false);
		SLredline.setEnabled(false);
        standtireHeight.setEnabled(false);
        standfinalGearRatio.setEnabled(false);
        standtireRadius.setEnabled(false);
        standweight.setEnabled(false);
        standstiffness.setEnabled(false);
        standmass.setEnabled(false);
        standidleRpm.setEnabled(false);
        standredline.setEnabled(false);
	}

	private void AffBase() {
		for (int i = 0; i < dataAllJoueur.size(); ++i) {
			allJoueurDropDown.addItem(dataAllJoueur.get(i).getLogin());
		}
        if (ProfilCurrent.getInstance() == null) {
        	AffCars(0);
        } else {
    		for (int i = 0; i < dataAllJoueur.size(); ++i) {
    			if (dataAllJoueur.get(i).getId() == ProfilCurrent.getInstance().getId()) {
    				AffCars(i);
    				allJoueurDropDown.selectItemByIndex(i);
    				break;
    			}
    		}
        }
	}
	
	public void AffCars(int rangProfil) {
		
		checkboxbmw.setEnabled(false);
		checkboxdv.setEnabled(false);
		checkboxsl.setEnabled(false);
		
		BmwtireHeight.setText("");
		BmwfinalGearRatio.setText("");
		BmwtireRadius.setText("");
		Bmwweight.setText("");
		Bmwstiffness.setText("");
		Bmwmass.setText("");
		BmwidleRpm.setText("");
		Bmwredline.setText("");
		DVtireHeight.setText("");
		DVfinalGearRatio.setText("");
		DVtireRadius.setText("");
		DVweight.setText("");
		DVstiffness.setText("");
		DVmass.setText("");
		DVidleRpm.setText("");
		DVredline.setText("");
		SLtireHeight.setText("");
		SLfinalGearRatio.setText("");
		SLtireRadius.setText("");
		SLweight.setText("");
		SLstiffness.setText("");
		SLmass.setText("");
		SLidleRpm.setText("");
		SLredline.setText("");
        standtireHeight.setText("");
        standfinalGearRatio.setText("");
        standtireRadius.setText("");
        standweight.setText("");
        standstiffness.setText("");
        standmass.setText("");
        standidleRpm.setText("");
        standredline.setText("");
		
		demi.setText(dataAllJoueur.get(rangProfil).getTimeDemi());
		quart.setText(dataAllJoueur.get(rangProfil).getTimeQuart());
		free.setText(dataAllJoueur.get(rangProfil).getTimefree());
		deadcarfree.setText(Integer.toString(dataAllJoueur.get(rangProfil).getCardead()));
		monnaie.setText(Integer.toString(dataAllJoueur.get(rangProfil).getMonnaie()));
		ArrayList<CarProperties> cars = dataAllJoueur.get(rangProfil).getCar();
		if (cars.get(dataAllJoueur.get(rangProfil).getChoixCar())
				instanceof BMWM3Properties) {
			checkboxbmw.check();
		}else if (cars.get(dataAllJoueur.get(rangProfil).getChoixCar())
				instanceof SkylineProperties) {
			checkboxsl.check();
		} else if (cars.get(dataAllJoueur.get(rangProfil).getChoixCar())
				instanceof DodgeViperProperties) {
			checkboxdv.check();
		} else {
			checkboxstand.check();
		}
		for (int i = 0; i < cars.size(); ++i) {
			if (cars.get(i) instanceof BMWM3Properties) {
				BmwtireHeight.setText(Double.toString(cars.get(i).getTireHeight()));
				BmwfinalGearRatio.setText(Double.toString(cars.get(i).getFinalGearRatio()));
				BmwtireRadius.setText(Double.toString(cars.get(i).getTireRadius()));
				Bmwweight.setText(Integer.toString(cars.get(i).getWeight()));
				Bmwstiffness.setText(Double.toString(cars.get(i).getStiffness()));
				Bmwmass.setText(Double.toString(cars.get(i).getMass()));
				BmwidleRpm.setText(Integer.toString(cars.get(i).getIdleRpm()));
				Bmwredline.setText(Integer.toString(cars.get(i).getRedline()));
				checkboxbmw.setEnabled(true);
			} else if (cars.get(i) instanceof DodgeViperProperties) {
				DVtireHeight.setText(Double.toString(cars.get(i).getTireHeight()));
				DVfinalGearRatio.setText(Double.toString(cars.get(i).getFinalGearRatio()));
				DVtireRadius.setText(Double.toString(cars.get(i).getTireRadius()));
				DVweight.setText(Integer.toString(cars.get(i).getWeight()));
				DVstiffness.setText(Double.toString(cars.get(i).getStiffness()));
				DVmass.setText(Double.toString(cars.get(i).getMass()));
				DVidleRpm.setText(Integer.toString(cars.get(i).getIdleRpm()));
				DVredline.setText(Integer.toString(cars.get(i).getRedline()));
				checkboxdv.setEnabled(true);
			} else if (cars.get(i) instanceof SkylineProperties) {
				SLtireHeight.setText(Double.toString(cars.get(i).getTireHeight()));
				SLfinalGearRatio.setText(Double.toString(cars.get(i).getFinalGearRatio()));
				SLtireRadius.setText(Double.toString(cars.get(i).getTireRadius()));
				SLweight.setText(Integer.toString(cars.get(i).getWeight()));
				SLstiffness.setText(Double.toString(cars.get(i).getStiffness()));
				SLmass.setText(Double.toString(cars.get(i).getMass()));
				SLidleRpm.setText(Integer.toString(cars.get(i).getIdleRpm()));
				SLredline.setText(Integer.toString(cars.get(i).getRedline()));
				checkboxsl.setEnabled(true);
			} else {
		        standtireHeight.setText(Double.toString(cars.get(i).getTireHeight()));
		        standfinalGearRatio.setText(Double.toString(cars.get(i).getFinalGearRatio()));
		        standtireRadius.setText(Double.toString(cars.get(i).getTireRadius()));
		        standweight.setText(Integer.toString(cars.get(i).getWeight()));
		        standstiffness.setText(Double.toString(cars.get(i).getStiffness()));
		        standmass.setText(Double.toString(cars.get(i).getMass()));
		        standidleRpm.setText(Integer.toString(cars.get(i).getIdleRpm()));
		        standredline.setText(Integer.toString(cars.get(i).getRedline()));
            }
		} // for
	}
	
	@NiftyEventSubscriber(id=ALLJOUEUR)
	public void onResolutionChange(final String id, final DropDownSelectionChangedEvent<String> event) {
		logchoose = event.getSelection();
		for (int i = 0; i < dataAllJoueur.size(); ++i) {
			if (logchoose.equals(dataAllJoueur.get(i).getLogin())) {
				AffCars(i);
				break;
			}
		}
	}
	
	@NiftyEventSubscriber(id="Stand")
	public void checkStand (final String id, final CheckBoxStateChangedEvent event) {
		if (event.isChecked()) {
			if (checkboxdv.isChecked())
				checkboxdv.uncheck();
			if (checkboxsl.isChecked())
				checkboxsl.uncheck();
			if (checkboxbmw.isChecked())
				checkboxbmw.uncheck();
		}
	}

	@NiftyEventSubscriber(id="Bmw")
	public void checkBmw (final String id, final CheckBoxStateChangedEvent event) {
		if (event.isChecked()) {
			if (checkboxstand.isChecked())
				checkboxstand.uncheck();
			if (checkboxdv.isChecked())
				checkboxdv.uncheck();
			if (checkboxsl.isChecked())
				checkboxsl.uncheck();
		}
	}
	
	@NiftyEventSubscriber(id="Dodge")
	public void checkDodge (final String id, final CheckBoxStateChangedEvent event) {
		if (event.isChecked()) {
			if (checkboxstand.isChecked())
				checkboxstand.uncheck();
			if (checkboxbmw.isChecked())
				checkboxbmw.uncheck();
			if (checkboxsl.isChecked())
				checkboxsl.uncheck();
		}
	}
	
	@NiftyEventSubscriber(id="Skyline")
	public void checkSkyline (final String id, final CheckBoxStateChangedEvent event) {
		if (event.isChecked()) {
			if (checkboxstand.isChecked())
				checkboxstand.uncheck();
			if (checkboxbmw.isChecked())
				checkboxbmw.uncheck();
			if (checkboxdv.isChecked())
				checkboxdv.uncheck();
		}
	}
	
	public void savemodif(int rangprofil) {
		int choixCar = dataAllJoueur.get(rangprofil).getChoixCar();
		ArrayList<CarProperties> cars = dataAllJoueur.get(rangprofil).getCar();
		if (checkboxbmw.isChecked()) {
			for (int j = 0; j < cars.size(); ++j) {
				if (cars.get(j) instanceof BMWM3Properties) {
					choixCar = j;
					break;
				}
			}
		} else if (checkboxdv.isChecked()) {
			for (int j = 0; j < cars.size(); ++j) {
				if (cars.get(j) instanceof DodgeViperProperties) {
					choixCar = j;
					break;
				}
			}
		} else if (checkboxsl.isChecked()) {
			for (int j = 0; j < cars.size(); ++j) {
				if (cars.get(j) instanceof SkylineProperties) {
					choixCar = j;
					break;
				}
			}
		} else if (checkboxstand.isChecked()) {
			for (int j = 0; j < cars.size(); ++j) {
				if (!(cars.get(j) instanceof SkylineProperties) && 
						!(cars.get(j) instanceof DodgeViperProperties)
							&& !(cars.get(j) instanceof BMWM3Properties)) {
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
	
	public void gotoMainMenu() {
		app.gotoStart();
	}
}
