package game;

import ia.IA;

import java.util.concurrent.TimeUnit;

import physics.BMWM3Properties;
import physics.CarProperties;
import physics.EnginePhysics;
import physics.tools.Conversion;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

import de.lessvoid.nifty.elements.render.TextRenderer;

public class GameScreenState extends AbstractGameScreenState {
	private Car bot;
	private CarProperties botCarProperties;
	private EnginePhysics botEnginePhysics;
	private IA botIA;

	private boolean playerFinish;
	private boolean botFinish;
	
	private long timerStopPlayer = 0;
	private long timerStopBot = 0;
	private long timePlayer;
	private long timeBot;
	
	public GameScreenState() {
		super();
	}

	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);

		initGame();
	}

	protected void initGame() {
		super.initGame();
		
		playerFinish = false;
		botFinish = false;
		
		buildBot();
	}	

	private void buildBot() {
		botCarProperties = new BMWM3Properties();
		bot = new Car(assetManager, botCarProperties);
		bot.setPhysicsLocation(new Vector3f(10, 27, 700));
		bot.getNode().setShadowMode(ShadowMode.CastAndReceive);
		botEnginePhysics = bot.getEnginePhysics();
		botIA = bot.getIA();
		rootNode.attachChild(bot.getNode());
		getPhysicSpace().add(bot);
	}

	@Override
	public void update(float tpf) {
		/** any main loop action happens here */
		if(needReset) 	{
			reset();
			return;
		}
		super.update(tpf);

		
		// Arrêter le joueur s'il a passé la ligne d'arrivée
		if (playerFinish
				&& (System.currentTimeMillis() - timerStopPlayer > 1000)) {
			player.accelerate(0);
			player.setLinearVelocity(Vector3f.ZERO);

		}
		if (botFinish && (System.currentTimeMillis() - timerStopBot > 1000)) {
			bot.accelerate(0);
			bot.setLinearVelocity(Vector3f.ZERO);
		}

		// Tester si le round est fini
		if (playerFinish && botFinish) {
			String text = "";
			if (timePlayer < timeBot) {
				text = "Gagne !\n ";
			} else {
				text = "Perdu !\n ";
			}
			text += String.format("Joueur:  %d : %d\n",
					TimeUnit.MILLISECONDS.toSeconds(timePlayer),
					(timePlayer % 1000) / 10);
			text += String.format("Bot:  %d : %d",
					TimeUnit.MILLISECONDS.toSeconds(timeBot),
					(timeBot % 1000) / 10);

			screen.findElementByName("startTimer")
					.getRenderer(TextRenderer.class).setText(text);

			runFinish = true;
			runIsOn = false;
		}

		int botSpeed = (int) Math.abs(bot.getCurrentVehicleSpeedKmHour());
		if (runIsOn) {
			botEnginePhysics.setSpeed(Math.abs(Conversion.kmToMiles(botSpeed)));

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
			botEnginePhysics.setBreaking(true);
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
			botIA.act();
			botIA.target(player);
			float force = -(float) playerEnginePhysics.getForce() / 5;
			player.accelerate(2, force * 2);
			player.accelerate(3, force * 2);

			bot.accelerate(-(float) botEnginePhysics.getForce() / 5);
		} else {
			// Baisser le régime moteur à l'arrêt
			initialRev -= 100;

			if (initialRev < playerCarProperties.getIdleRpm()) {
				initialRev = playerCarProperties.getIdleRpm();
			}

		}
		// Update audio
		

	}

	protected void reset() {
		super.reset();
		
		bot.accelerate(0);
		botEnginePhysics.setSpeed(0);
		botEnginePhysics.setRpm(1000);

		bot.setPhysicsLocation(new Vector3f(10, 27, 700));
		bot.setPhysicsRotation(new Matrix3f());
		bot.setLinearVelocity(Vector3f.ZERO);
		bot.setAngularVelocity(Vector3f.ZERO);
		botEnginePhysics.setGear(1);
		bot.resetSuspension();
		bot.steer(0);

		screen.findElementByName("startTimer").getRenderer(TextRenderer.class)
				.setText("Ready ?");
	}

	
	
	@Override
	public void onAnalog(String binding, float value, float tpf) {
		if (binding.equals("Throttle")) {
			if (countDown == 0) {
				countDown = System.currentTimeMillis();
			}

			initialRev += 400;

			int redline = playerCarProperties.getRedline();

			if (initialRev > redline) {
				isBreaking = true;
				/**
				 * When engine is breaking, oscillate rpm a little to simulate
				 * engine failure and get a nice sound ^^
				 */
				if (System.currentTimeMillis() - rpmTimer < 100) {
					initialRev = redline - 200;
				} else if (System.currentTimeMillis() - rpmTimer < 200) {
					initialRev = redline;
				} else {
					initialRev = redline;
					rpmTimer = System.currentTimeMillis();
				}
			} else {
				isBreaking = false;
			}
		}

	}


	@Override
	public void collision(PhysicsCollisionEvent arg0) {
		if (finishCell.getOverlappingObjects().contains(player)
				&& !playerFinish) {
			audio_motor.playStartBeep();

			timePlayer = (System.currentTimeMillis() - startTime);
			System.out.println(String.format("player : %d : %d",
					TimeUnit.MILLISECONDS.toSeconds(timePlayer),
					(timePlayer % 1000) / 10));

			timerStopPlayer = System.currentTimeMillis();
			playerFinish = true;
		}
		if (finishCell.getOverlappingObjects().contains(bot) && !botFinish) {
			timeBot = (System.currentTimeMillis() - startTime);
			System.out.println(String.format("player : %d : %d",
					TimeUnit.MILLISECONDS.toSeconds(timeBot),
					(timeBot % 1000) / 10));

			timerStopBot = System.currentTimeMillis();
			botFinish = true;
		}

	}

}
