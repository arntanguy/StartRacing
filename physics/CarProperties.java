package physics;

public class CarProperties {
	// Ring gear and pinion ratio (3.08, 3.55, ...)
	private double rgp = 3;
	// tire height (24.5, 26, 27.5, ...cm)
	private double th = 24.5;
	// transmission final gear (2.54, 1.43)
	private double tgr = 2.5;
	
	private double idleRpm = 1000;

	public double getIdleRpm() {
		return idleRpm;
	}

	public void setIdleRpm(double idleRpm) {
		this.idleRpm = idleRpm;
	}

	public CarProperties() {
	}

	public CarProperties(double rgp, double th, double tgr, double idleRpm) {
		this.rgp = rgp;
		this.th = th;
		this.tgr = tgr;
		this.idleRpm = idleRpm;
	}

	public double getRgp() {
		return rgp;
	}

	public void setRgp(double rgp) {
		this.rgp = rgp;
	}

	public double getTh() {
		return th;
	}

	public void setTh(double th) {
		this.th = th;
	}

	public double getTgr() {
		return tgr;
	}

	public void setTgr(double tgr) {
		this.tgr = tgr;
	}

}
