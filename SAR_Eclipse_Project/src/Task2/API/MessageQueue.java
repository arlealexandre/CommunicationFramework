package Task2.API;

import Task1.API.Channel;

public abstract class MessageQueue {
	
	protected Channel channel;
	
	public MessageQueue(Channel channel) {
		this.channel = channel;
	}
	
	public abstract void send(byte[] bytes, int offset, int length);
	public abstract byte[] receive();
	public abstract void close();
	public abstract boolean closed();
}
