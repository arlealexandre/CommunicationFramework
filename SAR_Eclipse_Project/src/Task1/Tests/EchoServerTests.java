package Task1.Tests;

import Task1.API.*;
import Task1.Implementation.BrokerImpl;
import Task1.Implementation.TaskImpl;

public class EchoServerTests {
	
	public static void main(String[] args) {
		
		Broker serverBroker = new BrokerImpl("ServerBroker");
		Broker clientBroker = new BrokerImpl("ClientBroker");
		
		Task serverTask = new TaskImpl(serverBroker, new Runnable() {
			public void run() {
				
				System.out.println("Server: waiting for new client connection...");
				
				Broker broker = Task.getBroker();
				Channel serverChannel = broker.accept(5373);
				
				byte[] buffer = new byte[256];
		        int bytesRead = 0;
		        try {
		            while ((bytesRead = serverChannel.read(buffer, 0, buffer.length)) > 0) {
		            	int byteWrite = 0;
						while (byteWrite < bytesRead) {
							byteWrite += serverChannel.write(buffer, byteWrite, bytesRead - byteWrite);
						}        
		             }
		        } catch (DisconnectedException e) {
		        	System.out.println("Disconnection occured");
		        } finally {
		        	serverChannel.disconnect();
		        }
			}
		});
		
		Task clientTask = new TaskImpl(clientBroker, new Runnable() {
			public void run() {
				Broker broker = Task.getBroker();
				Channel clientChannel = broker.connect("ServerBroker", 5373);
		        
		        try {
		        	
		        	String message = "Hello World!";
					byte[] messageBytes = message.getBytes();
		        	
		        	int bytesWrite = 0;
		        	while (bytesWrite < messageBytes.length) {
		        		bytesWrite += clientChannel.write(messageBytes, bytesWrite, messageBytes.length - bytesWrite);
		        	}
		        	
		            byte[] receivedBytes = new byte[255];
		            int bytesRead = clientChannel.read(receivedBytes, 0, receivedBytes.length);
		            
		            String receivedMessage = new String(receivedBytes);
		            assert receivedMessage.equals(message);
		            
		            System.out.println("Test: OK");
		        } catch (DisconnectedException e) {
		        	System.out.println("Disconnection occured");
		        } finally {
		        	clientChannel.disconnect();
		        }
				
			}
		});
		
        serverTask.start();
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        clientTask.start();
	}

}

