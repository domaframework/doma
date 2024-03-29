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
[![javadoc](https://javadoc.io/badge2/org.seasar.doma/doma-core/javadoc.svg)](https://javadoc.io/doc/org.seasar.doma/doma-core)
[![project chat](https://img.shields.io/badge/zulip-join_chat-green.svg)](https://domaframework.zulipchat.com)
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

Written in Java 15 or above:

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

Try [Getting started](https://doma.readthedocs.io/en/latest/getting-started/) and [simple-examples](https://github.com/domaframework/simple-examples).

Installing
----------

### Gradle

For Java projects:

```groovy
dependencies {
    implementation("org.seasar.doma:doma-core:2.57.0")
    annotationProcessor("org.seasar.doma:doma-processor:2.57.0")
}
```

For Kotlin projects, use doma-kotlin instead of doma-core and use kapt in place of annotationProcessor:

```groovy
dependencies {
    implementation("org.seasar.doma:doma-kotlin:2.57.0")
    kapt("org.seasar.doma:doma-processor:2.57.0")
}
```

### Maven

We recommend using Gradle, but if you want to use Maven, see below.

For Java projects:

```xml
...
<properties>
    <doma.version>2.57.0</doma.version>
</properties>
...
<dependencies>
    <dependency>
        <groupId>org.seasar.doma</groupId>
        <artifactId>doma-core</artifactId>
        <version>${doma.version}</version>
    </dependency>
</dependencies>
...
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>1.8</source> <!-- depending on your project -->
                <target>1.8</target> <!-- depending on your project -->
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.seasar.doma</groupId>
                        <artifactId>doma-processor</artifactId>
                        <version>${doma.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

For Kotlin projects, see [Kotlin document](https://kotlinlang.org/docs/reference/kapt.html#using-in-maven).

Documentation
---------------------

https://doma.readthedocs.io/

Chatroom
---------------------

https://domaframework.zulipchat.com

Related projects
---------------------

- [quarkus-doma](https://github.com/quarkiverse/quarkus-doma) - Supports integration with Quarkus
- [doma-spring-boot](https://github.com/domaframework/doma-spring-boot) - Supports integration with Spring Boot
- [doma-compile-plugin](https://github.com/domaframework/doma-compile-plugin) - Makes compilation easy
- [doma-codegen-plugin](https://github.com/domaframework/doma-codegen-plugin) - Generates Java and SQL files

Major versions
---------------------

### Status and Repository

| Version                                | Status | Repository                             | Branch |
|----------------------------------------|--------|----------------------------------------|--------|
| [Doma 1](http://doma.seasar.org/)      | stable | https://github.com/seasarorg/doma/     | master |
| [Doma 2](http://doma.readthedocs.org/) | stable | https://github.com/domaframework/doma/ | master |

### Compatibility matrix

|         | Doma 1 | Doma 2 |
|---------|--------|--------|
| Java 6  | v      |        |
| Java 7  | v      |        |
| Java 8  | v      | v      |
| Java 9  |        | v      |
| Java 10 |        | v      |
| Java 11 |        | v      |
| Java 12 |        | v      |
| Java 13 |        | v      |
| Java 14 |        | v      |
| Java 15 |        | v      |
| Java 16 |        | v      |
| Java 17 |        | v      |
| Java 18 |        | v      |
| Java 19 |        | v      |
| Java 20 |        | v      |
| Java 21 |        | v      |

Backers & Sponsors
------------------

If you use Doma in your project or enterprise and would like to support ongoing development, please consider becoming a backer or a sponsor.
Sponsor logos will show up here with a link to your website.

[[Become a backer or a sponsor](https://opencollective.com/doma#category-CONTRIBUTE)]

  [apt]: https://www.jcp.org/en/jsr/detail?id=269
