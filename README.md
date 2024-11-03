## Prerequisites

Before running the application, you need to install the following tools:

### Code Editor
Intellij and Eclipse are best for Java applications. 

### Frontend (Angular)
- **Node.js** (LTS version): [Download and install Node.js](https://nodejs.org/)
- **Angular CLI**: Install globally using npm:
  ```bash
  npm install -g @angular/cli
  ```
### Backend (Spring Boot)
- **JDK 21**: Can be done within intellij
- **Maven**: [Download and install Maven](https://maven.apache.org/install.html)
- **DATABASE**: To be determined 

## Backend Setup
### Check Database Config (TODO: DATABASE LOL)
### Build and run 
Terminal: 
```bash
cd legisloop
mvn clean install
mvn spring-boot:run
```
The project can also be run using most code editors. 

## Frontent Setup 
### Install Dependencies
```bash
cd legisloop-frontend
npm install
```
### Build and Run
Terminal: 
```bash
cd legisloop-frontend
npm start
```

The app should be available at http://localhost:3000/.

## Running with Docker 

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
