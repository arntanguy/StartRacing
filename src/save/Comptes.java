package save;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Comptes {
	
	private final String NOMFICHIERXML = "save/Comptes.xml";
	private ArrayList<Profil> listProfil;
	
	public Comptes () {
		listProfil = new ArrayList<Profil>();
		Recuperer();
	}
	
	public int searchId () {
		int max = 0;
		for (int i = 0; i < listProfil.size(); ++i) {
			if (listProfil.get(i).getId() > max) {
				max = listProfil.get(i).getId();
			}
		}
		++max;
		return max;
	}
	
	public boolean Enregistrer () {
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
	
	public boolean Recuperer() {
		XStream xstream = new XStream(new DomDriver());
		listProfil = new ArrayList<Profil> ();
        try {
            FileInputStream fs= new FileInputStream(NOMFICHIERXML);
            listProfil = (ArrayList<Profil>) xstream.fromXML(fs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }    
		return true;
	}

	public ArrayList<Profil> getListProfil() {
		return listProfil;
	}
	
	public void addProfil(Profil profil) {
		listProfil.add(profil);
	}
	
}
