package physics;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import physics.tools.MathTools;

/**
 * Holds the main properties of the car : torque map, redlines, weight...
 * @author TANGUY Arnaud
 *
 */
public class CarProperties {
	// tire height (24.5, 26, 27.5, ...cm)
	private double tireHeight = 24.5;
	// transmission final gear (2.54, 1.43) (final drive ratio)
	protected double finalGearRatio = 2.5;
	/**
	 * Tire radius in meters
	 */
	protected double tireRadius = 0.3;

	// Weight in kg
	protected int weight = 1552;
	protected float stiffness = 120.0f;// 200=f1 car
	protected float compValue = 0.2f; // (lower than damp!)
	protected float dampValue = 0.3f;
	protected float mass = 1200;

	protected int idleRpm = 1000;
	/**
	 * Gear ratio and properties
	 */
	protected Gears gears;

	protected int redline = 6000;

	protected String playerModel;
	protected ArrayList<String> availableModels;

	protected TypeCarProperties typeCar;
	
	protected int puissance;
	
	protected boolean improvepuis = false;
	protected boolean improvenitro = false;
	protected boolean improveweight = false;
	
	/**
	 * Torque corresponding to given engine speed <Engine speed, Torque> ordered
	 * by key
	 */
	protected TreeMap<Integer, Integer> torque;

	public CarProperties() {
		/*gears = new Gears();
		gears.setRatio(1, 3.827);
		gears.setRatio(2, 2.36);
		gears.setRatio(3, 1.685);
		gears.setRatio(4, 1.312);
		gears.setRatio(5, 1.d);
		gears.setRatio(6, 0.793);
		gears.setOptimalShiftPoint(1, 8387.29);
		gears.setOptimalShiftPoint(2, 7911.1);
		gears.setOptimalShiftPoint(3, 7623.54);
		gears.setOptimalShiftPoint(4, 7694.66);
		gears.setOptimalShiftPoint(5, 7562.64);

		torque = new TreeMap<Integer, Integer>();
		torque.put(0, 0);
		torque.put(75, 390);
		torque.put(140, 200);*/
		
		gears = new Gears();
		gears.setRatio(1, 2.66);
		gears.setRatio(2, 1.78);
		gears.setRatio(3, 1.30);
		gears.setRatio(4, 1.d);
		gears.setRatio(5, 0.74);
		gears.setRatio(6, 0.5);
		gears.setOptimalShiftPoint(1, 5000.29);
		gears.setOptimalShiftPoint(2, 5000.1);
		gears.setOptimalShiftPoint(3, 5000.54);
		gears.setOptimalShiftPoint(4,5000.66);
		gears.setOptimalShiftPoint(5, 5000.64);

		finalGearRatio = 3.07;
		tireRadius = 0.3266;
		weight = 1552;

		torque = new TreeMap<Integer, Integer>();
		torque.put(0, 0);
		torque.put(1000, 400);
		torque.put(2000, 450);
		torque.put(3000, 480);
		torque.put(4000, 500);
		torque.put(5000, 550);
		torque.put(6000, 500);
		torque.put(8000, 450); // wrong value
		torque.put(15000, 0);
		
		Set cles = torque.keySet();
		Iterator iterator = cles.iterator();
		Object n = iterator.next();
		int couple = torque.get(n);
		while (iterator.hasNext()) {
			Object newn = iterator.next();
			int newcouple = torque.get(newn);
			if (newcouple > couple) {
				couple = newcouple;
				n = newn;
			}
		}
		int tourmin = Integer.parseInt(n.toString());
		DecimalFormat df = new DecimalFormat ( ) ;
		df.setMaximumFractionDigits ( 2 ) ; 
		puissance = (int)(couple * tourmin * (Math.PI/30) / 736);

		availableModels = new ArrayList<String>();
		
		//typeCar = TypeCarProperties.STANDARD;
		
		playerModel = "Models/FerrariRed/Car.scene";
		availableModels.add("Models/FerrariGreen/Car.scene");
		availableModels.add("Models/FerrariBlue/Car.scene");
	}

	public CarProperties(double th, double tgr, int idleRpm) {
		this.tireHeight = th;
		this.finalGearRatio = tgr;
		this.idleRpm = idleRpm;
	}

	public String getPlayerModel()	{
		return playerModel;
	}
	
	public double getTireRadius() {
		return tireRadius;
	}

	public void setTireRadius(double tireRadius) {
		this.tireRadius = tireRadius;
	}

	public int getIdleRpm() {
		return idleRpm;
	}

	public void setIdleRpm(int idleRpm) {
		this.idleRpm = idleRpm;
	}

	public double getTh() {
		return tireHeight;
	}

	public void setTh(double th) {
		this.tireHeight = th;
	}

	public double getTgr() {
		return finalGearRatio;
	}

	public void setTgr(double tgr) {
		this.finalGearRatio = tgr;
	}

	public void setGearRatio(Gears gears) {
		this.gears = gears;
	}

	public double getGearRatio(int gear) {
		return gears.getRatio(gear);
	}

	public int getNbGears() {
		return gears.getNbGears();
	}

	/**
	 * Calculates the torque corresponding to a given rpm
	 * 
	 * @param rpm
	 *            Rotations per Minute
	 * @return A linear approximation of the torque
	 **/
	public double getTorque(double rpm) {
		/**
		 * The values constituting the torque curve are discrete. Gets the
		 * closer values to the given rpm, so that w1 <= rpm <= w2 That way, we
		 * can do a linear interpolation to estimate the value of the torque at
		 * any given rpm. This is achieved by the following : Torque ~= t1 +
		 * (rpm-w1) * (t2-t1)/(w2-w1)
		 */

		double t1 = 0;
		double t2 = 0;

		Iterator<Integer> it = torque.keySet().iterator();

		int w2 = 0;
		int w1 = 0;
		int wt = 0;
		while (it.hasNext() && rpm - w1 >= 0) {
			wt = w1;
			w1 = it.next();
		}
		w2 = w1;
		w1 = wt;

		// System.out.println("RMP (" + rpm + ") > value(" + w1 + "," + w2
		// + ") : get torque (" + torque.get(w1) + "," + torque.get(w2)
		// + ")");
		t1 = torque.get(w1);
		if (rpm <= w2) {
			t2 = torque.get(w2);
			// System.out.println("torque = " + t1 + (rpm - w1) * (t2 - t1)
			// / (w2 - w1));
			return t1 + (rpm - w1) * (t2 - t1) / (w2 - w1);
		} else {
			return 0;
		}
	}

	/**
	 * Gets the value of the optimal shift point. It is the RPM of the best
	 * moment to go to the next gear.
	 * 
	 * @param gear
	 *            The gear for which you want the optimal shift point.
	 * @return The value of the optimal shift point (in RPM)
	 */
	public double getOptimalShiftPoint(int gear) {
		return gears.getOptimalShiftPoints(gear);
	}

	public double getTireHeight() {
		return tireHeight;
	}

	public void setTireHeight(double tireHeight) {
		this.tireHeight = tireHeight;
	}

	public float getStiffness() {
		return stiffness;
	}

	public void setStiffness(float stiffness) {
		this.stiffness = stiffness;
	}

	public float getCompValue() {
		return compValue;
	}

	public void setCompValue(float compValue) {
		this.compValue = compValue;
	}

	public float getDampValue() {
		return dampValue;
	}

	public void setDampValue(float dampValue) {
		this.dampValue = dampValue;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = (mass >= 0) ? mass : 0.f;
	}

	public int getRedline() {
		return redline;
	}

	public ArrayList<String> getAvailableModels() {
		return availableModels;
	}

	public String getRandomModel() {
		int rand = MathTools.randBetween(0, availableModels.size());
		return availableModels.get(rand);
	}

	public double getFinalGearRatio() {
		return finalGearRatio;
	}

	public void setFinalGearRatio(double finalGearRatio) {
		this.finalGearRatio = finalGearRatio;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public TypeCarProperties getTypeCar() {
		return typeCar;
	}

	public int getPuissance() {
		return puissance;
	}

	public void setPuissance(int puissance) {
		this.puissance = puissance;
	}

	public boolean isImprovepuis() {
		return improvepuis;
	}

	public void setImprovepuis(boolean improvepuis) {
		this.improvepuis = improvepuis;
	}

	public boolean isImprovenitro() {
		return improvenitro;
	}

	public void setImprovenitro(boolean improvenitro) {
		this.improvenitro = improvenitro;
	}

	public boolean isImproveweight() {
		return improveweight;
	}

	public void setImproveweight(boolean improveweight) {
		this.improveweight = improveweight;
	}
	
}
