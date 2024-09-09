<div align="center">
<h1>RevLearn: Backend</h1>
Team 1's Backend Repo for Project 2: Rev Learn, a capstone project for <a href="(https://revature.com/">Revature</a>'s Full-Stack Java/React SWE Training Program.
</div>

---
<details>
<summary><strong>Table of Contents</strong></summary>
  <ul>
    <li><a href="#project-description">Project Description</a></li>
    <li>
      <a href="#development-build">Development Build</a>
      <ul>
        <li><a href="#database">Database</a></li>
        <li><a href="#environment-variables">Environment Variables</a></li>
        <li><a href="#intellij-ide-build">IntelliJ IDE Build</a></li>
        <li><a href="#docker-stack-build">Docker Stack Build</a>
          <ul>
            <li><a href="#port-note">Port Note</a></li>
          </ul>
        </li>
      </ul>
    </li>
    <li>
      <a href="#api-documentation">API Documentation</a>
      <ul>
        <li><a href="#production-link">Production Link</a></li>
        <li><a href="#development-link">Development Link</a></li>
        <li><a href="#swagger-tutorial">Swagger Tutorial</a></li>
      </ul>
    </li>
    <li>
      <a href="#stripe-information">Stripe Information</a>
      <ul>
        <li><a href="#request">Request</a></li>
        <li><a href="#response">Response</a></li>
      </ul>
    </li>
    <li>
      <a href="#production-deployment">Production Deployment</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#environment-file">Environment File</a></li>
        <li>
          <a href="#process-outline">Process Outline</a>
          <ul>
            <li><a href="#1-build-docker-image">1. Build Docker Image</a></li>
            <li><a href="#2-push-docker-image-to-docker-registry">2. Push Docker Image to Docker Registry</a></li>
            <li><a href="#3-pull-to-ec2-instance">3. Pull to EC2 Instance</a></li>
            <li><a href="#4-run-docker-container">4. Run Docker Container</a></li>
          </ul>
        </li>
        <li><a href="#reverse-proxy-server">Reverse Proxy Server</a></li>
      </ul>
    </li>
  </ul>
</details>


---

# Project Description

The RevLearn project aims to provide a platform for students and educators to engage in learning and teaching. The platform's core functional scope includes features such as student and educator accounts, courses and programs, discussion forums, progress tracking, and a payment gateway. The project also includes institutional accounts, which allow institutions to create and manage courses and programs, monitor student progress, and receive payments. The project's completion will be demonstrated through a cloud-hosted working version, technical presentation, and associated diagrams.

# Development Build
## Database
We recommend the creation of a new database for this application because Spring JPA is set to erase and recreate its database on every run.  If you need to persist data for tests you can change this JPA setting in the application.properties file on this line.
```
spring.jpa.hibernate.ddl-auto=create-drop
```
Instead of "create-drop", you can choose "update".
```
spring.jpa.hibernate.ddl-auto=update
```
See [Spring Boot Database Initialization Documentation](https://docs.spring.io/spring-boot/docs/1.1.0.M1/reference/html/howto-database-initialization.html) for more information.

## Environment Variables

1. Create an .env file in your backend root directory with this information:

```
POSTGRES_DB=p2team1
POSTGRES_USER=p2team1_user
POSTGRES_PASSWORD=password

SPRING_DATASOURCE_USERNAME=${POSTGRES_USER}
SPRING_DATASOURCE_PASSWORD=${POSTGRES_PASSWORD}
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/${POSTGRES_DB}

# Used by sample data initializer
SPRING_API_URL=http://localhost:8080/api/v1

DOCKER_DATASOURCE_URL=jdbc:postgresql://db:5432/${POSTGRES_DB}

# Stripe
STRIPE_API_KEY=yourStripePrivateAPIKey
CLIENT_URL=http://localhost:5173
```
2. Modify the top three values with respective information from your local Postgres DB installation.
* POSTGRES_DB
* POSTGRES_USER
* POSTGRES_PASSWORD     
3. You can now run this backend application in either:
* an IDE like IntelliJ
* a Docker stack

## IntelliJ IDE Build

Load the .env file into your run configuration's environment variables setting.

![Run Configurations Location](./docs/images/IntelliJIDEAnnotated.png)
![Environment Variables setting](./docs/images/RunConfigsAnnotated.png)
Run the application.
## Docker Stack Build

1. Download Docker
2. In a terminal, navigate to this project's root directory, and run:
```
docker compose up --build
```

Docker will build the Spring Boot application, and the configured PostgreSQL DB into two images, create containers from those images, and start the containers with environment variables in the .env file.
### Port Note
If you need to directly access the containerized PostgreSQL DB (like through pgadmin4), it is mapped to your host machine's port 5433 (not 5432 like normal because that would conflict with local PostgreSQL installations).

[//]: # (* The PostgreSQL DB variables of the environment file must be filled in with something, but it is not important what.)

---

# API Documentation
API documentation is automatically generated and maintained by [Swagger](https://swagger.io/solutions/api-documentation/).  Visit a respective Swagger UI link below to view this project's API documentation.

## Production Link
[Swagger UI](http://api.revaturelearn.com/swagger-ui/index.html)
## Development Link
Run the Spring Boot application locally, and open the below URL in a browser to view the Swagger UI.
```
http://localhost:8080/swagger-ui.html
```
## Swagger Tutorial

1. This is an example of what a Swagger UI looks like.

![Swagger page screenshot](./docs/images/Swagger.png)

2. Each section is a collection of routes, and each route is exactly what you append to the application's base URL:
   * Development: http://localhost:8080/
   * Production: http://api.revaturelearn.com/
   

3. Expand any route to view example JSON formats for requests and
   responses.

![Swagger Expanded Route](./docs/images/SwaggerExpandedRoute.png)

---

# Stripe Information
Later in the .env file there are two variables regarding Stripe. The first one is super easy to get, just create an Account with Stripe and copy your hidden API key to that enviro variable. The other one is just the base url of the frontend. I provided one that is usually the case, but if you guys have it running on a different port then you need to update the enviro variable accordingly.

When doing a payment on stripe it is essential to use 
```
4242 4242 4242 4242
```
for the card info as this is a sample card for testing and succeeding in a payment.
The current routes for a successful payment and a cancel payment are as follows:
```
<baseurl>/checkout-success
```
```
<baseurl>/checkout-cancel
```

The Swagger docs will give you some example request and response bodies that the API is expecting but for convenience here are some examples:

## Request

POST: localhost:8080/api/v1/transaction/checkout

```
{
    "id": 12345,
    "name": "Java Course",
    "description": "Java Full Stack Course",
    "price": 29900,
    "quantity": 1
}
```


## Response
200 OK

```
{
    "message": "Payment Processed!",
    "url": "url To Stripe Hosted checkout page for the given product"
}
```

---

# Production Deployment
## Prerequisites
* A Docker Registry account. [Docker Hub](https://hub.docker.com/) is a popular choice.
* An AWS EC2 instance running (preferably Amazon Linux 2 or Ubuntu).
* Docker installed on your EC2 instance.
* SSH access to your EC2 instance.
    
## Environment File
Create a production environment file with the following information:
```
# AWS PostgreSQL DB Credentials
AWS_POSTGRES_DB=<AWS-DB>
AWS_POSTGRES_USER=<AWS-User>
AWS_POSTGRES_PASSWORD=<AWS-Password>
AWS_POSTGRES_URL=<AWS DB Public (or Private if security correctly configured) Url>

# AWS
SPRING_DATASOURCE_USERNAME=${AWS_POSTGRES_USER}
SPRING_DATASOURCE_PASSWORD=${AWS_POSTGRES_PASSWORD}
SPRING_DATASOURCE_URL=jdbc:postgresql://${AWS_POSTGRES_URL}:5432/${AWS_POSTGRES_DB}

# Used by sample data initializer
SPRING_API_URL=http://localhost:8080/api/v1

# JWTs
SECRET_KEY=<base64-Key>

# Stripe
STRIPE_API_KEY=<yourStripePrivateAPIKey>
CLIENT_URL=http://localhost:5173
```
Replace the respective angle bracketed placeholders with your production database information and a secret key for the JWTs:
* AWS_POSTGRES_DB
* AWS_POSTGRES_USER
* AWS_POSTGRES_PASSWORD
* AWS_POSTGRES_URL
* SECRET_KEY
* STRIPE_API_KEY


Secure copy the environment file to your EC2 instance with your pem key file:
```
scp - i ~/.ssh/<PemKeyFile>.pem <production-env-file> ec2-user@<EC2-IP-Address>:/home/ec2-user
```

## Process Outline
1. Build Docker Image
2. Push Docker Image to Docker Registry
3. Pull Docker Image to EC2 Instance
4. Run Docker Container

### 1. Build Docker Image
Navigate to the project's root directory and run the terminal command: 
```
docker build -t revlearn:latest .
```
Do not forget the period at the end to denote the current directory.
### 2. Push Docker Image to Docker Registry
Create a personal repository on your Docker Registry, and use its name to tag the Docker image:
```
docker tag revlearn:latest <DockerRegistryUsername>/<DockerRegistryRepository>:latest
```
Log into your repository account in terminal:
```
docker login
```
Push the image to your repository:
```
docker push <DockerRegistryUsername>/<DockerRegistryRepository>:latest
```
Verify your push succeeded via a web browser visit to your Docker registry, or pull it in the terminal:
```
docker pull <DockerRegistryUsername>/<DockerRegistryRepository>:latest
```

### 3. Pull to EC2 Instance
Secure Shell into your AWS EC2 instance with your pem key file.
```
ssh -i ~/.ssh/<PemKeyFile>.pem ec2-user@<EC2-IP-Address>
```
Pull the Docker Image:
```
docker pull <DockerRegistryUsername>/<DockerRegistryRepository>:latest
```
### 4. Run Docker Container
Inside the EC2 instance, instantiate the Docker container with the production environment variables:
```
docker run -d -p 8080:8080 --name RevLearnBackend --env-file <production-env-file> <DockerRegistryUsername>/<DockerRegistryRepository>:latest
```
Verify the application works with test requests.

## Reverse Proxy Server
If your AWS settings only have port 80 exposed to HTTP traffic, (which is a recommended practice), then you will need a reverse proxy server like [nginx](https://nginx.org/) to map the standard browser requests (port 80) to the Java Spring Boot application (port 8080).
