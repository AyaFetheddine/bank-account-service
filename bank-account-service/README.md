# Bank Account Service

A Spring Boot microservice for managing bank accounts with both REST and GraphQL APIs.

## Overview

This project implements a bank account management system that allows:
- Creating and managing bank accounts
- Linking accounts to customers
- Providing both REST and GraphQL APIs

## Technologies

- **Spring Boot 3.5.5**
- **Spring Data JPA** with H2 database
- **Spring Web** (REST API)
- **Spring GraphQL** (GraphQL API)
- **Spring Data REST** (Auto-generated REST endpoints)
- **Lombok** for reducing boilerplate code

## Quick Start

```bash
# Run the application
mvn spring-boot:run
```

### Access Points
- **Application**: http://localhost:8081
- **GraphQL Playground**: http://localhost:8081/graphiql
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **H2 Database Console**: http://localhost:8081/h2-console

## API Usage

### REST API

**Custom Controller Endpoints:**
```
# Get all accounts
GET /api/bankAccounts

# Create account
POST /api/bankAccounts
{
  "type": "SAVING_ACCOUNT",
  "balance": 5000,
  "currency": "MAD",
  "customerId": 1
}

# Get account by ID
GET /api/bankAccounts/{id}

# Update account
PUT /api/bankAccounts/{id}

# Delete account
DELETE /api/bankAccounts/{id}
```

**Spring Data REST Endpoints (auto-generated):**
```
# Get all accounts
GET /bankAccounts

# Create account
POST /bankAccounts

# Get account by ID
GET /bankAccounts/{id}

# Update account
PUT /bankAccounts/{id}

# Delete account
DELETE /bankAccounts/{id}

# Find accounts by type
GET /bankAccounts/search/findByType?type=CURRENT_ACCOUNT
```

### GraphQL API

```graphql
# Query all accounts
query {
  accountsList {
    id
    type
    balance
    currency
    customer {
      id
      name
    }
  }
}

# Create account
mutation {
  addAccount(bankAccount: {
    type: "SAVING_ACCOUNT"
    balance: 5000
    currency: "MAD"
    customerId: 1
  }) {
    id
    type
    balance
    currency
    customer {
      id
      name
    }
  }
}

# Update account
mutation {
  updateAccount(id: "account-id", bankAccount: {
    type: "CURRENT_ACCOUNT"
    balance: 7500
    currency: "MAD"
    customerId: 2
  }) {
    id
    type
    balance
    currency
  }
}

# Delete account
mutation {
  deleteAccount(id: "account-id")
}
```

## Data Model

```
Customer (1) ←→ (N) BankAccount
```

### Customer
- `id` (Long, Auto-generated)
- `name` (String)
- `bankAccounts` (List<BankAccount>, One-to-Many)

### BankAccount
- `id` (String, UUID)
- `createdAt` (Date)
- `balance` (Double)
- `currency` (String)
- `type` (AccountType: CURRENT_ACCOUNT, SAVING_ACCOUNT)
- `customer` (Customer, Many-to-One)

## Architecture Principles

This project follows several important software engineering principles:

### SOLID Principles
- **Single Responsibility**: Each class has one reason to change
  - `AccountService` - Only handles account business logic
  - `AccountMapper` - Only converts between entities and DTOs
  - `BankAccountGraphQLController` - Only handles GraphQL requests

- **Open/Closed**: Open for extension, closed for modification
  - Service interfaces allow easy implementation changes
  - Repository pattern enables different data sources

- **Dependency Inversion**: Depend on abstractions, not concretions
  - Controllers depend on service interfaces, not implementations
  - Constructor injection throughout the application

### Clean Architecture
```
┌─────────────────────────────────────┐
│           Presentation Layer        │
│    REST Controllers + GraphQL       │
└─────────────────────────────────────┘
┌─────────────────────────────────────┐
│            Business Layer           │
│           Service Classes           │
└─────────────────────────────────────┘
┌─────────────────────────────────────┐
│             Data Layer              │
│        Repositories + Entities      │
└─────────────────────────────────────┘
```

### Design Patterns
- **Repository Pattern**: Abstracts data access logic
- **Service Layer Pattern**: Encapsulates business logic
- **DTO Pattern**: Separates internal models from API contracts
- **Mapper Pattern**: Handles entity-DTO conversions

### Why This Architecture is Good
- **Separation of Concerns**: Each layer has distinct responsibilities
- **Testability**: Easy to unit test individual components
- **Maintainability**: Changes in one layer don't affect others
- **Scalability**: Can easily add new features without breaking existing code
- **Flexibility**: Can swap implementations (e.g., different databases)
- **Reusability**: Service logic can be used by different controllers

## Features

- ✅ CRUD operations for bank accounts
- ✅ Customer-account relationships
- ✅ Dual API support (REST + GraphQL)
- ✅ Spring Data REST auto-generated endpoints
- ✅ Automatic test data generation (4 customers, 40 accounts)
- ✅ Error handling and validation
- ✅ API documentation (Swagger + GraphiQL)

## Project Structure

```
src/main/java/org/sid/bankaccountservice/
├── entities/           # JPA entities (BankAccount, Customer)
├── repositories/       # Data access layer
├── service/           # Business logic
├── web/              # Controllers (REST + GraphQL)
├── dto/              # Data transfer objects
├── mappers/          # Entity-DTO conversion
├── enums/            # AccountType enum
└── exceptions/       # Exception handling
```

## Configuration

```properties
# application.properties
server.port=8081
spring.datasource.url=jdbc:h2:mem:account-db
spring.h2.console.enabled=true
spring.graphql.graphiql.enabled=true
```

## Key Features Implemented

- **Customer Assignment**: Accounts can be created and assigned to specific customers
- **Dual API Support**: Both REST and GraphQL endpoints for maximum flexibility
- **Spring Data REST**: Automatic REST endpoints for repositories
- **Automatic Data Population**: Test data is created on application startup
- **Error Handling**: Proper exception handling for invalid requests
- **Entity Relationships**: Proper JPA relationships between Customer and BankAccount

## Testing

### GraphQL Playground
Access at `http://localhost:8081/graphiql` to test GraphQL queries and mutations.

### REST API
Use Swagger UI at `http://localhost:8081/swagger-ui.html` or tools like Postman.

### Sample Data
The application automatically creates:
- 4 customers (Aya, Mohamed, Yassine, Hanane)
- 10 accounts per customer with random balances and types

## Prerequisites
- Java 17+
- Maven 3.6+
