package game;

import ia.IALevel;

import java.util.ArrayList;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

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

	private TextField weight;
	private TextField puis;
	private TextField nitro;
	
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
		
		/* Keyboard Mappings */
        setInputMappings();
        
		Comptes.RecupeCar();
		dataAllCar = Comptes.getListCar();
		cinlogin = screen.findNiftyControl("login", TextField.class);
		erreurlog = screen.findNiftyControl("result", TextField.class);
		erreurlog.setEnabled(false);
		
		weight = screen.findNiftyControl("weight", TextField.class);
		puis = screen.findNiftyControl("puis", TextField.class);
		nitro = screen.findNiftyControl("nitro", TextField.class);
		
		cinlogin.setText("");
		erreurlog.setText("");
		weight.setText("");
		puis.setText("");
		nitro.setText("");
		
		AffDataCar();

        weight.setEnabled(false);
        puis.setEnabled(false);
        nitro.setEnabled(false);
	}
	
	@Override
	public void onEndScreen() {
		inputManager.clearMappings();
		super.onEndScreen();
	}
	
	private void setInputMappings() {
		inputManager.addMapping("save", new KeyTrigger(KeyInput.KEY_LCONTROL));
		inputManager.addListener(new ActionListener() {
			@Override
			public void onAction(String arg0, boolean arg1, float arg2) {
				gotoApply();
			}
		}, "save");
		
		inputManager.addMapping("save", new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addListener(new ActionListener() {
			@Override
			public void onAction(String arg0, boolean arg1, float arg2) {
				gotoMainMenu();
			}
		}, "save");
	}
	
	public void AffDataCar() {
		
		weight.setText(Integer.toString(dataAllCar.get(0).getWeight()));
		puis.setText(Double.toString(dataAllCar.get(0).getPuissance()));
		nitro.setText((dataAllCar.get(0).isImprovenitro()) ? "Oui" : "Non");
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
			Profil newprofil = new Profil(id, login, cars, choixCar, timedemi, timequart, timefree, cardead, monnaie, IALevel.DEBUTANT, true);
			if (ProfilCurrent.getInstance() != null) {
				ProfilCurrent.getInstance().setLastchoose(false);
				Comptes.modifier(ProfilCurrent.getInstance());
			}
			Comptes.addProfil(newprofil);
			Comptes.Enregistrer();
			ProfilCurrent.setInstance(newprofil);
			gotoMainMenu();
		}
	}
	
	public void gotoMainMenu() {
		app.gotoStart();
	}
}
