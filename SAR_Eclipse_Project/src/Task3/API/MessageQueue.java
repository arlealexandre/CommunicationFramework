package Task3.API;

public abstract class MessageQueue {

	interface Listener {
		void received(byte[] msg);
		void sent();
		void closed();
	}
	
	public abstract void setListener(Listener l);
	
	public abstract boolean send(byte[] bytes);
	public abstract boolean send(byte[] bytes, int offset, int length);
	
	public abstract void close();
	public abstract boolean closed();
}
