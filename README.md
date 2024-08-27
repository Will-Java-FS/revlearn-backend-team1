# revlearn-backend-team1
Backend Repo for revlearn - team 1

## Environment Variables
Create an .env file in your backend root directory to assign your database URL, username, and password values to the application's variables.  Mine looks like this:
```
POSTGRES_DB=revlearn
POSTGRES_USER=admin
POSTGRES_PASSWORD=1234

SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/${POSTGRES_DB}
```

Next, you can either 
A) download docker and run 'docker compose up --build' in your root directory which should auto-build your project with the variables in your .env
or 
B) load this .env file into your run configuration's environment variables setting.
![Run Configurations Location](./docs/images/IntelliJIDEAnnotated.png)
![Environment Variables setting](./docs/images/RunConfigsAnnotated.png)