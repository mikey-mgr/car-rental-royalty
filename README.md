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

## Deploying to Render

Since GitHub Pages doesn't support backend applications, you can deploy this full-stack app to Render for free.

### Prerequisites
- GitHub account
- Render account (sign up at https://render.com)
- Push the `render-deployment` branch to GitHub

### Quick Deployment (Using render.yaml)

1. **Push the deployment branch to GitHub:**
   ```bash
   git add .
   git commit -m "Add Render deployment configuration"
   git push origin render-deployment
   ```

2. **Deploy on Render:**
   - Go to https://dashboard.render.com
   - Click "New +" → "Blueprint"
   - Connect your GitHub repository
   - Select the `render-deployment` branch
   - Render will automatically create:
     - Web Service (Spring Boot backend)
     - Database (PostgreSQL/MySQL)

3. **Configure CORS:**
   - After deployment, update `application-prod.properties` with your frontend URL
   - Redeploy to apply changes

### Manual Deployment

1. **Create Database:**
   - Dashboard → "New +" → "PostgreSQL"
   - Name: `apex-car-rental-db`
   - Plan: Free
   - Save the connection string

2. **Create Web Service:**
   - Dashboard → "New +" → "Web Service"
   - Connect repository, select `render-deployment` branch
   - **Build Command:** `./mvnw clean install -DskipTests`
   - **Start Command:** `java -jar target/Apex-0.0.1-SNAPSHOT.jar`
   - **Environment Variables:**
     ```
     SPRING_PROFILES_ACTIVE=prod
     SERVER_PORT=8080
     DATABASE_URL=<your-database-connection-string>
     JAVA_TOOL_OPTIONS=-Xmx512m -Xms256m
     ```

### Important Notes

- **Free Tier:** Service spins down after 15 minutes of inactivity (30s cold start)
- **Database:** Render offers free PostgreSQL (1GB, 90-day limit)
- **Memory:** 512MB RAM on free tier
- **Backend URL:** `https://apex-car-rental-backend.onrender.com`

### Troubleshooting

- **Build fails:** Check Java version (17) and build logs
- **Database issues:** Verify `DATABASE_URL` environment variable
- **Won't start:** Check memory settings and application logs

## Additional Information

- API Documentation: Available via Swagger UI when backend is running
- Frontend Configuration: See [Vue CLI Configuration Reference](https://cli.vuejs.org/config/)
