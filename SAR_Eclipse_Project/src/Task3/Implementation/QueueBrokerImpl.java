package Task3.Implementation;

import java.util.HashMap;
import java.util.Map;

import Task1.API.Broker;
import Task1.API.Channel;
import Task1.API.DisconnectedException;
import Task1.Implementation.BrokerImpl;
import Task1.Implementation.BrokerManager;
import Task1.Implementation.ChannelImpl;
import Task3.API.*;

public class QueueBrokerImpl extends QueueBroker {
	
	private Broker broker;
	private Map<Integer, QueueBroker.AcceptListener> accepts;

	public QueueBrokerImpl(String name) {
		this.broker = new BrokerImpl(name);
		this.accepts = new HashMap<>();
	}

	@Override
	public boolean bind(int port, AcceptListener listener) {
		
		synchronized(accepts) {
			if (this.accepts.containsKey(port)) {
				return false;
			}
		}
				
		Task task = new Task();
		task.post(new Runnable() {

			@Override
			public void run() {
				ChannelImpl channel = (ChannelImpl) broker.accept(port);
				MessageQueue messageQueue = new MessageQueueImpl(channel);
				listener.accepted(messageQueue);
				
				Task task = new Task();
				task.post(new Runnable() {

					@Override
					public void run() {
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
							
							((MessageQueueImpl) messageQueue).getListener().received(messageBytes);
						} catch (DisconnectedException e) {
							e.printStackTrace();
						}
					}
					
				});
			}
			
		});
		
		return true;
	}

	@Override
	public boolean unbind(int port) {
		synchronized(accepts) {
			if (this.accepts.containsKey(port)) {
				return false;
			}
		}
		synchronized(accepts) {
			this.accepts.remove(port);
		}
		
		return true;
	}

	@Override
	public boolean connect(String name, int port, ConnectListener listener) {
		Broker broker = BrokerManager.getInstance().getBroker(name);
		
		if (broker == null) {
			return false;
		}
		
		Task task = new Task();
		task.post(new Runnable() {

			@Override
			public void run() {
				ChannelImpl channel = (ChannelImpl) broker.connect(name, port);
				if (channel == null) {
					listener.refused();
				} else {
					MessageQueue messageQueue = new MessageQueueImpl(channel);
					listener.connected(messageQueue);
					
					Task task = new Task();
					task.post(new Runnable() {

						@Override
						public void run() {
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
								
								((MessageQueueImpl) messageQueue).getListener().received(messageBytes);
							} catch (DisconnectedException e) {
								e.printStackTrace();
							}
						}
						
					});
				}
			}
			
		});
		
		return true;
	}
		
	public Broker getBroker() {
		return this.broker;
	}

}
