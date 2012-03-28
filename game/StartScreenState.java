package game;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.math.ColorRGBA;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class StartScreenState extends AbstractAppState implements ScreenController {
	private SimpleApplication app;

	private Nifty nifty;
	private NiftyJmeDisplay niftyDisplay;

	private ViewPort viewPort;
	private Node rootNode;
	private Node guiNode;
	private AssetManager assetManager;
	private Node localRootNode = new Node("Start Screen RootNode");
	private Node localGuiNode = new Node("Start Screen GuiNode");
	private final ColorRGBA backgroundColor = ColorRGBA.Gray;

	private AudioRenderer audioRenderer;

	private InputManager inputManager;

	private ViewPort guiViewport;

	public StartScreenState(SimpleApplication app) {
		this.rootNode = app.getRootNode();
		this.viewPort = app.getViewPort();
		this.guiNode = app.getGuiNode();
		this.assetManager = app.getAssetManager();
		this.audioRenderer = app.getAudioRenderer();
		this.inputManager = app.getInputManager();
		this.guiViewport = app.getGuiViewPort();
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		/** init the screen */
		super.initialize(stateManager, app);
		this.app = (SimpleApplication) app;

		niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager,
				audioRenderer, guiViewport);
		nifty = niftyDisplay.getNifty();
		// nifty.fromXml("Interface/Nifty/HelloJme.xml", "start", this);
		nifty.fromXml("Interface/Nifty/StartScreen.xml", "start", this);

		// attach the nifty display to the gui view port as a processor
		guiViewport.addProcessor(niftyDisplay);

		// disable the fly cam
		// flyCam.setEnabled(false);
		// flyCam.setDragToRotate(true);
		inputManager.setCursorVisible(true);
	}

	@Override
	public void update(float tpf) {
		/** any main loop action happens here */
	}

	@Override
	public void stateAttached(AppStateManager stateManager) {
		rootNode.attachChild(localRootNode);
		guiNode.attachChild(localGuiNode);
		viewPort.setBackgroundColor(backgroundColor);
	}

	@Override
	public void stateDetached(AppStateManager stateManager) {
		rootNode.detachChild(localRootNode);
		guiNode.detachChild(localGuiNode);
		guiViewport.removeProcessor(niftyDisplay);
	}

	@Override
	public void bind(Nifty arg0, Screen arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub
		
	}
	
	public void startGame(String nextScreen) {
		nifty.gotoScreen("hud");
	}
	
	public void quitGame() {
		app.stop();
	}

}