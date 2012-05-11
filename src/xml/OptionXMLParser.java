package xml;

import java.awt.Dimension;

public class OptionXMLParser {
	private static final String ROOT = "StartRacingOptions";
	private static final String SOUND_ELEMENT = "sound";
	private static final String WIDE_SCREEN_ELEMENT = "wideScreen";
	private static final String RESOLUTION_ELEMENT = "resolution";
	private static final String WIDTH_ATTRIBUTE = "width";
	private static final String HEIGHT_ATTRIBUTE = "height";
	
	public static Dimension screenResolution = new Dimension(1024, 768);
	public static boolean sound = true;
	public static boolean wideScreen = false;
	
	public OptionXMLParser() {}
	
	/**
	 * Charge les options de l'application depuis le fichier XML pointé par file.
	 * @param file
	 * 		Chemin vers le fichier xml d'options.
	 * @return	true si aucun problème, false sinon
	 */
	public static boolean loadAppOptions(String file) {
		
		return true;
	}
	
	public static boolean saveAppOptions(String file) {
		return true;
	}
}
