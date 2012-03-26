package ia;

import physics.CarProperties;
import physics.EnginePhysics;

public class IA {
	private EnginePhysics enginePhysics;
	private CarProperties carProperties;
	/**
	 * The bot will start trying to change gear at optimalShiftPoint-zone Thus,
	 * bigger the number, dumber the bot is!!
	 */
	private int zone = 100;
	/**
	 * Percentage to switch gear at the optimal shift point
	 */
	private double optimalShiftGearPercentage = 0.70;

	private long time = 0;
	private int delay = 50; // 50 ms delay

	public IA(EnginePhysics enginePhysics) {
		this.enginePhysics = enginePhysics;
		carProperties = enginePhysics.getCarProperties();
	}

	/**
	 * The probability that the bot will change gear. It depends upon a
	 * parabolic curve ranging from 0 to optimalShiftGearPercentage, where
	 * optimalShiftGearPercentage is the probability at the optimal shift point
	 * At optimalShiftPoint-zone there will be a null probability of changing
	 * gear At optimalShiftPoint there will be a optimalShiftGearPercentage
	 * probability of changing gear After optimShiftPoint+zone there will be a
	 * 100% probability of changing, ensuring that the bot does not stay stuck
	 * at one gear
	 * 
	 * @param rpm
	 * @param optimalShiftPoint
	 * @return Probability that the bot will change gear
	 */
	public double proba(double rpm, double optimalShiftPoint) {
		System.out.println("TIME: " + time);
		if (System.currentTimeMillis() - time >= delay) {
			time = System.currentTimeMillis();
			if (rpm < optimalShiftPoint - rpm)
				return 0.d;
			else
				return (rpm <= optimalShiftPoint + zone) ? optimalShiftGearPercentage
						* Math.cos(Math.PI / zone * (rpm - optimalShiftPoint))
						: 1.d;
		}
		return 0.d;
	}

	/**
	 * This method will make the bot change gear according to a parabolic
	 * probability curve. The closer it gets to the optimal shift point, the
	 * more probability there is that the bot will change gear. The probability
	 * depends upon: - The selected zone allowed to switch gear. It will only
	 * try to change between optimalShiftPoint-zone and optimalShiftPoint+rpm -
	 * The probability at optimalShiftPoint XXX: Non equiprobable !!! Plus de
	 * chance de changer de vitesse avant le point optimal qu'apr��s
	 */
	public void act() {
		int gear = enginePhysics.getGear();
		double optimalShiftPoint = carProperties.getOptimalShiftPoint(gear);
		double rpm = enginePhysics.getRpm();

		if (rpm >= optimalShiftPoint - zone) {
			int lower = 0;
			int higher = 100;

			int nb = (int) (100 * proba(rpm, optimalShiftPoint));

			int random = (int) (Math.random() * (higher - lower)) + lower;

			if (random < nb) {
				enginePhysics.incrementGear();
				System.err.println("Shifting to gear "
						+ enginePhysics.getGear() + " at RPM: " + rpm);
			}
		}
	}
}
