package game;

import java.util.ArrayList;

import physics.BMWM3Properties;
import physics.CarProperties;
import physics.DodgeViperProperties;
import physics.SkylineProperties;
import physics.TypeCarProperties;
import save.Comptes;
import save.ProfilCurrent;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.ImageSelect;
import de.lessvoid.nifty.controls.TextField;

public class Achat extends AbstractScreenController {
	
	private InputManager inputManager;
	
	private ArrayList<CarProperties> dataAllCar;
	private ArrayList<CarProperties> allCarJoueur;
	
	private TextField monnaie;
	private ArrayList<Integer> tabprix;
	private int calcul;
	
	private TextField typeCar;
	private TextField weight;
	private TextField puis;
	private TextField prix;
	private TextField msgerr;
	private String hasCar = "VOUS AVEZ DEJA CETTE VOITURE !";
	
	private ImageSelect imgCar;
	
	private final int DODGE = 0;
	private final int BMWM3 = 1;
	private final int FERRARI = 2;
	
	public Achat() {
		super();
		dataAllCar = Comptes.getListCar();
		tabprix = new ArrayList<Integer>();
		tabprix.add(22000);
		tabprix.add(34000);
		tabprix.add(48000);
		calcul = ProfilCurrent.getInstance().getMonnaie();
		allCarJoueur = ProfilCurrent.getInstance().getCar();
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application a) {
		super.initialize(stateManager, a);

		inputManager = app.getInputManager();
		inputManager.setCursorVisible(true);

		this.inputManager = app.getInputManager();
		
		imgCar = screen.findNiftyControl("imgcar", ImageSelect.class);
		typeCar = screen.findNiftyControl("typeCar", TextField.class);
		weight = screen.findNiftyControl("weight", TextField.class);
		puis  = screen.findNiftyControl("puis", TextField.class);
		
		prix = screen.findNiftyControl("prix", TextField.class);
		
		msgerr = screen.findNiftyControl("msgerr", TextField.class);
		
		typeCar.setText(TypeCarProperties.DODGEVIPER.toString());
		weight.setText(Integer.toString(dataAllCar.get(DODGE+1).getWeight()));
		puis.setText(Integer.toString(dataAllCar.get(DODGE+1).getPuissance()));
		prix.setText(Integer.toString(tabprix.get(DODGE)));
		for (int j = 0; j < allCarJoueur.size(); ++j) {
			if (allCarJoueur.get(j).getTypeCar().equals(TypeCarProperties.DODGEVIPER)) {
				msgerr.setText(hasCar);
			}
		}
		
		typeCar.setEnabled(false);
		prix.setEnabled(false);		
		weight.setEnabled(false);
		puis.setEnabled(false);
		msgerr.setEnabled(false);
		
		calcul = ProfilCurrent.getInstance().getMonnaie();
		monnaie = screen.findNiftyControl("monnaie", TextField.class);
		monnaie.setText(Integer.toString(calcul));
		monnaie.setEnabled(false);
		
	}
	
	/*public void blockcheckbmw () {
		if (hasBmw == false) {
			if (calcul < prixbmw) {
				checkboxbmw.setEnabled(false);
			} else {
				checkboxbmw.setEnabled(true);
			}
		}
	}
	
	public void blockcheckdv() {
		if (hasDv == false) {
			if (calcul < prixdv) {
				checkboxdv.setEnabled(false);
			} else {
				checkboxdv.setEnabled(true);
			}
		}
	}
	
	public void blockchecksk() {
		if (hasSk == false) {
			if (calcul < prixsk) {
				checkboxsl.setEnabled(false);
			} else {
				checkboxsl.setEnabled(true);
			}
		}
	}
	
	@NiftyEventSubscriber(id="Bmw")
	public void checkBmw (final String id, final CheckBoxStateChangedEvent event) {
		if (event.isChecked()) {
			calcul -= prixbmw;
		} else {
			calcul += prixbmw;
		}
		monnaie.setText(Integer.toString(calcul));
		blockcheckdv();
		blockchecksk();
	}
	
	@NiftyEventSubscriber(id="Dodge")
	public void checkDodge (final String id, final CheckBoxStateChangedEvent event) {
		if (event.isChecked()) {
			calcul -= prixdv;
		} else {
			calcul += prixdv;
		}
		monnaie.setText(Integer.toString(calcul));
		blockchecksk();
		blockcheckbmw();
	}
	
	@NiftyEventSubscriber(id="Skyline")
	public void checkSkyline (final String id, final CheckBoxStateChangedEvent event) {
		if (event.isChecked()) {
			calcul -= prixsk;
		} else {
			calcul += prixsk;
		}
		monnaie.setText(Integer.toString(calcul));
		blockcheckbmw();
		blockcheckdv();
	}*/
	
	public void changePhoto() {
		int maxphoto = 2;
		if (imgCar.getSelectedImageIndex() == maxphoto) {
			imgCar.setSelectedImageIndex(1);
			imgCar.backClick();
		} else if (imgCar.getSelectedImageIndex() >= 0 && imgCar.getSelectedImageIndex() < maxphoto){
			imgCar.forwardClick();
		}
		
		typeCar.setText("");
		weight.setText("");
		puis.setText("");
		msgerr.setText("");
		
		if (imgCar.getSelectedImageIndex() == DODGE) {
			for (int i = 0; i < dataAllCar.size(); ++i) {
				if (dataAllCar.get(i).getTypeCar().equals(TypeCarProperties.DODGEVIPER)) {
					typeCar.setText(dataAllCar.get(i).getTypeCar().toString());
					puis.setText(Integer.toString(dataAllCar.get(i).getPuissance()));
					weight.setText(Integer.toString(dataAllCar.get(i).getWeight()));
					prix.setText(Integer.toString(tabprix.get(DODGE)));
					for (int j = 0; j < allCarJoueur.size(); ++j) {
						if (allCarJoueur.get(j).getTypeCar().equals(TypeCarProperties.DODGEVIPER)) {
							msgerr.setText(hasCar);
						}
					}
					break;
				}						
			}
		} else if (imgCar.getSelectedImageIndex() == BMWM3) {
			for (int i = 0; i < dataAllCar.size(); ++i) {
				if (dataAllCar.get(i).getTypeCar().equals(TypeCarProperties.BMWM3)) {
					typeCar.setText(dataAllCar.get(i).getTypeCar().toString());
					puis.setText(Integer.toString(dataAllCar.get(i).getPuissance()));
					weight.setText(Integer.toString(dataAllCar.get(i).getWeight()));
					prix.setText(Integer.toString(tabprix.get(BMWM3)));
					for (int j = 0; j < allCarJoueur.size(); ++j) {
						if (allCarJoueur.get(j).getTypeCar().equals(TypeCarProperties.BMWM3)) {
							msgerr.setText(hasCar);
						}
					}
					break;
				}						
			}
		} else {
			for (int i = 0; i < dataAllCar.size(); ++i) {
				if (dataAllCar.get(i).getTypeCar().equals(TypeCarProperties.FERRARI)) {
					typeCar.setText(dataAllCar.get(i).getTypeCar().toString());
					puis.setText(Integer.toString(dataAllCar.get(i).getPuissance()));
					weight.setText(Integer.toString(dataAllCar.get(i).getWeight()));
					prix.setText(Integer.toString(tabprix.get(FERRARI)));
					for (int j = 0; j < allCarJoueur.size(); ++j) {
						if (allCarJoueur.get(j).getTypeCar().equals(TypeCarProperties.FERRARI)) {
							msgerr.setText(hasCar);
						}
					}
					break;
				}						
			} //for
		}
	}
	
	public void gotoChooseProfil() {
		app.gotoAffProfil();
	}
	
	public void Enregistrer() {
		String indic = "IMPOSSIBLE, VOUS AVEZ DEJA LA VOITURE !";
		if (msgerr.getText().equals(hasCar) || msgerr.getText().equals(indic)) {
			msgerr.setText(indic);
		} else {
			if (imgCar.getSelectedImageIndex() == BMWM3) {
				for (int i = 0; i < dataAllCar.size(); ++i) {
					if (dataAllCar.get(i).getTypeCar().equals(TypeCarProperties.BMWM3)) {
						ProfilCurrent.getInstance().setChoixCar(allCarJoueur.size());
						allCarJoueur.add(dataAllCar.get(i));
						calcul -= tabprix.get(1);
						break;
					}
				}
			}
			else if (imgCar.getSelectedImageIndex() == DODGE) {
				for (int i = 0; i < dataAllCar.size(); ++i) {
					if (dataAllCar.get(i).getTypeCar().equals(TypeCarProperties.DODGEVIPER)) {
						ProfilCurrent.getInstance().setChoixCar(allCarJoueur.size());
						allCarJoueur.add(dataAllCar.get(i));
						calcul -= tabprix.get(DODGE);
						break;
					}
				}
			}
			else if (imgCar.getSelectedImageIndex() == FERRARI) {
				for (int i = 0; i < dataAllCar.size(); ++i) {
					if (dataAllCar.get(i).getTypeCar().equals(TypeCarProperties.FERRARI)) {
						ProfilCurrent.getInstance().setChoixCar(allCarJoueur.size());
						allCarJoueur.add(dataAllCar.get(i));
						calcul -= tabprix.get(FERRARI);
						break;
					}
				}
			}
			
			if (calcul < 0) {
				calcul = ProfilCurrent.getInstance().getMonnaie();
				msgerr.setText("IMPOSSIBLE, VOUS N'AVEZ PAS ASSEZ D'ARGENT !");
			} else {
				ProfilCurrent.getInstance().setCar(allCarJoueur);
				ProfilCurrent.getInstance().setMonnaie(calcul);
				Comptes.modifier(ProfilCurrent.getInstance());
				app.gotoAffProfil();
			}
		
		}
	}
}
