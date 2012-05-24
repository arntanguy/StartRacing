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
	private ArrayList<CarProperties> dataAllCar;
	private TextField erreurlog;

	private TextField standweight;
	private TextField standpuis;
	
	public CreateProfilScreen () {
		super();
		dataAllCar = Comptes.getListCar();
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		super.initialize(stateManager, a);
		
		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);
	
		this.inputManager = app.getInputManager();
		Comptes.RecupeCar();
		dataAllCar = Comptes.getListCar();
		cinlogin = screen.findNiftyControl("login", TextField.class);
		erreurlog = screen.findNiftyControl("result", TextField.class);
		erreurlog.setEnabled(false);
		
		standweight = screen.findNiftyControl("Standweight", TextField.class);
		standpuis = screen.findNiftyControl("Standpuis", TextField.class);
		
		AffDataCar();

        standweight.setEnabled(false);
        standpuis.setEnabled(false);
	}
	
	public void AffDataCar() {
		
		standweight.setText(Integer.toString(dataAllCar.get(0).getWeight()));
		standpuis.setText(Double.toString(dataAllCar.get(0).getPuissance()));

	}
	
	public void gotoApply() {
		if (Comptes.existLogin(cinlogin.getText())) {
			cinlogin.setText("");
			erreurlog.setText("ERREUR, LE LOGIN EXISTE DEJA ! RECOMMENCEZ");
		}
		else if (cinlogin.getText().equals("")) {
			erreurlog.setText("ERREUR, LE LOGIN EST VIDE ! TAPEZ VOTRE NOM .");
		} else {
			erreurlog.setText("OK");
			int id = Comptes.searchId();
			String login = cinlogin.getText();
			ArrayList<CarProperties> cars = new ArrayList<CarProperties>();
			int choixCar = 0;
			cars.add(dataAllCar.get(0));
			String timedemi = "";
			String timequart = "";
			String timefree = "";
			int cardead = 0;
			int monnaie = 0;
			Profil newprofil = new Profil(id, login, cars, choixCar, timedemi, timequart, timefree, cardead, monnaie);
			Comptes.addProfil(newprofil);
			Comptes.Enregistrer();
			ProfilCurrent pc = new ProfilCurrent(newprofil);
			gotoMainMenu();
		}
	}
	
	public void gotoMainMenu() {
		app.gotoStart();
	}
}
