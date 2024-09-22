package Task1.Mocks;

import Task1.API.Broker;
import Task1.API.Task;

public class MockTask extends Task {

	public MockTask(Broker b, Runnable r) {
		super(b, r);
	}

}