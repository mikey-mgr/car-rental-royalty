# Complete Guide: Deploying Spring Boot + Vue.js to Render

This guide documents the **complete, tested process** for deploying a full-stack application (Spring Boot backend + Vue.js frontend) to Render, including all errors encountered and their solutions.

---

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Architecture Overview](#architecture-overview)
3. [Step-by-Step Deployment](#step-by-step-deployment)
4. [Common Errors & Solutions](#common-errors--solutions)
5. [Configuration Files Reference](#configuration-files-reference)
6. [Post-Deployment Checklist](#post-deployment-checklist)

---

## Prerequisites

### Required Accounts
- **GitHub account** with your repository
- **Render account** (free tier works)

### Local Setup
- Git installed and configured
- Your application running locally successfully

### Tech Stack Assumptions
- **Backend**: Spring Boot 3.x with Java 17
- **Frontend**: Vue.js 3.x with Vue Router
- **Database**: PostgreSQL (Render provides free tier)
- **Local DB**: MariaDB/MySQL (will be converted to PostgreSQL for production)

---

## Architecture Overview

### What Gets Deployed
```
┌─────────────────────────────────────────────────────────┐
│                    Render Platform                       │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  ┌──────────────────┐      ┌──────────────────┐        │
│  │  Frontend        │      │  Backend         │        │
│  │  Static Site     │─────▶│  Web Service     │        │
│  │  (Vue.js)        │ CORS │  (Spring Boot)   │        │
│  └──────────────────┘      └──────────────────┘        │
│                                     │                    │
│                                     ▼                    │
│                            ┌──────────────────┐         │
│                            │  PostgreSQL DB   │         │
│                            │  (Free Tier)     │         │
│                            └──────────────────┘         │
└─────────────────────────────────────────────────────────┘
```

### Service URLs
- **Backend**: `https://your-backend.onrender.com`
- **Frontend**: `https://your-frontend.onrender.com`
- **Database**: Internal connection (not publicly accessible)

---

## Step-by-Step Deployment

### Phase 1: Prepare Your Repository

#### 1.1 Create Deployment Branch
```bash
git checkout -b render-deployment
```

#### 1.2 Add Required Files

**File 1: `Dockerfile`** (for backend)
```dockerfile
# Multi-stage build for Spring Boot application

# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**File 2: `.dockerignore`**
```
target/
node_modules/
.git/
.gitignore
.mvn/
*.md
.vscode/
.settings/
*.log
*.tmp
.DS_Store
```

**File 3: `.npmrc`** (critical for Vue.js dependency resolution)
```
legacy-peer-deps=true
```

**File 4: `public/_redirects`** (for Vue Router history mode)
```
/*    /index.html   200
```

**File 5: `.env.production`** (for frontend API URL)
```
VUE_APP_API_URL=https://your-backend-name.onrender.com
```

**File 6: `render.yaml`** (Blueprint configuration)
```yaml
services:
  # Backend Service
  - type: web
    name: your-backend-name
    env: docker
    dockerfilePath: ./Dockerfile
    plan: free
    envVars:
      - key: JAVA_TOOL_OPTIONS
        value: -Xmx512m -Xms256m
      - key: SPRING_PROFILES_ACTIVE
        value: prod
      - key: SPRING_DATASOURCE_URL
        value: jdbc:postgresql://YOUR_DB_HOST:5432/YOUR_DB_NAME
      - key: SPRING_DATASOURCE_USERNAME
        value: YOUR_DB_USER
      - key: SPRING_DATASOURCE_PASSWORD
        sync: false
      - key: ALLOWED_ORIGINS
        value: https://your-frontend-name.onrender.com,http://localhost:8583

  # Frontend Static Site
  - type: web
    name: your-frontend-name
    env: static
    buildCommand: npm install --legacy-peer-deps && npm run build
    staticPublishPath: ./dist
    plan: free
    envVars:
      - key: VUE_APP_API_URL
        value: https://your-backend-name.onrender.com

databases:
  - name: your-database-name
    databaseName: your_db
    user: your_db_user
    plan: free
```

#### 1.3 Update Backend Configuration

**File: `pom.xml`** - Add PostgreSQL driver
```xml
<!-- PostgreSQL Driver for Production (Render) -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- MariaDB Driver for Local Development -->
<dependency>
    <groupId>org.mariadb.jdbc</groupId>
    <artifactId>mariadb-java-client</artifactId>
    <version>3.3.2</version>
    <scope>runtime</scope>
</dependency>

<!-- Actuator for health checks -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**File: `src/main/resources/application-prod.properties`**
```properties
# Production Configuration for Render
server.port=${PORT:8080}

# Database Configuration - Set via environment variables
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

# Production optimizations
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
logging.level.org.hibernate.SQL=WARN
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=WARN

# CORS Configuration
management.endpoints.web.cors.allowed-methods=POST, GET, PUT, DELETE, OPTIONS

# Security
spring.security.require-ssl=false

# Actuator for health checks
management.endpoints.web.exposure.include=health
management.endpoint.health.show-details=when-authorized
```

**File: `src/main/java/com/.../config/MyCorsConfiguration.java`**
```java
package com.yourpackage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyCorsConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                String allowedOrigins = System.getenv()
                    .getOrDefault("ALLOWED_ORIGINS", "http://localhost:8583");
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .allowedOrigins(allowedOrigins.split(","))
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
```

**CRITICAL FIX**: If you have JPA entities with reserved PostgreSQL keywords (like `user`), use `@Column` annotation:
```java
@Entity
public class YourEntity {
    @Column(name = "user_name")  // NOT just "user"
    private String user;
}
```

#### 1.4 Update Frontend Configuration

**File: `src/App.vue`** - Update baseURL to use environment variable
```javascript
export default {
  data() {
    return {
      baseURL: process.env.VUE_APP_API_URL || "http://localhost:8081",
      // ... rest of your data
    }
  }
}
```

#### 1.5 Commit and Push
```bash
git add .
git commit -m "Add Render deployment configuration"
git push origin render-deployment
```

---

### Phase 2: Deploy Database

#### 2.1 Create PostgreSQL Database
1. Go to [Render Dashboard](https://dashboard.render.com)
2. Click **"New +"** → **"PostgreSQL"**
3. Configure:
   - **Name**: `your-database-name`
   - **Database**: `your_db`
   - **User**: `your_db_user`
   - **Region**: Choose closest to you
   - **Plan**: **Free**
4. Click **"Create Database"**

#### 2.2 Get Database Connection Details
1. Once created, go to database **"Info"** page
2. Note down:
   - **Internal Database URL**: `postgresql://user:pass@host:5432/dbname`
   - **Hostname**: `dpg-xxxxx-a`
   - **Port**: `5432`
   - **Database**: `your_db_qnd8`
   - **Username**: `your_db_user`
   - **Password**: `[long random string]`

---

### Phase 3: Deploy Backend

#### 3.1 Create Web Service
1. Dashboard → **"New +"** → **"Web Service"**
2. Connect your GitHub repository
3. Select branch: `render-deployment`
4. Configure:
   - **Name**: `your-backend-name`
   - **Region**: Same as database
   - **Branch**: `render-deployment`
   - **Runtime**: **Docker**
   - **Plan**: **Free**

#### 3.2 Set Environment Variables
Click **"Advanced"** → **"Add Environment Variable"**

Add these variables:

| Key | Value |
|-----|-------|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `JAVA_TOOL_OPTIONS` | `-Xmx512m -Xms256m` |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://[HOST]:5432/[DATABASE]` |
| `SPRING_DATASOURCE_USERNAME` | `[DB_USERNAME]` |
| `SPRING_DATASOURCE_PASSWORD` | `[DB_PASSWORD]` |
| `ALLOWED_ORIGINS` | `https://your-frontend-name.onrender.com,http://localhost:8583` |

**CRITICAL**: Replace placeholders with actual values from Phase 2.2

Example:
```
SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-xxxxx-a:5432/your_db_qnd8
SPRING_DATASOURCE_USERNAME=your_db_user
SPRING_DATASOURCE_PASSWORD=N2jEhFAY8HvTfWFkRf57rmBndwkwoSKl
```

#### 3.3 Deploy
1. Click **"Create Web Service"**
2. Wait for build (5-10 minutes first time)
3. Monitor logs for errors

#### 3.4 Verify Backend is Running
- Check logs for: `Started [YourApp] in X seconds`
- Visit: `https://your-backend-name.onrender.com/actuator/health`
- Should return: `{"status":"UP"}`

---

### Phase 4: Deploy Frontend

#### 4.1 Create Static Site
1. Dashboard → **"New +"** → **"Static Site"**
2. Connect your GitHub repository
3. Select branch: `render-deployment`
4. Configure:
   - **Name**: `your-frontend-name`
   - **Branch**: `render-deployment`
   - **Build Command**: `npm install --legacy-peer-deps && npm run build`
   - **Publish Directory**: `dist`
   - **Plan**: **Free**

#### 4.2 Set Environment Variables
Click **"Advanced"** → **"Add Environment Variable"**

| Key | Value |
|-----|-------|
| `VUE_APP_API_URL` | `https://your-backend-name.onrender.com` |

**IMPORTANT**: This must be set BEFORE the first build!

#### 4.3 Deploy
1. Click **"Create Static Site"**
2. Wait for build (3-5 minutes)
3. Monitor logs for errors

#### 4.4 Verify Frontend is Running
- Visit: `https://your-frontend-name.onrender.com`
- Check browser console for errors
- Test API calls (login, fetch data, etc.)

---

## Common Errors & Solutions

### Error 1: "Driver org.postgresql.Driver claims to not accept jdbcUrl"

**Symptom**: Backend fails to start with database connection error

**Cause**: Missing `jdbc:` prefix or incorrect URL format

**Solution**:
```properties
# WRONG
spring.datasource.url=postgresql://host:5432/db

# CORRECT
spring.datasource.url=jdbc:postgresql://host:5432/db
```

Also ensure `SPRING_DATASOURCE_URL` environment variable is set correctly in Render.

---

### Error 2: "ERROR: syntax error at or near 'user'"

**Symptom**: Hibernate DDL fails with SQL syntax error

**Cause**: Using PostgreSQL reserved keywords as column names

**Solution**: Use `@Column` annotation with explicit name
```java
// WRONG
@JoinColumn(name = "user")
private String user;

// CORRECT
@Column(name = "user_name")
private String user;
```

**Common reserved keywords**: `user`, `order`, `group`, `table`, `select`, `where`

---

### Error 3: "npm error ERESOLVE could not resolve"

**Symptom**: Frontend build fails with dependency conflicts

**Cause**: Vue 3 + Bootstrap Vue 2 peer dependency conflicts

**Solution**: Create `.npmrc` file in project root
```
legacy-peer-deps=true
```

This tells npm to ignore peer dependency conflicts.

---

### Error 4: "Access-Control-Allow-Origin header is present"

**Symptom**: Frontend can't call backend APIs, CORS errors in browser console

**Cause**: Backend not configured to allow frontend origin

**Solution**:
1. Add `ALLOWED_ORIGINS` environment variable to backend:
   ```
   ALLOWED_ORIGINS=https://your-frontend-name.onrender.com,http://localhost:8583
   ```

2. Update CORS configuration to read from environment:
   ```java
   String allowedOrigins = System.getenv()
       .getOrDefault("ALLOWED_ORIGINS", "http://localhost:8583");
   registry.addMapping("/**")
           .allowedOrigins(allowedOrigins.split(","))
           .allowedHeaders("*")
           .allowCredentials(true);
   ```

3. **Redeploy backend** after adding environment variable

---

### Error 5: "404 Not Found" on Vue Router routes

**Symptom**: Direct navigation to `/home` or other routes returns 404

**Cause**: Static site doesn't know how to handle client-side routing

**Solution**: Create `public/_redirects` file
```
/*    /index.html   200
```

This tells Render to serve `index.html` for all routes, letting Vue Router handle navigation.

---

### Error 6: "Waiting for internal health check... port 10000"

**Symptom**: Backend deploys but Render can't detect it's running

**Cause**: App not binding to correct port or actuator not enabled

**Solution**:
1. Ensure `server.port=${PORT:8080}` in `application-prod.properties`
2. Add actuator dependency to `pom.xml`
3. Remove explicit `healthCheckPath` from `render.yaml` (let Render auto-detect)

---

### Error 7: "process is not defined" in browser console

**Symptom**: Environment variables not available in Vue app

**Cause**: Environment variables not set at build time

**Solution**:
1. Ensure `VUE_APP_API_URL` is set in Render environment variables
2. Verify it's set BEFORE triggering build
3. Trigger manual redeploy after adding variable
4. Use fallback in code:
   ```javascript
   baseURL: process.env.VUE_APP_API_URL || "http://localhost:8081"
   ```

---

### Error 8: Backend runs locally but fails on Render

**Symptom**: Works with MariaDB locally, fails with PostgreSQL on Render

**Cause**: Database dialect differences

**Solution**:
1. Add PostgreSQL driver to `pom.xml`
2. Create separate `application-prod.properties` with PostgreSQL dialect
3. Set `SPRING_PROFILES_ACTIVE=prod` in Render
4. Test locally with PostgreSQL before deploying

---

## Configuration Files Reference

### Complete File Structure
```
your-project/
├── .dockerignore
├── .env.production
├── .npmrc
├── Dockerfile
├── render.yaml
├── pom.xml
├── package.json
├── public/
│   └── _redirects
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/yourpackage/
│   │   │       └── config/
│   │   │           └── MyCorsConfiguration.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-prod.properties
│   ├── App.vue
│   └── main.js
└── README.md
```

### Environment Variables Summary

**Backend (Web Service)**:
```
SPRING_PROFILES_ACTIVE=prod
JAVA_TOOL_OPTIONS=-Xmx512m -Xms256m
SPRING_DATASOURCE_URL=jdbc:postgresql://[HOST]:5432/[DB]
SPRING_DATASOURCE_USERNAME=[USER]
SPRING_DATASOURCE_PASSWORD=[PASSWORD]
ALLOWED_ORIGINS=https://[FRONTEND].onrender.com,http://localhost:8583
```

**Frontend (Static Site)**:
```
VUE_APP_API_URL=https://[BACKEND].onrender.com
```

---

## Post-Deployment Checklist

### Backend Verification
- [ ] Service shows "Live" status in Render dashboard
- [ ] Logs show "Started [App] in X seconds"
- [ ] Health endpoint returns 200: `/actuator/health`
- [ ] Database tables created automatically (check logs)
- [ ] No CORS errors in backend logs

### Frontend Verification
- [ ] Service shows "Live" status in Render dashboard
- [ ] Site loads without 404 errors
- [ ] Static assets (images, CSS) load correctly
- [ ] Vue Router navigation works (no 404 on refresh)
- [ ] Browser console shows no errors

### Integration Verification
- [ ] Frontend can call backend APIs
- [ ] No CORS errors in browser console
- [ ] Login/authentication works
- [ ] Data fetching works
- [ ] Create/update operations work

### Performance Notes
- **Cold Start**: First request after 15 min inactivity takes ~30 seconds
- **Database**: Free tier has 1GB storage, 90-day expiration
- **Build Time**: Backend ~5-10 min, Frontend ~3-5 min
- **Memory**: 512MB RAM limit on free tier

---

## Troubleshooting Tips

### Check Backend Logs
```
Render Dashboard → Your Backend Service → Logs
```
Look for:
- Database connection errors
- Port binding issues
- Application startup errors
- CORS configuration logs

### Check Frontend Logs
```
Render Dashboard → Your Frontend Service → Logs
```
Look for:
- Build errors
- Dependency resolution issues
- Environment variable warnings

### Check Browser Console
```
F12 → Console Tab
```
Look for:
- CORS errors
- Network errors (failed API calls)
- 404 errors on routes
- Environment variable issues

### Force Redeploy
If changes don't take effect:
1. Go to service in Render Dashboard
2. Click **"Manual Deploy"** → **"Clear build cache & deploy"**

### Test Locally with Production Config
```bash
# Backend
SPRING_PROFILES_ACTIVE=prod ./mvnw spring-boot:run

# Frontend
npm run build
npx serve -s dist
```

---

## Cost Breakdown (Free Tier)

| Service | Free Tier Limits | Cost After Limit |
|---------|------------------|------------------|
| Web Service (Backend) | 750 hours/month | $7/month |
| Static Site (Frontend) | Unlimited | Free |
| PostgreSQL Database | 1GB, 90 days | $7/month |

**Total Free**: Enough for development/testing
**Upgrade Needed**: For production with uptime requirements

---

## Next Steps

### Custom Domain (Optional)
1. Go to service → **"Settings"** → **"Custom Domain"**
2. Add your domain
3. Update DNS records as instructed
4. Update `ALLOWED_ORIGINS` to include custom domain

### Monitoring
1. Enable email notifications for deploy failures
2. Set up uptime monitoring (e.g., UptimeRobot)
3. Monitor database storage usage

### Security Hardening
1. Change default admin credentials
2. Enable HTTPS-only (Render does this automatically)
3. Add rate limiting
4. Review CORS allowed origins
5. Enable Spring Security CSRF protection

---

## Summary

This guide covered:
✅ Complete file structure and configuration
✅ Step-by-step deployment process
✅ All common errors and solutions
✅ CORS configuration (the biggest pain point)
✅ Database migration (MariaDB → PostgreSQL)
✅ Environment variable management
✅ Vue Router configuration for static hosting
✅ Docker containerization for Spring Boot

**Key Takeaways**:
1. **CORS must be configured correctly** - most common issue
2. **Environment variables must be set before build** - especially for frontend
3. **PostgreSQL reserved keywords** - use explicit column names
4. **`.npmrc` is critical** - for Vue.js dependency resolution
5. **`_redirects` file is required** - for Vue Router history mode

Good luck with your deployment! 🚀
