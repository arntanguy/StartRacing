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

import physics.CarProperties;
import physics.EnginePhysics;
import physics.SkylineProperties;
import physics.tools.Conversion;
import hud.Hud;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.objects.VehicleWheel;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireFrustum;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;

import de.lessvoid.nifty.Nifty;

public class TestFancyCar extends SimpleApplication implements ActionListener {

	private BulletAppState bulletAppState;

	private VehicleControl player;
	private VehicleWheel fr, fl, br, bl;
	private Node node_fr, node_fl, node_br, node_bl;
	private float wheelRadius;
	private float steeringValue = 0;
	private float accelerationValue = 0;
	private Node carNode;
	private Geometry chasis;

	private ChaseCamera chaseCam;

	private TerrainQuad terrain;
	private Material mat_terrain;
	private RigidBodyControl terrainPhys;

	private Hud hud;
	private BitmapText hudText;

	private BasicShadowRenderer shadowRenderer;
	private PssmShadowRenderer pssmRenderer;
	private DepthOfFieldFilter dofFilter;

	private float startTime = 0f;

	private CarProperties carProperties;
	private EnginePhysics enginePhysics;

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
		initCar();

		// Initi Hud
		hudText = new BitmapText(guiFont, false);
		hudText.setSize(guiFont.getCharSet().getRenderedSize()); // font size
		hudText.setColor(ColorRGBA.White); // font color
		hudText.setText("0 km/h"); // the text
		hudText.setLocalTranslation(300, hudText.getLineHeight(), 0); // position
		guiNode.attachChild(hudText);

		// Active skybox
		Spatial sky = SkyFactory.createSky(assetManager,
				"Textures/Skysphere.jpg", true);
		rootNode.attachChild(sky);

		// Enable a chase cam
		chaseCam = new ChaseCamera(cam, chasis, inputManager);
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
		carNode.setShadowMode(ShadowMode.CastAndReceive); // normal behaviour
															// (slow)
		terrain.setShadowMode(ShadowMode.Receive);

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

	private void initCar() {
		carProperties = new SkylineProperties();
		enginePhysics = new EnginePhysics(carProperties);
	}

	private void setupKeys() {
		inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_Z));
		inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("Reset", new KeyTrigger(KeyInput.KEY_RETURN));
		inputManager.addMapping("GearUp", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("GearDown", new KeyTrigger(KeyInput.KEY_E));
		inputManager.addListener(this, "Lefts");
		inputManager.addListener(this, "Rights");
		inputManager.addListener(this, "Ups");
		inputManager.addListener(this, "Downs");
		inputManager.addListener(this, "Space");
		inputManager.addListener(this, "Reset");
		inputManager.addListener(this, "GearUp");
		inputManager.addListener(this, "GearDown");
	}

	private PhysicsSpace getPhysicsSpace() {
		return bulletAppState.getPhysicsSpace();
	}

	private Geometry findGeom(Spatial spatial, String name) {
		if (spatial instanceof Node) {
			Node node = (Node) spatial;
			for (int i = 0; i < node.getQuantity(); i++) {
				Spatial child = node.getChild(i);
				Geometry result = findGeom(child, name);
				if (result != null) {
					return result;
				}
			}
		} else if (spatial instanceof Geometry) {
			if (spatial.getName().startsWith(name)) {
				return (Geometry) spatial;
			}
		}
		return null;
	}

	private void buildPlayer() {
		float stiffness = 120.0f;// 200=f1 car
		float compValue = 0.2f; // (lower than damp!)
		float dampValue = 0.3f;
		final float mass = 1200;

		// Load model and get chassis Geometry
		carNode = (Node) assetManager.loadModel("Models/Ferrari/Car.scene");
		carNode.setShadowMode(ShadowMode.Cast);
		chasis = findGeom(carNode, "Car");
		BoundingBox box = (BoundingBox) chasis.getModelBound();

		// Create a hull collision shape for the chassis
		CollisionShape carHull = CollisionShapeFactory
				.createDynamicMeshShape(chasis);

		// Create a vehicle control
		player = new VehicleControl(carHull, mass);
		carNode.addControl(player);

		// Setting default values for wheels
		player.setSuspensionCompression(compValue * 2.0f
				* FastMath.sqrt(stiffness));
		player.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
		player.setSuspensionStiffness(stiffness);
		player.setMaxSuspensionForce(10000);

		// Create four wheels and add them at their locations
		// note that our fancy car actually goes backwards..
		Vector3f wheelDirection = new Vector3f(0, -1, 0);
		Vector3f wheelAxle = new Vector3f(-1, 0, 0);

		Geometry wheel_fr = findGeom(carNode, "WheelFrontRight");
		wheel_fr.center();
		box = (BoundingBox) wheel_fr.getModelBound();
		wheelRadius = box.getYExtent();
		float back_wheel_h = (wheelRadius * 1.7f) - 1f;
		float front_wheel_h = (wheelRadius * 1.9f) - 1f;
		player.addWheel(wheel_fr.getParent(),
				box.getCenter().add(0, -front_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, true);

		Geometry wheel_fl = findGeom(carNode, "WheelFrontLeft");
		wheel_fl.center();
		box = (BoundingBox) wheel_fl.getModelBound();
		player.addWheel(wheel_fl.getParent(),
				box.getCenter().add(0, -front_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, true);

		Geometry wheel_br = findGeom(carNode, "WheelBackRight");
		wheel_br.center();
		box = (BoundingBox) wheel_br.getModelBound();
		player.addWheel(wheel_br.getParent(),
				box.getCenter().add(0, -back_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, false);

		Geometry wheel_bl = findGeom(carNode, "WheelBackLeft");
		wheel_bl.center();
		box = (BoundingBox) wheel_bl.getModelBound();
		player.addWheel(wheel_bl.getParent(),
				box.getCenter().add(0, -back_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, false);

		player.getWheel(0).setFrictionSlip(11f);
		player.getWheel(1).setFrictionSlip(11f);
		player.getWheel(2).setFrictionSlip(10f);
		player.getWheel(3).setFrictionSlip(10f);

		rootNode.attachChild(carNode);
		getPhysicsSpace().add(player);
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
			player.accelerate(3, accelerationValue);
			player.accelerate(2, accelerationValue);
			player.setCollisionShape(CollisionShapeFactory
					.createDynamicMeshShape(findGeom(carNode, "Car")));
		} else if (binding.equals("Downs")) {
			if (value) {
				accelerationValue = +5000;
			} else {
				accelerationValue = 0;
			}
			player.accelerate(3, accelerationValue);
			player.accelerate(2, accelerationValue);
			player.setCollisionShape(CollisionShapeFactory
					.createDynamicMeshShape(findGeom(carNode, "Car")));
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
			} else {
			}
		} else if (binding.equals("GearUp")) {
			if (value) {
				enginePhysics.incrementGear();
			}
		} else if (binding.equals("GearDown")) {
			if (value) {
				enginePhysics.decrementGear();
			}
		}
	}

	@Override
	public void simpleUpdate(float tpf) {
		// cam.lookAt(carNode.getWorldTranslation(), Vector3f.UNIT_Y);
		enginePhysics.setSpeed(Math.abs(Conversion.kmToMiles(Math.abs(player
				.getCurrentVehicleSpeedKmHour()))));
		player.accelerate(-(float) enginePhysics.getForce()/10);
		hudText.setText(Math.abs(player.getCurrentVehicleSpeedKmHour())
				+ "km/h"
				+ "\tRPM: "
				+ (int) enginePhysics.getRpm(Conversion.kmToMiles(Math
						.abs(player.getCurrentVehicleSpeedKmHour())))
				+ "\tGear: " + enginePhysics.getGear() + "\tOptimal Shift: "
				+ (int)carProperties.getOptimalShiftPoint(enginePhysics.getGear())
				+ "\tForce: " + (int)enginePhysics.getForce());
	}
}