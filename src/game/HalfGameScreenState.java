package game;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;


public class HalfGameScreenState extends GameScreenState {

	/******* Initialize game ******/
	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);
		finishNode.setLocalTranslation(0, 27, -124);
		
	}	
	
}
