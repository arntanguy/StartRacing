package game;

import physics.CarProperties;
import physics.EnginePhysics;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;

public class AbstractShiftlight {

	protected Nifty nifty;
	protected Screen screen;

	protected CarProperties carProperties;
	protected EnginePhysics enginePhysics;

	public AbstractShiftlight(Nifty nifty, Screen screen, CarProperties carProperties,
			EnginePhysics enginePhysics) {
		this.nifty = nifty;
		this.screen = screen;
		this.carProperties = carProperties;
		this.enginePhysics = enginePhysics;
	}

	public void setRpm(int rpm) {
	}
}
