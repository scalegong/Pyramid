MainController controller;

void setup() {
	size(400, 400);
	controller = new MainController(
		0, 0,
		200, 200,
		-200, 200
		);
	Operation op1 = new Operation(
		"move", 100.0, 100.0, 100.0, 0, 0
		);
	controller.addOperation(op1);
	Operation op2 = new Operation(
		"move", -100.0, 100.0, 100.0, 0, 0
		);
	controller.addOperation(op2);
}

void draw() {
	controller.update();
	
}