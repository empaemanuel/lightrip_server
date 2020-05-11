package org.group72.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ServerApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
