==========================
Frequently Asked Questions
==========================

.. contents::
   :depth: 3

General
=======

What does "Doma" mean?
----------------------

The name "Doma" comes from the "Dao Oriented database MApping framework".

What is annotation processing?
------------------------------

Annotation processing, that was introduced in Java 6,
allows us to validate and generate source code at compile time.

We use annotation processing for the following purposes:

- Generating meta classes from the classes annotated with ``@Entity`` and ``@Domain``.
- Generating implementation classes of the interfaces annotated with ``@Dao``.
- Validating SQL templates.

Runtime environment
===================

Which version of JRE does Doma support?
---------------------------------------

JRE 8, 9, 10, 11, 12, 13 and 14.

Which libraries are required for Doma to work?
----------------------------------------------

None.

Doma has no dependencies on other libraries.

Development environment
=======================

.. _which-version-of-jdk-does-doma-support:

Which version of JDK does Doma support?
---------------------------------------

JDK 8, 9, 10, 11, 12, 13 and 14.

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

You can find it in the Navigator view.

I get the message that the sql file is not found, but it exists.
----------------------------------------------------------------

You may get the following message, though the file exists:

.. code-block:: sh

  [DOMA4019] The file[META-INF/../select.sql] is not found from the classpath

When you use Eclipse, check that the location of the output folder of resources is
same as the one for the class files in the Java Build Path dialog.

When you use Gradle, check that the resource files are copied to ``compileJava.destinationDir``
in advance of the compileJava task. See also :ref:`build-with-gradle`.


Features as a database access library
=====================================

Does Doma generate SQL statements?
----------------------------------

Yes, Doma generates the following statements:

- INSERT
- DELETE
- UPDATE
- Stored procedure call
- Stored function call

Doma doesn't generate SELECT statements
but executes arbitrary SELECT statements and maps the results to the Java objects.

See also :doc:`query/index` for detailed information.

How are dynamic SQL statements executed?
----------------------------------------

Dynamic SQL statements are built by directives that are represented by the SQL comments.

See also :doc:`sql` for detail information.

Does Doma map database relationships such as one-to-one and one-to-many to Java objects?
----------------------------------------------------------------------------------------

No.

Doma only maps each row of the SQL result set to a Java entity instance.

Does Doma provide a JDBC connection pooling feature?
----------------------------------------------------

No.

Use Doma together with
a JDBC connection pool library such as `HikariCP <https://github.com/brettwooldridge/HikariCP>`_.

