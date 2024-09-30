package Task2.Implementation;

import Task1.API.Broker;
import Task1.API.Channel;
import Task2.API.MessageQueue;
import Task2.API.QueueBroker;

public class QueueBrokerImpl extends QueueBroker {

	public QueueBrokerImpl(Broker broker) {
		super(broker);
	}

	@Override
	public MessageQueue accept(int port) {
		Channel channel = this.broker.accept(port);
		return new MessageQueueImpl(channel);
	}

	@Override
	public MessageQueue connect(String name, int port) {
		Channel channel = this.broker.connect(name, port);
		return new MessageQueueImpl(channel);
	}

}
