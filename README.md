# Authentication Service

A Spring Boot microservice for user authentication and authorization in the Expense Tracker application. This service handles user registration, login, JWT token generation, and user profile management.

## Features

- ✅ **User Registration (Sign-up)** - Create new user accounts with encrypted passwords
- ✅ **User Authentication (Login)** - Authenticate users and generate JWT tokens
- ✅ **JWT Token Verification** - Validate JWT tokens
- ✅ **User Profile Management** - Retrieve, update user information
- ✅ **Password Management** - Change passwords with old password validation
- ✅ **Email Update** - Update user email with duplicate prevention
- ✅ **MongoDB Integration** - Persistent data storage
- ✅ **Spring Security** - Authentication filter and authorization

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.2**
- **Spring Security**
- **MongoDB**
- **JWT (JJWT 0.12.5)**
- **Lombok**
- **MapStruct**

## Prerequisites

- Java 17+
- Maven 3.6+
- MongoDB (running locally or remote)

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd et-user-auth-service
```

### 2. Configure MongoDB
Update `src/main/resources/application.yml`:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/Authentication-Service
```

### 3. Build the Project
```bash
./mvnw clean install -DskipTests
```

### 4. Start MongoDB
```bash
brew services start mongodb-community
```

### 5. Run the Application
```bash
./mvnw spring-boot:run
```

The service will start on `http://localhost:8080`

# API Documentation

### Base URL
```
http://localhost:8080
```

---

## Authentication Endpoints (Public)

### 1. Sign Up
**Create a new user account**

```http
POST /api/auth/signup
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "Password123!"
}
```

**Response (201 Created):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNzcxNzI4MzM2LCJleHAiOjE3NzE4MTQ3MzZ9...",
  "expiresIn": 86400000
}
```

---

### 2. Login
**Authenticate user and get JWT token**

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "Password123!"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresIn": 86400000
}
```

**Error (401 Unauthorized):**
```
User not found OR Invalid password
```

---

### 3. Verify Token
**Validate JWT token**

```http
POST /api/auth/verify?token=YOUR_JWT_TOKEN_HERE
```

**Response (200 OK):**
```
true
```

---

## User Management Endpoints (Protected - Requires JWT)

> **Note:** All endpoints below require `Authorization: Bearer <JWT_TOKEN>` header

### 4. Get User Info
**Retrieve user profile**

```http
GET /api/users/{userId}
Authorization: Bearer YOUR_JWT_TOKEN
```

**Response (200 OK):**
```json
{
  "id": "699a6dd0b8b2d865db5dc4ac",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "createdAt": 1771728336255,
  "updatedAt": 1771728336255
}
```

**Error (404 Not Found):**
```
User not found
```

---

### 5. Change Password
**Update user password**

```http
POST /api/users/{userId}/change-password
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "oldPassword": "Password123!",
  "newPassword": "NewPassword456!"
}
```

**Response (200 OK):**
```
Password changed successfully
```

**Error (400 Bad Request):**
```
Old password is incorrect
```

---

### 6. Update User Info
**Update user name or email**

```http
PUT /api/users/{userId}/update
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Smith",
  "type": "NAME"
}
```

**Or update email:**

```http
PUT /api/users/{userId}/update
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "email": "jane.smith@example.com",
  "type": "EMAIL"
}
```

**Response (200 OK):**
```json
{
  "id": "699a6dd0b8b2d865db5dc4ac",
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "createdAt": 1771728336255,
  "updatedAt": 1771728337000
}
```

**Error (400 Bad Request):**
```
Email already exists
```

---

## Complete Postman Testing Guide

### Step 1: Import Requests in Postman

Create a new **Collection** called `ET User Auth Service` and add these requests.

### Step 2: Sign Up a New User

```
POST http://localhost:8080/api/auth/signup
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "Password123!"
}
```

**Copy the `accessToken` from response** - you'll use it for protected endpoints

### Step 3: Get User ID from MongoDB

```bash
mongosh
use et_user_auth_service
db.users.find()
```

**Copy the `_id` value** (e.g., `699a6dd0b8b2d865db5dc4ac`)

### Step 4: Get User Info (Protected)

```
GET http://localhost:8080/api/users/699a6dd0b8b2d865db5dc4ac
```

**In Postman:**
1. Go to **Authorization** tab
2. Select **Bearer Token** from dropdown
3. Paste your `accessToken`
4. Send request

### Step 5: Change Password (Protected)

```
POST http://localhost:8080/api/users/699a6dd0b8b2d865db5dc4ac/change-password
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "oldPassword": "Password123!",
  "newPassword": "NewPassword456!"
}
```

### Step 6: Update User Name (Protected)

```
PUT http://localhost:8080/api/users/699a6dd0b8b2d865db5dc4ac/update
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Smith",
  "type": "NAME"
}
```

### Step 7: Update User Email (Protected)

```
PUT http://localhost:8080/api/users/699a6dd0b8b2d865db5dc4ac/update
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "email": "jane.smith@example.com",
  "type": "EMAIL"
}
```

### Step 8: Login Again with New Password

```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "jane.smith@example.com",
  "password": "NewPassword456!"
}
```

### Step 9: Verify Token

```
POST http://localhost:8080/api/auth/verify?token=YOUR_JWT_TOKEN
```

---

## Project Structure

```
src/main/java/in/codingstreams/authentication-service/
├── config/
│   └── SecurityConfig.java          # Spring Security configuration
├── controller/
│   ├── AuthController.java          # Authentication endpoints
│   └── UserController.java          # User management endpoints
├── dto/
│   ├── AuthRequest.java             # Sign-up/Login request
│   ├── AuthResponse.java            # Token response
│   ├── UserDto.java                 # User info response
│   ├── UpdateUserRequest.java       # Update request
│   └── ChangePasswordRequest.java   # Password change request
├── entity/
│   └── User.java                    # MongoDB document
├── enums/
│   └── UpdateRequestType.java       # NAME or EMAIL update type
├── filter/
│   └── JwtAuthFilter.java           # JWT authentication filter
├── repository/
│   └── UserRepository.java          # MongoDB repository
├── service/
│   ├── AuthService.java             # Authentication logic
│   └── UserService.java             # User management logic
├── util/
│   └── JwtUtils.java                # JWT token utilities
└── EtUserAuthServiceApplication.java
```

---

## Configuration

### JWT Secret & Expiration

Update `src/main/resources/application.yml`:

```yaml
jwt:
  secret: your-secret-key-change-this-in-production-super-secret-key
  expiration: 86400000  # 24 hours in milliseconds
```

⚠️ **Change the secret in production!**

---

## Error Handling

| Status | Error | Resolution |
|--------|-------|-----------|
| 400 | Email already exists | Use a different email |
| 400 | Old password is incorrect | Verify old password |
| 400 | Email already exists (update) | Try different email |
| 401 | User not found | Check email in login |
| 401 | Invalid password | Check password |
| 403 | Unauthorized | Include valid JWT token |
| 404 | User not found | Check userId |

---

## Testing Checklist

- [ ] Sign up new user
- [ ] Get user info with token
- [ ] Change password successfully
- [ ] Update user name
- [ ] Update user email
- [ ] Login with new email
- [ ] Verify JWT token
- [ ] Test 403 without token
- [ ] Test 401 with wrong password
- [ ] Test 400 with duplicate email

---

## Security Features

✅ **Password Encryption** - BCrypt with salt  
✅ **JWT Authentication** - Stateless token-based auth  
✅ **CSRF Protection Disabled** - For API (not needed with JWT)  
✅ **Stateless Session** - No server-side sessions  
✅ **Email Validation** - No duplicate emails  
✅ **Old Password Validation** - Required for password change  

---

## Troubleshooting

### MongoDB Connection Failed
```bash
# Check if MongoDB is running
brew services list | grep mongodb

# Restart MongoDB
brew services restart mongodb-community
```

### Port 8080 Already in Use
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9
```

### JWT Token Expired
- Tokens expire after 24 hours
- Get new token by logging in again

### 403 Unauthorized on Protected Endpoints
- Ensure you added `Authorization: Bearer <token>` header
- Verify token is not expired
- Use token from login, not signup

---

## Contributors

- Viplav Khode
