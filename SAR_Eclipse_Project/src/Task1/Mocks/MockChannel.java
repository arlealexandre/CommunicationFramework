package Task1.Mocks;

import Task1.API.Channel;

public class MockChannel extends Channel {

	@Override
	public int read(byte[] bytes, int offset, int length) {
		return 0;
	}

	@Override
	public int write(byte[] bytes, int offset, int length) {
		return 0;
	}

	@Override
	public void disconnect() {
	}

	@Override
	public boolean disconnected() {
		return false;
	}

}