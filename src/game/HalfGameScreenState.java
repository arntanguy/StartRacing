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
		
	}	
}
