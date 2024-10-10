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
cd legisloopapp
npm install
```
### Configure Server Endpoint (TODO)
Make sure that the development and production API URLs are correct. `environment.ts` should be `http://localhost:8080`
by default. 
### Build and Run
Terminal: 
```bash
cd legisloopapp
ng serve
```