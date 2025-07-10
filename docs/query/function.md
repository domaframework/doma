===============
# Stored function

.. contents::
   :depth: 4

To call stored functions, you must annotate DAO methods with the `@Function` annotation:

```java

  @Dao
  public interface EmployeeDao {
      @Function
      Integer execute(@In Integer id, @InOut Reference<BigDecimal> salary);
      ...
  }

# Return type

If the stored function returns nothing, the return type must be `void`.

If the stored function returns a single result, the return type must be one of the following:

* [\1](\1)
* [\1](\1)
* [\1](\1)
* java.util.Map<String, Object>
* java.util.Optional, whose element type is one of [\1](\1), [\1](\1),
  [\1](\1), or java.util.Map<String, Object>
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

If the stored function returns a result list, the return type must be `java.util.List`
and its element type must be one of the following:

* [\1](\1)
* [\1](\1)
* [\1](\1)
* java.util.Map<String, Object>
* java.util.Optional whose element type is either [\1](\1) or [\1](\1)
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

# Function name

The annotated method name is recognized as the function name by default.
To override it, you can specify a value for the `@Function`'s `name` element:

```java

  @Function(name = "calculateSalary")
  void execute(@In Integer id, @InOut Reference<BigDecimal> salary);

# Parameters

The order of stored function parameters must correspond with the order of DAO method parameters.

All parameters must be annotated with one of the following annotations:

* @In
* @InOut
* @Out
* @ResultSet

## IN parameter

To indicate IN parameters, annotate corresponding DAO method parameters with the `@In` annotation.
The type of the DAO method parameter must be one of the following:

* [\1](\1)
* [\1](\1)
* java.util.Optional whose element type is either [\1](\1) or [\1](\1)
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

Suppose you have the following definition:

```java

  @Function
  void execute(@In Integer id);

You can invoke the method as follows:

```java

  EmployeeDao dao = new EmployeeDaoImpl();
  dao.execute(1);

## INOUT parameter

To indicate INOUT parameters, annotate corresponding DAO method parameters with
the `@InOut` annotation.
The type of the DAO method parameter must be `org.seasar.doma.jdbc.Reference`
and its type parameter must be one of the following:

* [\1](\1)
* [\1](\1)
* java.util.Optional whose element type is either [\1](\1) or [\1](\1)
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

Suppose you have the following definition:

```java

  @Function
  void execute(@InOut Reference<BigDecimal> salary);

You can invoke the method as follows:

```java

  EmployeeDao dao = new EmployeeDaoImpl();
  BigDecimal in = new BigDecimal(100);
  Reference<BigDecimal> ref = new Reference<BigDecimal>(in);
  dao.execute(ref);
  BigDecimal out = ref.get();

## OUT parameter

To indicate OUT parameters, annotate corresponding DAO method parameters with
the `@Out` annotation.
The type of the DAO method parameter must be `org.seasar.doma.jdbc.Reference`
and its type parameter must be one of the following:

* [\1](\1)
* [\1](\1)
* java.util.Optional whose element type is either [\1](\1) or [\1](\1)
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

Suppose you have the following definition:

```java

  @Function
  Integer execute(@Out Reference<BigDecimal> salary);

You can invoke the method as follows:

```java

  EmployeeDao dao = new EmployeeDaoImpl();
  Reference<BigDecimal> ref = new Reference<BigDecimal>();
  Integer result = dao.execute(ref);
  BigDecimal out = ref.get();

## Cursor or result set

To indicate cursors or result sets,
annotate corresponding DAO method parameters with the `@ResultSet` annotation.

The DAO method parameter type must be `java.util.List`
and its element type must be one of the following:

* [\1](\1)
* [\1](\1)
* [\1](\1)
* java.util.Map<String, Object>
* java.util.Optional whose element type is either [\1](\1) or [\1](\1)
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

Suppose you have the following definition:

```java

  @Function
  void execute(@ResultSet List<Employee> employee);

You can invoke the method as follows:

```java

  EmployeeDao dao = new EmployeeDaoImpl();
  List<Employee> employees = new ArrayList<Employee>();
  dao.execute(employees);
  for (Employee e : employees) {
      ...
  }
