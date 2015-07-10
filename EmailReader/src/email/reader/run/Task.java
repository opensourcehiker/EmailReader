package email.reader.run;

import java.util.TimerTask;

public class Task extends TimerTask{
	private Run run = null;
	
	public Task(Run run) {
		this.run = run;
	}

	public void run() {
		this.run.run();
	}

}
