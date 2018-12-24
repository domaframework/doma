==================
ファクトリ
==================

.. contents:: 目次
   :depth: 3

``java.sql.Connection`` が提供するファクトリメソッドからインスタンスを取得するには、
Daoのメソッドに次のアノテーションを注釈します。

* java.sql.Arrayを生成するには、@ArrayFactory
* java.sql.Blobを生成するには、@BlobFactory
* java.sql.Clobを生成するには、@ClobFactory
* java.sql.NClobを生成するには、@NClobFactory
* java.sql.SQLXMLを生成するには、@SQLXMLFactory

Arrayの生成
===========

戻り値の型は ``java.sql.Array`` 、パラメータは1つの配列型でなければいけません。
引数は ``null`` であってはいけません。

``@ArrayFactory`` の ``typeName`` 要素にはデータベースの型名を指定します。

.. code-block:: java

  @ArrayFactory(typeName = "integer")
  Array createIntegerArray(Integer[] elements);

Blobの生成
==========

戻り値の型は ``java.sql.Blob`` 、パラメータの数は0でなければいけません。

.. code-block:: java

  @BlobFactory
  Blob createBlob();

Clobの生成
==========

戻り値の型は ``java.sql.Clob`` 、パラメータの数は0でなければいけません。

.. code-block:: java

  @ClobFactory
  Clob createClob();

NClobの生成
===========

戻り値の型は ``java.sql.NClob`` 、パラメータの数は0でなければいけません。

.. code-block:: java

  @NClobFactory
  NClob createNClob();

SQLXMLの生成
============

戻り値の型は ``java.sql.SQLXML`` 、パラメータの数は0でなければいけません。

.. code-block:: java

  @SQLXMLFactory
  SQLXML createSQLXML();
