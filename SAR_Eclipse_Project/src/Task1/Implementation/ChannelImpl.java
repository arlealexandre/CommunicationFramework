package Task1.Implementation;

import Task1.API.Broker;
import Task1.API.Channel;
import Task1.API.CircularBuffer;
import Task1.API.DisconnectedException;

public class ChannelImpl extends Channel {
	
	private CircularBuffer in;
    private CircularBuffer out;
    private boolean isDisconnected;
    private ChannelImpl remoteChannel;
    private Boolean dangling;
 
    public ChannelImpl(Broker b, int port) {
    	super(b);
    	this.in = new CircularBuffer(1024);
    	this.dangling = false;
    }
    
    public void connect(ChannelImpl remote, String name) {
    	this.remoteChannel = remote;
    	remote.remoteChannel = this;
    	this.out = remote.in;
    	remote.out = this.in;
    }

	@Override
	public synchronized int read(byte[] bytes, int offset, int length) throws DisconnectedException {
		if (disconnected()) { 
			throw new DisconnectedException("Channel disconnected");
		}
        
        int nbytes = 0;
        try {
        	while (nbytes == 0) {
           	 if (in.empty()) {
           		 synchronized (in) {
           			 while (in.empty()) {
           				 if (disconnected() || dangling) {
           					 throw new DisconnectedException("");
           				 }
           				 try {
   							in.wait();
   						} catch (InterruptedException e) {
   							// nothing to do here
   						}
           			 }
           		 }
           	 }
           	 
           	 while (nbytes < length && !in.empty()) {
           		 byte val = in.pull();
           		 bytes[offset + nbytes] = val;
           		 nbytes++;
           	 }
           	 
           	 if (nbytes != 0) {
           		 synchronized(in) {
           			 in.notify();
           		 }
           	 }
            }
        } catch (DisconnectedException e) {
        	if (!disconnected()) {
        		isDisconnected = true;
        		synchronized(out) {
        			out.notifyAll();
        		}
        	}
        	throw e;
        }
        return nbytes;
	}

	@Override
	public synchronized int write(byte[] bytes, int offset, int length) throws DisconnectedException {
		if (disconnected()) { 
			throw new DisconnectedException("Channel disconnected");
		}
        
        int nbytes = 0;
        
        while (nbytes == 0) {
        	if (out.full()) {
        		synchronized(out) {
        			while(out.full()) {
        				if (disconnected() ) {
        					throw new DisconnectedException("");
        				}
        				if (dangling) {
        					return length;
        				}
        				try {
							out.wait();
						} catch (InterruptedException e) {
							// nothing to do here
						}
        			}
        		}
        	}
        	
        	while (nbytes < length && !out.full()) {
        		byte val = bytes[offset + nbytes];
        		out.push(val);
        		nbytes++;
        	}
        	
        	if (nbytes != 0) {
        		synchronized (out) {
        			out.notify();
        		}
        	}
        }
        return nbytes;
	}

	@Override
	public void disconnect() {
		synchronized(this) {
			if (disconnected()) {
				return;
			}
			this.isDisconnected = true;
			this.remoteChannel.dangling = true;
		}
		
		synchronized(out) {
			out.notifyAll();
		}
		
		synchronized(in) {
			in.notifyAll();
		}	
	}

	@Override
	public boolean disconnected() {
		return this.isDisconnected;
	}

}
