# User-Service: Simple Authentication and Authorization with JWT

This project implements a simple authentication and authorization mechanism using **JSON Web Token (JWT)** and **Spring Security**. The application is built with **Gradle** as the build tool and follows Spring Boot conventions.

---

## Features

1. **Authentication and Authorization**:
   - Secured APIs using JWT tokens.
   - Role-based access control.

2. **JWE Integration**:
   - Implements **JSON Web Encryption (JWE)** for added security.

3. **Spring Security**:
   - Provides secure API endpoints with authentication and authorization filters.

---

## Pending Implementation

The following features are planned for future implementation:

### Admin Features
1. **Manage Users APIs**:
   - Grant permissions.
   - Remove permissions.
   - Add, delete, or edit users.

2. **Create Custom Roles APIs**:
   - Role creation functionality limited to admins or those with the "Role Creator" entitlement.

3. **Additional Business Logic**:
   - Further business requirements and logic to be added as needed.



## Prerequisites

- **Java 21
- **Gradle** (if building manually)
- Any IDE (Preferred: IntelliJ IDEA)

---

## Setup and Usage

1. Clone the repository:
   ```bash
    git clone https://github.com/sudeep-hegde/user-service.git
    cd user-service
   ./gradlew build
