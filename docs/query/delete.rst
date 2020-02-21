==================
Delete
==================

.. contents::
   :depth: 3

Annotate with ``@Delete`` to Dao method for execute delete.

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @Delete
      int delete(Employee employee);
  }

By default DELETE statement is auto generated.
You can mapping arbitrary SQL file by specifying ``true`` to ``sqlFile`` property within the ``@Delete`` annotation.

The ``preDelete`` method of entity listener is called when before executing delete if the entity listener is specified at entity class parameter.
Also the ``postDelete`` method of entity listener is called when after executing delete.

Return value
============

Return value must be ``org.seasar.doma.jdbc.Result`` that make the entity class an element if parameter is immutable entity class.

Return value must be ``int`` that is represented updated count if the above conditions are not satisfied.

Delete by auto generated SQL
=============================

Parameter type must be entity class.
Specifiable parameter is only one.
Parameter must not be ``null``.

.. code-block:: java

  @Delete
  int delete(Employee employee);

  @Delete
  Result<ImmutableEmployee> delete(ImmutableEmployee employee);

Version number and optimistic concurrency control in auto generated SQL
-----------------------------------------------------------------------

Optimistic concurrency control is executed if you satisfied below conditions.

* Entity class within parameter has property that is annotated with @Version
* The ignoreVersion element within @Delete annotation is false

If optimistic concurrency control is enable, version number is included with identifier in delete condition.
``OptimisticLockException`` representing optimistic concurrency control failure is thrown, if at that time delete count is 0.

ignoreVersion
~~~~~~~~~~~~~

If ``ignoreVersion`` property within ``@Delete`` annotation is true, version number is not include in delete condition.
``OptimisticLockException`` is not thrown in this case, even if delete count is 0.

.. code-block:: java

  @Delete(ignoreVersion = true)
  int delete(Employee employee);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

If ``suppressOptimisticLockException`` property within ``@Delete`` is ``true``, version number is included in delete condition.
But in this case ``OptimisticLockException`` is not thrown even if delete count is 0.

.. code-block:: java

  @Delete(suppressOptimisticLockException = true)
  int delete(Employee employee);

Delete by SQL file
===========================

To execute deleting by SQL file, you set ``true`` to ``sqlFile`` property within ``@Delete`` annotation and prepare SQL file that correspond method.


You can use arbitrary type as parameter.
Specifiable parameters count is no limit.
You can set ``null`` to parameter if parameter type is basic type or domain class.
Parameter must not be ``null`` if the type is other than that.

Entity listener method is not called even if the entity listener is specified to entity.

.. code-block:: java

  @Delete(sqlFile = true)
  int delete(Employee employee);

For example, you describe SQL file like below to correspond above method.

.. code-block:: sql

  delete from employee where name = /* employee.name */'hoge'

Version number and optimistic concurrency control in  SQL File
--------------------------------------------------------------

Optimistic concurrency control is executed if you satisfied below conditions.

* Entity class is included in parameter
* Entity class at first from the left within parameter has property that is annotated with @Version
* The ignoreVersion property within @Delete annotation is false
* The suppressOptimisticLockException property within @Delete annotation is false

However, describing to SQL file for Optimistic concurrency control SQL is application developer's responsibility.
For example like below SQL, you must specify version number in WHERE clauses.

.. code-block:: sql

  delete from EMPLOYEE where ID = /* employee.id */1 and VERSION = /* employee.version */1

``OptimisticLockException`` representing optimistic concurrency control failure is thrown, if this SQL delete count is 0.
``OptimisticLockException`` is not thrown if delete count is not 0.

ignoreVersion
~~~~~~~~~~~~~

If ``ignoreVersion`` property within ``@Delete`` annotation is ``true``,
``OptimisticLockException`` is not thrown even if delete count is 0.

.. code-block:: java

  @Delete(sqlFile = true, ignoreVersion = true)
  int delete(Employee employee);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

If ``suppressOptimisticLockException`` property within ``@Delete`` annotation is ``true``,
``OptimisticLockException`` is not thrown even if delete count is 0.

.. code-block:: java

  @Delete(sqlFile = true, suppressOptimisticLockException = true)
  int delete(Employee employee);

Query timeout
==================


You can specify seconds of query timeout to ``queryTimeout`` property within ``@Delete`` annotation.

.. code-block:: java

  @Delete(queryTimeout = 10)
  int delete(Employee employee);

This specifying is applied regardless of with or without using sql file.
Query timeout that is specified in :doc:`../config` is used if ``queryTimeout`` property is not set value.

SQL log output format
=====================

You can specify SQL log output format to ``sqlLog`` property within ``@Delete`` annotation.

.. code-block:: java

  @Delete(sqlLog = SqlLogType.RAW)
  int delete(Employee employee);

``SqlLogType.RAW`` represent outputting log that is sql with a binding parameter.
