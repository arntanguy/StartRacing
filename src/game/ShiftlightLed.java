package game;

import physics.CarProperties;
import physics.EnginePhysics;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;

public class ShiftlightLed {

	private Nifty nifty;
	private Screen screen;

	private Element shiftlightElement;
	private NiftyImage shiftlightGrey;
	private NiftyImage shiftlightRed;
	private NiftyImage shiftlightGreen;
	private NiftyImage shiftlightBlue;

	private CarProperties carProperties;
	private EnginePhysics enginePhysics;

	private final int margin = 500;
	private final int optimalMargin = 40;

	public ShiftlightLed(Nifty nifty, Screen screen,
			CarProperties carProperties, EnginePhysics enginePhysics) {
		this.nifty = nifty;
		this.screen = screen;
		this.carProperties = carProperties;
		this.enginePhysics = enginePhysics;

		shiftlightGrey = nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_grey.png", false);
		shiftlightGreen = nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_green.png", false);
		shiftlightBlue = nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_blue.png", false);
		shiftlightRed = nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_red.png", false);

		shiftlightElement = screen.findElementByName("shiftlight");
		shiftlightElement.getRenderer(ImageRenderer.class).setImage(
				shiftlightGrey);
	}

	public void setRpm(int rpm) {
		int optimalShiftPoint = (int) carProperties
				.getOptimalShiftPoint(enginePhysics.getGear());

		if (enginePhysics.getGear() == carProperties.getNbGears()) {
			if (rpm >= carProperties.getRedline()) {
				shiftlightElement.getRenderer(ImageRenderer.class).setImage(
						shiftlightRed);
			} else {
				shiftlightElement.getRenderer(ImageRenderer.class).setImage(
						shiftlightGrey);
			}
		} else {

			if (rpm < optimalShiftPoint - margin) {
				shiftlightElement.getRenderer(ImageRenderer.class).setImage(
						shiftlightGrey);
			} else if (rpm >= optimalShiftPoint - optimalMargin
					&& rpm <= optimalShiftPoint + optimalMargin) {
				shiftlightElement.getRenderer(ImageRenderer.class).setImage(
						shiftlightBlue);
			} else if (rpm >= optimalShiftPoint - margin
					&& rpm <= optimalShiftPoint + margin) {
				shiftlightElement.getRenderer(ImageRenderer.class).setImage(
						shiftlightGreen);
			} else if (rpm > optimalShiftPoint + margin) {
				shiftlightElement.getRenderer(ImageRenderer.class).setImage(
						shiftlightRed);
			}
		}
	}
}
