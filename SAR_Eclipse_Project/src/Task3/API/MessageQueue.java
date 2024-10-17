package Task3.API;

import Task1.API.Channel;

public abstract class MessageQueue {
	
	protected Channel channel;
	
	public MessageQueue(Channel channel) {
		this.channel = channel;
	}

	public interface Listener {
		void received(byte[] msg);
		void sent();
		void closed();
	}
	
	public abstract void setListener(Listener l);
	
	public abstract boolean send(Message message);
	
	public abstract void close();
	public abstract boolean closed();
}
