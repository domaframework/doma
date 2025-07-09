=========
Factories
=========

.. contents::
   :depth: 4

To obtain instances from the factory methods of ``java.sql.Connection``,
annotate DAO methods with one of the following annotations:

* @ArrayFactory
* @BlobFactory
* @ClobFactory
* @NClobFactory
* @SQLXMLFactory

Creating Array instances
========================

A return type must be ``java.sql.Array`` and the number of parameters must be one.
The parameter type must be an array type and the parameter must not be null.

Specify a database type name to the ``@ArrayFactory``'s ``typeName`` element:

.. code-block:: java

  @ArrayFactory(typeName = "integer")
  Array createIntegerArray(Integer[] elements);

Creating Blob instances
=======================

A return type must be ``java.sql.Blob`` and the number of parameters must be zero:

.. code-block:: java

  @BlobFactory
  Blob createBlob();

Creating Clob instances
=======================

A return type must be ``java.sql.Clob`` and the number of parameters must be zero:

.. code-block:: java

  @ClobFactory
  Clob createClob();

Creating NClob instances
========================

A return type must be ``java.sql.NClob`` and the number of parameters must be zero:

.. code-block:: java

  @NClobFactory
  NClob createNClob();

Creating SQLXML instances
=========================

A return type must be ``java.sql.SQLXML`` and the number of parameters must be zero:

.. code-block:: java

  @SQLXMLFactory
  SQLXML createSQLXML();
