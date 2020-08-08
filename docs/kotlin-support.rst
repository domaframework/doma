==============
Kotlin support
==============

.. contents::
   :depth: 3

Doma supports `Kotlin <https://kotlinlang.org/>`_ 1.3.11 or above **experimentally**.

Best practices
==============

We show you recommended ways to define classes and build them with Kotlin.

Entity classes
--------------

* Define as a data class
* Specify ``true`` to the ``immutable`` element of ``@Entity``
* Define only one constructor
* Define properties only in the constructor
* Use `val` for the property definitions

.. code-block:: java

  @Entity(immutable = true)
  data class Person(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        val name: Name,
        val address: Address)

Domain classes
--------------

* Define as a data class
* Define only one constructor
* Define only one property whose name is ``value`` in the constructor
* Use `val` for the property definition

.. code-block:: java

  @Domain(valueType = String::class)
  data class Name(val value: String)

Embeddable classes
------------------

* Define as a data class
* Define only one constructor
* Define properties only in the constructor
* Use `val` for the property definitions

.. code-block:: java

  @Embeddable
  data class Address(val city: String, val street: String)

Dao interfaces
--------------

* Specify a SQL template to ``@org.seasar.doma.Sql``
* Use ``org.seasar.doma.jdbc.Result`` as the return type of ``@Delete``, ``@Insert`` and ``@Update``
* Use ``org.seasar.doma.jdbc.BatchResult`` as the return type of
  ``@BatchDelete``, ``@BatchInsert`` and ``@BatchUpdate``

.. code-block:: java

  @Dao(config = AppConfig::class)
  interface PersonDao {
    @Sql("""
    select * from person where id = /*id*/0
    """)
    @Select
    fun selectById(id: Int): Person

    @Insert
    fun insert(Person person): Result<Person>
  }

* Use `Destructuring Declarations <https://kotlinlang.org/docs/reference/multi-declarations.html>`_
  for ``org.seasar.doma.jdbc.Result`` and ``org.seasar.doma.jdbc.BatchResult``

.. code-block:: java

  val dao: PersonDao = ...
  val person = Person(name = Name("Jhon"), address = Address(city = "Tokyo", street = "Yaesu"))
  val (newPerson, count) = dao.insert(person)

Using kapt in Gradle
--------------------

Annotation processors are supported in Kotlin with the
`kapt <https://kotlinlang.org/docs/reference/kapt.html>`_ compiler plugin.

Add the dependencies using the `kapt` and `implementation` configuration in your dependencies block:

.. code-block:: groovy

  dependencies {
      implementation "org.seasar.doma:doma-core:2.40.0"
      kapt "org.seasar.doma:doma-processor:2.40.0"
  }

To simplify your build.script, we recommend you use
the `Doma Compile Plugin <https://github.com/domaframework/doma-compile-plugin>`_:

.. code-block:: groovy

  plugins {
    id 'org.seasar.doma.compile' version '1.0.0'
  }

For more details, see this `build.gradle <https://github.com/domaframework/kotlin-sample/blob/master/build.gradle>`_.

.. note::

    Remember that you always have options as follows:

    - Write all code in Kotlin
    - Write all code in Java
    - Write code annotated with Doma's annotations in Java and others in Kotlin

    The third option is worth considering, because it can avoid some troubles with the kapt.

Sample project
==============

* `kotlin-sample <https://github.com/domaframework/kotlin-sample>`_
