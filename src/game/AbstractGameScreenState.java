package game;

import java.util.HashMap;
import java.util.LinkedHashMap;

import physics.BMWM3Properties;
import physics.CarProperties;
import physics.EnginePhysics;
import physics.tools.Conversion;
import audio.AudioRender;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
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

public abstract class AbstractGameScreenState extends AbstractScreenController
		implements ActionListener, AnalogListener, PhysicsCollisionListener {

	private ViewPort viewPort;
	protected Node rootNode;
	protected AssetManager assetManager;
	private InputManager inputManager;

	private BulletAppState bulletAppState;

	protected AudioRender audio_motor;

	protected Car player;
	protected CarProperties playerCarProperties;
	protected EnginePhysics playerEnginePhysics;

	protected boolean runIsOn;
	protected boolean runFinish;

	private ChaseCamera chaseCam;

	private TerrainQuad terrain;
	private Material mat_terrain;
	private RigidBodyControl terrainPhys;

	private PssmShadowRenderer pssmRenderer;

	protected long startTime = 0;
	protected long countDown = 0;

	protected boolean soudIsActive = true;
	protected int initialRev;

	protected AppStateManager stateManager;

	protected DigitalDisplay digitalTachometer;
	protected DigitalDisplay digitalSpeed;
	protected DigitalDisplay digitalGear;
	protected ShiftlightLed shiftlight;
	protected boolean isBreaking;
	protected long rpmTimer;

	protected boolean needReset;
	private int accelerationValue;

	protected ParticuleMotor particule_motor;
	private long timerRedZone = 0;
	protected boolean playerFinish;
	protected long timerStopPlayer = 0;
	protected long timePlayer = 0;

	public AbstractGameScreenState() {
		super();
	}

	/***** Initialize Nifty gui ****/
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

	/******* Initialize game ******/
	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);

		this.rootNode = app.getRootNode();
		this.viewPort = app.getViewPort();
		this.assetManager = app.getAssetManager();
		this.inputManager = app.getInputManager();
	}

	protected void initGame() {
		app.setDisplayStatView(false);

		bulletAppState = new BulletAppState();
		stateManager = app.getStateManager();
		stateManager.attach(bulletAppState);
		runIsOn = false;
		runFinish = false;
		initialRev = 0;
		this.isBreaking = false;
		this.needReset = false;

		initGround();
		buildPlayer();
		setupKeys();

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

		// map.setShadowMode(ShadowMode.Receive);
		terrain.setShadowMode(ShadowMode.Receive);

		getPhysicsSpace().addCollisionListener(this);

		// Init audio
		audio_motor = new AudioRender(assetManager, player.getNode());

		// Init particule motor
		particule_motor = new ParticuleMotor(assetManager);

		LinkedHashMap<Integer, String> channels = new LinkedHashMap<Integer, String>();
		channels.put(1000, "Models/Default/1052_P.wav");
		// channels.put(1126, "Models/Default/1126_P.wav");
		// channels.put(1205, "Models/Default/1205_P.wav");
		// channels.put(1289, "Models/Default/1289_P.wav");
		// channels.put(1380, "Models/Default/1380_P.wav");
		// channels.put(1476, "Models/Default/1476_P.wav");
		// channels.put(1579, "Models/Default/1579_P.wav");
		// channels.put(1690, "Models/Default/1690_P.wav");
		// channels.put(1808, "Models/Default/1808_P.wav");
		// channels.put(1935, "Models/Default/1935_P.wav");
		// channels.put(2070, "Models/Default/2070_P.wav");
		// channels.put(2215, "Models/Default/2215_P.wav");
		// channels.put(2370, "Models/Default/2370_P.wav");
		// channels.put(2536, "Models/Default/2536_P.wav");
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
		// XXX: put real sound
		extraSound.put("burst", "Sound/start_beep.wav");

		audio_motor.init(channels, extraSound);

		digitalTachometer = new DigitalDisplay(nifty, screen,
				"digital_tachometer", 80);
		digitalSpeed = new DigitalDisplay(nifty, screen, "digital_speed", 50);
		digitalGear = new DigitalDisplay(nifty, screen, "digital_gear", 50);
		shiftlight = new ShiftlightLed(nifty, screen, playerCarProperties,
				playerEnginePhysics);
	}

	private void buildPlayer() {
		playerCarProperties = new BMWM3Properties();

		// Create a vehicle control
		player = new Car(assetManager, playerCarProperties);
		player.getNode().addControl(player);
		player.setPhysicsLocation(new Vector3f(0, 27, 700));

		playerCarProperties = player.getProperties();
		playerEnginePhysics = player.getEnginePhysics();

		rootNode.attachChild(player.getNode());

		getPhysicsSpace().add(player);

		initialRev = playerCarProperties.getIdleRpm();
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

		bulletAppState.getPhysicsSpace()
				.setGravity(new Vector3f(0, -19.81f, 0));
		terrainPhys.setFriction(0.5f);

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
		inputManager.addMapping("Throttle", new KeyTrigger(
				KeyInput.KEY_RCONTROL));
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

	@Override
	public void update(float tpf) {
		// tachometer.setRpm(playerRpm);
		int playerSpeed = (int) Math.abs(player.getCurrentVehicleSpeedKmHour());
		int playerRpm = initialRev;

		if (runIsOn) {
			playerRpm = player.getEnginePhysics().getRpm();

			// Arrêter le joueur s'il a passé la ligne d'arrivée
			if (playerFinish
					&& (System.currentTimeMillis() - timerStopPlayer > 1000)) {
				player.accelerate(0);
				player.setLinearVelocity(Vector3f.ZERO);
			}

			playerEnginePhysics.setSpeed(Math.abs(Conversion
					.kmToMiles(playerSpeed)));
			float force = -(float) playerEnginePhysics.getForce() / 5;
			player.accelerate(2, force * 2);
			player.accelerate(3, force * 2);
		} else if (!runFinish) {
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

		// Baisser le régime moteur à l'arrêt
		if (!runIsOn) {
			initialRev -= 100;

			if (initialRev < playerCarProperties.getIdleRpm()) {
				initialRev = playerCarProperties.getIdleRpm();
			}
		}

		// Update audio
		if (soudIsActive) {
			audio_motor.setRPM(playerRpm);
			app.getListener().setLocation(
					player.getNode().getWorldTranslation());
		}

		// Traiter le cas du sur-régime
		if (playerRpm > (playerCarProperties.getRedline() - 500)) {
			if (!particule_motor.getBurstEnabled()) {
				// Déclencher le timer s'il n'est pas activé
				if (timerRedZone == 0) {
					timerRedZone = System.currentTimeMillis();
				} else {
					if (System.currentTimeMillis() - timerRedZone > 3000) {
						triggerBurst(player);
						audio_motor.playBurst();
					}
				}
			}
		} else {
			timerRedZone = 0;
		}

		particule_motor.controlBurst();

		digitalTachometer.setText(((Integer) playerRpm).toString());
		digitalSpeed.setText(((Integer) playerSpeed).toString());
		digitalGear.setText(((Integer) playerEnginePhysics.getGear())
				.toString());
		shiftlight.setRpm(playerRpm);
	}

	public void triggerBurst(Car vehicule) {
		audio_motor.playBurst();
		particule_motor.addExplosion(vehicule.getNode());
		audio_motor.mute();
		playerFinish = true;
		timePlayer = 0;
		timerStopPlayer = System.currentTimeMillis();
	}

	protected void reset() {
		player.setPhysicsLocation(new Vector3f(0, 27, 700));
		player.setPhysicsRotation(new Matrix3f());
		player.setLinearVelocity(Vector3f.ZERO);
		player.setAngularVelocity(Vector3f.ZERO);
		playerEnginePhysics.setGear(1);
		player.resetSuspension();
		player.steer(0);
		audio_motor.playStartSound();

		player.accelerate(0);
		playerEnginePhysics.setSpeed(0);
		playerEnginePhysics.setRpm(1000);

		if (particule_motor.getBurstEnabled()) {
			particule_motor.removeExplosion(player.getNode());
		}

		timerRedZone = 0;
		playerFinish = false;
		runIsOn = false;
		needReset = false;
		runFinish = false;
		startTime = 0;
		countDown = 0;

		screen.findElementByName("startTimer").getRenderer(TextRenderer.class)
				.setText("Ready ?");
	}

	protected PhysicsSpace getPhysicsSpace() {
		return bulletAppState.getPhysicsSpace();
	}

	public void onAction(String binding, boolean value, float tpf) {
		if (binding.equals("Lefts")) {
			if (value) {
				player.setSteeringValue(.5f);
			} else {
				player.setSteeringValue(0.f);
				;
			}
			player.steer(player.getSteeringValue());
		} else if (binding.equals("Rights")) {
			if (value) {
				player.setSteeringValue(-.5f);
			} else {
				player.setSteeringValue(0.f);
			}
			player.steer(player.getSteeringValue());
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
		} else if (binding.equals("Menu")) {
			app.gotoStart();
		}
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
}