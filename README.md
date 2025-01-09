# ProductReview - Product Reviews Platform

This project is a Product Reviews platform. It allows users to write reviews, rate products, and browse reviews based on categories. It includes a frontend built with HTML, CSS, and JavaScript, and a backend using Java Spring Boot with MySQL. The platform also features real-time notifications and performance optimization using Redis caching.

## Features

### 1. **User Authentication and Authorization**
- **Registration and Login**: Users can register, log in, and leave reviews.
- **Roles**:
  - **Regular User**: Can write reviews, rate products, and view others' reviews.
  - **Administrator**: Can manage and delete inappropriate reviews.

### 2. **Backend (Java Spring Boot)**

## API Documentation

### User Data Controller (`/api/user`)

| HTTP Method | Endpoint           | Description                                                                                   |
|-------------|--------------------|-----------------------------------------------------------------------------------------------|
| `POST`      | `/login`           | Authenticate a user and return a JWT token.                                                  |
| `POST`      | `/add`             | Register a new user and optionally subscribe to newsletters.                                 |
| `PUT`       | `/update`          | Update user details. Requires Authorization header with a valid token.                       |
| `GET`       | `/name`            | Retrieve the name of a user by their ID. Requires Authorization header with a valid token.   |

### Review Controller (`/api/review`)

| HTTP Method | Endpoint           | Description                                                                                   |
|-------------|--------------------|-----------------------------------------------------------------------------------------------|
| `GET`       | `/category`        | Retrieve reviews for a specific category. Requires Authorization header with a valid token.   |
| `GET`       | `/user`            | Retrieve all reviews created by the logged-in user. Requires Authorization header with a valid token. |
| `POST`      | `/add`             | Add a new review. Accepts a review JSON, image, and category as parameters. Requires Authorization header with a valid token. |
| `DELETE`    | `/delete`          | Delete a review by its ID. Requires Authorization header with a valid token.                 |

### Categories Controller (`/api/categories`)

| HTTP Method | Endpoint   | Description                                                                                   |
|-------------|------------|-----------------------------------------------------------------------------------------------|
| `GET`       | `/all`     | Retrieve a list of all product categories. Requires Authorization header with a valid token.  |

#### WebSocket Endpoints

| Action             | Destination        | Description                                                                                  |
|--------------------|--------------------|----------------------------------------------------------------------------------------------|
| `@SendTo`          | `/topic/messages` | Broadcasts messages to all connected users subscribed to this topic.                         |

#### Live Notifications
- Real-time messages about new reviews using WebSocket.
- The `/topic/messages` channel is updated without requiring a page reload.

#### Redis Integration
- Stores and retrieves the latest review for optimized performance.
- Broadcasts the last review during user subscription to `/topic/messages`.

#### Authorization
- Validates the `Authorization` token for all endpoints and WebSocket connections.

#### Scheduled Message
- Sends a message with the latest review to new connected users.

--

- **Database (MySQL)**:
  - **Tables**:
    - `Users`: Stores user details (`ID`, `Name`, `Email`, `Password`, `Role`).
    - `Categories`: Stores product categories (`ID`, `Name`).
    - `Reviews`: Stores reviews (`ID`, `User ID`, `Product`, `Category ID`, `Text`, `Rating`).
  - **Relationships**:
    - One-to-Many: Users → Reviews.

### 3. **Redis Caching**
- Caches the latest reviews for faster client-side rendering.

### 4. **Real-Time Data Streaming**
- **WebSocket**: Live notifications for new reviews are broadcasted to all connected users without page reload.

### 5. **Multithreading**
- **Background Task**: Sends newsletters to users.

### 6. **Frontend (HTML, CSS, JavaScript)**

The frontend for this project is hosted in a separate repository. You can find it [here](https://github.com/vi70sion/product-review-frontend).

### 7. **Testing**
- **JUnit Tests**: Tests for review addition functionality.

- **Selenium Tests**
The Selenium Tests for this project is hosted in a separate repository. You can find it [here](https://github.com/vi70sion/product-review-autotest.git).
Selenium tests are used to verify the functionality of the Product Review platform through automated UI testing. Below is a brief description of the implemented tests:

#### 1. **User Registration Test**
This test verifies the user registration and login functionality:
- Navigates to the registration page.
- Enters user details such as full name, email, and password.
- Submits the registration form and confirms successful account creation.
- Logs in with the newly created credentials.
- Validates that the logged-in user's name matches the expected name.

#### 2. **Add Review Test**
This test checks the functionality of adding a new product review:
- Logs in with existing user credentials.
- Navigates to the review submission form.
- Selects a category, enters product details (name, review text, and rating), and uploads an image.
- Submits the review and verifies that it is successfully added.
- Validates the new review appears in the appropriate category's review list.


---

## Installation

### Prerequisites
1. **Java**: JDK 21 or newer.
2. **Spring Boot**: Compatible version installed.
3. **MySQL**: Database service running.
4. **Redis**: Installed and configured.


