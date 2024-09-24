package Task1.Implementation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import Task1.API.Broker;

public class BrokerManager {
	
	private static BrokerManager instance = null;
	private Map<String, Broker> brokers;
	
	private BrokerManager() {
		this.brokers = new ConcurrentHashMap<>();
	}
	
	public static synchronized BrokerManager getInstance() {
		if (instance==null) {
			instance = new BrokerManager();
		}
		return instance;
	}
	
	public void addBroker(String name, Broker broker) {
		this.brokers.put(name, broker);
	}
	
	public Broker getBroker(String name) {
		return this.brokers.get(name);
	}
}
