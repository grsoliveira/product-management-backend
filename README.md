# Product Management API

A backend project for product and category management, developed with Spring Boot.

## Technologies Used

- Java 17
- Spring Boot 3.4.5
- Spring Security
- Spring Data JPA
- MySQL
- Lombok
- Swagger/OpenAPI
- Maven

## Project Structure

The project follows a layered architecture:
- **controllers**: REST API endpoints
- **services**: Business logic
- **repositories**: Data access layer
- **entities**: Data models
- **dtos**: Data Transfer Objects

## Main Features

- Complete Product CRUD
- Category Management with hierarchical structure (parent/children)
- Product search filters
- API Documentation with Swagger
- Spring Security Authentication

## Authentication

The API uses Basic Authentication:
- Username: `admin`
- Password: `admin`

## How to Run

1. Clone the repository
2. Configure MySQL with appropriate credentials
3. Run SQL script to create necessary tables
4. Run the project:


## API Documentation

Complete API documentation is available through Swagger UI:
- URL: `http://localhost:8080/swagger-ui.html`

## Database

The project uses MySQL as the database. The structure includes:
- Products table
- Categories table with hierarchical relationships
- 
![image](https://github.com/user-attachments/assets/58992140-411b-44d8-9035-bc7ebf192c53)


## CI/CD

GitHub Actions workflow is configured to run tests automatically on merge to main branch.
