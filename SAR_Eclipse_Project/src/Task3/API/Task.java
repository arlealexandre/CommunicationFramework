package Task3.API;

public abstract class Task extends Thread {
	
	private Runnable runnable;
	private QueueBroker queueBroker;

	public Task(QueueBroker b, Runnable r) {
		this.queueBroker = b;
		this.runnable = r;
	}
			
	@Override
	public void run() {
		if (this.runnable != null) {
			runnable.run();
		} else {
			throw new NullPointerException("Runnable is null.");
		}
	}
	
	public static QueueBroker getQueueBroker() {
		if (Thread.currentThread() instanceof Task) {
			return ((Task) Thread.currentThread()).queueBroker;
		}
		return null;
	}

}
