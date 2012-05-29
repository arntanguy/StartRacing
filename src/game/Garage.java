package game;

import java.util.ArrayList;

import physics.CarProperties;
import save.Comptes;
import save.Profil;
import save.ProfilCurrent;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;

import de.lessvoid.nifty.controls.TextField;

public class Garage extends AbstractScreenController {
	
	private InputManager inputManager;
	
	private int rangCar;
	private CarProperties car;
	
	private TextField prixpuis;
	private TextField prixweight;
	private TextField prixnitro;
	
	private int ppuis;
	private int pweight;
	private int pnitro;
	
	private boolean hasnitro;
	private boolean haspuis;
	private boolean hasweight;
	
	private int poids;
	private int weight;
	
	public Garage() {
		super();
		rangCar = ProfilCurrent.getInstance().getChoixCar();
		car = ProfilCurrent.getInstance().getCar().get(rangCar);
		ppuis = 4000;
		pweight = 3000;
		pnitro = 5000;
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		super.initialize(stateManager, a);

		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);

		this.inputManager = app.getInputManager();
		
		prixpuis = screen.findNiftyControl("prixpuis", TextField.class);
		prixweight = screen.findNiftyControl("prixpoids", TextField.class);
		prixnitro = screen.findNiftyControl("prixnitro", TextField.class);
		
		prixpuis.setText(Integer.toString(ppuis));
		prixnitro.setText(Integer.toString(pnitro));
		prixweight.setText(Integer.toString(pweight));
		
		prixpuis.setEnabled(false);
		prixnitro.setEnabled(false);
		prixweight.setEnabled(false);
	}
	
	public void givenitro() {
		hasnitro=true;
		hasweight=false;
		haspuis=false;
	}
	
	public void takeoffweight() {
		hasnitro=false;
		hasweight=true;
		haspuis=false;
		int poids = 100;
		car.setWeight(car.getWeight()-poids);
	}
	
	public void givepuis() {
		hasnitro=false;
		hasweight=false;
		haspuis=true;
	}
	
	public void gotoChooseProfil() {
		app.gotoAffProfil();
	}
	
	public void Enregistrer() {
		if (hasnitro) {
			
		} else if (haspuis) {
			
		} else if (hasweight) {
			
		}
		Comptes.modifier(ProfilCurrent.getInstance());
		app.gotoAffProfil();
	}
}
