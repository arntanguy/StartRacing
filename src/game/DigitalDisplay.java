package game;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;

public class DigitalDisplay {
	
	private Label label;
	/**
	 * Delay in ms between two updates
	 */
	private int delay;
	private long time;

	public DigitalDisplay(Nifty nifty, Screen screen, String elementId,
			int delay) {
		this.label = screen.findNiftyControl(elementId, Label.class);
		this.time = System.currentTimeMillis();
		this.delay = delay;
	}

	public void setText(String text) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - time > delay) {
			label.setText(text);
			time = currentTime;
		}
	}
}
