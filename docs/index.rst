.. Doma documentation master file, created by
   sphinx-quickstart on Thu Feb 13 12:43:15 2014.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

.. image:: images/doma.png
   :height: 200px
   :width: 200px
   :align: right
   :target: https://github.com/domaframework/doma

Welcome to Doma 2
=====================

Doma 2 is a database access framework for Java 8+.
Doma has some strong points:

* Verifies and generates source code at compile-time using annotation processing.
* Maps a database column to a user-defined Java object.
* Uses SQL-templates that is called two-way-SQL.
* Supports classes introduced in Java8, such as ``java.time.LocalDate``,  ``java.util.Optional`` and ``java.util.stream.Stream``.
* Has no dependence on other libraries.

This document consists of following sections:

* `User Documentation`_
* `Developer Documentation`_
* `About Doma`_
* `Links`_

User Documentation
==================

.. toctree::
   :maxdepth: 2

   getting-started-eclipse
   getting-started-idea
   config
   basic
   domain
   embeddable
   entity
   dao
   query/index
   query-builder/index
   sql
   expression
   transaction
   annotation-processing
   build
   lombok-support
   kotlin-support

Developer Documentation
=======================

.. toctree::
   :maxdepth: 2

   development

About Doma
==========

.. toctree::
   :maxdepth: 1

   release-notes
   faq

Links
=====

* `GitHub repository <https://github.com/domaframework/doma>`_
* `JavaDoc <http://www.javadoc.io/doc/org.seasar.doma/doma>`_
* `Examples <https://github.com/domaframework/simple-examples>`_
* `Doma-Gen <http://doma-gen.readthedocs.org/>`_
* `Doma 1 <http://doma.seasar.org/>`_

