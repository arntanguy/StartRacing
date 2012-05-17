package save;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import physics.BMWM3Properties;
import physics.CarProperties;
import physics.DodgeViperProperties;
import physics.SkylineProperties;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*CarProperties bmw = new CarProperties();
		CarProperties dvp = new CarProperties();
		CarProperties skp = new CarProperties();
		ArrayList<CarProperties> listCar = new ArrayList<CarProperties>();
		listCar.add(bmw);
		listCar.add(dvp);
		listCar.add(skp);
		Profil joueur1 = new Profil(Comptes.searchId(), "joueur1", listCar, "", "", 0);
		Comptes.addProfil(joueur1);
		Profil joueur2 = new Profil(Comptes.searchId(), "joueur2", listCar, "", "", 0);
		Comptes.addProfil(joueur2);
		Profil joueur3 = new Profil(Comptes.searchId(), "joueur3", listCar, "", "", 0);
		Comptes.addProfil(joueur3);
		Comptes.Enregistrer();*/
		Comptes.Recuperer();
		//System.out.println(Comptes.getListProfil().get(1));
		ProfilCurrent pc = new ProfilCurrent(Comptes.getListProfil().get(1));
		ProfilCurrent.getInstance().setMonnaie(1000);
		Comptes.Enregistrer();
		
		
		/*Comptes compte = new Comptes();
		compte.Recuperer();
		ArrayList<Profil> listProfil = compte.getListProfil();
		ArrayList<CarProperties> listCar = new ArrayList<CarProperties>();
		CarProperties bmw = new CarProperties();
		listCar.add(bmw);
		Profil joueur1 = new Profil(compte.searchId(), "joueur1", listCar, "", "");
		profil = joueur1;*/
		
		//Enregistrement
		/*CarProperties car = new CarProperties();
		CarProperties bmw = new BMWM3Properties();
		CarProperties dvp = new DodgeViperProperties();
		CarProperties skp = new SkylineProperties();
		ArrayList<CarProperties> listCar = new ArrayList<CarProperties>();
		listCar.add(car);
		listCar.add(bmw);
		listCar.add(dvp);
		listCar.add(skp);
		XStream xs = new XStream(new DomDriver());
		try {
		    FileOutputStream fs = new FileOutputStream("save/Cars.xml");
		    xs.toXML(listCar, fs);
		} catch (FileNotFoundException e1) {
		    e1.printStackTrace();
		} */
		// Récupération
		
		/*XStream xstream = new XStream(new DomDriver());
		ArrayList<CarProperties> listVoiture = new ArrayList<CarProperties>  ();
        try {
            FileInputStream fs= new FileInputStream("save/Cars.xml");
            listVoiture = (ArrayList<CarProperties>) xstream.fromXML(fs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
		System.out.println(Comptes.getListProfil().get(1));
        
	}

}
