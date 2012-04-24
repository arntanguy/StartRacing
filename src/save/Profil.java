package save;

import java.util.ArrayList;

import physics.CarProperties;

public class Profil {
	
	private int id;
	private String login;
	private ArrayList<CarProperties> car;
	private String timedemi;
	private String timequart;
	
	public Profil (int id, String login, ArrayList<CarProperties> car, String timedemi, String timequart) {
		this.id = id;
		this.login = login;
		this.car = car;
		this.timedemi = timedemi;
		this.timequart = timequart;
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

	public String getScore() {
		return timedemi;
	}

	public String getTime() {
		return timequart;
	}
	
}
