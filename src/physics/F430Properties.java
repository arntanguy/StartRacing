package physics;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/*
 */
public class F430Properties extends CarProperties {
	public F430Properties() {
		super();
		gears = new Gears();
		gears.setRatio(1, 3.49);
		gears.setRatio(2, 2.16);
		gears.setRatio(3, 1.61);
		gears.setRatio(4, 1.27);
		gears.setRatio(5, 1.03);
		gears.setRatio(6, 0.87);
		gears.setOptimalShiftPoint(1, 7998);
		gears.setOptimalShiftPoint(2, 7999);
		gears.setOptimalShiftPoint(3, 7996);
		gears.setOptimalShiftPoint(4, 7996);
		gears.setOptimalShiftPoint(5, 7996);
		
		finalGearRatio = 3.0;
		tireRadius = 0.2313;

		weight = 1450;
		redline = 8640; 

		torque = new TreeMap<Integer, Integer>();
		torque.put(0, 0);
		torque.put(1000, 200);
		torque.put(2000, 340);
		torque.put(3000, 370);
		torque.put(4000, 440);
		torque.put(5000, 480);
		torque.put(6000, 470);
		torque.put(7000, 460);
		torque.put(8000, 450);
		torque.put(8700, 400); 
		torque.put(15000, 0);
		
		playerModel = "ferrari red";
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
		
		typeCar = TypeCarProperties.FERRARI;
	}
}
