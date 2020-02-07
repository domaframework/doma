===================
Development of Doma
===================

.. contents::
   :depth: 3

.. note::

  This document is written for the developers of Doma.

Before You Start
================

To build you will need Git and JDK 8.
Be sure that your ``JAVA_HOME`` environment variable points to the ``jdk1.8.0`` folder extracted from the JDK download.

Get the Source Code
===================

.. code-block:: bash

  $ git clone https://github.com/domaframework/doma.git
  $ cd doma

Build from the Command Line
===========================

.. code-block:: bash

  $ ./gradlew build

Format the Source Code
======================

We use `google-java-format`_ 1.6 for code formatting.

Command Line
------------

Use the `Spotless`_ gradle plugin:

.. code-block:: bash

  $ ./gradlew spotlessApply

IntelliJ
--------

Use the `google-java-format IntelliJ plugin`_.

Eclipse
-------

Use the `google-java-format Eclipse plugin`_.

Continuous Integration
======================

We use `Travis CI`_ for CI.
All pull requests to master brunch are tested on Travis CI.

  https://travis-ci.org/domaframework/doma

Documents
============

We use `Sphinx`_ to generate documents.
To use Sphinx you will need Python.

Set up an environment
---------------------

.. code-block:: bash

  $ cd docs
  $ pip install -r requirements.txt

Generate HTML files
-------------------

Execute the `sphinx-autobuild`_ command in the ``docs`` directory:

.. code-block:: bash

  $ sphinx-autobuild . _build/html

Visit the webpage served at http://127.0.0.1:8000. 


.. _google-java-format: https://github.com/google/google-java-format
.. _google-java-format IntelliJ plugin: https://github.com/google/google-java-format#intellij-android-studio-and-other-jetbrains-ides
.. _google-java-format Eclipse plugin: https://github.com/google/google-java-format#eclipse
.. _Spotless: https://github.com/diffplug/spotless
.. _Travis CI: http://docs.travis-ci.com/
.. _Sphinx: http://sphinx-doc.org/
.. _sphinx-autobuild: https://pypi.org/project/sphinx-autobuild/
