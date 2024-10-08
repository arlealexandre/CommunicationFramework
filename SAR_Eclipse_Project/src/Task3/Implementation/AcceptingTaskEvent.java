package Task3.Implementation;

import Task1.Implementation.ChannelImpl;
import Task3.API.EventTask;
import Task3.API.MessageQueue;
import Task3.API.QueueBroker;

public class AcceptingTaskEvent extends EventTask {
	
	private int port;
	private QueueBroker.AcceptListener listener;
    private boolean alreadyAccepted;
    private MessageQueue acceptMessageQueue, connectMessageQueue;
    
    public AcceptingTaskEvent(int port, QueueBroker.AcceptListener listener) {
        this.port = port;
        this.listener = listener;
    	this.alreadyAccepted = false;
        
        ChannelImpl acceptChannel = new ChannelImpl(null, port);
        ChannelImpl connectChannel = new ChannelImpl(null, port);
    	
    	this.connectChannels(acceptChannel, connectChannel);
    	
    	this.acceptMessageQueue = new MessageQueueImpl(acceptChannel);
    	this.connectMessageQueue = new MessageQueueImpl(connectChannel);
    }
        
    public MessageQueue getAcceptQueue() {
        return acceptMessageQueue;
    }
    
    public MessageQueue getConnectQueue() {
        return connectMessageQueue;
    }
    
    public void connectChannels(ChannelImpl c1, ChannelImpl c2) {
    	c1.setRemoteChannel(c2);
    	c2.setRemoteChannel(c1);
    	c1.setOut(c2.getIn());
    	c2.setOut(c1.getIn());
    	c1.setIn(c2.getOut());
    	c2.setIn(c1.getOut());
	}
    
    public boolean isAlreadyAccepted() {
    	return this.alreadyAccepted;
    }
}
