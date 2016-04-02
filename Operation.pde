class Operation {
	final String opType;

	final float targetX;
	final float targetY;
	final float targetZ;

	final long framePos;
	final long last;

	boolean done = false;

	Operation(
		String opType,
		float targetX,
		float targetY,
		float targetZ,
		long framePos,
		long last
		) {
		this.opType = opType;
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetZ = targetZ;
		this.framePos = framePos;
		this.last = last;
	}
}