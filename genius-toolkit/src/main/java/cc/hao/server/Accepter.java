package cc.hao.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import cc.hao.core.OMRunnable;

public class Accepter implements OMRunnable{

	SelectionKey key;
	
	
	public Accepter(SelectionKey key) {
		this.key = key;
	}
	
	public SelectionKey register(SocketChannel channel) throws IOException{
		return channel.register(key.selector(), SelectionKey.OP_READ);
	}

	public SocketChannel accept() throws IOException{
		return ((ServerSocketChannel) key.channel()).accept();
	}

	@Override
	public void run() throws Exception {
		SocketChannel socketChannel = accept();
		socketChannel.configureBlocking(false);
		SelectionKey k = register(socketChannel);
		k.attach(new Worker(k));
	}

}
