package de.nightlife.restservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"de.nightlife"})
public class NightlifeRestserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NightlifeRestserviceApplication.class, args);
	}

}
