==============
Kotlin support
==============

.. contents::
   :depth: 3

Doma supports `Kotlin <https://kotlinlang.org/>`_ 1.4.0 or later.

Best practices
==============

Here are some recommended methods, such as defining classes and building them with Kotlin.

Entity classes
--------------

* Define as a plain class
* Specify a ``Metamodel`` annotation to the ``metamodel`` element of ``@Entity``

.. code-block:: java

    @Entity(metamodel = Metamodel())
    class Person : AbstractPerson() {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = -1

        var name: Name? = null

        var age: Int? = -1

        var address: Address? = null

        @Column(name = "DEPARTMENT_ID")
        var departmentId: Int = -1

        @Version
        var version: Int = -1
    }

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

  @Dao
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

.. _kotlin-specific-criteria-api:

Kotlin specific Criteria API
----------------------------

.. note::

    Prefer the Kotlin specific Criteria API to DAO interfaces.

Doma provides Kotlin specific Criteria API, ``KEntityql`` and ``KNativeQl`` DSLs.
They are very similar with the ``Entityql`` and ``NativeQl`` DSLs, which are described in :doc:`criteria-api`.
The biggest feature of the ``KEntityql`` and ``KNativeQl`` DSLs is simplicity.

For example, when you use ``KEntityql``, you have to accept a lambda parameter in a WHERE expression as follows:

.. code-block:: kotlin

    val entityql = Entityql(config)
    val e = Employee_()

    val list = entityql
        .from(e)
        .where { c ->
            c.eq(e.departmentId, 2)
            c.isNotNull(e.managerId)
            c.or {
                c.gt(e.salary, Salary("1000"))
                c.lt(e.salary, Salary("2000"))
            }
        }
        .fetch()

The lambda parameter ``c`` is a bit annoying.
On the other hand, when you use ``KEntityql``, the parameter is gone.

.. code-block:: kotlin

    val entityql = KEntityql(config)
    val e = Employee_()

    val list = entityql
        .from(e)
        .where {
            eq(e.departmentId, 2)
            isNotNull(e.managerId)
            or {
                gt(e.salary, Salary("1000"))
                lt(e.salary, Salary("2000"))
            }
        }
        .fetch()

You can see a lot of sample code `here <https://github.com/domaframework/doma-it/tree/master/kotlin/src/test/kotlin/org/seasar/doma/it/criteria>`_.

The ``KEntityql`` and ``KNativeQl`` DSLs are included in doma-kotlin.jar.
Note that you should depend on doma-kotlin instead of doma-core in your build script.
You can write build.gradle.kts as follows:

.. code-block:: kotlin

    dependencies {
        implementation("org.seasar.doma:doma-kotlin:2.44.1")
    }

Code Generation
---------------

Use `Doma CodeGen Plugin <https://github.com/domaframework/doma-codegen-plugin>`_.
This plugin support Kotlin code generation.

Using kapt in Gradle
--------------------

Annotation processors are supported in Kotlin with the
`kapt <https://kotlinlang.org/docs/reference/kapt.html>`_ compiler plugin.

Add the dependencies using the `kapt` and `implementation` configuration in your dependencies block.
For example, you can write build.gradle.kts as follows:

.. code-block:: kotlin

    dependencies {
        kapt("org.seasar.doma:doma-processor:2.44.1")
        implementation("org.seasar.doma:doma-kotlin:2.44.1")
    }

To simplify your build script, we recommend you use
the `Doma Compile Plugin <https://github.com/domaframework/doma-compile-plugin>`_:

Sample project
==============

* `kotlin-sample <https://github.com/domaframework/kotlin-sample>`_
