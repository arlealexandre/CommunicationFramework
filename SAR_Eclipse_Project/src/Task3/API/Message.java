package Task3.API;

public class Message {

	private byte[] bytes;
	private int offset;
	private int length;
	
	public Message(byte[] bytes) {
		this.bytes = bytes;
		this.offset = 0;
		this.length = bytes.length;
	}
	
	public Message(byte[] bytes, int offset, int length) {
		this.bytes = bytes;
		this.offset = offset;
		this.length = length;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public int getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}
}
