package ia;

import game.Car;
import physics.CarProperties;
import physics.EnginePhysics;
import physics.tools.MathTools;
import save.ProfilCurrent;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class IA {

	private EnginePhysics enginePhysics;
	private CarProperties carProperties;
	private Car iaCar;

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

	private long followTimer = 0;

	public IA(Car car, EnginePhysics enginePhysics) {
		this.iaCar = car;
		this.enginePhysics = enginePhysics;
		carProperties = enginePhysics.getCarProperties();

		setIALevel( (ProfilCurrent.getInstance() == null) ? 
						IALevel.PROFESSIONNEL : 
							ProfilCurrent.getInstance().getLevel());
	}

	public void setIALevel(IALevel level) {
		switch (level) {
			case DEBUTANT:
				zone = 4000;
				optimalShiftGearPercentage = 0.5;
				redlineShiftProba = 0.5;
				break;
			case INTERMEDIAIRE:
				zone = 2000;
				optimalShiftGearPercentage = 0.75;
				redlineShiftProba = 0.6;
				break;
			case PROFESSIONNEL:
				zone = 400;
				optimalShiftGearPercentage = 0.90;
				redlineShiftProba = 0.7;
				break;
			case EXPERT:
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

	private boolean isProba(double proba) {
		int lower = 0;
		int higher = 100;

		int nb = (int) (100 * proba);

		int random = MathTools.randBetween(lower, higher);

		return random < nb;
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
		if (iaCar.isAlive()) {
			if (System.currentTimeMillis() - time >= delay) {
				time = System.currentTimeMillis();
				int gear = enginePhysics.getGear();
				double optimalShiftPoint = carProperties
						.getOptimalShiftPoint(gear);
				double rpm = enginePhysics.getRpm();

				if (rpm >= optimalShiftPoint - zone) {
					if (rpm <= carProperties.getRedline()) {
						if (isProba(proba(rpm, optimalShiftPoint))) {
							enginePhysics.incrementGear();
							System.out.println("Shifting to gear "
									+ enginePhysics.getGear() + " at RPM: "
									+ rpm);
						}
					}
				} else if (gear != 1 && rpm <= optimalShiftPoint / 5) {
					enginePhysics.decrementGear();
					System.out.println("Shifting to gear "
							+ enginePhysics.getGear() + " at RPM: " + rpm);
				}
				if (rpm > carProperties.getRedline()
						&& isProba(redlineShiftProba)) {
					enginePhysics.incrementGear();
				}
			}
		}
	}

	public static float angle(Vector2f v1, Vector2f v2) {
		float angle = MathTools.orientedAngle(v1, v2);
		angle = (float) ((angle < 0.5) ? angle : 0.5);
		return (float) ((angle < -0.5) ? -0.5 : angle);
	}

	/**
	 * Target a given point
	 * 
	 * @param posTarget
	 *            the point to target
	 * @param delayMs
	 *            a delay for the bot to react
	 * @param angularErrorFactor
	 *            the maximum error made by the bot when targeting
	 */
	public void target(Vector3f posTarget, int delayMs,
			double angularErrorFactor) {
		if (followTimer == 0)
			followTimer = System.currentTimeMillis();
		if (System.currentTimeMillis() - followTimer > delayMs) {
			Vector3f iaForward = new Vector3f(0, 0, 0).subtract(
					iaCar.getForwardVector(null)).normalize();
			Vector3f targetDirection = posTarget.subtract(
					iaCar.getPhysicsLocation()).normalize();

			Vector2f iaForward2 = new Vector2f(iaForward.x, iaForward.z);
			Vector2f targetDirection2 = new Vector2f(targetDirection.x,
					targetDirection.z);

			int intError = (int) (angularErrorFactor * 1000);
			float errorVal = ((float) MathTools
					.randBetween(-intError, intError)) / 1000.f;
			this.iaCar.steer(-angle(iaForward2, targetDirection2) + errorVal);
			followTimer = System.currentTimeMillis();
		}
	}

	/**
	 * The bot will target the given car, and try to bump into it.
	 * 
	 * @param targetCar
	 *            the targetted car
	 */
	public void target(Car targetCar, int delayMs, double angularErrorFactor) {
		target(targetCar.getPhysicsLocation(), delayMs, angularErrorFactor);
	}
}
