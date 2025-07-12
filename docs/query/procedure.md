# Stored procedure

```{contents}
:depth: 4
```

To call stored procedures, you must annotate DAO methods with the `@Procedure` annotation:

```java
@Dao
public interface EmployeeDao {
    @Procedure
    void execute(@In Integer id, @InOut Reference<BigDecimal> salary);
    ...
}
```

## Return Type

The method return type must be `void`.

## Procedure name

By default, the annotated method name is used as the procedure name.
To specify a different name, set the `name` property of the `@Procedure` annotation:

```java
@Procedure(name = "calculateSalary")
void execute(@In Integer id, @InOut Reference<BigDecimal> salary);
```

## Parameters

The order of the stored procedure parameters must match the order of the DAO method parameters.

All parameters must be annotated with one of following annotations:

- @In
- @InOut
- @Out
- @ResultSet

### IN parameter

To indicate IN parameters, annotate corresponding DAO method parameters with the `@In` annotation.
The type of the DAO method parameter must be one of the following:

- [Basic classes](../basic.md)
- [Domain classes](../domain.md)
- java.util.Optional whose element type is either [Basic classes](../basic.md) or [Domain classes](../domain.md)
- java.util.OptionalInt
- java.util.OptionalLong
- java.util.OptionalDouble

Suppose you have the following definition:

```java
@Procedure
void execute(@In Integer id);
```

You can invoke the method as follows:

```java
EmployeeDao dao = new EmployeeDaoImpl();
dao.execute(1);
```

### INOUT parameter

To indicate INOUT parameters, annotate corresponding DAO method parameters with
the `@InOut` annotation.
The type of the DAO method parameter must be `org.seasar.doma.jdbc.Reference`
and its type parameter must be one of the following:

- [Basic classes](../basic.md)
- [Domain classes](../domain.md)
- java.util.Optional whose element type is either [Basic classes](../basic.md) or [Domain classes](../domain.md)
- java.util.OptionalInt
- java.util.OptionalLong
- java.util.OptionalDouble

Suppose you have the following definition:

```java
@Procedure
void execute(@InOut Reference<BigDecimal> salary);
```

You can invoke the method as follows:

```java
EmployeeDao dao = new EmployeeDaoImpl();
BigDecimal in = new BigDecimal(100);
Reference<BigDecimal> ref = new Reference<BigDecimal>(in);
dao.execute(ref);
BigDecimal out = ref.get();
```

### OUT parameter

To indicate OUT parameters, annotate corresponding DAO method parameters with
the `@Out` annotation.
The type of the DAO method parameter must be `org.seasar.doma.jdbc.Reference`
and its type parameter must be one of the following:

- [Basic classes](../basic.md)
- [Domain classes](../domain.md)
- java.util.Optional whose element type is either [Basic classes](../basic.md) or [Domain classes](../domain.md)
- java.util.OptionalInt
- java.util.OptionalLong
- java.util.OptionalDouble

Suppose you have the following definition:

```java
@Procedure
void execute(@Out Reference<BigDecimal> salary);
```

You can invoke the method as follows:

```java
EmployeeDao dao = new EmployeeDaoImpl();
Reference<BigDecimal> ref = new Reference<BigDecimal>();
dao.execute(ref);
BigDecimal out = ref.get();
```

### Cursor or result set

To indicate cursors or result sets,
annotate corresponding DAO method parameters with the `@ResultSet` annotation.

The DAO method parameter type must be `java.util.List`
and its element type must be one of the following:

- [Basic classes](../basic.md)
- [Domain classes](../domain.md)
- [Entity classes](../entity.md)
- java.util.Map\<String, Object>
- java.util.Optional whose element type is either [Basic classes](../basic.md) or [Domain classes](../domain.md)
- java.util.OptionalInt
- java.util.OptionalLong
- java.util.OptionalDouble

Suppose you have the following definition:

```java
@Procedure
void execute(@ResultSet List<Employee> employees);
```

You can invoke the method as follows:

```java
EmployeeDao dao = new EmployeeDaoImpl();
List<Employee> employees = new ArrayList<Employee>();
dao.execute(employees);
for (Employee e : employees) {
    ...
}
```
