==================
Batch update
==================

.. contents::
   :depth: 3

Annotate with ``@BatchUpdate`` to Dao method for execute batch update.

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @BatchUpdate
      int[] update(List<Employee> employees);

      @BatchUpdate
      BatchResult<ImmutableEmployee> update(List<ImmutableEmployee> employees);
  }

By default UPDATE statement is auto generated.
You can mapping arbitrary SQL file by specifying ``true`` to ``sqlFile`` property within the ``@BatchUpdate`` annotation.

The ``preUpdate`` method of entity listener is called each entity when before executing update if the entity listener is specified at :doc:`../entity` parameter.
Also the ``postUpdate`` method of entity listener method is called each entity when after executing update.

Return value
=============

Return value must be ``org.seasar.doma.jdbc.Result`` that has entity class as an element if parameter ``Iterable`` subtype element is immutable entity class.

Return value must be ``int[]`` that is represented each updating process's updated count if the above conditions are not satisfied.

.. _auto-batch-update:

Batch update by auto generated SQL
===================================

Parameter type must be ``java.lang.Iterable`` subtype that has :doc:`../entity` as an element.
Specifiable parameter is only one.
Parameter must not be ``null``.
Return value array element count become equal ``Iterable`` element count.
Update count is returned to array each element.

Version number and optimistic concurrency control in auto generated SQL
-----------------------------------------------------------------------

Optimistic concurrency control is executed if you satisfied below conditions.

* :doc:`../entity` within parameter java.lang.Iterable subtype has property that is annotated with @Version
* The ignoreVersion element within @BatchUpdate annotation is false

If optimistic concurrency control is enable, version number is included with identifier in update condition and is updated increment by 1.
``BatchOptimisticLockException`` representing optimistic concurrency control failure is thrown, if at that time updated count is 0.
Also, ``BatchOptimisticLockException`` is not thrown and version property within entity is increment by 1 if updated count is 1.

ignoreVersion
~~~~~~~~~~~~~

If ``ignoreVersion`` property within ``@BatchUpdate`` annotation is true,
version number is not include in update condition and be included in SET clauses within UPDATE statement.
Version number is updated by setting value at application.
``BatchOptimisticLockException`` is not thrown in this case, even if update count is 0.

.. code-block:: java

  @BatchUpdate(ignoreVersion = true)
  int[] update(List<Employee> employees);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

In case of ``suppressOptimisticLockException`` property within ``@BatchUpdate`` is ``true``,
if property that annotated with ``@Version`` is exists then version number is include in update condition and be increment by 1 
but ``BatchOptimisticLockException`` is not thrown even if update count is 0.
However, version property value within entity is increment by 1.

.. code-block:: java

  @BatchUpdate(suppressOptimisticLockException = true)
  int[] update(List<Employee> employees);

Update target property
----------------------

updatable
~~~~~~~~~

The ``updatable`` property within ``@Column`` annotation that is specified ``false`` is excluded from updating target if :doc:`../entity` has property that is annotated with ``@Column``.

exclude
~~~~~~~

Property that is specified with ``exclude`` property within the ``@BatchUpdate`` annotation is excluded from updating target.
Even if ``updatable`` property within ``@Column`` annotation is  specified ``true`` the property is excluded from updating target if the property is specified by this element.

.. code-block:: java

  @BatchUpdate(exclude = {"name", "salary"})
  int[] update(List<Employee> employees);

include
~~~~~~~

Only property that is specified with ``include`` property within ``@BatchUpdate`` annotation is included to updating target.
If same property are specified with both of ``include`` property and ``exclude`` property within ``@BatchUpdate`` the property is excluded from updating target.
Even if property is specified with this element the property is excluded from updating target if ``updatable`` property within ``@Column`` annotation is ``false``.

.. code-block:: java

  @BatchUpdate(include = {"name", "salary"})
  int[] update(List<Employee> employees);

Batch update by SQL file
=========================

To execute batch updating by SQL file,
you set ``true`` to ``sqlFile`` property within ``@BatchUpdate`` annotation and prepare SQL file that correspond method.

.. note::

  In batch updating by SQL file, rule is different according to using or not using :ref:`populate`.

Case of using comment that generating update column list 
---------------------------------------------------------

.. code-block:: java

  @BatchUpdate(sqlFile = true)
  int[] update(List<Employee> employees);

  @BatchUpdate
  BatchResult<ImmutableEmployee> update(List<ImmutableEmployee> employees);

Parameter type must be ``java.lang.Iterable`` subtype that has :doc:`../entity` as an element.
Specifiable parameter is only one.
Parameter must not be ``null``.
Return value array element count become equal ``Iterable`` element count.
Update count is returned to array each element.

For example, you describe SQL like below to correspond above method.

.. code-block:: sql

  update employee set /*%populate*/ id = id where name = /* employees.name */'hoge'

Parameter name indicate ``Iterable`` subtype element in SQL file.

The rule that is about update target property  equals :ref:`auto-batch-update`.

Case of not using comment that generating update column list
------------------------------------------------------------

.. code-block:: java

  @BatchUpdate(sqlFile = true)
  int[] update(List<Employee> employees);

  @BatchUpdate
  BatchResult<ImmutableEmployee> update(List<ImmutableEmployee> employees);

Parameter type must be ``java.lang.Iterable`` subtype that has arbitrary type as an element.
Specifiable parameter is only one.
Parameter must not be ``null``.
Return value array element count become equal ``Iterable`` element count.
Update count is returned to array each element.

For example, you describe SQL like below to correspond above method.

.. code-block:: sql

  update employee set name = /* employees.name */'hoge', salary = /* employees.salary */100
  where id = /* employees.id */0

Parameter name indicate ``Iterable`` subtype element in SQL file.

Version number auto updating is not executed in batch update by SQL file.
Also, ``exclude`` property and ``include`` property within ``@BatchUpdate`` annotation are not referenced.

Version number and optimistic concurrency control in SQL file
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Optimistic concurrency control is executed if you satisfied below conditions.

* java.lang.Iterable subtype element in parameter is :doc:`../entity`
  and has property that is annotated @Version existing at :doc:`../entity`.
* ignoreVersion property within @BatchUpdate annotation is false.

However, describing to SQL file for Optimistic concurrency control SQL is application developer's responsibility.
For example like below SQL, you must specify version number in WHERE clauses and increment version number by 1 in SET clauses.

.. code-block:: sql

  update EMPLOYEE set DELETE_FLAG = 1, VERSION = /* employees.version */1 + 1
  where ID = /* employees.id */1 and VERSION = /* employees.version */1

``BatchOptimisticLockException`` representing optimistic concurrency control failure is thrown, if this SQL updated count is 0.
``BatchOptimisticLockException`` is not thrown and version property within entity is increment by 1 if updated count is not 0.

If optimistic concurrency control is enable, version number is included with identifier in update condition and is updated increment by 1.
``BatchOptimisticLockException`` representing optimistic concurrency control failure is thrown, if at that time updated count is 0.
On the other hand, if update count is 1, ``BatchOptimisticLockException`` is not thrown and entity version property is increment by 1.

ignoreVersion
^^^^^^^^^^^^^

If ``ignoreVersion`` property within ``@BatchUpdate`` annotation is true,
``BatchOptimisticLockException`` is not thrown, even if update count is 0 or multiple.
Also, entity version property is not modified.

.. code-block:: java

  @BatchUpdate(sqlFile = true, ignoreVersion = true)
  int[] update(List<Employee> employees);

suppressOptimisticLockException
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

In case of ``suppressOptimisticLockException`` property within ``@BatchUpdate`` is ``true``,
``BatchOptimisticLockException`` is not thrown even if update count is 0.
However, entity version property value is incremented by 1.

.. code-block:: java

  @BatchUpdate(sqlFile = true, suppressOptimisticLockException = true)
  int[] update(List<Employee> employees);

Unique constraint violation
============================

``UniqueConstraintException`` is thrown regardless of with or without using sql file if unique constraint violation is occurred.

Query timeout
==================

You can specify seconds of query timeout to ``queryTimeout`` property within ``@BatchUpdate`` annotation.

.. code-block:: java

  @BatchUpdate(queryTimeout = 10)
  int[] update(List<Employee> employees);

This specifying is applied regardless of with or without using sql file.
Query timeout that is specified in config class is used if ``queryTimeout`` property is not set value.

Batch size
============

You can specify batch size to ``batchSize`` property within ``@BatchUpdate`` annotation.

.. code-block:: java

  @BatchUpdate(batchSize = 10)
  int[] update(List<Employee> employees);

This specify is applied Regardless of using or not using SQL file.
It you do not specify the value to ``batchSize`` property, batch size that is specified at :doc:`../config` class is applied.

SQL log output format
======================

You can specify SQL log output format to ``sqlLog`` property within ``@BatchUpdate`` annotation.

.. code-block:: java

  @BatchUpdate(sqlLog = SqlLogType.RAW)
  int[] update(List<Employee> employees);

``SqlLogType.RAW`` represent outputting log that is sql with a binding parameter.
