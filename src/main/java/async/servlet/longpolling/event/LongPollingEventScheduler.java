
package async.servlet.longpolling.event;

import java.util.concurrent.atomic.AtomicInteger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@Singleton
public class LongPollingEventScheduler {

	private static AtomicInteger counter  = new AtomicInteger();
	
    @Inject
    private Event<String> msgEvent;

    @Schedule(hour = "*", minute = "*", second = "*/10")
    public synchronized void fireMessages() {  
    	
    	int cntr = counter.incrementAndGet();
    	String msg = "Message from longpolling server received <font color = 'red'>"+cntr+"</font> times at "+new java.util.Date();
       	msgEvent.fire(msg);
       	System.out.println("Message fired "+cntr+" times");   
       	
    }
}
