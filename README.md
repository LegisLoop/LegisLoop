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

# Backend Setup
### Build and run 
Make sure to fill in any API keys by creating a `.env` file in the root project directory (and don't push them them to the repo :D)
Example:
```bash
NEWSAPI_API_KEY=...
LEGISCAN_API_KEY=...
LEGISLOOP_API_KEY=...
```

Terminal: 
```bash
cd legisloop-backend
mvn clean install
mvn spring-boot:run
```
The project can also be run using most code editors. The api will be available at http://localhost:8080/

Visiting Http://localhost:8080/swatter-ui/index.html will redirect to the swagger page for the API.

### Database Setup
To spin up only the database, run the following in the root project directory: 
```bash
docker-compose -f docker-compose-db.yaml up -d
```
The database needs to be running BEFORE the backend starts otherwise it will throw an error. If there is an authentication error and the database is running, make sure that there isn't another process already running on port 5432. 

To start a shell session inside the db container, run the following: 
```bash
docker exec -it postgres_container psql -U postgres -d legisloop
```

To spin down the container, run the following: 
```bash
docker-compose down -v
```
The -v is important to remove the volume and ensure that there is no persistent data left over for a future run. Currently a way to check for existing data is not implemented, so spring boot will throw an error when it tries to automatically create the tables.

### Indexing the Database 
In order for the search endpoints to work, the indexes must be manually created by making a post request to the `/api/v1/initializeDb/initializeIndex` endpoint. The legisloop api key must be included as `X-API-KEY` in the request headers. If using swagger, the api key can be entered at the top of the page by clicking the 'authorize' button. 

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
The app should be available at http://localhost:3001/.
The backend swagger page will be available at http://localhost:3001/swagger-ui/index.html.

To stop the app do ctrl+c in the terminal or run this command: 
```bash
docker-compose down
```
