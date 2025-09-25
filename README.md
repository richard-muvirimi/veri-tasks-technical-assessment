# Task Manager - Full Stack Application

A simple, single-user task management application built with Spring Boot (Kotlin) backend and Angular frontend, featuring JWT authentication and CRUD operations for task management.

## Prerequisites

- [Git](https://git-scm.com/)
- [Docker](https://www.docker.com/get-started)

## Getting Started

### Cloning the Repository

This project uses Git submodules for frontend assets. Clone with submodules:

```bash
git clone --recurse-submodules https://github.com/richard-muvirimi/veri-tasks-technical-assessment.git
cd veri-tasks-technical-assessment
```

If already cloned without submodules:

```bash
git submodule update --init --recursive
```

### Running the Application

Start all services with Docker:

```bash
docker compose up --build
```

### Access Points

- **Frontend**: <http://localhost:4200>
- **Backend API**: <http://localhost:8081>
- **H2 Database Console**: <http://localhost:8082>

## Configuration

### Backend URL

The Angular frontend connects to the Spring Boot backend via the `BACKEND_URL` environment variable. In the Docker setup, this is automatically configured to `http://localhost:8081`.

If you need to override the backend URL (e.g., for local development or different environments), you can:

1. **Using Environment File**: Copy `.env.example` to `.env` in the project root and set the server URLs as needed:

   ```bash
   cp .env.example .env
   # Then edit .env to set BACKEND_URL, FRONTEND_URL, etc.
   ```

## Docker Commands

```bash
# Start services
docker compose up --build

# Start in background
docker compose up --build -d

# Stop services
docker compose down

# View logs
docker compose logs

# Rebuild without cache
docker compose build --no-cache
```

For additional Docker commands and troubleshooting, refer to the [Docker documentation](https://docs.docker.com/).

## Contact

For questions or issues, contact [richard-muvirimi](https://github.com/richard-muvirimi) or visit [https://tyganeutronics.com](https://tyganeutronics.com).
