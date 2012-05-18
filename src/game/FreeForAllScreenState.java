package game;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import physics.BMWM3Properties;
import physics.CarProperties;
import physics.tools.Conversion;
import physics.tools.MathTools;
import save.Comptes;
import save.ProfilCurrent;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import de.lessvoid.nifty.elements.render.TextRenderer;

public class FreeForAllScreenState extends AbstractGameScreenState {

	private ArrayList<Car> bots;
	private boolean win = false;

	private boolean givePt = false;
	private boolean bonus = false;
	private int nbBotDead = 0;
	private int argent = 0;
	
	private DigitalDisplay digitalLife;

	protected GhostControl finishCell;
	protected Node finishNode;
	
	public FreeForAllScreenState() {
		super();
	}

	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);

		bots = new ArrayList<Car>();

		initGame();
		initNiftyControls();
	}

	protected void initGame() {
		super.initGame();

		player.setPhysicsLocation(new Vector3f(0, 27, 700));
		BMWM3Properties properties = new BMWM3Properties();
		addBot(new Vector3f(new Vector3f(10, 27, 700)), properties);

		addBot(new Vector3f(new Vector3f(20, 27, 800)), properties);
		addBot(new Vector3f(new Vector3f(30, 27, 500)), properties);
		addBot(new Vector3f(new Vector3f(40, 27, 600)), properties);
		addBot(new Vector3f(new Vector3f(40, 27, 600)), properties);
		addBot(new Vector3f(new Vector3f(300, 27, 800)), properties);
		addBot(new Vector3f(new Vector3f(200, 27, 700)), properties);

		/*
		 * addBot(new Vector3f(new Vector3f(100, 27, 600)), new
		 * BMWM3Properties()); addBot(new Vector3f(new Vector3f(400, 27, 600)),
		 * new BMWM3Properties()); addBot(new Vector3f(new Vector3f(500, 27,
		 * 500)), new BMWM3Properties()); addBot(new Vector3f(new Vector3f(70,
		 * 27, 650)), new BMWM3Properties()); addBot(new Vector3f(new
		 * Vector3f(90, 27, 600)), new BMWM3Properties()); addBot(new
		 * Vector3f(new Vector3f(0, 27, 600)), new BMWM3Properties());
		 */

		resetCars();

		addObjects();
		
		buildFinishLine();
	}

	protected void initNiftyControls() {
		digitalLife = new DigitalDisplay(nifty, screen, "life", 100);
	}

	private void resetCars() {
		for (Car bot : bots) {
			bot.getNode().setLocalTranslation(
					MathTools.randBetween(-2000, 2000), 27,
					MathTools.randBetween(-2000, 2000));
			bot.setLife(100.d);
			bot.removeExplosion();
		}
		player.setLife(100.d);
		player.removeExplosion();
	}

	protected void addBot(Vector3f location, CarProperties carProperties) {
		Car bot = new Car(assetManager, carProperties,
				carProperties.getRandomModel());
		bot.setPhysicsLocation(location);
		bot.getNode().setShadowMode(ShadowMode.CastAndReceive);

		rootNode.attachChild(bot.getNode());
		getPhysicsSpace().add(bot);

		bot.setLife(100);
		bots.add(bot);
	}

	protected void addObjects() {
		BoxCollisionShape treeShape = new BoxCollisionShape(new Vector3f(0.5f,
				10.f, 1.f));
		BoxCollisionShape plotShape = new BoxCollisionShape(new Vector3f(0.5f,
				0.6f, 1.f));

		Spatial node2 = assetManager.loadModel("Models/Tree/Tree.mesh.j3o");
		node2.setShadowMode(ShadowMode.Cast);
		node2.setName("Tree");
		for (int i = 0; i < 60; i++) {
			Spatial node = node2.clone();
			node.scale(5);
			node.setLocalTranslation(MathTools.randBetween(-1000, 1000), 25,
					MathTools.randBetween(-1000, 1000));

			RigidBodyControl controlTree = new RigidBodyControl(treeShape, 0.f);
			controlTree.setUserObject("Tree");

			node.addControl(controlTree);
			getPhysicsSpace().add(node);

			rootNode.attachChild(node);

		}

		Spatial node = assetManager
				.loadModel("Models/cone_altglass/cone_altglass.j3o");
		node.scale(0.2f);
		node.setLocalTranslation(0, 27, 200);
		for (int i = 0; i < 200; i++) {
			Spatial plot = node.clone();
			plot.setLocalTranslation(MathTools.randBetween(-1000, 1000), 27,
					MathTools.randBetween(-1000, 1000));

			RigidBodyControl controlPlot = new RigidBodyControl(plotShape);
			controlPlot.setMass(0.75f);
			controlPlot.setFriction(10);

			plot.addControl(controlPlot);
			getPhysicsSpace().add(plot);
			rootNode.attachChild(plot);
		}
	}

	@Override
	public void update(float tpf) {
		/** any main loop action happens here */
		if (needReset) {
			reset();
			return;
		}
		super.update(tpf);

		int nbBotsAlive = 0;
		String sTimer;
		String conclusion;
		
		if (runIsOn) {
			digitalLife.setText(((Integer) player.getLife()).toString());
			for (Car bot : bots) {
				if (bot.isAlive() && player.isAlive()) {
					++nbBotsAlive;

					bot.getEnginePhysics().setSpeed(
							Math.abs(Conversion.kmToMiles(bot
									.getCurrentVehicleSpeedKmHour())));
					bot.getIA().act();
					bot.getIA().target(player, 200, 0);
					bot.accelerate(-(float) bot.getEnginePhysics().getForce() / 5);
				} else {
					bot.stop(3000);
				}
			}

			if (nbBotsAlive == 0 && player.isAlive()) {
				win = true;
				runIsOn = false;
				runFinish = true;
			} else if (!player.isAlive()) {
				win = false;
				runIsOn = false;
				runFinish = true;
			}
			timePlayer = (System.currentTimeMillis() - startTime);

			sTimer = String.format("%d : %d",
					TimeUnit.MILLISECONDS.toSeconds(timePlayer),
					(timePlayer % 1000) / 10);
			
			screen.findElementByName("timer").getRenderer(TextRenderer.class).setText(sTimer);
		} else {
			long secondes = TimeUnit.MILLISECONDS.toSeconds(timePlayer);
			long millisec = (timePlayer % 1000) / 10;
			if (runFinish) {
				if (win) {
					if (givePt == false) {
						argent = 500;
						givePt = true;
					}
					conclusion ="Gagneg!\nVous avez gagne 500 Eur";
				} else {
					int nbbot = 0;
					int nbbotlive = 0;
					
					for (Car bot : bots) {
						++nbbot;
						if (bot.isAlive()) ++nbbotlive;
					}
					if (givePt == false) {
						nbBotDead = nbbot - nbbotlive;
						argent = (int) ((nbBotDead * 4000) / secondes);
						givePt = true;
					}
					conclusion = "Perdu !\n" + "Vous avez gagne " + argent + " Eur";
					
				}
				String text = String.format("%d : %d", secondes, millisec);
				
				if (ProfilCurrent.getInstance() != null) {
					ProfilCurrent.getInstance().setTimefree(text);
					ProfilCurrent.getInstance().setMonnaie(ProfilCurrent.getInstance().getMonnaie() + argent);
					if (!ProfilCurrent.getInstance().getTimefree().equals("")) {
						String tps[] = ProfilCurrent.getInstance().getTimefree().split(" : ");
						//bat le nombre de bot tué
						if (ProfilCurrent.getInstance().getCardead() < nbBotDead) {
							ProfilCurrent.getInstance().setTimefree(text);
							ProfilCurrent.getInstance().setCardead(nbBotDead);
							ProfilCurrent.getInstance().setMonnaie(ProfilCurrent.getInstance().getMonnaie() + 70);
							bonus = true;
						}
						//bat le temps de bot tué
						else if (ProfilCurrent.getInstance().getCardead() == nbBotDead) {
							if (Long.parseLong(tps[0]) > secondes || 
									(Long.parseLong(tps[0]) == secondes && Long.parseLong(tps[1]) > millisec)) {
								ProfilCurrent.getInstance().setTimefree(text);
								ProfilCurrent.getInstance().setMonnaie(ProfilCurrent.getInstance().getMonnaie() + 70);
								bonus = true;
							}
						}
					} else {
						//Premier score
						ProfilCurrent.getInstance().setTimefree(text);
						ProfilCurrent.getInstance().setCardead(nbBotDead);
						argent = (int) ((nbBotDead * 4000) / secondes);
						ProfilCurrent.getInstance().setMonnaie(ProfilCurrent.getInstance().getMonnaie() + argent);
					}
					Comptes.modifier(ProfilCurrent.getInstance());
					Comptes.Enregistrer();
				} // Fin Enregistrement Profil
				
				if (bonus)
					conclusion = conclusion + "\nBonus de 70 Eur!";
				digitalStart.setText(conclusion);
			}

		}
	}

	protected void reset() {
		super.reset();
		resetCars();
		runIsOn = false;
		givePt = false;
		bonus = false;
		nbBotDead = 0;
		for (Car bot : bots) {
			bot.stop(0);
		}
		player.stop(0);
		screen.findElementByName("timer").getRenderer(TextRenderer.class).setText("0 : 0");
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
	
	@Override
	public void collision(PhysicsCollisionEvent event) {
		super.collision(event);
		if (finishCell.getOverlappingObjects().contains(player)
				&& !runFinish) {
			timePlayer = (System.currentTimeMillis() - startTime);
			System.out.println(String.format("player : %d : %d",
					TimeUnit.MILLISECONDS.toSeconds(timePlayer),
					(timePlayer % 1000) / 10));

			//playerFinish = true;
		}
	}

}
