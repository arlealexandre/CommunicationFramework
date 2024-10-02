package Task3.API;

public abstract class QueueBroker {
	
	public QueueBroker(String name) {
	}
	
	interface AcceptListener {
		void accepted(MessageQueue queue);
	}
	
	public abstract boolean bind(int port, AcceptListener listener);
	public abstract boolean unbind(int port);
	
	interface ConnectListener {
		void connected(MessageQueue queue);
		void refused();
	}
	
	public abstract boolean connect(String name, int port, ConnectListener listener);
}
