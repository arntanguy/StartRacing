package game;

import ia.IA;

import java.util.Timer;
import java.util.TimerTask;

import physics.CarProperties;
import physics.EnginePhysics;
import physics.tools.MathTools;
import audio.AudioRender;
import audio.EngineAudioRender;
import audio.EngineSoundStore;
import audio.SoundStore;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class Car extends VehicleControl {

	private AssetManager assetManager;

	private CarProperties properties;
	private EnginePhysics enginePhysics;
	private IA ia;

	private Node carNode;

	private Geometry chassis;
	private float wheelRadius;
	private float steeringValue = 0;

	private double life = 100;
	private String driverName;

	private long timerNos = 0;
	private int nosCharge = 0;

	public enum CarType {
		BOT, PLAYER
	};

	private CarType type;

	protected ParticuleMotor particuleMotor;
	protected AudioRender<String> audioRender;
	protected EngineAudioRender engineRender;

	// Ensures that the stop thread is not launched more than needed
	private boolean willStop = false;

	private boolean nosEnabled = false;

	public Car(AssetManager assetManager, CarProperties properties, String scene) {
		super();
		this.assetManager = assetManager;
		this.properties = properties;
		this.enginePhysics = new EnginePhysics(properties);
		this.ia = new IA(this, enginePhysics);

		this.driverName = "Unknown";
		this.type = CarType.BOT;

		buildCar(scene);
		buildParticuleMotor();
		buildAudioRender();
	}

	private void buildParticuleMotor() {
		// Init particule motor
		particuleMotor = new ParticuleMotor(assetManager);
	}

	private void buildAudioRender() {
		engineRender = new EngineAudioRender(carNode,
				EngineSoundStore.getInstance());
		audioRender = new AudioRender(carNode, SoundStore.getInstance(), true);
		System.out.println(SoundStore.getInstance().getSounds());
	}

	private void buildCar(String model) {
		if ("corvette".equals(model)) {
			CarFactory.createCorvette(assetManager, this);
		} else if ("charger".equals(model))	{
			CarFactory.createCharger(assetManager, this);
		} else if ("bmw".equals(model))	{
			CarFactory.createBMW(assetManager, this);
		} else if ("ferrari red".equals(model))	{
			CarFactory.createFerrari(assetManager, this, "Red");
		} else if ("ferrari blue".equals(model)) {
			CarFactory.createFerrari(assetManager, this, "Blue");
		} else if ("ferrari green".equals(model)) {
			CarFactory.createFerrari(assetManager, this, "Green");
		} else if ("ferrari orange".equals(model)) {
			CarFactory.createFerrari(assetManager, this, "Orange");
		} else {
			CarFactory.createFerrari(assetManager, this, "Blue");
		}
	}

	public Geometry getChassis() {
		return chassis;
	}

	public void setChassis(Geometry chassis) {
		this.chassis = chassis;
	}

	public Node getNode() {
		return carNode;
	}

	public void setNode(Node node) {
		this.carNode = node;
	}

	public CarProperties getProperties() {
		return properties;
	}

	public EnginePhysics getEnginePhysics() {
		return enginePhysics;
	}

	public IA getIA() {
		return ia;
	}

	public float getSteeringValue() {
		return steeringValue;
	}

	public void setSteeringValue(float steeringValue) {
		this.steeringValue = (float) ((steeringValue <= 0.5) ? steeringValue
				: 0.5);
		this.steeringValue = (float) ((steeringValue >= -0.5) ? steeringValue
				: -0.5);
	}

	public float getWheelRadius() {
		return wheelRadius;
	}

	public void setWheelRadius(float radius) {
		this.wheelRadius = radius;
	}

	public void increaseLife(double value) {
		life += value;
		if (life > 100)
			life = 100;
	}

	public void decreaseLife(double value) {
		life -= value;
		if (life < 0)
			life = 0;
		if (life == 0) {
			if (!particuleMotor.getBurstEnabled())
				explode();
		}
	}

	public int getLife() {
		return (int) life;
	}

	public void setDriverName(String name) {
		this.driverName = name;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setType(CarType type) {
		this.type = type;
	}

	public CarType getType() {
		return type;
	}

	public boolean inFront(Car c) {
		Vector3f vect3 = getPhysicsLocation().subtract(c.getPhysicsLocation());
		Vector2f vect = new Vector2f(vect3.x, vect3.z);

		Vector3f ref = new Vector3f().subtract(this.getForwardVector(null));
		Vector2f referenceOrientation = new Vector2f(ref.x, ref.z);

		float angle = Math.abs(MathTools.orientedAngle(vect,
				referenceOrientation));
		if (angle <= Math.PI / 2)
			return true;
		else
			return false;
	}

	public boolean getBurstEnabled() {
		return particuleMotor.getBurstEnabled();
	}

	public void addNos() {
		if (!nosEnabled) {
			// Vérifier qu'il reste une charge
			if (nosCharge > 0) {
				nosEnabled = true;
				nosCharge--;
				enginePhysics.activeNos();
				particuleMotor.addNos(carNode);

				enginePhysics.activeNos();

				timerNos = System.currentTimeMillis();
			}
		}
	}

	public void stopNos() {
		if (nosEnabled) {
			nosEnabled = false;
			particuleMotor.removeNos(carNode);
			timerNos = 0;

			enginePhysics.stopNos();
		}
	}

	public void controlNos() {
		if (nosEnabled && System.currentTimeMillis() - timerNos > 2500) {
			particuleMotor.removeNos(carNode);
			enginePhysics.stopNos();

			nosEnabled = false;
			timerNos = 0;
		}
	}

	public boolean getNosActivity() {
		return nosEnabled;
	}

	public void setNosCharge(int nombre) {
		nosCharge = nombre;
	}

	public void explode() {
		life = 0;
		particuleMotor.addExplosion(carNode);
		enginePhysics.setBreaking(true);
		audioRender.play("burst");
		engineRender.mute();
		stop(1000);
	}

	public void removeExplosion() {
		particuleMotor.removeExplosion(carNode);
	}

	public void updateSound(int rpm) {
		engineRender.setRPM(rpm);
	}

	public void mute() {
		audioRender.mute();
		engineRender.mute();
	}
	
	public void stopSound()	{
		audioRender.close();
		engineRender.close();
	}

	public void updateSound() {
		engineRender.setRPM(enginePhysics.getRpm());
	}

	/**
	 * Stops the car after a time delay Asynchronous method : will start a
	 * thread not to block the rest of the application
	 * 
	 * @param delay
	 *            the delay in ms
	 */
	public void stop(int delay) {
		if (!willStop) {
			willStop = true;
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					accelerate(0);
					setLinearVelocity(Vector3f.ZERO);
					engineRender.mute();
					willStop = false;
				}
			}, delay);
		}
	}

	public void setLife(double life) {
		this.life = (life <= 100) ? life : 100;
	}

	public boolean isAlive() {
		return (life > 0.f && !getBurstEnabled());
	}
	
	public EngineAudioRender getEngineAudioRender() {
		return engineRender;
	}
}
