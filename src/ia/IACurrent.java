package ia;

public class IACurrent {
	
	private static IALevel IALevelCourant;
	
	public IACurrent (IALevel IALevelCourant) {
		this.IALevelCourant = IALevelCourant;
	}
	
    public static IALevel getInstance() {
        return IALevelCourant;
    }
}
