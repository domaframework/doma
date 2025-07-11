# Getting started

```{contents}
:depth: 4
```

## Summary

This guide explains how to set up your development environment and introduces the basic functionality of Doma.

## Install JDK

First, you need to install JDK 17 or later.

## Get sample project

To get the sample project, clone the [getting-started](https://github.com/domaframework/getting-started)
repository and navigate to the created directory using these commands:

```bash
$ git clone https://github.com/domaframework/getting-started.git
$ cd getting-started
```

Ensure successful project setup with:

```bash
$ ./gradlew build
```

:::{note}
For Windows users, execute `gradlew build`.
:::

## Sample project structure

The getting-started sample is a Gradle multi-project containing a java-17 subproject.
In this guide, we'll focus on the java-17 subproject.

## Import project to your IDE

### Eclipse

This guide has been tested with Eclipse 4.23.0.
Import the getting-started project as a Gradle project.

:::{note}
If you want to store SQL statements in files,
[Doma Tools](https://github.com/domaframework/doma-tools) can help you.
:::

### IntelliJ IDEA

Tested with IntelliJ IDEA Community 2023.3.4.
Import the getting-started project as a Gradle project.

:::{note}
You can use the following IntelliJ IDEA plugins to support Doma. Since they have overlapping features, please choose one:

- [Doma Support](https://plugins.jetbrains.com/plugin/7615-doma-support)
- [Doma Tools for IntelliJ](https://plugins.jetbrains.com/plugin/26701-doma-tools/)
:::

## Programming styles

Doma supports two programming styles: DSL and DAO.
We recommend using both styles together to maximize their benefits.

The DSL style uses a type-safe Criteria API, which excels at automatically generating simple dynamic SQL.
The DAO style maps SQL statements to Java interface methods and works well for both automatically generating
standard SQL for insert, update, and delete operations and for writing complex SQL manually.

## DSL style

In the DSL style, you work with examples in the `boilerplate.java17.repository.EmployeeRepository`
and the [](query-dsl.md) for operations.

### SELECT

To execute a SELECT query and retrieve Java object results, follow this example:

```java
public Employee selectById(Integer id) {
  var e = new Employee_();
  return queryDsl.from(e).where(c -> c.eq(e.id, id)).fetchOne();
}
```

You'll use a metamodel class, like `Employee_` for `Employee`, which is auto-generated through annotation processing.

The `queryDsl` instance from the `QueryDsl` class serves as the Criteria API's starting point.

The above code generates the following SQL statement:

```sql
select t0_.id, t0_.name, t0_.age, t0_.version from Employee t0_ where t0_.id = ?
```

### DELETE

To issue a DELETE statement, write as follows:

```java
public void delete(Employee employee) {
  var e = new Employee_();
  queryDsl.delete(e).single(employee).execute();
}
```

### INSERT

To issue an INSERT statement, write as follows:

```java
public void insert(Employee employee) {
  var e = new Employee_();
  queryDsl.insert(e).single(employee).execute();
}
```

### UPDATE

To issue an UPDATE statement, write as follows:

```java
public void update(Employee employee) {
  var e = new Employee_();
  queryDsl.update(e).single(employee).execute();
}
```

## DAO style

You can find some examples in `boilerplate.java17.dao.EmployeeDao`.
See [](dao.md) and [](sql.md) for more information.

### SELECT (DAO)

In the DAO style, for issuing a SELECT statement to retrieve Java objects,
use the `@Sql` annotation with Text Blocks for SQL templates:

```java
@Sql("""
    select
      /*%expand*/*
    from
      employee
    where
      id = /* id */0
    """)
@Select
Employee selectById(Integer id);
```

This SQL template contains two special expressions: `/*%expand*/` and `/* id */`.
When processing the SQL template, `/*%expand*/` and the following `*` are replaced with a complete column list.
Similarly, `/* id */` and the following `0` are replaced with the bind variable `?`.
The value bound to this variable is the `id` parameter of the `selectById` method.

The above code generates the following SQL statement:

```sql
select
  id, name, age, version
from
  employee
where
  id = ?
```

### DELETE (DAO)

To issue a DELETE statement, write as follows:

```java
@Delete
int delete(Employee employee);
```

### INSERT (DAO)

To issue an INSERT statement, write as follows:

```java
@Insert
int insert(Employee employee);
```

### UPDATE (DAO)

To issue an UPDATE statement, write as follows:

```java
@Update
int update(Employee employee);
```

## Next Step

See other example projects:

- [simple-examples](https://github.com/domaframework/simple-examples)
