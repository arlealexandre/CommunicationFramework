package Task3.Implementation;

import java.util.HashMap;
import java.util.Map;

import Task3.API.QueueBroker;

public class QueueBrokerManager {

	private static QueueBrokerManager instance = null;
	private Map<String, QueueBroker> queueBrokers;
	
	private QueueBrokerManager() {
		this.queueBrokers = new HashMap<>();
	}
	
	public static synchronized QueueBrokerManager getInstance() {
		if (instance==null) {
			instance = new QueueBrokerManager();
		}
		return instance;
	}
	
	public synchronized void addQueueBroker(QueueBroker queueBroker) {
		String name = queueBroker.getName();
		if (this.queueBrokers.containsKey(name)) {
			throw new IllegalStateException("Broker "+name+" already exists.");
		}
		this.queueBrokers.put(name, queueBroker);
	}
	
	public synchronized QueueBroker getQueueBroker(String name) {
		return this.queueBrokers.get(name);
	}
}
