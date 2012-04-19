package game;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import physics.BMWM3Properties;
import physics.CarProperties;
import physics.tools.Conversion;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

import de.lessvoid.nifty.elements.render.TextRenderer;

public class FreeForAllScreenState extends AbstractGameScreenState {

	private ArrayList<Car> bots;

	public FreeForAllScreenState() {
		super();
	}

	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);

		bots = new ArrayList<Car>();

		initGame();
	}

	protected void initGame() {
		super.initGame();
		player.setPhysicsLocation(new Vector3f(0, 27, 700));
		addBot(new Vector3f(new Vector3f(10, 27, 700)), new BMWM3Properties());
		addBot(new Vector3f(new Vector3f(20, 27, 700)), new BMWM3Properties());
		addBot(new Vector3f(new Vector3f(30, 27, 700)), new BMWM3Properties());
		addBot(new Vector3f(new Vector3f(40, 27, 700)), new BMWM3Properties());
	}

	protected void addBot(Vector3f location, CarProperties carProperties) {
		Car bot = new Car(assetManager, carProperties);
		bot.setPhysicsLocation(location);
		bot.getNode().setShadowMode(ShadowMode.CastAndReceive);

		rootNode.attachChild(bot.getNode());
		getPhysicsSpace().add(bot);

		bots.add(bot);
	}

	@Override
	public void update(float tpf) {
		/** any main loop action happens here */
		if (needReset) {
			reset();
			return;
		}
		super.update(tpf);

		// int botSpeed = (int) Math.abs(bot.getCurrentVehicleSpeedKmHour());
		if (runIsOn) {
			for (Car bot : bots) {
				bot.getEnginePhysics().setSpeed(
						Math.abs(Conversion.kmToMiles(bot
								.getCurrentVehicleSpeedKmHour())));
			}

			long timeMili = (System.currentTimeMillis() - startTime);
			/*
			 * String timer = String.format( "%d min, %d sec %d ",
			 * TimeUnit.MILLISECONDS.toMinutes(timeMili),
			 * TimeUnit.MILLISECONDS.toSeconds(timeMili) -
			 * TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
			 * .toMinutes(timeMili)), (timeMili % 1000) / 10);
			 */

			String sTimer = String.format("%d : %d",
					TimeUnit.MILLISECONDS.toSeconds(timeMili),
					(timeMili % 1000) / 10);

			screen.findElementByName("timer").getRenderer(TextRenderer.class)
					.setText(sTimer);
		} else if (!runFinish) {
			for (Car bot : bots) {
				bot.getEnginePhysics().setBreaking(true);
			}

			// Afficher le compte à rebour
			long time = System.currentTimeMillis() - countDown;

			if (countDown != 0) {
				if (time > 5000) {
					screen.findElementByName("startTimer")
							.getRenderer(TextRenderer.class).setText("");
					runIsOn = true;
					audio_motor.playStartBeep();
					playerEnginePhysics.setRpm(initialRev);
					startTime = System.currentTimeMillis();
				} else if (time > 4000) {
					screen.findElementByName("startTimer")
							.getRenderer(TextRenderer.class).setText("1");

				} else if (time > 3000) {
					screen.findElementByName("startTimer")
							.getRenderer(TextRenderer.class).setText("2");
				} else if (time > 2000) {
					screen.findElementByName("startTimer")
							.getRenderer(TextRenderer.class).setText("3");
				}
			}
		}

		if (runIsOn) {
			for (Car bot : bots) {
				bot.getIA().act();
				bot.getIA().target(player);
				bot.accelerate(-(float) bot.getEnginePhysics().getForce() / 5);
			}
		} else {
			// Baisser le régime moteur à l'arrêt
			initialRev -= 100;

			if (initialRev < playerCarProperties.getIdleRpm()) {
				initialRev = playerCarProperties.getIdleRpm();
			}
		}

	}

	@Override
	public void collision(PhysicsCollisionEvent arg0) {
	}

}
