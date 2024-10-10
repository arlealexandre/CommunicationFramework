package Task3.Implementation;

import Task1.API.Channel;
import Task1.API.DisconnectedException;
import Task3.API.Message;
import Task3.API.MessageQueue;
import Task3.API.Task;


public class MessageQueueImpl extends MessageQueue {

	private Listener listener;

	public MessageQueueImpl(Channel channel) {
		super(channel);
	}

	@Override
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	@Override
	public synchronized boolean send(Message message) {
		if (this.listener == null || channel.disconnected()) {
			return false;
		}
		
		Task task = new Task();
		task.post(new Runnable() {

			@Override
			public void run() {
				try {
					
					// sending the length of the message
					int length = message.getLength();
					int offset = message.getOffset();
					byte[] bytes = message.getBytes();
					int bytesWrite = 0;
					byte[] lengthBytes = new byte[4]; // int is a four bytes block
					lengthBytes[0] = (byte) (length >> 24);
					lengthBytes[1] = (byte) (length >> 16);
					lengthBytes[2] = (byte) (length >> 8);
					lengthBytes[3] = (byte) length;
					
					while (bytesWrite < 4) {
						bytesWrite += channel.write(lengthBytes, bytesWrite, 4 - bytesWrite);
					}
					
					// sending the message
					bytesWrite = 0;
			    	while (bytesWrite < length) {
			    		bytesWrite += channel.write(bytes, offset + bytesWrite, length - bytesWrite);
			    	}
			    	
			    	listener.sent();
			    	
				} catch (DisconnectedException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		return true;
	}
	
	public Listener getListener() {
		return this.listener;
	}

	@Override
	public void close() {
		channel.disconnect();
	}

	@Override
	public boolean closed() {
		return channel.disconnected();
	}
}
