# Docker Compose Structure

This project uses Docker Compose's `extends` feature to eliminate code duplication between development and production environments.

## File Structure

- `docker-compose.common.yml` - Base configuration shared by all environments
- `docker-compose.yml` - Development environment configuration
- `docker-compose.prod.yml` - Production environment configuration

## How it Works

### Common Configuration (`docker-compose.common.yml`)
Contains shared configurations for all services:
- **postgres**: Database configuration with health checks
- **backend**: Spring Boot application with database connection
- **pgadmin**: Database administration tool
- **frontend**: Base frontend configuration
- **networks**: Shared network configuration

### Development Environment (`docker-compose.yml`)
Extends the common configuration and adds development-specific settings:
- **frontend**: Uses `Dockerfile.dev`, hot reloading, development ports
- **postgres**: Exposes port for local development
- **backend**: Development health checks, local volume mounts
- **pgadmin**: Exposes port for database administration

### Production Environment (`docker-compose.prod.yml`)
Extends the common configuration and adds production-specific settings:
- **frontend**: Uses production Dockerfile with nginx, production ports, restart policy
- **backend**: Production ports, persistent volume mounts, restart policy
- **postgres**: Persistent volume mounts
- **pgadmin**: Production port exposure

## Benefits

1. **DRY Principle**: No code duplication between environments
2. **Maintainability**: Changes to common configuration apply to all environments
3. **Clarity**: Environment-specific differences are clearly visible
4. **Consistency**: Ensures all environments use the same base configuration
5. **Consistent Naming**: Same service names across all environments

## Usage

### Development
```bash
docker-compose up
```

### Production
```bash
docker-compose -f docker-compose.prod.yml up
```

### View Merged Configuration
To see the final merged configuration for any environment:
```bash
# Development
docker-compose config

# Production
docker-compose -f docker-compose.prod.yml config
```

## Key Differences Between Environments

| Aspect | Development | Production |
|--------|-------------|------------|
| Frontend | Hot reloading, dev ports | Production build with nginx, restart policy |
| Backend | Health checks, local volumes | Restart policy, persistent volumes |
| Database | Exposed ports | Internal only |
| Volumes | Local bind mounts | Named volumes |

## Service Naming Convention

All services use consistent names across environments:
- `frontend` - React application (development: Vite dev server, production: nginx)
- `backend` - Spring Boot application
- `postgres` - PostgreSQL database
- `pgadmin` - Database administration interface

## Container Names

Each service has a descriptive container name that includes the technology:
- `caravan-react` - React frontend application
- `caravan-spring` - Spring Boot backend API
- `caravan-postgres` - PostgreSQL database
- `caravan-pgadmin` - PgAdmin database administration

## Adding New Services

1. Add the base configuration to `docker-compose.common.yml`
2. Extend and customize in environment-specific files as needed
3. Remember that `depends_on` and `volumes_from` are never shared between extended services 