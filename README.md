# Prerequisites

Before running the application, you need to install the following tools:

## Code Editor
Intellij and Eclipse are best for Java applications.

### Eclipse
The following software should be installed to accommodate the environment.
- **Lombok plugin**: For Eclipse to understand Lombok annotations. *Help --> Add New Software --> Add the link and select the tool* https://projectlombok.org/p2
- **Spring tools**: For running the SpringBoot backend in Eclipse. *Help --> Eclipse Marketplace --> Search 'Spring Tools 4'

## Frontend (React)
- **Node.js** (LTS version): [Download and install Node.js](https://nodejs.org/)
  - If using nvm, launch command prompt as Admin
    ```bash
    nvm install lts
    nvm use lts
    ```
## Backend (Spring Boot)
- **JDK 21**: Can be done within intellij
- **Maven**: [Download and install Maven](https://maven.apache.org/install.html)
- **DATABASE**: To be determined 

# Backend Setup
### Build and run 
Make sure to fill in any API keys in the respective application.properties files (and don't push them them to the repo :D)
Terminal: 
```bash
cd legisloop-backend
mvn clean install
mvn spring-boot:run
```
The project can also be run using most code editors. The api will be available at http://localhost:8080/

Visiting Http://localhost:8080/swatter-ui/index.html will redirect 

# Frontent Setup 
### Install Dependencies
```bash
cd legisloop-frontend
npm install
```
## Build and Run
Terminal: 
```bash
cd legisloop-frontend
npm start
```

The app should be available at http://localhost:3000/.

# Running with Docker 

Make sure to have docker desktop installed. Then run the following commands. 

Terminal: 
```bash
cd LegisLoop
docker-compose up
```

If you have made changes to the code, you will need to run this instead: 
```bash
docker-compose up --build 
```
The app should be available at http://localhost:3000/.

To stop the app do ctrl+c in the terminal or run this command: 
```bash
docker-compose down
```
