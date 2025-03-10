package eventLoopTest1;

//import eventLoopTest1.EventLoop;

public class SimpleEventLoop {
	public static void main(String[] args) {
		EventLoop eventLoop = new EventLoop();
		new Thread(()->{
			/*
			 * A first thread that dispatches
			 * events every second to the
			 * event loop
			 * */
			for(int n=0;n<6;n++) {
				delay(1000);
				eventLoop.dispatch(new Event("tick",n));
			}
			eventLoop.dispatch(new Event("stop",null));
		}).start();
		
		new Thread(()->{
			/*
			 * A second thread that dispatches two
			 * events at 2500 ms and 3300 ms
			 * */
			delay(2500);
			eventLoop.dispatch(new Event("hello","beautiful world"));
			delay(800);
			eventLoop.dispatch(new Event("hello","beautiful universe"));
		}
				).start();
		eventLoop.dispatch(new Event("hello","world")); // EVents Dispatched From Main thread
		// Foo does not have a handler defined m hence will through error
		eventLoop.dispatch(new Event("foo","bar"));
		// Event handlers defined as Java lambda function
		eventLoop.on("hello", s -> System.out.println("hello " + s))
		.on("tick", n -> System.out.println("tick #" + n))
		.on("stop", v -> eventLoop.stop())
		.run();
		
		System.out.println("Bye!");
	}
	
	private static void delay(long millis){
		try {
			Thread.sleep(millis);
		}catch(InterruptedException e){
			throw new RuntimeException(e);
		}
	}

}
