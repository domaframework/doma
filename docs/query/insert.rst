==================
Insert
==================

.. contents::
   :depth: 3

Annotate with ``@Insert`` to Dao method for execute insert.

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @Insert
      int insert(Employee employee);

      @Insert
      Result<ImmutableEmployee> insert(ImmutableEmployee employee);
  }

By default INSERT statement is auto generated.
You can mapping arbitrary SQL file by setting true to ``sqlFile`` element of ``@Insert``.

The ``preInsert`` method of entity listener is called when before executing insert if the entity listener is specified at :doc:`../entity` parameter.
Also the ``postInsert`` entity listener  method is called when after executing insert.

Return value
============

Return value must be ``org.seasar.doma.jdbc.Result`` that has entity class as an element if parameter is immutable entity class.

Return value must be ``int`` that is represented updated count if the above conditions are not satisfied.

Insert by auto generated SQL
============================

Parameter type must be entity class.
Specifiable parameter is only one.
Parameter must not be null.

.. code-block:: java

  @Insert
  int insert(Employee employee);

  @Insert
  Result<ImmutableEmployee> insert(ImmutableEmployee employee);

Identifier
----------


Identifier is auto generated and setting if :doc:`../entity` identifier is annotated with ``@GeneratedValue``.

Reference :ref:`identity-auto-generation` about cautionary point.

Version numbers
----------------

If :doc:`../entity` has property that is annotated  with ``@Version`, if the property is explicitly set over 0 then use the value.

If the value is not set or is less than 0 then the value is set 1 automatically.

Control insertion target property
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

insertable
~~~~~~~~~~

Property that is set false to ``insertable`` element of ``@Column`` is excluded from insertion target if entity class has property that is annotated with ``@Column``.

exclude
~~~~~~~

Property that is specified with ``exclude`` element of ``@Insert`` is excluded from insertion target.
Even if ``insertable`` element of ``@Column`` is true the property is excluded from insertion target if the property is specified by this element.

.. code-block:: java

  @Insert(exclude = {"name", "salary"})
  int insert(Employee employee);

include
~~~~~~~

Only property that is specified with ``include`` element of ``@Insert`` is included to insertion target.
If same property are specified with both of ``include`` element and ``exclude`` element of ``@Insert`` the property is excluded from insertion target.

Even if property is specified with this element the property is excluded from insertion target if ``insertable`` element of ``@Column`` is false.

.. code-block:: java

  @Insert(include = {"name", "salary"})
  int insert(Employee employee);

excludeNull
~~~~~~~~~~~

Property that value is ``null`` is excluded from insertion target if ``excludeNull`` element of ``@Insert`` is true.
If this element is true, even if ``insertable`` element of ``@Column`` is true or property is specified with ``include`` element of ``@Insert``
the property is excluded from insertion target if value is ``null``.

.. code-block:: java

  @Insert(excludeNull = true)
  int insert(Employee employee);

Insert by SQL file
=====================

To execute insertion by SQL file,
you set ``true`` to ``sqlFile`` element of ``@Insert`` and prepare SQL file that correspond method.

You can use arbitrary type as parameter.
Specifiable parameters count is no limit.
You can set ``null`` to parameter if parameter type is basic type or domain class.
Parameter must not be ``null`` if the type is other than that.

.. code-block:: java

  @Insert(sqlFile = true)
  int insert(Employee employee);

  @Insert(sqlFile = true)
  Result<ImmutableEmployee> insert(ImmutableEmployee employee);

For example, you describe SQL file like below to correspond above method.

.. code-block:: sql

  insert into employee (id, name, salary, version) 
  values (/* employee.id */0, 
          /* employee.name */'hoge', 
          /* employee.salary */100, 
          /* employee.version */0)

Identifier auto setting and version value auto setting are not done by insertion by SQL file.
Also, ``exclude`` element and ``include`` element and ``excludeNull`` element of ``@Insert`` are not referenced.

Unique constraint violation
===========================

``UniqueConstraintException`` is thrown regardless of with or without using sql file if unique constraint violation is occurred.

Query timeout
==================

You can specify seconds of query timeout to ``queryTimeout`` element of ``@Insert``.

.. code-block:: java

  @Insert(queryTimeout = 10)
  int insert(Employee employee);

This specifying is applied regardless of with or without using sql file.
Query timeout that is specified in :doc:`../config` is used if ``queryTimeout`` element is not set value.

SQL log output format
======================

You can specify SQL log output format to ``sqlLog`` element of ``@Insert``.

.. code-block:: java

  @Insert(sqlLog = SqlLogType.RAW)
  int insert(Employee employee);

``SqlLogType.RAW`` represent outputting log that is sql with a binding parameter.
