package game;

import physics.CarProperties;
import physics.EnginePhysics;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;

/**
 * Displays a shiftlight to know when to change the rpm values
 * 
 * @author TANGUY Arnaud
 * 
 */
public abstract class AbstractShiftlight {

	protected Nifty nifty;
	protected Screen screen;

	protected CarProperties carProperties;
	protected EnginePhysics enginePhysics;

	public AbstractShiftlight(Nifty nifty, Screen screen,
			CarProperties carProperties, EnginePhysics enginePhysics) {
		this.nifty = nifty;
		this.screen = screen;
		this.carProperties = carProperties;
		this.enginePhysics = enginePhysics;
	}

	public void setRpm(int rpm) {
	}
}
