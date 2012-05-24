package save;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import physics.BMWM3Properties;
import physics.CarProperties;
import physics.DodgeViperProperties;
import physics.SkylineProperties;
import physics.TypeCarProperties;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Enregistrement
		CarProperties car = new CarProperties();
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
		}
		
		CarProperties st = new CarProperties();
		CarProperties bmw1 = new BMWM3Properties();
		CarProperties dvp2 = new DodgeViperProperties();
		ArrayList<CarProperties> car1 = new ArrayList<CarProperties>();
		ArrayList<CarProperties> car2 = new ArrayList<CarProperties>();
		ArrayList<CarProperties> car3 = new ArrayList<CarProperties>();
		car1.add(st);
		car1.add(bmw1);
		car1.add(dvp2);
		car1.add(skp);
		car2.add(st);
		car3.add(st);
		Profil joueur1 = new Profil(Comptes.searchId(), "joueur1", car1, 0, "", "", "", 0, 30000);
		Comptes.addProfil(joueur1);
		Profil joueur2 = new Profil(Comptes.searchId(), "joueur2", car2, 0, "", "", "", 0, 150000);
		Comptes.addProfil(joueur2);
		Profil joueur3 = new Profil(Comptes.searchId(), "joueur3", car3, 0, "", "", "", 0, 1000000);
		Comptes.addProfil(joueur3);
		Comptes.Enregistrer();
		
		
		
		/*Comptes.Recuperer();
		//System.out.println(Comptes.getListProfil().get(1));
		ProfilCurrent pc = new ProfilCurrent(Comptes.getListProfil().get(1));
		ProfilCurrent.getInstance().setMonnaie(1000);
		Comptes.Enregistrer();*/
		
		
		/*Comptes compte = new Comptes();
		compte.Recuperer();
		ArrayList<Profil> listProfil = compte.getListProfil();
		ArrayList<CarProperties> listCar = new ArrayList<CarProperties>();
		CarProperties bmw = new CarProperties();
		listCar.add(bmw);
		Profil joueur1 = new Profil(compte.searchId(), "joueur1", listCar, "", "");
		profil = joueur1;*/
		

		// Récupération
		
		/*XStream xstream = new XStream(new DomDriver());
		ArrayList<CarProperties> listVoiture = new ArrayList<CarProperties>  ();
        try {
            FileInputStream fs= new FileInputStream("save/Cars.xml");
            listVoiture = (ArrayList<CarProperties>) xstream.fromXML(fs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		//System.out.println(Comptes.getListProfil().get(1));
        /*Comptes.RecupeCar();
        if (Comptes.getListCar().get(1) instanceof SkylineProperties)
        	System.out.println("coucou");*/

	
	}

}
