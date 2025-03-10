package eventLoopTest1;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;


public final class EventLoop {
	private final ConcurrentLinkedDeque<Event> events = new ConcurrentLinkedDeque<>();
	private final ConcurrentHashMap<String,Consumer<Object>> handlers = new ConcurrentHashMap<>();
	
	// Handlers are stored in a map where each key has a handler.
	// Consumer is a Functional Interface , that accepts a value but does not return it
	
	public EventLoop on(String key, Consumer<Object> handler) {
		handlers.put(key, handler);
		return this;
	}
	// Dispatching is pushing events to a queue.
	public void dispatch(Event event) {events.add(event);}
	public void stop() {Thread.currentThread().interrupt();}
	
	
	
	//Keeps runing untill no Events or Thread is Interuppted
	/*
	 * 
	 * Step-by-Step Execution
1- while (!(events.isEmpty() && Thread.interrupted())) → Keep Running Until No Events or Thread is Interrupted
The loop runs until both conditions are true:
    events.isEmpty() → The event queue is empty.
    Thread.interrupted() → The current thread is interrupted (via stop()).
⚠️ Potential Issue:
    The check events.isEmpty() && Thread.interrupted() is not effective. If the thread is interrupted before the queue is empty, the loop still continues.
    A better condition would be while (!Thread.currentThread().isInterrupted()) to properly check the thread state.
2- if (!events.isEmpty()) → Process Events If Available
The event queue is checked to see if there are any pending events.
3- Event event = events.pop(); → Retrieve and Remove Event
The next event is taken from the ConcurrentLinkedDeque event queue.
4- if (handlers.containsKey(event.getKey())) → Check If a Handler Exists
It checks if an event handler is registered for the event type (event.getKey()).
5- handlers.get(event.getKey()).accept(event.getData()); → Execute the Handler
If a handler exists, it is retrieved and executed using .accept(event.getData()).
This calls the function registered via .on().
6- System.err.println("No Handler for Key" + event.getKey()); → Handle Missing Events
If no handler is found for the event type, an error message is printed.
	 * 
	 * */
	
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
