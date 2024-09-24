package Task1.API;

import java.util.HashMap;
import java.util.Map;

import Task1.Implementation.RendezVous;

public abstract class Broker {
	
	private Map<Integer, RendezVous> rendezVousList;
	
	public Broker(String name) {
		rendezVousList = new HashMap<>();
	}
	
	public abstract Channel accept(int port);
	
	public abstract Channel connect(String name, int port);
	
	public Map<Integer, RendezVous> getRendezVousList() {
		return this.rendezVousList;
	}
}
