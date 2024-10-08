package Task3.Implementation;

import Task3.API.QueueBroker;
import Task3.API.Task;

public class TaskImpl extends Task {

	public TaskImpl(QueueBroker b, Runnable r) {
		super(b, r);
	}

}
