package Task1.Implementation;

import java.nio.channels.Channel;

import Task1.API.Broker;

public class RendezVous {
	
	private Broker brokerAccept, brokerConnect;
	private int port;
	
	public RendezVous (int port) {
		this.port = port;
	}
	
	public boolean isComplete() {
        return this.brokerAccept != null && this.brokerConnect != null;
    }
	
	public Channel createChannel() {
       return (Channel) new ChannelImpl(1024);
    }
	
	public void setAcceptBroker(Broker ba) {
        this.brokerAccept = ba;
    }

    public void setConnectBroker(Broker bc) {
        this.brokerConnect = bc;
    }

    public int getPort() {
        return port;
    }
}
