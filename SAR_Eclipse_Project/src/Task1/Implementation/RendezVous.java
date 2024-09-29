package Task1.Implementation;

import Task1.API.Broker;

public class RendezVous {
	
	private Broker brokerAccept, brokerConnect;
	private ChannelImpl channelAccept, channelConnect;
		
	private void _wait() {
		while (channelAccept == null || channelConnect == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				// nothing to do here
			}
		}
	}
	
	public synchronized ChannelImpl connect(Broker cb, int port) {
		this.brokerConnect = cb;
		channelConnect = new ChannelImpl(cb, port);
		if (channelAccept != null) {
			channelAccept.connect(channelConnect,cb.getName());
			notify();
		} else {
			_wait();
		}
		
		return channelConnect;
	}
	
	public synchronized ChannelImpl accept(Broker ab, int port) {
		this.brokerAccept = ab;
		channelAccept = new ChannelImpl(ab, port);
		if (channelConnect != null) {
			channelAccept.connect(channelConnect,ab.getName());
			notify();
		} else {
			_wait();
		}
		
		return channelAccept;
	}
}
