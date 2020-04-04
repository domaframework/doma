=======================
Building an application
=======================

.. contents::
   :depth: 3

Before You Start
================

Maven Central Repository
------------------------

You can pull the jar file of the Doma framework from the Maven central repository.
The group id and artifact id are as follows:

:GroupId: org.seasar.doma
:ArtifactId: doma

See also: https://search.maven.org/artifact/org.seasar.doma/doma/

.. _build-with-gradle:

Build with Gradle
=================

Write your build.gradle script, then run ``gradle build``.

To simplify your build.script, we recommend you use
the `Doma Compile Plugin`_.

See build.gradle in the `simple-boilerplate`_ repository as an example.

Build with IntelliJ IDEA
========================

Use a newer version of IntelliJ IDEA, and then import your project as a Gradle project.
To know how to import project, see :ref:`idea-import-project`.

.. _eclipse-build:

Build with Eclipse
==================

Generate eclipse setting files with Gradle, and then import your project into Eclipse.
To generate the setting files, run ``gradle eclipse``.

To simplify your build.script, we recommend you use the `Doma Compile Plugin`_ and the `AptEclipsePlugin`_.

See build.gradle in the `simple-boilerplate`_ repository as an example.


.. _Doma Compile Plugin: https://github.com/domaframework/doma-compile-plugin
.. _AptEclipsePlugin: https://plugins.gradle.org/plugin/com.diffplug.eclipse.apt
.. _simple-boilerplate: https://github.com/domaframework/simple-boilerplate