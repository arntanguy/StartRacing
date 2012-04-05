package game;

import java.util.LinkedList;

import physics.CarProperties;
import physics.EnginePhysics;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;

public class ShiftlightFormule1 extends AbstractShiftlight {

	private NiftyImage idleImage;
	private LinkedList<NiftyImage> greenImages;
	private LinkedList<NiftyImage> yellowImages;
	private LinkedList<NiftyImage> redImages;

	private Element shiftlightElement;

	private int yellowMargin = 100;
	private int currentImage = 0;
	private int optimalShiftPoint;
	private int minimal;

	private int ospY;

	private int zoneGreen;
	private int zoneYellow;
	private int zoneRed;

	public ShiftlightFormule1(Nifty nifty, Screen screen,
			CarProperties carProperties, EnginePhysics enginePhysics) {
		super(nifty, screen, carProperties, enginePhysics);

		greenImages = new LinkedList<NiftyImage>();
		yellowImages = new LinkedList<NiftyImage>();
		redImages = new LinkedList<NiftyImage>();

		idleImage = nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_f1_0.png", false);
		greenImages.add(nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_f1_1.png", false));
		greenImages.add(nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_f1_2.png", false));
		greenImages.add(nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_f1_3.png", false));
		greenImages.add(nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_f1_4.png", false));
		yellowImages.add(nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_f1_5.png", false));
		yellowImages.add(nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_f1_6.png", false));
		yellowImages.add(nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_f1_7.png", false));
		redImages.add(nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_f1_8.png", false));
		redImages.add(nifty.getRenderEngine().createImage(
				"Interface/Nifty/shiftlight_f1_9.png", false));
		shiftlightElement = screen.findElementByName("shiftlight");

		shiftlightElement.getRenderer(ImageRenderer.class).setImage(idleImage);

		optimalShiftPoint = (int) carProperties
				.getOptimalShiftPoint(enginePhysics.getGear());
		ospY = optimalShiftPoint - yellowMargin;
		zoneGreen = ospY / greenImages.size();
		zoneYellow = 2 * yellowMargin;
		zoneRed = carProperties.getRedline()
				- (optimalShiftPoint + yellowMargin);
		minimal = optimalShiftPoint - 1500;
	}

	@Override
	public void setRpm(int rpm) {
		int img = currentImage;
		if (rpm <= minimal) {
			shiftlightElement.getRenderer(ImageRenderer.class).setImage(
					idleImage);
		} else if (rpm <= ospY) {
			img = rpm / zoneGreen;
			img = (img < greenImages.size()) ? img : greenImages.size() - 1;
			if (img != currentImage) {
				shiftlightElement.getRenderer(ImageRenderer.class).setImage(
						greenImages.get(img));
			}
		} else if (rpm <= optimalShiftPoint + yellowMargin) {
			img = (rpm - ospY) / zoneYellow;
			img = (img < yellowImages.size()) ? img : yellowImages.size() - 1;
			if (img != currentImage) {
				shiftlightElement.getRenderer(ImageRenderer.class).setImage(
						yellowImages.get(img));
			}
		} else if (rpm > optimalShiftPoint + yellowMargin) {
			img = (rpm - (optimalShiftPoint + yellowMargin)) / zoneRed;
			img = (img < redImages.size()) ? img : redImages.size() - 1;
			if (img != currentImage) {

				shiftlightElement.getRenderer(ImageRenderer.class).setImage(
						redImages.get(img));
			}
		}
		currentImage = img;
	}
}
