Doma [![Build Status](https://travis-ci.org/domaframework/doma.svg?branch=master)](https://travis-ci.org/domaframework/doma) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.seasar.doma/doma/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.seasar.doma/doma)
========================================

[![Join the chat at https://gitter.im/domaframework/doma](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/domaframework/doma?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Doma is a database access framework for Java. 
Doma uses [Pluggable Annotation Processing API][apt] to generate source code and validate sql mappings **at compile time**.

Example
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
    List<Employee> selectAll();
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


Documentation
---------------------

https://doma.readthedocs.io/

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

License
-------

Apache License, Version 2.0

  [apt]: https://www.jcp.org/en/jsr/detail?id=269
