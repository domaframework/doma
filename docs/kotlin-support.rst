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

* Specify a SQL template to ``@org.seasar.doma.experimental.Sql``
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
    Result<Person> insert(Person person);
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
      kapt "org.seasar.doma:doma:2.21.1-SNAPSHOT"
      implementation "org.seasar.doma:doma:2.21.1-SNAPSHOT"
  }

If you use resource files such as SQL files, make the kapt find them:

.. code-block:: groovy

    kapt {
        arguments {
            arg("doma.resources.dir", compileKotlin.destinationDir)
        }
    }

    task copyDomaResources(type: Sync)  {
        from sourceSets.main.resources.srcDirs
        into compileKotlin.destinationDir
        include 'doma.compile.config'
        include 'META-INF/**/*.sql'
        include 'META-INF/**/*.script'
    }

    compileKotlin {
        dependsOn copyDomaResources
    }


.. note::

    Remember that you always have options as follows:

    - Write all codes with Kotlin
    - Write all codes with Java
    - Write codes annotated with Doma's annotations in Java and others in Kotlin

    The third option is worth considering, because it can avoid some troubles with the kapt.

Sample project
==============

* `kotlin-sample <https://github.com/domaframework/kotlin-sample>`_
