# Frequently Asked Questions

```{contents}
:depth: 4
```

## General

### What does "Doma" mean?

The name "Doma" is derived from "Dao Oriented database MApping framework".

### What is annotation processing?

Annotation processing is a mechanism for parsing annotations
at compile time and using them to generate code or perform compile-time checks.

We use annotation processing for the following purposes:

- Generating meta-classes from classes annotated with `@Entity` and `@Domain`.
- Generating implementation classes for interfaces annotated with `@Dao`.
- Validating SQL templates.

## Runtime environment

### Which version of JRE does Doma support?

JRE 17 and above.

### Which libraries are required for Doma to work?

None.

Doma has no dependencies on other libraries.

## Development environment

(which-version-of-jdk-does-doma-support)=

### Which version of JDK does Doma support?

JDK 17 and above.

### Which IDE do you recommend?

Doma works with both Eclipse and IntelliJ IDEA, but IntelliJ IDEA is recommended.
This is because Eclipse's annotation processing may behave differently from the JRE used in the production environment.

### Which build tool do you recommend?

Gradle and Maven are both supported.

### Where are the files output by annotation processing?

#### Gradle

Check `build/generated/sources/annotationProcessor`.

#### Maven

Check `target/generated-sources/annotations`.

#### Eclipse

Check the output destination directory in the annotation processing settings.

#### IntelliJ IDEA

See the above Gradle and Maven sections.

### I get the message that the sql file is not found, but it exists. Why?

You may get the following message, though the file exists:

```sh
[DOMA4019] The file[META-INF/../select.sql] is not found from the classpath
```

You can avoid this error by configuring recommended settings for each build tool and IDE.

See [](build.md).

### Do you provide a tool to generate Java code from a database schema?

Yes.

We provide the [Doma CodeGen plugin](https://github.com/domaframework/doma-codegen-plugin)
that generates Java and SQL files from your database schema.

## Features as a database access library

### Does Doma generate SQL statements?

Yes, Doma generates the following statements:

- SELECT
- INSERT
- DELETE
- UPDATE
- Stored procedure call
- Stored function call

### Is the generation of dynamic SQL statements supported?

There are two ways:

- SQL Templates.
- The Criteria API.

See [](sql.md) and [](query-dsl.md) for detailed information.

### Does Doma support fetching relationships such as one-to-one or one-to-many?

Yes, there are two ways to fetch relationships:

- [](aggregate-strategy.md) with SQL Templates.
- [](query-dsl.md#association) with the Criteria API.

### Does Doma provide a JDBC connection pooling feature?

No.

Use Doma together with
a JDBC connection pool library such as [HikariCP](https://github.com/brettwooldridge/HikariCP).
