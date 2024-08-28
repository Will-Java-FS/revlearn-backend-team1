# revlearn-backend-team1

Backend Repo for revlearn - team 1

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

### Development Build

Create an .env file in your backend root directory to assign your database URL, username, and password values to the
application's variables. Mine looks like this:

```
SPRING_DATASOURCE_USERNAME=p2team1_user
SPRING_DATASOURCE_PASSWORD=password
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/p2team1
```

Load this .env file into your run configuration's environment variables setting.
![Run Configurations Location](./docs/images/IntelliJIDEAnnotated.png)
![Environment Variables setting](./docs/images/RunConfigsAnnotated.png)

### Production Build

Create an .env file in your backend root directory to assign your database URL, username, and password values to the
application's variables. Mine looks like this:

```
POSTGRES_DB=revlearn
POSTGRES_USER=admin
POSTGRES_PASSWORD=1234

SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/${POSTGRES_DB}
```   

Download docker and run 'docker compose up --build' in your root directory which should auto-build your project with the
variables in your .env    
