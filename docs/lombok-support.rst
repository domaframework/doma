==============
Lombok support
==============

.. contents::
   :depth: 4

Doma supports `Lombok <https://projectlombok.org/>`_ version 1.16.12 and later.

.. note::

  If you intend to use Eclipse, use version 4.5 or above.

Overview
========

Both Lombok and Doma use annotation processors.
Java doesn't guarantee any specific order for annotation processors to run.
This means Doma's processors might execute before Lombok has finished modifying classes.

To address this issue, Doma's annotation processors recognize several Lombok annotations
and adjust their behavior accordingly.
For example, when Doma's annotation processors encounter a class annotated with ``@lombok.Value``,
they assume that the class has a constructor with parameters matching all its instance fields.

Best practices
==============

Here are the recommended patterns for using Lombok annotations with Doma classes:

Entity class definition
-----------------------

Immutable Entity Classes
~~~~~~~~~~~~~~~~~~~~~~~~

* Specify ``true`` to the ``immutable`` element of ``@Entity``
* Specify either ``@lombok.Value`` or ``@lombok.AllArgsConstructor``
* Specify ``@lombok.Getter`` to generate getters, in case of ``@lombok.AllArgsConstructor``

.. code-block:: java

  @Entity(immutable = true)
  @Value
  public class Employee {
    @Id
    Integer id;
    String name;
    Age age;
  }

.. code-block:: java

  @Entity(immutable = true)
  @AllArgsConstructor
  @Getter
  public class Worker {
    @Id
    private final Integer id;
    private final String name;
    private final Age age;
  }

mutable entity classes
~~~~~~~~~~~~~~~~~~~~~~

* Define a default constructor
* Specify ``@lombok.Data`` or ``@lombok.Getter``/``@lombok.Setter`` to generate getters/setters

.. code-block:: java

  @Entity
  @Data
  public class Person {
    @Id
    private Integer id;
    private String name;
    private Age age;
  }

.. code-block:: java

  @Entity
  @Getter
  @Setter
  public class Businessman {
    @Id
    private Integer id;
    private String name;
    private Age age;
  }

Domain class definition
-----------------------

* Specify ``@lombok.Value``
* Define only one instance field whose name is ``value``

.. code-block:: java

  @Domain(valueType = Integer.class)
  @Value
  public class Age {
    Integer value;
  }

Embeddable class definition
---------------------------

* Specify either ``@lombok.Value`` or ``@lombok.AllArgsConstructor``
* Specify ``@lombok.Getter`` to generate getters, in case of ``@lombok.AllArgsConstructor``

.. code-block:: java

  @Embeddable
  @Value
  public class Address {
    String street;
    String city;
  }

.. code-block:: java

  @Embeddable
  @AllArgsConstructor
  @Getter
  public class Location {
    private final String street;
    private final String city;
  }
