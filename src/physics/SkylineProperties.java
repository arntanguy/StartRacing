package physics;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * air density = 1.29kgm-3 mass = 1550kg red line = 8500rpm drag coefficient
 * (cd) = 0.34 (not an official number) height = 1.36m width = 1.785m frontal
 * area = height × width = 2.4276m2 (only an approximation) wheelbase = 2.665m
 * tire = 245/40ZR18 tire radius = 0.3266m (this number is calculated from the
 * tire spec) torque = 392Nm@4400rpm power = 206000W@6800rpm 1st gear ratio =
 * 3.827 2nd gear ratio = 2.36 3rd gear ratio = 1.685 4th gear ratio = 1.312 5th
 * gear ratio = 1 6th gear ratio = 0.793 reverse gear ratio = 3.28 final drive
 * ratio = 3.545 lateral acceleration = 0.94g on skidpad of 300ft radius (From
 * Road & Track magazine Feb 1999) weight distribution (front/rear) = 57%/43%
 * (From Road & Track magazine Feb 1999) minimum turning radius = 5.6m
 * 
 * @author arnaud
 * 
 */
public class SkylineProperties extends CarProperties {
	public SkylineProperties() {
		super();
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

		finalGearRatio = 3.545;
		tireRadius = 0.3266;*/

		/**
		 * TODO : put it in rpm
		 */
		/*torque = new TreeMap<Integer, Integer>();
		torque.put(0, 0);
		torque.put(75, 390);
		torque.put(140, 200);
		torque.put(1000, 50);
		torque.put(10000, 0);*/
		
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
		
		playerModel = "charger";
		availableModels.add("Models/FerrariGreen/Car.scene");
		availableModels.add("Models/FerrariBlue/Car.scene");
	
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
		
		typeCar = TypeCarProperties.SKYLINE;
	}
}
