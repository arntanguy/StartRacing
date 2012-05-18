package game;

import java.util.ArrayList;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;

import de.lessvoid.nifty.controls.TextField;

import physics.CarProperties;
import save.Comptes;
import save.Profil;
import save.ProfilCurrent;

public class CreateProfilScreen extends AbstractScreenController {
	
	private InputManager inputManager;
	private TextField cinlogin;
	
	public CreateProfilScreen () {
		super();
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		super.initialize(stateManager, a);
		
		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);
	
		this.inputManager = app.getInputManager();
		cinlogin = screen.findNiftyControl("login", TextField.class);
	}
	
	public void gotoApply() {
		int id = Comptes.searchId();
		String login = cinlogin.getText();
		ArrayList<CarProperties> car = new ArrayList<CarProperties>();
		String timedemi = "";
		String timequart = "";
		String timefree = "";
		int cardead = 0;
		int monnaie = 0;
		Profil newprofil = new Profil(id, login, car, timedemi, timequart, timefree, cardead, monnaie);
		Comptes.addProfil(newprofil);
		Comptes.Enregistrer();
		ProfilCurrent pc = new ProfilCurrent(newprofil);
		gotoMainMenu();
	}
	
	public void gotoMainMenu() {
		app.gotoStart();
	}
}
