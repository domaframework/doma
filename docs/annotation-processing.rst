=====================
Annotation processing
=====================

.. contents::
   :depth: 3

Doma uses `Pluggable Annotation Processing API <https://www.jcp.org/en/jsr/detail?id=269>`_ at compile time.

In this document, we describe the options for the annotation processors in Doma
and show you how to pass them to build tools.

Options
=======

doma.dao.package
  The package that the generated implementation classes of interfaces annotated with ``@Dao`` belong to.
  The specified value overrides the value of doma.dao.subpackage.
  The default value is the same package as the one the interfaces annotated with ``@Dao`` belong to.

doma.dao.subpackage
  The subpackage that the generated implementation classes of interfaces annotated with ``@Dao`` belong to.
  The specified value is overridden by the value of doma.dao.package.
  If this value is ``impl`` and the package of interfaces annotated with ``@Dao`` is ``example.dao``,
  the generated implementation classes belong to the package ``example.dao.impl``.

doma.dao.suffix
  The name suffix that the generated implementation classes of interfaces annotated with ``@Dao`` have.
  If this value is ``Bean`` and the simple name of the interface annotated with ``@Dao`` is ``EmployeeDao``,
  the simple name of the generated implementation class is ``EmployeeDaoBean``.
  The default value is ``Impl``.

doma.debug
  Whether to output the debug log in annotation processing.
  If the value is ``true``, the annotation processors output the debug log.
  The default value is ``false``.

doma.domain.converters
  The full qualified names of the classes annotated with ``@DomainConverters``.
  The names are described as comma separated list.
  This value are used to find external domain classes.

doma.entity.field.prefix
  The name prefix that the fields of the generated entity meta classes have.
  The value ``none`` means the prefix is not used.
  The default value is ``$``.

doma.expr.functions
  The full qualified name of the class that implements ``org.seasar.doma.expr.ExpressionFunctions``.
  The default value is ``org.seasar.doma.expr.ExpressionFunctions``.
  This value are used to determine which functions are available in expression comments.

doma.resources.dir
  The resource directory that contains the resource files such as a doma.compile.config file and sql files.
  Specify the value as an absolute path.
  If the value is not specified, the resource directory is same as the directory the classes are generated.

doma.sql.validation
  Whether to validate the existence of sql files and the grammar of sql comments.
  If the value is ``true``, the validations run.
  To disable the validations, set ``false``.
  The default value is ``true``.

doma.version.validation
  Whether to validate the versions of doma.jar between runtime and compile-time.
  If the value is ``true``, the validation runs.
  To disable the validation, set ``false``.
  The default value is ``true``.

doma.config.path
  The file path of the configuration file for Doma.
  The default value is ``doma.compile.config``.

Setting options in Eclipse
==========================

- Select “Project > Properties” from the menu bar and open the dialog
- Select “Java Compiler > Annotation Processing” from the left menu of the dialog
- Add "Processor options"

Setting options in IntelliJ IDEA
================================

- Select "Preferrences" from the menu bar and open the dialog
- Select "Build, Execution, Deployment > Compiler > Annotation Processors" from the left menu of the dialog
- Add "Annotation Processor options"

Setting options in javac
========================

- Use `the -A option <https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javac.html#BHCDIFEE>`_

Setting options in Gradle
=========================

- Use `the compilerArgs property
  <https://docs.gradle.org/5.0/dsl/org.gradle.api.tasks.compile.CompileOptions.html#org.gradle.api.tasks.compile.CompileOptions:compilerArgs>`_

.. code-block:: groovy

  compileJava {
      options {
          compilerArgs = ['-Adoma.dao.subpackage=impl', '-Adoma.dao.suffix=Impl']
      }
  }

Setting options with configuration file
=======================================

The options specified in the ``doma.compile.config`` file are available in all build tools
such as Eclipse, IDEA, Gradle and so on.

The ``doma.compile.config`` file must follow the properties file format
and be placed in the root directory such as ``src/main/resources``.

.. note::
  The options specified in the ``doma.compile.config`` file are overridden by
  the ones specific to the build tools.
