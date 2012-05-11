package xml;

import de.lessvoid.nifty.Nifty;

public final class XMLFileStore {

	public static final String START_SCREEN_FILE = "Interface/Nifty/StartScreen.xml";
	public static final String OPTION_SCREEN_FILE = "Interface/Nifty/OptionScreen.xml";
	public static final String GAME_SCREEN_FILE = "Interface/Nifty/GameScreen.xml";
	
	public static final String OPTION_SAVE_FILE = "save/startracing_options.xml";
	
	/**
	 * Méthode de debug!
	 * Contrôle la validité des fichiers xml de l'interface graphique.\n
	 * Ne doit être exécutée que sur les fichiers d'interface graphique nifty-gui.
	 * @param nifty
	 * 		Objet Nifty
	 * @return true si aucun problème, false sinon
	 */
	public static boolean validateXMLFiles(Nifty nifty) {
		validateXML(nifty, XMLFileStore.GAME_SCREEN_FILE);
		validateXML(nifty, XMLFileStore.OPTION_SCREEN_FILE);
		validateXML(nifty, XMLFileStore.START_SCREEN_FILE);
		return true;
	}
	
	private static boolean validateXML(Nifty nifty, String file) {
    	try {
    		nifty.validateXml(file);
    	} catch(Exception e) {
    		System.out.println("XML Exception '"+ file + "': " + e.getMessage());
    		return false;
    	}
    	
    	return true;
    }
	
}
