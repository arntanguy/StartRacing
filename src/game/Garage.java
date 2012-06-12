package game;

import physics.CarProperties;
import save.Comptes;
import save.ProfilCurrent;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

import de.lessvoid.nifty.controls.TextField;

public class Garage extends AbstractScreenController {
	
	private InputManager inputManager;
	
	private final String msgdejaImprove = "VOUS AVEZ DEJA AMELIORER CE COMPOSANT !";
	private final String msgNoMoney = "VOUS N'AVEZ PAS ASSEZ D'ARGENT !";
	
	private int rangCar;
	private CarProperties car;
	private int playermonnaie;
	
	private TextField monnaie;
	private TextField prixpuis;
	private TextField prixweight;
	private TextField prixnitro;
	private TextField europuis;
	private TextField euronitro;
	private TextField europoids;
	private TextField msg;
	
	private int ppuis;
	private int pweight;
	private int pnitro;
	
	public Garage() {
		super();
		rangCar = ProfilCurrent.getInstance().getChoixCar();
		car = ProfilCurrent.getInstance().getCar().get(rangCar);
		playermonnaie = ProfilCurrent.getInstance().getMonnaie();
		ppuis = 4000;
		pweight = 3000;
		pnitro = 5000;
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		super.initialize(stateManager, a);

		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);
		
		/* keyboard mappings */
		setInputMappings();
		
		monnaie = screen.findNiftyControl("monnaie", TextField.class);
		prixpuis = screen.findNiftyControl("prixpuis", TextField.class);
		prixweight = screen.findNiftyControl("prixpoids", TextField.class);
		prixnitro = screen.findNiftyControl("prixnitro", TextField.class);
		europuis = screen.findNiftyControl("europuis", TextField.class);
		euronitro = screen.findNiftyControl("euronitro", TextField.class);
		europoids = screen.findNiftyControl("europoids", TextField.class);
		msg = screen.findNiftyControl("msg", TextField.class);
		
		monnaie.setText(Integer.toString(playermonnaie));
		prixpuis.setText(Integer.toString(ppuis));
		prixnitro.setText(Integer.toString(pnitro));
		prixweight.setText(Integer.toString(pweight));
		
		monnaie.setEnabled(false);
		prixpuis.setEnabled(false);
		prixnitro.setEnabled(false);
		prixweight.setEnabled(false);
		europuis.setEnabled(false);
		euronitro.setEnabled(false);
		europoids.setEnabled(false);
		msg.setEnabled(false);
	}
	
	private void setInputMappings() {
		inputManager.addMapping("return", new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addListener(new ActionListener() {
			@Override
			public void onAction(String arg0, boolean arg1, float arg2) {
				gotoChooseProfil();
			}
		}, "return");
		
		/*inputManager.addMapping("save", new KeyTrigger(KeyInput.KEY_LCONTROL));
		inputManager.addListener(new ActionListener() {
			@Override
			public void onAction(String arg0, boolean arg1, float arg2) {
				Enregistrer();
			}
		}, "save");*/
	}
	
	@Override
	public void onEndScreen() {
		inputManager.clearMappings();
		super.onEndScreen();
	}
	
	public void givenitro() {
		msg.setText("");
		if (car.isImprovenitro() == false) {
			if ( (playermonnaie - pnitro) > 0) {
				car.setImprovenitro(true);
				playermonnaie -= pnitro;
				ProfilCurrent.getInstance().setMonnaie((playermonnaie));
				monnaie.setText(Integer.toString(playermonnaie));
				Comptes.modifier(ProfilCurrent.getInstance());
				Comptes.Enregistrer();
			} else {
				msg.setText(msgNoMoney);
			}
		} else {
			msg.setText(msgdejaImprove);
		}
	}
	
	public void takeoffweight() {
		msg.setText("");
		if (car.isImproveweight() == false) {
			if ((playermonnaie - pweight) > 0) {
				int poids = 100;
				car.setWeight(car.getWeight()-poids);
				car.setImproveweight(true);
				playermonnaie -= pweight;
				ProfilCurrent.getInstance().setMonnaie(playermonnaie);
				monnaie.setText(Integer.toString(playermonnaie));
				Comptes.modifier(ProfilCurrent.getInstance());
				Comptes.Enregistrer();
			} else {
				msg.setText(msgNoMoney);
			}
		} else {
			msg.setText(msgdejaImprove);
		}
	}
	
	public void givepuis() {
		msg.setText("");
		if (car.isImprovepuis() == false) {
			if ( (playermonnaie - ppuis) > 0) {
				double puis = 1.5;
				car.setPuissance((int)(car.getPuissance() * puis));
				car.setImprovepuis(true);
				playermonnaie -= ppuis;
				ProfilCurrent.getInstance().setMonnaie((playermonnaie));
				monnaie.setText(Integer.toString(playermonnaie));
				Comptes.modifier(ProfilCurrent.getInstance());
				Comptes.Enregistrer();
			} else {
				msg.setText(msgNoMoney);
			}
		} else {
			msg.setText(msgdejaImprove);
		}
	}
	
	public void gotoChooseProfil() {
		app.gotoAffProfil();
	}
}
