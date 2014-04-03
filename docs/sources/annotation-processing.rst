============================
注釈処理
============================

.. contents:: 目次
   :depth: 3

`Pluggable Annotation Processing API <https://www.jcp.org/en/jsr/detail?id=269>`_
を利用すると、ソースコードの自動生成や検証を **コンパイル時** に行うことができます。

DomaではこのAPIを利用することで ``@Entity`` や ``@Dao`` と言ったアノテーションが注釈されたクラスやインタフェースを処理し、
必要なクラスを自動生成します。
また、注釈されたクラスやインタフェースの検証を行い、Domaの規約に従っていないソースコードがある場合は
エラーメッセージをIDE（Eclipseなど）のエディタやjavacを実行したコンソール上に表示します。

ここでは、Domaが提供するオプションの種類と、ビルドツールごとのオプションの設定方法を説明します。

オプション
==================

doma.dao.package
  ``@Dao`` が注釈されたインタフェースの実装クラスが生成されるパッケージ。
  何らかの値を指定した場合、doma.dao.subpackageの指定よりも優先される。
  デフォルトの値は、 ``@Dao`` が注釈されたインタフェースと同じパッケージ。

doma.dao.subpackage
  ``@Dao`` が注釈されたインタフェースの実装クラスが生成されるサブパッケージ。
  doma.dao.packageに値を指定していない場合にのみ有効。
  ``@Dao`` が注釈されたインタフェースのパッケージが ``example.dao`` で、ここに指定した値が ``impl`` の場合、
  生成されるクラスのパッケージは ``example.dao.impl`` となる。

doma.dao.suffix
  ``@Dao`` が注釈されたインタフェースの実装クラスの名前のサフィックス。
  ``@Dao`` が注釈されたインタフェースの単純名が ``EmployeeDao`` で、ここに指定した値が ``Bean`` の場合、
  生成されるクラスの単純名は ``EmployeeDaoBean`` となる。
  デフォルトの値は ``Impl`` 。

doma.debug
  注釈処理のデバッグ情報をログ出力するかどうか。
  ``true`` の場合、ログ出力を行う。
  デフォルトの値は、 ``false`` 。

doma.domain.converters
  任意の型と基本型を相互変換する ``DomainConverter`` のプロバイダとなるクラスの完全修飾名のカンマ区切り。
  クラスは ``org.seasar.doma.DomainConverters`` によって注釈されていないければいけない。

doma.entity.field.prefix
  ``@Entity`` が注釈されたクラスごとに生成されるタイプクラスで使用される。
  タイプクラスのpublicなフィールド名のプレフィックス。
  ``none`` を指定するとプレフィックスを使用しないという意味になる。
  デフォルトの値は、 ``$`` 。

doma.expr.functions
  式コメントで利用可能な関数群を表すクラスの完全修飾名。
  ``org.seasar.doma.expr.ExpressionFunctions`` のサブタイプでなければいけない。
  デフォルトの値は、 ``org.seasar.doma.expr.ExpressionFunctions`` 。

doma.sql.validation
  SQLファイルの存在チェックとSQLコメントの文法チェックを行う場合は ``true`` 。
  行わない場合は ``false`` 。
  デフォルトの値は、 ``true`` 。

doma.version.validation
  注釈処理によるソースコード生成で利用したDomaのバージョンと実行時のDomaのバージョンが同じであることを
  チェックする場合は ``true`` 。
  しない場合は ``false`` 。
  Domaのあるバージョンで生成されたコードを含むライブラリを作成する場合に ``false`` を指定してビルドすると、
  そのライブラリの再利用性が高まります。
  ライブラリが依存するDomaのバージョンとは異なるバージョンのDomaで実行できるからです
  （Domaのバージョンに互換性がある限りにおいて）。
  デフォルトの値は、 ``true`` 。

Eclipse
=======

プロジェクトの「Properties」-「Java Compiler」-「Annotation Processing」の項目でオプションを登録します。

javac
=====

-Aオプションにより登録します。
詳細はjavacのドキュメントを参照してください。

Gradle
======

``compileJava.options.compilerArgs`` に指定します。

.. code-block:: groovy

  compileJava.options.compilerArgs = ['-Adoma.dao.subpackage=impl', '-Adoma.dao.suffix=Impl']

