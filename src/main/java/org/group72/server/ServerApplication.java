package org.group72.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
@EntityScan(basePackages = "org.group72.server")
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ServerApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
