package Task1.API;

public abstract class Task extends Thread {
	
	private Broker broker;
	protected Runnable runnable;

	public Task(Broker b, Runnable r) {
		this.broker = b;
		this.runnable = r;
	}
	
	public static Broker getBroker() {
		if (Thread.currentThread() instanceof Task) {
			return ((Task) Thread.currentThread()).broker;
		}
		return null;
	}
}
