.. Doma documentation master file, created by
   sphinx-quickstart on Thu Feb 13 12:43:15 2014.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

.. image:: images/doma.png
   :height: 200px
   :width: 200px
   :align: right
   :target: https://github.com/domaframework/doma

Welcome to Doma
===============

Doma は Java のDBアクセスフレームワークです。

Doma のバージョンには 1 と 2 がありますが、
このドキュメントは **バージョン 2** を対象としています。

Doma 2 には以下の特徴があります。

* 注釈処理を使用して **コンパイル時** にコードの生成やコードの検証を行う
* データベース上のカラムの値を振る舞いを持った Java オブジェクトにマッピングできる
* 2-way SQL と呼ばれる SQL テンプレートを利用できる
* Java 8 の ``java.time.LocalDate`` や  ``java.util.Optional`` や ``java.util.stream.Stream`` を利用できる
* JRE 以外のライブラリへの依存が一切ない

このドキュメントは複数のセクションから成ります。

* `User Documentation`_
* `Developer Documentation`_
* `About Doma`_
* `Links`_

User Documentation
==================

.. toctree::
   :maxdepth: 2

   getting-started
   config
   basic
   domain
   entity
   dao
   query/index
   query-builder/index
   sql
   expression
   transaction
   annotation-processing
   build

Developer Documentation
=======================

.. toctree::
   :maxdepth: 2

   development
   integration-test

About Doma
==========

.. toctree::
   :maxdepth: 1

   release-notes
   changelog
   faq

Links
=====

* `GitHub repository <https://github.com/domaframework/doma>`_
* `JavaDoc <http://www.javadoc.io/doc/org.seasar.doma/doma>`_
* `Examples <https://github.com/domaframework/simple-examples>`_
* `Doma-Gen <http://doma-gen.readthedocs.org/>`_
* `Doma 1 <http://doma.seasar.org/>`_

