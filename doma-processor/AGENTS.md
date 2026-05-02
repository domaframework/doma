# doma-processor Instructions

These instructions supplement the repository-level `AGENTS.md` for work under `doma-processor/`.

## Module Scope

`doma-processor` contains Doma's annotation processor. It validates annotated code at compile time, validates SQL templates, and generates DAO implementations, entity metamodels, domain support, embeddable support, aggregate strategy support, and related source files.

Changes here can affect compiler diagnostics, generated source compatibility, and user build behavior. Prefer small, well-tested changes that keep existing annotation processor options and generated APIs stable.

## Local Commands

- Run module tests with `./gradlew :doma-processor:test`.
- Run one test with `./gradlew :doma-processor:test --tests "FullyQualifiedTestClassName"`.
- The default compiler is configured by `compiler=javac` in `gradle.properties`.
- When changing compiler-sensitive behavior, also run `./gradlew :doma-processor:test -Pcompiler=ecj`.

## Development Notes

- Processor tests are organized around `org.seasar.doma.internal.apt`; reuse the existing `AptinaTestCase`-based patterns for compile-success, compile-failure, diagnostics, and generated-source assertions.
- Keep fixture classes close to the processor or validator test that uses them, usually under `src/test/java`.
- For new or changed diagnostics, assert the relevant message code and location rather than only checking that compilation fails.
- For generated code changes, verify both generated source shape and runtime behavior when practical.
- SQL validation behavior may involve files under `src/test/resources`; keep resource names and package-relative lookup behavior consistent with existing tests.
- Be careful when changing element/type utilities, validators, or generators because they are reused by several processors.
