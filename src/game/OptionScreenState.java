package game;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;

public class OptionScreenState extends AbstractScreenController {

	private InputManager inputManager;

	public OptionScreenState() {
		super();
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);
		
		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);
	
		this.inputManager = app.getInputManager();
	}
	
	@Override
	public void onEndScreen() {
		stateManager.detach(this);
	}
	
	public void gotoMainMenu() {
		app.gotoStart();
	}
	
	public void applyOptions() {
		System.out.println("Option saved");
	}
	
	public String getMenuTitle() {
		return StringStore.OPTION_MENU_TITLE;
	}
}
