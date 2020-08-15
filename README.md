Doma
====

Doma 2 is a database access framework for Java 8+.
Doma has various strengths:

- Verifies and generates source code **at compile time** using [annotation processing][apt].
- Maps database columns to user-defined Java objects.
- Uses SQL templates, called “two-way SQL”.
- Supports classes introduced in Java 8, such as `java.time.LocalDate`, `java.util.Optional`, and `java.util.stream.Stream`.
- Has no dependence on other libraries.

[![Build Status](https://github.com/domaframework/doma/workflows/Java%20CI%20with%20Gradle/badge.svg)](https://github.com/domaframework/doma/actions?query=workflow%3A%22Java+CI+with+Gradle%22)
[![docs](https://readthedocs.org/projects/doma/badge/?version=latest)](https://doma.readthedocs.io/en/latest/)
[![javadoc](https://javadoc.io/badge2/org.seasar.doma/doma-core/javadoc.svg)](https://javadoc.io/doc/org.seasar.doma/doma-core)
[![Join the chat at https://gitter.im/domaframework/doma](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/domaframework/doma?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Google group : doma-user](https://img.shields.io/badge/Google%20Group-doma--user-orange.svg)](https://groups.google.com/g/doma-user)
[![Twitter](https://img.shields.io/badge/twitter-@domaframework-blue.svg?style=flat)](https://twitter.com/domaframework)

Examples
---------------------

Define an entity class:
```java
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "EMPLOYEE_SEQ")
    public Integer id;
    public String name;
    public Integer age;
    @Version
    public Integer version;
}
```

Define a DAO interface:
```java
@Dao(config = AppConfig.class)
public interface EmployeeDao {
    @Select
    Employee selectById(Integer id);
    @Update
    int update(Employee employee);
}
```

Execute queries:
```java
public class App {
    public static void main(String[] args) {
        TransactionManager tm = AppConfig.singleton().getTransactionManager();
        tm.required(() -> {
            EmployeeDao dao = new EmployeeDaoImpl();
            Employee employee = dao.selectById(1);
            employee.age++;
            dao.update(employee);
        });
    }
}
```

Try following getting started examples:
- [Get started! (IntelliJ IDEA)](https://doma.readthedocs.io/en/latest/getting-started-idea/)
- [Get started! (Eclipse)](https://doma.readthedocs.io/en/latest/getting-started-eclipse/)

For more complete examples,
see [simple-examples](https://github.com/domaframework/simple-examples)
and [spring-boot-jpetstore](https://github.com/domaframework/spring-boot-jpetstore).

Build with Gradle
-----------------

```groovy
dependencies {
    implementation "org.seasar.doma:doma-core:2.40.0"
    annotationProcessor "org.seasar.doma:doma-processor:2.40.0"
}
```

For Kotlin projects, use kapt in place of annotationProcessor.

Documentation
---------------------

https://doma.readthedocs.io/

Google Group
---------------------

https://groups.google.com/g/doma-user

Related projects
---------------------

- [doma-spring-boot](https://github.com/domaframework/doma-spring-boot) : Supports integration with Spring Boot
- [doma-quarkus](https://github.com/domaframework/doma-quarkus) : Supports integration with Quarkus
- [doma-compile-plugin](https://github.com/domaframework/doma-compile-plugin) : Makes compilation easy
- [doma-codegen-plugin](https://github.com/domaframework/doma-codegen-plugin) : Generates Java and SQL files

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

  [apt]: https://www.jcp.org/en/jsr/detail?id=269
