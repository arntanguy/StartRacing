package ia;

public class IATools {
	/**
	 * Returns a random between lower and higher included
	 * @param lower
	 * 		lower boundary
	 * @param higher
	 * 		higher boundary
	 * @return
	 * 		a random number between lower and higher
	 */
	public static int randBetween(int lower, int higher) {
		return (int) ((Math.random() * (higher - lower)) + lower);
	}
}
