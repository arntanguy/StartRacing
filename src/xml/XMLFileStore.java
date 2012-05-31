package xml;

import de.lessvoid.nifty.Nifty;

public final class XMLFileStore {

	/* Nifty GUI */
	public static final String START_SCREEN_FILE = "Interface/Nifty/StartScreen.xml";
	public static final String OPTION_SCREEN_FILE = "Interface/Nifty/OptionScreen.xml";
	public static final String GAME_SCREEN_FILE = "Interface/Nifty/GameScreen.xml";
	public static final String FREE_4_ALL_FILE = "Interface/Nifty/FreeForAllScreen.xml";
	public static final String HALF_GAME_FILE = "Interface/Nifty/HalfGameScreen.xml";
	public static final String QUARTER_GAME_FILE = "Interface/Nifty/QuarterGameScreen.xml";
	public static final String CREATE_PROFIL_FILE = "Interface/Nifty/CreateProfil.xml";
	public static final String CHOOSE_PROFIL_FILE = "Interface/Nifty/ChooseProfil.xml";
	public static final String ACHAT_FILE = "Interface/Nifty/Achat.xml";
	public static final String GARAGE_FILE = "Interface/Nifty/Garage.xml";
	public static final String TUTORIEL_FILE = "Interface/Nifty/Tutoriel.xml";
	
	/* Custom XML */
	public static final String OPTION_SAVE_FILE = "ressources/save/startracing_options.xml";
	
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
