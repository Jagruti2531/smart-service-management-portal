# Smart Service Management Portal — React SPA + Spring Boot

## Stack
- React + Vite + Axios + Lucide React
- Spring Boot + Spring Data JPA + Spring Security
- H2 for local development (MySQL can be configured later)
- Email OTP authentication architecture

## Roles
- CUSTOMER: self-registers, creates tickets, sees only own tickets and history.
- EMPLOYEE: sees only assigned tickets and can update their status.
- ADMIN: sees all tickets, assigns employees, monitors users and reports.

## Run backend in Eclipse
Run `SmartServiceApplication.java` as Java Application. Backend: http://localhost:8080

## Run React frontend
Open a terminal in `frontend`:
```bash
npm install
npm run dev
```
Open http://localhost:5173

## OTP testing
During local development the OTP is printed in the Spring Boot/Eclipse console. The `EmailService` is intentionally isolated so it can be replaced/configured with Gmail SMTP or AWS SES without changing authentication logic.

## Demo accounts
- Admin: admin@smartservice.com
- Employee: employee@smartservice.com
- Customer: customer@smartservice.com

If these accounts are not present in your local database, seed/create them as users with the corresponding roles. Public registration always creates CUSTOMER accounts; users cannot choose ADMIN or EMPLOYEE during registration.
