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
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;

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

	private long timerRedZone = 0;

	private long rpmTimer;
	private GhostControl finishCell;
	private Node finishNode;

	private ParticuleMotor particule_motor;

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

		buildBot();
		buildFinishLine();

		playerFinish = false;
		botFinish = false;

		// Init particule motor
		particule_motor = new ParticuleMotor(assetManager);

	}

	protected void buildFinishLine() {
		// Init finish cell detection
		finishCell = new GhostControl(new BoxCollisionShape(new Vector3f(40, 1,
				1)));
		finishNode = new Node("finish zone");
		finishNode.addControl(finishCell);
		finishNode.move(0, 27, 298);

		rootNode.attachChild(finishNode);
		super.getPhysicsSpace().add(finishCell);
	}

	private void buildBot() {
		botCarProperties = new BMWM3Properties();
		bot = new Car(assetManager, botCarProperties);
		bot.setPhysicsLocation(new Vector3f(10, 27, 700));
		bot.getNode().setShadowMode(ShadowMode.CastAndReceive);
		botEnginePhysics = bot.getEnginePhysics();
		botIA = bot.getIA();
		rootNode.attachChild(bot.getNode());
		super.getPhysicsSpace().add(bot);
	}

	@Override
	public void update(float tpf) {
		/** any main loop action happens here */
		if (needReset) {
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
		if (playerFinish && botFinish && !runFinish) {
			String text = "";

			if (timePlayer < timeBot && !particule_motor.getBurstEnabled()) {
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

		particule_motor.controlBurst();

		int botSpeed = (int) Math.abs(bot.getCurrentVehicleSpeedKmHour());
		if (runIsOn) {
			botEnginePhysics.setSpeed(Math.abs(Conversion.kmToMiles(botSpeed)));

			long timeMili = (System.currentTimeMillis() - startTime);

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

		// Traiter le cas du sur-régime
		int playerRpm = player.getEnginePhysics().getRpm();
		if (playerRpm > (playerCarProperties.getRedline() - 500)) {
			if (!particule_motor.getBurstEnabled()) {
				// Déclencher le timer s'il n'est pas activé
				if (timerRedZone == 0) {
					timerRedZone = System.currentTimeMillis();
				} else {
					if (System.currentTimeMillis() - timerRedZone > 3000) {
						triggerBurst(player);
						audio_motor.playBurst();
						playerFinish = true;
						timePlayer = 0;
						timerStopPlayer = System.currentTimeMillis();
					}
				}
			}
		} else {
			timerRedZone = 0;
		}

		if (runIsOn) {
			botIA.act();
			bot.accelerate(-(float) botEnginePhysics.getForce() / 5);
		} else {
			// Baisser le régime moteur à l'arrêt
			initialRev -= 100;

			if (initialRev < playerCarProperties.getIdleRpm()) {
				initialRev = playerCarProperties.getIdleRpm();
			}

		}
	}

	public void triggerBurst(Car vehicule) {
		audio_motor.playBurst();
		particule_motor.addExplosion(vehicule.getNode());
		audio_motor.mute();
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

		if (particule_motor.getBurstEnabled()) {
			particule_motor.removeExplosion(player.getNode());
		}

		bot.steer(0);

		runIsOn = false;
		runFinish = false;
		playerFinish = false;
		botFinish = false;
		startTime = 0;
		countDown = 0;
		timerRedZone = 0;

		screen.findElementByName("startTimer").getRenderer(TextRenderer.class)
				.setText("Ready ?");
	}

	@Override
	public void stateAttached(AppStateManager stateManager) {
	}

	@Override
	public void stateDetached(AppStateManager stateManager) {

	}

	@Override
	public void bind(Nifty nifty, Screen screen) {
		super.bind(nifty, screen);
		// nifty.setDebugOptionPanelColors(true);
	}

	@Override
	public void onEndScreen() {
		stateManager.detach(this);
	}

	@Override
	public void onStartScreen() {
	}

	@Override
	public void onAnalog(String binding, float value, float tpf) {
		if (binding.equals("Throttle")) {
			if (!particule_motor.getBurstEnabled()) {
				if (countDown == 0) {
					countDown = System.currentTimeMillis();
				}

				initialRev += 400;

				int redline = playerCarProperties.getRedline();

				if (initialRev > redline) {
					isBreaking = true;
					/**
					 * When engine is breaking, oscillate rpm a little to
					 * simulate engine failure and get a nice sound ^^
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
