==================
Update
==================

.. contents::
   :depth: 4

Annotate DAO methods with ``@Update`` to execute update operations.

.. code-block:: java

  @Dao
  public interface EmployeeDao {
      @Update
      int update(Employee employee);

      @Update
      Result<ImmutableEmployee> update(ImmutableEmployee employee);
  }

By default, the UPDATE statement is automatically generated.
You can map to an arbitrary SQL file by setting the ``sqlFile`` property to ``true`` in the ``@Update`` annotation.

If an entity listener is specified for the entity class parameter, the ``preUpdate`` method of the entity listener is called before executing the update.
Similarly, the ``postUpdate`` method of the entity listener is called after executing the update.

Return value
============

When using the returning property
---------------------------------

See :ref:`update-returning`.

When not using the returning property
-------------------------------------

If the parameter is an immutable entity class, the return value must be an ``org.seasar.doma.jdbc.Result`` with an entity class as its element.

If the above condition is not satisfied, the return value must be an ``int`` representing the number of updated rows.

.. _auto-update:

Update by auto generated SQL
============================

The parameter type must be an entity class.
Only one parameter can be specified.
The parameter must not be null.

.. code-block:: java

  @Update
  int update(Employee employee);

  @Update
  Result<ImmutableEmployee> update(ImmutableEmployee employee);

Version number and optimistic concurrency control in auto generated SQL
------------------------------------------------------------------------

Optimistic concurrency control is executed if the following conditions are met:

* The entity class parameter has a property that is annotated with @Version
* The ignoreVersion element in the @Update annotation is false

When optimistic concurrency control is enabled, the version number is included with the identifier in the update condition and is incremented by 1.
If the update count is 0, an ``OptimisticLockException`` is thrown to indicate optimistic concurrency control failure.
If the update count is not 0, no ``OptimisticLockException`` is thrown and the version property in the entity is incremented by 1.

ignoreVersion
~~~~~~~~~~~~~

If the ``ignoreVersion`` property in the ``@Update`` annotation is set to true,
the version number is not included in the update condition but is included in the SET clauses of the UPDATE statement.
The version number is updated by setting a value at the application level.
In this case, an ``OptimisticLockException`` is not thrown even if the update count is 0.

.. code-block:: java

  @Update(ignoreVersion = true)
  int update(Employee employee);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

When the ``suppressOptimisticLockException`` property in the ``@Update`` annotation is set to true,
if a property annotated with ``@Version`` exists, the version number is included in the update condition and incremented by 1.
An ``OptimisticLockException`` is not thrown even if the update count is 0.
However, the version property value in the entity is still incremented by 1.

.. code-block:: java

  @Update(suppressOptimisticLockException = true)
  int update(Employee employee);

Control updating target property
--------------------------------

updatable
~~~~~~~~~

Properties annotated with ``@Column`` that have their ``updatable`` property set to ``false`` are excluded from the update targets.

exclude
~~~~~~~

Properties specified in the ``exclude`` property of the ``@Update`` annotation are excluded from the update targets.
Even if the ``updatable`` property in the ``@Column`` annotation is set to ``true``, properties listed in the ``exclude`` property will not be updated.

.. code-block:: java

  @Update(exclude = {"name", "salary"})
  int update(Employee employee);

include
~~~~~~~

Only properties specified in the ``include`` property of the ``@Update`` annotation will be updated.
If the same property appears in both the ``include`` and ``exclude`` properties of ``@Update``, it will not be updated.
Even if a property is listed in the ``include`` property, it will not be updated if its ``updatable`` property in the ``@Column`` annotation is set to ``false``.

.. code-block:: java

  @Update(include = {"name", "salary"})
  int update(Employee employee);

excludeNull
~~~~~~~~~~~

When the ``excludeNull`` property of the ``@Update`` annotation is set to ``true``, properties with a value of ``null`` will not be updated.
This takes precedence over other settings - even if a property's ``updatable`` attribute in its ``@Column`` annotation is set to ``true`` or the property is listed in the ``include`` property of the ``@Update`` annotation, it will not be updated if its value is ``null``.

.. code-block:: java

  @Update(excludeNull = true)
  int update(Employee employee);

includeUnchanged
~~~~~~~~~~~~~~~~

This property is only effective if the entity class being updated contains a property annotated with ``@OriginalStates``.

When set to ``true``, all properties in the entity will be updated.
This means all corresponding columns will be included in the SET clauses of the UPDATE statement.

When set to ``false``, only properties that have changed since the entity was loaded will be updated.
Only the columns corresponding to these modified properties will be included in the SET clauses of the UPDATE statement.

.. code-block:: java

  @Update(includeUnchanged = true)
  int update(Employee employee);

.. _update-returning:

returning
~~~~~~~~~

By specifying ``@Returning`` in the ``returning`` property,
you can generate SQL with the ``UPDATE .. RETURNING`` clause.

.. code-block:: java

  @Dao
  public interface EmployeeDao {
      @Update(returning = @Returning)
      Employee update(Employee employee);

      @Update(returning = @Returning(include = { "employeeId", "version" }))
      Employee updateReturningIdAndVersion(Employee employee);

      @Update(returning = @Returning(exclude = { "password" }))
      Employee updateReturningExceptPassword(Employee employee);

      @Update(returning = @Returning, suppressOptimisticLockException = true)
      Optional<Employee> updateOrIgnore(Employee employee);
  }

You can use the ``include`` element of ``@Returning`` to specify which entity properties
(corresponding to database columns) should be returned by the RETURNING clause.
Alternatively, you can use the ``exclude`` element to specify which properties should be excluded from the results.
If a property appears in both the ``include`` and ``exclude`` elements, it will not be returned.

The return type must be either an entity class
or an ``Optional`` containing an entity class as its element.

.. note::

  Only H2 Database, PostgreSQL, SQL Server, and SQLite Dialects support this feature.

Update by SQL file
==================

To execute an update using an SQL file,
set the ``sqlFile`` property to ``true`` in the ``@Update`` annotation and prepare an SQL file that corresponds to the method.

.. note::

  When updating via SQL file, the rules differ depending on whether you use :ref:`populate`.

Case of using the populate directive
------------------------------------

The first parameter must be an entity class.
There is no limit on the number of parameters you can specify.
You can pass ``null`` for parameters of basic types or domain classes.
For all other parameter types, ``null`` values are not allowed.

.. code-block:: java

  @Update(sqlFile = true)
  int update(Employee employee, BigDecimal salary);

  @Update(sqlFile = true)
  Result<ImmutableEmployee> update(ImmutableEmployee employee, BigDecimal salary);

For example, you would write an SQL file like the one below to correspond to the above method:

.. code-block:: sql

  update employee set /*%populate*/ id = id where salary > /* salary */0

The rules for controlling update target properties are the same as in :ref:`auto-update`.

Case of not using the populate directive
----------------------------------------

You can use any type as a parameter.
There is no limit on the number of parameters you can specify.
You can pass ``null`` for parameters of basic types or domain classes.
For all other parameter types, ``null`` values are not allowed.

.. code-block:: java

  @Update(sqlFile = true)
  int update(Employee employee);

  @Update(sqlFile = true)
  Result<ImmutableEmployee> update(ImmutableEmployee employee);

For example, you would write an SQL file like the one below to correspond to the above method:

.. code-block:: sql

  update employee set name = /* employee.name */'hoge', salary = /* employee.salary */100
  where id = /* employee.id */0

The ``exclude``, ``include``, ``excludeNull``, and ``includeUnchanged`` properties within the ``@Update`` annotation are not referenced when updating via SQL file.


Version number and optimistic concurrency control in SQL file
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Optimistic concurrency control is executed if the following conditions are met:

* An entity class is included in the parameters
* The leftmost entity class parameter has a property annotated with @Version
* The ignoreVersion element in the @Update annotation is false

However, writing SQL for optimistic concurrency control is the application developer's responsibility.
For example, in the SQL below, you must include the version number in the WHERE clause and increment it by 1 in the SET clause.

.. code-block:: sql

  update EMPLOYEE set DELETE_FLAG = 1, VERSION = /* employee.version */1 + 1
  where ID = /* employee.id */1 and VERSION = /* employee.version */1

If this SQL statement's update count is 0, an ``OptimisticLockException`` is thrown to indicate optimistic concurrency control failure.
If the update count is not 0, no ``OptimisticLockException`` is thrown and the version property in the entity is incremented by 1.

ignoreVersion
^^^^^^^^^^^^^

If the ``ignoreVersion`` property of the ``@Update`` annotation is set to true,
no ``OptimisticLockException`` will be thrown even if the update count is 0.
Additionally, the version property value in the entity remains unchanged.

.. code-block:: java

  @Update(sqlFile = true, ignoreVersion = true)
  int update(Employee employee);

suppressOptimisticLockException
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If the ``suppressOptimisticLockException`` property of the ``@Update`` annotation is set to true,
no ``OptimisticLockException`` will be thrown even if the update count is 0.
However, the version property value in the entity will still be incremented by 1.

.. code-block:: java

  @Update(sqlFile = true, suppressOptimisticLockException = true)
  int update(Employee employee);

Unique constraint violation
===========================

A ``UniqueConstraintException`` is thrown if a unique constraint violation occurs, regardless of whether an SQL file is used or not.

Query timeout
=============

You can specify the query timeout in seconds using the ``queryTimeout`` property in the ``@Update`` annotation.

.. code-block:: java

  @Update(queryTimeout = 10)
  int update(Employee employee);

This specification applies regardless of whether an SQL file is used or not.
If the ``queryTimeout`` property is not set, the query timeout specified in :doc:`../config` is used.

SQL log output format
=====================

You can specify the SQL log output format using the ``sqlLog`` property in the ``@Update`` annotation.

.. code-block:: java

  @Update(sqlLog = SqlLogType.RAW)
  int update(Employee employee);

``SqlLogType.RAW`` indicates that the log output will contain the SQL with binding parameters.
