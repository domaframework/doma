Doma
====

Doma 2 is a database access framework for Java 8+.
Doma has various strengths:

- Verifies and generates source code **at compile time** using [annotation processing][apt].
- Provides type-safe Criteria API.
- Supports Kotlin.
- Uses SQL templates, called "two-way SQL".
- Has no dependence on other libraries.

[![Build Status](https://github.com/domaframework/doma/workflows/Java%20CI%20with%20Gradle/badge.svg)](https://github.com/domaframework/doma/actions?query=workflow%3A%22Java+CI+with+Gradle%22)
[![docs](https://readthedocs.org/projects/doma/badge/?version=latest)](https://doma.readthedocs.io/en/latest/)
[![javadoc](https://javadoc.io/badge2/org.seasar.doma/doma-core/javadoc.svg)](https://javadoc.io/doc/org.seasar.doma/doma-core)
[![Join the chat at https://gitter.im/domaframework/doma](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/domaframework/doma?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Google group : doma-user](https://img.shields.io/badge/Google%20Group-doma--user-orange.svg)](https://groups.google.com/g/doma-user)
[![Twitter](https://img.shields.io/badge/twitter-@domaframework-blue.svg?style=flat)](https://twitter.com/domaframework)

Examples
---------------------

### Type-safe Criteria API

Written in Java 8:

```java
Entityql entityql = new Entityql(config);
Employee_ e = new Employee_();
Department_ d = new Department_();

List<Employee> list = entityql
    .from(e)
    .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
    .where(c -> c.eq(d.departmentName, "SALES"))
    .associate(e, d, (employee, department) -> {
        employee.setDepartment(department);
        department.getEmployeeList().add(employee);
    })
    .fetch();
```

Written in Kotlin:

```kotlin
val entityql = KEntityql(config)
val e = Employee_()
val d = Department_()

val list = entityql
    .from(e)
    .innerJoin(d) { eq(e.departmentId, d.departmentId) }
    .where { eq(d.departmentName, "SALES") }
    .associate(e, d) { employee, department ->
        employee.department = department
        department.employeeList += employee
    }
    .fetch()
```

See [Criteria API](https://doma.readthedocs.io/en/latest/criteria-api/)
for more information.

### SQL templates

Written in Java 15:

```java
@Dao
public interface EmployeeDao {

  @Sql(
    """
    select * from EMPLOYEE where
    /*%if salary != null*/
      SALARY >= /*salary*/9999
    /*%end*/
    """)
  @Select
  List<Employee> selectBySalary(BigDecimal salary);
}
```

See [SQL templates](https://doma.readthedocs.io/en/latest/sql/)
for more information.

### More Examples

Try [Getting started](https://doma.readthedocs.io/en/latest/getting-started/).

For more complete examples,
see [simple-examples](https://github.com/domaframework/simple-examples)
and [spring-boot-jpetstore](https://github.com/domaframework/spring-boot-jpetstore).

Build with Gradle
-----------------

For Java projects:

```groovy
dependencies {
    implementation "org.seasar.doma:doma-core:2.43.0"
    annotationProcessor "org.seasar.doma:doma-processor:2.43.0"
}
```

For Kotlin projects, use doma-kotlin instead of doma-core and use kapt in place of annotationProcessor:

```groovy
dependencies {
    implementation "org.seasar.doma:doma-kotlin:2.43.0"
    kapt "org.seasar.doma:doma-processor:2.43.0"
}
```


Documentation
---------------------

https://doma.readthedocs.io/

Google Group
---------------------

https://groups.google.com/g/doma-user

Related projects
---------------------

- [doma-spring-boot](https://github.com/domaframework/doma-spring-boot) - Supports integration with Spring Boot
- [doma-quarkus](https://github.com/domaframework/doma-quarkus) - Supports integration with Quarkus
- [doma-compile-plugin](https://github.com/domaframework/doma-compile-plugin) - Makes compilation easy
- [doma-codegen-plugin](https://github.com/domaframework/doma-codegen-plugin) - Generates Java and SQL files

Major versions
---------------------

### Status and Repository

| Version                                | Status            | Repository                             | Branch |
| -------------------------------------- | ----------------- | -------------------------------------- | ------ |
| [Doma 1](http://doma.seasar.org/)      | stable            | https://github.com/seasarorg/doma/     | master |
| [Doma 2](http://doma.readthedocs.org/) | stable            | https://github.com/domaframework/doma/ | master |

### Compatibility matrix

|         | Doma 1 | Doma 2 |
| ------- | ------ | ------ |
| Java 6  |   v    |        |
| Java 7  |   v    |        |
| Java 8  |   v    |   v    |
| Java 9  |        |   v    |
| Java 10 |        |   v    |
| Java 11 |        |   v    |
| Java 12 |        |   v    |
| Java 13 |        |   v    |
| Java 14 |        |   v    |
| Java 15 |        |   v    |

  [apt]: https://www.jcp.org/en/jsr/detail?id=269
