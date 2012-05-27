package game;

import java.util.ArrayList;

import physics.CarProperties;
import save.Comptes;
import save.Profil;
import save.ProfilCurrent;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;

public class Garage extends AbstractScreenController {
	
	private InputManager inputManager;
	
	private int rangCar;
	private CarProperties car;
	
	public Garage() {
		super();
		rangCar = ProfilCurrent.getInstance().getChoixCar();
		car = ProfilCurrent.getInstance().getCar().get(rangCar);
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		super.initialize(stateManager, a);

		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);

		this.inputManager = app.getInputManager();
	}
	
	public void givenitro() {
		
	}
	
	public void takeoffweight() {
		int poids = 100;
		car.setWeight(car.getWeight()-poids);
	}
	
	public void givepuis() {
		
	}
	
	public void gotoChooseProfil() {
		app.gotoAffProfil();
	}
	
	public void Enregistrer() {
		Comptes.modifier(ProfilCurrent.getInstance());
		app.gotoAffProfil();
	}
}
