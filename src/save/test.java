package save;

import physics.BMWM3Properties;
import physics.DodgeViperProperties;
import physics.SkylineProperties;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BMWM3Properties bmw = new BMWM3Properties();
		DodgeViperProperties dvp = new DodgeViperProperties();
		SkylineProperties skp = new SkylineProperties();
		Comptes compte = new Comptes();
		Profil joueur1 = new Profil(compte.searchId(), "joueur1", bmw, 0, "");
		compte.addProfil(joueur1);
		Profil joueur2 = new Profil(compte.searchId(), "joueur2", dvp, 0, "");
		compte.addProfil(joueur2);
		Profil joueur3 = new Profil(compte.searchId(), "joueur3", skp, 0, "");
		compte.addProfil(joueur3);
		System.out.println(compte.Enregistrer()? "Enregistrer" : "Erreur");
	}

}
