package Task1.API;

public abstract class Broker {
	
	public Broker(String name) {
		
	}
	
	public abstract Channel accept(int port);
	
	public abstract Channel connect(String name, int port);
}
