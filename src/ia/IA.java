package ia;

import physics.CarProperties;
import physics.EnginePhysics;

public class IA {
	public enum IALevel { NOOB, INTERMEDIATE, PRO, BOSS };
	private EnginePhysics enginePhysics;
	private CarProperties carProperties;
	/**
	 * The bot will start trying to change gear at optimalShiftPoint-zone Thus,
	 * bigger the number, dumber the bot is!!
	 */
	private int zone = 1000;

	/**
	 * Percentage to switch gear at the optimal shift point
	 */
	private double optimalShiftGearPercentage = 1.0;
	/**
	 * Probablility of changing gear after the redline is reached
	 */
	private double redlineShiftProba = 0.5;

	private long time = 0;
	private int delay = 50; // 50 ms delay

	public IA(EnginePhysics enginePhysics) {
		this.enginePhysics = enginePhysics;
		carProperties = enginePhysics.getCarProperties();
		
		setIALevel(IALevel.PRO);
	}
	
	public void setIALevel(IALevel level) {
		switch(level) {
		case NOOB:
			zone = 4000;
			optimalShiftGearPercentage = 0.5;
			redlineShiftProba = 0.5;
			break;
		case INTERMEDIATE:
			zone = 2000;
			optimalShiftGearPercentage = 0.75;
			redlineShiftProba = 0.6;
			break;
		case PRO:
			zone = 400;
			optimalShiftGearPercentage = 0.90;
			redlineShiftProba = 0.7;
			break;
		case BOSS:
			zone = 20;
			optimalShiftGearPercentage = 0.95;
			redlineShiftProba = 0.8;
			break;
		}
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

		if (rpm < optimalShiftPoint - rpm)
			return 0.d;
		else
			return (rpm <= optimalShiftPoint + zone) ? optimalShiftGearPercentage
					* Math.cos(Math.PI / zone * (rpm - optimalShiftPoint))
					: 1.d;
	}

	/**
	 * This method will make the bot change gear according to a parabolic
	 * probability curve. The closer it gets to the optimal shift point, the
	 * more probability there is that the bot will change gear. The probability
	 * depends upon: - The selected zone allowed to switch gear. It will only
	 * try to change between optimalShiftPoint-zone and optimalShiftPoint+rpm -
	 * The probability at optimalShiftPoint XXX: Non equiprobable !!! Plus de
	 * chance de changer de vitesse avant le point optimal qu'après
	 */
	public void act() {
		if (System.currentTimeMillis() - time >= delay) {
			time = System.currentTimeMillis();
			int gear = enginePhysics.getGear();
			double optimalShiftPoint = carProperties.getOptimalShiftPoint(gear);
			double rpm = enginePhysics.getRpm();

			if (rpm >= optimalShiftPoint - zone) {
				if (rpm <= carProperties.getRedline()) {

					if (isProba(proba(rpm, optimalShiftPoint))) {
						enginePhysics.incrementGear();
						System.out.println("Shifting to gear "
								+ enginePhysics.getGear() + " at RPM: " + rpm);
					}
				}

			}
			if (rpm > carProperties.getRedline() && isProba(redlineShiftProba)) {
				enginePhysics.incrementGear();
			}
		}
	}

	private boolean isProba(double proba) {
		int lower = 0;
		int higher = 100;

		int nb = (int) (100 * proba);

		int random = (int) (Math.random() * (higher - lower)) + lower;

		return random < nb;
	}
}
