## Project Documentation

## Table of Contents

*   [Project Overview](#project-overview)
*   [Technologies Used](#technologies-used)
*   [Getting Started](#getting-started)
    *   [Prerequisites](#prerequisites)
    *   [Installation](#installation)
*   [Configuration](#configuration)
*   [Usage](#usage)
    *   [Running the Application](#running-the-application)
    *   [Swagger](#swagger)
    *   [Accessing H2 Database Console](#accessing-h2-database-console)
    *   [Testing](#testing)
*   [Security](#security)
*   [Endpoints](#endpoints)
*   [Sample Requests](#sample-requests)

## Project Overview

**wagering-transactionmanager-api**

The Transaction Management API is a Spring Boot-based RESTful web service designed to manage transactions between customers and products. It provides various features for handling transactions, including creating transactions, retrieving transaction reports, and performing basic authentication.

#### Technologies Used

*   **Java** The project is written in Java, making use of its robust features for building a backend service.
*   **Spring Boot:** Spring Boot is used to create the RESTful web service, simplify configuration, and handle various aspects of the application.
*   **Spring Security:** Spring Security is employed for basic authentication and authorization of API endpoints.
*   **Spring Data JPA:** It is used for data persistence and interacting with the H2 database.
*   **H2 Database:** H2 is an in-memory database used for storing customer, product, and transaction data.
*   **Log4j**: Log4j is utilized for logging important events and information within the application.
*   **BCrypt**: BCrypt is used to securely encode passwords for authentication.
*   **Swagger**: Swagger is integrated to provide interactive API documentation.
*   **JUnit and Mockito:** These frameworks are used for testing the application's functionality.
*   **SOAPUI**: SOAPUI is employed for testing API endpoints.

#### Getting Started

TBD

#### Prerequisites

*   Java Development Kit (JDK) 8: Ensure you have Java 11 installed on your system.
*   Maven: You should have Maven installed to manage project dependencies.
*   SOAPUI: SOAPUI is required for testing API endpoints.
*   IDE: Use an Integrated Development Environment (IDE) like IntelliJ IDEA or Eclipse for development.
*   Postman (Optional): Postman can be used for manually testing the API

#### Installation

*   Clone the project repository from GitHub URL to your local machine.
*   Open the project in your preferred IDE.
*   Build the project using Maven**: mvn clean install**.
*   Start the application by running the main class TransactionManagementApiApplication or using **mvn spring-boot:run**

#### Configuration

##### Database Configuration

The project uses an H2 in-memory database for development and testing. Configuration details are provided in application-test.properties.  
Configuration details are provided in **application.properties** and test configuration at **application-test.properties**.

##### Logging Configuration

Log4j is configured to log events and information to the console. The configuration is available in log4j.properties.

#### Usage

TBD

#### Running the Application

Run the application from your IDE or using Maven: mvn spring-boot:run. The API will be accessible at **http://localhost:8086.** Data is preloaded into tables **Customer** and **Product** the db from **data.sql** and uses **schema.sql**

#### Accessing H2 Database Console

The project uses an H2 in-memory database for development and testing It can be accesses at **http://localhost:8086/h2-console**.

#### Swagger

On startup, use the following to access swagger and view endpoints and documentation  
http://localhost:8086/swagger-ui/index.html?urls.primaryName=public-api#/

#### Logging configuration

Log4j is configured to log events and information to the console. The configuration is available in log4j.properties.

#### Testing

SOAPUI is used for testing API endpoints. Import the provided SOAPUI project provided in the “artifacts folder” and execute test cases.

#### Security

Basic authentication is implemented in the application. Two user roles are defined:  
ADMIN and USER. Authentication and Authorization is defined in the SecurityConfig class

#### Endpoints

*   **POST /transactions/json**: Create a transaction from JSON data.
*   **GET /transactions/report/total-cost-by-customer/{customerId}**: Get the total cost of transactions for a specific customer.
*   **GET /transactions/report/total-cost-by-product/{productCode}**: Get the total cost of transactions for a specific product.
*   **GET /transactions/report/transactions-to-australia-count**: Get the count of transactions sold to customers from Australia.
*   **GET /transactions/report/total-cost-per-customer**: Get the total cost of transactions for each customer.
*   **GET /transactions/report/total-cost-per-product**: Get the total cost of transactions for each product.
*   **GET /transactions/report/transactions-in-australia/{customerId}**: Get transactions in Australia for a specific customer.
*   **GET /transactions/allTransactions**: Get all transactions with pagination.

#### Sample Requests

#### **VALID Requests**

##### _Scenario 1 : Inserts into transaction table_

```plaintext


{
  "customerId": 10002,
  "productCode": "PRODUCT_005",
  "transactionTime": "2024-09-02T21:15:00",
  "quantity": 2
}

{
  "customerId": 10004,
  "productCode": "PRODUCT_001",
  "transactionTime": "2024-09-02T21:15:00",
  "quantity": 2
}
{
  "customerId": 10005,
  "productCode": "PRODUCT_001",
  "transactionTime": "2024-09-02T21:15:00",
  "quantity": 2
}
{
  "customerId": 10002,
  "productCode": "PRODUCT_003",
  "transactionTime": "2024-09-02T21:15:00",
  "quantity": 2
}

(RECORD FOR AUSTRALIA)
{
  "customerId": 10001,
  "productCode": "PRODUCT_003",
  "transactionTime": "2023-09-03T21:15:00",
  "quantity": 2
}
```

##### _Scenario 2 : Inserts into transaction table_

```plaintext


{
  "customerId": 10002,
  "productCode": "PRODUCT_005",
  "transactionTime": "2024-09-02T21:15:00",
  "quantity": 2
}

{
  "customerId": 10004,
  "productCode": "PRODUCT_001",
  "transactionTime": "2024-09-02T21:15:00",
  "quantity": 2
}
{
  "customerId": 10005,
  "productCode": "PRODUCT_001",
  "transactionTime": "2024-09-02T21:15:00",
  "quantity": 2
}
{
  "customerId": 10002,
  "productCode": "PRODUCT_003",
  "transactionTime": "2024-09-02T21:15:00",
  "quantity": 2
}
```

##### _Scenario 2 : Get the count of transactions sold to customers from Australia._

```plaintext
GET http://localhost:8084/transactions/report/transactions-to-australia-count
```

##### _Scenario 2 : Get the total cost of transactions for a specific product._

```plaintext
GET http://localhost:8084/transactions/report/total-cost-by-product/PRODUCT_003
```

##### _Scenario 2 : Get the total cost of transactions for a specific customer._

```plaintext
GET http://localhost:8084/transactions/report/total-cost-by-customer/10001
```

##### _Scenario 2 : Get the list of total cost of transactions for each product._

```plaintext
GET http://localhost:8084/transactions/report/total-cost-per-product
```

##### _Scenario 2 :Get the list of total cost of transactions for each customer._

```plaintext
GET http://localhost:8084/transactions/report/total-cost-per-customer
```

#### **INVALID Requests**

##### _Scenario 2 : PreDated record_

```plaintext
{
  "customerId": 10005,
  "productCode": "PRODUCT_003",
  "transactionTime": "2023-09-01T21:15:00",
  "quantity": 2
}
```

##### _Scenario 2 : transaction cost greater than 5000_

```plaintext
{
  "customerId": 10005,
  "productCode": "PRODUCT_005",
  "transactionTime": "2023-09-01T21:15:00",
  "quantity": 13
}
```

##### _Scenario 2 : Inactive product_

```plaintext
{
 "customerId": 10001,
 "productCode": "PRODUCT_004",
 "transactionTime": "2024-12-21T21:15:00",
 "quantity": 2
}
```

##### _Scenario 2 : INVALID Product_

```plaintext
{
 "customerId": 10001,
 "productCode": "PRODUCT_009",
 "transactionTime": "2024-12-21T21:15:00",
 "quantity": 2
}
```

##### _Scenario 2 : INVALID  Customer_

```plaintext
{
 "customerId": 10000,
 "productCode": "PRODUCT_004",
 "transactionTime": "2024-12-21T21:15:00",
 "quantity": 2
}
```

##### _Scenario 2 : INVALID  endpoint_

```plaintext
GET http://localhost:8084/transactions/report/total-cost-per-customer
```

---

---

## Documentation

This documentation provides an overview of the Transaction Management API, including its main classes, package structure, and endpoints.

#### Project Package Structure

The Transaction Management API project follows a typical Spring Boot project structure. Here is an overview of the main packages and their contents:

*   **com.tabcorp.transactionmanagementapi**(Root package)
    *   **WageringTransactionmangementApiApplication.java**: The main application class responsible for starting the Spring Boot application.
*   **com.tabcorp.transactionmanagementapi.controller**
    *   **TransactionController.java**: Defines RESTful endpoints for managing transactions, including creating transactions from JSON, retrieving transaction reports, and handling exceptions.
*   **com.tabcorp.transactionmanagementapi.dto**
*   **com.tabcorp.transactionmanagementapi.errorhandling**
    *   **CustomerNotFoundException.java**: Custom exception class for handling customer not found errors.
    *   **ErrorResponse.java**: Represents an error response structure for exception handling.
    *   **IllegalInputException.java**: Custom exception class for handling illegal input errors.
    *   **ProductNotFoundException.java**: Custom exception class for handling product not found errors.
    *   **TransactionManagementApiExceptionHandler.java**: Global exception handler for handling custom exceptions and generating error responses
    *   **TransactionValidationException**: Custom exception used for handling transaction validation errors( invalid input or business rule violations.)
    *   **JsonDeserializationException**: Custom exception class used for handling errors related to JSON deserialization while parsing JSON data.
*   **com.tabcorp.transactionmanagementapi.models**
    *   **Customer.java**: Represents a customer entity with properties such as customer ID, first name, last name, age, and location.
    *   **Product.java**: Represents a product entity with properties including product code, cost, and status.
    *   **Transaction.java**: Represents a transaction entity with properties like transaction ID, transaction time, customer ID, quantity, and product code. It also includes a method to calculate the transaction cost.
*   **com.tabcorp.transactionmanagementapi.repository**
    *   **CustomerRepository.java**: Spring Data JPA repository interface for managing customer data.
    *   **ProductRepository.java**: Spring Data JPA repository interface for managing product data.
    *   **TransactionRepository.java**: Spring Data JPA repository interface for managing transaction data.
*   **com.tabcorp.transactionmanagementapi.resource**
    *   **TransactionController.java**: Contains the RESTful endpoints for managing transactions, including creating transactions from JSON, retrieving transaction reports, and handling exceptions.
*   **com.tabcorp.transactionmanagementapi.security**
    *   **SecurityConfig.java**: Configuration class for Spring Security, including role-based access control.
*   **com.tabcorp.transactionmanagementapi.service**
    *   **TransactionService.java**: Service class responsible for handling business logic related to transactions, including adding transactions, calculating totals, and filtering transactions.

**Main Classes**

**WageringTransactionmangementApiApplication.java**

The **WageringTransactionmangementApiApplication** class is the main entry point for the Spring Boot application. It initializes and starts the application.

**TransactionController.java**

The **TransactionController** class defines RESTful endpoints for managing transactions. It includes methods for creating transactions from JSON, retrieving transaction reports, and handling exceptions related to transactions.

**TransactionRequest.java**

The **TransactionRequest** class is a data transfer object (DTO) that represents the structure of incoming transaction requests. It includes properties such as transaction time, customer ID, product code, and quantity.

**Exception Handling Classes**

*   **CustomerNotFoundException.java**: Custom exception class for handling customer not found errors.
*   **ErrorResponse.java**: Represents the structure of error responses generated during exception handling.
*   **IllegalInputException.java**: Custom exception class for handling illegal input errors.
*   **ProductNotFoundException.java**: Custom exception class for handling product not found errors.
*   **TransactionManagementApiExceptionHandler.java**: Global exception handler for handling custom exceptions and generating error responses.
*   **TransactionValidationException**: Custom exception used for handling transaction validation errors( invalid input or business rule violations.)
*   **JsonDeserializationException**: Custom exception class used for handling errors related to JSON deserialization while parsing JSON data.

**Entity Classes**

*   **Customer.java**: Represents a customer entity with properties such as customer ID, first name, last name, age, and location.
*   **Product.java**: Represents a product entity with properties including product code, cost, and status.
*   **Transaction.java**: Represents a transaction entity with properties like transaction ID, transaction time, customer ID, quantity, and product code. It also includes a method to calculate the transaction cost.

**Repository Interfaces**

*   **CustomerRepository.java**: Spring Data JPA repository interface for managing customer data.
*   **ProductRepository.java**: Spring Data JPA repository interface for managing product data.
*   **TransactionRepository.java**: Spring Data JPA repository interface for managing transaction data.

**Service Class**

*   **TransactionService.java**: The service class responsible for handling business logic related to transactions. It includes methods for adding transactions, calculating totals, and filtering transactions.

**Endpoints**

The **TransactionController** defines the a lot of RESTful endpoints documented as above:

These endpoints allow you to perform various operations related to transactions and retrieve reports on transaction data.