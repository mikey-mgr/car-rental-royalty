# Apex Car Rental

A full-stack e-commerce application for car rentals with Vue.js frontend and Spring Boot backend.

## Prerequisites

- Node.js and npm
- Java 17 or higher
- Maven
- MariaDB database

## Running the Application

### Backend (Spring Boot)

1. **Database Setup** - Choose one option:

   **Option A: MariaDB** (Production-like)
   - Install and start MariaDB on port 3306
   - Default credentials: username=`root`, password=`` (empty)
   - Database will be created automatically

   **Option B: H2 Database** (Quick Development)
   - Edit `src/main/resources/application.properties`
   - Comment out MariaDB configuration (lines starting with `spring.datasource` and `spring.jpa`)
   - Uncomment H2 configuration lines
   - No installation needed - H2 runs in-memory

2. Run the backend server:
```bash
mvnw spring-boot:run
```

The backend API will start on port **8081** at `http://localhost:8081`

   **Default Admin Account:**
   - Email: `admin@carrental.com`
   - Password: `admin123`
   - Created automatically on first startup
   - **Important**: Sign in with this account to access admin features
   - Change the password after first login for security

   **Admin Features:**
   After signing in as admin, you'll see an "ADMIN" dropdown in the navbar:
   - **Dashboard** - Overview of bookings, earnings, and vehicles
   - **Vehicles** - Manage vehicles (view all, add new, edit, delete)
   - **Categories** - Manage categories (view all, add new, edit, delete)
   - **Users** - View all registered users

### Frontend (Vue.js)

1. Install dependencies:
```bash
npm install --legacy-peer-deps
```

2. Run the development server:
```bash
npm run serve
```

The frontend will start on port 8583 at `http://localhost:8583`

### Production Build

To build the frontend for production:
```bash
npm run build
```

### Linting

To lint and fix files:
```bash
npm run lint
```

## Additional Information

- API Documentation: Available via Swagger UI when backend is running
- Frontend Configuration: See [Vue CLI Configuration Reference](https://cli.vuejs.org/config/)
