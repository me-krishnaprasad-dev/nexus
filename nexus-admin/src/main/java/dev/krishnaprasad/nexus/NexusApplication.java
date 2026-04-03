package dev.krishnaprasad.nexus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class will be the main entry point for the Nexus application. It will be responsible for starting the Spring Boot application and initializing any necessary components or configurations.
 * The @SpringBootApplication annotation is a convenience annotation that adds all of the following:
 * - @Configuration: Indicates that the class can be used by the Spring IoC container as a source of bean definitions.
 * - @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
 * - @ComponentScan: Tells Spring to look for other components, configurations, and services in the specified package, allowing it to find the controllers and other components that we will define in our application.
 */
@SpringBootApplication
public class NexusApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexusApplication.class, args);
	}

}
