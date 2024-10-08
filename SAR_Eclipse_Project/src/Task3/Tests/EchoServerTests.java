package Task3.Tests;

import Task3.API.*;
import Task3.API.MessageQueue;
import Task3.API.MessageQueue.Listener;
import Task3.API.QueueBroker;
import Task3.API.QueueBroker.AcceptListener;
import Task3.API.QueueBroker.ConnectListener;
import Task3.Implementation.QueueBrokerImpl;
import Task3.Implementation.TaskImpl;

public class EchoServerTests {

	public static void main(String[] args) {
		
		QueueBroker serverQueueBroker = new QueueBrokerImpl("ServerQueueBroker");
		QueueBroker clientQueueBroker = new QueueBrokerImpl("ClientQueueBroker");
		
		Task serverTask = new TaskImpl(serverQueueBroker, new Runnable() {

			@Override
			public void run() {

				QueueBroker queueBroker = Task.getQueueBroker();
				
				queueBroker.bind(5373, new AcceptListener () {

					@Override
					public void accepted(MessageQueue queue) {
						
						queue.setListener(new Listener() {

							@Override
							public void received(byte[] msg) {
								queue.send(msg);
								System.out.println("Message sent by server");
							}

							@Override
							public void sent() {
								System.out.println("A message has been sent.");
							}

							@Override
							public void closed() {
								System.out.println("MessageQueue is closed.");
							}
							
						});
					}
					
				});
			}
			
		});
		
		TaskImpl clientTask = new TaskImpl(clientQueueBroker, new Runnable() {

			@Override
			public void run() {
				QueueBroker queueBroker = TaskImpl.getQueueBroker();
				
				queueBroker.connect("ServerQueueBroker", 5373, new ConnectListener() {

					@Override
					public void connected(MessageQueue queue) {
						String message = "Hello World!";
						byte[] messageBytes = message.getBytes();
						queue.send(messageBytes);
						
						queue.setListener((Listener) new Listener() {

							@Override
							public void received(byte[] msg) {
								String receivedMessage = new String(msg);
								System.out.println("Received: "+receivedMessage);
								assert receivedMessage.equals(message);
								System.out.println("Tests OK");
							}

							@Override
							public void sent() {
								System.out.println("A message has been sent.");
								
							}

							@Override
							public void closed() {
								System.out.println("MessageQueue is closed.");
							}
							
						});
					}

					@Override
					public void refused() {
						System.out.println("Connection has been refused");
					}
					
				});
			}
			
		});
		
		serverTask.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// nothing to do here
		}
		clientTask.start();
	}
}

