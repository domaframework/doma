===========
統合テスト
===========

.. contents:: 目次
   :depth: 3

.. note::

  このページは Doma の開発者に向けて書かれています。

統合テストの目的は2つです。

* Eclipse や javac の注釈処理の挙動をテストする
* データベースへのアクセスを伴う機能をテストする

統合テストでは RDBMS を使ったテストを行います。
デフォルトで使用する RDBMS は H2 Database Engine です。

ソースコード
============

.. code-block:: bash

  $ git clone git@github.com:domaframework/doma-it.git

ビルド
======

.. code-block:: bash

  $ ./gradlew build

Eclipse
=======

Eclipse の設定ファイルを生成できます。

.. code-block:: bash

  $ ./gradlew eclipse

ファクトリパスの設定も行われます。

Continuous Integration
======================

`Travis CI`_ を利用しています。

  https://travis-ci.org/domaframework/doma-it

以下の RDBMS を利用したテストを実行しています。

* H2 Database Engine
* HSQLDB
* MySQL
* PostgreSQL


.. _Travis CI: http://docs.travis-ci.com/
