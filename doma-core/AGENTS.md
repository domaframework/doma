# doma-core Instructions

These instructions supplement the repository-level `AGENTS.md` for work under `doma-core/`.

## Module Scope

`doma-core` contains Doma's runtime and public API surface: annotations, JDBC abstractions, dialects, wrappers, domain and entity types, query execution support, Criteria API support, SQL parsing, and expression handling.

Treat changes here as broadly visible. Before changing public packages under `org.seasar.doma`, consider source and binary compatibility, documented behavior, and downstream generated code from `doma-processor`.

## Local Commands

- Run module tests with `./gradlew :doma-core:test`.
- Run one test with `./gradlew :doma-core:test --tests "FullyQualifiedTestClassName"`.
- Run benchmarks only when performance-sensitive code changes need measurement: `./gradlew :doma-core:jmh -Pjmh.includes=Pattern`.

## Development Notes

- Keep tests close to the changed package under `src/test/java`.
- For SQL tokenizer, SQL parser, expression parser, dialect, wrapper, or result mapping changes, add focused unit tests for edge cases and consider whether integration tests are also needed.
- Preserve behavior across supported dialects. Dialect-specific SQL transformations usually need tests for the affected database dialect and nearby dialect variants.
- Avoid changing public exception messages or `org.seasar.doma.message.Message` entries without checking processor and integration test expectations.
- Keep benchmark changes in `src/jmh/java`; do not use benchmarks as a substitute for correctness tests.
