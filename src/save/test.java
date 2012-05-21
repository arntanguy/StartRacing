package save;

import java.util.ArrayList;

import physics.BMWM3Properties;
import physics.CarProperties;
import physics.DodgeViperProperties;
import physics.SkylineProperties;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CarProperties bmw = new CarProperties();
		CarProperties dvp = new CarProperties();
		CarProperties skp = new CarProperties();
		CarProperties bmw1 = new BMWM3Properties();
		CarProperties dvp2 = new DodgeViperProperties();
		ArrayList<CarProperties> car1 = new ArrayList<CarProperties>();
		ArrayList<CarProperties> car2 = new ArrayList<CarProperties>();
		ArrayList<CarProperties> car3 = new ArrayList<CarProperties>();
		car1.add(bmw);
		car1.add(bmw1);
		car1.add(dvp2);
		car2.add(dvp);
		car3.add(skp);
		Profil joueur1 = new Profil(Comptes.searchId(), "joueur1", car1, 0, "", "", "", 0, 0);
		Comptes.addProfil(joueur1);
		Profil joueur2 = new Profil(Comptes.searchId(), "joueur2", car2, 0, "", "", "", 0, 0);
		Comptes.addProfil(joueur2);
		Profil joueur3 = new Profil(Comptes.searchId(), "joueur3", car3, 0, "", "", "", 0, 0);
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
		}
		// Récupération
		
		XStream xstream = new XStream(new DomDriver());
		ArrayList<CarProperties> listVoiture = new ArrayList<CarProperties>  ();
        try {
            FileInputStream fs= new FileInputStream("save/Cars.xml");
            listVoiture = (ArrayList<CarProperties>) xstream.fromXML(fs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
		//System.out.println(Comptes.getListProfil().get(1));
        Comptes.RecupeCar();
        if (Comptes.getListCar().get(1) instanceof SkylineProperties)
        	System.out.println("coucou");
	}

}
