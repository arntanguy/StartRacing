package physics;

/**
 * EnginePhysics is meant to simulate the workings of a gearbox. It can
 * calculate the force generated by an engine according to various parameters,
 * such as speed and rotations per minute.
 * 
 * @author TANGUY Arnaud
 * 
 */
public class EnginePhysics {
	private CarProperties p;

	/**
	 * Current selected gear (vitesse) 0 : reverse
	 */
	private int gear = 1;
	private double speed = 0;

	public EnginePhysics(CarProperties prop) {
		this.p = prop;
	}

	/**
	 * The speed must be up to date to get accurate value
	 * 
	 * @return rotation per minute
	 */
	public int getRpm() {
		int rpm = (int) (p.getGearRatio(gear) * speed * 336 * p.getTgr()
				/ p.getTh());
		return (rpm <= p.getIdleRpm()) ? p.getIdleRpm() : rpm;
	}

	/**
	 * 
	 * @param rpm
	 *            rotation per minute
	 * @return miles per hour
	 */
	public double getMph(double rpm) {
		return p.getTh() * rpm / (p.getGearRatio(gear) * 336 * p.getTgr());
	}

	public void setGear(int gear) {
		if (gear >= 0)
			this.gear = gear;
	}

	public int getGear() {
		return gear;
	}

	/**
	 * Go to the next gear, if it exists, otherwise do nothing
	 */
	public void incrementGear() {
		if (gear < p.getNbGears()) {
			this.gear++;
		}
	}

	/**
	 * Go to the previous gear if it exists, otherwise do nothing
	 */
	public void decrementGear() {
		if (gear > 1) {
			this.gear--;
		}
	}

	/**
	 * Sets the vehicule speed.
	 * 
	 * @param speed
	 *            The speed in mph
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * Gets the speed of the internal wheel of the engine
	 * 
	 * @return The engine speed
	 */
	public double getEngineSpeed() {
		/**
		 * v = 2��r��/(G*gk) w = v*G*gk/(2*pi*r)
		 */
		return speed * p.getTgr() * p.getGearRatio(gear)
				/ (2 * Math.PI * p.getTireRadius());
	}

	/**
	 * Force generated by the engine at a given velocity with a given gear ratio
	 * To have an accurate result, the speed and gear ratio must be up to date,
	 * see the setSpeed and setGear function
	 * 
	 * @return The force generated (in Newtons)
	 */
	public double getForce() {
		/**
		 * v = 2��r��/Ggk, where v = velocity of the car (ms-1) r = radius of tire
		 * (m) �� = engine speed in rotations per second (s-1) G = final drive
		 * ratio (no unit) gk = k-th gear ratio (no unit)
		 * 
		 * ��(��)G*gk 1 F = -------- ��� _ crrmg ��� -- cdA��v2, where r 2
		 **/
	//	System.out.println("Engine speed: " + getEngineSpeed() + " rpm: "
	//			+ getRpm());
		return (getRpm()<p.getRedline()) ? p.getTorque(getRpm()) * p.getTgr() * p.getGearRatio(gear)
				/ p.getTireRadius():-1000;
	}

	public CarProperties getCarProperties() {
		return p;
	}
}