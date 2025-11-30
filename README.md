## Features about this API
* Complete Spring Boot API with Java 21
* Secure JWT Authentication
* Card CRUD with end-to-end encryption
* Batch TXT File Upload
* Validations and error handling
* Logging of all requests
* Swagger/OpenAPI interactive documentation
* Unit Tests
* MySQL integration
* Custom exception handling

## Prerequisites

* Docker and Docker Compose installed on your machine.
* Java 21 installed on your machine (for building the project).
* Maven installed on your machine (for building the project).

## 1. How to run the project
1. Clone this repository to your local machine.
2. Navigate to the project directory.
3. Build the project using Maven:
> mvn clean install

### 2. Mysql Configuration (docker and first user import)

### 3. Execute the docker-compose.yml file (/docker-coompose/docker-compose.yml)
> docker-compose up -d

### 4. Connect to Mysql container
> name: mysql-visa-challenge<br/>
> database: challenge_db<br/>
> user: administrator<br/>
> password: v1s4Ch4ll3ng#r<br/>
> port: 3306

### 5. Run the SQL script to create the first admin user (/data-sql/import.sql)

### 6. Run the Spring Boot Application
> mvn spring-boot:run

### 7. Access the Swagger UI for API documentation and testing:

### 7.1 Authentication

>POST /api/auth/login - JTW Login

>POST /api/auth/validate - Validate active token

### 7.2 Card Management

>POST /api/cards/single - Add a single card

>GET /api/cards/search?cardNumber=... - Search by card number

>GET /api/cards/exists?cardNumber=... - Check if card exists

### 7.3 Batch File Upload

>POST /api/files/upload?validateOnly=false - Upload a TXT file with multiple cards
 
### 7.4 Log API Requests

>GET /api/logs/all - Get all API logs order by date desc defaut

### 7.5 Health Check Endpoints

>GET /api/health - Status OK

>GET /api/health/ready - Readiness

>GET /api/health/live - Liveness


### 8. Running Unit Tests
To run the unit tests for the project, use the following Maven command:
> mvn test
> This will execute all the unit tests and display the results in the console.

### Important Notes
* Ensure that the MySQL container is running before starting the Spring Boot application.
* Use the Swagger UI to explore and test the API endpoints interactively.
* Also, you can test the endpoints using the folder /api-requests with the REST Client extension in your Intellij.
* Modify the database connection settings in the application.properties file if necessary.

