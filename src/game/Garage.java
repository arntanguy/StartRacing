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

		this.inputManager = app.getInputManager();
		
		prixpuis = screen.findNiftyControl("prixpuis", TextField.class);
		prixweight = screen.findNiftyControl("prixpoids", TextField.class);
		prixnitro = screen.findNiftyControl("prixnitro", TextField.class);
		europuis = screen.findNiftyControl("europuis", TextField.class);
		euronitro = screen.findNiftyControl("euronitro", TextField.class);
		europoids = screen.findNiftyControl("europoids", TextField.class);
		msg = screen.findNiftyControl("msg", TextField.class);
		
		prixpuis.setText(Integer.toString(ppuis));
		prixnitro.setText(Integer.toString(pnitro));
		prixweight.setText(Integer.toString(pweight));
		
		prixpuis.setEnabled(false);
		prixnitro.setEnabled(false);
		prixweight.setEnabled(false);
		europuis.setEnabled(false);
		euronitro.setEnabled(false);
		europoids.setEnabled(false);
		msg.setEnabled(false);
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
		String msgdejaImprove = "Vous avez déjà améliorer ce composant !";
		String msgNoMoney = "Vous n'avez pas assez d'argent !";
		if (hasnitro) {
			if (car.isImprovenitro() == false) {
				if ( (ProfilCurrent.getInstance().getMonnaie() - pnitro) > 0) {
					car.setImprovenitro(true);
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
