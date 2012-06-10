package save;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import physics.CarProperties;

import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Comptes {
	
	private final static String NOMFICHIERXML = "ressources/save/Comptes.xml";
	private final static String CARXML = "ressources/save/Cars.xml";
	private static ArrayList<Profil> listProfil = new ArrayList<Profil>();
	private static ArrayList<CarProperties> listCar = new ArrayList<CarProperties>();	
	
	/*public Comptes () {
		listProfil = new ArrayList<Profil>();
		Recuperer();
	}*/
	
	public static int searchId () {
		int max = 0;
		for (int i = 0; i < listProfil.size(); ++i) {
			if (listProfil.get(i).getId() > max) {
				max = listProfil.get(i).getId();
			}
		}
		++max;
		return max;
	}
	
	public static boolean existLogin(String login) {
		for (int i = 0; i < listProfil.size(); ++i) {
			if (listProfil.get(i).getLogin().equals(login)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean Enregistrer () {
		//listProfil.add(profil);
		XStream xs = new XStream(new DomDriver());
		try {
		    FileOutputStream fs = new FileOutputStream(NOMFICHIERXML);
		    xs.toXML(listProfil, fs);
		} catch (FileNotFoundException e1) {
		    e1.printStackTrace();
		    return false;
		} 
		return true;
	}
	
	public static boolean Recuperer() {
		XStream xstream = new XStream(new DomDriver());
		listProfil = new ArrayList<Profil> ();
        try {
            FileInputStream fs= new FileInputStream(NOMFICHIERXML);
            listProfil = (ArrayList<Profil>) xstream.fromXML(fs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IndexOutOfBoundsException e) {
        	return false;
        }
		return true;
	}

	public static ArrayList<Profil> getListProfil() {
		return listProfil;
	}
	
	public static void addProfil(Profil profil) {
		listProfil.add(profil);
	}
	
	public static void modifier (Profil profil) {
		for (int i = 0; i < listProfil.size(); ++i) {
			if (listProfil.get(i).getId() == profil.getId()) {
				listProfil.get(i).setCar(profil.getCar());
				listProfil.get(i).setChoixCar(profil.getChoixCar());
				listProfil.get(i).setLogin(profil.getLogin());
				listProfil.get(i).setTimedemi(profil.getTimeDemi());
				listProfil.get(i).setTimequart(profil.getTimeQuart());
				listProfil.get(i).setTimefree(profil.getTimefree());
				listProfil.get(i).setCardead(profil.getCardead());
				listProfil.get(i).setMonnaie(profil.getMonnaie());
				listProfil.get(i).setLevel(profil.getLevel());
				listProfil.get(i).setLastchoose(profil.isLastchoose());
			}
		}
	}
	
	public static boolean RecupeCar() {
		XStream xstream = new XStream(new DomDriver());
		listCar = new ArrayList<CarProperties> ();
        try {
            FileInputStream fs= new FileInputStream(CARXML);
            listCar = (ArrayList<CarProperties>) xstream.fromXML(fs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IndexOutOfBoundsException e) {
        	return false;
        }
		return true;
	}
	
	public static boolean SaveCar() {
		XStream xstream = new XStream(new DomDriver());
		listCar = new ArrayList<CarProperties>  ();
        try {
            FileInputStream fs= new FileInputStream("save/Cars.xml");
            listCar = (ArrayList<CarProperties>) xstream.fromXML(fs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public static ArrayList<CarProperties> getListCar() { 
		return listCar;
	}
}
