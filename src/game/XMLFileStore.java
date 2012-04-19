package game;

import de.lessvoid.nifty.Nifty;

public final class XMLFileStore {

	public static final String START_SCREEN_FILE = "Interface/Nifty/StartScreen.xml";
	public static final String OPTION_SCREEN_FILE = "Interface/Nifty/OptionScreen.xml";
	public static final String GAME_SCREEN_FILE = "Interface/Nifty/GameScreen.xml";
	
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
