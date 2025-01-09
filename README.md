# ProductReview - Product Reviews Platform

This project is a Product Reviews platform. It allows users to write reviews, rate products, and browse reviews based on categories. It includes a frontend built with HTML, CSS, and JavaScript, and a backend using Java Spring Boot with MySQL. The platform also features real-time notifications and performance optimization using Redis caching.

## Features

### 1. **User Authentication and Authorization**
- **Registration and Login**: Users can register, log in, and leave reviews.
- **Roles**:
  - **Regular User**: Can write reviews, rate products, and view others' reviews.
  - **Administrator**: Can manage and delete inappropriate reviews.

### 2. **Backend (Java Spring Boot)**
- **REST API Endpoints**:
  - User Management: Registration, login (authentication).
  - Review Management: Create, read, update, delete (CRUD) reviews. Admin can delete inappropriate reviews.
  - Category Management: Organize products into categories.

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
- **User Interfaces**:
  - Login and Registration screens.
  - Review listing by category (with search and filtering options).
  - Review submission form.

### 7. **Testing**
- **JUnit Tests**: Tests for review addition functionality.
- **Selenium Tests**: Ensures users can add new reviews.

---

## Installation

### Prerequisites
1. **Java**: JDK 21 or newer.
2. **Spring Boot**: Compatible version installed.
3. **MySQL**: Database service running.
4. **Redis**: Installed and configured.


