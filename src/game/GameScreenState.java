package game;

import ia.IA;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import org.lwjgl.Sys;

import physics.BMWM3Properties;
import physics.CarProperties;
import physics.EnginePhysics;
import physics.tools.Conversion;
import audio.audioRender;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;

public class GameScreenState extends AbstractScreenController implements
ActionListener, AnalogListener, PhysicsCollisionListener {

	private ViewPort viewPort;
	private Node rootNode;
	private AssetManager assetManager;
	private InputManager inputManager;

	private BulletAppState bulletAppState;

	private Car player;
	private CarProperties playerCarProperties;
	private EnginePhysics playerEnginePhysics;

	private Car bot;
	private CarProperties botCarProperties;
	private EnginePhysics botEnginePhysics;

	private IA botIA;

	private float steeringValue = 0;
	private float accelerationValue = 0;

	private boolean runIsOn;
	private boolean playerFinish;
	private boolean botFinish;
	private boolean runFinish;

	private ChaseCamera chaseCam;

	private TerrainQuad terrain;
	private Material mat_terrain;
	private RigidBodyControl terrainPhys;

	private PssmShadowRenderer pssmRenderer;

	private long startTime = 0;
	private long countDown = 0;
	private long timerStopPlayer = 0;
	private long timerStopBot = 0;
	private long timerExplosion = 0;
	private long timerRedZone = 0;

	private long timePlayer;
	private long timeBot;

	private audioRender audio_motor;

	private boolean soudIsActive = true;
	private int initialRev;

	private AppStateManager stateManager;

	private Spatial map;
	private RigidBodyControl mapPhysic;

	// private Tachometer tachometer;
	private DigitalDisplay digitalTachometer;
	private DigitalDisplay digitalSpeed;
	private DigitalDisplay digitalGear;
	private ShiftlightLed shiftlight;
	private boolean isBreaking;
	private long rpmTimer;
	private GhostControl finishCell;
	private Node finishNode;

	private boolean needReset;
	private ParticleEmitter fire;
	private ParticleEmitter burst;
	private ParticleEmitter embers;
	private ParticleEmitter smoke;
	private ParticleEmitter debris;
	private ParticleEmitter shockwave;
	private Node explosionEffect;
	private int explosionState;
	private boolean burstIsEnable;

	public GameScreenState() {
		super();
	}

	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);

		this.rootNode = app.getRootNode();
		this.viewPort = app.getViewPort();
		this.assetManager = app.getAssetManager();
		this.inputManager = app.getInputManager();

		initGame();
	}

	private void initGame() {
		app.setDisplayStatView(false);

		bulletAppState = new BulletAppState();
		stateManager = app.getStateManager();
		stateManager.attach(bulletAppState);
		runIsOn = false;
		runFinish = false;
		playerFinish = false;
		botFinish = false;
		burstIsEnable = false;
		initialRev = 0;
		explosionState = 0;
		this.isBreaking = false;
		this.needReset = false;

		/*
		 * if (settings.getRenderer().startsWith("LWJGL")) { BasicShadowRenderer
		 * bsr = new BasicShadowRenderer(assetManager, 512);
		 * bsr.setDirection(new Vector3f(-0.5f, -0.3f, -0.3f).normalizeLocal());
		 * viewPort.addProcessor(bsr); }
		 */

		// Disable the default first-person cam!
		// flyCam.setEnabled(false);

		setupKeys();
		initGround();
		buildPlayer();

		/*
		 * // Initi Hud hudText = new BitmapText(guiFont, false);
		 * hudText.setSize(guiFont.getCharSet().getRenderedSize()); // font size
		 * hudText.setColor(ColorRGBA.White); // font color
		 * hudText.setText("0 km/h"); // the text
		 * hudText.setLocalTranslation(300, hudText.getLineHeight(), 0); //
		 * position guiNode.attachChild(hudText);
		 * 
		 * botHudText = new BitmapText(guiFont, false);
		 * botHudText.setSize(guiFont.getCharSet().getRenderedSize()); // font
		 * size botHudText.setColor(ColorRGBA.Yellow); // font color
		 * botHudText.setText("0 km/h"); // the text
		 * botHudText.setLocalTranslation(300, 5 + 2 *
		 * botHudText.getLineHeight(), 0); // position
		 * guiNode.attachChild(botHudText);
		 */

		// Active skybox
		Spatial sky = SkyFactory.createSky(assetManager,
				"Textures/Skysphere.jpg", true);
		rootNode.attachChild(sky);

		// Enable a chase cam
		chaseCam = new ChaseCamera(app.getCamera(), player.getChassis(),
				inputManager);
		chaseCam.setSmoothMotion(true);

		// Set up light
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f(-0.5f, -1f, -0.3f).normalizeLocal());
		rootNode.addLight(dl);

		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(1.3f));
		rootNode.addLight(al);

		// Set up shadow
		pssmRenderer = new PssmShadowRenderer(assetManager, 1024, 3);
		pssmRenderer.setDirection(new Vector3f(0.5f, -0.1f, 0.3f)
		.normalizeLocal()); // light direction
		viewPort.addProcessor(pssmRenderer);

		rootNode.setShadowMode(ShadowMode.Off); // reset all
		player.getNode().setShadowMode(ShadowMode.CastAndReceive); // normal

		bot.getNode().setShadowMode(ShadowMode.CastAndReceive);

		// map.setShadowMode(ShadowMode.Receive);
		terrain.setShadowMode(ShadowMode.Receive);

		// Init finish cell detection
		finishCell =  new GhostControl(new BoxCollisionShape(new Vector3f(40, 1, 1)));
		finishNode = new Node("finish zone");
		finishNode.addControl(finishCell);
		finishNode.move(0, 27, 298);
		//		finishNode.move(0, 27, -104);

		rootNode.attachChild(finishNode);
		getPhysicsSpace().add(finishCell);
		getPhysicsSpace().addCollisionListener(this);

		// Init audio
		audio_motor = new audioRender(assetManager, player.getNode());

		LinkedHashMap<Integer, String> channels = new LinkedHashMap<Integer, String>();
		channels.put(1000, "Models/Default/1052_P.wav");
		//		 channels.put(1126, "Models/Default/1126_P.wav");
		//		 channels.put(1205, "Models/Default/1205_P.wav");
		//		 channels.put(1289, "Models/Default/1289_P.wav");
		//		 channels.put(1380, "Models/Default/1380_P.wav");
		//		 channels.put(1476, "Models/Default/1476_P.wav");
		//		 channels.put(1579, "Models/Default/1579_P.wav");
		//		 channels.put(1690, "Models/Default/1690_P.wav");
		//		 channels.put(1808, "Models/Default/1808_P.wav");
		//		 channels.put(1935, "Models/Default/1935_P.wav");
		//		 channels.put(2070, "Models/Default/2070_P.wav");
		//		 channels.put(2215, "Models/Default/2215_P.wav");
		//		 channels.put(2370, "Models/Default/2370_P.wav");
		//		 channels.put(2536, "Models/Default/2536_P.wav");
		channels.put(2714, "Models/Default/2714_P.wav");
		// channels.put(2904, "Models/Default/2904_P.wav");
		// channels.put(3107, "Models/Default/3107_P.wav");
		// channels.put(3324, "Models/Default/3324_P.wav");
		// channels.put(3557, "Models/Default/3557_P.wav");
		// channels.put(3806, "Models/Default/3806_P.wav");
		// channels.put(4073, "Models/Default/4073_P.wav");
		channels.put(4358, "Models/Default/4358_P.wav");
		// channels.put(4663, "Models/Default/4663_P.wav");
		// channels.put(4989, "Models/Default/4989_P.wav");
		// channels.put(5338, "Models/Default/5338_P.wav");
		// channels.put(5712, "Models/Default/5712_P.wav");
		// channels.put(6112, "Models/Default/6112_P.wav");
		channels.put(8540, "Models/Default/6540_P.wav");

		HashMap<String, String> extraSound = new HashMap<String, String>();
		extraSound.put("start", "Models/Default/start.wav");
		extraSound.put("up", "Models/Default/up.wav");
		extraSound.put("beep", "Sound/start_beep.wav");

		audio_motor.init(channels, extraSound);

		// tachometer = new Tachometer(nifty, screen, playerCarProperties,
		// playerEnginePhysics);
		digitalTachometer = new DigitalDisplay(nifty, screen,
				"digital_tachometer", 80);
		digitalSpeed = new DigitalDisplay(nifty, screen, "digital_speed", 50);
		digitalGear = new DigitalDisplay(nifty, screen, "digital_gear", 50);
		shiftlight = new ShiftlightLed(nifty, screen, playerCarProperties,
				playerEnginePhysics);
	}

	@Override
	public void update(float tpf) {
		/** any main loop action happens here */
		int playerRpm = initialRev;

		if(needReset) 	{
			reset();
			return;
		}

		// Arrêter le joueur s'il a passé la ligne d'arrivée
		if (playerFinish && (System.currentTimeMillis() - timerStopPlayer > 1000))	{
			player.accelerate(0);
			player.setLinearVelocity(Vector3f.ZERO);

		}
		if (botFinish && (System.currentTimeMillis() - timerStopBot > 1000))	{
			bot.accelerate(0);
			bot.setLinearVelocity(Vector3f.ZERO);
		}

		// Tester si le round est fini
		if (playerFinish && botFinish && !runFinish)	{
			String text = "";


			if (timePlayer < timeBot && !burstIsEnable)	{
				text = "Gagne !\n ";
			}
			else	{
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
		

		controlBurst();

		int playerSpeed = (int) Math.abs(player.getCurrentVehicleSpeedKmHour());
		int botSpeed = (int) Math.abs(bot.getCurrentVehicleSpeedKmHour());
		if (runIsOn) {

			playerEnginePhysics.setSpeed(Math.abs(Conversion.kmToMiles(playerSpeed)));
			botEnginePhysics.setSpeed(Math.abs(Conversion.kmToMiles(botSpeed)));

			playerEnginePhysics.setSpeed(Math.abs(Conversion
					.kmToMiles(playerSpeed)));
			botEnginePhysics.setSpeed(Math.abs(Conversion.kmToMiles(botSpeed)));

			playerRpm = (int) playerEnginePhysics.getRpm();

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
		// tachometer.setRpm(playerRpm);
		digitalTachometer.setText(((Integer) playerRpm).toString());
		digitalSpeed.setText(((Integer) playerSpeed).toString());
		digitalGear.setText(((Integer) playerEnginePhysics.getGear())
				.toString());
		shiftlight.setRpm(playerRpm);

		// Traiter le cas du sur-régime
		if (playerRpm > (playerCarProperties.getRedline() - 500))	{
			if (!burstIsEnable)	{
				// Déclencher le timer s'il n'est pas activé
				if (timerRedZone == 0)	{
					timerRedZone = System.currentTimeMillis();
				}
				else	{
					if (System.currentTimeMillis() - timerRedZone > 3000)	{
						triggerBurst(player);
						playerFinish = true;
						timePlayer = 0;
						timerStopPlayer = System.currentTimeMillis();
					}
				}
			}
		}
		else	{
			timerRedZone = 0;
		}

		if (runIsOn) {
			botIA.act();
			float force = -(float) playerEnginePhysics.getForce() / 5;
			
			if (!burstIsEnable)	{
				player.accelerate(2, force*2);
				player.accelerate(3, force*2);
			}
			bot.accelerate(-(float) botEnginePhysics.getForce() / 5);
		} else {
			// Baisser le régime moteur à l'arrêt
			initialRev -= 100;

			if (initialRev < playerCarProperties.getIdleRpm()) {
				initialRev = playerCarProperties.getIdleRpm();
			}

		}
		/*
		 * hudText.setText(Math.abs(player.getCurrentVehicleSpeedKmHour()) +
		 * "km/h" + "\tRPM: " + playerRpm + "\tGear: " +
		 * playerEnginePhysics.getGear() + "\tOptimal Shift: " + (int)
		 * playerCarProperties
		 * .getOptimalShiftPoint(playerEnginePhysics.getGear()) + "\tForce: " +
		 * (int) playerEnginePhysics.getForce() + "\n " + timer);
		 * botHudText.setText(Math.abs(bot.getCurrentVehicleSpeedKmHour()) +
		 * "km/h" + "\tRPM: " + botRpm + "\tGear: " + botEnginePhysics.getGear()
		 * + "\tOptimal Shift: " + (int)
		 * botCarProperties.getOptimalShiftPoint(botEnginePhysics .getGear()) +
		 * "\tForce: " + (int) botEnginePhysics.getForce() + "\n ");
		 */
		// Update audio
		if (soudIsActive) {
			audio_motor.setRPM(playerRpm);
			app.getListener().setLocation(
					player.getNode().getWorldTranslation());
		}

	}

	public void controlBurst()	{
		if (burstIsEnable)	{	
			long time = System.currentTimeMillis() - timerExplosion;
			if (time > 100 && explosionState == 0) {
				explosionState++;
			}
			if (time > 500 && explosionState == 1) {
				shockwave.emitAllParticles();
				burst.emitAllParticles();
				debris.emitAllParticles();
				embers.emitAllParticles();
				explosionState++;
			}
			if (time > 700 && explosionState == 2) {
				fire.emitAllParticles();
				smoke.emitAllParticles();
				explosionState++;
			}
			if (time > 1000 && explosionState == 3) {
				burst.killAllParticles();
				debris.killAllParticles();
				explosionState++;
			}
			if (time > 4000 && explosionState == 4) {
//				fire.killAllParticles();
//				smoke.killAllParticles();
				embers.killAllParticles();
				shockwave.killAllParticles();
			}
		}
	}
	public void triggerBurst(Car vehicule) {
		burstIsEnable = true;
		initBurst();

		timerExplosion = System.currentTimeMillis();

		vehicule.getNode().attachChild(explosionEffect);

	}

	public void initBurst() {
		explosionEffect = new Node("explosionFX");
		
		createBurst();
		createDebris();
		createSmoke();
		createFire();
		createEmbers();
		createShockwave();
	}

	private void createFire() {
		fire = new ParticleEmitter("Emitter", Type.Triangle, 200);
		Material fire_mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		fire_mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
		fire.setMaterial(fire_mat);
		fire.setImagesX(2);
		fire.setImagesY(2);
		fire.setRandomAngle(true);
		explosionEffect.attachChild(fire);


		fire.setStartColor(new ColorRGBA(1f, 1f, .5f, 1f));
//		fire.setStartColor(ColorRGBA.Blue);
		fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 0f));
//		fire.setEndColor(ColorRGBA.Yellow);
		fire.setGravity(0, 0, 0);
		fire.setStartSize(1.5f);
		fire.setEndSize(0.05f);
		fire.setLowLife(0.5f);
		fire.setHighLife(2f);
		fire.getParticleInfluencer().setVelocityVariation(0.3f);
		fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 3f, 0));
		fire.setParticlesPerSec(100);
	}


	private void createBurst() {
		burst = new ParticleEmitter("Flash", Type.Triangle, 5);
		Material burst_mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		burst_mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flash.png"));
		burst.setMaterial(burst_mat);
		burst.setImagesX(2);
		burst.setImagesY(2);
		burst.setSelectRandomImage(true);
		explosionEffect.attachChild(burst);


		burst.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, 1f));
		burst.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
		burst.setStartSize(.1f);
		burst.setEndSize(6.0f);
		burst.setGravity(0, 0, 0);
		burst.setLowLife(.5f);
		burst.setHighLife(.5f);
		burst.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 5f, 0));
		burst.getParticleInfluencer().setVelocityVariation(1);
		burst.setShape(new EmitterSphereShape(Vector3f.ZERO, .5f));
		burst.setParticlesPerSec(0);


	}


	private void createEmbers() {
		embers = new ParticleEmitter("embers", Type.Triangle, 50);
		Material embers_mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		embers_mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/roundspark.png"));
		embers.setMaterial(embers_mat);
		embers.setImagesX(1);
		embers.setImagesY(1);
		explosionEffect.attachChild(embers);


		embers.setStartColor(new ColorRGBA(1f, 0.29f, 0.34f, 1.0f));
		embers.setEndColor(new ColorRGBA(0, 0, 0, 0.5f));
		embers.setStartSize(1.2f);
		embers.setEndSize(1.8f);
		embers.setGravity(0, -.5f, 0);
		embers.setLowLife(1.8f);
		embers.setHighLife(5f);
		embers.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 3, 0));
		embers.getParticleInfluencer().setVelocityVariation(.5f);
		embers.setShape(new EmitterSphereShape(Vector3f.ZERO, 2f));
		embers.setParticlesPerSec(0);


	}


	private void createSmoke() {
		smoke = new ParticleEmitter("Smoke emitter", Type.Triangle, 70);
		Material smoke_mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		smoke_mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/smoketrail.png"));
		smoke.setMaterial(smoke_mat);
		smoke.setImagesX(1);
		smoke.setImagesY(3);
		smoke.setSelectRandomImage(true);
		explosionEffect.attachChild(smoke);


		smoke.setStartColor(ColorRGBA.DarkGray);
		smoke.setEndColor(ColorRGBA.Black);
		smoke.setLowLife(4f);
		smoke.setHighLife(6f);
		smoke.setGravity(0,1,0);
		smoke.setFacingVelocity(true);
		smoke.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 5f, 0));
		smoke.getParticleInfluencer().setVelocityVariation(0.15f);
		smoke.setStartSize(.5f);
		smoke.setEndSize(2f);
		smoke.setParticlesPerSec(60);
	}


	private void createDebris() {
		debris = new ParticleEmitter("Debris", Type.Triangle, 15);
		Material debris_mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		debris_mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/Debris.png"));
		debris.setMaterial(debris_mat);
		debris.setImagesX(3);
		debris.setImagesY(3);
		debris.setSelectRandomImage(false);
		explosionEffect.attachChild(debris);


		debris.setRandomAngle(true);
		debris.setRotateSpeed(FastMath.TWO_PI * 2);
		debris.setStartColor(new ColorRGBA(0.4f, 0.4f, 0.4f, 1.0f));
		debris.setEndColor(new ColorRGBA(0.4f, 0.4f, 0.4f, 1.0f));
		debris.setStartSize(.2f);
		debris.setEndSize(1f);
		debris.setGravity(0,10f,0);
		debris.setLowLife(1f);
		debris.setHighLife(1.1f);
		debris.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 15, 0));
		debris.getParticleInfluencer().setVelocityVariation(.60f);
		debris.setParticlesPerSec(0);


	}


	private void createShockwave() {
		shockwave = new ParticleEmitter("Shockwave", Type.Triangle, 2);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/shockwave.png"));
		shockwave.setImagesX(1);
		shockwave.setImagesY(1);
		shockwave.setMaterial(mat);
		explosionEffect.attachChild(shockwave);

		/* The shockwave faces upward (along the Y axis) to make it appear as
		 * a horizontally expanding circle. */
		shockwave.setFaceNormal(Vector3f.UNIT_Y);
		shockwave.setStartColor(new ColorRGBA(.68f, 0.77f, 0.61f, 1f));
		shockwave.setEndColor(new ColorRGBA(.68f, 0.77f, 0.61f, 0f));
		shockwave.setStartSize(1f);
		shockwave.setEndSize(7f);
		shockwave.setGravity(0, 0, 0);
		shockwave.setLowLife(1f);
		shockwave.setHighLife(1f);
		shockwave.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, 0));
		shockwave.getParticleInfluencer().setVelocityVariation(0f);
		shockwave.setParticlesPerSec(0);
	}

	private void reset() {
		player.setPhysicsLocation(new Vector3f(0, 27, 700));
		player.setPhysicsRotation(new Matrix3f());
		player.setLinearVelocity(Vector3f.ZERO);
		player.setAngularVelocity(Vector3f.ZERO);
		playerEnginePhysics.setGear(1);
		player.resetSuspension();
		audio_motor.playStartSound();

		player.accelerate(0);
		bot.accelerate(0);
		playerEnginePhysics.setSpeed(0);
		botEnginePhysics.setSpeed(0);
		botEnginePhysics.setRpm(1000);
		playerEnginePhysics.setRpm(1000);


		bot.setPhysicsLocation(new Vector3f(10, 27, 700));
		bot.setPhysicsRotation(new Matrix3f());
		bot.setLinearVelocity(Vector3f.ZERO);
		bot.setAngularVelocity(Vector3f.ZERO);
		botEnginePhysics.setGear(1);
		bot.resetSuspension();

		if (burstIsEnable)	{
			player.getNode().detachChild(explosionEffect);
		}
		
		burstIsEnable = false;
		runIsOn = false;
		needReset = false;
		runFinish = false;
		playerFinish = false;
		botFinish = false;
		startTime = 0;
		countDown = 0;
		timerExplosion = 0;
		timerRedZone = 0;

	
		screen.findElementByName("startTimer")
		.getRenderer(TextRenderer.class).setText("Ready ?");
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

	public void initGround() {
		/** 1. Create terrain material and load four textures into it. */
		mat_terrain = new Material(assetManager,
				"Common/MatDefs/Terrain/Terrain.j3md");

		/** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
		mat_terrain.setTexture("Alpha",
				assetManager.loadTexture("Textures/alphamap.png"));

		/** 1.2) Add GRASS texture into the red layer (Tex1). */
		Texture grass = assetManager
				.loadTexture("Textures/Terrain/splat/grass.jpg");
		grass.setWrap(WrapMode.Repeat);
		mat_terrain.setTexture("Tex1", grass);
		mat_terrain.setFloat("Tex1Scale", 64f);

		/** 1.3) Add DIRT texture into the green layer (Tex2) */
		Texture dirt = assetManager
				.loadTexture("Textures/Terrain/splat/dirt.jpg");
		dirt.setWrap(WrapMode.Repeat);
		mat_terrain.setTexture("Tex2", dirt);
		mat_terrain.setFloat("Tex2Scale", 32f);

		/** 1.4) Add ROAD texture into the blue layer (Tex3) */
		Texture rock = assetManager
				.loadTexture("Textures/Terrain/splat/road.jpg");
		rock.setWrap(WrapMode.Repeat);
		mat_terrain.setTexture("Tex3", rock);
		mat_terrain.setFloat("Tex3Scale", 128f);

		/** 2. Create the height map */
		AbstractHeightMap heightmap = null;
		Texture heightMapImage = assetManager
				.loadTexture("Textures/mountains512.png");

		heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
		heightmap.load();

		/**
		 * 3. We have prepared material and heightmap. Now we create the actual
		 * terrain: 3.1) Create a TerrainQuad and name it "my terrain". 3.2) A
		 * good value for terrain tiles is 64x64 -- so we supply 64+1=65. 3.3)
		 * We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
		 * 3.4) As LOD step scale we supply Vector3f(1,1,1). 3.5) We supply the
		 * prepared heightmap itself.
		 */
		int patchSize = 65;
		terrain = new TerrainQuad("my terrain", patchSize, 513,
				heightmap.getHeightMap());

		/**
		 * 4. We give the terrain its material, position & scale it, and attach
		 * it.
		 */
		terrain.setMaterial(mat_terrain);
		terrain.setLocalTranslation(0, -100, 0);
		terrain.setLocalScale(2f, 1f, 2f);
		rootNode.attachChild(terrain);

		/** 5. The LOD (level of detail) depends on were the camera is: */
		TerrainLodControl control = new TerrainLodControl(terrain,
				app.getCamera());
		terrain.addControl(control);

		// Rendre le terrain physique

		terrain.setLocalScale(3f, 2f, 4f);

		terrainPhys = new RigidBodyControl(0.0f);
		terrain.addControl(terrainPhys);
		bulletAppState.getPhysicsSpace().add(terrainPhys);

		bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0, -9.81f, 0));
		terrainPhys.setFriction(0.5f);

		// bulletAppState.getPhysicsSpace().enableDebug(assetManager);

		//
		// map = assetManager.loadModel("Models/Map/vc-a.j3o");
		// map.setLocalScale(2f);
		// rootNode.attachChild(map);
		//
		// // We set up collision detection for the scene by creating a
		// // compound collision shape and a static RigidBodyControl with mass
		// zero.
		// PlaneCollisionShape ground = new PlaneCollisionShape(new Plane(new
		// Vector3f(0, 1, 0), 0));
		// Node node = new Node("Physicsground");
		// RigidBodyControl control = new RigidBodyControl(ground, 0f);
		// node.addControl(control);
		// node.getControl(RigidBodyControl.class).setPhysicsLocation(new
		// Vector3f(0f, -77.3f, 0f));
		//
		// //CollisionShape sceneShape =
		// CollisionShapeFactory.createMeshShape((Node) map);
		// //mapPhysic = new RigidBodyControl(sceneShape, 0);
		// //control.setFriction(1f);
		// //map.addControl(mapPhysic);
		//
		// Box floor_shape = new Box(new Vector3f(0, -77.5f, 0), 500f, 0.1f,
		// 500f);
		// Geometry floor = new Geometry("floor", floor_shape);
		// Material floor_mat = new Material(assetManager,
		// "Common/MatDefs/Misc/Unshaded.j3md");
		// floor.setMaterial(floor_mat);
		// // floor phy
		// RigidBodyControl floor_phy = new RigidBodyControl(0);
		// floor.addControl(floor_phy);
		// rootNode.attachChild(floor);
		//
		// //bulletAppState.getPhysicsSpace().add(mapPhysic);
		// bulletAppState.getPhysicsSpace().add(floor_phy);
		// //bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0, -9.81f,
		// 0));
		bulletAppState.getPhysicsSpace().enableDebug(assetManager);
	}

	private void setupKeys() {
		inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_Z));
		inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("Reset", new KeyTrigger(KeyInput.KEY_RETURN));
		inputManager.addMapping("Mute", new KeyTrigger(KeyInput.KEY_M));
		inputManager.addMapping("GearUp", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("GearDown", new KeyTrigger(KeyInput.KEY_E));

		inputManager.addMapping("GearUp", new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("GearDown", new KeyTrigger(KeyInput.KEY_DOWN));
		//		inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_LEFT)); // frein
		inputManager.addMapping("Throttle", new KeyTrigger(KeyInput.KEY_RCONTROL));
		inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_RIGHT));

		inputManager.addMapping("Menu", new KeyTrigger(KeyInput.KEY_ESCAPE));

		inputManager.addListener(this, "Lefts");
		inputManager.addListener(this, "Rights");
		inputManager.addListener(this, "Ups");
		inputManager.addListener(this, "Downs");
		inputManager.addListener(this, "Space");
		inputManager.addListener(this, "Reset");
		inputManager.addListener(this, "Mute");
		inputManager.addListener(this, "GearUp");
		inputManager.addListener(this, "GearDown");
		inputManager.addListener(this, "Throttle");

	}

	private PhysicsSpace getPhysicsSpace() {
		return bulletAppState.getPhysicsSpace();
	}

	private void buildPlayer() {
		playerCarProperties = new BMWM3Properties();
		playerEnginePhysics = new EnginePhysics(playerCarProperties);

		// Create a vehicle control
		player = new Car(assetManager, playerCarProperties);
		player.getNode().addControl(player);
		player.setPhysicsLocation(new Vector3f(0, 27, 700));

		botCarProperties = new BMWM3Properties();
		botEnginePhysics = new EnginePhysics(botCarProperties);
		bot = new Car(assetManager, botCarProperties);
		bot.setPhysicsLocation(new Vector3f(10, 27, 700));
		botIA = new IA(botEnginePhysics);

		rootNode.attachChild(player.getNode());
		rootNode.attachChild(bot.getNode());

		getPhysicsSpace().add(player);
		getPhysicsSpace().add(bot);

		initialRev = playerCarProperties.getIdleRpm();

	}

	public void onAction(String binding, boolean value, float tpf) {
		if (binding.equals("Lefts")) {
			if (value) {
				steeringValue += .5f;
			} else {
				steeringValue += -.5f;
			}
			player.steer(steeringValue);
		} else if (binding.equals("Rights")) {
			if (value) {
				steeringValue += -.5f;
			} else {
				steeringValue += .5f;
			}
			player.steer(steeringValue);
		} // note that our fancy car actually goes backwards..
		else if (binding.equals("Ups")) {
			if (value) {
				accelerationValue -= 10000;
			} else {
				accelerationValue += 10000;
			}
			// player.accelerate(accelerationValue);
			if (runIsOn) {
				player.accelerate(3, accelerationValue);
				player.accelerate(2, accelerationValue);
			}
		} else if (binding.equals("Downs")) {
			if (value) {
				accelerationValue = +5000;
			} else {
				accelerationValue = 0;
			}
			player.accelerate(3, accelerationValue);
			player.accelerate(2, accelerationValue);
		} else if (binding.equals("Space")) {
			if (value) {
				player.brake(700f);
			} else {
				player.brake(0f);
			}
		} else if (binding.equals("Reset")) {
			if (value) {
				System.out.println("Reset");
				// XXX: reset
				needReset = true;
			}
		} else if (binding.equals("GearUp")) {
			if (value) {
				audio_motor.gearUp();
				playerEnginePhysics.incrementGear();
			}
		} else if (binding.equals("GearDown")) {
			if (value) {
				playerEnginePhysics.decrementGear();
			}
		} else if(binding.equals("Menu")) {
			app.gotoStart();
		}
	}

	@Override
	public void onAnalog(String binding, float value, float tpf) {
		if (binding.equals("Throttle")) {
			if (countDown == 0)	{
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
		if (finishCell.getOverlappingObjects().contains(player) && !playerFinish)	{
			audio_motor.playStartBeep();

			timePlayer = (System.currentTimeMillis() - startTime);
			System.out.println(String.format("player : %d : %d",
					TimeUnit.MILLISECONDS.toSeconds(timePlayer),
					(timePlayer % 1000) / 10));


			timerStopPlayer = System.currentTimeMillis();
			playerFinish = true;
		}
		if (finishCell.getOverlappingObjects().contains(bot) && !botFinish)	{
			timeBot = (System.currentTimeMillis() - startTime);
			System.out.println(String.format("player : %d : %d",
					TimeUnit.MILLISECONDS.toSeconds(timeBot),
					(timeBot % 1000) / 10));

			timerStopBot = System.currentTimeMillis();
			botFinish = true;
		}

	}
}