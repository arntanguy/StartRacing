package game;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;

public class DigitalDisplay {
	// screen.findElementByName("hint_text").getRenderer(TextRenderer.class).changeText(message);
	private Nifty nifty;
	private Screen screen;
	private Element element;
	/**
	 * Delay in ms between two updates
	 */
	private int delay;
	private long time;

	public DigitalDisplay(Nifty nifty, Screen screen, String elementId,
			int delay) {
		this.nifty = nifty;
		this.screen = screen;

		this.element = screen.findElementByName(elementId);
		this.time = System.currentTimeMillis();
		this.delay = delay;
	}

	public void setText(String text) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - time > delay) {
			element.getRenderer(TextRenderer.class).setText(text);
			time = currentTime;
		}
	}
}
