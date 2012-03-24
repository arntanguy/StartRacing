/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package test;

import java.util.concurrent.TimeUnit;

import physics.CarProperties;
import physics.DodgeViperProperties;
import physics.EnginePhysics;
import physics.tools.Conversion;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;

public class TestFancyCar extends SimpleApplication implements ActionListener {

	private BulletAppState bulletAppState;

	private Car player;
	private CarProperties playerCarProperties;
	private EnginePhysics playerEnginePhysics;

	private Car bot;
	private CarProperties botCarProperties;
	private EnginePhysics botEnginePhysics;

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

	private AudioNode audio_motor;

	private boolean soudIsActive = true;

	public static void main(String[] args) {
		TestFancyCar app = new TestFancyCar();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		this.setDisplayStatView(false);

		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		if (settings.getRenderer().startsWith("LWJGL")) {
			BasicShadowRenderer bsr = new BasicShadowRenderer(assetManager, 512);
			bsr.setDirection(new Vector3f(-0.5f, -0.3f, -0.3f).normalizeLocal());
			viewPort.addProcessor(bsr);
		}

		// Disable the default first-person cam!
		flyCam.setEnabled(false);

		// init GUI
		// hud = new Hud();
		// stateManager.attach(hud);
		// NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
		// inputManager, audioRenderer, guiViewPort);
		// Nifty nifty = niftyDisplay.getNifty();
		// guiViewPort.addProcessor(niftyDisplay);
		// nifty.fromXml("Interface/gui.xml", "hud", hud);

		setupKeys();
		initGround();
		buildPlayer();

		// Initi Hud
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
		guiNode.attachChild(botHudText);

		// Active skybox
		Spatial sky = SkyFactory.createSky(assetManager,
				"Textures/Skysphere.jpg", true);
		rootNode.attachChild(sky);

		// Enable a chase cam
		chaseCam = new ChaseCamera(cam, player.getChassis(), inputManager);
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
		// (slow)
		terrain.setShadowMode(ShadowMode.Receive);

		// Init audio
		audio_motor = new AudioNode(assetManager, "Sound/engine.wav", false);
		audio_motor.setLooping(true);
		rootNode.attachChild(audio_motor);
		audio_motor.setPitch(0.5f);
		audio_motor.play();
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
		TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
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
		playerCarProperties = new DodgeViperProperties();
		playerEnginePhysics = new EnginePhysics(playerCarProperties);

		// Create a vehicle control
		player = new Car(assetManager, playerCarProperties);
		player.getNode().addControl(player);

		botCarProperties = new DodgeViperProperties();
		botEnginePhysics = new EnginePhysics(botCarProperties);
		bot = new Car(assetManager, botCarProperties);
		bot.setPhysicsLocation(new Vector3f(10, 0, 0));

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
				player.setPhysicsLocation(Vector3f.ZERO);
				player.setPhysicsRotation(new Matrix3f());
				player.setLinearVelocity(Vector3f.ZERO);
				player.setAngularVelocity(Vector3f.ZERO);
				player.resetSuspension();

				bot.setPhysicsLocation(new Vector3f(10, 0, 0));
				bot.setPhysicsRotation(new Matrix3f());
				bot.setLinearVelocity(Vector3f.ZERO);
				bot.setAngularVelocity(Vector3f.ZERO);
				bot.resetSuspension();
			} else {
			}
		} else if (binding.equals("GearUp")) {
			if (value) {
				playerEnginePhysics.incrementGear();
			}
		} else if (binding.equals("GearDown")) {
			if (value) {
				playerEnginePhysics.decrementGear();
			}
		} else if (binding.equals("Mute")) {
			if (soudIsActive) {
				audio_motor.stop();
				audio_motor.setLooping(false);
				soudIsActive = false;
			} else {
				audio_motor.setLooping(true);
				audio_motor.play();
				soudIsActive = true;
			}
		}
	}

	@Override
	public void simpleUpdate(float tpf) {
		float playerSpeed = Math.abs(player.getCurrentVehicleSpeedKmHour());
		float botSpeed = Math.abs(bot.getCurrentVehicleSpeedKmHour());

		int playerRpm = (int) playerEnginePhysics.getRpm(Conversion
				.kmToMiles(playerSpeed));
		int botRpm = (int) botEnginePhysics.getRpm(Conversion
				.kmToMiles(botSpeed));

		long timeMili = (System.currentTimeMillis() - startTime);
		String timer = String.format(
				"%d min, %d sec %d ",
				TimeUnit.MILLISECONDS.toMinutes(timeMili),
				TimeUnit.MILLISECONDS.toSeconds(timeMili)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(timeMili)), (timeMili % 1000) / 10);

		// cam.lookAt(carNode.getWorldTranslation(), Vector3f.UNIT_Y);
		playerEnginePhysics
				.setSpeed(Math.abs(Conversion.kmToMiles(playerSpeed)));
		botEnginePhysics.setSpeed(Math.abs(Conversion.kmToMiles(botSpeed)));
		player.accelerate(-(float) playerEnginePhysics.getForce() / 5);
		bot.accelerate(-(float) botEnginePhysics.getForce() / 5);
		hudText.setText(Math.abs(player.getCurrentVehicleSpeedKmHour())
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
				+ (int) botEnginePhysics.getForce() + "\n ");
		// Update audio
		if (soudIsActive) {
			float pitch;
			// pitch = (vitesse/ 350f)* 1.5f + 0.5f;
			pitch = (playerRpm / 7000f) * 1.5f + 0.5f;
			System.out.println(pitch);

			if (pitch < 0.5f) {
				pitch = 0.5f;
			}
			if (pitch > 2.f) {
				pitch = 2.f;
			}
			audio_motor.setPitch(pitch);
		}

	}
}