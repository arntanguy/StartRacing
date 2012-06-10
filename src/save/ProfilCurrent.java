package save;

import java.util.ArrayList;

public class ProfilCurrent {

	private static Profil profilCourant;
	
    public static Profil getInstance() {
    	if (profilCourant == null) {
    		ArrayList<Profil> listProfil = Comptes.getListProfil();
    		for (int i = 0; i < listProfil.size(); ++i) {
    			if (listProfil.get(i).isLastchoose()) {
    				profilCourant = listProfil.get(i);
    			}
    		}
    	}
        return profilCourant;
    }
    
    public static void setInstance(Profil profilcur) {
    	profilCourant = profilcur;
    }
}
