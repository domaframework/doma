=============
SQL templates
=============

.. contents::
   :depth: 3

Overview
========

Doma supports SQL templates, called "two-way SQL".
"Two-way SQL" means that the SQL templates can be used in two ways:

* To build dynamic SQL statements from the templates.
* To execute the templates in SQL tools as they are.

Every SQL template must correspond to a DAO method.
For example, suppose you have the pair of an SQL template and a DAO method as follows:

.. code-block:: sql

  select * from employee where employee_id = /* employeeId */99

.. code-block:: java

  Employee selectById(Integer employeeId);

The ``employeeId`` expression enclosed between ``/*`` and ``*/`` corresponds to
the method parameter "employeeId" of the DAO.
In runtime, the SQL comment and following number ``/* employeeId */99`` is replaced with a bind variable ``?``
and the method parameter "employeeId" is passed to the variable.
The SQL statement generated from the SQL template is as follows:

.. code-block:: sql

  select * from employee where employee_id = ?

The number ``99`` in the SQL template is test data and never used in runtime.
The test data is only useful when you execute the SQL template as is.
In other words, you can check whether the SQL template is grammatically correct with your favorite SQL tools.

Each SQL template is represented either a text file or an annotation.

SQL templates in files
======================

You can specify SQL templates in text files:

.. code-block:: java

  @Dao
  public interface EmployeeDao {
    @Select
    Employee selectById(Integer employeeId);

    @Delete(sqlFile = true)
    int deleteByName(Employee employee);
  }

Above ``selectById`` and ``deleteByName`` methods are mapped onto their own SQL files.
DAO methods must be annotated with one of following annotations:

* @Select
* @Insert(sqlFile = true)
* @Update(sqlFile = true)
* @Delete(sqlFile = true)
* @BatchInsert(sqlFile = true)
* @BatchUpdate(sqlFile = true)
* @BatchDelete(sqlFile = true)

Encoding
--------

The SQL files must be saved as UTF-8 encoded.

Location
--------

The SQL files must be located in directories below a "META-INF" directory which is included in CLASSPATH.

Format of file path
-------------------

The SQL file path must follow the following format:

  META-INF/*path-format-of-dao-interface*/*dao-method*.sql


For example, when the DAO interface name is ``aaa.bbb.EmployeeDao`` and the DAO method name is ``selectById``,
the SQL file path is as follows:

  META-INF/aaa/bbb/EmployeeDao/selectById.sql

.. _dependency-on-a-specific-rdbms:

Dependency on a specific RDBMS
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

You can specify dependency on a specific RDBMS by file name.
To do this, put the hyphen "-" and RDBMS name before the extension ".sql".
For example, the file path specific to PostgreSQL is as follows:

  META-INF/aaa/bbb/EmployeeDao/selectById-*postgres*.sql

The SQL files specific to RDBMSs are given priority.
For example, in the environment where PostgreSQL is used,
"META-INF/aaa/bbb/EmployeeDao/selectById-postgres.sql"
is chosen instead of "META-INF/aaa/bbb/EmployeeDao/selectById.sql".

The RDBMS names are stem from dialects:

+----------------------------+------------------+------------+
| RDBMS                      | Dialect          | RDBMS Name |
+============================+==================+============+
| DB2                        | Db2Dialect       | db2        |
+----------------------------+------------------+------------+
| H2 Database                | H2Dialect        | h2         |
+----------------------------+------------------+------------+
| HSQLDB                     | HsqldbDialect    | hsqldb     |
+----------------------------+------------------+------------+
| Microsoft SQL Server       | MssqlDialect     | mssql      |
+----------------------------+------------------+------------+
| MySQL                      | MySqlDialect     | mysql      |
+----------------------------+------------------+------------+
| Oracle Database            | OracleDialect    | oracle     |
+----------------------------+------------------+------------+
| PostgreSQL                 | PostgresDialect  | postgres   |
+----------------------------+------------------+------------+
| SQLite                     | SqliteDialect    | sqlite     |
+----------------------------+------------------+------------+

.. _sql-templates-in-annotations:

SQL templates in annotations
============================

You can specify SQL templates to DAO methods with the ``@Sql`` annotation:

.. code-block:: java

  @Dao
  public interface EmployeeDao {
    @Sql("select * from employee where employee_id = /* employeeId */99")
    @Select
    Employee selectById(Integer employeeId);

    @Sql("delete from employee where employee_name = /* employee.employeeName */'aaa'")
    @Delete
    int deleteByName(Employee employee);
  }


The ``@Sql`` annotation must be combined with following annotations:

* @Select
* @Script
* @Insert
* @Update
* @Delete
* @BatchInsert
* @BatchUpdate
* @BatchDelete

.. warning::

  The ``@Sql`` annotation is an experimental feature.
  The full qualified name of ``@Sql`` is ``@org.seasar.doma.experimental.Sql``.

Directives
==========

In SQL templates, the SQL comments following the specific rules are recognised as directives.
Supported directives are as follows:

* `Bind variable directive`_
* `Literal variable directive`_
* `Embedded variable directive`_
* `Condition directive`_
* `Loop directive`_
* `Expansion directive`_
* `Population directive`_

.. note::

  See also :doc:`expression` for information of the expression language available in directives.

Bind variable directive
-----------------------

Bind variable directive is represented with the format ``/*...*/``.
The expression enclosed between ``/*`` and ``*/`` is evaluated and
its evaluation result is passed to bind variable in SQL statement.
The directive must be followed by test data, which is never used in runtime.

Basic and domain parameters
~~~~~~~~~~~~~~~~~~~~~~~~~~~

The parameter whose type is one of :doc:`basic` and :doc:`domain`
is recognised as a bind variable.

The following example is the pair of a DAO method and an SQL template:

.. code-block:: java

   Employee selectById(Integer employeeId);

.. code-block:: sql

   select * from employee where employee_id = /* employeeId */99

The following SQL statement is generated from the SQL template:

.. code-block:: sql

   select * from employee where employee_id = ?

Parameters in IN clause
~~~~~~~~~~~~~~~~~~~~~~~

The parameter whose type is a subtype of ``java.lang.Iterable`` or an array type is
recognised as bind variables in IN clause.
The type argument of ``java.lang.Iterable`` must be one of :doc:`basic` and :doc:`domain`.
The directives must be followed by test data enclosed between ``(`` and ``)``.

The following example is the pair of a DAO method and an SQL template:

.. code-block:: java

  List<Employee> selectByIdList(List<Integer> employeeIdList);

.. code-block:: sql

  select * from employee where employee_id in /* employeeIdList */(1,2,3)

In case that the ``employeeIdList`` contains five elements,
the following SQL statement is generated from the SQL template:

.. code-block:: sql

  select * from employee where employee_id in (?, ?, ?, ?, ?)

In case that the ``employeeIdList`` is empty,
the IN clause is replaced with ``in (null)`` in runtime:

.. code-block:: sql

  select * from employee where employee_id in (null)

Literal variable directive
--------------------------

Literal variable directive is represented with the format ``/*^...*/``.
The expression enclosed between ``/*^`` and ``*/`` is evaluated and
its evaluation result is converted to literal format to be embedded in SQL statement.
The directive must be followed by test data, which is never used in runtime.

The following example is the pair of a DAO method and an SQL template:

.. code-block:: java

   Employee selectByCode(String code);

.. code-block:: sql

   select * from employee where code = /*^ code */'test'

The DAO method is invoked as follows:

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  List<Employee> list = dao.selectByCode("abc");

The generated SQL statement is as follows:

.. code-block:: sql

  select * from employee where code = 'abc'

.. note::

  Literal variable directives are helpful to avoid bind variables and fix SQL plans.

.. warning::

  Literal variable directives do not escape parameters for SQL injection.
  But the directives reject parameters containing the single quotation ``'``.

Embedded variable directive
---------------------------

Embedded variable directive is represented with the format ``/*#...*/``.
The expression enclosed between ``/*#`` and ``*/`` is evaluated and
its evaluation result is embedded in SQL statement.

The following example is the pair of a DAO method and an SQL template:

.. code-block:: java

  List<Employee> selectAll(BigDecimal salary, String orderyBy);

.. code-block:: sql

  select * from employee where salary > /* salary */100 /*# orderBy */

The DAO method is invoked as follows:

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  BigDecimal salary = new BigDecimal(1000);
  String orderBy = "order by salary asc, employee_name";
  List<Employee> list = dao.selectAll(salary, orderBy);

The generated SQL statement is as follows:

.. code-block:: sql

  select * from employee where salary > ? order by salary asc, employee_name

.. note::

  Embedded variable directives are helpful to build SQL fragments such as ORDER BY clause.

.. warning::

  To prevent SQL injection vulnerabilities,
  embedded variable directives reject parameters containing the following values:

  * a single quotation ``'``
  * a semi colon ``;``
  * two hyphen ``--``
  * a slash and an asterisk ``/*``

Condition directive
-------------------

Condition directive allows you to build SQL statements conditionally.

Synopsis
~~~~~~~~

.. code-block:: sql

  /*%if condition*/
    ...
  /*%elseif condition2*/
    ...
  /*%elseif condition3*/
    ...
  /*%else*/
    ...
  /*%end*/

The expressions ``condition``, ``condition2``, and ``condition3`` must be evaluated
to either ``boolean`` or ``java.lang.Boolean``.

The ``elseif`` directives and the ``else`` directive are optional.

if
~~

Suppose you have the following SQL template:

.. code-block:: sql

  select * from employee where
  /*%if employeeId != null */
      employee_id = /* employeeId */99
  /*%end*/

If the ``employeeId`` is not ``null``, the generated SQL statement is as follows:

.. code-block:: sql

  select * from employee where employee_id = ?

If the ``employeeId`` is ``null``, the generated SQL statement is as follows:

.. code-block:: sql

  select * from employee

The SQL keyword ``where`` is removed automatically.

elseif and else
~~~~~~~~~~~~~~~

Suppose you have the following SQL template:

.. code-block:: sql

  select
    *
  from
    employee
  where
  /*%if employeeId != null */
    employee_id = /* employeeId */9999
  /*%elseif department_id != null */
    and
    department_id = /* departmentId */99
  /*%else*/
    and
    department_id is null
  /*%end*/

If the ``employeeId != null`` is evaluated ``true``, the generated SQL statement is as follows:

.. code-block:: sql

  select
    *
  from
    employee
  where
    employee_id = ?

If the ``employeeId == null && department_id != null`` is evaluated ``true``,
the generated SQL statement is as follows:

.. code-block:: sql

  select
    *
  from
    employee
  where
    department_id = ?

The SQL keyword ``and`` followed by ``department_id`` is remove automatically:

If the ``employeeId == null && department_id == null`` is evaluated ``true``,
the generated SQL statement is as follows:

.. code-block:: sql

  select
    *
  from
    employee
  where
    department_id is null

The SQL keyword ``and`` followed by ``department_id`` is remove automatically:

Nested condition directive
~~~~~~~~~~~~~~~~~~~~~~~~~~

You can nest condition directives as follows:

.. code-block:: sql

  select * from employee where
  /*%if employeeId != null */
    employee_id = /* employeeId */99
    /*%if employeeName != null */
      and
      employee_name = /* employeeName */'hoge'
    /*%else*/
      and
      employee_name is null
    /*%end*/
  /*%end*/

Removal of clauses on the condition directive
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Following clauses can become unnecessary on the condition directive:

* WHERE
* HAVING
* ORDER BY
* GROUP BY

In the case, they are removed automatically.

Suppose you have the following SQL template:

.. code-block:: sql

  select * from employee where
  /*%if employeeId != null */
      employee_id = /* employeeId */99
  /*%end*/

If the ``employeeId != null`` is evaluated ``false``,
the generated SQL statement is as follows:

.. code-block:: sql

  select * from employee

Because the SQL clause ``where`` followed by ``/*%if ...*/`` is unnecessary,
it is removed automatically.

Removal of AND and OR keywords on the condition directives
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

AND and OR keywords can become unnecessary on the condition directive.
In the case, they are removed automatically.

Suppose you have the following SQL template:

.. code-block:: sql

  select * from employee where
  /*%if employeeId != null */
      employee_id = /* employeeId */99
  /*%end*/
  and employeeName like 's%'

If the ``employeeId != null`` is evaluated ``false``,
the generated SQL statement is as follows:

.. code-block:: sql

  select * from employee where employeeName like 's%'

Because the SQL keyword ``and`` following ``/*%end*/`` is unnecessary,
it is removed automatically.

Restriction on condition directive
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

``/*%if condition*/`` and ``/*%end*/`` must be included in
same SQL clause and in same statement level.

The following template is invalid, because ``/*%if condition*/`` is
in the FROM clause and ``/*%end*/`` is in the WHERE clause:

.. code-block:: sql

  select * from employee /*%if employeeId != null */
  where employee_id = /* employeeId */99 /*%end*/

The following template is invalid, because ``/*%if condition*/`` is
in the outer statement and ``/*%end*/`` is in the inner statement:

.. code-block:: sql

  select * from employee
  where employee_id in /*%if departmentId != null */(select ...  /*%end*/ ...)

Loop directive
--------------

Loop directive allows you to build SQL statements using loop.

Synopsis
~~~~~~~~

.. code-block:: sql

  /*%for item : sequence*/
    ...
  /*%end*/

The ``item`` is the loop variable.
The expression ``sequence`` must be evaluated to a subtype of ``java.lang.Iterable`` or an array type.

In the inside between ``/*%for item : sequence*/`` and ``/*%end*/``,
two extra loop variables are available:

:item_index: The index (0-based number) of the current item in the loop
:item_has_next: Boolean value that tells if the current item is the last in the sequence or not

The prefix ``item`` indicates the name of the loop variable.

for and item_has_next
~~~~~~~~~~~~~~~~~~~~~

Suppose you have the following SQL template:

.. code-block:: sql

  select * from employee where
  /*%for name : names */
  employee_name like /* name */'hoge'
    /*%if name_has_next */
  /*# "or" */
    /*%end */
  /*%end*/

If the sequence ``names`` contains three items,
the generated SQL statement is as follows:

.. code-block:: sql

  select * from employee where
  employee_name like ?
  or
  employee_name like ?
  or
  employee_name like ?

Removal of clauses on the loop directive
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Following clauses can become unnecessary on the loop directive:

* WHERE
* HAVING
* ORDER BY
* GROUP BY

In the case, they are removed automatically.

Suppose you have the following SQL template:

.. code-block:: sql

  select * from employee where
  /*%for name : names */
  employee_name like /* name */'hoge'
    /*%if name_has_next */
  /*# "or" */
    /*%end */
  /*%end*/

If the sequence ``names`` is empty,
the generated SQL statement is as follows:

.. code-block:: sql

  select * from employee

Because the SQL clause ``where`` followed by ``/*%for ...*/`` is unnecessary,
it is removed automatically.

Removal of AND and OR keywords on the loop directive
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

AND and OR keywords can become unnecessary on the loop directive.
In the case, they are removed automatically.

Suppose you have the following SQL template:

.. code-block:: sql

  select * from employee where
  /*%for name : names */
  employee_name like /* name */'hoge'
    /*%if name_has_next */
  /*# "or" */
    /*%end */
  /*%end*/
  or
  salary > 1000

If the sequence ``names`` is empty,
the generated SQL statement is as follows:

.. code-block:: sql

  select * from employee where salary > 1000

Because the SQL keyword ``or`` following ``/*%end*/`` is unnecessary,
it is removed automatically.

Restriction on loop directive
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

``/*%for ...*/`` and ``/*%end*/`` must be included in
same SQL clause and in same statement level.

See also `Restriction on condition directive`_.

Expansion directive
-------------------

Expansion directive allows you to build column list of SELECT clause from the definition of :doc:`entity`.

Synopsis
~~~~~~~~

.. code-block:: sql

  /*%expand alias*/

The expression ``alias`` is optional.
If it is specified, it must be evaluated to ``java.lang.String``.

The directive must be followed by the asterisk ``*``.

expand
~~~~~~

Suppose you have the following SQL template and the entity class mapped to the template:

.. code-block:: sql

  select /*%expand*/* from employee

.. code-block:: java

   @Entity
   public class Employee {
       Integer id;
       String name;
       Integer age;
   }

The generated SQL statement is as follows:

.. code-block:: sql

  select id, name, age from employee

If you specify an alias to the table, specify same alias to the expansion directive:

.. code-block:: sql

  select /*%expand "e" */* from employee e

The generated SQL statement is as follows:

.. code-block:: sql

  select e.id, e.name, e.age from employee e

.. _populate:

Population directive
--------------------

Population directive allows you to build column list of
UPDATE SET clause from the definition of :doc:`entity`.

Synopsis
~~~~~~~~

.. code-block:: sql

  /*%populate*/

populate
~~~~~~~~

Suppose you have the following SQL template and the entity class mapped to the template:

.. code-block:: sql

  update employee set /*%populate*/ id = id where age < 30

.. code-block:: java

   @Entity
   public class Employee {
       Integer id;
       String name;
       Integer age;
   }

The generated SQL statement is as follows:

.. code-block:: sql

  update employee set id = ?, name = ?, age = ? where age < 30

Comments
========

This section show you how to distinguish between directives and normal SQL comments.

Single line comment
-------------------

Always the string consisting of two hyphens ``--`` is a single line comment.
It is never directive.

Multi line comment
------------------

If the character following ``/*`` is not permissible as the first character in a Java identifier
and it is neither ``%``, ``#``, ``@``, ``"`` nor ``'``,
the ``/*`` is beginning of a multi line comment.

The followings are the beginning of a multi line comment:

* /\*\*...\*/
* /\*+...\*/
* /\*=...\*/
* /\*:...\*/
* /\*;...\*/
* /\*(...\*/
* /\*)...\*/
* /\*&...\*/

In other hand, the followings are the beginning of a directive:

* /\* ...\*/
* /\*a...\*/
* /\*$...\*/
* /\*@...\*/
* /\*"...\*/
* /\*'...\*/
* /\*#...\*/
* /\*%...\*/

.. note::

  We recommend you always use ``/**...*/`` to begin multi line comments because it is simple.
