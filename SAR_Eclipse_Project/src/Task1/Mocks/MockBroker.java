package Task1.Mocks;

import Task1.API.Broker;
import Task1.API.Channel;

public class MockBroker extends Broker {

	public MockBroker(String name) {
		super(name);
	}

	@Override
	public Channel accept(int port) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Channel connect(String name, int port) {
		// TODO Auto-generated method stub
		return null;
	}

}
