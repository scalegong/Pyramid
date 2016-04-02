import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Queue; 
import java.util.LinkedList; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Pyramid extends PApplet {

MainController controller;

public void setup() {
	controller = new MainController(
		0, 0,
		200, 200,
		-200, 200
		);
	Operation op1 = new Operation(
		"move", 100.0f, 100.0f, 100.0f, 0, 0
		);
	controller.addOperation(op1);
	Operation op2 = new Operation(
		"move", -100.0f, 100.0f, 100.0f, 0, 0
		);
	controller.addOperation(op2);
}

public void draw() {
	controller.update();
}
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

	public void start() {
		this.idle = true;
	}

	public boolean update() {
		acce += (ACTORSPEED_MAX - acce) * 0.1f;
		slowDown = min(abs(targetA - a) / 50, 1);
		if (abs(targetA - a) < 0.1f) {
			this.idle = true;
			return true;
		}
		a += (targetA - a)/abs(targetA - a) * acce * slowDown;
		return false;
	}

	public void toTarget(
		float targetX, 
		float targetY, 
		float targetZ
		) {
		targetA = sqrt(sq(targetX - x) + sq(targetY - y) + sq(targetZ));
	}
}



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

  public void update() {
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

  public void debug() {
    for (Activator actor : actors) {
      print(actor.a);
      print(",");
    }
    print("\n");
  }

  public void addOperation(Operation op) {
    queue.offer(op);
  }
}
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
class Platform {
	float x, y, z;

	Platform(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void fromActors(Activator[] actors) {
		
	}
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Pyramid" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
