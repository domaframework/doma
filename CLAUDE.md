# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Doma is a database access framework for Java 17+ that uses compile-time annotation processing. It provides type-safe database operations through a Criteria API and SQL templates ("two-way SQL"). The project is organized into multiple modules with comprehensive integration testing across various databases.

## Build Commands

### Core Development
- `./gradlew build` - Build all modules with Spotless formatting applied
- `./gradlew test` - Run tests for all modules (defaults to H2 database)
- `./gradlew spotlessApply` - Apply code formatting (automatically runs with build)
- `./gradlew spotlessCheck` - Check code formatting compliance

### Testing with Different Databases
The project supports extensive database testing. Tests can be run against specific databases:
- `./gradlew h2` - Run tests against H2 (in-memory)
- `./gradlew mysql` - Run tests against MySQL 5.7 (via Testcontainers)
- `./gradlew mysql8` - Run tests against MySQL 8.0 (via Testcontainers)
- `./gradlew oracle` - Run tests against Oracle 21c (via Testcontainers)
- `./gradlew postgresql` - Run tests against PostgreSQL 12 (via Testcontainers)
- `./gradlew sqlite` - Run tests against SQLite
- `./gradlew sqlserver` - Run tests against SQL Server 2019 (via Testcontainers)
- `./gradlew testAll` - Run tests against all supported databases

### Single Test Execution
To run a specific test class:
```bash
./gradlew :module-name:test --tests "ClassName"
# Example: ./gradlew :doma-core:test --tests "ConfigTest"
```

To run integration tests:
```bash
./gradlew :integration-test-java:test --tests "TestClassName"
```

## Module Architecture

### Core Modules
- **doma-core**: Main framework containing annotations, JDBC abstractions, query builders, and type system
- **doma-processor**: Annotation processor that generates metamodel classes and validates SQL at compile time
- **doma-kotlin**: Kotlin-specific extensions and integration
- **doma-slf4j**: SLF4J logging integration
- **doma-template**: SQL template processing utilities
- **doma-mock**: Test utilities and mocks

### Integration Test Modules
- **integration-test-common**: Shared test infrastructure and configuration
- **integration-test-java-common**: Common Java test utilities
- **integration-test-java**: Java-based integration tests
- **integration-test-kotlin**: Kotlin-based integration tests

## Key Architectural Concepts

### Annotation Processing Pipeline
The framework heavily relies on compile-time code generation. The `doma-processor` module generates:
- Entity metamodel classes (ending with `_`)
- DAO implementation classes
- SQL validation and type checking

### SQL Template System ("Two-way SQL")
SQL files use special comments for dynamic SQL that remain valid SQL when run directly:
- `/*%if condition*/` - Conditional blocks
- `/*parameter*/'default'` - Parameter binding
- `/*%expand*/` - Column expansion

### Type System Hierarchy
- **Wrappers** (`org.seasar.doma.wrapper`): Low-level JDBC type mappings
- **Domain Types** (`org.seasar.doma.jdbc.domain`): Custom value object mappings
- **Entity Types** (`org.seasar.doma.jdbc.entity`): ORM entity mappings
- **Criteria API** (`org.seasar.doma.jdbc.criteria`): Type-safe query building

### Configuration System
- `Config` interface defines database connection, naming conventions, dialects
- `SimpleConfig` provides basic implementation
- Dialect classes handle database-specific SQL generation

## Development Guidelines

### Setting Up Development Environment
- Install JDK 21 (recommended via [SDKMAN](https://sdkman.io/jdks))
- Clone repository: `git clone https://github.com/domaframework/doma.git`
- Build project: `./gradlew build`
- For IDE setup, import as a Gradle project (IntelliJ IDEA recommended)

### Annotation Processing Configuration
When modifying annotation processors or entity/domain classes, the `ap` property in `gradle.properties` can be used to pass additional annotation processor options in CSV format.

### Code Formatting
All code must pass Spotless formatting checks. The build automatically applies formatting, but you can run `./gradlew spotlessCheck` to verify compliance before committing.

**IMPORTANT**: Always run `./gradlew spotlessApply` before committing changes to ensure consistent code formatting across the project.

### Import Statement Guidelines
- Do not use wildcard imports (e.g., `import java.util.*;`) in Java code
- Always use explicit imports for each class (e.g., `import java.util.List;`, `import java.util.Map;`)
- This improves code readability and makes dependencies explicit

### Contributing Guidelines
- Submit contributions via GitHub Pull Requests from your own fork
- Write issues and PRs in English for broader accessibility
- All contributions are licensed under Apache License 2.0
- Use snapshot versions from Sonatype repository for testing unreleased features

### Database Compatibility Testing
When making changes that affect SQL generation or JDBC operations, run tests against multiple databases using `./gradlew testAll` to ensure compatibility.

### Integration Test Structure
Integration tests use Testcontainers for database provisioning. Database URLs are configured in `gradle.properties` with the `TC_DAEMON=true` flag to improve test performance by reusing containers.

## Documentation

### Documentation System
The project uses Sphinx for documentation generation, hosted on ReadTheDocs:
- **Source**: Documentation source files are in the `docs/` directory
- **Format**: Written in reStructuredText (`.rst`) format
- **Languages**: English and Japanese (full translation support)
- **URL**: https://doma.readthedocs.io/

### Documentation Structure
- **Getting Started**: Setup guide and quickstart (`getting-started.rst`)
- **Core Concepts**: Entity, Domain, DAO, Embeddable documentation
- **Query Operations**: Comprehensive guides for all database operations in `query/` subdirectory
- **Query Building**: Criteria API, Query DSL, SQL templates documentation
- **Framework Integration**: Spring Boot, Quarkus, Lombok, Kotlin support guides
- **Development**: Build configuration, annotation processing, code generation

### Building Documentation Locally
```bash
# Install dependencies
cd docs
pip install -r requirements.txt

# Generate HTML documentation with auto-reload
sphinx-autobuild . _build/html
# Visit http://127.0.0.1:8000

# Generate translation files
sphinx-build -b gettext . _build/gettext

# Updates Japanese translation files
sphinx-intl update -p _build/gettext -l ja
```

### Documentation Guidelines
- Verify RST syntax and rendering before submitting PRs
- Maintain consistency with existing documentation style
- Update both English and Japanese translations when applicable
- Test documentation builds locally using `sphinx-autobuild`
