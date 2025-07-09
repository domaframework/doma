==================
Batch update
==================

.. contents::
   :depth: 4

Annotate a Dao method with ``@BatchUpdate`` to execute batch update operations.

.. code-block:: java

  @Dao
  public interface EmployeeDao {
      @BatchUpdate
      int[] update(List<Employee> employees);

      @BatchUpdate
      BatchResult<ImmutableEmployee> update(List<ImmutableEmployee> employees);
  }

By default, the UPDATE statement is automatically generated.
You can map to an arbitrary SQL file by setting the ``sqlFile`` property to ``true`` in the ``@BatchUpdate`` annotation.

If an entity listener is specified for the entity class, its ``preUpdate`` method is called for each entity before executing the update operation.
Similarly, the ``postUpdate`` method is called for each entity after the update operation completes.

Return value
=============

If the elements of the parameter (which must be an ``Iterable`` subtype) are immutable entity classes, the return value must be ``org.seasar.doma.jdbc.BatchResult`` with the entity class as its element type.

If the above condition is not met, the return value must be ``int[]``, where each element represents the number of rows affected by each update operation.

.. _auto-batch-update:

Batch update by auto generated SQL
===================================

The parameter type must be a subtype of ``java.lang.Iterable`` with entity classes as its elements.
Only one parameter can be specified.
The parameter must not be ``null``.
The number of elements in the return value array will equal the number of elements in the ``Iterable``.
Each element in the array represents the number of rows affected by the corresponding update operation.

Version number and optimistic concurrency control in auto generated SQL
-----------------------------------------------------------------------

Optimistic concurrency control is executed if you satisfied below conditions.

* :doc:`../entity` within parameter java.lang.Iterable subtype has property that is annotated with @Version
* The ignoreVersion element within @BatchUpdate annotation is false

When optimistic concurrency control is enabled, the version number is included with the identifier in the update condition and is incremented by 1.
If the update count is 0, a ``BatchOptimisticLockException`` is thrown, indicating an optimistic concurrency control failure.
If the update count is 1, the version property in the entity is incremented by 1 and no exception is thrown.

ignoreVersion
~~~~~~~~~~~~~

If the ``ignoreVersion`` property of the ``@BatchUpdate`` annotation is set to ``true``,
the version number is not included in the update condition but is included in the SET clauses of the UPDATE statement.
The version number is updated with the value set in the application.
In this case, ``BatchOptimisticLockException`` is not thrown even if the update count is 0.

.. code-block:: java

  @BatchUpdate(ignoreVersion = true)
  int[] update(List<Employee> employees);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

When the ``suppressOptimisticLockException`` property of the ``@BatchUpdate`` annotation is set to ``true``,
if a property annotated with ``@Version`` exists, then the version number is included in the update condition and incremented by 1,
but ``BatchOptimisticLockException`` is not thrown even if the update count is 0.
However, the version property value in the entity is incremented by 1.

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

Properties specified in the ``exclude`` property of the ``@BatchUpdate`` annotation are excluded from the update operation.
Even if the ``updatable`` property of the ``@Column`` annotation is set to ``true``, a property will be excluded from the update if it is listed in the ``exclude`` property.

.. code-block:: java

  @BatchUpdate(exclude = {"name", "salary"})
  int[] update(List<Employee> employees);

include
~~~~~~~

Only properties specified in the ``include`` property of the ``@BatchUpdate`` annotation are included in the update operation.
If a property is specified in both the ``include`` and ``exclude`` properties of the ``@BatchUpdate`` annotation, it is excluded from the update operation.
Even if a property is specified in the ``include`` property, it is excluded from the update operation if the ``updatable`` property of its ``@Column`` annotation is set to ``false``.

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

Optimistic concurrency control is performed when the following conditions are met:

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

If the ``ignoreVersion`` property of the ``@BatchUpdate`` annotation is set to true,
``BatchOptimisticLockException`` is not thrown, even if the update count is 0 or multiple.
Additionally, the entity version property is not modified.

.. code-block:: java

  @BatchUpdate(sqlFile = true, ignoreVersion = true)
  int[] update(List<Employee> employees);

suppressOptimisticLockException
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

When the ``suppressOptimisticLockException`` property of the ``@BatchUpdate`` annotation is set to ``true``,
``BatchOptimisticLockException`` is not thrown even if the update count is 0.
However, the entity version property value is incremented by 1.

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
