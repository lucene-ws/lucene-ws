package net.lucenews3.lucene.support;

import net.lucenews.LuceneWebService;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class ServerLauncher implements Runnable {
	
	public static void main(String... arguments) throws Exception {
		ServerLauncher launcher = new ServerLauncher();
		launcher.run();
	}
	
	public void run() {
		try {
			Server server = new Server();
			
			Connector connector = new SelectChannelConnector();
			connector.setPort(2009);
			connector.setHost("127.0.0.1");
			server.addConnector(connector);
			
			Context root = new Context(server, "/", Context.SESSIONS);
			ServletHolder holder = new ServletHolder(new LuceneWebService());
			holder.setInitParameter("directory", "/home/adam/Desktop");
			holder.setInitParameter("debug", "1");
			root.addServlet(holder, "/*");
			
			server.setStopAtShutdown(true);
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
