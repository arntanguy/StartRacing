package game;

import java.util.ArrayList;

import physics.CarProperties;
import physics.TypeCarProperties;
import save.Comptes;
import save.ProfilCurrent;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ImageSelect;
import de.lessvoid.nifty.controls.ImageSelectSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextField;

public class Achat extends AbstractScreenController {
	
	private InputManager inputManager;
	
	private final String IMGCAR = "imagecar";
	
	private ArrayList<CarProperties> dataAllCar;
	private ArrayList<CarProperties> allCarJoueur;
	
	private TextField monnaie;
	private ArrayList<Integer> tabprix;
	private int calcul;
	
	private TextField typeCar;
	private TextField weight;
	private TextField puis;
	private TextField nitro;
	private TextField prix;
	private TextField msgerr;
	private String hasCar = "VOUS AVEZ DEJA CETTE VOITURE !";
	
	private ImageSelect imgCar;
	
	private final int CORVETTE = 0;
	private final int BMWM3 = 1;
	private final int FERRARI = 2;
	
	public Achat() {
		super();
		dataAllCar = Comptes.getListCar();
		tabprix = new ArrayList<Integer>();
		tabprix.add(44000);
		tabprix.add(68000);
		tabprix.add(86000);
		calcul = ProfilCurrent.getInstance().getMonnaie();
		allCarJoueur = ProfilCurrent.getInstance().getCar();
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		super.initialize(stateManager, a);

		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);

		this.inputManager = app.getInputManager();
		
		imgCar = screen.findNiftyControl(IMGCAR, ImageSelect.class);
		typeCar = screen.findNiftyControl("typeCar", TextField.class);
		weight = screen.findNiftyControl("weight", TextField.class);
		puis  = screen.findNiftyControl("puis", TextField.class);
		nitro  = screen.findNiftyControl("nitro", TextField.class);
		
		prix = screen.findNiftyControl("prix", TextField.class);
		
		msgerr = screen.findNiftyControl("msgerr", TextField.class);
		
		typeCar.setText(TypeCarProperties.CORVETTE.toString());
		weight.setText(Integer.toString(dataAllCar.get(CORVETTE+1).getWeight()));
		puis.setText(Integer.toString(dataAllCar.get(CORVETTE+1).getPuissance()));
		prix.setText(Integer.toString(tabprix.get(CORVETTE)));
		nitro.setText((dataAllCar.get(CORVETTE+1).isImprovenitro()) ? "Oui" : "Non");
		for (int j = 0; j < allCarJoueur.size(); ++j) {
			if (allCarJoueur.get(j).getTypeCar().equals(TypeCarProperties.CORVETTE)) {
				msgerr.setText(hasCar);
			}
		}
		
		typeCar.setEnabled(false);
		prix.setEnabled(false);		
		weight.setEnabled(false);
		puis.setEnabled(false);
		nitro.setEnabled(false);
		msgerr.setEnabled(false);
		
		calcul = ProfilCurrent.getInstance().getMonnaie();
		monnaie = screen.findNiftyControl("monnaie", TextField.class);
		monnaie.setText(Integer.toString(calcul));
		monnaie.setEnabled(false);
		
	}
	
	@NiftyEventSubscriber(id=IMGCAR)
	public void onChangePhoto(final String id, final ImageSelectSelectionChangedEvent event) {
		changeDataPhoto();
	}
	
	public void changePhoto() {
		int maxphoto = 2;
		if (imgCar.getSelectedImageIndex() == maxphoto) {
			imgCar.setSelectedImageIndex(1);
			imgCar.backClick();
		} else if (imgCar.getSelectedImageIndex() >= 0 && imgCar.getSelectedImageIndex() < maxphoto){
			imgCar.forwardClick();
		}
		
		changeDataPhoto();
	}
	
	public void forAllCar(TypeCarProperties typeCarChoose, int rangprix) {
		for (int i = 0; i < dataAllCar.size(); ++i) {
			if (dataAllCar.get(i).getTypeCar().equals(typeCarChoose)) {
				typeCar.setText(dataAllCar.get(i).getTypeCar().toString());
				puis.setText(Integer.toString(dataAllCar.get(i).getPuissance()));
				weight.setText(Integer.toString(dataAllCar.get(i).getWeight()));
				nitro.setText((dataAllCar.get(i).isImprovenitro()) ? "Oui" : "Non");
				prix.setText(Integer.toString(tabprix.get(rangprix)));
				for (int j = 0; j < allCarJoueur.size(); ++j) {
					if (allCarJoueur.get(j).getTypeCar().equals(typeCarChoose)) {
						msgerr.setText(hasCar);
					}
				}
				break;
			}						
		}
	}
	
	public void changeDataPhoto() {
		
		typeCar.setText("");
		weight.setText("");
		puis.setText("");
		nitro.setText("");
		msgerr.setText("");
		
		if (imgCar.getSelectedImageIndex() == CORVETTE) {
			forAllCar(TypeCarProperties.CORVETTE, CORVETTE);
		} else if (imgCar.getSelectedImageIndex() == BMWM3) {
			forAllCar(TypeCarProperties.BMWM3, BMWM3);
		} else {
			forAllCar(TypeCarProperties.FERRARI, FERRARI);
		}
	}
	
	public void gotoChooseProfil() {
		app.gotoAffProfil();
	}
	
	public void saveCarBuy(TypeCarProperties typebuy) {
		for (int i = 0; i < dataAllCar.size(); ++i) {
			if (dataAllCar.get(i).getTypeCar().equals(typebuy)) {
				ProfilCurrent.getInstance().setChoixCar(allCarJoueur.size());
				allCarJoueur.add(dataAllCar.get(i));
				break;
			}
		}
	}
	
	public void Enregistrer() {
		String indic = "IMPOSSIBLE, VOUS AVEZ DEJA LA VOITURE !";

		if (imgCar.getSelectedImageIndex() == BMWM3) {
			calcul -= tabprix.get(BMWM3);
		} else if (imgCar.getSelectedImageIndex() == CORVETTE) {
			calcul -= tabprix.get(CORVETTE);
		} else if (imgCar.getSelectedImageIndex() == FERRARI) {
			calcul -= tabprix.get(FERRARI);
		}
		
		if (msgerr.getText().equals(hasCar)) {
			msgerr.setText(indic);
		} else if (calcul < 0) {
			calcul = ProfilCurrent.getInstance().getMonnaie();
			msgerr.setText("IMPOSSIBLE, VOUS N'AVEZ PAS ASSEZ D'ARGENT !");
		} else {
			if (imgCar.getSelectedImageIndex() == BMWM3) {
				saveCarBuy(TypeCarProperties.BMWM3);
			} else if (imgCar.getSelectedImageIndex() == CORVETTE) {
				saveCarBuy(TypeCarProperties.CORVETTE);
			} else if (imgCar.getSelectedImageIndex() == FERRARI) {
				saveCarBuy(TypeCarProperties.FERRARI);
			}
			ProfilCurrent.getInstance().setCar(allCarJoueur);
			ProfilCurrent.getInstance().setMonnaie(calcul);
			Comptes.modifier(ProfilCurrent.getInstance());
			app.gotoAffProfil();
		}
	}
}
