package physics.tools;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

public class MathTools {
	/**
	 * Returns a random between lower and higher included
	 * 
	 * @param lower
	 *            lower boundary
	 * @param higher
	 *            higher boundary
	 * @return a random number between lower and higher
	 */
	public static int randBetween(int lower, int higher) {
		return (int) ((Math.random() * (higher - lower)) + lower);
	}

	/**
	 * Returns the oriented angle between 2 vectors 2d. v1 is considered as the
	 * "fixed" direction, and v2 and the vector rotation Thus, a positive
	 * returns value means that v2 is rotating in the trigonometric way
	 * 
	 * @param v1
	 * @param v2
	 * @return The oriented angle between v1 and v2, in radian between [-Pi;Pi]
	 */
	public static float orientedAngle(Vector2f v1, Vector2f v2) {
		v1 = v1.normalize();
		v2 = v2.normalize();
		float ps = v1.dot(v2);
		float det = v1.determinant(v2);
		float angle = FastMath.acos(ps);
		return (det < 0) ? -angle : angle;
	}

	public static Vector2f inFront(Vector2f v1, Vector2f v2,
			Vector2f referenceDirection) {
		Vector2f vect = v2.subtract(v1);
		float angle = Math.abs(orientedAngle(vect, referenceDirection));
		if (angle <= Math.PI / 2)
			return v1;
		else
			return v2;
	}
}
