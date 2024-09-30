package Task2.API;

import Task1.API.Broker;

public abstract class Task extends Thread {
	
	private Broker broker;
	private QueueBroker queueBroker;
	protected Runnable runnable;

	public Task(Broker b, Runnable r) {
		this.broker = b;
		this.runnable = r;
	}
	
	public Task(QueueBroker b, Runnable r) {
		this.queueBroker = b;
		this.runnable = r;
	}
	
	public Broker getBroker() {
		return this.broker;
	}
	
	public QueueBroker getQueueBroker() {
		return this.queueBroker;
	}
	
	public static Task getTask() {
		if (Thread.currentThread() instanceof Task) {
			return (Task) Thread.currentThread();
		}
		return null;
	}
	
}
