package Task1.Implementation;


import Task1.API.Broker;
import Task1.API.Channel;

public class BrokerImpl extends Broker {
	
	public BrokerImpl(String name) {
		super(name);
		BrokerManager.getInstance().addBroker(name, this);
	}

	@Override
	public synchronized Channel accept(int port) {
		RendezVous rdv = this.getRendezVousList().get(port);
		
        if (rdv == null) {
            rdv = new RendezVous(port);
            this.getRendezVousList().put(port, rdv);
        }
        
        rdv.setAcceptBroker(this);
        
        while (!rdv.isComplete()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return (Channel) rdv.createChannel();
	}

	@Override
	public synchronized Channel connect(String name, int port) {
		Broker remoteBroker = BrokerManager.getInstance().getBroker(name);
		
		if (remoteBroker == null) {
			return null; // no broker found
		} else {
			RendezVous rdv = remoteBroker.getRendezVousList().get(port);
			
			if (rdv == null) {
				rdv = new RendezVous(port);
				remoteBroker.getRendezVousList().put(port, rdv);
			}
			
			rdv.setConnectBroker(this);
			
			while (!rdv.isComplete()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			return (Channel) rdv.createChannel();
		}
	}

}
