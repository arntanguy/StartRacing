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

public class ProfilsScreen extends AbstractScreenController {
	
	private InputManager inputManager;
	private final String ALLJOUEUR = "allJoueur";
	private ArrayList<Profil> dataAllJoueur;
	private DropDown<String> allJoueurDropDown;
	private String logchoose;
	
	public ProfilsScreen() {
		super();
		Comptes.Recuperer();
		dataAllJoueur = Comptes.getListProfil();
		for (int i = 0; i < dataAllJoueur.size(); ++i) {
			System.out.println(dataAllJoueur.get(i).getLogin());
		}
	}

	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);
		
		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);
	
		this.inputManager = app.getInputManager();
		
		allJoueurDropDown = screen.findNiftyControl(ALLJOUEUR, DropDown.class);
		
		fillResolutionDropDown(dataAllJoueur);
	}

	private void fillResolutionDropDown(ArrayList<Profil> allProfil) {
		for (int i = 0; i < allProfil.size(); ++i) {
			allJoueurDropDown.addItem(allProfil.get(i).getLogin());
		}
	}
	
	@NiftyEventSubscriber(id=ALLJOUEUR)
	public void onResolutionChange(final String id, final DropDownSelectionChangedEvent<String> event) {
		logchoose = event.getSelection();
	}

	public void Enregistrer() {
		ArrayList<Profil> listProfil = Comptes.getListProfil();
		for (int i = 0; i < listProfil.size(); ++i) {
			if (logchoose.equals(listProfil.get(i).getLogin())) {
				ProfilCurrent pc = new ProfilCurrent(Comptes.getListProfil().get(i));
				break;
			}
		}
		gotoMainMenu();
	}
	
	public void Achat() {
		
	}
	
	public void gotoMainMenu() {
		app.gotoStart();
	}
}
