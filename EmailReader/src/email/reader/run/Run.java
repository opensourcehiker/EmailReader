package email.reader.run;

import java.util.Timer;

public abstract class Run implements Runnable{
	private Task task = null;

	public abstract void run();
	
	public void runRepeated(int delay, int repeat) {
		task = new Task(this);
		Timer t = new Timer();
		t.scheduleAtFixedRate(task, delay, repeat);
	}
	
	public void runDelayed(int delay) {
		task = new Task(this);
		Timer t = new Timer();
		t.schedule(task, delay);
	}
	
	public void cancel() {
		task.cancel();
	}

}
