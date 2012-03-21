package physics;

public class EnginePhysics {
	private CarProperties p;
	
	/**
	 * Current selected gear (vitesse)
	 */
	private int gear=1;
	
	public EnginePhysics(CarProperties prop) {
		this.p = prop;
	}
	
	/**
	 * 
	 * @param d
	 * 		miles per hour
	 * @return
	 * 		rotation per minute
	 */
	public double getRpm(double d) {
		double rpm = p.getGearRatio(gear)*d*336*p.getTgr()/p.getTh();
		return (rpm<=p.getIdleRpm()) ? p.getIdleRpm() : rpm; 
	}
	
	/**
	 * 
	 * @param rpm
	 * 		rotation per minute
	 * @return
	 * 		miles per hour
	 */
	public double getMph(double rpm) {
		return p.getTh()*rpm/(p.getGearRatio(gear)*336*p.getTgr());
	}
	
	public void setGear(int gear) {
		this.gear = gear;
	}
	
	public int getGear() {
		return gear;
	}

	public void incrementGear() {
		if(gear < p.getNbGears()) {
			this.gear++;
		}
	}
	
	public void decrementGear() {
		if(gear>1) {
			this.gear--;
		}
	}
}
