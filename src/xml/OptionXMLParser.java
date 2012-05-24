package xml;

import java.awt.Dimension;
import java.awt.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class OptionXMLParser {
	
	public static Dimension screenResolution = new Dimension(1024, 768);
	public static boolean sound = true;
	public static boolean wideScreen = false;
	private static XStream xmlStream = new XStream(new DomDriver());
		
	public OptionXMLParser() {}
	
	/**
	 * Charge les options de l'application depuis le fichier XML pointé par file.
	 * @param file
	 * 		Chemin vers le fichier xml d'options.
	 * @return	true si aucun problème, false sinon
	 */
	public static boolean loadAppOptions(String str_file) {
		try {
			FileInputStream in = new FileInputStream(XMLFileStore.OPTION_SAVE_FILE);
			List list = new List();
			list = (List) xmlStream.fromXML(in);
			
			screenResolution = (Dimension) xmlStream.fromXML(list.getItem(0));
			sound = (Boolean) xmlStream.fromXML(list.getItem(1));
			wideScreen = (Boolean) xmlStream.fromXML(list.getItem(2));
			
			return true;
		} catch (IOException e) {
			System.out.println("ERREUR: Fichier d'options introuvable.");
		}
		return false;
	}
	
	public static boolean saveAppOptions(String str_file) {
		try {
			FileOutputStream out = new FileOutputStream(XMLFileStore.OPTION_SAVE_FILE);
			List list = new List();
			
			list.add(xmlStream.toXML(screenResolution));
			list.add(xmlStream.toXML(sound));
			list.add(xmlStream.toXML(wideScreen));

			out.write(xmlStream.toXML(list).getBytes());
			
			return true;
		} catch (IOException e) {
			System.out.println("ERREUR: Fichier d'options introuvable.");
		}
		return false;
	}
}
