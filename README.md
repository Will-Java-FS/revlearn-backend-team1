# revlearn-backend-team1

Backend Repo for revlearn - team 1

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
