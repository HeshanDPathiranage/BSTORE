# BSTORE — Modern E-Commerce Platform

BSTORE is a modern, monolithic e-commerce application built with **Java 21** and **Spring Boot 3.5.7**. It integrates a responsive, single-page storefront rendered with **Thymeleaf** and **Tailwind CSS**, a robust **Spring Data JPA** backend supporting multiple database profiles (H2, MySQL, and PostgreSQL), and a server-side **session-based admin authentication** and inventory management system.

---

## 1. Tech Stack & Architecture Overview

* **Backend Framework:** Java 21 LTS, Spring Boot 3.5.7
* **Monolithic Architecture:** Spring Boot serves the frontend Single Page Application (SPA) at `/dashboard` (with `/` automatically redirecting to `/dashboard`), while exposing REST endpoints under `/api/products` and `/api/auth`.
* **Spring Boot Modules:**
  * `spring-boot-starter-web`: Embedded Tomcat server, Spring MVC controllers, and REST APIs.
  * `spring-boot-starter-data-jpa`: Hibernate ORM, Jakarta Persistence, and repository abstraction.
  * `spring-boot-starter-thymeleaf`: Monolithic server-side view template serving `src/main/resources/templates/dashboard.html`.
  * `spring-boot-starter-actuator`: Production health and metrics monitoring.
  * `spring-boot-devtools`: Hot reloading and rapid development tools.
  * `lombok`: Boilerplate reduction.
* **Frontend Tech Stack (`src/main/resources/templates/dashboard.html`):**
  * **HTML5 & Vanilla ES6+ JavaScript**: Client-side view routing, reactive DOM rendering, cart management, and asynchronous `fetch()` API calls.
  * **Tailwind CSS (CDN)**: Modern utility-first styling with custom brand palettes (`brand-50` to `brand-900`) and smooth transitions.
  * **Font Awesome 6.0**: Vector icons for navigation, shopping actions, badges, and status controls.
  * **Google Fonts (Inter)**: Clean modern typography.
* **Supported Database Engines:**
  * **H2 Database** (`dev` profile): In-memory embedded database for zero-setup local development.
  * **MySQL** (`mysql` profile): Production relational database support via `com.mysql:mysql-connector-j`.
  * **PostgreSQL** (`postgres` profile): Production relational database support via `org.postgresql:postgresql`.

---

## 2. Customer & Storefront Features

* **Hero Section & Festive Banners:**
  * Full-width hero banner with promotional call-to-actions.
  * Interactive canvas Christmas snow animation generated via dynamic JavaScript.
  * Announcement bar highlighting active shipping thresholds and sales.
* **Dynamic Product Catalog:**
  * Real-time product cards populated from `GET /api/products` (with offline fallback data).
  * Product hover zoom effects, price formatting (`$XX.XX`), category tagging, and automatic placeholder fallback for broken image URLs.
* **Product Badges & Stock Status:**
  * Color-coded marketing badges: `New` (blue), `Sale` (red), `Hot` / `Low Stock` (amber/dark).
  * **Sold-Out Handling**: Out-of-stock items automatically apply grayscale image filters, display a "Sold Out" badge, switch button to "Out of Stock", and prevent cart addition.
* **Category Filtering:**
  * Filter catalog instantly by category: `All`, `Men`, `Women`, and `Accessories`.
  * Smooth auto-scroll to the product showcase section upon selection.
* **Wishlist / Favorites:**
  * One-click heart toggle on each product card with animated state changes.
  * Wishlist counter badge in the navbar.
  * Dedicated **Wishlist View** displaying saved products with quick "Add to Cart" and "Remove" buttons.
* **Slide-Out Shopping Cart Drawer:**
  * Side drawer overlay with item counter badge.
  * Line-item controls: quantity increment (`+`), decrement (`-`), and item deletion.
  * Real-time calculated subtotal and empty-cart graphic.
* **Simulated Checkout & Payment Modal:**
  * Interactive credit card preview reflecting live order totals.
  * Cardholder name, card number, expiry, and CVV form validation.
  * Simulated checkout processing spinner, confirmation screen, and automatic cart reset.
* **Customer Account & Static Pages:**
  * Responsive navigation bar with mobile hamburger menu drawer.
  * **About View**: Company backstory, statistics, and core values.
  * **Account View**: Customer profile details and mock order history with delivery tracking statuses.

---

## 3. Admin & Product Management Features

* **Admin Authentication UI:**
  * Prominent **Admin Login** button in desktop and mobile navigation menus when logged out.
  * Clean login modal with error feedback (`#login-error-msg`), red field outlines, and auto-focusing on failed attempts.
* **Admin State & Persistence:**
  * Visual **Admin Indicator** (with a pulsing green status indicator) and direct **Logout** button in the header.
  * Client-side session verification via `GET /api/auth/status` on page load to ensure browser refreshes maintain authenticated status securely.
* **Storefront Management (Admin-Only):**
  * Prominent **"+ Add Product"** button displayed directly in the storefront `#new-arrivals` header toolbar.
  * Contextual admin hint banner confirming active administrative permissions.
  * Card-level quick **Edit** and **Delete** action buttons overlaid on product images.
* **Admin Dashboard & Inventory Table:**
  * Dedicated dashboard view (`#view-admin`) listing all products with thumbnails, name, category, price, stock status, and badge information.
  * Quick-switch button to return to "Store View" with one click.
* **Add / Edit Product Modal:**
  * Form supporting both product creation (`POST`) and updates (`PUT`).
  * Fields:
    * `name`: Product title (required text).
    * `category`: Text input with datalist suggestions (`Men`, `Women`, `Accessories`, `Unisex`).
    * `price`: Numeric price with two-decimal precision (required, `step="0.01"`).
    * `image`: URL text input with **real-time live image preview** and placeholder fallback.
    * `badge`: Optional text input with datalist suggestions (`New`, `Sale`, `Hot`, `Low Stock`).
    * `soldOut`: Checkbox toggle for stock status.
  * Submit button with loading spinner animation (`fa-circle-notch fa-spin`) during network calls.
* **Toast Notification System:**
  * Toast alerts in the bottom-right corner for operations (success, errors, updates, deletions, and auth state changes).

---

## 4. Authentication & Security Features

* **Server-Side Session Authentication (`src/main/java/com/example/BSTORE/controller/AuthController.java`):**
  * Powered by Spring's `HttpSession` (`JSESSIONID` cookie). Credentials are never stored or evaluated in client-side code.
* **Timing-Attack Resistance:**
  * Password and username comparison uses `MessageDigest.isEqual(...)` for constant-time byte verification.
* **Protected REST Endpoints:**
  * Public access: `GET /api/products` and `GET /api/products/{id}`.
  * Admin-only access: `POST /api/products`, `PUT /api/products/{id}`, and `DELETE /api/products/{id}` verify the active `HttpSession`. Unauthenticated requests receive `401 Unauthorized` (`{ "error": "Admin authentication required" }`).
* **Cross-Origin Resource Sharing (CORS) (`src/main/java/com/example/BSTORE/config/CorsConfig.java`):**
  * Configured with `.allowedOriginPatterns("*")`, standard HTTP methods (`GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`), and `.allowCredentials(true)` for cross-origin development support.

---

## 5. Configuration & Environment Variables

Credentials and database settings are configurable via environment variables or a `.env` file in the project root:

| Environment Variable | Description | Default Fallback Value |
| :--- | :--- | :--- |
| `ADMIN_USERNAME` | Admin account username | `admin` |
| `ADMIN_PASSWORD` | Admin account password | `admin123` |
| `DB_URL` | PostgreSQL connection JDBC URL | `jdbc:postgresql://localhost:5432/bstore` |
| `DB_USERNAME` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `postgres` |

---

## 6. REST API Endpoints Reference

| HTTP Method | Route | Access Level | Description | Request Body | Success Response |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **GET** | `/` | **Public** | Redirects browser to `/dashboard` | None | `302 Found` (redirect) |
| **GET** | `/dashboard` | **Public** | Renders storefront and admin dashboard template | None | `200 OK` (HTML) |
| **GET** | `/api/products` | **Public** | Retrieves all products | None | `200 OK` (`List<Product>`) |
| **GET** | `/api/products/{id}` | **Public** | Retrieves a single product by its database ID | None | `200 OK` (`Product`) or `404 Not Found` |
| **POST** | `/api/products` | **Admin Only** | Creates a new product in the database | `Product` JSON object | `200 OK` (saved `Product`) or `401 Unauthorized` |
| **PUT** | `/api/products/{id}` | **Admin Only** | Updates an existing product | `Product` JSON object | `200 OK` (updated `Product`), `401 Unauthorized`, or `404 Not Found` |
| **DELETE**| `/api/products/{id}` | **Admin Only** | Deletes a product by ID | None | `204 No Content` or `401 Unauthorized` |
| **POST** | `/api/auth/login` | **Public** | Validates admin credentials and creates session | `{ "username": "...", "password": "..." }` | `200 OK` (`{ "authenticated": true, "username": "..." }`) or `401 Unauthorized` |
| **GET** | `/api/auth/status` | **Public** | Returns the current session's authentication state | None | `200 OK` (`{ "authenticated": true/false, "username": "..." }`) |
| **POST** | `/api/auth/logout` | **Public** | Invalidates the active `HttpSession` | None | `200 OK` (`{ "authenticated": false }`) |

---

## 7. Database & Data Model (`Product`)

### Entity / Table Schema (`src/main/java/com/example/BSTORE/model/Product.java`)
* **Table Name:** `products`
* **Model Class:** `com.example.BSTORE.model.Product`

| Field / Attribute | Database Column | SQL Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- | :--- |
| `Long id` | `id` | `BIGINT` | Primary Key, `IDENTITY` / `BIGSERIAL` | Unique auto-generated identifier |
| `String name` | `name` | `VARCHAR(255)` | Not null / text | Product display title |
| `String category` | `category` | `VARCHAR(255)` | Standard string | Category (`Men`, `Women`, `Accessories`, etc.) |
| `Double price` | `price` | `DOUBLE PRECISION` / `DOUBLE` | Floating-point | Unit price |
| `String image` | `image` | `VARCHAR(1000)` | Length up to 1000 characters | Image URL supporting CDN links |
| `String badge` | `badge` | `VARCHAR(255)` | Nullable | Marketing badge text (`New`, `Sale`, `Hot`, etc.) |
| `boolean soldOut` | `sold_out` | `BOOLEAN` / `TINYINT(1)` | NOT NULL, Default `FALSE` | Stock status flag |

### Database Profiles & Initialization Scripts
* **H2 Profile (`dev`)**: Configured in `src/main/resources/application-dev.properties` using `jdbc:h2:~/bstore`.
* **MySQL Profile (`mysql`)**: Configured in `src/main/resources/application-mysql.properties` using `jdbc:mysql://localhost:3306/bstore`. Schema script located at `db/create-bstore.sql`.
* **PostgreSQL Profile (`postgres`)**: Configured in `src/main/resources/application-postgres.properties` using `${DB_URL}`. Schema script located at `db/create-bstore-postgres.sql`.

---

## 8. Run Instructions

### Option A — Run with H2 In-Memory Database (`dev`)
```powershell
# Using Maven Wrapper (Windows PowerShell)
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev

# Using standard Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Option B — Run with PostgreSQL (`postgres`)
1. Create the database and tables using the initialization script:
   ```powershell
   psql -U postgres -f db/create-bstore-postgres.sql
   ```
2. Start the application with the `postgres` profile:
   ```powershell
   # Using Maven Wrapper (Windows PowerShell)
   .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres

   # With custom environment variables
   $env:DB_URL="jdbc:postgresql://localhost:5432/bstore"
   $env:DB_USERNAME="postgres"
   $env:DB_PASSWORD="your_password"
   $env:ADMIN_USERNAME="admin"
   $env:ADMIN_PASSWORD="your_admin_password"
   .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
   ```

### Option C — Run with MySQL (`mysql`)
1. Create the database using the MySQL initialization script:
   ```powershell
   mysql -u root -p < db/create-bstore.sql
   ```
2. Start the application with the `mysql` profile:
   ```powershell
   .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=mysql
   ```

---

## 9. Verification & Health Check

After launching the application:
* **Web Storefront & Admin Portal:** Open [http://localhost:8080/](http://localhost:8080/) or [http://localhost:8080/dashboard](http://localhost:8080/dashboard).
* **CLI Endpoint Verification:**
  ```powershell
  # Check Web Server Status
  Invoke-WebRequest -UseBasicParsing http://localhost:8080/dashboard | Select-Object StatusCode

  # Check Public Products API
  Invoke-RestMethod -Uri http://localhost:8080/api/products

  # Check Auth Status API
  Invoke-RestMethod -Uri http://localhost:8080/api/auth/status
  ```
