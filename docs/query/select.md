===============
# Select

.. contents::
   :depth: 4

To perform a search operation using the SELECT statement, annotate the DAO method with `@Select`.

```java

  @Dao
  public interface EmployeeDao {
      @Select
      List<Employee> selectByDepartmentName(String departmentName);
      ...
  }

The `@Select` annotation requires an [\1](\1). 
You can define the SQL template either in an SQL file or in the `@Sql` annotation.

# Search condition

Search conditions are defined using method parameters.
The following parameter types are supported:

* [\1](\1)
* [\1](\1)
* Arbitrary type
* java.util.Optional containing either [\1](\1), [\1](\1), or arbitrary type as its element.
* java.util.Iterable containing either [\1](\1) or [\1](\1) as its element.
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

If the parameter type is either a basic type or a domain type, you can pass `null` as an argument.
For all other parameter types, the argument must not be `null`.

## Query using basic classes or domain classes

Declare [\1](\1) or [\1](\1) as method parameters.

```java

  @Select
  List<Employee> selectByNameAndSalary(String name, Salary salary);

Use the [\1](\1) to bind method parameters to SQL.

```sql

  select * from employee where employee_name = /* name */'hoge' and salary > /* salary */100

## Query using arbitrary type

When using arbitrary types as method parameters, use a dot `.` in the bind variable directive 
to access fields or invoke methods, and bind the result to SQL.

```java

  @Select
  List<Employee> selectByExample(Employee employee);

```sql

  select * from employee where employee_name = /* employee.name */'hoge' and salary > /* employee.getSalary() */100

Multiple parameters can be specified.

```java

  @Select
  List<Employee> selectByEmployeeAndDepartment(Employee employee, Department department);

## Mapping to the IN clause

To bind to the IN clause, use a subtype of `java.lang.Iterable` as the parameter.

```java

  @Select
  List<Employee> selectByNames(List<String> names);

```sql

  select * from employee where employee_name in /* names */('aaa','bbb','ccc')

# Single record search

For single record searches, the return type of the method must be one of the following:

* [\1](\1)
* [\1](\1)
* [\1](\1)
* java.util.Map<String, Object>
* java.util.Optional containing either [\1](\1), [\1](\1), [\1](\1), or java.util.Map<String, Object> as its element.
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

```java

  @Select
  Employee selectByNameAndSalary(String name, BigDecimal salary);

If the return type is not `Optional` and the result count is 0, `null` is returned.

If there are 2 or more search results, a `NonUniqueResultException` is thrown.

# Multiple record search

When searching for multiple records, specify `java.util.List` as the return type of the method. 
The elements of the `List` can be of the following types:

* [\1](\1)
* [\1](\1)
* [\1](\1)
* java.util.Map<String, Object>
* java.util.Optional containing either [\1](\1) or [\1](\1) as its element.
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

```java

  @Select
  List<Employee> selectByNameAndSalary(String name, Salary salary);

If there are no search results, an empty list is returned.

.. _stream-search:

# Stream search

For processing a large number of records incrementally, you can use stream search with `java.util.stream.Stream`.

There are two approaches to stream searches: you can either pass a Stream to a `java.util.Function`, 
or return a `Stream` directly from the method.

## Passing a Stream to Function

Set the `strategy` property in the `@Select` annotation to `SelectType.STREAM`, 
and add a parameter that is a subtype of `java.util.Function<Stream<TARGET, RESULT>>`.

```java

  @Select(strategy = SelectType.STREAM)
  BigDecimal selectByNameAndSalary(String name, BigDecimal salary, Function<Stream<Employee>, BigDecimal> mapper);

The caller of the DAO method passes a lambda expression that receives a stream and returns the result.

```java

  EmployeeDao dao = new EmployeeDaoImpl();
  BigDecimal result = dao.selectByNameAndSalary(name, salary, stream -> {
      return ...;
  });

The type parameter `TARGET` of `Function<Stream<TARGET>, RESULT>` must be one of the following:

* [\1](\1)
* [\1](\1)
* [\1](\1)
* java.util.Map<String, Object>
* Either [\1](\1) or [\1](\1) is within java.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

The type parameter `RESULT` must match the return type of the DAO method.

## Returning a Stream

Define the method return type as `java.util.stream.Stream`.
The Stream can contain elements of the following types:

* [\1](\1)
* [\1](\1)
* [\1](\1)
* java.util.Map<String, Object>
* java.util.Optional containing either [\1](\1) or [\1](\1) as its element.
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

```java

  @Select
  Stream<Employee> selectByNameAndSalary(String name, BigDecimal salary);

The caller of the DAO method will be as follows:

```java

  EmployeeDao dao = new EmployeeDaoImpl();
  try (Stream<Employee> stream = dao.selectByNameAndSalary(name, salary)) {
    ...
  }

```{warning}

  To ensure the proper closing of resources such as 
  `java.sql.ResultSet`, `java.sql.PreparedStatement`, and `java.sql.Connection`, 
  always close the `Stream`.

```{note}

  Due to the risk of forgetting to release resources when returning values, Doma displays a warning message. 
  To suppress the warning message, please specify `@Suppress` as follows:

```java

  @Select
  @Suppress(messages = { Message.DOMA4274 })
  Stream<Employee> selectByNameAndSalary(String name, BigDecimal salary);

.. _collector-search:

# Collector search

Search results can be processed using `java.util.Collector`.

To process search results using `Collector`, set the `strategy` property of `@Select` to `SelectType.COLLECT`, 
and add a parameter that is a subtype of either `java.stream.Collector<TARGET, ACCUMULATION, RESULT>` or 
`java.stream.Collector<TARGET, ?, RESULT>`.

```java

  @Select(strategy = SelectType.COLLECT)
  <RESULT> RESULT selectBySalary(BigDecimal salary, Collector<Employee, ?, RESULT> collector);

The caller of the DAO method passes an instance of `Collector`.

```java

  EmployeeDao dao = new EmployeeDaoImpl();
  Map<Integer, List<Employee>> result =
      dao.selectBySalary(salary, Collectors.groupingBy(Employee::getDepartmentId));

The type parameter `TARGET` of `Collector<TARGET, ACCUMULATION, RESULT>` must be one of the following:

* [\1](\1)
* [\1](\1)
* [\1](\1)
* java.util.Map<String, Object>
* java.util.Optional containing either [\1](\1) or [\1](\1) as its element.
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

The type parameter `RESULT` of `Collector<TARGET, ACCUMULATION, RESULT>` must match the return type of the DAO method.

```{note}

  Collector search is a shortcut for passing a collector to a Function in stream search.
  You can achieve the same result by calling the `collect` method on the `Stream` object obtained from a stream search.

# Aggregate strategy

The `aggregateStrategy` property in `@Select` allows query results to be mapped
to hierarchical entity structures based on a predefined aggregate strategy.

```java

  @Select(aggregateStrategy = EmployeeStrategy.class)
  Employee selectByName(String name);

For more details, see [\1](\1).

```{note}

  The aggregate strategy cannot be used in combination with [\1](\1) or [\1](\1).

# Search options

By using `SelectOptions`, you can convert the SELECT statement into SQL for paging or pessimistic locking purposes.

`SelectOptions` is defined as a parameter of the DAO method.

```java

  @Dao
  public interface EmployeeDao {
      @Select
      List<Employee> selectByDepartmentName(String departmentName, SelectOptions options);
      ...
  }

You can obtain an instance of `SelectOptions` through a static `get` method.

```java

  SelectOptions options = SelectOptions.get();

## Paging

To implement pagination, use the `offset` method to specify the starting position and the `limit` method to specify the number of records to retrieve in `SelectOptions`. 
Then pass this `SelectOptions` instance to the DAO method.

```java

  SelectOptions options = SelectOptions.get().offset(5).limit(10);
  EmployeeDao dao = new EmployeeDaoImpl();
  List<Employee> list = dao.selectByDepartmentName("ACCOUNT", options);

Paging is achieved by modifying the original SQL, which must meet the following conditions: 

* it is a SELECT statement.
* it does not perform set operations like UNION, EXCEPT, or INTERSECT at the top level (though subqueries are allowed).
* it does not include paging operations.

Additionally, specific conditions must be met according to the dialect.

+------------------+-------------------------------------------------------------------------------------+
| Dialect          |    Condition                                                                        |
+==================+=====================================================================================+
| Db2Dialect       |    When specifying an offset, all columns listed in the ORDER BY clause             |
|                  |    must be included in the SELECT clause.                                           |
+------------------+-------------------------------------------------------------------------------------+
| Mssql2008Dialect |    When specifying an offset, all columns listed in the ORDER BY clause             |
|                  |    must be included in the SELECT clause.                                           |
+------------------+-------------------------------------------------------------------------------------+
| MssqlDialect     |    When specifying an offset, the ORDER BY clause is required.                      |
+------------------+-------------------------------------------------------------------------------------+
| StandardDialect  |    The ORDER BY clause is required.                                                 |
|                  |    All columns listed in the ORDER BY clause must be included in the SELECT clause. |
+------------------+-------------------------------------------------------------------------------------+

## Pessimistic concurrency control

You can indicate pessimistic concurrency control using the `forUpdate` method of `SelectOptions`.

```java

  SelectOptions options = SelectOptions.get().forUpdate();
  EmployeeDao dao = new EmployeeDaoImpl();
  List<Employee> list = dao.selectByDepartmentName("ACCOUNT", options);

`SelectOptions` provides methods for pessimistic concurrency control with names starting with `forUpdate`, 
such as `forUpdate` to specify aliases for tables or columns to be locked, 
and `forUpdateNowait` to acquire locks without waiting.

Pessimistic concurrency control is achieved by rewriting the original SQL, which must meet the following conditions:

* it is a SELECT statement.
* it does not perform set operations like UNION, EXCEPT, or INTERSECT at the top level (though subqueries are allowed).
* it does not include pessimistic concurrency control operations.


Depending on the dialect, some or all of the methods for pessimistic concurrency control may not be available for use.

+------------------+-----------------------------------------------------------------------------+
| Dialect          |    Description                                                              |
+==================+=============================================================================+
| Db2Dialect       |    You can use forUpdate().                                                 |
+------------------+-----------------------------------------------------------------------------+
| H2Dialect        |    You can use forUpdate().                                                 |
+------------------+-----------------------------------------------------------------------------+
| HsqldbDialect    |    You can use forUpdate().                                                 |
+------------------+-----------------------------------------------------------------------------+
| Mssql2008Dialect |    You can use forUpdate() and forUpdateNowait().                           |
|                  |    However, FROM clauses in original SQL must consist single table.         |
+------------------+-----------------------------------------------------------------------------+
| MysqlDialect     |    You can use forUpdate()                                                  |
+------------------+-----------------------------------------------------------------------------+
| MysqlDialect (V8)|    You can use forUpdate(), forUpdate(String... aliases),                   |
|                  |    forUpdateNowait(), and forUpdateNowait(String... aliases).               |
+------------------+-----------------------------------------------------------------------------+
| OracleDialect    |    You can use forUpdate(), forUpdate(String... aliases),                   |
|                  |    forUpdateNowait(), forUpdateNowait(String... aliases),                   |
|                  |    forUpdateWait(int waitSeconds), and                                      |
|                  |    forUpdateWait(int waitSeconds, String... aliases).                       |
+------------------+-----------------------------------------------------------------------------+
| PostgresDialect  |    You can use forUpdate() and forUpdate(String... aliases).                |
+------------------+-----------------------------------------------------------------------------+
| StandardDialect  |    You can not use all of pessimistic concurrency control method.           |
+------------------+-----------------------------------------------------------------------------+

## Count

Use the `count` method of `SelectOptions` to retrieve the total number of records. 
This is typically used with pagination to get the total record count before any pagination filtering is applied.

```java

  SelectOptions options = SelectOptions.get().offset(5).limit(10).count();
  EmployeeDao dao = new EmployeeDaoImpl();
  List<Employee> list = dao.selectByDepartmentName("ACCOUNT", options);
  long count = options.getCount();

The total count of records is obtained using the `getCount` method of `SelectOptions` after calling the DAO method. 
If the `count` method hasn't been executed before the DAO method call, the `getCount` method will return -1.

# Ensure the existence of search results

To ensure that at least one result is returned from the search, set the `ensureResult` property of the `@Select` annotation to `true`.

```java

  @Select(ensureResult = true)
  Employee selectById(Integer id);

If there are no search results, a `NoResultException` will be thrown.

# Ensure the mapping of search results

If you want to ensure that all columns of the result set are mapped to properties of the entity without missing any, 
specify `true` for the `ensureResultMapping` element of `@Select`.

```java

  @Select(ensureResultMapping = true)
  Employee selectById(Integer id);

If there are properties in the entity that are not mapped to columns in the result set, 
a `ResultMappingException` will be thrown.

# Query timeout

You can specify the query timeout in seconds using the `queryTimeout` property in the `@Select` annotation.

```java

  @Select(queryTimeout = 10)
  List<Employee> selectAll();


If the value of the `queryTimeout` property is not set, the query timeout specified in the [\1](\1) will be used.

# Fetch size

You can specify the fetch size using the `fetchSize` property in the `@Select` annotation.

```java

  @Select(fetchSize = 20)
  List<Employee> selectAll();

If the value of the `fetchSize` property is not set, the fetch size specified in the [\1](\1) will be used.

# Max row count

You can specify the maximum number of rows using the `maxRows` property in the `@Select` annotation.

```java

  @Select(maxRows = 100)
  List<Employee> selectAll();

If the value of the `maxRows` property is not set, the maximum number of rows specified in the [\1](\1) will be used.

# The naming convention for the keys of the Map

If you are mapping search results to `java.util.Map<String, Object>`, 
you can specify the naming convention for the keys of the map using the `mapKeyNaming` property of `@Select`.

```java

  @Select(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  List<Map<String, Object>> selectAll();

`MapKeyNamingType.CAMEL_CASE` indicates that the column names will be converted to camel case. 
There are also conventions to convert column names to uppercase or lowercase.

The final conversion result is determined by the value specified here and the implementation of `MapKeyNaming`
specified in the [\1](\1).

# Output format of SQL logs

You can specify the format of SQL log output using the `sqlLog` property of the `@Select` annotation.

```java

  @Select(sqlLog = SqlLogType.RAW)
  List<Employee> selectById(Integer id);

`SqlLogType.RAW` outputs the SQL statement with its binding parameters in the log.
