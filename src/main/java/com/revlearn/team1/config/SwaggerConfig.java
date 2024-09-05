package com.revlearn.team1.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "RevLearn",
                version = "1.0",
                description = """
                        API documentation for Team 1's capstone project (project 2) in Revature's Full-Stack Java/React SWE Training Program.
                                                
                        The RevLearn project aims to provide a platform for students and educators to engage in learning and teaching. The platform's core functional scope includes features such as student and educator accounts, courses and programs, discussion forums, progress tracking, and a payment gateway. The project also includes institutional accounts, which allow institutions to create and manage courses and programs, monitor student progress, and receive payments. The project's completion will be demonstrated through a cloud-hosted working version, technical presentation, and associated diagrams. \s
                                                
                        Date: 2024/09/04

                        Instructor(s): William Terry, Kelsey Morrison
                        """,
                contact = @Contact(name = "API Support", email = "notARealEmail@notReal.com"),
                license = @License(name = "Apache 2.0", url = "http://springdoc.org")
        ),
        servers = {
                //I think this just made things needlessly complicated, so I'm commenting it out for now
//                @Server(url = "http://api.revaturelearn.com", description = "Production Server"),
//                @Server(url = "http://localhost:8080", description = "Local Server")
        }
)
public class SwaggerConfig {
}
