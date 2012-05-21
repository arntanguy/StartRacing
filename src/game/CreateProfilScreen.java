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
	
	private TextField standtireHeight;
	private TextField standfinalGearRatio;
	private TextField standtireRadius;
	private TextField standweight;
	private TextField standstiffness;
	private TextField standmass;
	private TextField standidleRpm;
	private TextField standredline;
	
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
		
		standtireHeight = screen.findNiftyControl("StandtireHeight", TextField.class);
		standfinalGearRatio = screen.findNiftyControl("StandfinalGearRatio", TextField.class);
		standtireRadius = screen.findNiftyControl("StandtireRadius", TextField.class);
		standweight = screen.findNiftyControl("Standweight", TextField.class);
		standstiffness = screen.findNiftyControl("Standstiffness", TextField.class);
		standmass = screen.findNiftyControl("Standmass", TextField.class);
		standidleRpm = screen.findNiftyControl("StandidleRpm", TextField.class);
		standredline = screen.findNiftyControl("Standredline", TextField.class);
		
		AffDataCar();
		
        standtireHeight.setEnabled(false);
        standfinalGearRatio.setEnabled(false);
        standtireRadius.setEnabled(false);
        standweight.setEnabled(false);
        standstiffness.setEnabled(false);
        standmass.setEnabled(false);
        standidleRpm.setEnabled(false);
        standredline.setEnabled(false);
	}
	
	public void AffDataCar() {
		
		standtireHeight.setText(Double.toString(dataAllCar.get(0).getTireHeight()));
		standfinalGearRatio.setText(Double.toString(dataAllCar.get(0).getFinalGearRatio()));
		standtireRadius.setText(Double.toString(dataAllCar.get(0).getTireRadius()));
		standweight.setText(Integer.toString(dataAllCar.get(0).getWeight()));
		standstiffness.setText(Double.toString(dataAllCar.get(0).getStiffness()));
		standmass.setText(Double.toString(dataAllCar.get(0).getMass()));
		standidleRpm.setText(Integer.toString(dataAllCar.get(0).getIdleRpm()));
		standredline.setText(Integer.toString(dataAllCar.get(0).getRedline()));

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
