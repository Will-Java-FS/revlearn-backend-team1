# revlearn-backend-team1
Backend Repo for revlearn - team 1

## Environment Variables
Create an .env file to assign your database URL, username, and password values to the application's variables.  Mine looks like this:
```
SPRING_DATASOURCE_USERNAME=p2team1_user
SPRING_DATASOURCE_PASSWORD=password
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/p2team1
```
It can go into any directory so long as you know where it is, but most people put it into the root directory (top level).  Next, load this .env file into your run configuration's environment variables setting.
![Run Configurations Location](./docs/images/IntelliJIDEAnnotated.png)
![Environment Variables setting](./docs/images/RunConfigsAnnotated.png)