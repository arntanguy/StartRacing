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

	private NiftyImage shiftlightGrey;
	private NiftyImage shiftlightRed;
	private NiftyImage shiftlightGreen;
	private NiftyImage shiftlightBlue;

	private CarProperties carProperties;
	private EnginePhysics enginePhysics;

	private final int margin = 100;
	private final int optimalMargin = 20;
	
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
		NiftyImage newImage = nifty.getRenderEngine().createImage(
				"Interface/Nifty/tachometer_7000.png", false);
		shiftlightGrey = nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_grey.png", false);
		shiftlightGreen = nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_green.png", false);
		shiftlightBlue = nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_blue.png", false);
		shiftlightRed = nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_red.png", false);

		// find the element with it's id
		Element element = screen.findElementByName("tachometer");
		// change the image with the ImageRenderer
		element.getRenderer(ImageRenderer.class).setImage(newImage);
		screen.findElementByName("shiftlightImage")
				.getRenderer(ImageRenderer.class).setImage(shiftlightGrey);
	}

	public void setRpm(int rpm) {
		int optimalShiftPoint = (int) carProperties
				.getOptimalShiftPoint(enginePhysics.getGear());
	
		if (rpm < optimalShiftPoint - margin) {
			screen.findElementByName("shiftlightImage")
					.getRenderer(ImageRenderer.class).setImage(shiftlightGrey);
		} else if (rpm >= optimalShiftPoint - optimalMargin
				&& rpm <= optimalShiftPoint + optimalMargin) {
			screen.findElementByName("shiftlightImage")
					.getRenderer(ImageRenderer.class).setImage(shiftlightBlue);
		} else if (rpm >= optimalShiftPoint - margin
				&& rpm <= optimalShiftPoint + margin) {
			screen.findElementByName("shiftlightImage")
					.getRenderer(ImageRenderer.class).setImage(shiftlightGreen);
		} else if (rpm > optimalShiftPoint + margin) {
			screen.findElementByName("shiftlightImage")
					.getRenderer(ImageRenderer.class).setImage(shiftlightRed);
		}
	}
}
