package br.com.gouvea.config;

import java.net.InetSocketAddress;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;

@Configuration
public class SpringLoggingAutoConfiguration {

	private static final String LOGSTASH_APPENDER_NAME = "LOGSTASH";
	
	private String host;

	private int port;

	private int queue;

	public SpringLoggingAutoConfiguration(@Value("${app.logstash.host}") String host,
			@Value("${app.logstash.port}") int port, @Value("${app.logstash.queue}") int queue,
			@Value("${app.logstash.enabled}") boolean enabled) {
		this.host = host;
		this.port = port;
		this.queue = queue;
		if (enabled) {
			logstashAppender();
		}
	}

	private void logstashAppender() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		LogstashTcpSocketAppender logstashTcpSocketAppender = new LogstashTcpSocketAppender();
		logstashTcpSocketAppender.setName(LOGSTASH_APPENDER_NAME);
		logstashTcpSocketAppender.setContext(loggerContext);
		logstashTcpSocketAppender.addDestinations(new InetSocketAddress(host, port));
		logstashTcpSocketAppender.setQueueSize(queue);
		LogstashEncoder encoder = new LogstashEncoder();
		encoder.setContext(loggerContext);
		encoder.setIncludeContext(true);
		encoder.setCustomFields("{\"appname\":\"batch-integrator\"}");
		encoder.start();
		logstashTcpSocketAppender.setEncoder(encoder);
		logstashTcpSocketAppender.start();
		loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(logstashTcpSocketAppender);
	}

}
