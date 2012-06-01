package game;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.ImageSelect;
import de.lessvoid.nifty.controls.ImageSelectSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextField;

public class TutorielScreen extends AbstractScreenController {
	
	private InputManager inputManager;
	private final String ALLJOUEUR = "allJoueur";
	private final String IMGCAR = "imgcar";
	private TextField texte;

	
	private ImageSelect imgCar;
	
	private final String NIVEAU = "niveau";
	private String[] slide;
	
	public TutorielScreen() {
		super();
		
		slide = new String[4];
		slide[0] = 	"Pour démarrer la voiture et commencer \n" +
					"le compte, appuyez sur l'accélérateur\n" +
					"(touche [Control])";
		slide[1] = 	"Vous contrôlez la boîte de vitesse de la voiture\n" +
					"Pour gagner la course, il faut passer les vitesses\n" +
					"au meilleur moment.\n" +
					"Pour vous aider, il y a une lumière qui change de couleur\n" +
					"Elle devient verte quand c'est bientôt le bon moment\n" +
					"Elle devient bleue quand c'est le meilleur moment\n" +
					"Elle devient rouge quand le moteur est en sur-régime";
		slide[2] = 	"Quand votre moteur est en sur-régime trop longtemps,\n" +
					"la voiture explose.\n" +
					"Il vous faut aller redémarrer la partie en appuyant \n" +
					"sur la touche [Entrée], ou alors retourner au menu \n" +
					"principal en appuyant sur la touche [Echappe]";
		slide[3] = 	"Dans le menu « Gestion de profil », il est possible \n" +
					"d'acheter une nouvelle voiture ou d'améliorer celles \n" +
					"que vous possédez.\n" +
					"Si vous avez équipé votre voiture de nitro, il faut \n" +
					"appuyer sur la touche [Majuscule] pour la déclencher.";
	}

	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		/** init the screen */
		super.initialize(stateManager, a);
		
		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);
	
		this.inputManager = app.getInputManager();
		
		texte = screen.findNiftyControl("texte", TextField.class);
		
		imgCar = screen.findNiftyControl(IMGCAR, ImageSelect.class);
		
		texte.setText(slide[0]);	
		texte.setEnabled(false);
		
		setInputMappings();
	}
	
	private void setInputMappings() {
		inputManager.addMapping("return", new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addListener(new ActionListener() {
			@Override
			public void onAction(String arg0, boolean arg1, float arg2) {
				app.gotoStart();
			}
		}, "return");
	}
	
	@Override
	public void onEndScreen() {
		inputManager.clearMappings();
		super.onEndScreen();
	}

	public void AffDataPlayer(int rangProfil) {
		
		texte.setText(slide[rangProfil]);
		imgCar.setSelectedImageIndex(rangProfil+1);
		imgCar.backClick();
		
	}
	
	@NiftyEventSubscriber(id=ALLJOUEUR)
	public void onResolutionChange(final String id, final DropDownSelectionChangedEvent<String> event) {
//		logchoose = event.getSelection();
//		for (int i = 0; i < dataAllJoueur.size(); ++i) {
//			if (logchoose.equals(dataAllJoueur.get(i).getLogin())) {
//				AffDataPlayer(i);
//				break;
//			}
//		}
	}
	
	@NiftyEventSubscriber(id=IMGCAR)
	public void onChangePhoto(final String id, final ImageSelectSelectionChangedEvent event) {
		changeDataPhoto();
	}
	
	public void changePhoto() {
		int maxphoto = 3;
		if (imgCar.getSelectedImageIndex() == maxphoto) {
			imgCar.setSelectedImageIndex(1);
			imgCar.backClick();
		} else if (imgCar.getSelectedImageIndex() >= 0 && imgCar.getSelectedImageIndex() < maxphoto){
			imgCar.forwardClick();
		}
		
		changeDataPhoto();
	}
    
	public void changeDataPhoto() {
		int slideAffiche = imgCar.getSelectedImageIndex();
		texte.setText(slide[slideAffiche]);
	}
	
	public void gotoMainMenu() {
		app.gotoStart();
	}
}
