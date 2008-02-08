package net.lucenews3.lucene.support;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class ServerLauncher implements Runnable {
	
	public static void main(String... arguments) throws Exception {
		ServerLauncher launcher = new ServerLauncher();
		launcher.run();
	}
	
	public void run() {
		try {
			Server server = new Server();
			
			Connector connector = new SelectChannelConnector();
			connector.setPort(2008);
			connector.setHost("127.0.0.1");
			server.addConnector(connector);
			
			WebAppContext context = new WebAppContext();
			context.setContextPath("/");
			context.setWar("war/EB-INF/web.xml");
			server.addHandler(context);
			
			server.setStopAtShutdown(true);
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
