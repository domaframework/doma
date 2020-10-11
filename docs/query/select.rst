===============
Search
===============

.. contents::
   :depth: 3

Annotate with ``@Select`` to Dao method for execute search.

.. code-block:: java

  @Dao
  public interface EmployeeDao {
      @Select
      List<Employee> selectByDepartmentName(String departmentName);
      ...
  }

**SQL file is required** in search.
There is no feature that auto generating search SQL.

.. note::

  You need creating entity class **depending on search result**.
  For example, result set including column in EMPLOYEE table is accepted Employee class if the Employee class that correspond EMPLOYEE table is declared.
  But, you need different class from the Employee entity class(For example EmmployeeDepartment class) for result set that is get by joining EMPLOYEE table and DEPARTMENT table.

Query condition
=================

You use method parameters for query condition.
Available types is below.

* :doc:`../basic`
* :doc:`../domain`
* Arbitrary type
* :doc:`../basic` , :doc:`../domain` or arbitrary type are within java.util.Optional
* :doc:`../basic` or :doc:`../domain` are within java.util.Iterable
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

Parameters count is no limit.
You can set ``null`` to parameter if parameter type is :doc:`../basic` or :doc:`../domain`.
Parameter must not be ``null`` if the type is other than that.

Query that is used basic type or domain class
----------------------------------------------

You declare :doc:`../basic` or :doc:`../domain` to method or parameter.

.. code-block:: java

  @Select
  List<Employee> selectByNameAndSalary(String name, Salary salary);

You map method parameter to SQL by using SQL comment in SQL file.
In SQL comment, method parameter name is referenced.

.. code-block:: sql

  select * from employee where employee_name = /* name */'hoge' and salary > /* salary */100

Query that is used arbitrary type
----------------------------------

You map to SQL by access field or call method there are using by dot ``.`` if using arbitrary parameter type in method parameter.

.. code-block:: java

  @Select
  List<Employee> selectByExample(Employee employee);

.. code-block:: sql

  select * from employee where employee_name = /* employee.name */'hoge' and salary > /* employee.getSalary() */100

You can specify multiple parameter.

.. code-block:: java

  @Select
  List<Employee> selectByEmployeeAndDepartment(Employee employee, Department department);

Mapping to IN clauses by using Iterable.
-----------------------------------------

You use subtype of ``java.lang.Iterable`` if excute searching by using IN clauses.

.. code-block:: java

  @Select
  List<Employee> selectByNames(List<String> names);

.. code-block:: sql

  select * from employee where employee_name in /* names */('aaa','bbb','ccc')

Single record search
=====================

You specify method return value type either of below for search single record.

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* Either :doc:`../basic` , :doc:`../domain` , :doc:`../entity` or java.util.Map<String, Object>
  is within java.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

.. code-block:: java

  @Select
  Employee selectByNameAndSalary(String name, BigDecimal salary);

``null`` is return if return type is not ``Optional`` and result count is 0.
If `Ensure of search result`_ is enabled, exception is thrown regardless return value type if search count is 0.

``NonUniqueResultException`` is thrown if result exists 2 or more.

Multiple record search
========================

You specify ``java.util.List`` to method return value type to for search multiple record.
You can use below property in ``List``.

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* Either :doc:`../basic` or :doc:`../domain` is within java.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

.. code-block:: java

  @Select
  List<Employee> selectByNameAndSalary(String name, Salary salary);

Empty list instead of ``null`` is return if result count is 0.
But if `Ensure of search result`_ is enabled, exception is thrown if search count is 0.

Stream search
==============

You can use stream search if  handle all record at one try as ``java.util.stream.Stream`` rather than recieve as ``java.util.List``.

There are two kind in stream search such as return the return value and pass ``Stream`` to ``java.util.Function``.

Pass to the Function
---------------------------

You set ``SelectType.STREAM`` to ``strategy`` property within ``@Select`` annotation and
define subtype that is ``java.util.Function<Stream<TARGET>, RESULT>`` or ``java.util.Function<Stream<TARGET>, RESULT>`` to method parameter.

.. code-block:: java

  @Select(strategy = SelectType.STREAM)
  BigDecimal selectByNameAndSalary(String name, BigDecimal salary, Function<Stream<Employee>, BigDecimal> mapper);

Caller receive stream and pass lambda expression that return result.

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  BigDecimal result = dao.selectByNameAndSalary(name, salary, stream -> {
      return ...;
  });

``Function<Stream<TARGET>, RESULT>`` corresponding type parameter ``TARGET`` must be either of below.

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* Either :doc:`../basic` or :doc:`../domain` is within java.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

Type parameter ``RESULT`` must match to Dao method return value.

If `Ensure of search result`_ is enabled, exception is thrown if search count is 0.

Return the return value
---------------------------

You define ``java.util.stream.Stream`` to method return value.
You can use following type at property within ``Stream``.

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* Either :doc:`../basic` or :doc:`../domain` within java.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

.. code-block:: java

  @Select
  Stream<Employee> selectByNameAndSalary(String name, BigDecimal salary);

Below is a caller.

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  try (Stream<Employee> stream = dao.selectByNameAndSalary(name, salary)) {
    ...
  }

If `Ensure of search result`_ is enabled, exception is thrown if search count is 0.

.. warning::

  Make sure to close the stream for prevent forgetting of release the resource.
  If you do not close the stream, ``java.sql.ResultSet``  or ``java.sql.PreparedStatement`` ,
  ``java.sql.Connection`` those are not closing.

.. note::

  Consider adoption of pass to Function unless there is some particular reason,
  because return the return value has the risk that is forgetting of release the resource.
  Doma display warning message at Dao method for attention.
  You specify ``@Suppress`` below for suppress warning.

  .. code-block:: java

    @Select
    @Suppress(messages = { Message.DOMA4274 })
    Stream<Employee> selectByNameAndSalary(String name, BigDecimal salary);

Collect search
===============

You can use collect search if handle result as ``java.util.Collector``.

You set ``SelectType.COLLECT`` to ``strategy`` property within ``@Select`` annotation and
define subtype that is ``java.stream.Collector<TARGET, ACCUMULATION, RESULT>`` or ``java.stream.Collector<TARGET, ?, RESULT>`` to method parameter.

.. code-block:: java

  @Select(strategy = SelectType.COLLECT)
  <RESULT> RESULT selectBySalary(BigDecimal salary, Collector<Employee, ?, RESULT> collector);

Caller pass ``Collector`` instance.

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  Map<Integer, List<Employee>> result =
      dao.selectBySalary(salary, Collectors.groupingBy(Employee::getDepartmentId));

``Collector<TARGET, ACCUMULATION, RESULT>`` corresponding type parameter ``TARGET`` must be either of below.

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* Either :doc:`../basic` or :doc:`../domain` within java.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

Type parameter ``RESULT`` must match Dao method return value.

If `Ensure of search result`_ is enabled, exception is thrown if search count is 0.

.. note::

  Collect search is the shortcut that pass to Function within stream search.
  You can do equivalent by using `collect`` method in ``Stream`` object that is getting from stream search.

Using search option search
============================

You can automatically generate SQL for paging and pessimistic concurrency control from SQL file that is wrote SELECT clauses
by you use ``SelectOptions`` that is represent search option.

You use ``SelectOptions`` in combination with `Single record search`_ ,  `Multiple record search`_ ,  `Stream search`_

You define ``SelectOptions`` as Dao method parameter.

.. code-block:: java

  @Dao
  public interface EmployeeDao {
      @Select
      List<Employee> selectByDepartmentName(String departmentName, SelectOptions options);
      ...
  }

You can get ``SelectOptions`` instance by static ``get`` method.

.. code-block:: java

  SelectOptions options = SelectOptions.get();

Paging
----------

You specify start position by ``offset`` method and get count by ``limit`` method those are within ``SelectOptions``,
and pass the ``SelectOptions`` instance to Dao method.

.. code-block:: java

  SelectOptions options = SelectOptions.get().offset(5).limit(10);
  EmployeeDao dao = new EmployeeDaoImpl();
  List<Employee> list = dao.selectByDepartmentName("ACCOUNT", options);

Paging is materialized by rewriting original SQL writing in file and executing.
Original SQL must be satisfied condition below.

* SQL is SELECT clauses
* In top level, set operation is not executed like UNION, EXCEPT, INTERSECT.(But using at subquery is able)
* Paging process is not included.

In addition, particular condition must be satisfied according to the database dialect.

If specify offset, there are ORDER BY clauses and all column that is specified at ORDER BY clauses is included in SELECT clauses.

+------------------+-------------------------------------------------------------------------------------+
| Dialect          |    Condition                                                                        |
+==================+=====================================================================================+
| Db2Dialect       |    If specify offset, there are ORDER BY clauses and                                |
|                  |    all column that is specified at ORDER BY clauses is included in SELECT clauses.  |
+------------------+-------------------------------------------------------------------------------------+
| Mssql2008Dialect |    If specify offset, there are ORDER BY clauses and                                |
|                  |    all column that is specified at ORDER BY clauses is included in SELECT clauses.  |
+------------------+-------------------------------------------------------------------------------------+
| MssqlDialect     |    If specify offset, there are ORDER BY clauses.                                   |
+------------------+-------------------------------------------------------------------------------------+
| StandardDialect  |    There are ORDER BY clauses and                                                   |
|                  |    all column that is specified at ORDER BY clauses is included in SELECT clauses.  |
+------------------+-------------------------------------------------------------------------------------+

Pessimistic concurrency control
---------------------------------

You indicate executing pessimistic concurrency control by ``forUpdate`` within ``SelectOptions``,
and pass the SelectOptions instance to Dao method.

.. code-block:: java

  SelectOptions options = SelectOptions.get().forUpdate();
  EmployeeDao dao = new EmployeeDaoImpl();
  List<Employee> list = dao.selectByDepartmentName("ACCOUNT", options);

The method that name is started *forUpdate* for pessimistic concurrency control is prepared
such as ``forUpdateNowait`` method that do not wait for getting lock
and ``forUpdate`` method that can specify lock target table or column alias.

Pessimistic concurrency control is executed by rewriting original SQL writing in file.
Original SQL must be satisfied condition below.

* SQL is SELECT clauses
* In top level, set operation is not executed like UNION, EXCEPT, INTERSECT.(But using at subquery is able)
* Pessimistic concurrency control process is not included.

Part or all of pessimistic concurrency control method can not used according to the database dialect.

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
| OracleDialect    |    You can use forUpdate(), forUpdate(String... aliases),                   |
|                  |    forUpdateNowait(), forUpdateNowait(String... aliases),                   |
|                  |    forUpdateWait(int waitSeconds),                                          |
|                  |    forUpdateWait(int waitSeconds, String... aliases).                       |
+------------------+-----------------------------------------------------------------------------+
| PostgresDialect  |    You can use forUpdate() and forUpdate(String... aliases).                |
+------------------+-----------------------------------------------------------------------------+
| StandardDialect  |    You can not use all of pessimistic concurrency control method.           |
+------------------+-----------------------------------------------------------------------------+

Aggregate
---------

You can get aggregate count by calling ``count`` method within ``SelectOptions``.
Usually, you use combination in paging option and use in case of getting all count if not narrowing by paging.

.. code-block:: java

  SelectOptions options = SelectOptions.get().offset(5).limit(10).count();
  EmployeeDao dao = new EmployeeDaoImpl();
  List<Employee> list = dao.selectByDepartmentName("ACCOUNT", options);
  long count = options.getCount();

Aggregate count is get by using ``getCount`` method within ``SelectOptions`` after calling Dao method.
The ``getCount`` method is return ``-1`` if you do not execute ``count`` method before calling method.

Ensure of search result
========================

You specify ``true`` to ``ensureResult`` property within ``@Select`` annotation if you want to ensure of search result count is over 1.

.. code-block:: java

  @Select(ensureResult = true)
  Employee selectById(Integer id);

``NoResultException`` is thrown if search result count is 0.

Ensure of mapping search result
================================

You specify ``true`` to ``ensureResultMapping`` property within ``@Select`` annotation,
if you want ensure that mapping result set column to all entity properties without exception.

.. code-block:: java

  @Select(ensureResultMapping = true)
  Employee selectById(Integer id);

``ResultMappingException`` is thrown if there are property that is not mapping to result set column.

Query timeout
==================

You can specify seconds of query timeout to ``queryTimeout`` property within ``@Update`` annotation.

.. code-block:: java

  @Select(queryTimeout = 10)
  List<Employee> selectAll();

Query timeout that is specified in :doc:`../config` is used if ``queryTimeout`` property is not set value.

Fetch size
==============

You can specify fetch size to ``fetchSize`` property within ``@Select`` annotation.

.. code-block:: java

  @Select(fetchSize = 20)
  List<Employee> selectAll();

Fetch size that is specified in :doc:`../config` is used if value is not set.

Max row count
===============

You can specify max row count to ``maxRows`` property within ``@Select`` annotation.

.. code-block:: java

  @Select(maxRows = 100)
  List<Employee> selectAll();

Max row count that is is specified in :doc:`../config` is used if value is not set.

Naming rule of map's key
============================

You can specify naming rule of map's key to ``mapKeyNaming`` property within ``@Select`` annotation,
if you want mapping search result to ``java.util.Map<String, Object>``.

.. code-block:: java

  @Select(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  List<Map<String, Object>> selectAll();

``MapKeyNamingType.CAMEL_CASE`` present converting column name to camel case.
In addition to there are rule that converting upper case or lower case.

The final conversion result is decide by value specified here and implementation of ``MapKeyNaming`` is specified at :doc:`../config`.

SQL log output format
======================

You can specify SQL log output format to ``sqlLog`` property within ``@Select`` annotation.

.. code-block:: java

  @Select(sqlLog = SqlLogType.RAW)
  List<Employee> selectById(Integer id);

``SqlLogType.RAW`` represent outputting log that is sql with a binding parameter.
