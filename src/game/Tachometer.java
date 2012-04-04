package game;

import physics.CarProperties;
import physics.EnginePhysics;

import com.jme3.math.Vector2f;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;

public class Tachometer {
	private double alpha;
	private Vector2f start;
	private Vector2f end;

	private Nifty nifty;
	private Screen screen;

	private NiftyImage tachometer;
	private NiftyImage tachometerNeedle;
	private Element tachometerNeedleElement;

	private CarProperties carProperties;
	private EnginePhysics enginePhysics;

	public Tachometer(Nifty nifty, Screen screen, CarProperties carProperties,
			EnginePhysics enginePhysics) {
		this.nifty = nifty;
		this.screen = screen;
		this.carProperties = carProperties;
		this.enginePhysics = enginePhysics;

		// first load the new image
		/*
		 * false means don't linear filter the image, true would apply linear
		 * filtering
		 */
		tachometer = nifty.getRenderEngine().createImage(
				"Interface/Nifty/tachometer_7000.png", false);
		tachometerNeedle = nifty.getRenderEngine().createImage(
				"Interface/Nifty/tachometer_needle.png", false);

		tachometerNeedleElement = screen.findElementByName("tachometerNeedle");
		screen.findElementByName("tachometer").getRenderer(ImageRenderer.class)
				.setImage(tachometer);
		tachometerNeedleElement.getRenderer(ImageRenderer.class).setImage(
				tachometerNeedle);
	}

	public void setRpm(int rpm) {

	}
}
