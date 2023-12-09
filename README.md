# Dining Review API Documentation

This document provides instructions on running the Dining Review API on a new machine and includes detailed documentation of the API endpoints.

## Prerequisites

Before you begin, ensure that you have the following installed on your machine:

- Java Development Kit (JDK) version 11 or later
- Maven
- Your preferred IDE (IntelliJ IDEA, Eclipse, etc.)
- MySQL or another compatible relational database

## Getting Started

1. **Clone the Repository:**

   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. **Database Configuration:**

   - Set up your MySQL database and update the `application.properties` file in the `src/main/resources` directory with your database configuration.

3. **Build the Project:**

   ```bash
   mvn clean install
   ```

4. **Run the Application:**

   ```bash
   mvn spring-boot:run
   ```

   The application should now be running locally.

## API Endpoints

### Restaurant Endpoints

#### 1. Get All Restaurants

- **Endpoint:** `/api/v1/restaurant/`
- **Method:** `GET`
- **Description:** Retrieves a list of all restaurants.
- **Response:**
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
      },
      ...
  ]
  ```

#### 2. Get Restaurant by ID

- **Endpoint:** `/api/v1/restaurant/{id}`
- **Method:** `GET`
- **Description:** Retrieves details of a specific restaurant by ID.
- **Response:**
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

#### 3. Create Restaurant

- **Endpoint:** `/api/v1/restaurant/`
- **Method:** `POST`
- **Description:** Creates a new restaurant.
- **Request:**
  ```json
  {
    "name": "New Restaurant",
    "zipCode": "54321"
  }
  ```
- **Response:**
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

#### 4. Search Restaurants

- **Endpoint:** `/api/v1/restaurant/search`
- **Method:** `GET`
- **Description:** Searches for restaurants based on zip code and optional allergy.
- **Query Parameters:**
  - `zipCode` (required): The zip code for restaurant search.
  - `allergy` (optional): Filter restaurants by allergy type (e.g., "peanut").
- **Response:**
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
      },
      ...
  ]
  ```

### Review Endpoints

#### 1. Get All Reviews

- **Endpoint:** `/api/v1/review/`
- **Method:** `GET`
- **Description:** Retrieves a list of all reviews.
- **Response:**
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
      },
      ...
  ]
  ```

#### 2. Get Review by ID

- **Endpoint:** `/api/v1/review/{id}`
- **Method:** `GET`
- **Description:** Retrieves details of a specific review by ID.
- **Response:**
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

#### 3. Create Review

- **Endpoint:** `/api/v1/review/`
- **Method:** `POST`
- **Description:** Creates a new review.
- **Request:**
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
- **Response:**
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

### User Account Endpoints

#### 1. Get All User Accounts

- **Endpoint:** `/api/v1/user-account/`
- **Method:** `GET`
- **Description:** Retrieves a list of all user accounts.
- **Response:**
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
      },
      ...
  ]
  ```

#### 2. Get User Account by Name

- **Endpoint:** `/api/v1/user-account/{name}`
- **Method:** `GET`
- **Description:** Retrieves details of a specific user account by name.
- **Response:**
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
      "interestedInDairyAll
  ```

ergies": true
}
```

#### 3. Create User Account

- **Endpoint:** `/api/v1/user-account/`
- **Method:** `POST`
- **Description:** Creates a new user account.
- **Request:**
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
- **Response:**
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

#### 4. Update User Account

- **Endpoint:** `/api/v1/user-account/{name}`
- **Method:** `PUT`
- **Description:** Updates an existing user account.
- **Request:**
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
- **Response:**
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

### Admin Endpoints

#### 1. Get Reviews by Status (Admin)

- **Endpoint:** `/api/v1/admin/review/status`
- **Method:** `GET`
- **Description:** Retrieves a list of reviews with the specified status.
- **Query Parameter:**
  - `status` (required): Review status (e.g., "ACCEPTED").
- **Response:**
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
          "status": "ACCEPTED"
      },
      ...
  ]
  ```

#### 2. Update Review Status (Admin)

- **Endpoint:** `/api/v1/admin/review/{reviewId}/`
- **Method:** `PUT`
- **Description:** Approves or rejects a given dining review.
- **Path Parameter:**
  - `reviewId` (required): ID of the review to update.
- **Request:**
  ```json
  {
    "accept": true
  }
  ```
- **Response:**
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

## Conclusion

Congratulations! You have successfully set up and documented the Dining Review API. Refer to this documentation for information on available endpoints and how to interact with them.
