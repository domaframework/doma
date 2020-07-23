==================
Entity classes
==================

.. contents::
   :depth: 3

Entity classes correspond to database tables or query result sets.

Entity definition
==================

The following code snippet shows how to define an entity:

.. code-block:: java

  @Entity
  public class Employee {
      ...
  }

An entity class can inherit other entity class.

The following code snippet shows how to inherit other entity class:

.. code-block:: java

  @Entity
  public class SkilledEmployee extends Employee {
      ...
  }

.. note::
  In Java 14 and later version, you can annotate `records`_ with ``@Entity``:

  .. code-block:: java

    @Entity
    public record Employee(...) {
    }

  In the case, the entity is recognize as :ref:`immutable`
  even though the immutable property of ``@Entity`` is ``false``.

.. _records: https://openjdk.java.net/jeps/359

Entity listeners
---------------------------

Entity listeners work before/after Doma issues the database modification statements - INSERT, DELETE and UPDATE.

The following code snippet shows how to define an entity listener:

.. code-block:: java

  public class EmployeeEntityListener implements EntityListener<Employee> {
      ...
  }

To use the entity listener, specify it to the ``listener`` property within the ``@Entity`` annotation:

.. code-block:: java

  @Entity(listener = EmployeeEntityListener.class)
  public class Employee {
      ...
  }

An entity subclass inherits parent`s entity listener.

Naming convention
---------------------------

Naming convention maps the names between:

* the database tables and the Java entity classes
* the database column and the Java entity fields

The following code snippet shows how to apply the naming convention to an entity:

.. code-block:: java

  @Entity(naming = NamingType.SNAKE_UPPER_CASE)
  public class EmployeeInfo {
      ...
  }

When the ``name`` property within the ``@Table`` or ``@Column`` annotation is explicitly specified,
the naming convention is ignored.

An entity subclass inherits parent's naming convention.

.. _immutable:

Immutable
----------------------------

An entity class can be immutable.

The following code snippet shows how to define an immutable entity:

.. code-block:: java

  @Entity(immutable = true)
  public class Employee {
      @Id
      final Integer id;
      final String name;
      @Version
      final Integer version;

      public Employee(Integer id, String name, Integer version) {
          this.id = id;
          this.name = name;
          this.version = version;
      }
      ...
  }

The ``immutable`` property within the ``@Entity`` annotation must be ``true``.
The persistent field must be ``final``.

An entity subclass inherits parent's immutable property.

Table
------------------

You can specify the corresponding table name with the ``@Table`` annotation:

.. code-block:: java

  @Entity
  @Table(name = "EMP")
  public class Employee {
      ...
  }

Without the ``@Table`` annotation, the table name is resolved by `Naming Convention`_.

Field definition
==================

By default, the fields are persistent and correspond to the database columns or result set columns.

The field type must be one of the following:

* :doc:`basic`
* :doc:`domain`
* :doc:`embeddable`
* java.util.Optional, whose element is either :doc:`basic` or :doc:`domain`
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble


The following code snippet shows how to define a filed:

.. code-block:: java

  @Entity
  public class Employee {
      ...
      Integer employeeId;
  }

Column
------------------

You can specify the corresponding column name with the ``@Column`` annotation:

.. code-block:: java

  @Column(name = "ENAME")
  String employeeName;


To exclude fields from INSERT or UPDATE statements, specify ``false`` to the ``insertable`` or ``updatable``
property within the ``@Column`` annotation:

.. code-block:: java

  @Column(insertable = false, updatable = false)
  String employeeName;

Without the ``@Column`` annotation, the column name is resolved by `Naming Convention`_.

.. note::

  When the filed type is :doc:`embeddable`, you cannot specify the ``@Column`` annotation to the field.

Id
--------------------

The database primary keys are represented with the ``@Id`` annotation:

.. code-block:: java

  @Id
  Integer id;

When there is a composite primary key, use the ``@Id`` annotation many times:

.. code-block:: java

  @Id
  Integer id;

  @Id
  Integer id2;

.. note::

  When the filed type is :doc:`embeddable`, you cannot specify the ``@Id`` annotation to the field.

.. _identity-auto-generation:

Id generation
~~~~~~~~~~~~~~~~~~~~~~~~~~

You can instruct Doma to generate id values automatically using the ``@GeneratedValue`` annotation.

The field type must be one of the following:

* the subclass of java.lang.Number
* :doc:`domain`, whose value type is the subclass of java.lang.Number
* java.util.Optional, whose element is either above types
* OptionalInt
* OptionalLong
* OptionalDouble
* the primitive types for number

.. note::

  The generated values are assign to the field only when the field is either ``null`` or less than ``0``.
  If you use one of the primitive types as filed type,
  initialize the field with tha value that is less than ``0``, such as ``-1``.

Id generation by IDENTITY
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

To generate values using the RDBMS IDENTITY function, specify the ``GenerationType.IDENTITY`` enum value
to ``strategy`` property within the ``@GeneratedValue``:

.. code-block:: java

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

In advance, define the database primary key as IDENTITY.

.. warning::

  All RDBMS does't support the IDENTITY function.

Id generation by SEQUENCE
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

To generate values using the RDBMS SEQUENCE, specify the ``GenerationType.SEQUENCE`` enum value
to ``strategy`` property within the ``@GeneratedValue`` annotation.
And use the ``@SequenceGenerator`` annotation:

.. code-block:: java

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequence = "EMPLOYEE_SEQ")
  Integer id;

In advance, define the SEQUENCE in the database.
The SEQUENCE definitions such as the name, the allocation size and the initial size must
correspond the properties within the ``@SequenceGenerator`` annotation.

.. warning::

  All RDBMS does't support the SEQUENCE.

Id generation by TABLE
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

To generate values using the RDBMS TABLE, specify the ``GenerationType.TABLE`` enum value
to ``strategy`` property within the ``@GeneratedValue`` annotation.
And use the ``@TableGenerator`` annotation:

.. code-block:: java

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  @TableGenerator(pkColumnValue = "EMPLOYEE_ID")
  Integer id;

In advance, define the TABLE in the database.
The TABLE`s definition must correspond to the properties within the ``@TableGenerator`` annotation.
For example, the DDL should be following:

.. code-block:: sql

  CREATE TABLE ID_GENERATOR(PK VARCHAR(20) NOT NULL PRIMARY KEY, VALUE INTEGER NOT NULL);

You can change the table name and the column names using the properties within the ``@TableGenerator`` annotation.

Version
------------------

The version fields for optimistic locking are represented with the ``@Version`` annotation.

The field type must be one of the following:

* the subclass of java.lang.Number
* :doc:`domain`, whose value type is the subclass of java.lang.Number
* java.util.Optional, whose element is either above types
* OptionalInt
* OptionalLong
* OptionalDouble
* the primitive types for number

.. code-block:: java

  @Version
  Integer version;

.. note::

  When the filed type is :doc:`embeddable`, you cannot specify the ``@Version`` annotation to the field.

Tenant Id
------------------------------

The tenant id fields are represented with the ``@TenantId`` annotation.
The column corresponding to the annotated field is included in the WHERE clause of UPDATE and DELETE stetements.

.. code-block:: java

  @TenantId
  String tenantId;

.. note::

  When the filed type is :doc:`embeddable`, you cannot specify the ``@TenantId`` annotation to the field.

Transient
----------------

If an entity has fields that you don't want to persist, you can annotate them using ``@Transient``:

.. code-block:: java

  @Transient
  List<String> nameList;

OriginalStates
--------------------------------------------

If you want to include only changed values in UPDATE statements,
you can define fields annotated with ``@OriginalStates``.
The fields can hold the original values that were fetched from the database.

Doma uses the values to know which values are changed in the application and
includes the only changed values in UPDATE statements.

The following code snippet shows how to define original states:

.. code-block:: java

  @OriginalStates
  Employee originalStates;

The field type must be the same as the entity type.

Method definition
====================

There are no limitations in the use of methods.

Example
==================

Instantiate the ``Employee`` entity class and use its instance:

.. code-block:: java

  Employee employee = new Employee();
  employee.setEmployeeId(1);
  employee.setEmployeeName("SMITH");
  employee.setSalary(new BigDecimal(1000));
