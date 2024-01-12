Library Management System (LMS) - Spring Boot Application
Introduction
Welcome to the documentation for the Library Management System (LMS) Spring Boot Application. This document provides instructions on how to run the application, interact with API endpoints, and use authentication features implemented using JWT (JSON Web Token).
1. Running the Application
git clone https://github.com/your-username/lms-spring.git
cd lms-spring
http://localhost:8080.

2. Interacting with API Endpoints
API Documentation
For detailed information about available API endpoints and their usage, please refer to the provided Postman collection:
lms_spring.postman_collection.json
Import this collection into Postman to explore and test the API endpoints interactively.







3. Authentication
JWT Authentication
The application uses JSON Web Token (JWT) for authentication. To obtain an access token, follow these steps:
POST /api/register
{
    "displayName": "test",
    "email": "test@test.com",
    "password": "Pa$$w0rd"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ5b3VyX3VzZXJuYW1lIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYyMzk2MjJ9.nIuERZDFCMiD8DKBbHuJh25G2DX6gB8f4THGCV4Y9Hw"
}

4. Testing
Running Tests
The application includes unit tests using JUnit 5 and Mockito.
