# Architecture Documentation

## Overview

The Dining Review API is built using a **layered architecture pattern** that separates concerns into distinct layers, each with specific responsibilities. This document provides a comprehensive overview of the system architecture, design patterns, and technology choices.

## Table of Contents

1. [Layered Architecture](#layered-architecture)
2. [Design Patterns](#design-patterns)
3. [Technology Stack](#technology-stack)
4. [Data Model](#data-model)
5. [API Design](#api-design)
6. [Security Architecture](#security-architecture)
7. [Performance Considerations](#performance-considerations)
8. [Scalability Strategy](#scalability-strategy)
9. [Future Roadmap](#future-roadmap)

---

## Layered Architecture

The application follows a 4-tier layered architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│  (React UI, REST Controllers, Request Validation)           │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                 API/Controller Layer                        │
│  (HTTP Endpoints, Request/Response Handling)                │
│  - ReviewController                                         │
│  - RestaurantController                                     │
│  - UserAccountController                                    │
│  - AdminController                                          │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                   Service Layer                             │
│  (Business Logic, Calculations, Validations)                │
│  - RestaurantService                                        │
│  - ReviewService                                            │
│  - UserAccountService                                       │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                 Repository/Data Layer                       │
│  (Database Access, Query Execution)                         │
│  - RestaurantRepository                                     │
│  - ReviewRepository                                         │
│  - UserAccountRepository                                    │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                   Database Layer                            │
│  (H2, MySQL, PostgreSQL)                                    │
└─────────────────────────────────────────────────────────────┘
```

### Layer Responsibilities

#### **Presentation Layer (React Frontend)**
- User interface components
- Form handling and validation
- State management
- API consumption

#### **Controller Layer**
- HTTP endpoint definitions
- Request routing
- Input validation using `@Valid`
- Response formatting
- Error handling delegation to GlobalExceptionHandler

Example:
```java
@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
    // Handles HTTP requests and delegates to service layer
}
```

#### **Service Layer**
- Core business logic
- Complex calculations (e.g., average score calculations)
- Transaction management
- Logging and auditing via AOP
- Cross-cutting concerns

Example:
```java
@Service
public class RestaurantService {
    public void updateRestaurantScores(Long restaurantId) {
        // Business logic for calculating averages
    }
}
```

#### **Repository Layer**
- Database access abstraction
- Query execution via Spring Data JPA
- Entity lifecycle management
- Custom query methods

Example:
```java
@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findByStatus(ReviewStatus status);
    List<Review> findByStatusAndRestaurantId(ReviewStatus status, Long restaurantId);
}
```

#### **Data Layer**
- Persistent data storage
- Schema management via Hibernate DDL
- Connection pooling

---

## Design Patterns

### 1. **Repository Pattern**
Abstracts data access logic and provides a collection-like interface for accessing domain objects.

**Location:** `com.example.diningReview.repositories`

**Benefits:**
- Decouples business logic from database access
- Enables easy testing with mock repositories
- Simplifies switching databases (H2 → MySQL → PostgreSQL)

```java
public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
    Optional<Restaurant> findByNameAndZipCode(String name, String zipCode);
    List<Restaurant> findByZipCode(String zipCode);
}
```

### 2. **Service/Business Logic Pattern**
Encapsulates core business logic and provides a clean API for controllers.

**Location:** `com.example.diningReview.services`

**Responsibilities:**
- Orchestrate multiple repository calls
- Perform validations and calculations
- Handle transactions
- Implement business rules

```java
@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    public void updateRestaurantScores(Long restaurantId) {
        // Complex business logic here
    }
}
```

### 3. **Data Transfer Object (DTO) Pattern**
Transfers data between processes, reducing method signatures and improving encapsulation.

**Current Implementation:** Models are used as DTOs
**Future Improvement:** Separate entity models from DTOs

**Benefits:**
- Protects internal domain objects
- Allows API response transformation
- Improves performance by selective field transfer

```java
// Future implementation
public class ReviewDTO {
    private Long id;
    private String reviewerName;
    private Double peanutScore;
    // Only expose necessary fields
}
```

### 4. **Dependency Injection Pattern**
Spring automatically injects dependencies, promoting loose coupling.

**Implementation:** Constructor injection via `@Autowired`

```java
@RestController
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final RestaurantService restaurantService;
    
    public ReviewController(ReviewRepository reviewRepository, 
                           RestaurantService restaurantService) {
        this.reviewRepository = reviewRepository;
        this.restaurantService = restaurantService;
    }
}
```

### 5. **Aspect-Oriented Programming (AOP)**
Separates cross-cutting concerns like logging from business logic.

**Location:** `com.example.diningReview.aop`

**Current Usage:** Method-level logging for audit trails

**Example Aspect:**
```java
@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.example.diningReview.services.*.*(..))")
    public void logBeforeService(JoinPoint joinPoint) {
        log.debug("Entering method: {}", joinPoint.getSignature());
    }
}
```

### 6. **Enum Pattern**
Type-safe representation of fixed set of values.

**Location:** `com.example.diningReview.enums`

**Example:**
```java
public enum ReviewStatus {
    PENDING,    // Awaiting admin action
    ACCEPTED,   // Approved by admin
    REJECTED    // Rejected by admin
}
```

---

## Technology Stack

### Backend

| Component | Technology | Version | Rationale |
|-----------|-----------|---------|-----------|
| Framework | Spring Boot | 3.1.5 | Enterprise-grade, production-ready framework |
| Language | Java | 21 | Latest LTS with performance improvements |
| ORM | JPA/Hibernate | 2.x | Industry standard for data persistence |
| Database | H2 (dev) | Latest | Embedded for easy local development |
| Security | Spring Security | 6.x | Built-in security features, authentication/authorization |
| Testing | JUnit 5 | Latest | Modern testing framework with better features |
| Mocking | Mockito | 5.x | Standard mocking framework for unit tests |
| Logging | SLF4J + Logback | Latest | Flexible, performant logging |
| Build | Maven | 3.6+ | Dependency management and build automation |
| API Docs | SpringDoc OpenAPI | Latest | Auto-generated Swagger/OpenAPI documentation |

### Frontend

| Component | Technology | Version | Rationale |
|-----------|-----------|---------|-----------|
| Framework | React | 18 | Component-based UI, excellent ecosystem |
| Router | React Router | v6 | Client-side routing for SPA |
| Styling | Tailwind CSS | Latest | Utility-first CSS for rapid development |
| HTTP Client | Axios | Latest | Promise-based, simple HTTP client |
| UI Components | Material-UI | 5.x | Pre-built accessible components |

### DevOps

| Component | Technology | Version | Rationale |
|-----------|-----------|---------|-----------|
| Containerization | Docker | Latest | Industry standard for containerization |
| Orchestration | Docker Compose | Latest | Multi-container local development |
| CI/CD | GitHub Actions | Latest | Integrated with GitHub, no additional setup |

---

## Data Model

### Entity Relationships

```
┌──────────────────┐         ┌──────────────────┐
│   Restaurant     │         │    UserAccount   │
├──────────────────┤         ├──────────────────┤
│ id (PK)          │         │ id (PK)          │
│ name             │◄────────│ name             │
│ zipCode          │  1:N    │ city             │
│ avgScore         │         │ state            │
│ peanutScore      │         │ zipCode          │
│ eggScore         │         │ isAdmin          │
│ dairyScore       │         │ allergies[]      │
└──────────────────┘         └──────────────────┘
        ▲
        │ 1:N
        │
┌──────────────────┐
│     Review       │
├──────────────────┤
│ id (PK)          │
│ reviewerName (FK)│
│ restaurantId (FK)│
│ peanutScore      │
│ eggScore         │
│ dairyScore       │
│ commentary       │
│ status           │
│ createdAt        │
└──────────────────┘
```

### Database Schema

**Restaurant Table**
```sql
CREATE TABLE restaurant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    avg_score DOUBLE,
    peanut_score DOUBLE,
    egg_score DOUBLE,
    dairy_score DOUBLE,
    UNIQUE KEY uk_name_zip (name, zip_code)
);
```

**Review Table**
```sql
CREATE TABLE review (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    reviewer_name VARCHAR(255) NOT NULL,
    restaurant_id BIGINT NOT NULL REFERENCES restaurant(id),
    peanut_score DOUBLE,
    egg_score DOUBLE,
    dairy_score DOUBLE,
    commentary VARCHAR(500),
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant(id)
);
```

**UserAccount Table**
```sql
CREATE TABLE user_account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    is_admin BOOLEAN DEFAULT FALSE,
    interested_in_peanut_allergies BOOLEAN,
    interested_in_egg_allergies BOOLEAN,
    interested_in_dairy_allergies BOOLEAN
);
```

---

## API Design

### REST Principles

The API follows RESTful conventions:

- **Resources** are identified by URIs: `/api/v1/restaurant/{id}`
- **HTTP Methods** represent operations:
  - `GET` - Retrieve resource(s)
  - `POST` - Create new resource
  - `PUT` - Update existing resource
  - `DELETE` - Delete resource
- **HTTP Status Codes** indicate result:
  - `200 OK` - Successful GET/PUT
  - `201 Created` - Successful POST
  - `400 Bad Request` - Validation error
  - `404 Not Found` - Resource not found
  - `500 Internal Server Error` - Server error

### API Versioning

Current version: `v1`
- Base URL: `http://localhost:8080/api/v1`
- Future versions will use `/api/v2`, `/api/v3`, etc.

### Request/Response Format

**Standard Request:**
```json
{
  "name": "Restaurant Name",
  "zipCode": "12345"
}
```

**Standard Response:**
```json
{
  "id": 1,
  "name": "Restaurant Name",
  "zipCode": "12345",
  "avgScore": 4.5,
  "peanutScore": 4.0,
  "eggScore": 4.2,
  "dairyScore": 4.8
}
```

**Error Response:**
```json
{
  "timestamp": "2026-05-25T10:15:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Restaurant not found",
  "path": "/api/v1/restaurant/999"
}
```

---

## Security Architecture

### Authentication & Authorization

**Current Implementation:**
- Role-based access control (RBAC) via `isAdmin` flag
- Public endpoints for restaurants and reviews (read)
- Protected endpoints for admin operations (write)

**Future Implementation:**
- JWT (JSON Web Token) authentication
- OAuth 2.0 for third-party integrations
- API Key management

### Security Measures

1. **Input Validation**
   ```java
   @NotBlank(message = "Name is mandatory")
   private String reviewerName;
   ```

2. **CORS Protection**
   ```java
   @Configuration
   public class SecurityConfig {
       // CORS configuration
   }
   ```

3. **Error Handling**
   - Sensitive information not exposed in error messages
   - Centralized exception handler in `GlobalExceptionHandler`

4. **SQL Injection Prevention**
   - JPA parameterized queries
   - No raw SQL execution

---

## Performance Considerations

### Database Optimization

1. **Indexing**
   ```java
   @Column(unique = true)
   private String name;
   ```

2. **Query Optimization**
   - Use `CrudRepository` derived queries
   - Lazy loading to reduce data transfer
   - Select only necessary fields

3. **Caching Strategy**
   ```java
   // Future implementation
   @Cacheable("restaurants")
   public Restaurant findById(Long id) { }
   ```

### API Performance

1. **Pagination** (Future)
   ```java
   Page<Review> findByStatus(ReviewStatus status, Pageable pageable);
   ```

2. **Response Compression**
   ```properties
   server.compression.enabled=true
   ```

3. **Connection Pooling**
   - HikariCP for optimal database connections

---

## Scalability Strategy

### Horizontal Scaling

1. **Stateless API Design**
   - No session state stored in application
   - Any instance can handle any request
   - Load balancer can distribute traffic

2. **Database Scaling**
   - Read replicas for query optimization
   - Sharding strategy for large datasets
   - Migration from H2 to PostgreSQL/MySQL

### Vertical Scaling

1. **Increased Resources**
   - Java heap size configuration
   - Database optimization
   - Connection pool tuning

### Microservices (Future)

```
┌─────────────────────────────────────────────┐
│       API Gateway (Load Balancer)           │
└─────┬─────────────────────────┬─────────────┘
      │                         │
  ┌───▼─────┐          ┌───────▼──┐
  │ Restaurant    │          │ Review Service │
  │ Service       │          │              │
  └───────────┘          └──────────┘
```

---

## Logging Strategy

### Log Levels

- **DEBUG** - Detailed tracing of method entry/exit
- **INFO** - General informational messages
- **WARN** - Warning conditions (invalid input, retries)
- **ERROR** - Error conditions (exceptions, failures)

### Audit Trail

AOP-based aspect logs all service method calls:

```java
@Aspect
@Component
@Slf4j
public class AuditAspect {
    @Before("execution(* com.example.diningReview.services.*.*(..))")
    public void auditLog(JoinPoint joinPoint) {
        log.info("User: {} executed: {}", 
                 getCurrentUser(), 
                 joinPoint.getSignature());
    }
}
```

---

## Testing Strategy

### Test Pyramid

```
          ▲
         ╱ ╲        Integration Tests (10-15%)
        ╱   ╲       - @SpringBootTest
       ╱     ╲      - Full context
      ╱───────╲
     ╱         ╲    Unit Tests (70-80%)
    ╱           ╲   - @ExtendWith(MockitoExtension.class)
   ╱─────────────╲  - Mocked dependencies
  ╱               ╲
 ╱_________________╲ E2E Tests (5-10%)
                    - Postman collections
                    - Selenium tests
```

### Test Coverage Goals

- **Unit Tests**: 80%+ coverage for services
- **Integration Tests**: 60%+ coverage for controllers
- **Overall**: 75%+ coverage target

### Test Execution

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

---

## Deployment Architecture

### Local Development
```
Docker Compose
├── Backend (Spring Boot)
├── Frontend (React)
└── Database (H2/MySQL)
```

### Production Deployment (Future)
```
Kubernetes Cluster
├── Ingress (API Gateway)
├── Pods (Spring Boot replicas)
├── Service (Load Balancer)
└── StatefulSet (Database)
```

---

## Future Roadmap

### v1.1 - Enhanced Features
- [ ] JWT authentication
- [ ] API key management
- [ ] Rate limiting
- [ ] Request caching
- [ ] Pagination for large datasets

### v1.2 - Performance
- [ ] Redis caching layer
- [ ] Database query optimization
- [ ] API response compression
- [ ] Bulk operations support

### v2.0 - Microservices
- [ ] Service decomposition
- [ ] Event-driven architecture
- [ ] Message queue (RabbitMQ/Kafka)
- [ ] API Gateway pattern

### v3.0 - Advanced Features
- [ ] GraphQL API
- [ ] Real-time notifications (WebSocket)
- [ ] Advanced analytics
- [ ] Machine learning recommendations

---

## Conclusion

This architecture provides a solid foundation for building scalable, maintainable REST APIs. The separation of concerns, use of established design patterns, and adherence to best practices make the codebase easy to understand, test, and extend.

For questions or clarifications, refer to the code comments or open an issue in the GitHub repository.
