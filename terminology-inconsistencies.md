# Terminology Inconsistencies

This document tracks potential terminology inconsistencies between JavaDoc comments in the Doma framework's codebase and the RST documentation in the `domaframework/doma-docs` repository.

## Observed Inconsistencies

So far, no significant terminology inconsistencies have been identified. The JavaDoc comments generally align well with the terminology used in the RST documentation.

Some minor observations:

1. **DAO vs Dao**: The codebase uses both "DAO" (all caps) and "Dao" (title case) in different contexts. The RST documentation seems to prefer "DAO" when referring to the concept, while the annotation is named `@Dao`.

2. **SQL vs Sql**: Similar to the above, both "SQL" and "Sql" are used in different contexts. The RST documentation consistently uses "SQL" when referring to the language, while some class names use "Sql" (e.g., `SqlFileRepository`).

These are minor stylistic differences rather than true terminology inconsistencies and don't require changes unless specified by the user.

## Confirmation Process

Before making any changes to address potential terminology inconsistencies, I will:

1. Document the specific inconsistency in this file
2. Provide examples from both the JavaDoc and RST documentation
3. Seek confirmation from the user on the preferred terminology

## Terminology Reference

The following key terms appear consistently in both the JavaDoc and RST documentation:

- Entity
- Domain
- Dao/DAO
- SQL/Sql
- Batch operations (BatchInsert, BatchUpdate, BatchDelete)
- Two-way SQL
- Entity Listener
- Config/Configuration
