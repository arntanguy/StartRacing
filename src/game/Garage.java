package game;

import java.util.ArrayList;

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
	
	private int rangCar;
	private CarProperties car;
	
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
	
	private boolean hasnitro;
	private boolean haspuis;
	private boolean hasweight;
	
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
		
		monnaie.setText(Integer.toString(ProfilCurrent.getInstance().getMonnaie()));
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
		
		inputManager.addMapping("save", new KeyTrigger(KeyInput.KEY_LCONTROL));
		inputManager.addListener(new ActionListener() {
			@Override
			public void onAction(String arg0, boolean arg1, float arg2) {
				Enregistrer();
			}
		}, "save");
	}
	
	@Override
	public void onEndScreen() {
		inputManager.clearMappings();
		super.onEndScreen();
	}
	
	public void givenitro() {
		hasnitro=true;
		hasweight=false;
		haspuis=false;
		msg.setText("");
	}
	
	public void takeoffweight() {
		hasnitro=false;
		hasweight=true;
		haspuis=false;
		msg.setText("");
	}
	
	public void givepuis() {
		hasnitro=false;
		hasweight=false;
		haspuis=true;
		msg.setText("");
	}
	
	public void gotoChooseProfil() {
		app.gotoAffProfil();
	}
	
	public void Enregistrer() {
		String msgdejaImprove = "VOUS AVEZ DEJA AMELIORER CE COMPOSANT !";
		String msgNoMoney = "VOUS N'AVEZ PAS ASSEZ D'ARGENT !";
		if (hasnitro) {
			if (car.isImprovenitro() == false) {
				if ( (ProfilCurrent.getInstance().getMonnaie() - pnitro) > 0) {
					car.setImprovenitro(true);
					ProfilCurrent.getInstance().setMonnaie((ProfilCurrent.getInstance().getMonnaie() - pnitro));
					Comptes.modifier(ProfilCurrent.getInstance());
					app.gotoAffProfil();
				} else {
					msg.setText(msgNoMoney);
				}
			} else {
				msg.setText(msgdejaImprove);
			}
		} else if (haspuis) {
			if (car.isImprovepuis() == false) {
				if ( (ProfilCurrent.getInstance().getMonnaie() - ppuis) > 0) {
					double puis = 1.5;
					car.setPuissance((int)(car.getPuissance() * puis));
					car.setImprovepuis(true);
					ProfilCurrent.getInstance().setMonnaie((ProfilCurrent.getInstance().getMonnaie() - ppuis));
					Comptes.modifier(ProfilCurrent.getInstance());
					app.gotoAffProfil();
				} else {
					msg.setText(msgNoMoney);
				}
			} else {
				msg.setText(msgdejaImprove);
			}
		} else if (hasweight) {
			if (car.isImproveweight() == false) {
				if ((ProfilCurrent.getInstance().getMonnaie() - pweight) > 0) {
					int poids = 100;
					car.setWeight(car.getWeight()-poids);
					car.setImproveweight(true);
					ProfilCurrent.getInstance().setMonnaie((ProfilCurrent.getInstance().getMonnaie() - pweight));
					Comptes.modifier(ProfilCurrent.getInstance());
					app.gotoAffProfil();
				} else {
					msg.setText(msgNoMoney);
				}
			} else {
				msg.setText(msgdejaImprove);
			}
		}
	}
}
