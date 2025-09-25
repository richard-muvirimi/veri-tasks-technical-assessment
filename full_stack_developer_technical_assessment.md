# FULL STACK DEVELOPER – TECHNICAL ASSESSMENT

Project Brief: Simple Task Manager  
The goal is to build a simple, single-user task management application. The backend will handle the business logic and data persistence for tasks, while the frontend will provide a user interface for a registered user to manage their tasks.

Backend Requirements (Spring Boot)  
The backend should be a RESTful API built with Spring Boot. It will manage user authentication and CRUD operations for "Task" items.

1\. Data Model  
Create two main entities:  
User: Should have at least an id, username, and a securely hashed password.  
Task: Should have an id, title (e.g., "Deploy the application"), description (optional), and a status (e.g., PENDING, COMPLETED). Each task must be associated with a User.

2\. API Endpoints  
All endpoints under /api/tasks must be protected and accessible only by an authenticated user.

**Authentication Endpoints (Public):**  
 \- POST /auth/register: To register a new user.  
 \- POST /auth/login: To authenticate a user and return a JWT token.

**Task Endpoints (Protected):**  
 \- POST /api/tasks: Creates a new task for the logged-in user.  
 \- GET /api/tasks: Retrieves all tasks for the logged-in user.  
 \- GET /api/tasks/{id}: Retrieves a single task by its ID. The system should ensure the task belongs to the logged-in user.  
 \- PUT /api/tasks/{id}: Updates an existing task.  
 \- DELETE /api/tasks/{id}: Deletes a task.

3\. Security  
Implement authentication using Spring Security and JSON Web Tokens (JWT).  
The /auth/login endpoint should validate user credentials and, if successful, generate a JWT.  
All subsequent requests to protected endpoints (/api/\*\*) must include the JWT in the Authorization header (Bearer \<token\>).  
A security filter should validate the token on each request to a protected endpoint.

4\. Database & Logging  
**Database**: Use an in-memory database like H2 for simplicity. Configure it to be accessible via the Spring Boot application.  
**Logging**: Implement logging using SLF4J/LOG4J(included with Spring Boot). Log important events such as user registrations, logins, and critical errors.

Frontend Requirements (Angular)  
The frontend application should be a single-page application (SPA) built with Angular that consumes the backend API.

1\. Project Structure  
Use the Angular CLI to generate the project.  
Organize the code logically into modules, components, and services.  
Implement routing using RouterModule.

2\. User Authentication Flow  
Create a Login Page and a Register Page. These pages should be publicly accessible.  
Implement an AuthService to handle login and register API calls.  
Upon successful login, store the received JWT securely (e.g., in localStorage).  
Implement an HttpInterceptor to automatically attach the JWT to the headers of all outgoing API requests.  
Create a Route Guard (CanActivate) to protect the main task management dashboard from unauthenticated access. If a non-logged-in user tries to access it, they should be redirected to the login page.  
Include a Logout button that clears the stored JWT and redirects the user to the login page.

3\. Task Management UI  
Create a Dashboard/Tasks Page that is protected by the route guard.  
On this page, display the list of tasks belonging to the logged-in user by fetching data from the GET /api/tasks endpoint.  
Provide a form (e.g., in a modal or on the same page) to create a new task.  
Allow the user to update a task's details (e.g., change its status from PENDING to COMPLETED).  
Allow the user to delete a task. The UI should update immediately to reflect the change.  
Provide clear user feedback for all operations (e.g., loading indicators, success/error messages).

Evaluation Criteria  
Candidates will be evaluated on the following criteria:

\- Functionality: Does the application meet all the specified requirements? Do the CRUD and authentication features work correctly?  
\- Code Quality: Is the code clean, well-structured, readable, and maintainable? Are conventions for Java/Spring Boot and TypeScript/Angular followed?  
\- Backend Concepts: Correct implementation of the REST API, proper use of Spring Security for JWT authentication, and correct data modeling and persistence.  
\- Frontend Concepts: Proper use of Angular components, services, routing, and state management. Correct implementation of the authentication flow (interceptor, route guards).
