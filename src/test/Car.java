package test;

import physics.CarProperties;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Car extends VehicleControl {

	private AssetManager assetManager;

	private CarProperties properties;

	private Node carNode;

	private Geometry chasis;

	private float wheelRadius;

	public Car(AssetManager assetManager, CarProperties properties) {
		super();
		this.assetManager = assetManager;
		this.properties = properties;
		buildPlayer();
	}

	private void buildPlayer() {
		float stiffness = properties.getStiffness();// 200=f1 car
		float compValue = properties.getCompValue(); // (lower than damp!)
		float dampValue = properties.getDampValue();
		final float mass = properties.getMass();

		this.setMass(mass);

		// Load model and get chassis Geometry
		carNode = (Node) assetManager.loadModel("Models/Ferrari/Car.scene");
		carNode.setShadowMode(ShadowMode.Cast);

		// Create a hull collision shape for the chassis
		chasis = findGeom(carNode, "Car");
		BoundingBox box = (BoundingBox) chasis.getModelBound();
		CollisionShape carHull = CollisionShapeFactory
				.createDynamicMeshShape(chasis);
		this.setCollisionShape(carHull);

		// Create a vehicle control
		carNode.addControl(this);

		// Setting default values for wheels
		this.setSuspensionCompression(compValue * 2.0f
				* FastMath.sqrt(stiffness));
		this.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
		this.setSuspensionStiffness(stiffness);
		this.setMaxSuspensionForce(10000);

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
		this.addWheel(wheel_fr.getParent(),
				box.getCenter().add(0, -front_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, true);

		Geometry wheel_fl = findGeom(carNode, "WheelFrontLeft");
		wheel_fl.center();
		box = (BoundingBox) wheel_fl.getModelBound();
		this.addWheel(wheel_fl.getParent(),
				box.getCenter().add(0, -front_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, true);

		Geometry wheel_br = findGeom(carNode, "WheelBackRight");
		wheel_br.center();
		box = (BoundingBox) wheel_br.getModelBound();
		this.addWheel(wheel_br.getParent(),
				box.getCenter().add(0, -back_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, false);

		Geometry wheel_bl = findGeom(carNode, "WheelBackLeft");
		wheel_bl.center();
		box = (BoundingBox) wheel_bl.getModelBound();
		this.addWheel(wheel_bl.getParent(),
				box.getCenter().add(0, -back_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, false);

		this.getWheel(0).setFrictionSlip(11f);
		this.getWheel(1).setFrictionSlip(11f);
		this.getWheel(2).setFrictionSlip(10f);
		this.getWheel(3).setFrictionSlip(10f);
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

	public Geometry getChassis() {
		return findGeom(carNode, "Car");
	}

	public Node getNode() {
		return carNode;
	}
}
