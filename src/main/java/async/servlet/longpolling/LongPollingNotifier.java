
package async.servlet.longpolling;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletOutputStream;

@Singleton
public class LongPollingNotifier {

    private final Queue<AsyncContext> clients = new ConcurrentLinkedQueue<AsyncContext>();

    public synchronized void notifyClients(@Observes String msg) {
        for (AsyncContext ac : clients) {
            try {
                final ServletOutputStream os = ac.getResponse().getOutputStream();
                os.println(msg);
                ac.complete();
            } catch (IOException ex) {
              
            } finally {
                clients.remove(ac);
            }
        }
    }

    public synchronized void addAsyncContext(final AsyncContext ac) {
        ac.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                clients.remove(ac);
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                clients.remove(ac);
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                clients.remove(ac);
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
            }
        });
        clients.add(ac);
    }
}
