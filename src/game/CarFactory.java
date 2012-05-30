package game;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;

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

	public static void createBuggy(AssetManager assetManager, Car car)	{
		float stiffness = car.getProperties().getStiffness();// 200=f1 car
		float compValue = car.getProperties().getCompValue(); // (lower than damp!)
		float dampValue = car.getProperties().getDampValue();
		final float mass = car.getProperties().getMass();

		car.setMass(mass);

		// Load model and get chassis Geometry
		car.setNode((Node) assetManager.loadModel("Models/Buggy/Buggy.j3o"));
		car.getNode().setShadowMode(ShadowMode.Cast);

		// Create a hull collision shape for the chassis
		car.setChassis(findGeom(car.getNode(), "Chassis-geom-1"));

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

		Geometry wheel_fr = findGeom(car.getNode(), "Wheel_RF-geom-1");
		//		wheel_fr.center();
		box = (BoundingBox) wheel_fr.getModelBound();
		float wheelRadius = box.getYExtent();
		car.setWheelRadius(wheelRadius);

		float back_wheel_h = (wheelRadius * 1.7f) - 1f;
		float front_wheel_h = (wheelRadius * 1.9f) - 1f;
		car.addWheel(wheel_fr.getParent(),
				box.getCenter().add(0, -front_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, true);

		Geometry wheel_fl = findGeom(car.getNode(), "Wheel_LF-geom-1");
		//		wheel_fl.center();
		box = (BoundingBox) wheel_fl.getModelBound();
		car.addWheel(wheel_fl.getParent(),
				box.getCenter().add(0, -front_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, true);

		Geometry wheel_br = findGeom(car.getNode(), "Wheel_RR-geom-1");
		//		wheel_br.center();
		box = (BoundingBox) wheel_br.getModelBound();
		car.addWheel(wheel_br.getParent(),
				box.getCenter().add(0, -back_wheel_h, 0), wheelDirection,
				wheelAxle, 0.2f, wheelRadius, false);

		Geometry wheel_bl = findGeom(car.getNode(), "Wheel_LR-geom-1");
		//		wheel_bl.center();
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
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	
		mat.getAdditionalRenderState().setWireframe(true);
		mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
//		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		


		//create a compound shape and attach the BoxCollisionShape for the car body at 0,1,0
		//this shifts the effective center of mass of the BoxCollisionShape to 0,-1,0
		CompoundCollisionShape compoundShape = new CompoundCollisionShape();
		BoxCollisionShape box = new BoxCollisionShape(new Vector3f(1.2f, 0.5f, 2.4f));
		compoundShape.addChildShape(box, new Vector3f(0, 0.8f, 0));


		//create vehicle node
		car.setNode((Node) assetManager.loadModel("Models/Corvette/corvette.scene"));
		car.getNode().scale(1.5f);
		car.setCollisionShape(compoundShape);
		car.getNode().addControl(car);
		car.getEnginePhysics().setBackward(false);


		float stiffness = car.getProperties().getStiffness();// 200=f1 car
		float compValue = car.getProperties().getCompValue(); // (lower than damp!)
		float dampValue = car.getProperties().getDampValue();
		final float mass = car.getProperties().getMass();

		car.setMass(mass);
		// Setting default values for wheels
		car.setSuspensionCompression(compValue * 2.0f
				* FastMath.sqrt(stiffness));
		car.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
		car.setSuspensionStiffness(stiffness);
		car.setMaxSuspensionForce(10000);


		//Create four wheels and add them at their locations
		Vector3f wheelDirection = new Vector3f(0, -1, 0); // was 0, -1, 0
		Vector3f wheelAxle = new Vector3f(-1, 0, 0); // was -1, 0, 0
		float radius = 0.28f;
		float restLength = 0.3f;
		float yOff = 0.6f;
		float xOff = 1.3f;
		float zOff = 2f;


		Cylinder wheelMesh = new Cylinder(16, 16, radius, radius * 0.6f, true);


		Node node1 = new Node("wheel 1 node");
		Geometry wheels1 = new Geometry("wheel 1", wheelMesh);
		node1.attachChild(wheels1);
		wheels1.rotate(0, FastMath.HALF_PI, 0);
		wheels1.setMaterial(mat);
//		wheels1.setQueueBucket(Bucket.Transparent);
		car.addWheel(node1, new Vector3f(-xOff, yOff, zOff),
				wheelDirection, wheelAxle, restLength, radius, true);


		Node node2 = new Node("wheel 2 node");
		Geometry wheels2 = new Geometry("wheel 2", wheelMesh);
		node2.attachChild(wheels2);
		wheels2.rotate(0, FastMath.HALF_PI, 0);
		wheels2.setMaterial(mat);
//		wheels2.setQueueBucket(Bucket.Transparent);
		car.addWheel(node2, new Vector3f(xOff, yOff, zOff),
				wheelDirection, wheelAxle, restLength, radius, true);


		Node node3 = new Node("wheel 3 node");
		Geometry wheels3 = new Geometry("wheel 3", wheelMesh);
		node3.attachChild(wheels3);
		wheels3.rotate(0, FastMath.HALF_PI, 0);
		wheels3.setMaterial(mat);
//		wheels3.setQueueBucket(Bucket.Transparent);
		car.addWheel(node3, new Vector3f(-xOff, yOff, -zOff),
				wheelDirection, wheelAxle, restLength, radius, false);


		Node node4 = new Node("wheel 4 node");
		Geometry wheels4 = new Geometry("wheel 4", wheelMesh);
		node4.attachChild(wheels4);
		wheels4.rotate(0, FastMath.HALF_PI, 0);
		wheels4.setMaterial(mat);
//		wheels4.setQueueBucket(Bucket.Transparent);
		car.addWheel(node4, new Vector3f(xOff, yOff, -zOff),
				wheelDirection, wheelAxle, restLength, radius, false);


		car.getNode().attachChild(node1);
		car.getNode().attachChild(node2);
		car.getNode().attachChild(node3);
		car.getNode().attachChild(node4);

		car.getNode().rotate(0, (float)Math.PI, 0);
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
