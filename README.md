# Dining Review API

A Java-based REST API for managing restaurant reviews with allergy information. Users can submit reviews of dining establishments, and admins can moderate and approve reviews based on quality and accuracy.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Architecture](#architecture)
- [Troubleshooting](#troubleshooting)

## Features

- **Restaurant Management**: Create and search restaurants by location
- **Allergy Tracking**: Track allergy-specific scores for peanuts, eggs, and dairy
- **User Accounts**: Create user profiles with allergy preferences and location info
- **Review System**: Submit detailed reviews with allergy-specific ratings and commentary
- **Admin Moderation**: Review and approve/reject pending submissions
- **Location-Based Search**: Find restaurants by zip code with optional allergy filtering

## Prerequisites

Before you begin, ensure that you have the following installed on your machine:

- **Java Development Kit (JDK)** version 11 or later
- **Maven** 3.6 or higher
- **MySQL** 5.7 or later (or compatible relational database)
- **Git** for cloning the repository
- **IDE** (recommended): IntelliJ IDEA, Eclipse, or VS Code with Java extensions

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/AndreR10/dining-review-api.git
cd dining-review-api
```

### 2. Database Setup

Create a MySQL database for the application:

```sql
CREATE DATABASE dining_review_db;
```

### 3. Configure Application Properties

Update `src/main/resources/application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dining_review_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080` by default.

## Configuration

### Application Properties

Key configuration options in `application.properties`:

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | 8080 | Port the application runs on |
| `spring.jpa.hibernate.ddl-auto` | update | Hibernate DDL strategy (update/create/validate) |
| `spring.jpa.show-sql` | false | Enable SQL logging |

## Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

### Using IDE

1. Open the project in your IDE
2. Locate the main application class
3. Run it directly from the IDE

### Testing the API

Use tools like:
- **Postman**: Import the endpoint examples below
- **cURL**: Command-line requests
- **IntelliJ IDEA**: Built-in HTTP Client

## API Documentation

### Base URL

```
http://localhost:8080/api/v1
```

### Authentication

Currently, the API does not require authentication. Admin endpoints should be protected in production.

---

### Restaurant Endpoints

#### Get All Restaurants

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

#### Get Restaurant by ID

```
GET /restaurant/{id}
```

**Response:**
```json
{
  "id": 1,
  "name": "Restaurant A",
  "zipCode": "12345",
  "avgScore": 4.5,
  "peanutScore": 4.0,
  "eggScore": 4.2,
  "dairyScore": 4.8
}
```

#### Create Restaurant

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

**Response:**
```json
{
  "id": 2,
  "name": "New Restaurant",
  "zipCode": "54321",
  "avgScore": 0.0,
  "peanutScore": null,
  "eggScore": null,
  "dairyScore": null
}
```

#### Search Restaurants

```
GET /restaurant/search?zipCode=12345&allergy=peanut
```

**Query Parameters:**
- `zipCode` (required): Zip code to search in
- `allergy` (optional): Filter by allergy type (peanut, egg, dairy)

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

---

### Review Endpoints

#### Get All Reviews

```
GET /review/
```

**Response:**
```json
[
  {
    "id": 1,
    "reviewerName": "John Doe",
    "restaurantId": 1,
    "peanutScore": 4.0,
    "eggScore": 4.2,
    "dairyScore": 4.8,
    "commentary": "Great experience!",
    "status": "PENDING"
  }
]
```

#### Get Review by ID

```
GET /review/{id}
```

**Response:**
```json
{
  "id": 1,
  "reviewerName": "John Doe",
  "restaurantId": 1,
  "peanutScore": 4.0,
  "eggScore": 4.2,
  "dairyScore": 4.8,
  "commentary": "Great experience!",
  "status": "PENDING"
}
```

#### Create Review

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

**Status Values:** `PENDING`, `ACCEPTED`, `REJECTED`

---

### User Account Endpoints

#### Get All User Accounts

```
GET /user-account/
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "city": "Cityville",
    "state": "ST",
    "zipCode": "12345",
    "isAdmin": false,
    "interestedInPeanutAllergies": true,
    "interestedInEggAllergies": false,
    "interestedInDairyAllergies": true
  }
]
```

#### Get User Account by Name

```
GET /user-account/{name}
```

**Response:**
```json
{
  "id": 1,
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

#### Create User Account

```
POST /user-account/
Content-Type: application/json
```

**Request:**
```json
{
  "name": "Jane Smith",
  "city": "Townsville",
  "state": "TS",
  "zipCode": "54321",
  "isAdmin": false,
  "interestedInPeanutAllergies": true,
  "interestedInEggAllergies": true,
  "interestedInDairyAllergies": false
}
```

**Response:**
```json
{
  "id": 2,
  "name": "Jane Smith",
  "city": "Townsville",
  "state": "TS",
  "zipCode": "54321",
  "isAdmin": false,
  "interestedInPeanutAllergies": true,
  "interestedInEggAllergies": true,
  "interestedInDairyAllergies": false
}
```

#### Update User Account

```
PUT /user-account/{name}
Content-Type: application/json
```

**Request:**
```json
{
  "city": "NewCity",
  "state": "NS",
  "zipCode": "98765",
  "interestedInPeanutAllergies": false,
  "interestedInEggAllergies": true,
  "interestedInDairyAllergies": true
}
```

**Response:**
```json
{
  "id": 2,
  "name": "Jane Smith",
  "city": "NewCity",
  "state": "NS",
  "zipCode": "98765",
  "isAdmin": false,
  "interestedInPeanutAllergies": false,
  "interestedInEggAllergies": true,
  "interestedInDairyAllergies": true
}
```

---

### Admin Endpoints

#### Get Reviews by Status

```
GET /admin/review/status?status=PENDING
```

**Query Parameters:**
- `status` (required): Review status filter (PENDING, ACCEPTED, REJECTED)

**Response:**
```json
[
  {
    "id": 1,
    "reviewerName": "John Doe",
    "restaurantId": 1,
    "peanutScore": 4.0,
    "eggScore": 4.2,
    "dairyScore": 4.8,
    "commentary": "Great experience!",
    "status": "PENDING"
  }
]
```

#### Approve or Reject Review

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

**Response:**
```json
{
  "id": 1,
  "reviewerName": "John Doe",
  "restaurantId": 1,
  "peanutScore": 4.0,
  "eggScore": 4.2,
  "dairyScore": 4.8,
  "commentary": "Great experience!",
  "status": "ACCEPTED"
}
```

---

## Architecture

### Technology Stack

- **Framework**: Spring Boot
- **Language**: Java 11+
- **Database**: MySQL
- **Build Tool**: Maven
- **ORM**: JPA/Hibernate

### Project Structure

```
dining-review-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/
│   │   │       ├── controller/    # REST endpoints
│   │   │       ├── service/       # Business logic
│   │   │       ├── repository/    # Database access
│   │   │       ├── model/         # Entity classes
│   │   │       └── DiningReviewApiApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## Troubleshooting

### Common Issues

#### Database Connection Error

**Problem:** `java.sql.SQLException: Access denied for user 'root'@'localhost'`

**Solution:** 
- Verify MySQL is running
- Check credentials in `application.properties`
- Ensure the database exists: `CREATE DATABASE dining_review_db;`

#### Port Already in Use

**Problem:** `Address already in use: bind`

**Solution:**
- Change the port in `application.properties`: `server.port=8081`
- Or kill the process using port 8080

#### Hibernate DDL Errors

**Problem:** Schema mismatch errors during startup

**Solution:**
- Set `spring.jpa.hibernate.ddl-auto=create` to rebuild the schema
- Or manually create tables matching your entity definitions

#### Maven Build Failures

**Problem:** Build compilation errors

**Solution:**
```bash
mvn clean install -U
```

---

## Contributing

To contribute to this project:

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Open a pull request

## License

This project is provided as-is for educational purposes.

## Support

For questions or issues, please open a GitHub issue or contact the maintainers.

---

**Last Updated:** 2026-05-22
