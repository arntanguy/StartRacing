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

	public String getTime() {
		return timequart;
	}

	public String getTimedemi() {
		return timedemi;
	}

	public void setTimedemi(String timedemi) {
		this.timedemi = timedemi;
	}

	public String getTimequart() {
		return timequart;
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
	
}
