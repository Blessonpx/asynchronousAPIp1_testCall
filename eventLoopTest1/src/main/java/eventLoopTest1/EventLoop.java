package eventLoopTest1;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;


public final class EventLoop {
	private final ConcurrentLinkedDeque<Event> events = new ConcurrentLinkedDeque<>();
	private final ConcurrentHashMap<String,Consumer<Object>> handlers = new ConcurrentHashMap<>();
	
	// Handlers are stored in a map where each key has a handler.
	public EventLoop on(String key, Consumer<Object> handler) {
		handlers.put(key, handler);
		return this;
	}
	// Dispatching is pushing events to a queue.
	public void dispatch(Event event) {events.add(event);}
	public void stop() {Thread.currentThread().interrupt();}
	
	public void run() {
		while(!(events.isEmpty() && Thread.interrupted())) {
			if(!events.isEmpty()) {
				Event event = events.pop();
				if(handlers.containsKey(event.getKey())) {
					handlers.get(event.getKey()).accept(event.getData());
				}else {
					System.err.println("No Handler for Key"+event.getKey());
				}
			}
		}
	}
}
