package game;

import ia.IA;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

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
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
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
import de.lessvoid.nifty.screen.Screen;

public class GameScreenState extends AbstractScreenController implements ActionListener {

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

	private ChaseCamera chaseCam;

	private TerrainQuad terrain;
	private Material mat_terrain;
	private RigidBodyControl terrainPhys;

	private BitmapText hudText;
	private BitmapText botHudText;

	private PssmShadowRenderer pssmRenderer;
	private long startTime = 0;

	private audioRender audio_motor;

	private boolean soudIsActive = true;

	private AppStateManager stateManager;
	
	private Tachometer tachometer;
	private ShiftlightFormule1 shiftlight;
	
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
		/*
		if (settings.getRenderer().startsWith("LWJGL")) {
			BasicShadowRenderer bsr = new BasicShadowRenderer(assetManager, 512);
			bsr.setDirection(new Vector3f(-0.5f, -0.3f, -0.3f).normalizeLocal());
			viewPort.addProcessor(bsr);
		}
		 */
		
		// Disable the default first-person cam!
		//flyCam.setEnabled(false);

		setupKeys();
		initGround();
		buildPlayer();

		
/*		// Initi Hud
		hudText = new BitmapText(guiFont, false);
		hudText.setSize(guiFont.getCharSet().getRenderedSize()); // font size
		hudText.setColor(ColorRGBA.White); // font color
		hudText.setText("0 km/h"); // the text
		hudText.setLocalTranslation(300, hudText.getLineHeight(), 0); // position
		guiNode.attachChild(hudText);

		botHudText = new BitmapText(guiFont, false);
		botHudText.setSize(guiFont.getCharSet().getRenderedSize()); // font size
		botHudText.setColor(ColorRGBA.Yellow); // font color
		botHudText.setText("0 km/h"); // the text
		botHudText.setLocalTranslation(300, 5 + 2 * botHudText.getLineHeight(),
				0); // position
		guiNode.attachChild(botHudText); */

		// Active skybox
		Spatial sky = SkyFactory.createSky(assetManager,
				"Textures/Skysphere.jpg", true);
		rootNode.attachChild(sky);

		// Enable a chase cam
		chaseCam = new ChaseCamera(app.getCamera(), player.getChassis(), inputManager);
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
		// behaviour
		bot.getNode().setShadowMode(ShadowMode.CastAndReceive);
		// (slow)
		terrain.setShadowMode(ShadowMode.Receive);

		// Init audio
		audio_motor = new audioRender(assetManager, player.getNode());

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
		// channels.put(2714, "Models/Default/2714_P.wav");
		// channels.put(2904, "Models/Default/2904_P.wav");
		// channels.put(3107, "Models/Default/3107_P.wav");
		// channels.put(3324, "Models/Default/3324_P.wav");
		// channels.put(3557, "Models/Default/3557_P.wav");
		// channels.put(3806, "Models/Default/3806_P.wav");
		channels.put(4073, "Models/Default/4073_P.wav");
		// channels.put(4358, "Models/Default/4358_P.wav");
		// channels.put(4663, "Models/Default/4663_P.wav");
		// channels.put(4989, "Models/Default/4989_P.wav");
		// channels.put(5338, "Models/Default/5338_P.wav");
		// channels.put(5712, "Models/Default/5712_P.wav");
		// channels.put(6112, "Models/Default/6112_P.wav");
		channels.put(8540, "Models/Default/6540_P.wav");

		HashMap<String, String> extraSound = new HashMap<String, String>();
		extraSound.put("start", "Models/Default/start.wav");
		extraSound.put("up", "Models/Default/up.wav");

		audio_motor.init(channels, extraSound);
		
		tachometer = new Tachometer(nifty, screen, playerCarProperties, playerEnginePhysics);
		shiftlight = new ShiftlightFormule1(nifty, screen, playerCarProperties, playerEnginePhysics);
		

	}
	
	@Override
	public void update(float tpf) {
		/** any main loop action happens here */
		float playerSpeed = Math.abs(player.getCurrentVehicleSpeedKmHour());
		float botSpeed = Math.abs(bot.getCurrentVehicleSpeedKmHour());

		playerEnginePhysics
				.setSpeed(Math.abs(Conversion.kmToMiles(playerSpeed)));
		botEnginePhysics.setSpeed(Math.abs(Conversion.kmToMiles(botSpeed)));

		int playerRpm = (int) playerEnginePhysics.getRpm();
		int botRpm = (int) botEnginePhysics.getRpm();

		long timeMili = (System.currentTimeMillis() - startTime);
		String timer = String.format(
				"%d min, %d sec %d ",
				TimeUnit.MILLISECONDS.toMinutes(timeMili),
				TimeUnit.MILLISECONDS.toSeconds(timeMili)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(timeMili)), (timeMili % 1000) / 10);

		// cam.lookAt(carNode.getWorldTranslation(), Vector3f.UNIT_Y);
		
		tachometer.setRpm(playerRpm);
		shiftlight.setRpm(playerRpm);
		
		botIA.act();
		player.accelerate(-(float) playerEnginePhysics.getForce() / 5);
		bot.accelerate(-(float) botEnginePhysics.getForce() / 5);
	/*	hudText.setText(Math.abs(player.getCurrentVehicleSpeedKmHour())
				+ "km/h"
				+ "\tRPM: "
				+ playerRpm
				+ "\tGear: "
				+ playerEnginePhysics.getGear()
				+ "\tOptimal Shift: "
				+ (int) playerCarProperties
						.getOptimalShiftPoint(playerEnginePhysics.getGear())
				+ "\tForce: " + (int) playerEnginePhysics.getForce() + "\n "
				+ timer);
		botHudText.setText(Math.abs(bot.getCurrentVehicleSpeedKmHour())
				+ "km/h"
				+ "\tRPM: "
				+ botRpm
				+ "\tGear: "
				+ botEnginePhysics.getGear()
				+ "\tOptimal Shift: "
				+ (int) botCarProperties.getOptimalShiftPoint(botEnginePhysics
						.getGear()) + "\tForce: "
				+ (int) botEnginePhysics.getForce() + "\n "); */
		// Update audio
		if (soudIsActive) {
			audio_motor.setRPM(playerRpm);
		}

	}

	@Override
	public void stateAttached(AppStateManager stateManager) {
		//rootNode.attachChild(localRootNode);
		//guiNode.attachChild(localGuiNode);
		//viewPort.setBackgroundColor(backgroundColor);
	}

	@Override
	public void stateDetached(AppStateManager stateManager) {
		//rootNode.detachChild(localRootNode);
		//guiNode.detachChild(localGuiNode);
	}

	@Override
	public void bind(Nifty nifty, Screen screen) {
		// TODO Auto-generated method stub
		super.bind(nifty, screen);
		//nifty.setDebugOptionPanelColors(true);
	}

	@Override
	public void onEndScreen() {
		stateManager.detach(this);
	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub		
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
		// Texture heightMapImage =
		// assetManager.loadTexture("Textures/monaco.png");

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
		TerrainLodControl control = new TerrainLodControl(terrain, app.getCamera());
		terrain.addControl(control);

		// Rendre le terrain physique
		terrainPhys = new RigidBodyControl(0.0f);
		terrain.addControl(terrainPhys);
		bulletAppState.getPhysicsSpace().add(terrainPhys);
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
		inputManager.addListener(this, "Lefts");
		inputManager.addListener(this, "Rights");
		inputManager.addListener(this, "Ups");
		inputManager.addListener(this, "Downs");
		inputManager.addListener(this, "Space");
		inputManager.addListener(this, "Reset");
		inputManager.addListener(this, "Mute");
		inputManager.addListener(this, "GearUp");
		inputManager.addListener(this, "GearDown");

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
		player.setPhysicsLocation(new Vector3f(0, -36, 0));

		botCarProperties = new BMWM3Properties();
		botEnginePhysics = new EnginePhysics(botCarProperties);
		bot = new Car(assetManager, botCarProperties);
		bot.setPhysicsLocation(new Vector3f(10, -36, 0));
		botIA = new IA(botEnginePhysics);

		rootNode.attachChild(player.getNode());
		rootNode.attachChild(bot.getNode());

		getPhysicsSpace().add(player);
		getPhysicsSpace().add(bot);

	}

	public void onAction(String binding, boolean value, float tpf) {
		// Initialisation du timer
		if (startTime == 0) {
			startTime = System.currentTimeMillis();
		}

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
			player.accelerate(3, accelerationValue);
			player.accelerate(2, accelerationValue);
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
				player.setPhysicsLocation(new Vector3f(0, -36, 0));
				player.setPhysicsRotation(new Matrix3f());
				player.setLinearVelocity(Vector3f.ZERO);
				player.setAngularVelocity(Vector3f.ZERO);
				playerEnginePhysics.setGear(1);
				player.resetSuspension();
				audio_motor.playStartSound();

				bot.setPhysicsLocation(new Vector3f(10, -36, 0));
				bot.setPhysicsRotation(new Matrix3f());
				bot.setLinearVelocity(Vector3f.ZERO);
				bot.setAngularVelocity(Vector3f.ZERO);
				botEnginePhysics.setGear(1);
				bot.resetSuspension();
			} else {
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
		}
	}
}