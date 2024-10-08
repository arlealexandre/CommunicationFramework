package Task3.Implementation;

import Task1.API.Channel;
import Task3.API.MessageQueue;

public class MessageQueueImpl extends MessageQueue {
	
	private Listener listener;

	public MessageQueueImpl(Channel channel) {
		super(channel);
	}

	@Override
	public void setListener(Listener l) {
		this.listener = l;
	}

	@Override
	public boolean send(byte[] bytes) {
		return false;
	}

	@Override
	public boolean send(byte[] bytes, int offset, int length) {
		return false;
	}

	@Override
	public void close() {
		this.channel.disconnect();
	}

	@Override
	public boolean closed() {
		return this.channel.disconnected();
	}

}
