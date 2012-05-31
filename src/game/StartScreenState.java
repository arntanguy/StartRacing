package game;

import java.util.Hashtable;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class StartScreenState extends AbstractScreenController {

	private InputManager inputManager;
	private Hashtable<Effect, Element> hoverEffects;

	public StartScreenState() {
		super();
		hoverEffects = new Hashtable<Effect, Element>();
	}

	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);

		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);

		this.inputManager = app.getInputManager();
		voiceRender.setRootNode(app.getGuiNode());
		
		setInputMappings();

		initNiftyCallbacks(nifty.getCurrentScreen().getFocusHandler()
				.getFirstFocusElement().getParent());
	}
	
	private void setInputMappings() {		
		inputManager.addMapping("quitGame", new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean keyPressed, float arg2) {
				if (keyPressed) /* Avoid double strikes! */
					app.stop();
			}
		}, "quitGame");
		
		inputManager.addMapping("quickPlay", new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				app.gotoFreeForAll();
			}
		}, "quickPlay");
		
		inputManager.addMapping("halfGame", new KeyTrigger(KeyInput.KEY_H));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				app.gotoGame("half");
			}
		}, "halfGame");
		
		inputManager.addMapping("quarterGame", new KeyTrigger(KeyInput.KEY_H));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				app.gotoGame("");
			}
		}, "halfGame");
		
		inputManager.addMapping("options", new KeyTrigger(KeyInput.KEY_O));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				app.gotoOptions();
			}
		}, "options");
		
		inputManager.addMapping("createProfil", new KeyTrigger(KeyInput.KEY_C));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				app.gotoCrtProfil();
			}
		}, "createProfil");
		
		inputManager.addMapping("chooseProfil", new KeyTrigger(KeyInput.KEY_P));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				app.gotoAffProfil();
			}
		}, "chooseProfil");
		
		inputManager.addMapping("achat", new KeyTrigger(KeyInput.KEY_B));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				app.gotoAchat();
			}
		}, "achat");
		
		inputManager.addMapping("garage", new KeyTrigger(KeyInput.KEY_G));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				app.gotoGarage();
			}
		}, "garage");
		
		inputManager.addMapping("tutoriel", new KeyTrigger(KeyInput.KEY_T));
		inputManager.addListener(new ActionListener() {
			public void onAction(String arg0, boolean arg1, float arg2) {
				app.gotoTutoriel();
			}
		}, "tutoriel");
	}

	@Override
	public void onStartScreen() {
		if(voiceRender != null) {
			voiceRender.stopAndReset();
		}
	}
	
	public void initNiftyCallbacks(Element root) {
		if (root == null)
			return;
		List<Element> elements = root.getElements();
		// get all effects attached to this element (please note that you get a
		// list back and that you need to provide the actual effect class)
		List<Effect> hoverHintEffects = null;
		for (Element e : elements) {
			hoverHintEffects = e.getEffects(EffectEventId.onFocus,
					EffectImpl.class);
			if (hoverHintEffects != null && hoverHintEffects.size() > 0) {
				for (int i = 0; i < hoverHintEffects.size(); i++) {
					hoverEffects.put(hoverHintEffects.get(i), e);
				}
			}
		}
	}

	@Override
	public void update(float tpf) {
		/** any main loop action happens here */

		for (Effect e : hoverEffects.keySet()) {
			if (e.isActive()) {
				//System.out.println("hover "
				//		+ hoverEffects.get(e));
				try {
					voiceRender.playVoice(hoverEffects.get(e).getId());
				} catch(Exception ex) {
					System.err.println(ex);
				}
			}
		}

	}

	@Override
	public void stateAttached(AppStateManager stateManager) {
	}

	@Override
	public void stateDetached(AppStateManager stateManager) {
	}

	@Override
	public void bind(Nifty nifty, Screen screen) {
		super.bind(nifty, screen);
	}

	@Override
	public void onEndScreen() {
		inputManager.clearMappings();
		stateManager.detach(this);
	}

	public void startGame(String nextScreen) {
		voiceRender.stopAndReset();
		app.gotoGame(nextScreen);
	}

	public void startFreeForAll() {
//		System.out.println("START free for all");
		voiceRender.stopAndReset();
		app.gotoFreeForAll();
	}

	public void showOptions() {
		app.gotoOptions();
	}

	public void newProfil() {
		app.gotoCrtProfil();
	}

	public void showTutoriel() {
		app.gotoTutoriel();
	}
	
	public void chooseProfil() {
		// XXX
		app.gotoAffProfil();
	}

	public void quitGame() {
		app.stop();
	}

	public void hover(String name) {
		System.out.println("Hover: " + name);
		try {
			voiceRender.play(name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	/**
	 * Permet d'interfacer avec le format XML.
	 * 
	 * @return Le titre de l'application
	 */
	public static String getTitle() {
		return StringStore.APP_TITLE;
	}
}
