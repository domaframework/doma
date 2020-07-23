==================
Update
==================

.. contents::
   :depth: 3

Annotate with ``@Update`` to Dao method for execute update.

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @Update
      int update(Employee employee);

      @Update
      Result<ImmutableEmployee> update(ImmutableEmployee employee);
  }

By default UPDATE statement is auto generated.
You can mapping arbitrary SQL file by specifying ``true`` to ``sqlFile`` property within the ``@Update`` annotation.

The ``preUpdate`` method of entity listener is called when before executing update if the entity listener is specified at entity class parameter.
Also the ``postUpdate`` method of entity listener  method is called when after executing update.

Return value
============

Return value must be ``org.seasar.doma.jdbc.Result`` that has entity class as an element if parameter is immutable entity class.

Return value must be ``int`` that is represented updated count if the above conditions are not satisfied.

.. _auto-update:

Update by auto generated SQL
============================

Parameter type must be entity class.
Specifiable parameter is only one.
Parameter must not be null.

.. code-block:: java

  @Update
  int update(Employee employee);

  @Update
  Result<ImmutableEmployee> update(ImmutableEmployee employee);

Version number and optimistic concurrency control in auto generated SQL
------------------------------------------------------------------------

Optimistic concurrency control is executed if you satisfied below conditions.

* Entity class within parameter has property that is annotated with @Version
* The ignoreVersion element within @Update annotation is false

If optimistic concurrency control is enable, version number is included with identifier in update condition and is updated increment by 1.
``OptimisticLockException`` representing optimistic concurrency control failure is thrown, if at that time updated count is 0.
Also, ``OptimisticLockException`` is not thrown and version property within entity is increment by 1 if updated count is not 0.

ignoreVersion
~~~~~~~~~~~~~

If ``ignoreVersion`` property within ``@Update`` annotation is true,
version number is not include in update condition and be included in SET clauses within UPDATE statement.
Version number is updated by setting value at application.
``OptimisticLockException`` is not thrown in this case, even if update count is 0.

.. code-block:: java

  @Update(ignoreVersion = true)
  int update(Employee employee);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

In case of ``suppressOptimisticLockException`` property within ``@Update`` is true,
if property that annotated with ``@Version`` is exists then version number is include in update condition and be increment by 1
but ``OptimisticLockException`` is not thrown even if update count is 0.
However, version property value within entity is increment by 1.

.. code-block:: java

  @Update(suppressOptimisticLockException = true)
  int update(Employee employee);

Control updating target property
--------------------------------

updatable
~~~~~~~~~

The ``updatable`` property within ``@Column`` annotation that is specified ``false`` is excluded from updating target if entity class has property that is annotated with ``@Column``.

exclude
~~~~~~~

Property that is specified with ``exclude`` property within the ``@Update`` annotation is excluded from updating target.
Even if ``updatable`` property within ``@Column`` annotation is  specified ``true`` the property is excluded from updating target if the property is specified by this element.

.. code-block:: java

  @Update(exclude = {"name", "salary"})
  int update(Employee employee);

include
~~~~~~~

Only property that is specified with ``include`` property within ``@Update`` annotation is included to updating target.
If same property are specified with both of ``include`` property and ``exclude`` property within ``@Update`` the property is excluded from updating target.
Even if property is specified with this element the property is excluded from updating target if ``updatable`` property within ``@Column`` annotation is ``false``.

.. code-block:: java

  @Update(include = {"name", "salary"})
  int update(Employee employee);

excludeNull
~~~~~~~~~~~

Property that value is ``null`` is excluded from updating target if ``excludeNull`` property within ``@Update`` annotation is specified ``true``.
If this element is ``true``, even if ``updatable`` property within ``@Column`` annotation is specified ``true`` or property is specified with ``include`` property within ``@Update`` annotation
the property is excluded from insertion target if value is ``null``.

.. code-block:: java

  @Update(excludeNull = true)
  int update(Employee employee);

includeUnchanged
~~~~~~~~~~~~~~~~

This element is enable only if property that annotated with ``@OriginalStates`` is exists within updating target entity class.

All property within entity is updating target if this element is true.
That is, the column corresponding to all property is included in SET clauses within UPDATE statement.

Only properties that have actually changed since the entity is updating target if this element is ``false``.
That is, only the column corresponding to modified property is included in SET clauses within UPDATE statement.

.. code-block:: java

  @Update(includeUnchanged = true)
  int update(Employee employee);

Update by SQL file
=====================

To execute updating by SQL file,
you set ``true`` to ``sqlFile`` property within ``@Update`` annotation and prepare SQL file that correspond method.

.. note::

  In updating by SQL file, rule is different with or without use ref:`populate`.

Case of using comment that generating update column list
---------------------------------------------------------

First parameter type must be entity class.
Specifiable parameters count is no limit.
You can set ``null`` to parameter if parameter type is basic type or domain class.
Parameter must not be ``null`` if the type is other than that.

.. code-block:: java

  @Update(sqlFile = true)
  int update(Employee employee, BigDecimal salary);

  @Update(sqlFile = true)
  Result<ImmutableEmployee> update(ImmutableEmployee employee, , BigDecimal salary);

For example, you describe SQL file like below to correspond above method.

.. code-block:: sql

  update employee set /*%populate*/ id = id where salary > /* salary */0

The rule about controlling updating target property is same as :ref:`auto-update`.

Case of not using comment that generating update column list
------------------------------------------------------------

You can use arbitrary type as parameter.
Specifiable parameters count is no limit.
You can set ``null`` to parameter if parameter type is basic type or domain class.
Parameter must not be ``null`` if the type is other than that.

.. code-block:: java

  @Update(sqlFile = true)
  int update(Employee employee);

  @Update(sqlFile = true)
  Result<ImmutableEmployee> update(ImmutableEmployee employee);

For example, you describe SQL file like below to correspond above method.

.. code-block:: sql

  update employee set name = /* employee.name */'hoge', salary = /* employee.salary */100
  where id = /* employee.id */0

``exclude`` property and ``include`` property, ``excludeNull`` property, ``includeUnchanged`` property they are within ``@Update`` annotation are not referenced in updating by SQL file.


Version number and optimistic concurrency control in SQL file
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Optimistic concurrency control is executed if you satisfied below conditions.

* Entity class is included in parameter.
* Entity class at first from the left within parameter has property that is annotated with @Version
* The ignoreVersion element within @Update annotation is false

However, describing to SQL file for Optimistic concurrency control SQL is application developer's responsibility.
For example like below SQL, you must specify version number in WHERE clauses and increment version number by 1 in SET clauses.

.. code-block:: sql

  update EMPLOYEE set DELETE_FLAG = 1, VERSION = /* employee.version */1 + 1
  where ID = /* employee.id */1 and VERSION = /* employee.version */1

``OptimisticLockException`` representing optimistic concurrency control failure is thrown, if this SQL updated count is 0.
``OptimisticLockException`` is not thrown and version property within entity is increment by 1 if updated count is not 0.

ignoreVersion
^^^^^^^^^^^^^

If ``ignoreVersion`` property within ``@Update`` annotation is true,
``OptimisticLockException`` is not thrown even if update count is 0.
Also, version property value within entity is not modified.

.. code-block:: java

  @Update(sqlFile = true, ignoreVersion = true)
  int update(Employee employee);

suppressOptimisticLockException
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If ``suppressOptimisticLockException`` property within ``@Update`` annotation is true,
``OptimisticLockException`` is not thrown even if update count is 0.
However, version property value within entity is incremented by 1.

.. code-block:: java

  @Update(sqlFile = true, suppressOptimisticLockException = true)
  int update(Employee employee);

Unique constraint violation
===========================

``UniqueConstraintException`` is thrown regardless of with or without using sql file if unique constraint violation is occurred.

Query timeout
==================

You can specify seconds of query timeout to ``queryTimeout`` property within ``@Update`` annotation.

.. code-block:: java

  @Update(queryTimeout = 10)
  int update(Employee employee);

This specifying is applied regardless of with or without using sql file.
Query timeout that is specified in :doc:`../config` is used if ``queryTimeout`` property is not set value.

SQL log output format
======================

You can specify SQL log output format to ``sqlLog`` property within ``@Update`` annotation.

.. code-block:: java

  @Update(sqlLog = SqlLogType.RAW)
  int update(Employee employee);

``SqlLogType.RAW`` represent outputting log that is sql with a binding parameter.
