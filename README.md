# Spring Boot JWT RBAC Project

This project demonstrates **JWT authentication with role-based authorization** in Spring Boot.

## Tech stack
- Java 17
- Spring Boot 3
- Spring Security
- JJWT

## Default users
- `user` / `user123` → role `ROLE_USER`
- `admin` / `admin123` → role `ROLE_ADMIN`

## Run
```bash
mvn spring-boot:run
```

## Login and get token
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

## Access protected API
```bash
curl http://localhost:8080/api/admin/hello \
  -H "Authorization: Bearer <YOUR_TOKEN>"
```

## Push this project to GitHub
```bash
git remote add origin https://github.com/<your-username>/springboot-jwt-rbac.git
git branch -M main
git push -u origin main
```
