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
- [ ] Implement tests  
- [ ] Set up CI/CD with GitHub Actions
- [ ] Expand controller endpoints arguments
- [ ] Containerize the application with Docker  
- [ ] Implement database persistence  
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

### Build the project  

```sh
mvn clean install  
```

### Run the service  

```sh
mvn spring-boot:run  
```

The service will be available at `http://localhost:4040`.  

## Endpoints  

| Method | Endpoint     | Description |
|--------|-------------|-------------|
| POST   | `/logs`     | Registers a new error log |
| GET    | `/logs`     | Retrieves all registered logs |
| GET    | `/logs/{id}` | Retrieves a specific log |

## Testing  

To run tests:  

```sh
mvn test  
```

## Contribution  

If you would like to contribute, feel free to open issues or submit pull requests.  

---

