package Task2.Implementation;

import Task1.API.Channel;
import Task1.API.DisconnectedException;
import Task2.API.MessageQueue;

public class MessageQueueImpl extends MessageQueue {

	public MessageQueueImpl(Channel channel) {
		super(channel);
	}

	@Override
	public synchronized void send(byte[] bytes, int offset, int length) {
		try {
			
			// sending the length of the message
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
	    		bytesWrite += this.channel.write(bytes, offset + bytesWrite, length - bytesWrite);
	    	}
	    	
		} catch (DisconnectedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized byte[] receive() {
		try {
			
			// reading the length of the message
			int bytesRead = 0;
			byte[] lengthBytes = new byte[4];
			while (bytesRead < 4) {
				bytesRead += channel.read(lengthBytes, bytesRead, 4 - bytesRead);
			}
						
			int length = (lengthBytes[0] << 24) | (lengthBytes[1] << 16) | (lengthBytes[2] << 8) | lengthBytes[3];

			// reading the message
			bytesRead = 0;
			byte[] messageBytes = new byte[length];
			while (bytesRead < length) {
				bytesRead += channel.read(messageBytes, bytesRead, length - bytesRead);
			}
			
			return messageBytes;
		} catch (DisconnectedException e) {
			return null;
		}
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
