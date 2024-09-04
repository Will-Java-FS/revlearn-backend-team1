package com.revlearn.team1;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Rev Learn",
				version = "1.0",
				description = "API documentation for Team 1: Project 2",
//				contact = @Contact(name = "API Support", email = "support@example.com"),
				license = @License(name = "Apache 2.0", url = "http://springdoc.org")
		),
		servers = {
				@Server(url = "http://api.revaturelearn.com", description = "Production Server"),
				@Server(url = "http://localhost:8080", description = "Local Server")
		}
)
public class Team1Application {

	public static void main(String[] args) {
		SpringApplication.run(Team1Application.class, args);
	}

}
