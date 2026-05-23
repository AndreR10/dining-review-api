````markdown
# Dining Review API

A Java-based REST API for managing restaurant reviews with allergy information, built with enterprise-grade security, logging, and a modern React web UI. Users can submit reviews of dining establishments, and admins can moderate and approve reviews based on quality and allergy-specific ratings.

## 🎯 Features

- **Restaurant Management**: Create and search restaurants by location
- **Allergy Tracking**: Track allergy-specific scores for peanuts, eggs, and dairy
- **User Accounts**: Create user profiles with allergy preferences and location info
- **Review System**: Submit detailed reviews with allergy-specific ratings and commentary
- **Admin Moderation**: Review and approve/reject pending submissions with role-based authorization
- **Location-Based Search**: Find restaurants by zip code with optional allergy filtering
- **API Documentation**: Auto-generated Swagger/OpenAPI documentation
- **Modern Web UI**: React-based frontend with Tailwind CSS styling
- **Security**: Spring Security with CORS protection and role-based access control
- **Logging & Auditing**: AOP-based method logging for audit trails
- **Containerization**: Docker & Docker Compose for easy deployment

## 📋 Prerequisites

Before you begin, ensure that you have the following installed on your machine:

- **Java Development Kit (JDK)** version 21 or later
- **Maven** 3.6 or higher
- **Node.js** 18+ and npm (for frontend development)
- **Docker & Docker Compose** (optional, for containerized deployment)
- **Git** for cloning the repository

## 🚀 Quick Start

### Option 1: Docker Compose (Recommended - One Command)

```bash
git clone https://github.com/AndreR10/dining-review-api.git
cd dining-review-api
docker-compose up --build
```

Access the application:
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api
- **Swagger API Docs**: http://localhost:8080/api/swagger-ui.html
- **H2 Database Console**: http://localhost:8080/h2-console

### Option 2: Local Development

#### Backend Setup

```bash
# Clone the repository
git clone https://github.com/AndreR10/dining-review-api.git
cd dining-review-api

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

Backend will start on `http://localhost:8080/api`

#### Frontend Setup

```bash
# Navigate to frontend directory
cd dining-review-ui

# Install dependencies
npm install

# Start the development server
npm start
```

Frontend will start on `http://localhost:3000`

## ⚙️ Configuration

### Application Properties

Key configuration options in `src/main/resources/application.properties`:

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | 8080 | Port the application runs on |
| `server.servlet.context-path` | /api | Base path for API endpoints |
| `spring.datasource.url` | jdbc:h2:./db/database | H2 database URL |
| `spring.jpa.hibernate.ddl-auto` | create-drop | Hibernate DDL strategy |
| `logging.level.com.example` | DEBUG | Application logging level |
| `springdoc.swagger-ui.enabled` | true | Enable Swagger UI |

### Environment Profiles

The application supports multiple profiles. Set via `spring.profiles.active`:
- `dev` (default) - Development environment with H2 database
- `prod` - Production environment (configure database separately)

## 📚 API Documentation

### Base URL

```
http://localhost:8080/api/v1
```

### Interactive API Documentation

Access Swagger UI at: `http://localhost:8080/api/swagger-ui.html`

### Authentication & Authorization

- **Public Endpoints**: Restaurants, reviews (view), user registration
- **Admin Endpoints**: Review moderation (`/admin/**`)
- **Default Users**: Currently no authentication required (add users via User Account endpoint)

---

## 🍽️ Restaurant Endpoints

### Get All Restaurants

```
GET /restaurant/
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Restaurant A",
    "zipCode": "12345",
    "avgScore": 4.5,
    "peanutScore": 4.0,
    "eggScore": 4.2,
    "dairyScore": 4.8
  }
]
```

### Get Restaurant by ID

```
GET /restaurant/{id}
```

### Create Restaurant

```
POST /restaurant/
Content-Type: application/json
```

**Request:**
```json
{
  "name": "New Restaurant",
  "zipCode": "54321"
}
```

### Search Restaurants

```
GET /restaurant/search?zipCode=12345&allergy=peanut
```

**Query Parameters:**
- `zipCode` (required): Zip code to search in
- `allergy` (optional): Filter by allergy type (peanut, egg, dairy)

---

## ⭐ Review Endpoints

### Submit Review

```
POST /review/
Content-Type: application/json
```

**Request:**
```json
{
  "reviewerName": "Jane Smith",
  "restaurantId": 2,
  "peanutScore": 4.5,
  "eggScore": 3.8,
  "dairyScore": 4.2,
  "commentary": "Enjoyed the meal!"
}
```

**Response:**
```json
{
  "id": 2,
  "reviewerName": "Jane Smith",
  "restaurantId": 2,
  "peanutScore": 4.5,
  "eggScore": 3.8,
  "dairyScore": 4.2,
  "commentary": "Enjoyed the meal!",
  "status": "PENDING"
}
```

### Get All Reviews

```
GET /review/
```

### Get Review by ID

```
GET /review/{id}
```

---

## 👤 User Account Endpoints

### Create User Account

```
POST /user-account/
Content-Type: application/json
```

**Request:**
```json
{
  "name": "John Doe",
  "city": "Cityville",
  "state": "ST",
  "zipCode": "12345",
  "isAdmin": false,
  "interestedInPeanutAllergies": true,
  "interestedInEggAllergies": false,
  "interestedInDairyAllergies": true
}
```

### Get User Account by Name

```
GET /user-account/{name}
```

### Update User Account

```
PUT /user-account/{name}
Content-Type: application/json
```

### Get All User Accounts

```
GET /user-account/
```

---

## 👨‍⚖️ Admin Endpoints

### Get Reviews by Status

```
GET /admin/review/status?status=PENDING
```

**Query Parameters:**
- `status` (required): PENDING, ACCEPTED, or REJECTED

### Approve or Reject Review

```
PUT /admin/review/{reviewId}/
Content-Type: application/json
```

**Request:**
```json
{
  "accept": true
}
```

---

## 🏗️ Architecture

### Technology Stack

**Backend:**
- Framework: Spring Boot 3.1.5
- Language: Java 21
- Database: H2 (default), configurable to MySQL/PostgreSQL
- Build Tool: Maven
- ORM: JPA/Hibernate
- Security: Spring Security
- API Docs: SpringDoc OpenAPI (Swagger)
- Logging: SLF4J

**Frontend:**
- Framework: React 18
- Routing: React Router v6
- Styling: Tailwind CSS
- HTTP Client: Axios
- UI Components: Material-UI

### Project Structure

```
dining-review-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── config/           # Security & app configuration
│   │   │   ├── controller/        # REST endpoints
│   │   │   ├── service/           # Business logic
│   │   │   ├── repository/        # Database access
│   │   │   ├── model/             # Entity classes
│   │   │   ├── exception/         # Custom exceptions & handlers
│   │   │   ├── aop/               # Aspect-oriented programming
│   │   │   └── DiningReviewApiApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── dining-review-ui/
│   ├── src/
│   │   ├── pages/                 # Page components
│   │   ├── services/              # API client
│   │   ├── App.jsx                # Main component
│   │   └── index.js               # Entry point
│   ├── public/
│   ├── package.json
│   └── Dockerfile
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── README.md
```

## 🔐 Security Features

- **Spring Security**: Stateless API with role-based authorization
- **CORS Protection**: Configured for localhost development and production domains
- **Password Encryption**: BCrypt for secure password storage
- **H2 Console**: Protected in production environments
- **Validation**: Input validation on all request bodies
- **Error Handling**: Secure error messages without exposing system details

## 📊 Monitoring & Health

### Actuator Endpoints

- **Health Check**: `GET /actuator/health`
- **Application Info**: `GET /actuator/info`
- **Metrics**: `GET /actuator/metrics`

## 🐛 Troubleshooting

### Common Issues

#### Port Already in Use

**Problem:** `Address already in use: bind`

**Solution:**
```bash
# Change port in application.properties
server.port=8081

# Or kill the process using port 8080
lsof -i :8080
kill -9 <PID>
```

#### Database Connection Error

**Problem:** H2 database connection issues

**Solution:**
- Ensure `./db` directory exists or is created by the application
- Check `spring.datasource.url` in `application.properties`

#### Frontend Cannot Connect to Backend

**Problem:** CORS errors in browser console

**Solution:**
- Ensure backend is running on port 8080
- Check `REACT_APP_API_URL` environment variable in frontend
- Verify CORS configuration in `SecurityConfig.java`

#### Maven Build Fails

**Solution:**
```bash
mvn clean install -U
```

## 📝 Development Practices

### Code Style
- Follow Google Java Style Guide
- Use Lombok for reducing boilerplate
- Organize code by feature/module

### Testing
- Unit tests for services and repositories
- Integration tests for controllers
- Use `@SpringBootTest` for full application context

### Logging
- Use SLF4J with logback
- Log method entry/exit in services
- Use appropriate log levels (DEBUG, INFO, WARN, ERROR)

## 🤝 Contributing

To contribute to this project:

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Open a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For questions or issues:
- Open a GitHub issue with detailed description
- Check existing issues for solutions
- Contact the maintainers

---

## 🎉 Industry Best Practices Implemented

✅ Security: Spring Security with role-based authorization  
✅ API Documentation: Swagger/OpenAPI integration  
✅ Error Handling: Centralized exception handling with consistent responses  
✅ Logging & Auditing: AOP-based method logging  
✅ Code Quality: Lombok, validation, clean architecture  
✅ Configuration: Environment-specific profiles  
✅ Containerization: Docker & Docker Compose  
✅ Frontend: Modern React with responsive design  
✅ Monitoring: Spring Actuator health checks  
✅ Testing: Ready for unit and integration tests  

---

**Last Updated:** 2026-05-23
**Version:** 1.0.0 - Production Ready

````
