package Task3.Implementation;

import Task3.API.EventTask;
import Task3.API.QueueBroker;

public class ConnectingTaskEvent extends EventTask {
	
	private QueueBroker queueBroker;
	private int port;
	private QueueBroker.ConnectListener listener;
	
	public ConnectingTaskEvent(QueueBroker queueBroker, int port, QueueBroker.ConnectListener listener) {
		this.queueBroker = queueBroker;
		this.port = port;
		this.listener = listener;
	}

}
