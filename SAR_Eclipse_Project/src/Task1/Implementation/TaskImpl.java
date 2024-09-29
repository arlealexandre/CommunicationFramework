package Task1.Implementation;

import Task1.API.Broker;
import Task1.API.Task;

public class TaskImpl extends Task {
	
	public TaskImpl(Broker b, Runnable r) {
		super(b, r);
	}

	@Override
	public void run() {
		if (this.runnable != null) {
			runnable.run();
		} else {
			throw new NullPointerException("Runnable is null.");
		}
	}
}
