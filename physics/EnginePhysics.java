package physics;

public class EnginePhysics {
	private CarProperties p;
	
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
		double rpm = p.getRgp()*d*336*p.getTgr()/p.getTh();
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
		return p.getTh()*rpm/(p.getRgp()*336*p.getTgr());
	}
}
