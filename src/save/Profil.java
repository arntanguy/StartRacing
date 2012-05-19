package save;

import java.util.ArrayList;

import physics.CarProperties;

public class Profil {
	
	private int id;
	private String login;
	private ArrayList<CarProperties> car;
	private String timedemi;
	private String timequart;
	private String timefree;
	private int cardead;
	private int monnaie;
	private int choixCar;
	
	public Profil (int id, String login, ArrayList<CarProperties> car, int choixCar, String timedemi, 
			String timequart, String timefree, int cardead, int monnaie) {
		this.id = id;
		this.login = login;
		this.car = car;
		this.choixCar = choixCar;
		this.timedemi = timedemi;
		this.timequart = timequart;
		this.timefree = timefree;
		this.cardead = cardead;
		this.monnaie = monnaie;
	}
		
	public int getMonnaie() {
		return monnaie;
	}

	public void setMonnaie(int monnaie) {
		this.monnaie = monnaie;
	}

	public int getId() {
		return id;
	}
	
	public String getLogin() {
		return login;
	}

	public ArrayList<CarProperties> getCar() {
		return car;
	}

	public String getTimeQuart() {
		return timequart;
	}

	public String getTimeDemi() {
		return timedemi;
	}	

	public void setTimedemi(String timedemi) {
		this.timedemi = timedemi;
	}

	public void setTimequart(String timequart) {
		this.timequart = timequart;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setCar(ArrayList<CarProperties> car) {
		this.car = car;
	}

	public String getTimefree() {
		return timefree;
	}

	public void setTimefree(String timefree) {
		this.timefree = timefree;
	}

	public int getCardead() {
		return cardead;
	}

	public void setCardead(int cardead) {
		this.cardead = cardead;
	}

	public int getChoixCar() {
		return choixCar;
	}

	public void setChoixCar(int choixCar) {
		this.choixCar = choixCar;
	}
}
