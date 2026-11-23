# Blogify

A RESTful blogging backend built with **Java 17** and **Spring Boot**. It supports users, posts (with image upload), and tags, with paginated and sortable listings, post search, email notifications, and Spring Security authentication.

## Tech stack

- Java 17, Spring Boot 3.4
- Spring Web (REST APIs), Spring Data JPA / Hibernate, Bean Validation
- Spring Security (HTTP Basic, BCrypt password hashing, stateless sessions)
- MySQL
- Spring Mail (Gmail SMTP), JPA auditing, async and scheduling support
- Maven, Lombok, Jackson

## Architecture

The project follows a layered **controller → service → repository** structure:

- `controllers/` handle HTTP requests and responses
- `services/` contain business logic (posts, users, tags, images, email)
- `repositories/` use Spring Data JPA for database access
- `dtos/` keep the API request/response models separate from the JPA entities
- `customException/` returns consistent validation error responses

## API overview

All endpoints require authentication (HTTP Basic). With the `dev` profile, the base URL is `http://localhost:8081/dev/api/Blogify`.

| Method | Endpoint | Description |
|---|---|---|
| GET | `/posts?page=0&size=2&sortby=id&sortdirection=ASC` | List posts (paginated, sortable) |
| GET | `/posts/{id}` | Get a post by ID |
| GET | `/posts/search?valve=...` | Search posts |
| GET | `/posts/title?value=...` | Find posts by title |
| GET | `/posts/tag?value=...` | Find posts by tag name |
| POST | `/posts/user/{userId}` | Create a post with an image (multipart: `post` JSON + `image` file) |
| PUT / PATCH | `/posts/{id}/user/{userId}` | Update a post (full / partial) |
| DELETE | `/posts/{id}/user/{userId}` | Delete a post |
| GET | `/Users?page=0&size=2` | List users (paginated) |
| GET | `/Users/{id}` | Get a user |
| POST | `/Users` | Create a user |
| POST | `/Users/{id}` | Update a user |
| DELETE | `/Users/{id}` | Delete a user |
| GET / POST | `/tags` | List / create tags |
| PUT | `/tags/{tagname}` | Update a tag |
| DELETE | `/tags/{id}` | Delete a tag |
| GET | `/email/sent?to=...&subject=...&body=...` | Send an email |

## Running locally

### Prerequisites

- Java 17+
- MySQL 8 running locally
- (Optional) A Gmail account with an [app password](https://myaccount.google.com/apppasswords) for the email feature

### 1. Create the database

```sql
CREATE DATABASE bloghive_dev;
```

### 2. Set environment variables

Credentials are read from environment variables and are never stored in the repository.

**macOS / Linux**
```bash
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
export MAIL_USERNAME=your_email@gmail.com
export MAIL_PASSWORD=your_gmail_app_password
```

**Windows (PowerShell)**
```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
$env:MAIL_USERNAME="your_email@gmail.com"
$env:MAIL_PASSWORD="your_gmail_app_password"
```

### 3. Run the application

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

The API will be available at `http://localhost:8081/dev/api/Blogify`.

### Profiles

| Profile | Port | Database |
|---|---|---|
| `dev` | 8081 | `bloghive_dev` |
| `test` | 8082 | `bloghive_test` |
| `prod` | 8083 | `bloghive_prod` |
