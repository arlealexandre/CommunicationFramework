package Task1.Implementation;

import java.util.HashMap;
import java.util.Map;

import Task1.API.Broker;

public class BrokerManager {
	
	private static BrokerManager instance = null;
	private Map<String, Broker> brokers;
	
	private BrokerManager() {
		this.brokers = new HashMap<>();
	}
	
	public static synchronized BrokerManager getInstance() {
		if (instance==null) {
			instance = new BrokerManager();
		}
		return instance;
	}
	
	public synchronized void addBroker(Broker broker) {
		String name = broker.getName();
		if (this.brokers.containsKey(name)) {
			throw new IllegalStateException("Broker "+name+" already exists.");
		}
		this.brokers.put(name, broker);
	}
	
	public synchronized Broker getBroker(String name) {
		return this.brokers.get(name);
	}
}
