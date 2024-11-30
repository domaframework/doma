Doma
====

Doma is a database access framework for Java.
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

Prerequisite
---------------------

The latest major version of Doma supports Java 17 and above.
If you are using Java 8, please use Doma 2.

See also [Major versions](#major-versions).

Supported databases
---------------------

We are testing against the following databases:

| Database           |      version |   status   |
|--------------------|-------------:|:----------:|
| H2 Database        |        2.3.x |   stable   |
| MySQL v5           |          5.7 |   stable   |
| MySQL v8           |       8.0.36 |   stable   |
| Oracle Database XE |          21c |   stable   |
| PostgreSQL         |        12.20 |   stable   |
| SQL Server         |         2019 |   stable   |

Examples
---------------------

### Type-safe Criteria API

```java
var queryDsl = new QueryDsl(config);
var e = new Employee_();
var d = new Department_();

var employees = queryDsl
    .from(e)
    .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
    .where(c -> c.eq(d.departmentName, "SALES"))
    .associate(e, d, (employee, department) -> {
        employee.setDepartment(department);
        department.getEmployeeList().add(employee);
    })
    .fetch();
```

See [Unified Criteria API](https://doma.readthedocs.io/en/latest/query-dsl/)
for more information.

### SQL templates

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

```kotlin
plugins {
    id("org.domaframework.doma.compile") version "3.0.1"
}

dependencies {
    implementation("org.seasar.doma:doma-core:3.1.0")
    annotationProcessor("org.seasar.doma:doma-processor:3.1.0")
}
```

For Kotlin projects, use doma-kotlin instead of doma-core and use kapt in place of annotationProcessor:

```kotlin
plugins {
  id("org.domaframework.doma.compile") version "3.0.1"
}

dependencies {
    implementation("org.seasar.doma:doma-kotlin:3.1.0")
    kapt("org.seasar.doma:doma-processor:3.1.0")
}
```

### Maven

We recommend using Gradle, but if you want to use Maven, see below.

For Java projects:

```xml
...
<properties>
    <doma.version>3.1.0</doma.version>
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
                <source>17</source> <!-- depending on your project -->
                <target>17</target> <!-- depending on your project -->
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.seasar.doma</groupId>
                        <artifactId>doma-processor</artifactId>
                        <version>${doma.version}</version>
                    </path>
                </annotationProcessorPaths>
                <compilerArgs>
                    <!-- if you are using a Maven project in Eclipse, this argument is required -->
                    <arg>-Adoma.resources.dir=${project.basedir}/src/main/resources</arg>
                </compilerArgs>
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

| Version                                | Status          | Repository                             | Branch |
|----------------------------------------|-----------------|----------------------------------------|--------|
| [Doma 1](http://doma.seasar.org/)      | limited-support | https://github.com/seasarorg/doma/     | master |
| [Doma 2](http://doma.readthedocs.org/) | limited-support | https://github.com/domaframework/doma/ | 2.x    |
| [Doma 3](http://doma.readthedocs.org/) | stable          | https://github.com/domaframework/doma/ | master |

### Compatibility matrix

|         | Doma 1 | Doma 2 | Doma 3 |
|---------|--------|--------|--------|
| Java 6  | v      |        |        |
| Java 7  | v      |        |        |
| Java 8  | v      | v      |        |
| Java 9  |        | v      |        |
| Java 10 |        | v      |        |
| Java 11 |        | v      |        |
| Java 12 |        | v      |        |
| Java 13 |        | v      |        |
| Java 14 |        | v      |        |
| Java 15 |        | v      |        |
| Java 16 |        | v      |        |
| Java 17 |        | v      | v      |
| Java 18 |        | v      | v      |
| Java 19 |        | v      | v      |
| Java 20 |        | v      | v      |
| Java 21 |        | v      | v      |
| Java 22 |        | v      | v      |

Backers
---------------------

If you use Doma in your project or enterprise and
would like to support ongoing development,
please consider becoming a backer.
[[Become a backer](https://opencollective.com/doma#category-CONTRIBUTE)]

Our top backers are shown below!

<a href="https://opencollective.com/doma/backer/0/website" target="_blank"><img src="https://opencollective.com/doma/backer/0/avatar.svg"></a>
<a href="https://opencollective.com/doma/backer/1/website" target="_blank"><img src="https://opencollective.com/doma/backer/1/avatar.svg"></a>
<a href="https://opencollective.com/doma/backer/2/website" target="_blank"><img src="https://opencollective.com/doma/backer/2/avatar.svg"></a>
<a href="https://opencollective.com/doma/backer/3/website" target="_blank"><img src="https://opencollective.com/doma/backer/3/avatar.svg"></a>
<a href="https://opencollective.com/doma/backer/4/website" target="_blank"><img src="https://opencollective.com/doma/backer/4/avatar.svg"></a>
<a href="https://opencollective.com/doma/backer/5/website" target="_blank"><img src="https://opencollective.com/doma/backer/5/avatar.svg"></a>
<a href="https://opencollective.com/doma/backer/6/website" target="_blank"><img src="https://opencollective.com/doma/backer/6/avatar.svg"></a>

  [apt]: https://www.jcp.org/en/jsr/detail?id=269
