# DeRoyalty Car Rental

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
   - Name: `DeRoyalty-car-rental-db`
   - Plan: Free
   - Save the connection string

2. **Create Web Service:**
   - Dashboard → "New +" → "Web Service"
   - Connect repository, select `render-deployment` branch
   - **Build Command:** `./mvnw clean install -DskipTests`
   - **Start Command:** `java -jar target/DeRoyalty-0.0.1-SNAPSHOT.jar`
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
- **Backend URL:** `https://deroyalty-car-rental-backend.onrender.com`

### Troubleshooting

- **Build fails:** Check Java version (17) and build logs
- **Database issues:** Verify `DATABASE_URL` environment variable
- **Won't start:** Check memory settings and application logs

## Additional Information

- API Documentation: Available via Swagger UI when backend is running
- Frontend Configuration: See [Vue CLI Configuration Reference](https://cli.vuejs.org/config/)


## Video Caching & Performance Optimization

### Problem
The home page background videos were re-downloading on every page visit, wasting bandwidth and causing slow load times.

### Solution Implemented
We've added multiple layers of caching to ensure videos are stored in the browser:

#### 1. Service Worker (Aggressive Caching)
- **Location:** `public/service-worker.js`
- **What it does:** Intercepts video requests and caches them permanently in the browser
- **Cache strategy:** Cache-first for videos (`.mp4`, `.webm`)
- **Benefits:** 
  - Videos load instantly after first visit
  - Works offline
  - Reduces server bandwidth

#### 2. HTTP Cache Headers
- **Location:** `public/_headers`
- **What it does:** Tells browsers and CDNs to cache videos for 1 year
- **Headers set:**
  ```
  Cache-Control: public, max-age=31536000, immutable
  ```
- **Benefits:**
  - Browser native caching
  - CDN caching (if using Cloudflare/similar)
  - No JavaScript required

#### 3. Service Worker Registration
- **Location:** `src/main.js`
- **What it does:** Registers the service worker on app load
- **Auto-activates:** Runs automatically when user visits the site

### How to Verify Caching Works

1. **First Visit:**
   - Open DevTools → Network tab
   - Visit home page
   - Videos will show "200 OK" (downloaded from server)

2. **Second Visit:**
   - Refresh the page
   - Videos will show "200 OK (from ServiceWorker)" or "(disk cache)"
   - Size column shows "(from cache)" or "0 B"

3. **Check Service Worker:**
   - DevTools → Application tab → Service Workers
   - Should show "activated and running"

### Cache Invalidation

If you update videos and need users to download new versions:

1. **Update cache version** in `public/service-worker.js`:
   ```javascript
   const VIDEO_CACHE = 'deroyalty-videos-v2'; // Change v1 to v2
   ```

2. **Rebuild and deploy:**
   ```bash
   npm run build
   ```

### File Sizes & Recommendations

- **Desktop video:** Keep under 5MB for reasonable first load
- **Mobile video:** Keep under 2MB (mobile users have limited data)
- **Format:** MP4 with H.264 codec (best browser support)
- **Compression:** Use tools like HandBrake to reduce file size without quality loss

### Production Deployment Notes

- Service worker works on **HTTPS only** (or localhost)
- Render automatically serves over HTTPS ✓
- Cache headers in `public/_headers` work with most static hosts
- For Render, cache headers are handled by the `serve` package

### Troubleshooting

**Videos still re-downloading?**
1. Check if service worker is registered (DevTools → Application)
2. Clear browser cache and reload twice
3. Verify HTTPS is being used (service workers require it)
4. Check console for service worker errors

**Service worker not activating?**
1. Make sure you're on HTTPS or localhost
2. Check for JavaScript errors in console
3. Try hard refresh (Ctrl+Shift+R / Cmd+Shift+R)

**Want to disable caching during development?**
1. DevTools → Application → Service Workers
2. Check "Bypass for network"
3. Or unregister the service worker temporarily


## SEO & Social Media Optimization

### What Was Added

Your site now has comprehensive SEO and social media sharing optimization:

#### 1. Open Graph Meta Tags (WhatsApp, Facebook, LinkedIn)
- **Title:** "DeRoyalty Car Rental - Premium Vehicle Rentals"
- **Description:** Business description with key services
- **Image:** Logo appears in link previews
- **URL:** Canonical URL for proper indexing

#### 2. Twitter Card Meta Tags
- Optimized for Twitter link previews
- Large image card format
- Proper title and description

#### 3. SEO Meta Tags
- **Primary meta tags:** Title, description, keywords
- **Robots:** Tells search engines to index and follow links
- **Language:** English
- **Author:** DeRoyalty Car Rental Group
- **Geo tags:** Zimbabwe, Harare location

#### 4. Structured Data (JSON-LD)
- **Schema.org markup** for Google
- Business type: AutoRental
- Service areas: 6 countries listed
- Contact information
- Aggregate rating (4.8/5)
- Helps Google show rich snippets in search results

#### 5. Sitemap (sitemap.xml)
- Lists all public pages
- Priority and update frequency for each page
- Helps search engines crawl efficiently

#### 6. Robots.txt
- Allows search engines to crawl public pages
- Blocks admin/cart/wishlist from indexing
- Points to sitemap location

### How Link Previews Work Now

When you share your site link on:

**WhatsApp:**
- Shows: Logo image
- Title: "DeRoyalty Car Rental - Premium Vehicle Rentals"
- Description: Business summary

**Facebook/LinkedIn:**
- Shows: Large logo image
- Title and description
- Clickable preview card

**Twitter:**
- Shows: Large image card
- Title and description
- Professional appearance

### Testing Link Previews

1. **Facebook Sharing Debugger:**
   - Go to: https://developers.facebook.com/tools/debug/
   - Enter your URL
   - Click "Scrape Again" to refresh cache

2. **Twitter Card Validator:**
   - Go to: https://cards-dev.twitter.com/validator
   - Enter your URL
   - See preview

3. **LinkedIn Post Inspector:**
   - Go to: https://www.linkedin.com/post-inspector/
   - Enter your URL
   - Clear cache if needed

4. **WhatsApp:**
   - Just paste the link in a chat
   - Preview appears automatically
   - May take 1-2 minutes for first time

### SEO Best Practices Implemented

✅ **Descriptive title tags** (under 60 characters)  
✅ **Meta descriptions** (under 160 characters)  
✅ **Relevant keywords** (car rental, luxury vehicles, etc.)  
✅ **Structured data** (helps Google understand your business)  
✅ **Mobile-friendly** (responsive design)  
✅ **Fast loading** (optimized assets, caching)  
✅ **HTTPS** (secure connection on Render)  
✅ **Sitemap** (helps search engines find pages)  
✅ **Robots.txt** (controls what gets indexed)  
✅ **Canonical URLs** (prevents duplicate content issues)  
✅ **Alt text on images** (accessibility + SEO)  
✅ **Semantic HTML** (proper heading hierarchy)  

### Improving SEO Further

**Content:**
- Add blog posts about car rental tips
- Create location-specific pages (Zimbabwe, UAE, etc.)
- Add customer testimonials
- Include FAQs

**Technical:**
- Submit sitemap to Google Search Console
- Set up Google Analytics
- Monitor page speed with Lighthouse
- Add more internal links between pages

**Off-Page:**
- Get listed on Google My Business
- Build backlinks from relevant sites
- Encourage customer reviews
- Share on social media regularly

### Google Search Console Setup

1. Go to: https://search.google.com/search-console
2. Add property: `deroyalty-car-rental-frontend.onrender.com`
3. Verify ownership (HTML file or meta tag)
4. Submit sitemap: `/sitemap.xml`
5. Monitor indexing status and search performance

### Current SEO Score Estimate

Based on implemented features:

- **Technical SEO:** 85/100 ✅
- **On-Page SEO:** 80/100 ✅
- **Mobile SEO:** 90/100 ✅
- **Social Sharing:** 95/100 ✅
- **Performance:** 75/100 ⚠️ (videos are large)

### What's Missing (Optional Improvements)

- Google Analytics tracking
- Google Tag Manager
- Schema markup for individual vehicles
- Breadcrumb navigation
- Blog/content section
- Customer reviews section
- Multi-language support
- AMP (Accelerated Mobile Pages)

### Customizing for Other Businesses

When adapting this template for other SMEs, update:

1. **public/index.html:**
   - Change title, description, keywords
   - Update business name and contact info
   - Modify structured data (business type, services)
   - Update logo image path

2. **public/sitemap.xml:**
   - Update domain URL
   - Add/remove pages as needed
   - Update lastmod dates

3. **public/robots.txt:**
   - Update sitemap URL
   - Adjust disallowed paths

4. **Logo/Images:**
   - Use `royalty-logo-dark.jpeg` (1200x630px recommended)
   - Optimize images for web (compress)
   - Add descriptive alt text

### Testing Checklist

After deployment:

- [ ] Share link on WhatsApp - preview shows?
- [ ] Share on Facebook - image and description appear?
- [ ] Google search: `site:yourdomain.com` - pages indexed?
- [ ] Check mobile responsiveness
- [ ] Test page load speed (Google PageSpeed Insights)
- [ ] Verify sitemap accessible: `/sitemap.xml`
- [ ] Verify robots.txt accessible: `/robots.txt`
- [ ] Check structured data: Google Rich Results Test
