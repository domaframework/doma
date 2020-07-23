==================
Batch delete
==================

.. contents::
   :depth: 3

Annotate with ``@BatchDelete`` to Dao method for execute batch delete.

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @BatchDelete
      int[] delete(List<Employee> employees);
      ...
  }

By default DELETE statement is auto generated.
You can mapping arbitrary SQL file by specifying ``true`` to ``sqlFile`` property within the ``@BatchDelete`` annotation.

The ``preDelete`` method of entity listener is called each entity when before executing delete if the entity listener is specified at :doc:`../entity` parameter.
Also the ``postDelete`` method of entity listener method is called each entity when after executing delete.

Return value
==============

Return value must be ``org.seasar.doma.jdbc.BatchResult`` that has entity class as an element if parameter ``Iterable`` subtype element is immutable entity class.

Return value must be ``int[]`` that is represented each deleting process's updated count if the above conditions are not satisfied.

Batch delete by auto generated SQL
====================================

Parameter type must be ``java.lang.Iterable`` subtype that has :doc:`../entity` as an element.
Specifiable parameter is only one.
Parameter must not be ``null``.
Return value array element count become equal ``Iterable`` element count.
Delete count is returned to array each element.

Version number and optimistic concurrency control in auto generated SQL
-----------------------------------------------------------------------------

Optimistic concurrency control is executed if you satisfied below conditions.

* :doc:`../entity` within parameter java.lang.Iterable subtype has property that is annotated with @Version
* The ignoreVersion element within @BatchDelete annotation is false

If optimistic concurrency control is enable, version number is included with identifier in delete condition.
``BatchOptimisticLockException`` representing optimistic concurrency control failure is thrown, if at that time deleted count is 0.

ignoreVersion
~~~~~~~~~~~~~

If ``ignoreVersion`` property within ``@BatchDelete`` annotation is ``true``, version number is not include in delete condition.
``BatchOptimisticLockException`` is not thrown, even if delete count is 0.

.. code-block:: java

  @BatchDelete(ignoreVersion = true)
  int[] delete(List<Employee> employees);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

In case of ``suppressOptimisticLockException`` property within ``@BatchDelete`` is ``true``,
version number is include in delete condition but ``BatchOptimisticLockException`` is not thrown even if delete count is 0.

.. code-block:: java

  @BatchDelete(suppressOptimisticLockException = true)
  int[] delete(List<Employee> employees);

Batch delete by SQL file
===========================

To execute batch deleting by SQL file,
you set ``true`` to ``sqlFile`` property within ``@BatchDelete`` annotation and prepare SQL file that correspond method.

.. code-block:: java

  @BatchDelete(sqlFile = true)
  int[] delete(List<Employee> employees);

Parameter type must be ``java.lang.Iterable`` subtype that has arbitrary type as an element.
Specifiable parameter is only one.
Parameter must not be ``null``.
Return value array element count become equal ``Iterable`` element count.
Delete count is returned to array each element.

For example, you describe SQL like below to correspond above method.

.. code-block:: sql

  delete from employee where name = /* employees.name */'hoge'

Parameter name indicate ``java.lang.Iterable`` subtype element in SQL file.

Version number and optimistic concurrency control in SQL file
--------------------------------------------------------------

Optimistic concurrency control is executed if you satisfied below conditions.

* The parameter ``java.lang.Iterable`` subtype has :doc:`../entity` element, the  :doc:`../entity` element is annotated with @Version
* The ignoreVersion element within @BatchDelete annotation is false

However, describing to SQL file for Optimistic concurrency control SQL is application developer's responsibility.
For example like below SQL, you must specify version number in WHERE clauses.

.. code-block:: sql

  delete from EMPLOYEE where ID = /* employees.id */1 and VERSION = /* employees.version */1

``BatchOptimisticLockException`` representing optimistic concurrency control failure is thrown, if deleted count is 0 or multiple in this SQL.

ignoreVersion
~~~~~~~~~~~~~

If ``ignoreVersion`` property within ``@BatchDelete`` annotation is true,
``BatchOptimisticLockException`` is not thrown even if deleted count is 0 or multiple.

.. code-block:: java

  @BatchDelete(sqlFile = true, ignoreVersion = true)
  int[] delete(List<Employee> employees);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

If ``suppressOptimisticLockException`` property within ``@BatchDelete`` is ``true``,
``BatchOptimisticLockException`` is not thrown even if deleted count is 0 or multiple.

.. code-block:: java

  @BatchDelete(sqlFile = true, suppressOptimisticLockException = true)
  int[] delete(List<Employee> employees);

Query timeout
==================

You can specify seconds of query timeout to ``queryTimeout`` property within ``@BatchDelete`` annotation.

.. code-block:: java

  @BatchDelete(queryTimeout = 10)
  int[] delete(List<Employee> employees);

This specifying is applied regardless of with or without using sql file.
Query timeout that is specified in config class is used if ``queryTimeout`` property is not set value.

Batch size
============

You can specify batch size to ``batchSize`` property within ``@BatchDelete`` annotation.

.. code-block:: java

  @BatchDelete(batchSize = 10)
  int[] delete(List<Employee> employees);

This specify is applied regardless of using or not using SQL file.
It you do not specify the value to ``batchSize`` property, batch size that is specified at :doc:`../config` class is applied.

SQL log output format
=======================

You can specify SQL log output format to ``sqlLog`` property within ``@BatchDelete`` annotation.

.. code-block:: java

  @BatchDelete(sqlLog = SqlLogType.RAW)
  int[] delete(List<Employee> employees);

``SqlLogType.RAW`` represent outputting log that is sql with a binding parameter.
