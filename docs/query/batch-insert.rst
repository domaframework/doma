==================
Batch insert
==================

.. contents::
   :depth: 3

Annotate with ``@BatchInsert`` to Dao method for execute batch insert.

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @BatchInsert
      int[] insert(List<Employee> employees);

      @BatchInsert
      BatchResult<ImmutableEmployee> insert(List<ImmutableEmployee> employees);
  }

By default INSERT statement is auto generated.
You can mapping arbitrary SQL file by specifying ``true`` to ``sqlFile`` property within the ``@BatchInsert`` annotation.

The ``preInsert`` method of entity listener is called each entity when before executing insert if the entity listener is specified at :doc:`../entity` parameter.
Also the ``postInsert`` method of entity listener method is called each entity when after executing insert.

Return value
=============

Return value must be ``org.seasar.doma.jdbc.BatchResult`` that has entity class as an element if parameter ``Iterable`` subtype element is immutable entity class.

Return value must be ``int[]`` that is represented each inserting process's updated count if the above conditions are not satisfied.

Batch insert by auto generated SQL
=====================================

Parameter type must be ``java.lang.Iterable`` subtype that has :doc:`../entity` as an element.
Specifiable parameter is only one.
Parameter must not be ``null``.
Return value array element count become equal ``Iterable`` element count.
Insert count is returned to array each element.

Identifier
-----------

If annotated with ``@GeneratedValue`` at :doc:`../entity` identifier, the identifier is auto generated and set.

You reference :ref:`identity-auto-generation` about cautionary point.

Version number
----------------

If value that explicitly set is over ``0`` then use the value if :doc:`../entity` has property that is annotated  with ``@Version``.
If the value is not set or is less than ``0`` the value is set ``1`` automatically.

Insert target property
-----------------------

insertable
~~~~~~~~~~

The ``insertable`` property within ``@BatchInsert`` annotation that is specified ``false`` is excluded from insert target if :doc:`../entity` has property that is annotated with ``@Column``.

exclude
~~~~~~~

Property that is specified with ``exclude`` property within the ``@BatchInsert`` annotation is excluded from inserting target.
Even if ``insertable`` property within ``@Column`` annotation is specified ``true`` the property is excluded from inserting target if the property is specified by this element.

.. code-block:: java

  @BatchInsert(exclude = {"name", "salary"})
  int[] insert(List<Employee> employees);

include
~~~~~~~

Only property that is specified with ``include`` property within ``@BatchInsert`` annotation is included to inserting target.
If same property are specified with both of ``include`` property and ``exclude`` property within ``@BatchInsert`` the property is excluded from updating target.
Even if property is specified with this element the property is excluded from inserting target if ``insertable`` property within ``@Column`` annotation is ``false``.

.. code-block:: java

  @BatchInsert(include = {"name", "salary"})
  int[] insert(List<Employee> employees);

Batch insert by SQL file
===========================

To execute batch inserting by SQL file,
you set ``true`` to ``sqlFile`` property within ``@BatchInsert`` annotation and prepare SQL file that correspond method.

.. code-block:: java

  @BatchInsert(sqlFile = true)
  int[] insert(List<Employee> employees);

  @BatchInsert(sqlFile = true)
  BatchResult<ImmutableEmployee> insert(List<ImmutableEmployee> employees);

Parameter type must be ``java.lang.Iterable`` subtype that has :doc:`../entity` as an element.
Specifiable parameter is only one.
Parameter must not be ``null``.
Return value array element count become equal ``Iterable`` element count.
Insert count is returned to array each element.

If entity listener is specified at :doc:`../entity` then entity listener method is not called.

For example, you describe SQL like below to correspond above method.

.. code-block:: sql

  insert into employee (id, name, salary, version) 
  values (/* employees.id */0, /* employees.name */'hoge', /* employees.salary */100, /* employees.version */0)

Parameter name indicate ``java.lang.Iterable`` subtype element in SQL file.

Identifier auto setting and version number auto setting are not executed in batch insert by SQL file.
Also, ``exclude`` property and ``include`` property within ``@BatchInsert`` are not referenced.

Unique constraint violation
============================

``UniqueConstraintException`` is thrown regardless of with or without using sql file if unique constraint violation is occurred.

Query timeout
==================

You can specify seconds of query timeout to ``queryTimeout`` property within ``@BatchInsert`` annotation.

.. code-block:: java

  @BatchInsert(queryTimeout = 10)
  int[] insert(List<Employee> employees);

This specifying is applied regardless of with or without using sql file.
Query timeout that is specified in config class is used if ``queryTimeout`` property is not set value.

Batch size
============

You can specify batch size to ``batchSize`` property within ``@BatchInsert`` annotation.

.. code-block:: java

  @BatchInsert(batchSize = 10)
  int[] insert(List<Employee> employees);

This specify is applied Regardless of using or not using SQL file.
It you do not specify the value to ``batchSize`` property, batch size that is specified at :doc:`../config` class is applied.

SQL log output format
=====================

You can specify SQL log output format to ``sqlLog`` property within ``@BatchInsert`` annotation.

.. code-block:: java

  @BatchInsert(sqlLog = SqlLogType.RAW)
  int insert(Employee employee);

``SqlLogType.RAW`` represent outputting log that is sql with a binding parameter.
