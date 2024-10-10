package Task3.Tests;

import Task3.API.Message;
import Task3.API.MessageQueue;
import Task3.API.QueueBroker;
import Task3.API.QueueBroker.AcceptListener;
import Task3.API.QueueBroker.ConnectListener;
import Task3.API.MessageQueue.Listener;
import Task3.API.Task;
import Task3.Implementation.QueueBrokerImpl;

public class EchoServerTests {

	public static void main(String[] args) {
		
		Task serverTask = new Task();
		serverTask.post(new Runnable() {

			@Override
			public void run() {
				QueueBroker serverBroker = new QueueBrokerImpl("ServerBroker");
				boolean bindResult = serverBroker.bind(5373, new AcceptListener () {

					@Override
					public void accepted(MessageQueue queue) {
						
						queue.setListener(new Listener() {

							@Override
							public void received(byte[] msg) {
								Message message = new Message(msg);
								queue.send(message);
							}

							@Override
							public void sent() {
								System.out.println("ServerBroker sent a message.");
								
							}

							@Override
							public void closed() {
								System.out.println("ServerBroker's message queue is now closed.");
							}
							
						});
						
					}
					
				});
			
				if (bindResult == false) {
					System.out.println("ServerBroker failed to bind on port 5373.");
				}
			}
			
		});
		
		Task clientTask = new Task();
		clientTask.post(new Runnable() {

			@Override
			public void run() {
				QueueBroker clientBroker = new QueueBrokerImpl("ClientBroker");
				boolean connectResult = clientBroker.connect("ServerBroker", 5373, new ConnectListener() {

					@Override
					public void connected(MessageQueue queue) {
						System.out.println("Connection to ServerBroker on port 5373 has been successfully established.");
						
						queue.setListener(new Listener() {

							@Override
							public void received(byte[] msg) {
								String message = new String(msg);
								System.out.println("Received message: "+message);
								assert message.equals("Hello World!");
								System.out.println("Tests OK");
							}

							@Override
							public void sent() {
								System.out.println("ClientBroker sent a message.");
								
							}

							@Override
							public void closed() {
								System.out.println("ClientBroker's message queue is now closed.");
							}
							
						});
						
						String messageString = "Hello World!";
						byte[] messageBytes = messageString.getBytes();
						Message message = new Message(messageBytes);
						queue.send(message);
					}

					@Override
					public void refused() {
						System.out.println("Connection refused.");
					}
					
				});
				
				if (connectResult == false) {
					System.out.println("ClientBroker failed to connect to ServerBroker on port 5373.");
				}
			}
			
		});
	}
}

