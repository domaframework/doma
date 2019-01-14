==========================
Frequently Asked Questions
==========================

.. contents::
   :depth: 3

General Questions
=================

What does Doma stand for?
-------------------------

Doma stands for the **D** ao **O** riented database **MA** pping framework.

What is annotation processing?
------------------------------

Annotation processing, that is introduced in Java 6,
allows us to validate and generate source codes at compile time.

We use annotation processing to do followings:

- Generating meta classes from the classes annotated with ``@Entity`` and ``@Domain``
- Generating implementation classes of the interfaces annotated with ``@Dao``
- Validating SQL statements

Runtime environment
===================

Which version of JRE does Doma support?
---------------------------------------

JRE 8、9、10 and 11.

What libraries are required to work Doma?
-----------------------------------------

Nothing.

Doma has no dependencies to other libraries.

Development environment
=======================

Which version of JDK does Doma support?
---------------------------------------

JDK 8、9、10 and 11.

Which IDE do you recommend?
---------------------------

We recommend Eclipse and IntelliJ IDEA.

In Eclipse, the jar file of Doma is added to the Java Build Path but annotation processing doesn't run.
-------------------------------------------------------------------------------------------------------

Enable annotation processing and add the jar file to the Factory Path too.
See also :ref:`eclipse-build`.

Where are generated source files in annotation processing?
----------------------------------------------------------

In Eclipse, they are found in the .apt_generated directory.

In Eclipse, where is the .apt_generated directory?
--------------------------------------------------

You can find it in the Nivigator view.

I get the message that the sql file is not found, but it exists.
----------------------------------------------------------------

You may get the following message, though the file exists:

.. code-block:: sh

  [DOMA4019] The file[META-INF/../select.sql] is not found from the classpath

When you use Eclipse, check that the location of the output folder of resources is
same as the one of class files in the Java Build Path dialog.

When you use Gradle, check that the resource files are copied to ``compileJava.destinationDir``
in advance of the compileJava task. See also :ref:`build-with-gradle`.


Features as the database access library
=======================================

Does Doma generate SQL statements?
----------------------------------

Yes, Doma generates following statements:

- INSERT
- DELETE
- UPDATE
- stored procedure call
- stored function call

Doma doesn't generate SELECT statements
but executes arbitrary SELECT statements and maps the results to the Java objects.

See also :doc:`query/index` for detail information.

How dynamic SQL statements are executed?
----------------------------------------

The dynamic SQL statements are built by directives that are represented as the SQL comments.

See also :doc:`sql` for detail information.

Does Doma map database relationships such as one-to-one and one-to-many to Java objects?
----------------------------------------------------------------------------------------

No.

Doma only maps each row of the SQL result set to a Java entity instance.

Does Doma provide a JDBC connection pooling feature?
----------------------------------------------------

No.

Use Doma together with
the JDBC connection pool library such as `HikariCP <https://github.com/brettwooldridge/HikariCP>`_.

