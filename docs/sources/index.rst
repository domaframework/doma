.. Doma documentation master file, created by
   sphinx-quickstart on Thu Feb 13 12:43:15 2014.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Welcome to Doma
===============

Doma は Java のDBアクセスライブラリです。

Doma のバージョンには 1 と 2 がありますが、
このドキュメントは **バージョン 2** を対象としています。
Doma 2 はオープンソースであり、コードは `GitHub で <https://github.com/domaframework/doma/>`_ ホストされています。
バージョン 1 については http://doma.seasar.org/ を参照してください。

Doma 2 には以下の特徴があります。

* 注釈処理を使用して **コンパイル時** にコードの生成やコードの検証を行う
* データベース上のカラムの値を振る舞いを持った Java オブジェクトにマッピングできる
* 2-way SQL と呼ばれる SQL テンプレートを利用できる
* Java 8 の ``java.util.Optional`` や ``java.util.Stream`` を利用できる
* JRE 以外のライブラリへの依存が一切ない

このドキュメントは複数のセクションから成ります。

* `ユーザードキュメント`_
* `Domaについて`_

ユーザードキュメント
====================

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

Domaについて
============

.. toctree::
   :maxdepth: 1

   faq
   changelog

