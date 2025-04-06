# Overseer

Overseer is a study project developed in Java to explore microservices concepts. The application functions as a REST service to register and emit error or crash logs, enabling the monitoring of other applications.

## Technologies

- **Java**
- **Spring Boot**
- **Maven**
- **JUnit**
- **Docker**

## Goals

- [x] Build a functional in-memory service
- [x] Implement tests
- [x] Set up CI/CD with GitHub Actions
- [ ] Expand controller endpoints arguments: Add support for additional query parameters and request body fields to enhance the flexibility and functionality of the controller endpoints.
- [ ] Create a Docker container for the application: Set up a Dockerfile and necessary configurations to containerize the service.
- [x] Implement database persistence
- [ ] Deploy the service

## Installation and Execution

### Prerequisites

Make sure you have installed:

- [JDK X](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)

### Clone the repository

```sh
git clone https://github.com/your-username/overseer.git
cd overseer
```

### Configure Environment Variables

Create a .env file in the root directory of the project with the following content:

```
DB_HOST=localhost
DB_PORT=3306
DB_USERNAME=root
DB_PASSWORD=your_password
DB_NAME=your_database_name
```

Note: these settings are for a MySQL database.
Make sure you have a MySQL instance running and a database named as per DB_NAME created before starting the application.

### Build the project

```sh
mvn clean install
```

### Run the service

```sh
mvn spring-boot:run
```

The service will be available at `http://localhost:8080`.

## Endpoints

| Method | Endpoint     | Description                                                                                                                              |
| ------ | ------------ | ---------------------------------------------------------------------------------------------------------------------------------------- |
| POST   | `/logs`      | Registers a new error log. Example request body: `{ "message": "Error message", "level": "ERROR", "timestamp": "2023-01-01T12:00:00Z" }` |
| GET    | `/logs`      | Retrieves all registered logs                                                                                                            |
| GET    | `/logs/{id}` | Retrieves a specific log                                                                                                                 |

## Testing

To run tests:

```sh
mvn test
```

## Contribution

If you would like to contribute, feel free to open issues or submit pull requests.

---
