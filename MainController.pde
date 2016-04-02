import java.util.Queue;
import java.util.LinkedList;

final int ACTIVATOR_NUM = 3;
final int ROPE_LEN_0 = 1000; // in cm
final int ROPE_LEN_1 = 1000;
final int ROPE_LEN_2 = 1000;

final float ACTORSPEED_MAX = 10; // in cm/frame

class MainController {
  Activator[] actors;
  Platform platform;
  Queue queue; 		// the operation queue
  Operation curOp = null; 	// current operation

  // create the controller with the location of the three activators
  MainController(
  	float x0, float y0,
  	float x1, float y1,
  	float x2, float y2
  	) {
  	// create the activators
  	this.actors = new Activator[ACTIVATOR_NUM];
  	actors[0] = new Activator(x0, y0, ROPE_LEN_0, 0);
  	actors[1] = new Activator(x1, y1, ROPE_LEN_1, 282);
  	actors[2] = new Activator(x2, y2, ROPE_LEN_2, 282);

  	platform = new Platform(0, 0, 0);

  	queue = new LinkedList();
  }

  void update() {
  	if (curOp != null) {
  		executeCurrentOperation();
  	} else {
  		if (this.queue.isEmpty()) {
  			// no operation to be executed
  			return;
  		} else {
  			this.curOp = (Operation)this.queue.poll();
  			onOperationStart();
  			executeCurrentOperation();
  		}
  	}
  }

  private void executeCurrentOperation() {
  	switch(curOp.opType) {
  		case "move":
  			move();
  			break;
  		default :
  			break;
  	}
    debug();
  }

  private void onOperationStart() {
  	// set the actor target state here
  	float targetX = curOp.targetX;
  	float targetY = curOp.targetY;
  	float targetZ = curOp.targetZ;
	
	  for (Activator actor : actors) {
		  actor.toTarget(targetX, targetY,targetZ);
		  actor.start();
	  }  	
  }

  private void move() {
  	boolean flag = true;
  	for (Activator actor : actors) {
		  flag = flag && actor.update();
  	}
  	if (flag) {
  		// All operator has reached the target Position
  		curOp = null;
      println("========================");
  	}
  }

  void debug() {
    for (Activator actor : actors) {
      print(actor.a);
      print(",");
    }
    print("\n");
  }

  void addOperation(Operation op) {
    queue.offer(op);
  }
}