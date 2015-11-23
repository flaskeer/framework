package cc.hao.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

public class ChannelWriter {

	private ByteBuffer buffer;
	private ByteChannel channel;
	private byte[] toWrite;
	private Listener listener;
	private int index = 0;
	
	public static interface Listener{
		public void onFinish() throws Exception;
	}

	public ChannelWriter(ByteBuffer buffer, ByteChannel channel, byte[] toWrite, Listener listener) {
		this.buffer = buffer;
		this.channel = channel;
		this.toWrite = toWrite;
		this.listener = listener;
	}
	
	public void update() throws Exception{
		int length = Math.min(Cfg.buffer(), toWrite.length - index);
		if(length > 0){
			buffer.put(toWrite, index, length);
			buffer.flip();
			channel.write(buffer);
			buffer.clear();
		}
		if(length < Cfg.buffer()){
			listener.onFinish();
			return;
		}
		index += Cfg.buffer();
	}
	
}
