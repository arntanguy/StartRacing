package game;

import java.util.ArrayList;

import save.Comptes;
import save.Profil;
import save.ProfilCurrent;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextField;

public class ProfilsScreen extends AbstractScreenController {
	
	private InputManager inputManager;
	private final String ALLJOUEUR = "allJoueur";
	private ArrayList<Profil> dataAllJoueur;
	private DropDown<String> allJoueurDropDown;
	//private TextField car;
	private TextField demi;
	private TextField quart;
	private TextField free;
	private TextField deadcarfree;
	private TextField monnaie;
	private String logchoose;
	
	public ProfilsScreen() {
		super();
		dataAllJoueur = Comptes.getListProfil();
	}

	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);
		
		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);
	
		this.inputManager = app.getInputManager();
		
		allJoueurDropDown = screen.findNiftyControl(ALLJOUEUR, DropDown.class);
		//car = screen.findNiftyControl("car", TextField.class);
		demi = screen.findNiftyControl("demi", TextField.class);
		quart = screen.findNiftyControl("quart", TextField.class);
		free = screen.findNiftyControl("free", TextField.class);
		deadcarfree = screen.findNiftyControl("cardead", TextField.class);
		monnaie = screen.findNiftyControl("monnaie", TextField.class);
		
		AffBase();
	}

	private void AffBase() {
		for (int i = 0; i < dataAllJoueur.size(); ++i) {
			allJoueurDropDown.addItem(dataAllJoueur.get(i).getLogin());
			System.out.println(dataAllJoueur.get(i).getLogin());
		}
		//car.setText(dataAllJoueur.get(0).getCar());
		demi.setText(dataAllJoueur.get(0).getTimeDemi());
		quart.setText(dataAllJoueur.get(0).getTimeQuart());
		free.setText(dataAllJoueur.get(0).getTimefree());
		deadcarfree.setText(Integer.toString(dataAllJoueur.get(0).getCardead()));
		monnaie.setText(Integer.toString(dataAllJoueur.get(0).getMonnaie()));
	}
	
	@NiftyEventSubscriber(id=ALLJOUEUR)
	public void onResolutionChange(final String id, final DropDownSelectionChangedEvent<String> event) {
		logchoose = event.getSelection();
		for (int i = 0; i < dataAllJoueur.size(); ++i) {
			if (logchoose.equals(dataAllJoueur.get(i).getLogin())) {
				//car.setText(dataAllJoueur.get(i).getCar());
				demi.setText(dataAllJoueur.get(i).getTimeDemi());
				quart.setText(dataAllJoueur.get(i).getTimeQuart());
				free.setText(dataAllJoueur.get(i).getTimefree());
				deadcarfree.setText(Integer.toString(dataAllJoueur.get(i).getCardead()));
				monnaie.setText(Integer.toString(dataAllJoueur.get(i).getMonnaie()));
				break;
			}
		}
	}

	public void Enregistrer() {
		for (int i = 0; i < dataAllJoueur.size(); ++i) {
			if (logchoose.equals(dataAllJoueur.get(i).getLogin())) {
				ProfilCurrent pc = new ProfilCurrent(Comptes.getListProfil().get(i));
				break;
			}
		}
		gotoMainMenu();
	}
	
	public void Remove() {
		for (int i = 0; i < dataAllJoueur.size(); ++i) {
			if (logchoose.equals(dataAllJoueur.get(i).getLogin())) {
				dataAllJoueur.remove(i);
				Comptes.Enregistrer();
				break;
			}
		}
		allJoueurDropDown = null;
		nifty.addXml("Interface/Nifty/ChooseProfil.xml");
        nifty.gotoScreen("chooseprofil");
        ProfilsScreen pfscreen = (ProfilsScreen) nifty.getCurrentScreen().getScreenController();
        stateManager.attach(pfscreen);
	}
	
	public void Achat() {
		app.gotoAchat();
	}
	
	public void gotoMainMenu() {
		app.gotoStart();
	}
}
