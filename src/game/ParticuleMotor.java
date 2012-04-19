package game;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class ParticuleMotor {

	private AssetManager assetManager;

	private long timerExplosion = 0;
	private ParticleEmitter fire;
	private ParticleEmitter burst;
	private ParticleEmitter embers;
	private ParticleEmitter smoke;
	private ParticleEmitter debris;
	private ParticleEmitter shockwave;
	private Node explosionEffect;
	private int explosionState;
	private boolean burstEnabled;



	public ParticuleMotor(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public boolean getBurstEnabled()	{
		return burstEnabled;
	}
	
	public void addExplosion(Node node)	{
		initBurst();
		burstEnabled = true;
		
		node.attachChild(explosionEffect);
		
		timerExplosion = System.currentTimeMillis();	
	}
	
	public void removeExplosion(Node node)	{
		if (burstEnabled)	{
			node.detachChild(explosionEffect);
			burstEnabled = false;
		}
	}

	public void controlBurst() {
		if (burstEnabled)	{	
			long time = System.currentTimeMillis() - timerExplosion;
			if (time > 100 && explosionState == 0) {
				explosionState++;
			}
			if (time > 500 && explosionState == 1) {
				shockwave.emitAllParticles();
				burst.emitAllParticles();
				debris.emitAllParticles();
				embers.emitAllParticles();
				explosionState++;
			}
			if (time > 700 && explosionState == 2) {
				fire.emitAllParticles();
				smoke.emitAllParticles();
				explosionState++;
			}
			if (time > 1000 && explosionState == 3) {
				burst.killAllParticles();
				debris.killAllParticles();
				explosionState++;
			}
			if (time > 4000 && explosionState == 4) {
				//				fire.killAllParticles();
				//				smoke.killAllParticles();
				embers.killAllParticles();
				shockwave.killAllParticles();
			}
		}
	}

	public void initBurst() {
		explosionEffect = new Node("explosionFX");

		createBurst();
		createDebris();
		createSmoke();
		createFire();
		createEmbers();
		createShockwave();
	}

	private void createFire() {
		fire = new ParticleEmitter("Emitter", Type.Triangle, 200);
		Material fire_mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		fire_mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
		fire.setMaterial(fire_mat);
		fire.setImagesX(2);
		fire.setImagesY(2);
		fire.setRandomAngle(true);
		explosionEffect.attachChild(fire);


		fire.setStartColor(new ColorRGBA(1f, 1f, .5f, 1f));
		//		fire.setStartColor(ColorRGBA.Blue);
		fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 0f));
		//		fire.setEndColor(ColorRGBA.Yellow);
		fire.setGravity(0, 0, 0);
		fire.setStartSize(1.5f);
		fire.setEndSize(0.05f);
		fire.setLowLife(0.5f);
		fire.setHighLife(2f);
		fire.getParticleInfluencer().setVelocityVariation(0.3f);
		fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 3f, 0));
		fire.setParticlesPerSec(100);
	}

	private void createBurst() {
		burst = new ParticleEmitter("Flash", Type.Triangle, 5);
		Material burst_mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		burst_mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flash.png"));
		burst.setMaterial(burst_mat);
		burst.setImagesX(2);
		burst.setImagesY(2);
		burst.setSelectRandomImage(true);
		explosionEffect.attachChild(burst);


		burst.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, 1f));
		burst.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
		burst.setStartSize(.1f);
		burst.setEndSize(6.0f);
		burst.setGravity(0, 0, 0);
		burst.setLowLife(.5f);
		burst.setHighLife(.5f);
		burst.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 5f, 0));
		burst.getParticleInfluencer().setVelocityVariation(1);
		burst.setShape(new EmitterSphereShape(Vector3f.ZERO, .5f));
		burst.setParticlesPerSec(0);


	}

	private void createEmbers() {
		embers = new ParticleEmitter("embers", Type.Triangle, 50);
		Material embers_mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		embers_mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/roundspark.png"));
		embers.setMaterial(embers_mat);
		embers.setImagesX(1);
		embers.setImagesY(1);
		explosionEffect.attachChild(embers);


		embers.setStartColor(new ColorRGBA(1f, 0.29f, 0.34f, 1.0f));
		embers.setEndColor(new ColorRGBA(0, 0, 0, 0.5f));
		embers.setStartSize(1.2f);
		embers.setEndSize(1.8f);
		embers.setGravity(0, -.5f, 0);
		embers.setLowLife(1.8f);
		embers.setHighLife(5f);
		embers.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 3, 0));
		embers.getParticleInfluencer().setVelocityVariation(.5f);
		embers.setShape(new EmitterSphereShape(Vector3f.ZERO, 2f));
		embers.setParticlesPerSec(0);


	}

	private void createSmoke() {
		smoke = new ParticleEmitter("Smoke emitter", Type.Triangle, 70);
		Material smoke_mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		smoke_mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/smoketrail.png"));
		smoke.setMaterial(smoke_mat);
		smoke.setImagesX(1);
		smoke.setImagesY(3);
		smoke.setSelectRandomImage(true);
		explosionEffect.attachChild(smoke);


		smoke.setStartColor(ColorRGBA.DarkGray);
		smoke.setEndColor(ColorRGBA.Black);
		smoke.setLowLife(4f);
		smoke.setHighLife(6f);
		smoke.setGravity(0,1,0);
		smoke.setFacingVelocity(true);
		smoke.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 5f, 0));
		smoke.getParticleInfluencer().setVelocityVariation(0.15f);
		smoke.setStartSize(.5f);
		smoke.setEndSize(2f);
		smoke.setParticlesPerSec(60);
	}

	private void createDebris() {
		debris = new ParticleEmitter("Debris", Type.Triangle, 15);
		Material debris_mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		debris_mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/Debris.png"));
		debris.setMaterial(debris_mat);
		debris.setImagesX(3);
		debris.setImagesY(3);
		debris.setSelectRandomImage(false);
		explosionEffect.attachChild(debris);


		debris.setRandomAngle(true);
		debris.setRotateSpeed(FastMath.TWO_PI * 2);
		debris.setStartColor(new ColorRGBA(0.4f, 0.4f, 0.4f, 1.0f));
		debris.setEndColor(new ColorRGBA(0.4f, 0.4f, 0.4f, 1.0f));
		debris.setStartSize(.2f);
		debris.setEndSize(1f);
		debris.setGravity(0,10f,0);
		debris.setLowLife(1f);
		debris.setHighLife(1.1f);
		debris.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 15, 0));
		debris.getParticleInfluencer().setVelocityVariation(.60f);
		debris.setParticlesPerSec(0);


	}

	private void createShockwave() {
		shockwave = new ParticleEmitter("Shockwave", Type.Triangle, 2);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/shockwave.png"));
		shockwave.setImagesX(1);
		shockwave.setImagesY(1);
		shockwave.setMaterial(mat);
		explosionEffect.attachChild(shockwave);

		/* The shockwave faces upward (along the Y axis) to make it appear as
		 * a horizontally expanding circle. */
		shockwave.setFaceNormal(Vector3f.UNIT_Y);
		shockwave.setStartColor(new ColorRGBA(.68f, 0.77f, 0.61f, 1f));
		shockwave.setEndColor(new ColorRGBA(.68f, 0.77f, 0.61f, 0f));
		shockwave.setStartSize(1f);
		shockwave.setEndSize(7f);
		shockwave.setGravity(0, 0, 0);
		shockwave.setLowLife(1f);
		shockwave.setHighLife(1f);
		shockwave.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, 0));
		shockwave.getParticleInfluencer().setVelocityVariation(0f);
		shockwave.setParticlesPerSec(0);
	}

}