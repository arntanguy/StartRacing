package game;

import com.jme3.app.SimpleApplication;

public class Main {
	public static void main(String[] args) {
		
		java.util.logging.Logger.getLogger("de.lessvoid.nifty.*").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getAnonymousLogger().getParent().setLevel(java.util.logging.Level.OFF);
		
		SimpleApplication app = new App();
		app.start();
	}
}
