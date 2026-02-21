# Product Manager API

A RESTful API solution for Product Management built with Java 17, Spring Boot 3, and Spring Security with JWT authentication. This project demonstrates best practices in API design, security, testing, and containerization.


## Features

✅ **Full CRUD Operations** on Products and Items
✅ **JWT Authentication** with Access & Refresh Token rotation
✅ **Role-Based Authorization** (RBAC)
✅ **Pagination Support** for collection endpoints
✅ **Comprehensive Error Handling** with standardized responses
✅ **OpenAPI/Swagger Documentation** with interactive UI
✅ **Input Validation** using Jakarta Validation
✅ **Database Indexing** for performance optimization
✅ **Async Processing** support
✅ **CORS Configuration** enabled
✅ **Docker & Docker Compose** for containerized deployment
✅ **Comprehensive Testing** - Unit, Integration & Mock tests
✅ **Clean Architecture** with separated layers

## Technology Stack
- **Java 17** LTS
- **Spring Boot 3.2.0**
- **Spring Security** 6.x
- **Spring Data JPA** with Hibernate ORM
- **JWT (JJWT)** for token management

### Database
- **MySQL 8.0** (Primary)
- **PostgreSQL 15** (Optional)
- **H2** (In-memory for testing)

### Testing
- **JUnit 5** (Jupiter)
- **Mockito** 5.x
- **Spring Boot Test**

### Build & DevOps
- **Maven 3.9.x**
- **Docker** 24.x
- **Docker Compose** 2.x


### Local Development Setup



#### 2. Configure Database


**For PostgreSQL:**

```sql
CREATE DATABASE product_manager;
CREATE USER productuser WITH PASSWORD 'productpass';
GRANT ALL PRIVILEGES ON DATABASE product_manager TO productuser;
```

#### 3. Update Application Properties

**For MySQL** (default):
```bash
# No changes needed, application-mysql.properties is loaded by default
```

**For PostgreSQL:**
```properties
# Add to environment variables or application.properties
spring.profiles.active=postgres
```

Use environment variables:
```bash
export SPRING_PROFILES_ACTIVE=postgres
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/product_manager
export SPRING_DATASOURCE_USERNAME=productuser
export SPRING_DATASOURCE_PASSWORD=productpass
```


Swagger UI: **http://localhost:8080/swagger-ui.html**

API Docs: **http://localhost:8080/v3/api-docs**

## Docker Deployment

### Using Docker Compose (Recommended)

#### 1. Build and Run with MySQL

```bash
docker-compose up --build
```

The API will be available at: **http://localhost:8080**

#### 2. Run with PostgreSQL

```bash
docker-compose --profile postgres up --build
```

#### 3. Run with Database Admin UI (Adminer)

```bash
docker-compose --profile admin up --build
```

Adminer will be available at: **http://localhost:8888**

#### 4. Stop Services

```bash
docker-compose down
```

Remove volumes as well:
```bash
docker-compose down -v
```

### Manual Docker Build

```bash
# Build image
docker build -t product-manager-api:1.0.0 .

# Run container
docker run -d \
  --name product-manager \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/product_manager \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=root \
  product-manager-api:1.0.0
```

## API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Health Check Endpoints

#### Get Health Status
```
GET /
GET /health
```

### Authentication Endpoints

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "tokenType": "Bearer",
  "expiresIn": 3600000,
  "username": "admin"
}
```

#### Refresh Token
```http
POST /auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGc..."
}
```

### Product Endpoints


#### Get All Products
```http
GET /products?page=0&size=10&sortBy=createdOn&direction=DESC
Authorization: Bearer {accessToken}
```

#### Get Product by ID
```http
GET /products/{id}
Authorization: Bearer {accessToken}
```

#### Update Product
```http
PUT /products/{id}
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "productName": "Updated Product Name"
}
```

#### Delete Product
```http
DELETE /products/{id}
Authorization: Bearer {accessToken}
```

**Status:** 204 No Content

#### Search Products
```http
GET /products/search?keyword=laptop&page=0&size=10
Authorization: Bearer {accessToken}
```

### Item Endpoints

#### Create Item
```http
POST /products/{productId}/items
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "quantity": 100
}
```

#### Get Product Items
```http
GET /products/{productId}/items?page=0&size=10
Authorization: Bearer {accessToken}
```

#### Get Item by ID
```http
GET /products/{productId}/items/{itemId}
Authorization: Bearer {accessToken}
```

#### Update Item
```http
PUT /products/{productId}/items/{itemId}
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "quantity": 150
}
```

#### Delete Item
```http
DELETE /products/{productId}/items/{itemId}
Authorization: Bearer {accessToken}
```

## Authentication

### JWT Token Structure

The API uses JWT (JSON Web Tokens) for stateless authentication.

**Token Format:**
```
Authorization: Bearer {accessToken}
```

### Token Claims

- **sub** (subject): Username
- **iat** (issued at): Token creation time
- **exp** (expiration): Token expiration time

### Token Expiration

- **Access Token:** 1 hour (3600000 ms)
- **Refresh Token:** 7 days (604800000 ms)

### Token Validation

All protected endpoints require a valid JWT token in the `Authorization` header.



## Security Considerations

### Implemented Security Features

✅ **JWT Authentication** - Stateless, token-based authentication
✅ **Role-Based Authorization** - Fine-grained access control
✅ **CORS Configuration** - Controlled cross-origin requests
✅ **Input Validation** - Jakarta Validation annotations
✅ **SQL Injection Prevention** - Parameterized queries via JPA
✅ **HTTPS Support** - Enforce secure communication (production)
✅ **Password Hashing** - BCrypt for password storage
✅ **Token Expiration** - Automatic token invalidation


### Environment Variables

```bash
# JWT Configuration
JWT_SECRET=YourVerySecureSecret
JWT_EXPIRATION=3600000
JWT_REFRESH_TOKEN_EXPIRATION=604800000

# Database Configuration
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/product_manager
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
SPRING_PROFILES_ACTIVE=mysql

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200
CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,PATCH,OPTIONS
```

### Docker Issues

```bash
# Remove all containers and volumes
docker system prune -a --volumes

# Rebuild images
docker-compose up --build --force-recreate
```

**Last Updated:** February 20, 2024
**Version:** 1.0.0
**Status:** Production Ready ✅
