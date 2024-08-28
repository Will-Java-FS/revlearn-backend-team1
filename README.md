# revlearn-backend-team1

Backend Repo for revlearn - team 1

## Database
We recommend the creation of a new database for this application because Spring JPA is set to erase and recreate its database on every run.  If you need to persist data for tests you can change this setting in the application.properties file on this line.
```
spring.jpa.hibernate.ddl-auto=create-drop
```
Instead of "create-drop", you can choose "update".
```
spring.jpa.hibernate.ddl-auto=update
```
See [Spring Boot Database Initialization Documentation](https://docs.spring.io/spring-boot/docs/1.1.0.M1/reference/html/howto-database-initialization.html) for more information.
## API Documentation

### Swagger

1. Turn on (run) your application.
2. Copy and paste this whole URL into your web browser.

```
http://localhost:8080/swagger-ui.html
```

3. You should see a page like this: ![Swagger page screenshot](./docs/images/Swagger.png)
4. Each section is a collection of routes.
5. Each route is exactly what you append to our application's base URL: 
* Development: http://localhost:8080/ 
* Production: TBD
6. Expand any route to view example JSON formats for requests and
   responses. ![Swagger Expanded Route](./docs/images/SwaggerExpandedRoute.png)

## Environment Variables

### .env File Creation
1. Create an .env file in your backend root directory.
2. Assign your Database Name, Database Username, and Database Password values to the
respective variables. Mine looks like this:

```
POSTGRES_DB=p2team1
POSTGRES_USER=p2team1_user
POSTGRES_PASSWORD=password

SPRING_DATASOURCE_USERNAME=${POSTGRES_USER}
SPRING_DATASOURCE_PASSWORD=${POSTGRES_PASSWORD}
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/${POSTGRES_DB}
```
3. This will enable you to run the application in either a development environment like in the IntelliJ IDE, or as a Docker container.
### Development (IntelliJ IDE) Build

Load the .env file into your run configuration's environment variables setting.
![Run Configurations Location](./docs/images/IntelliJIDEAnnotated.png)
![Environment Variables setting](./docs/images/RunConfigsAnnotated.png)
Run the application.
### Production (Docker) Build

1. Download Docker
2. In your root directory, run 
```
docker compose up --build
```
3.  Docker should auto-build your project with the variables in your .env    
