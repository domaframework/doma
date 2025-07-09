=====================
Annotation Processing
=====================

.. contents::
   :depth: 4

Doma uses the `Pluggable Annotation Processing API <https://www.jcp.org/en/jsr/detail?id=269>`_ at compile time.

This document describes the options available for the annotation processors in Doma
and demonstrates how to configure them in various build tools.

Options
=======

doma.dao.package
----------------

The package to which the generated implementation classes of interfaces annotated with ``@Dao`` will belong.
This value overrides the value of doma.dao.subpackage.
By default, implementation classes are generated in the same package as the interfaces annotated with ``@Dao``.

doma.dao.subpackage
-------------------

The subpackage to which the generated implementation classes of interfaces annotated with ``@Dao`` will belong.
This value is overridden by the value of doma.dao.package if both are specified.
For example, if this value is set to ``impl`` and the package of interfaces annotated with ``@Dao`` is ``example.dao``,
the generated implementation classes will belong to the package ``example.dao.impl``.

doma.dao.suffix
---------------

The name suffix for the generated implementation classes of interfaces annotated with ``@Dao``.
For example, if this value is set to ``Bean`` and the simple name of the interface annotated with ``@Dao`` is ``EmployeeDao``,
the simple name of the generated implementation class will be ``EmployeeDaoBean``.
The default value is ``Impl``.

doma.debug
----------

Controls whether debug logs are output during annotation processing.
When set to ``true``, the annotation processors will output debug logs.
The default value is ``false``.

doma.domain.converters
----------------------

The fully qualified names of classes annotated with ``@DomainConverters``.
Multiple names should be provided as a comma-separated list.
This option is used to locate external domain classes.

doma.entity.field.prefix
------------------------

The name prefix for fields in the generated entity meta classes.
Setting the value to ``none`` disables the prefix.
The default value is ``$``.

doma.expr.functions
-------------------

The fully qualified name of the class that implements ``org.seasar.doma.expr.ExpressionFunctions``.
The default value is ``org.seasar.doma.expr.ExpressionFunctions``.
This option determines which functions are available in expressions within SQL directives.

doma.metamodel.enabled
----------------------

Controls whether meta classes are generated for the Criteria API.
When set to ``true``, metamodels are generated for all entity classes
even if they are not explicitly configured with ``metamodel = @Metamodel``.
The default value is ``false``.

doma.metamodel.prefix
---------------------

The name prefix for metamodel classes used in the Criteria API.
The default value is an empty string.

doma.metamodel.suffix
---------------------

The name suffix for metamodel classes used in the Criteria API.
The default value is ``_``.

doma.resources.dir
------------------

The resource directory containing resource files such as doma.compile.config and SQL files.
Specify this option as an absolute path.
If not specified, the resource directory defaults to the same directory where classes are generated.

doma.sql.validation
-------------------

Controls whether to validate the existence of SQL files and the grammar of SQL directives.
When set to ``true``, validations will run.
To disable validations, set this to ``false``.
The default value is ``true``.

doma.trace
----------

Controls whether trace logs are output during annotation processing.
When set to ``true``, the annotation processors will output trace logs, including the execution time of annotation processing.
The default value is ``false``.

doma.version.validation
-----------------------

Controls whether to validate version compatibility between the runtime and compile-time doma.jar.
When set to ``true``, version validation will run.
To disable validation, set this to ``false``.
The default value is ``true``.

doma.config.path
----------------

The file path of the configuration file for Doma.
The default value is ``doma.compile.config``.

Setting Options in Gradle
=========================

Use `the compilerArgs property
<https://docs.gradle.org/current/dsl/org.gradle.api.tasks.compile.CompileOptions.html#org.gradle.api.tasks.compile.CompileOptions:compilerArgs>`_:

.. tabs::

    .. tab:: Kotlin

        .. code-block:: kotlin

            tasks {
                compileJava {
                    options.compilerArgs.addAll(listOf("-Adoma.dao.subpackage=impl", "-Adoma.dao.suffix=Impl"))
                }
            }

    .. tab:: Groovy

        .. code-block:: groovy

            compileJava {
                options {
                    compilerArgs = ['-Adoma.dao.subpackage=impl', '-Adoma.dao.suffix=Impl']
                }
            }

Setting Options in Maven
=========================

Use `the compilerArgs parameter
<https://maven.apache.org/plugins/maven-compiler-plugin/examples/pass-compiler-arguments.html>`_:

.. code-block:: xml

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>17</source> <!-- depending on your project -->
                    <target>17</target> <!-- depending on your project -->
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.seasar.doma</groupId>
                            <artifactId>doma-processor</artifactId>
                            <version>${doma.version}</version>
                        </path>
                    </annotationProcessorPaths>
                    <compilerArgs>
                        <arg>-Adoma.resources.dir=${project.basedir}/src/main/resources</arg>
                        <arg>-Adoma.dao.subpackage=impl</arg>
                        <arg>-Adoma.dao.suffix=Impl</arg>
                    </compilerArgs>
                </configuration>
            </plugin>
        </plugins>
    </build>

Setting Options in Eclipse
==========================

Gradle
------

Use the Gradle plugin `com.diffplug.eclipse.apt
<https://plugins.gradle.org/plugin/com.diffplug.eclipse.apt>`_
and the ``processorArgs`` property:

.. tabs::

    .. tab:: Kotlin

        .. code-block:: kotlin

            plugins {
                id("com.diffplug.eclipse.apt") version "{{ eclipse_apt_version }}"
            }

            tasks {
                compileJava {
                    val aptOptions = extensions.getByType<com.diffplug.gradle.eclipse.apt.AptPlugin.AptOptions>()
                    aptOptions.processorArgs = mapOf(
                        "doma.dao.subpackage" to "impl",
                        "doma.dao.suffix" to "Impl"
                    )
                }
            }

    .. tab:: Groovy

        .. code-block:: groovy

            plugins {
                id 'com.diffplug.eclipse.apt' version '{{ eclipse_apt_version }}'
            }

            compileJava {
                aptOptions {
                    processorArgs = [
                        'doma.dao.subpackage' : 'impl', 'doma.dao.suffix' : 'Impl'
                    ]
                }
            }

Right-click on the project in Eclipse and select Gradle > Refresh Gradle Project.
This will apply the Gradle annotation processing options to Eclipse.

Maven
-----

Right-click on the project in Eclipse and select Maven > Update Project....
This will apply the Maven annotation processing options to Eclipse.

Setting Options in IntelliJ IDEA
================================

Gradle
------

Import your project as a Gradle project.
In this case, the options defined in build.gradle(.kts) will be used.

Maven
-----

Import your project as a Maven project.
In this case, the options defined in pom.xml will be used.

Setting Options with Configuration File
=======================================

Options specified in the ``doma.compile.config`` file are available across all build tools
including Eclipse, IntelliJ IDEA, Gradle, and Maven.

The ``doma.compile.config`` file must follow the properties file format
and should be placed in a root directory such as ``src/main/resources``.

.. note::
  Options specified in the ``doma.compile.config`` file are overridden by
  any options specified directly in the build tools.
