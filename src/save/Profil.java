package save;

import physics.CarProperties;

public class Profil {
	
	private int id;
	private String login;
	private CarProperties car;
	private int score;
	private String time;
	
	public Profil (int id, String login, CarProperties car, int score, String time) {
		this.id = id;
		this.login = login;
		this.car = car;
		this.score = score;
		this.time = time;
	}
		
	public int getId() {
		return id;
	}
	
	public String getLogin() {
		return login;
	}

	public CarProperties getCar() {
		return car;
	}

	public int getScore() {
		return score;
	}

	public String getTime() {
		return time;
	}
	
}
