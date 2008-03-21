package net.lucenews3.model;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.springframework.beans.factory.InitializingBean;

public class Log4JConfigurer implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		Logger logger = Logger.getLogger("net.lucenews3");
		logger.setLevel(Level.ALL);
		Layout layout = new PatternLayout();
		Appender appender = new ConsoleAppender(layout, ConsoleAppender.SYSTEM_ERR);
		logger.addAppender(appender);
	}

}
