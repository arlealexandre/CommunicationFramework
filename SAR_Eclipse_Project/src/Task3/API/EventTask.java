package Task3.API;

import Task3.Implementation.EventPump;

public abstract class EventTask {
	
	private boolean isKilled;
	private EventPump eventPump;
	protected Runnable runnable;
	
	public EventTask() {
		this.isKilled = false;
		this.eventPump = EventPump.getInstance();
	}
		
	public void post(Runnable r) {
		this.runnable = r;
		this.eventPump.postEvent(this);
	}
	
	public static EventTask task() {
		EventPump pump = EventPump.getEventPump();
		if (pump != null) {
			return pump.getCurrentTask();
		}
		return null;
	}
	
	public void kill() {
		this.isKilled = true;
		this.eventPump.removeEvent(this);
	}
	
	public boolean killed() {
		return this.isKilled;
	}
	
	public void run() {
		this.runnable.run();
	}
}
