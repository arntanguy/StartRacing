package physics;

import java.util.TreeMap;

/*
 * name=BMW E46 M3
 gears=4.35 2.5 1.66 1.23 1.00 0.85
 final_gear_ratio=2.93
 redline=8000......
 weight=1570

 Best shift = 7996-7998
 */
public class BMWM3Properties extends CarProperties {
	public BMWM3Properties() {
		super();
		gears = new Gears();
		gears.setRatio(1, 4.35);
		gears.setRatio(2, 2.5);
		gears.setRatio(3, 1.66);
		gears.setRatio(4, 1.23d);
		gears.setRatio(5, 1.00);
		gears.setRatio(6, 0.85);
		gears.setOptimalShiftPoint(1, 7998);
		gears.setOptimalShiftPoint(2, 7999);
		gears.setOptimalShiftPoint(3, 7996);
		gears.setOptimalShiftPoint(4, 7996);
		gears.setOptimalShiftPoint(5, 7996);

		finalGearRatio = 2.93;
		tireRadius = 0.3266;

		weight = 1570;
		redline = 8500; // not real value

		torque = new TreeMap<Double, Double>();
		torque.put(0.d, 0.d);
		torque.put(1000.d, 400.d);
		torque.put(2000.d, 450.d);
		torque.put(3000.d, 480.d);
		torque.put(4000.d, 500.d);
		torque.put(5000.d, 550.d);
		torque.put(6000.d, 500.d);
		torque.put(8000.d, 450.d); // wrong value
		torque.put(15000.d, 0.d);
	}
}
