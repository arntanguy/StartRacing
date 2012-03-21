package physics;

import java.util.ArrayList;

public class CarProperties {
	// tire height (24.5, 26, 27.5, ...cm)
	private double th = 24.5;
	// transmission final gear (2.54, 1.43)
	private double tgr = 2.5;
	
	private double idleRpm = 1000;
	/**
	 * Gear ratio in order 1, 2, ..., 6
	 */
	private ArrayList<Double> gearRatio;


	public double getIdleRpm() {
		return idleRpm;
	}

	public void setIdleRpm(double idleRpm) {
		this.idleRpm = idleRpm;
	}

	public CarProperties() {
		gearRatio = new ArrayList<Double>(6);
		gearRatio.add(3.827);
		gearRatio.add(2.36);
		gearRatio.add(1.685);
		gearRatio.add(1.312);
		gearRatio.add(1.d);
		gearRatio.add(0.793);
	}

	public CarProperties(double th, double tgr, double idleRpm) {
		this.th = th;
		this.tgr = tgr;
		this.idleRpm = idleRpm;
	}

	public double getTh() {
		return th;
	}

	public void setTh(double th) {
		this.th = th;
	}

	public double getTgr() {
		return tgr;
	}

	public void setTgr(double tgr) {
		this.tgr = tgr;
	}

	public double getGearRatio(int gear) {
		return gearRatio.get(gear-1);
	}

	public int getNbGears() {
		return gearRatio.size();
	}
}
