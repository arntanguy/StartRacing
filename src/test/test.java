package test;


import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
 
/**
 * Example 12 - how to give objects physical properties so they bounce and fall.
 * @author base code by double1984, updated by zathras
 */
public class test extends SimpleApplication implements ActionListener {
 
  public static void main(String args[]) {
    test app = new test();
    app.start();
  }
 
  /** Prepare the Physics Application State (jBullet) */
  private BulletAppState bulletAppState;
 
  /** Prepare Materials */
  Material floor_mat;
 
  /** Prepare geometries and physical nodes for bricks and cannon balls. */
  private RigidBodyControl    floor_phy;
  private static final Box    floor;
 

  /** Stuff for the car   */
private CompoundCollisionShape compoundShape;
private BoxCollisionShape box;
private Node vehicleNode;
private VehicleControl vehicle;
float stiffness = 6.0f;//200=f1 car
float compValue = .3f; //(should be lower than damp)
float dampValue = .4f;

private final float accelerationForce = 1000.0f;
private final float brakeForce = 100.0f;
private float steeringValue = 0;
private float accelerationValue = 0f;
private Vector3f jumpForce = new Vector3f(0, 3000, 0);
 
  static {
    /** Initialize the floor geometry */
    floor = new Box(Vector3f.ZERO, 100f, 0.1f, 50f);
    floor.scaleTextureCoordinates(new Vector2f(3, 6));
  }
 
  @Override
  public void simpleInitApp() {
    /** Set up Physics Game */
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    bulletAppState.getPhysicsSpace().enableDebug(assetManager);
 

    /** Initialize the scene, materials, and physics space */
    initMaterials();
    initFloor();
    initCar();
    setupKeys();

  }
 
 
  /** Initialize the materials used in this scene. */
  public void initMaterials() {
    TextureKey key = new TextureKey("Textures/Terrain/BrickWall/BrickWall.jpg");
    key.setGenerateMips(true);
 
    TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
    key2.setGenerateMips(true);

    floor_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key3 = new TextureKey("Textures/Terrain/Pond/Pond.jpg");
    key3.setGenerateMips(true);
    Texture tex3 = assetManager.loadTexture(key3);
    tex3.setWrap(WrapMode.Repeat);
    floor_mat.setTexture("ColorMap", tex3);
  }
 
  /** Make a solid floor and add it to the scene. */
  public void initFloor() {
    Geometry floor_geo = new Geometry("Floor", floor);
    floor_geo.setMaterial(floor_mat);
    floor_geo.setLocalTranslation(0, -0.1f, 0);
    this.rootNode.attachChild(floor_geo);
    /* Make the floor physical with mass 0.0f! */
    floor_phy = new RigidBodyControl(0.0f);
    floor_geo.addControl(floor_phy);
    bulletAppState.getPhysicsSpace().add(floor_phy);
  }
  
  private void setupKeys() {
      inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_H));
      inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_K));
      inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_U));
      inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_J));
      inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
      inputManager.addMapping("Reset", new KeyTrigger(KeyInput.KEY_RETURN));
      
      inputManager.addListener(this, "Lefts");
      inputManager.addListener(this, "Rights");
      inputManager.addListener(this, "Ups");
      inputManager.addListener(this, "Downs");
      inputManager.addListener(this, "Space");
      inputManager.addListener(this, "Reset");
  }
  
  public void initCar()	{
	//create a compound shape and attach the BoxCollisionShape for the car body at 0,1,0
      //this shifts the effective center of mass of the BoxCollisionShape to 0,-1,0
	  compoundShape = new CompoundCollisionShape();
      box = new BoxCollisionShape(new Vector3f(1.2f, 0.5f, 2.4f));
      compoundShape.addChildShape(box, new Vector3f(0, 1, 0));


      //create vehicle node
      vehicleNode=new Node("vehicleNode");
      vehicle = new VehicleControl(compoundShape, 400);
      vehicleNode.addControl(vehicle);


      //setting suspension values for wheels, this can be a bit tricky
      //see also https://docs.google.com/Doc?docid=0AXVUZ5xw6XpKZGNuZG56a3FfMzU0Z2NyZnF4Zmo&hl=en
      float stiffness = 60.0f;//200=f1 car
      float compValue = .3f; //(should be lower than damp)
      float dampValue = .4f;
      vehicle.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
      vehicle.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
      vehicle.setSuspensionStiffness(stiffness);
      vehicle.setMaxSuspensionForce(10000.0f);
	  
	  Vector3f wheelDirection = new Vector3f(0, -1, 0);
	  Vector3f wheelAxle = new Vector3f(-1, 0, 0);
	  float radius = 0.5f;
	  float restLength = 0.3f;
	  float yOff = 0.5f;
	  float xOff = 1f;
	  float zOff = 2f;
	  
	  Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
      mat.getAdditionalRenderState().setWireframe(true);
      mat.setColor("Color", ColorRGBA.Red);
	  
	  Cylinder wheelMesh = new Cylinder(16, 16, radius, radius * 0.6f, true);
	  Node node1 = new Node("wheel 1 node");
	  Geometry wheels1 = new Geometry("wheel 1", wheelMesh);
	  node1.attachChild(wheels1);
	  wheels1.rotate(0, FastMath.HALF_PI, 0);
	  wheels1.setMaterial(mat);
	   
	  vehicle.addWheel(node1, new Vector3f(-xOff, yOff, zOff), wheelDirection, wheelAxle, restLength, radius, true);
	  Node node2 = new Node("wheel 2 node");
      Geometry wheels2 = new Geometry("wheel 2", wheelMesh);
      node2.attachChild(wheels2);
      wheels2.rotate(0, FastMath.HALF_PI, 0);
      wheels2.setMaterial(mat);
      vehicle.addWheel(node2, new Vector3f(xOff, yOff, zOff), wheelDirection, wheelAxle, restLength, radius, true);


      Node node3 = new Node("wheel 3 node");
      Geometry wheels3 = new Geometry("wheel 3", wheelMesh);
      node3.attachChild(wheels3);
      wheels3.rotate(0, FastMath.HALF_PI, 0);
      wheels3.setMaterial(mat);
      vehicle.addWheel(node3, new Vector3f(-xOff, yOff, -zOff), wheelDirection, wheelAxle, restLength, radius, false);


      Node node4 = new Node("wheel 4 node");
      Geometry wheels4 = new Geometry("wheel 4", wheelMesh);
      node4.attachChild(wheels4);
      wheels4.rotate(0, FastMath.HALF_PI, 0);
      wheels4.setMaterial(mat);
      vehicle.addWheel(node4, new Vector3f(xOff, yOff, -zOff), wheelDirection, wheelAxle, restLength, radius, false);


      vehicleNode.attachChild(node1);
      vehicleNode.attachChild(node2);
      vehicleNode.attachChild(node3);
      vehicleNode.attachChild(node4);

      
      rootNode.attachChild(vehicleNode);
      getPhysicsSpace().add(vehicle);
	  
  }
  
  
  
  public void onAction(String binding, boolean value, float tpf) {
      if (binding.equals("Lefts")) {
          if (value) {
              steeringValue += .5f;
          } else {
              steeringValue += -.5f;
          }
          vehicle.steer(steeringValue);
      } else if (binding.equals("Rights")) {
          if (value) {
              steeringValue += -.5f;
          } else {
              steeringValue += .5f;
          }
          vehicle.steer(steeringValue);
      } else if (binding.equals("Ups")) {
          if (value) {
              accelerationValue += accelerationForce;
          } else {
              accelerationValue -= accelerationForce;
          }
          vehicle.accelerate(accelerationValue);
      } else if (binding.equals("Downs")) {
          if (value) {
              vehicle.brake(brakeForce);
          } else {
              vehicle.brake(0f);
          }
      } else if (binding.equals("Space")) {
          if (value) {
        	  System.out.println("jump");
              vehicle.applyImpulse(jumpForce, Vector3f.ZERO);
          }
      } else if (binding.equals("Reset")) {
          if (value) {
              System.out.println("Reset");
              vehicle.setPhysicsLocation(Vector3f.ZERO);
              vehicle.setPhysicsRotation(new Matrix3f());
              vehicle.setLinearVelocity(Vector3f.ZERO);
              vehicle.setAngularVelocity(Vector3f.ZERO);
              vehicle.resetSuspension();
          } else {
          }
      }
  }
  
  @Override
  public void simpleUpdate(float tpf) {
      cam.lookAt(vehicle.getPhysicsLocation(), Vector3f.UNIT_Y);
  }
  
  private PhysicsSpace getPhysicsSpace(){
      return bulletAppState.getPhysicsSpace();
  }
 
}