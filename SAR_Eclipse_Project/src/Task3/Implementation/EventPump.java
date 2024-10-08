package Task3.Implementation;

import java.util.LinkedList;
import java.util.Queue;

import Task3.API.EventTask;

public class EventPump extends Thread {

	private static EventPump instance;
	private Queue<EventTask> eventQueue;
	private EventTask currentTask;
	private boolean running;
	
	public EventPump() {
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
	
	public synchronized void postEvent(EventTask eventTask) {
		this.eventQueue.add(eventTask);
		notify();
	}
	
	public synchronized boolean removeEvent(EventTask eventTask) {
		return this.eventQueue.remove(eventTask);
	}
	
	public static EventPump getEventPump() {
	    Thread t = Thread.currentThread();
		if (t instanceof EventPump) {
			return (EventPump) t;
		}
		return null;
	}
	
	public EventTask getCurrentTask() {
		return this.currentTask;
	}
	
	@Override
	public void run() {
		while (running) {
			EventTask eventTask = null;

            synchronized (eventQueue) {
                if (!eventQueue.isEmpty()) {
                	eventTask = eventQueue.poll();
                	
                	if (eventTask != null) {
    	                try {
    	                	this.currentTask = eventTask;
    	                	eventTask.run();
    	                } catch (Exception e) {
    	                    e.printStackTrace();
    	                }
    	            }
                }
            }
		}
	}
	
	public void stopEventPump() {
		this.running = false;
	}
}
