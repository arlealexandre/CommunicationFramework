package Task2.Implementation;

import Task1.API.Broker;
import Task2.API.QueueBroker;
import Task2.API.Task;

public class TaskImpl extends Task {

	public TaskImpl(Broker b, Runnable r) {
		super(b, r);
	}
	
	public TaskImpl(QueueBroker b, Runnable r) {
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
