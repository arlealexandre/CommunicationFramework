package Task3.API;

public abstract class Task {
	
	public abstract void post();
	
	public static Task task() {
		return null;
	}
	
	public abstract void kill();
	
	public abstract boolean killed();
}
