package physics;

import java.util.Hashtable;
import java.util.Iterator;

public class CarProperties {
	// tire height (24.5, 26, 27.5, ...cm)
	private double tireHeight = 24.5;
	// transmission final gear (2.54, 1.43) (final drive ratio)
	protected double finalGearRatio = 2.5;
	// Tire radius in meters
	protected double tireRadius = 0.3;

	protected double idleRpm = 1000;
	/**
	 * Gear ratio and properties
	 */
	protected Gears gears;

	/**
	 * Torque corresponding to given engine speed <Engine speed, Torque>
	 */
	protected Hashtable<Double, Double> torque;

	public double getTireRadius() {
		return tireRadius;
	}

	public void setTireRadius(double tireRadius) {
		this.tireRadius = tireRadius;
	}


	public double getIdleRpm() {
		return idleRpm;
	}

	public void setIdleRpm(double idleRpm) {
		this.idleRpm = idleRpm;
	}

	public CarProperties() {
		gears = new Gears();
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


		torque = new Hashtable<Double, Double>();
		torque.put(0.d, 0.d);
		torque.put(75.d, 390.d);
		torque.put(140.d, 200.d);
	}

	public CarProperties(double th, double tgr, double idleRpm) {
		this.tireHeight = th;
		this.finalGearRatio = tgr;
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

	public double getTorque(double engineSpeed) {
		double w1 = 0;
		double w2 = 0;
		double t1 = 0;
		double t2 = 0;
		Iterator<Double> it = torque.keySet().iterator();

		while(it.hasNext()) {
			Double w = it.next();
			if(w > torque.get(w)) {
				w1 = w;
				t1 = torque.get(w);
				if(it.hasNext()) {
					w2 = it.next(); 
					t2 = torque.get(it.next());
					return (engineSpeed-w1) * (t2-t1)/(w2-w1);
				}
				else {
					return 0;
				}
			}
		}
		return 0;
	}

	public double getOptimalShiftPoint(int gear) {
		return gears.getOptimalShiftPoints(gear);
	}
}
