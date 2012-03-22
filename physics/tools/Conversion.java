package physics.tools;

public class Conversion {
	// 1km = 0.62 miles
	/**
	 * Convert miles to km (or miles per hour to km per hour)
	 * @param mph
	 * @return
	 * 		Converted number in kilometers
	 */
	public double milesToKm(double mph) {
		return mph/0.62;
	}
	public static double kmToMiles(double f) {
		return f*0.62;
	}
}
