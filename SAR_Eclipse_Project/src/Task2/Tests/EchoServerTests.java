package Task2.Tests;

import Task1.API.Broker;
import Task1.API.Channel;
import Task1.API.DisconnectedException;
import Task1.Implementation.BrokerImpl;
import Task2.API.MessageQueue;
import Task2.API.QueueBroker;
import Task2.API.Task;
import Task2.Implementation.QueueBrokerImpl;
import Task2.Implementation.TaskImpl;

public class EchoServerTests {

	public static void main(String[] args) {
		
		Broker serverBroker = new BrokerImpl("ServerBroker");
		Broker clientBroker = new BrokerImpl("ClientBroker");
		
		QueueBroker serverQueueBroker = new QueueBrokerImpl(serverBroker);
		QueueBroker clientQueueBroker = new QueueBrokerImpl(clientBroker);
		
		Task serverTask = new TaskImpl(serverQueueBroker, new Runnable () {
			public void run() {
				System.out.println("Server: waiting for new client connection...");
				
				QueueBroker queueBroker = Task.getTask().getQueueBroker();
				MessageQueue messageQueue = queueBroker.accept(5373);
				
				byte[] buffer = messageQueue.receive();
		        
		        messageQueue.send(buffer, 0, buffer.length);
		        
		        messageQueue.close();
			}
		});
		
		Task clientTask = new TaskImpl(clientQueueBroker, new Runnable () {
			public void run() {
				QueueBroker queueBroker = Task.getTask().getQueueBroker();
				MessageQueue messageQueue = queueBroker.connect("ServerBroker", 5373);
		       
		        	
		        String message = "Hello World!";
				byte[] messageBytes = message.getBytes();
		        	
		        messageQueue.send(messageBytes, 0, messageBytes.length);
		        	
		        byte[] receivedBytes = messageQueue.receive();
		            
		        String receivedMessage = new String(receivedBytes);
		        assert receivedMessage.equals(message);
		            
		        System.out.println("Test: OK");
		        messageQueue.close();
			}
		});
		
		serverTask.start();
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        clientTask.start();
	}
}
