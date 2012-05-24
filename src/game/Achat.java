package game;

import java.util.ArrayList;

import physics.BMWM3Properties;
import physics.CarProperties;
import physics.DodgeViperProperties;
import physics.SkylineProperties;
import physics.TypeCarProperties;
import save.Comptes;
import save.ProfilCurrent;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.ImageSelect;
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
	

	private TextField Bmwweight;
	private TextField Bmwpuis;
	private TextField DVweight;
	private TextField DVpuis;
	private TextField SLweight;
	private TextField SLpuis;
	
	private TextField SLprix;
	private TextField DVprix;
	private TextField BMWprix;
	
	private ImageSelect imgCar;
	private boolean droite;
	
	public Achat() {
		super();
		//Comptes.RecupeCar();
		dataAllCar = Comptes.getListCar();
		prixbmw = 48000;
		prixdv = 34000;
		prixsk = 22000;
		calcul = ProfilCurrent.getInstance().getMonnaie();
		hasBmw = false;
		hasDv = false;
		hasSk = false;
		droite = true;
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
		
		imgCar = screen.findNiftyControl("imgcar", ImageSelect.class);
		
		Bmwweight = screen.findNiftyControl("Bmwweight", TextField.class);
		Bmwpuis  = screen.findNiftyControl("Bmwpuis", TextField.class);
		DVweight = screen.findNiftyControl("DVweight", TextField.class);
		DVpuis = screen.findNiftyControl("DVpuis", TextField.class);
		SLweight = screen.findNiftyControl("SLweight", TextField.class);
		SLpuis  = screen.findNiftyControl("SLpuis", TextField.class);
		
		BMWprix = screen.findNiftyControl("Bmwprix", TextField.class);
		DVprix = screen.findNiftyControl("DVprix", TextField.class);
		SLprix = screen.findNiftyControl("SLprix", TextField.class);
		BMWprix.setText(Integer.toString(prixbmw));
		DVprix.setText(Integer.toString(prixdv));
		SLprix.setText(Integer.toString(prixsk));
		BMWprix.setEnabled(false);
		DVprix.setEnabled(false);
		SLprix.setEnabled(false);
		
		Bmwweight.setEnabled(false);
		Bmwpuis.setEnabled(false);
		DVweight.setEnabled(false);
		DVpuis.setEnabled(false);
		SLweight.setEnabled(false);
		SLpuis.setEnabled(false);
		
		for (int i = 0; i < dataAllCar.size(); ++i) {
			if (dataAllCar.get(i).getTypeCar().equals(TypeCarProperties.BMWM3)) {
				Bmwweight.setText(Integer.toString(dataAllCar.get(i).getWeight()));
				Bmwpuis.setText(Double.toString(dataAllCar.get(i).getPuissance()));
				checkboxbmw.setEnabled(true);
			} else if (dataAllCar.get(i).getTypeCar().equals(TypeCarProperties.DODGEVIPER)) {
				DVweight.setText(Integer.toString(dataAllCar.get(i).getWeight()));
				DVpuis.setText(Double.toString(dataAllCar.get(i).getPuissance()));
				checkboxdv.setEnabled(true);
			} else if (dataAllCar.get(i).getTypeCar().equals(TypeCarProperties.SKYLINE)) {
				SLweight.setText(Integer.toString(dataAllCar.get(i).getWeight()));
				SLpuis.setText(Double.toString(dataAllCar.get(i).getPuissance()));
				checkboxsl.setEnabled(true);
			}
		}
		
		ArrayList<CarProperties> allCarJoueur = ProfilCurrent.getInstance().getCar();
		for (int i = 0; i < allCarJoueur.size(); ++i) {
			if (allCarJoueur.get(i).getTypeCar().equals(TypeCarProperties.BMWM3)) {
				checkboxbmw.check();
				hasBmw = true;
				checkboxbmw.setEnabled(false);
			}
			if (allCarJoueur.get(i).getTypeCar().equals(TypeCarProperties.DODGEVIPER)) {
				checkboxdv.check();
				hasDv = true;
				checkboxdv.setEnabled(false);
			}
			if (allCarJoueur.get(i).getTypeCar().equals(TypeCarProperties.SKYLINE)) {
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
	
	public void changePhoto() {
		int maxphoto = 3;
		if (imgCar.getSelectedImageIndex() == maxphoto) {
			imgCar.setSelectedImageIndex(1);
			droite = false;
		} else if (droite || imgCar.getSelectedImageIndex() == 0){
			imgCar.forwardClick();
			switch (imgCar.getSelectedImageIndex()) {
				case 0:
					//checkboxstand.check();
					break;
				case 1:
					checkboxbmw.check();
					break;
				case 2:
					checkboxdv.check();
					break;
				case 3:
					checkboxsl.check();
					break;
			}
		} else if (!droite) {
			imgCar.backClick();
			/*NiftyImage img;
			imgCar.addImage();*/
			droite = true;
			//checkboxstand.check();
		}
	}
	
	public void gotoChooseProfil() {
		app.gotoAffProfil();
	}
	
	public void Enregistrer() {
		ArrayList<CarProperties> allCarJoueur = ProfilCurrent.getInstance().getCar();
		if (checkboxbmw.isChecked()) {
			for (int i = 0; i < dataAllCar.size(); ++i) {
				if (dataAllCar.get(i).getTypeCar().equals(TypeCarProperties.BMWM3)) {
					allCarJoueur.add(dataAllCar.get(i));
					break;
				}
			}
		}
		if (checkboxdv.isChecked()) {
			for (int i = 0; i < dataAllCar.size(); ++i) {
				if (dataAllCar.get(i).getTypeCar().equals(TypeCarProperties.DODGEVIPER)) {
					allCarJoueur.add(dataAllCar.get(i));
					break;
				}
			}
		}
		if (checkboxsl.isChecked()) {
			for (int i = 0; i < dataAllCar.size(); ++i) {
				if (dataAllCar.get(i).getTypeCar().equals(TypeCarProperties.SKYLINE)) {
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
