package game;

import physics.tools.MathTools;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;


public class HalfGameScreenState extends GameScreenState {

	/******* Initialize game ******/
	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);
		finishNode.setLocalTranslation(0, 27, -124);
		
		initObjects();
	}
	
	private void initObjects() {
		BoxCollisionShape treeShape = new BoxCollisionShape(new Vector3f(0.5f,
				10.f, 1.f));
		BoxCollisionShape plotShape = new BoxCollisionShape(new Vector3f(0.5f,
				0.6f, 1.f));

		Spatial node2 = assetManager.loadModel("Models/Tree/Tree.mesh.j3o");
		node2.setShadowMode(ShadowMode.Cast);
		node2.setName("Tree");
		node2.scale(5);
		for (int i = 0; i < 60; i++) {
			Spatial node = node2.clone();
			node.setLocalTranslation(-20, 25,
					i*40);
			
			Spatial nodeD = node2.clone();
			nodeD.setLocalTranslation(40, 25, i*40);

			RigidBodyControl controlTree = new RigidBodyControl(treeShape, 0.f);
			controlTree.setUserObject("Tree");

			RigidBodyControl controlTreeD = new RigidBodyControl(treeShape, 0.f);
			controlTreeD.setUserObject("Tree");

			
			node.addControl(controlTree);
			nodeD.addControl(controlTreeD);
			getPhysicsSpace().add(node);
			getPhysicsSpace().add(nodeD);

			rootNode.attachChild(node);
			rootNode.attachChild(nodeD);
		}

		Spatial node = assetManager
				.loadModel("Models/cone_altglass/cone_altglass.j3o");
		node.scale(0.2f);
		rootNode.attachChild(node);
		node.setShadowMode(ShadowMode.Cast);
		node.setLocalTranslation(0, 27, 200);

		for (int i = 0; i < 200; i++) {
			Spatial plot = node.clone();
			plot.setLocalTranslation(MathTools.randBetween(-1000, 1000), 27,
					MathTools.randBetween(-1000, 1000));

			RigidBodyControl controlPlot = new RigidBodyControl(plotShape);
			controlPlot.setMass(0.75f);
			controlPlot.setFriction(10);

			plot.addControl(controlPlot);
			getPhysicsSpace().add(plot);
			rootNode.attachChild(plot);
		}
	}
	

}
