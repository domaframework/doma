=============
JPMS support
=============

.. contents::
   :depth: 4

Overview
========

Doma supports the Java Platform Module System (JPMS) introduced with Java 9.

Modules
=======

Doma provides the following modules:

+----------------+------------------------------------+
| Artifact ID    |  Module Name                       |
+================+====================================+
| doma-core      |  org.seasar.doma.core              |
+----------------+------------------------------------+
| doma-kotlin    |  org.seasar.doma.kotlin            |
+----------------+------------------------------------+
| doma-processor |  org.seasar.doma.processor         |
+----------------+------------------------------------+
| doma-slf4j     |  org.seasar.doma.slf4j             |
+----------------+------------------------------------+
| doma-template  |  org.seasar.doma.template          |
+----------------+------------------------------------+

Usage
=====

Doma uses reflection to access Entity properties.
For this to work, you need to open packages containing Entity classes:

.. code-block:: java

    module example.app {
      requires org.seasar.doma.core;
      requires org.seasar.doma.slf4j;

      opens example.app.entity;
    }

Alternatively, you can open your entire module instead of specific packages:

.. code-block:: java

    open module example.app {
      requires org.seasar.doma.core;
      requires org.seasar.doma.slf4j;
    }
