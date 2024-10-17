package Task3.Implementation;

import java.util.LinkedList;
import java.util.Queue;

import Task3.API.Task;

public class EventPump extends Thread {

	private static EventPump instance;
	private Queue<Runnable> eventQueue;
	private Runnable currentTask;
	private boolean running;
	
	private EventPump() {
		eventQueue = new LinkedList<>();
		running = true;
		this.start();
	}
	
	public static synchronized EventPump getInstance() {
		if (instance == null) {
			return new EventPump();
		} else {
			return instance;
		}
	}
	
	public synchronized void postTask(Runnable eventTask) {
		this.eventQueue.add(eventTask);
		this.notify();
	}
	
	public synchronized boolean removeTask(Runnable eventTask) {
		return this.eventQueue.remove(eventTask);
	}
	
	public static EventPump getEventPump() {
	    Thread t = Thread.currentThread();
		if (t instanceof EventPump) {
			return (EventPump) t;
		}
		return null;
	}
	
	public Runnable getCurrentTask() {
		return this.currentTask;
	}
	
	@Override
	public void run() {
		while (this.running) {
			Runnable task;
			synchronized (this) {
				while (this.eventQueue.isEmpty()) {
					try {
						wait();
					} catch (InterruptedException e) {
						return;
					}
				}
			}
			
			task = this.eventQueue.poll(); // Fetch the next event

			if (task != null) {
				this.currentTask = task;
				task.run();
				this.currentTask = null;
			}
		}
	}
	
	public void stopEventPump() {
		this.running = false;
	}
}
