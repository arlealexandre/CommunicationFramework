package Task1.Tests;

import Task1.API.*;
import Task1.Mocks.*;

public class EchoServerTests {
	
	private static final String HOST = "localhost";
	private static final int PORT = 5373;
	
	private static final int BUFFER_SIZE = 512;
	
	private static final String MESSAGE = "echo Hello";
	private static final int MESSAGE_LENGTH = 10;

	public static void main(String[] args) {
		
		Broker broker = new MockBroker(HOST); // Instance of Broker
		
		Task serverTask = new MockTask(broker, new Runnable() {
			public void run() {
				
				while (true) {
					
					Channel client = broker.accept(PORT);
					assert client.disconnected() == false;
					
					byte[] buffer = new byte[BUFFER_SIZE];
					
					while(!client.disconnected()) {
						
						int bytesRead = client.read(buffer, 0, MESSAGE_LENGTH);
						assert bytesRead > 0 && bytesRead <= MESSAGE_LENGTH;
						
						String message = new String(buffer, 0, bytesRead);
						assert message.length() == MESSAGE_LENGTH;
						assert message.equals(MESSAGE);
						
						client.write(buffer, 0, bytesRead);
					}
					
					assert client.disconnected() == true;
				}
			}
		}); // Instance of Server
		
		Task clientTask = new MockTask(broker, new Runnable() {
			public void run() {
				
				Channel channel = broker.connect(HOST, PORT);
				assert channel.disconnected() == false;
								
				System.out.println("Sending: "+MESSAGE);
				int bytesWrite = channel.write(MESSAGE.getBytes(), 0, MESSAGE.length());
				assert bytesWrite > 0 && bytesWrite <= MESSAGE.length();
				
				byte[] response = new byte[BUFFER_SIZE];
				int bytesRead = channel.read(response, 0, MESSAGE_LENGTH);
				assert bytesRead > 0 && bytesRead <= BUFFER_SIZE;
				
				String receivedMessage = new String(response,0,bytesRead);
				assert receivedMessage.length() == MESSAGE_LENGTH;
				assert receivedMessage == MESSAGE;
				
				System.out.println("Received: "+receivedMessage);
				
				channel.disconnect();
			}
		}); // Instance of Client
		
		// Start all tasks
        serverTask.start();
        clientTask.start();
	}

}

