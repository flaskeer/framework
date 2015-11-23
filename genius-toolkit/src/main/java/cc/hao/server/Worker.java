package cc.hao.server;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cc.hao.core.OMRunnable;
import cc.hao.core.OMThread;
import cc.hao.util.Cfg;
import cc.hao.util.ChannelReader;
import cc.hao.util.ChannelWriter;
import cc.hao.util.FileUtil;

public class Worker implements OMRunnable{


	private static ByteBuffer buffer;
	private static ExecutorService pool;
	
	static{
		buffer = ByteBuffer.allocate(Cfg.buffer());
		pool = Executors.newFixedThreadPool(Cfg.pool());
	}
	
	private SelectionKey key;
	private SocketChannel channel;
	private OMRunnable updatable;
	
	
	public Worker(SelectionKey key) {
		this.key = key;
		this.channel = (SocketChannel) key.channel();
		this.updatable = new Reading();
	}

	@Override
	public void run() throws Exception {
		// TODO Auto-generated method stub
		updatable.run();	
	}
	
	private class Reading implements OMRunnable,ChannelReader.Listener{

		ChannelReader reader;

		public Reading() {
			reader = new ChannelReader(this, channel, buffer);
		}
		@Override
		public void onFinish(byte[] bytes) throws Exception {
			updatable = new OMRunnable.Empty();
			key.interestOps(0);
			if(bytes.length == 0){
				channel.close();
				return;
			}
			String in = new String(bytes);
			pool.execute(new Processing(in));
		}

		@Override
		public void run() throws Exception {
			reader.update();
		}
		
	}
	
	private class Processing extends OMThread{
		String in;
		public Processing(String in) {
			this.in = in;
		}

		@Override
		protected void doRun() throws Exception {
			Request request = new Request(in);
			if(request.isResources()){
				toWrite(FileUtil.bytes(request.path()));
			}else{
//				toWrite(new Controller(request));
			}
		}

		private void toWrite(byte[] bytes) throws Exception {
			if(!key.isValid()){
				channel.close();
				return;
			}
			updatable = new Writing(bytes);
		}
		
	}
	
	private class Writing implements OMRunnable,ChannelWriter.Listener{

		ChannelWriter writer;
		
		public Writing(byte[] out) {
			writer = new ChannelWriter(buffer, channel, out, this);
			key.interestOps(SelectionKey.OP_WRITE);
			key.selector().wakeup();
		}

		@Override
		public void run() throws Exception {
			writer.update();
		}

		@Override
		public void onFinish() throws Exception {
			channel.close();
		}
		
	}

}
