package Task3.API;

import Task3.Implementation.EventPump;

public class Task {
	
	private boolean isKilled;
	private EventPump eventPump;
	protected Runnable runnable;
	
	public Task() {
		this.isKilled = false;
		this.eventPump = EventPump.getInstance();
	}
		
	public void post(Runnable r) {
		this.eventPump.postTask(r);
	}
	
	public static Task task() {
		return null;
	}
	
	public void kill() {
		this.isKilled = true;
	}
	
	public boolean killed() {
		return this.isKilled;
	}
	
}
