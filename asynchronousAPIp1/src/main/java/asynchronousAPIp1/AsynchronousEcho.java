package asynchronousAPIp1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class AsynchronousEcho {
	public static void main(String[] args) throws IOException{
		Selector selector = Selector.open();
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress(3000));
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT); //The selector will notify of incoming connections. 
		/*
		 * server socket channel preparation code. It opens the server socket channel and makes 
		 * it non-blocking, then registers an NIO key selector for pro-cessing events.
		 * The main loop iterates over the selector keys that have events ready for
		 * processing and dispatches them to specialized methods depending on the event type
		 * (new connections, data has arrived, or data can be sent again).
		 * */
		while(true) {
			selector.select(); //This collects all non-blocking I/O notifications.
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while(it.hasNext()) {
				SelectionKey key = it.next();
				if(key.isAcceptable()) { //We have a new connection.
					newConnection(selector,key);
				}else if(key.isReadable()) { // A socket has recieved data
					echo(key);
				}else if(key.isWritable()) { // A socket is ready for writing again
					continueEcho(selector,key);
				}
				it.remove(); //Selection keys need to be manually removed, or they will be available again in the next loop iteration.
			}
		}
	}
}


