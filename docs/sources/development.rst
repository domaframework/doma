===========
Doma の開発
===========

.. contents:: 目次
   :depth: 3

.. note::

  このページは Doma の開発者に向けて書かれています。

ソースコード
============

.. code-block:: bash

  $ git clone git@github.com:domaframework/doma.git

ビルド
======

.. code-block:: bash

  $ ./gradlew build

Maven ローカルリポジトリへのインストール
----------------------------------------

.. code-block:: bash

  $ ./gradlew build install

.. note::

  ローカルで修正を加えたコードに対して :doc:`integration-test` を実行するには、
  ローカルの Maven リポジトリに
  Doma 本体の成果物を事前にインストールしておく必要があります。

Eclipse
=======

Eclipse の設定ファイルを生成できます。

.. code-block:: bash

  $ ./gradlew eclipse


Continuous Integration
======================

Continuous Integration の実行には `Travis CI`_ を利用しています。

  https://travis-ci.org/domaframework/doma

ドキュメント
============

ドキュメントの作成には `Sphinx`_ を利用しています。

環境構築
--------

.. code-block:: bash

   $ cd docs
   $ pip install -r requirements.txt

ドキュメントの生成
------------------

.. code-block:: bash

   $ make dirhtml

LiveReload
----------

Google Chrome に `LiveReload`_ をインストールすることで
ドキュメントの修正を即座にブラウザで確認できます。

この拡張機能を有効にした上でサーバーを起動します。

.. code-block:: bash

   $ python server.py

次の URL でドキュメントを確認できます。

   http://localhost:5500/_build/dirhtml/


.. _Travis CI: http://docs.travis-ci.com/
.. _LiveReload: https://chrome.google.com/webstore/detail/livereload/jnihajbhpnppcggbcgedagnkighmdlei
.. _Sphinx: http://sphinx-doc.org/

