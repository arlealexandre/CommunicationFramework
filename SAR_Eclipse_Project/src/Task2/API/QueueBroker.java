package Task2.API;

import Task1.API.Broker;

public abstract class QueueBroker {
	
	protected Broker broker;

	public QueueBroker(Broker broker) {
		this.broker = broker;
	}
	
	public String getName() {
		return this.broker.getName();
	}
	
	public abstract MessageQueue accept(int port);
	
	public abstract MessageQueue connect(String name, int port);
}
