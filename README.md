## 📦 Spring Boot 6 Template with Authentication (formLogin) and MariaDB

This project is a **starter template** for web applications built with **Spring Boot 6**, featuring:

-   User authentication via `formLogin` using Spring Security.
    
-   Integration with **MariaDB**
    
-   A clean, scalable architecture ready for further development.
    

----------

## 🚀 Technologies Used

-   ✅ Spring Boot 6
    
-   ✅ Spring Security
    
-   ✅ Spring Data JPA
    
-   ✅ MariaDB
    
-   ✅ Maven
    
-   ✅ Thymeleaf
      
----------

## ⚙️ Project Setup

### Requirements

-   Java 17 or higher
    
-   Maven 4.0.0
    
-   MariaDB Server
    
    



### Database

| Column             | Type          | Null | Key | Extra          |
|--------------------|---------------|------|-----|----------------|
| id                 | bigint(20)    | NO   | PRI | auto_increment |
| created_at         | datetime(6)   | NO   |     |                |
| password           | varchar(255)  | NO   |     |                |
| profile_image_path | varchar(255)  | YES  |     |                |
| role               | varchar(255)  | YES  |     |                |
| username           | varchar(255)  | NO   | UNI |                |


## 🔐 Authentication


This project uses **Spring Security** with form-based login to handle user authentication and authorization.

-   **Public pages:** The homepage, login, registration, error pages, and profile images are accessible to everyone.
    
-   **Login:** Users log in via a custom `/login` page.
    
-   **Access control:**
    
    -   Only users with the `ADMIN` role can access `/admin` pages.
        
    -   Logged-in users can access their profile and upload profile images.
        
-   **Logout:** Users can log out via `/logout`, which clears their session and cookies.
    
-   **Remember Me:** Users can stay logged in even after closing the browser if they choose “Remember Me”.
    
-   **Password security:** Passwords are securely hashed using BCrypt.

----------

## 📁 Project Structure

```
src
└── main
    ├── java
    │   └── com.marcptr.auth_template
    │       ├── controller
    │       ├── exceptions
    │       ├── model
    │       ├── repository
    │       ├── security
    │       ├── service
    │       └── AuthTemplateApplication.java
    └── resources
        ├── templates
	    │       ├── fragments 
	    │       ├── layouts    
	    │       └── pages
        └── application.properties

```
