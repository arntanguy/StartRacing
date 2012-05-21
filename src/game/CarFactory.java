package game;

import physics.CarProperties;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class CarFactory {

	public static void createFerrariRed(AssetManager assetManager, Car car)	{
		createFerrari(assetManager, car, "Red");
	}
	
	public static void createFerrariBlue(AssetManager assetManager, Car car)	{
		createFerrari(assetManager, car, "Blue");
	}
	
	public static void createFerrariGreen(AssetManager assetManager, Car car)	{
		createFerrari(assetManager, car, "Green");
	}
	
	public static void createFerrariOrange(AssetManager assetManager, Car car)	{
		createFerrari(assetManager, car, "Orange");
	}
	
	public static void createFerrari(AssetManager assetManager, Car car, String color)	{
		float stiffness = car.getProperties().getStiffness();// 200=f1 car
		float compValue = car.getProperties().getCompValue(); // (lower than damp!)
		float dampValue = car.getProperties().getDampValue();
		final float mass = car.getProperties().getMass();

		car.setMass(mass);

		// Load model and get chassis Geometry
		car.setNode((Node) assetManager.loadModel("Models/Ferrari"+ color + "/Car.scene"));
		car.getNode().setShadowMode(ShadowMode.Cast);

		// Create a hull collision shape for the chassis
		car.setChassis(findGeom(car.getNode(), "Car"));
		
		BoundingBox box = (BoundingBox) car.getChassis().getModelBound();
		CollisionShape carHull = CollisionShapeFactory
				.createDynamicMeshShape(car.getChassis());
		car.setCollisionShape(carHull);

		// Create a vehicle control
		car.getNode().addControl(car);

		// Setting default values for wheels
		car.setSuspensionCompression(compValue * 2.0f
				* FastMath.sqrt(stiffness));
		car.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
		car.setSuspensionStiffness(stiffness);
		car.setMaxSuspensionForce(10000);

		// Create four wheels and add them at their locations
		// note that our fancy car actually goes backwards..
		Vector3f wheelDirection = new Vector3f(0, -1, 0);
		Vector3f wheelAxle = new Vector3f(-1, 0, 0);

		Geometry wheel_fr = findGeom(car.getNode(), "WheelFrontRight");
		wheel_fr.center();
		box = (BoundingBox) wheel_fr.getModelBound();
		float wheelRadius = box.getYExtent();
		car.setWheelRadius(wheelRadius);
		
		float back_wheel_h = (wheelRadius * 1.7f) - 1f;
		float front_wheel_h = (wheelRadius * 1.9f) - 1f;
		car.addWheel(wheel_fr.getParent(),
				box.getCenter().add(0, -front_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, true);

		Geometry wheel_fl = findGeom(car.getNode(), "WheelFrontLeft");
		wheel_fl.center();
		box = (BoundingBox) wheel_fl.getModelBound();
		car.addWheel(wheel_fl.getParent(),
				box.getCenter().add(0, -front_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, true);

		Geometry wheel_br = findGeom(car.getNode(), "WheelBackRight");
		wheel_br.center();
		box = (BoundingBox) wheel_br.getModelBound();
		car.addWheel(wheel_br.getParent(),
				box.getCenter().add(0, -back_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, false);

		Geometry wheel_bl = findGeom(car.getNode(), "WheelBackLeft");
		wheel_bl.center();
		box = (BoundingBox) wheel_bl.getModelBound();
		car.addWheel(wheel_bl.getParent(),
				box.getCenter().add(0, -back_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, false);

		car.getWheel(0).setFrictionSlip(11f);
		car.getWheel(1).setFrictionSlip(11f);
		car.getWheel(2).setFrictionSlip(10f);
		car.getWheel(3).setFrictionSlip(10f);
	}
	
	public static void createCorvette(AssetManager assetManager, Car car)	{
		float stiffness = car.getProperties().getStiffness();// 200=f1 car
		float compValue = car.getProperties().getCompValue(); // (lower than damp!)
		float dampValue = car.getProperties().getDampValue();
		final float mass = car.getProperties().getMass();

		car.setMass(mass);

		// Load model and get chassis Geometry
		car.setNode((Node) assetManager.loadModel("Models/Corvette/Corvette.scene"));
		car.getNode().setShadowMode(ShadowMode.Cast);

		// Create a hull collision shape for the chassis
//		chasis = findGeom(carNode, "Car");
		Node nodeLoader = (Node) assetManager.loadModel("Models/Corvette/Base.mesh.xml");
		car.setChassis((Geometry) nodeLoader.getChild(0));
		
		BoundingBox box = (BoundingBox) car.getChassis().getModelBound();
		CollisionShape carHull = CollisionShapeFactory
				.createDynamicMeshShape(car.getChassis());
		car.setCollisionShape(carHull);

		// Create a vehicle control
		car.getNode().addControl(car);

		// Setting default values for wheels
		car.setSuspensionCompression(compValue * 2.0f
				* FastMath.sqrt(stiffness));
		car.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
		car.setSuspensionStiffness(stiffness);
		car.setMaxSuspensionForce(10000);

		// Create four wheels and add them at their locations
		// note that our fancy car actually goes backwards..
		Vector3f wheelDirection = new Vector3f(0, -1, 0);
		Vector3f wheelAxle = new Vector3f(-1, 0, 0);

//		Geometry wheel_fr = findGeom(carNode, "WheelFrontRight");
		nodeLoader = (Node) assetManager.loadModel("Models/Corvette/TireFR.mesh.xml");
		Geometry wheel_fr = (Geometry) nodeLoader.getChild(0);
		wheel_fr.center();
		box = (BoundingBox) wheel_fr.getModelBound();
		float wheelRadius = box.getYExtent();
		car.setWheelRadius(wheelRadius);
		
		float back_wheel_h = (wheelRadius * 1.7f) - 1f;
		float front_wheel_h = (wheelRadius * 1.9f) - 1f;
		car.addWheel(wheel_fr.getParent(),
				box.getCenter().add(0, -front_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, true);

//		Geometry wheel_fl = findGeom(carNode, "WheelFrontLeft");
		nodeLoader = (Node) assetManager.loadModel("Models/Corvette/TireFL.mesh.xml");
		Geometry wheel_fl = (Geometry) nodeLoader.getChild(0);
		wheel_fl.center();
		box = (BoundingBox) wheel_fl.getModelBound();
		car.addWheel(wheel_fl.getParent(),
				box.getCenter().add(0, -front_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, true);

//		Geometry wheel_br = findGeom(carNode, "WheelBackRight");
		nodeLoader = (Node) assetManager.loadModel("Models/Corvette/TireRR.mesh.xml");
		Geometry wheel_br = (Geometry) nodeLoader.getChild(0);
		wheel_br.center();
		box = (BoundingBox) wheel_br.getModelBound();
		car.addWheel(wheel_br.getParent(),
				box.getCenter().add(0, -back_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, false);

//		Geometry wheel_bl = findGeom(carNode, "WheelBackLeft");
		nodeLoader = (Node) assetManager.loadModel("Models/Corvette/TireRL.mesh.xml");
		Geometry wheel_bl = (Geometry) nodeLoader.getChild(0);
		wheel_bl.center();
		box = (BoundingBox) wheel_bl.getModelBound();
		car.addWheel(wheel_bl.getParent(),
				box.getCenter().add(0, -back_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, false);

		car.getWheel(0).setFrictionSlip(11f);
		car.getWheel(1).setFrictionSlip(11f);
		car.getWheel(2).setFrictionSlip(10f);
		car.getWheel(3).setFrictionSlip(10f);
	}
	
	private static Geometry findGeom(Spatial spatial, String name) {
		if (spatial instanceof Node) {
			Node node = (Node) spatial;
			for (int i = 0; i < node.getQuantity(); i++) {
				Spatial child = node.getChild(i);
				
				if (name.equals(child.getName()))	{
					;
				}
				
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

}
