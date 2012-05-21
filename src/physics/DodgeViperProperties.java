package physics;

import java.util.Hashtable;
import java.util.TreeMap;

public class DodgeViperProperties extends CarProperties {
	public DodgeViperProperties() {
		super();
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
	}

}
