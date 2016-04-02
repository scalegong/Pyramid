class Activator {
	float x, y;			// locatin of the Activator
	float ropeLen;		// total length of the rope
	float acce;
	float slowDown;

	private boolean idle = false;
	float a;
	float targetA;

	Activator(
		float x,
		float y,
		float ropeLen,
		float a
		) {
		this.x = x;
		this.y = y;
		this.ropeLen = ropeLen;
		this.a = a;
	}

	void start() {
		this.idle = true;
	}

	boolean update() {
		acce += (ACTORSPEED_MAX - acce) * 0.1;
		slowDown = min(abs(targetA - a) / 50, 1);
		if (abs(targetA - a) < 0.1) {
			this.idle = true;
			return true;
		}
		a += (targetA - a)/abs(targetA - a) * acce * slowDown;
		return false;
	}

	void toTarget(
		float targetX, 
		float targetY, 
		float targetZ
		) {
		targetA = sqrt(sq(targetX - x) + sq(targetY - y) + sq(targetZ));
	}
}