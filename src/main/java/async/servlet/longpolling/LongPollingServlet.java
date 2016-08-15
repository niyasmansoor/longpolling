
package async.servlet.longpolling;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

@WebServlet(urlPatterns = {"/poll"}, asyncSupported = true, loadOnStartup = 1)
public class LongPollingServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final int STATUS_CODE = 202;
	private final long TIME_OUT_INTERVAL = 300000;
	private final String PRAGMA = "Pragma";
	private final String NO_CACHE = "no-cache";
	private final String ENCODING = "UTF-8";
	
    @EJB(name="notifierEJB")
    private LongPollingNotifier lpNotifier;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType(MediaType.TEXT_PLAIN);
        res.setStatus(STATUS_CODE);
        res.setHeader(PRAGMA, NO_CACHE);
        res.setCharacterEncoding(ENCODING);
        res.flushBuffer();

        final AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(TIME_OUT_INTERVAL);

        lpNotifier.addAsyncContext(asyncContext);
    }
}
