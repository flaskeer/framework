package cc.hao.util;

import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.ArrayList;
import java.util.List;

public class ChannelReader {
	
	private Listener listener;
	private ByteBuffer buffer;
	private ByteChannel channel;
	private List<Byte> bytes = new ArrayList<>();
	
	public static interface Listener{
		public void onFinish(byte[] bytes) throws Exception;
	}

	public ChannelReader(ByteChannel channel) {
		this.channel = channel;
		this.buffer = ByteBuffer.allocate(Cfg.buffer());
	}

	public ChannelReader(Listener listener, ByteChannel channel,ByteBuffer buffer) {
		this.listener = listener;
		this.channel = channel;
		this.buffer = buffer;
	}

	public boolean update() throws Exception{
		int length = channel.read(buffer);
		if(length > 0){
			buffer.flip();
			drain();
			buffer.clear();
		}
		if(length < Cfg.buffer()){
			onFinish();
			return false;
		}
		return true;
	}

	public void drain() {
		while(buffer.hasRemaining()){
			bytes.add(buffer.get());
		}
	}
	
	public void onFinish() throws Exception{
		if(listener != null){
			listener.onFinish(ArrayKit.toArray(bytes));
		}
	}

	public byte[] read() throws Exception {
		while(update()){ }
		return ArrayKit.toArray(bytes);
	}
	

}
