package cc.hao.server;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

import cc.hao.core.OMRunnable;
import cc.hao.util.Cfg;
import cc.hao.util.LoadUtil;

public class Server {

	public void run() throws Exception{
		init();
		Selector selector = openSelector();
		while(true){
			doSelector(selector);
		}
	}
	
	private void doSelector(Selector selector) throws Exception {
		selector.select();
		Set<SelectionKey> selectedKeys = selector.selectedKeys();
		Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
		while(iterator.hasNext()){
			((OMRunnable)iterator.next().attachment()).run();
		}
		selectedKeys.clear();
	}

	private Selector openSelector() throws Exception {
		Selector selector = Selector.open();
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		key.attach(new Accepter(key));
		serverSocketChannel.socket().bind(new InetSocketAddress(Cfg.port()));
		return selector;
	}

	private void init() throws Exception {
		Cfg.init();
		LoadUtil.init();
		SessionUtil.init();
	}

	public static void main(String[] args) throws Exception {
		new Server().run();
	}
	
}
