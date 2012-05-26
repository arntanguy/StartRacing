package game;

import ia.IA;

import java.util.concurrent.TimeUnit;

import physics.BMWM3Properties;
import physics.CarProperties;
import physics.DodgeViperProperties;
import physics.EnginePhysics;
import physics.SkylineProperties;
import physics.TypeCarProperties;
import physics.tools.Conversion;
import save.Comptes;
import save.ProfilCurrent;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import de.lessvoid.nifty.elements.render.TextRenderer;

public abstract class GameScreenState extends AbstractGameScreenState {
	protected Car bot;
	protected CarProperties botCarProperties;
	protected EnginePhysics botEnginePhysics;
	protected IA botIA;

	protected Vector3f botArrivalPoint;

	protected boolean botFinish;
	protected boolean botStoped;

	protected long timeBot;

	protected GhostControl finishCell;
	protected Node finishNode;
	
	private boolean win;

	public GameScreenState() {
		super();
	}

	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);
		
		win = false;

		try {
			initGame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void initGame() throws Exception {
		super.initGame();

		buildBot();
		buildFinishLine();
		initObjects();

		playerFinish = false;
		botFinish = false;
		botStoped = false;
	}

	private void initObjects() {
		BoxCollisionShape treeShape = new BoxCollisionShape(new Vector3f(0.5f,
				10.f, 1.f));

		Spatial node2 = assetManager.loadModel("Models/Tree/Tree.mesh.j3o");
		node2.setShadowMode(ShadowMode.Cast);
		node2.setName("Tree");
		node2.scale(5);
		for (int i = -15; i <= 15; i++) {
			Spatial node = node2.clone();
			node.setLocalTranslation(-20, 25, i * 60);

			Spatial nodeD = node2.clone();
			nodeD.setLocalTranslation(40, 25, i * 60);

			RigidBodyControl controlTree = new RigidBodyControl(treeShape, 0.f);
			controlTree.setUserObject("Tree");

			RigidBodyControl controlTreeD = new RigidBodyControl(treeShape, 0.f);
			controlTreeD.setUserObject("Tree");

			node.addControl(controlTree);
			nodeD.addControl(controlTreeD);
			getPhysicsSpace().add(node);
			getPhysicsSpace().add(nodeD);

			rootNode.attachChild(node);
			rootNode.attachChild(nodeD);
		}
	}

	protected void buildFinishLine() {
		// Init finish cell detection
		finishCell = new GhostControl(new BoxCollisionShape(new Vector3f(40, 1,
				1)));
		finishNode = new Node("finish zone");
		finishNode.addControl(finishCell);
		finishNode.move(0, 27, 0);

		rootNode.attachChild(finishNode);
		super.getPhysicsSpace().add(finishCell);
	}

	private void buildBot() {
		//botCarProperties = new BMWM3Properties();
		//XXX
		if (ProfilCurrent.getInstance() == null) {
			for (int i = 0; i < Comptes.getListCar().size(); ++i) {
				if (Comptes.getListCar().get(i).getTypeCar().equals(TypeCarProperties.BMWM3)) {
					botCarProperties = Comptes.getListCar().get(i);
					break;
				}
			}
		} else {
			botCarProperties = ProfilCurrent.getInstance().getCar().get
									(ProfilCurrent.getInstance().getChoixCar());
		}
	    
	    bot = new Car(assetManager, botCarProperties,
				"Models/FerrariGreen/Car.scene");

		bot = new Car(assetManager, botCarProperties, "ferrari green");

		bot.setPhysicsLocation(new Vector3f(10, 27, 700));
		bot.getNode().setShadowMode(ShadowMode.CastAndReceive);
		botEnginePhysics = bot.getEnginePhysics();
		botIA = bot.getIA();
		rootNode.attachChild(bot.getNode());
		super.getPhysicsSpace().add(bot);
		botArrivalPoint = (new Vector3f(0, 0, 0)).subtract(
				bot.getForwardVector(null)).mult(5000);
	}

	@Override
	public void update(float tpf) {
		/** any main loop action happens here */
		if (needReset) {
			reset();
			return;
		}
		super.update(tpf);

		if (botFinish && !botStoped) {
			bot.stop(1000);
			botStoped = true;
		}

		// Tester si le round est fini
		if (playerFinish && botFinish && !runFinish) {
			String text = "";

			if (timePlayer <= timeBot && !player.getBurstEnabled()) {
				text = "Gagne !\n ";
				win = true;
				audioRender.play("win");
			} else {
				audioRender.play("lost");
				text = "Perdu !\n ";
				win = false;
			}
			long secondes = TimeUnit.MILLISECONDS.toSeconds(timePlayer);
			long millisec = (timePlayer % 1000) / 10;

			String time = String.format("%d:%d",secondes, millisec);
			
			text += "Vous: " + time + "  /  ";
			
			text += String.format("Bot: %d:%d",TimeUnit.MILLISECONDS.toSeconds(timeBot),
					(timeBot % 1000) / 10);
			
			int argent = 0;
			int bonus = 0;

			if (ProfilCurrent.getInstance() != null) {
				// Enregistrement du temps lorsque le temps est meilleur que le
				// précédent
				if (this instanceof HalfGameScreenState) {
					if (win) {
						argent = (int) (120000 / secondes);
						ProfilCurrent.getInstance().setMonnaie
							(ProfilCurrent.getInstance().getMonnaie() + argent);
					}
					if (!ProfilCurrent.getInstance().getTimeDemi().equals("")) {
						String tps[] = ProfilCurrent.getInstance()
								.getTimeDemi().split(" : ");
						if (Long.parseLong(tps[0]) > secondes
								|| (Long.parseLong(tps[0]) == secondes && Long
										.parseLong(tps[1]) > millisec)) {
							ProfilCurrent.getInstance().setTimedemi(time);
							bonus = 2500;
							ProfilCurrent.getInstance().setMonnaie
								(ProfilCurrent.getInstance().getMonnaie() + bonus);
						}
					} else {
						ProfilCurrent.getInstance().setTimedemi(time);

					}
				} else if (this instanceof QuarterGameScreenState) {
					if (win) {
						argent = (int) (50000 / secondes);
						ProfilCurrent.getInstance().setMonnaie
							(ProfilCurrent.getInstance().getMonnaie() + argent);
					}
					if (!ProfilCurrent.getInstance().getTimeQuart().equals("")) {
						String tps[] = ProfilCurrent.getInstance().getTimeQuart().split(" : ");
						if (Long.parseLong(tps[0]) > secondes ||
								(Long.parseLong(tps[0]) == secondes && 
										Long.parseLong(tps[1]) > millisec)) {
							ProfilCurrent.getInstance().setTimequart(time);
							bonus = 1000;
							ProfilCurrent.getInstance().setMonnaie(ProfilCurrent.getInstance().getMonnaie() + bonus);
						}
					} else {
						ProfilCurrent.getInstance().setTimequart(time);
					}
				} //Quater
				if (argent != 0) {
					text += "\n" + argent;
					if (bonus != 0)
						text += " + " + bonus;
					text += " Eur";
				} else {
					if (bonus != 0)
						text += "\n" + bonus + " Eur";
				}
				Comptes.modifier(ProfilCurrent.getInstance());
				Comptes.Enregistrer();
			} //if(ProfilCurrent.getInstance() != null)
			
			screen.findElementByName("startTimer")
					.getRenderer(TextRenderer.class).setText(text);

			runFinish = true;
			runIsOn = false;
		}

		int botSpeed = (int) Math.abs(bot.getCurrentVehicleSpeedKmHour());
		if (runIsOn) {

			long timeMili = (System.currentTimeMillis() - startTime);

			String sTimer = String.format("%d : %d",
					TimeUnit.MILLISECONDS.toSeconds(timeMili),
					(timeMili % 1000) / 10);

			screen.findElementByName("timer").getRenderer(TextRenderer.class)
					.setText(sTimer);

			if (!botFinish) {
				botEnginePhysics.setSpeed(Math.abs(Conversion
						.kmToMiles(botSpeed)));
				bot.accelerate(-(float) botEnginePhysics.getForce() / 5);
				botIA.act();
				botIA.target(botArrivalPoint, 0, 0);
			} else if (player.getBurstEnabled()) {
				playerFinish = true;
			}
		}
	}

	protected void reset() {
		super.reset();

		bot.accelerate(0);
		bot.removeExplosion();
		botEnginePhysics.setSpeed(0);
		botEnginePhysics.setRpm(1000);

		bot.setPhysicsLocation(new Vector3f(10, 27, 700));
		bot.setPhysicsRotation(new Matrix3f());
		bot.setLinearVelocity(Vector3f.ZERO);
		bot.setAngularVelocity(Vector3f.ZERO);
		botEnginePhysics.setGear(1);
		bot.resetSuspension();

		bot.steer(0);

		bot.setLife(100);

		win = false;
		botFinish = false;
		runIsOn = false;
		needReset = false;
		runFinish = false;
		playerFinish = false;
		botFinish = false;
		botStoped = false;

		startTime = 0;
		countDown = 0;

		screen.findElementByName("timer").getRenderer(TextRenderer.class)
				.setText("0 : 0");
		screen.findElementByName("startTimer").getRenderer(TextRenderer.class)
				.setText("Ready ?");
	}

	@Override
	public void collision(PhysicsCollisionEvent event) {
		super.collision(event);
		if (finishCell.getOverlappingObjects().contains(player)
				&& !playerFinish) {
			timePlayer = (System.currentTimeMillis() - startTime);

			playerFinish = true;
		}
		if (finishCell.getOverlappingObjects().contains(bot) && !botFinish) {
			timeBot = (System.currentTimeMillis() - startTime);
			System.out.println(String.format("player : %d : %d",
					TimeUnit.MILLISECONDS.toSeconds(timeBot),
					(timeBot % 1000) / 10));

			botFinish = true;
		}

	}
}
