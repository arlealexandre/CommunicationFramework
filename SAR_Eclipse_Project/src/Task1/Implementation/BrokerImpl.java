package Task1.Implementation;


import java.util.HashMap;
import java.util.Map;

import Task1.API.Broker;
import Task1.API.Channel;

public class BrokerImpl extends Broker {
	
	private Map<Integer, RendezVous> rendezVousList;
	
	public BrokerImpl(String name) {
		super(name);
		rendezVousList = new HashMap<>();
		BrokerManager.getInstance().addBroker(this);
	}

	@Override
	public Channel accept(int port) {
		RendezVous rdv;
		
		synchronized(rendezVousList) {
			rdv = this.rendezVousList.get(port);
			if (rdv != null) {
	        	throw new IllegalStateException("Port already accepting.");
	        }
	        
	        rdv = new RendezVous();
	        this.rendezVousList.put(port, rdv);
	        this.rendezVousList.notifyAll();
		}
        
        return (Channel) rdv.accept(this, port);
	}

	@Override
	public Channel connect(String name, int port) {
		Broker remoteBroker = BrokerManager.getInstance().getBroker(name);
		
		if (remoteBroker == null) {
			return null;
		} else {
			RendezVous rdv;
			synchronized(rendezVousList) {
				rdv = rendezVousList.get(port);
				
				while (rdv == null) {
					try {
						rendezVousList.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					rdv = rendezVousList.get(port);  
				}
			}
			return (Channel) rdv.connect(this, port);
		}
	}

}
