package xml;

import java.awt.Dimension;
import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
	public static boolean loadAppOptions(String str_file) {
		File file = new File(str_file);
		
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			doc.getDocumentElement().normalize();
			System.out.println("ROOT: " + doc.getDocumentElement().getNodeName() + ". ");
			
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public static boolean saveAppOptions(String str_file) {
		File file = new File(str_file);
		Element root = new Element(ROOT);
		
		return true;
	}
}
