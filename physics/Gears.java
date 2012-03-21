package physics;

import java.util.Hashtable;

public class Gears {
	private Hashtable<Integer, Double> ratios;
	/**
	 * RPM of the best moment to change gears
	 */
	private Hashtable<Integer, Double> optimalShiftPoints;

	public Gears() {
		ratios = new Hashtable<Integer, Double>();
		optimalShiftPoints = new Hashtable<Integer, Double>();
	}

	public Gears(Hashtable<Integer, Double> ratio,
			Hashtable<Integer, Double> optimalShiftPoint) {
		this.ratios = ratio;
		this.optimalShiftPoints = optimalShiftPoint;
	}

	public void setRatios(Hashtable<Integer, Double> ratio) {
		this.ratios = ratio;
	}

	public void setOptimalShiftPoints(
			Hashtable<Integer, Double> optimalShiftPoints) {
		this.optimalShiftPoints = optimalShiftPoints;
	}

	public void setRatio(int gear, double ratio) {
		ratios.put(gear, ratio);
	}

	public void setOptimalShiftPoint(int gear, double optimalShiftPoint) {
		this.optimalShiftPoints.put(gear, optimalShiftPoint);
	}

	public double getRatio(int gear) {
		Double ratio = ratios.get(gear);
		return (ratio!=null)?ratio:0.d;
	}

	public double getOptimalShiftPoints(int gear) {
		Double os = optimalShiftPoints.get(gear);
		return (os!=null) ? os : 0;
	}

	public int getNbGears() {
		return ratios.size();
	}
}
