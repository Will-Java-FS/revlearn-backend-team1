# revlearn-backend-team1
Backend Repo for revlearn - team 1

## Database
We recommend the creation of a new database for local development of this application because Spring JPA is set to erase and recreate its database on every run.  If you need to persist data for tests you can change this JPA setting in the application.properties file on this line.
```
spring.jpa.hibernate.ddl-auto=create-drop
```
Instead of "create-drop", you can choose "update".
```
spring.jpa.hibernate.ddl-auto=update
```
See [Spring Boot Database Initialization Documentation](https://docs.spring.io/spring-boot/docs/1.1.0.M1/reference/html/howto-database-initialization.html) for more information.

## Environment Variables

### .env File Creation
1. Create an .env file in your backend root directory with this information:

```
# Assign values to these three variables with information from your local Postgres DB installation:
POSTGRES_DB=p2team1
POSTGRES_USER=p2team1_user
POSTGRES_PASSWORD=password

SPRING_DATASOURCE_USERNAME=${POSTGRES_USER}
SPRING_DATASOURCE_PASSWORD=${POSTGRES_PASSWORD}
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/${POSTGRES_DB}
# Not entirely sure what this does, yet.  Could change.
SPRING_API_URL=http://localhost:8080

DOCKER_DATASOURCE_URL=jdbc:postgresql://db:5432/${POSTGRES_DB}
```
2. Assign values to these three variables with information from your local Postgres DB installation:
* POSTGRES_DB
* POSTGRES_USER
* POSTGRES_PASSWORD     
3. You can now run this backend application in either a development environment like the IntelliJ IDE, or as a Docker stack.
### Development (IntelliJ IDE) Build

Load the .env file into your run configuration's environment variables setting.
![Run Configurations Location](./docs/images/IntelliJIDEAnnotated.png)
![Environment Variables setting](./docs/images/RunConfigsAnnotated.png)
Run the application.
### Production (Docker) Build

1. Download Docker
2. In a terminal, navigate to this project's root directory, and run:
```
docker compose up --build
```

Docker will build the Spring Boot application, and the configured Postgres DB into two images, instantiate containers from the images, and start the containers.
* Note: If you need to directly access the containerized Postgres DB (like through pgadmin4), it is mapped to your host machine's port 5433 (not 5432 like normal because that would conflict with local Postgres installations).
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


## Production Deployment
The general steps are:
1. Build Docker image of application
2. Push Docker image to Docker Repository
3. Pull Docker image to EC2 Instance
4. Initialize Docker container from Docker image with production environment variables

### Dockerize application
Navigate to the project's root directory and run the terminal command: 
```
docker build -t revlearn:latest .
```
### Push image to Docker repo
You need to set up an account on a Docker repository.  [Docker Hub](https://hub.docker.com/) is a popular choice.  Create a personal repository (public is easier), and use its name to tag the Docker image:
```
docker tag revlearn:latest <myDockerRepoName>/<revlearn>:latest
```
Log into your repository account in terminal:
```
docker login
```
Push the image to your repository:
```
docker push <myDockerRepoName>/<revlearn>:latest
```
Verify your push succeeded via a web browser visit to physically see your Docker repository's contents, or pull it in the terminal:
```
docker pull <myDockerRepoName>/<revlearn>:latest
```
### Pull to EC2 Instance
Secure Shell into your AWS EC2 instance.
```
ssh -i ~/.ssh/myPemKeyFile.pem ec2-user@<EC2-IP-Address>
```
