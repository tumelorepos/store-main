# Setup and Development Guide

## Prerequisites

- Java 17 or higher
- Docker and Docker Compose (optional, for containerized setup)
- Git
- Terminal/PowerShell

## Project Structure

```
store-main/
├── .github/workflows/       # CI/CD pipelines
├── gradle/                  # Gradle wrapper configuration
├── src/
│   ├── main/
│   │   ├── java/           # Java source code
│   │   └── resources/      # Configuration and database migrations
│   └── test/
│       ├── java/           # Test classes
│       └── resources/      # Test configuration
├── build.gradle            # Gradle build configuration
├── Dockerfile             # Docker configuration
├── docker-compose.yml     # Docker Compose configuration
├── README.md              # Original README
├── IMPLEMENTATION.md      # Implementation details
└── QUICK_REFERENCE.md     # Quick reference guide
```

## Quick Start

### Option 1: Docker Compose (Recommended)

1. **Start the complete stack**:
   ```bash
   docker-compose up -d
   ```

2. **Verify services are running**:
   ```bash
   docker ps
   ```

3. **Access the application**:
   - API: http://localhost:8080
   - PostgreSQL: localhost:5433

4. **Stop the stack**:
   ```bash
   docker-compose down
   ```

### Option 2: Local Development

1. **Start PostgreSQL**:
   ```bash
   docker run -d \
     --name postgres \
     -e POSTGRES_USER=admin \
     -e POSTGRES_PASSWORD=admin \
     -e POSTGRES_DB=store \
     -v postgres:/var/lib/postgresql/data \
     -p 5433:5432 \
     postgres:16.2 \
     postgres -c wal_level=logical
   ```

2. **Build the project**:
   ```bash
   cd store-main
   ./gradlew build
   ```

3. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```

4. **Application starts at**: http://localhost:8080

## Development Workflow

### Building

```bash
# Full clean build (with tests)
./gradlew clean build

# Build without tests
./gradlew clean build -x test

# Build only
./gradlew build

# Incremental build
./gradlew build
```

### Testing

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests CustomerControllerTests

# Run with debug output
./gradlew test --debug

# Generate coverage report
./gradlew test jacocoTestReport
# Report location: build/reports/jacoco/test/html/index.html
```

### Code Quality

```bash
# Check code formatting
./gradlew spotlessCheck

# Auto-fix formatting issues
./gradlew spotlessApply

# Run all quality checks
./gradlew check
```

### Debugging

1. **Start application in debug mode**:
   ```bash
   ./gradlew bootRun --debug-jvm
   ```

2. **Attach debugger**:
   - IDE will usually auto-detect and offer to connect
   - Set breakpoints and step through code

3. **View application logs**:
   ```bash
   tail -f build/logs/application.log
   ```

## Common Tasks

### Adding a New Endpoint

1. **Create a DTO** (if needed):
   ```java
   package com.example.store.dto;
   @Data
   public class MyDTO { ... }
   ```

2. **Create a Mapper** (if needed):
   ```java
   @Mapper(componentModel = "spring")
   public interface MyMapper { ... }
   ```

3. **Add Controller Method**:
   ```java
   @GetMapping("/path")
   public MyDTO myMethod() { ... }
   ```

4. **Add Tests**:
   - Create test class extending existing tests
   - Use `@MockitoBean` for repository mocking

### Adding a Database Migration

1. **Create migration file**:
   ```yaml
   # db.changelog-N.yaml
   databaseChangeLog:
     - changeSet:
         id: N-description
         author: developer@example.com
         changes:
           - sqlFile:
               path: db/changelog/schema-description.sql
   ```

2. **Create SQL file**:
   ```sql
   -- db/changelog/schema-description.sql
   CREATE TABLE ...;
   ```

3. **Update master changelog**:
   ```yaml
   # db.changelog-master.yaml
   - include:
       file: db/changelog/db.changelog-N.yaml
   ```

4. **Run migrations**:
   - Automatic on application start via Liquibase

### Troubleshooting

**Problem**: Gradle download timeout
- **Solution**: Check internet connection or configure proxy

**Problem**: Database connection refused
- **Solution**: Ensure PostgreSQL is running on port 5433

**Problem**: Tests fail with H2 issues
- **Solution**: H2 may not support all PostgreSQL syntax; consider mocking database calls

**Problem**: Spotless formatting fails
- **Solution**: Run `./gradlew spotlessApply` to auto-fix

**Problem**: Tests timeout
- **Solution**: Increase timeout in build.gradle or debug slow tests

## IDE Setup

### IntelliJ IDEA

1. Open project: `File > Open > store-main`
2. Configure JDK: `File > Project Structure > Project > SDK > 17`
3. Enable Gradle: `Gradle tool window > Enable auto-reload`
4. Install Lombok plugin: `Settings > Plugins > Lombok`
5. Enable annotation processing: `Settings > Build > Compiler > Annotation Processors`

### VS Code

1. Install extensions:
   - Java Extension Pack
   - Gradle for Java
   - Lombok Annotations Support

2. Configure settings.json:
   ```json
   {
     "java.configuration.runtimes": [
       { "name": "JavaSE-17", "path": "/path/to/jdk17" }
     ]
   }
   ```

## Database Management

### Connect to PostgreSQL

```bash
# Using psql (if installed)
psql -h localhost -p 5433 -U admin -d store

# Or through Docker
docker exec -it postgres psql -U admin -d store
```

### View Database State

```sql
-- List all tables
\dt

-- Inspect table schema
\d customer

-- View indexes
\di

-- Check migrations applied
SELECT * FROM databasechangelog;
```

### Reset Database

```bash
# Stop and remove container
docker-compose down -v

# Or manually
DROP DATABASE store;
CREATE DATABASE store;

# Migrations will run automatically on next start
```

## Environment Configuration

### application.properties (Development)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/store
spring.datasource.username=admin
spring.datasource.password=admin
spring.jpa.show-sql=false
logging.level.com.example.store=INFO
```

### application.properties (Testing)

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.liquibase.enabled=true
logging.level.com.example.store=DEBUG
```

## Performance Tips

1. **Use Database Indexes**: Check query execution plans
2. **Enable Query Logging**: Set `spring.jpa.show-sql=true` temporarily
3. **Monitor N+1 Queries**: Use Hibernate statistics or profiling
4. **Lazy Load Collections**: Already configured in entities
5. **Use DTOs**: Prevents unnecessary data fetches
6. **Batch Operations**: Consider batch inserts/updates for large datasets

## Deployment

### Docker Build

```bash
# Build image locally
docker build -f Dockerfile -t store-app:latest store-main/

# Run container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/store \
  -e SPRING_DATASOURCE_USERNAME=admin \
  -e SPRING_DATASOURCE_PASSWORD=admin \
  store-app:latest
```

### CI/CD Pipeline

- Automatically triggered on push to main/develop branches
- Runs in GitHub Actions
- See `.github/workflows/build.yml` for configuration
- Builds Docker image and publishes artifacts

## Support and Documentation

- **Spring Boot**: https://spring.io/projects/spring-boot
- **Gradle**: https://gradle.org/docs/
- **PostgreSQL**: https://www.postgresql.org/docs/
- **MapStruct**: https://mapstruct.org/documentation/
- **Liquibase**: https://docs.liquibase.com/

## Contributing Guidelines

1. Follow existing code style (Spotless checks this)
2. Add tests for new features
3. Update documentation
4. Run full test suite before committing
5. Ensure CI pipeline passes

## Git Workflow

```bash
# Create feature branch
git checkout -b feature/my-feature

# Commit changes
git add .
git commit -m "Add my feature"

# Push and create PR
git push origin feature/my-feature

# CI/CD runs automatically
# After approval, merge to main

# Pull latest
git pull origin main
```
