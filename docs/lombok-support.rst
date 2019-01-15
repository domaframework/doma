==============
Lombok support
==============

.. contents::
   :depth: 3

Doma supports `Lombok <https://projectlombok.org/>`_ 1.16.12 or above.

.. note::

  If you intend to use Eclipse, use version 4.5 or above.

Overview
========

Both Lombok and Doma provide their own annotation processors.
Because the execution order of annotation processors is not determined in Java,
the Doma's annotation processors are not always aware of the class modifications made by
the Lombok annotation processors.

To resolve the issue, the Doma's annotation processors recognise several Lombok's annotations
and change their own behavior.
For example, if the Doma's annotation processors find a class annotated with ``@lombok.Value``,
they supposes that the class has a constructor whose arguments covers all instance fields.

Best practices
==============

We show you recommended ways to define classes with Lombok annotations.

Entity class definition
-----------------------

immutable entity classes
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
