package save;

public class ProfilCurrent {

	private static Profil profilCourant;
	
	public ProfilCurrent (Profil profilcur) {
		profilCourant = profilcur;
	}
	
    public static Profil getInstance() {
        return profilCourant;
    }
}
