package Task1.Implementation;

import Task1.API.Channel;
import Task1.API.CircularBuffer;
import Task1.API.DisconnectedException;

public class ChannelImpl extends Channel {
	
	private CircularBuffer in;
    private CircularBuffer out;
    private boolean isDisconnected;
 
    public ChannelImpl(int capacity) {
    	this.in = new CircularBuffer(capacity);
    	this.out = new CircularBuffer(capacity);
    	this.isDisconnected = false;
    }

	@Override
	public synchronized int read(byte[] bytes, int offset, int length) throws DisconnectedException {
		if (disconnected()) { 
			throw new DisconnectedException("Channel disconnected");
		}
        
        while (this.in.empty()) {
            try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        
        int bytesRead = 0;
        
        while (bytesRead < length) {
        	bytes[offset + bytesRead++] = this.in.pull();
        }
        
        notifyAll();

        if (bytesRead == 0 && disconnected()) {
            throw new DisconnectedException("End of stream");
        }

        return bytesRead;
	}

	@Override
	public synchronized int write(byte[] bytes, int offset, int length) throws DisconnectedException {
		if (disconnected()) { 
			throw new DisconnectedException("Channel disconnected");
		}
        
        while (this.out.full()) {
            try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        
        int bytesWritten = 0;
        
        while (bytesWritten < length) {
        	this.out.push(bytes[offset + bytesWritten++]);
        }
        
        notifyAll();

        return bytesWritten;
	}

	@Override
	public void disconnect() {
		this.isDisconnected = true;
	}

	@Override
	public boolean disconnected() {
		return this.isDisconnected;
	}

}
