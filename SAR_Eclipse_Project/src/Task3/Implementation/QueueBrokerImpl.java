package Task3.Implementation;

import java.util.HashMap;
import java.util.Map;

import Task3.API.*;

public class QueueBrokerImpl extends QueueBroker {
	
	private Map<Integer, EventTask> eventTasks;
	private AcceptingTaskEvent acceptTask;
	private ConnectingTaskEvent connectTask;

	public QueueBrokerImpl(String name) {
		super(name);
		eventTasks = new HashMap<>();
		QueueBrokerManager.getInstance().addQueueBroker(this);
	}

	@Override
	public boolean bind(int port, AcceptListener listener) {
		if (this.eventTasks.get(port) != null) {
			return false; // task already bind
		}
		acceptTask = new AcceptingTaskEvent(port, listener);
		this.eventTasks.put(port, acceptTask);
		acceptTask.post(new Runnable() {

			@Override
			public void run() {
				if (!acceptTask.isAlreadyAccepted()) {
					EventPump.getInstance().postEvent(EventTask.task());
				} else {
					listener.accepted(acceptTask.getAcceptQueue());
				}			
			}
			
		});
		return true;
	}

	@Override
	public boolean unbind(int port) {
		EventTask eventTask = this.eventTasks.get(port);
		if (!eventTask.killed()) {
			eventTask.kill();
			this.eventTasks.remove(port);
			return true;
		}
		return false;
	}

	@Override
	public boolean connect(String name, int port, ConnectListener listener) {
		QueueBroker queueBroker = QueueBrokerManager.getInstance().getQueueBroker(name);
		
		if (queueBroker == null) {
			return false;
		}
		
		connectTask = new ConnectingTaskEvent(queueBroker, port, listener);
		connectTask.post(new Runnable() {

			@Override
			public void run() {
				AcceptingTaskEvent eventTask = (AcceptingTaskEvent) eventTasks.get(port);
				if (eventTask == null || eventTask.isAlreadyAccepted()) {
					listener.refused();
				} else {
					listener.connected(eventTask.getConnectQueue());
				}
			}
			
		});
		return true;
	}

}
