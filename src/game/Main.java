package game;

import save.Comptes;
import save.ProfilCurrent;

import com.jme3.app.SimpleApplication;

public class Main {

	public static void main(String[] args) {
		
		Comptes.Recuperer();
		Comptes.RecupeCar();
		ProfilCurrent.getInstance();
		
		java.util.logging.Logger.getLogger("de.lessvoid.nifty.*").setLevel(java.util.logging.Level.SEVERE);
		java.util.logging.Logger.getAnonymousLogger().getParent().setLevel(java.util.logging.Level.SEVERE);
		
		SimpleApplication app = new App();
		app.start();
	}
}
