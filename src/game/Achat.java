package game;

import java.util.ArrayList;

import physics.BMWM3Properties;
import physics.CarProperties;
import physics.DodgeViperProperties;
import physics.SkylineProperties;
import save.Comptes;
import save.ProfilCurrent;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.TextField;

public class Achat extends AbstractScreenController {
	
	private InputManager inputManager;
	
	private ArrayList<CarProperties> dataAllCar;
	
	private TextField monnaie;
	private int prixbmw;
	private int prixdv;
	private int prixsk;
	private int calcul;
	private boolean hasBmw;
	private boolean hasDv;
	private boolean hasSk;
	
	private CheckBox checkboxbmw;
	private CheckBox checkboxdv;
	private CheckBox checkboxsl;
	
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
	
	private TextField SLprix;
	private TextField DVprix;
	private TextField BMWprix;
	
	public Achat() {
		super();
		Comptes.RecupeCar();
		dataAllCar = Comptes.getListCar();
		prixbmw = 48000;
		prixdv = 34000;
		prixsk = 22000;
		calcul = ProfilCurrent.getInstance().getMonnaie();
		hasBmw = false;
		hasDv = false;
		hasSk = false;
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		super.initialize(stateManager, a);

		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);

		this.inputManager = app.getInputManager();
		
		checkboxbmw = screen.findNiftyControl("Bmw", CheckBox.class);
		checkboxdv = screen.findNiftyControl("Dodge", CheckBox.class);
		checkboxsl = screen.findNiftyControl("Skyline", CheckBox.class);
		
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
		
		BMWprix = screen.findNiftyControl("Bmwprix", TextField.class);
		DVprix = screen.findNiftyControl("DVprix", TextField.class);
		SLprix = screen.findNiftyControl("SLprix", TextField.class);
		BMWprix.setText(Integer.toString(prixbmw));
		DVprix.setText(Integer.toString(prixdv));
		SLprix.setText(Integer.toString(prixsk));
		BMWprix.setEnabled(false);
		DVprix.setEnabled(false);
		SLprix.setEnabled(false);
		
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
		
		for (int i = 0; i < dataAllCar.size(); ++i) {
			if (dataAllCar.get(i) instanceof BMWM3Properties) {
				BmwtireHeight.setText(Double.toString(dataAllCar.get(i).getTireHeight()));
				BmwfinalGearRatio.setText(Double.toString(dataAllCar.get(i).getFinalGearRatio()));
				BmwtireRadius.setText(Double.toString(dataAllCar.get(i).getTireRadius()));
				Bmwweight.setText(Integer.toString(dataAllCar.get(i).getWeight()));
				Bmwstiffness.setText(Double.toString(dataAllCar.get(i).getStiffness()));
				Bmwmass.setText(Double.toString(dataAllCar.get(i).getMass()));
				BmwidleRpm.setText(Integer.toString(dataAllCar.get(i).getIdleRpm()));
				Bmwredline.setText(Integer.toString(dataAllCar.get(i).getRedline()));
				checkboxbmw.setEnabled(true);
			} else if (dataAllCar.get(i) instanceof DodgeViperProperties) {
				DVtireHeight.setText(Double.toString(dataAllCar.get(i).getTireHeight()));
				DVfinalGearRatio.setText(Double.toString(dataAllCar.get(i).getFinalGearRatio()));
				DVtireRadius.setText(Double.toString(dataAllCar.get(i).getTireRadius()));
				DVweight.setText(Integer.toString(dataAllCar.get(i).getWeight()));
				DVstiffness.setText(Double.toString(dataAllCar.get(i).getStiffness()));
				DVmass.setText(Double.toString(dataAllCar.get(i).getMass()));
				DVidleRpm.setText(Integer.toString(dataAllCar.get(i).getIdleRpm()));
				DVredline.setText(Integer.toString(dataAllCar.get(i).getRedline()));
				checkboxdv.setEnabled(true);
			} else if (dataAllCar.get(i) instanceof SkylineProperties) {
				SLtireHeight.setText(Double.toString(dataAllCar.get(i).getTireHeight()));
				SLfinalGearRatio.setText(Double.toString(dataAllCar.get(i).getFinalGearRatio()));
				SLtireRadius.setText(Double.toString(dataAllCar.get(i).getTireRadius()));
				SLweight.setText(Integer.toString(dataAllCar.get(i).getWeight()));
				SLstiffness.setText(Double.toString(dataAllCar.get(i).getStiffness()));
				SLmass.setText(Double.toString(dataAllCar.get(i).getMass()));
				SLidleRpm.setText(Integer.toString(dataAllCar.get(i).getIdleRpm()));
				SLredline.setText(Integer.toString(dataAllCar.get(i).getRedline()));
				checkboxsl.setEnabled(true);
			}
		}
		
		ArrayList<CarProperties> allCarJoueur = ProfilCurrent.getInstance().getCar();
		for (int i = 0; i < allCarJoueur.size(); ++i) {
			if (allCarJoueur.get(i) instanceof BMWM3Properties) {
				checkboxbmw.check();
				hasBmw = true;
				checkboxbmw.setEnabled(false);
			}
			if (allCarJoueur.get(i) instanceof DodgeViperProperties) {
				checkboxdv.check();
				hasDv = true;
				checkboxdv.setEnabled(false);
			}
			if (allCarJoueur.get(i) instanceof SkylineProperties) {
				checkboxsl.check();
				hasSk = true;
				checkboxsl.setEnabled(false);
			}
		}
		
		calcul = ProfilCurrent.getInstance().getMonnaie();
		monnaie = screen.findNiftyControl("monnaie", TextField.class);
		monnaie.setText(Integer.toString(calcul));
		monnaie.setEnabled(false);
		
		blockcheckbmw();
		blockcheckdv();
		blockchecksk();
	}
	
	public void blockcheckbmw () {
		if (hasBmw == false) {
			if (calcul < prixbmw) {
				checkboxbmw.setEnabled(false);
			} else {
				checkboxbmw.setEnabled(true);
			}
		}
	}
	
	public void blockcheckdv() {
		if (hasDv == false) {
			if (calcul < prixdv) {
				checkboxdv.setEnabled(false);
			} else {
				checkboxdv.setEnabled(true);
			}
		}
	}
	
	public void blockchecksk() {
		if (hasSk == false) {
			if (calcul < prixsk) {
				checkboxsl.setEnabled(false);
			} else {
				checkboxsl.setEnabled(true);
			}
		}
	}
	
	@NiftyEventSubscriber(id="Bmw")
	public void checkBmw (final String id, final CheckBoxStateChangedEvent event) {
		if (event.isChecked()) {
			calcul -= prixbmw;
		} else {
			calcul += prixbmw;
		}
		monnaie.setText(Integer.toString(calcul));
		blockcheckdv();
		blockchecksk();
	}
	
	@NiftyEventSubscriber(id="Dodge")
	public void checkDodge (final String id, final CheckBoxStateChangedEvent event) {
		if (event.isChecked()) {
			calcul -= prixdv;
		} else {
			calcul += prixdv;
		}
		monnaie.setText(Integer.toString(calcul));
		blockchecksk();
		blockcheckbmw();
	}
	
	@NiftyEventSubscriber(id="Skyline")
	public void checkSkyline (final String id, final CheckBoxStateChangedEvent event) {
		if (event.isChecked()) {
			calcul -= prixsk;
		} else {
			calcul += prixsk;
		}
		monnaie.setText(Integer.toString(calcul));
		blockcheckbmw();
		blockcheckdv();
	}
	
	public void gotoChooseProfil() {
		app.gotoAffProfil();
	}
	
	public void Enregistrer() {
		ArrayList<CarProperties> allCarJoueur = ProfilCurrent.getInstance().getCar();
		if (checkboxbmw.isChecked()) {
			for (int i = 0; i < dataAllCar.size(); ++i) {
				if (dataAllCar.get(i) instanceof BMWM3Properties) {
					allCarJoueur.add(dataAllCar.get(i));
					break;
				}
			}
		}
		if (checkboxdv.isChecked()) {
			for (int i = 0; i < dataAllCar.size(); ++i) {
				if (dataAllCar.get(i) instanceof DodgeViperProperties) {
					allCarJoueur.add(dataAllCar.get(i));
					break;
				}
			}
		}
		if (checkboxsl.isChecked()) {
			for (int i = 0; i < dataAllCar.size(); ++i) {
				if (dataAllCar.get(i) instanceof SkylineProperties) {
					allCarJoueur.add(dataAllCar.get(i));
					break;
				}
			}
		}
		ProfilCurrent.getInstance().setCar(allCarJoueur);
		ProfilCurrent.getInstance().setMonnaie(calcul);
		Comptes.modifier(ProfilCurrent.getInstance());
		app.gotoAffProfil();
	}
}
