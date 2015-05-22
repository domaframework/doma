==================
バッチ更新
==================

.. contents:: 目次
   :depth: 3

バッチ更新を行うには、 ``@BatchUpdate`` をDaoのメソッドに注釈します。

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @BatchUpdate
      int[] update(List<Employee> employees);

      @BatchUpdate
      BatchResult<ImmutableEmployee> update(List<ImmutableEmployee> employees);
  }

デフォルトでは、UPDATE文が自動生成されます。
``@BatchUpdate`` の ``sqlFile`` に ``true`` を設定することで、任意のSQLファイルにマッピングできます。

パラメータの要素の :doc:`../entity` にエンティティリスナーが指定されている場合、
更新の実行前にエンティティリスナーの ``preUpdate`` メソッドがエンティティごとに呼び出されます。
また、更新の実行後にエンティティリスナーの ``postUpdate`` メソッドがエンティティごとに呼び出されます。

戻り値
======

パラメータ ``Iterable`` のサブタイプの要素がイミュータブルなエンティティクラスの場合、
戻り値はそのエンティティクラスを要素とする ``org.seasar.doma.BatchResult``
でなければいけません。

上記の条件を満たさないない場合、戻り値は各更新処理の更新件数を表す ``int[]`` でなければいけません。

.. _auto-batch-update:

SQLの自動生成によるバッチ更新
=============================

パラメータの型は :doc:`../entity` を要素とする ``java.lang.Iterable`` のサブタイプでなければいけません。
指定できるパラメータの数は1つです。
引数は ``null`` であってはいけません。
戻り値の配列の要素の数はパラメータの ``Iterable`` の要素の数と等しくなります。
配列のそれぞれの要素が更新された件数を返します。

SQL自動生成におけるバージョン番号と楽観的排他制御
-------------------------------------------------

次の条件を満たす場合に、楽観的排他制御が行われます。

* パラメータのjava.lang.Iterableのサブタイプの要素である
  :doc:`../entity` に@Versionが注釈されたプロパティがある
* @BatchUpdateのignoreVersion要素がfalseである

楽観的排他制御が有効であれば、バージョン番号は識別子とともに更新条件に含まれ、
1増分して更新されます。
このときの更新件数が0件の場合、楽観的排他制御の失敗を示す
``BatchOptimisticLockException`` がスローされます。
一方、更新件数が1件の場合は、 ``BatchOptimisticLockException`` はスローされず、
エンティティのバージョンプロパティの値が1増分されます。

ignoreVersion
~~~~~~~~~~~~~

``@BatchUpdate`` の ``ignoreVersion`` 要素が ``true`` の場合、
バージョン番号は更新条件には含まれず、UPDATE文のSET句に含まれます。
バージョン番号はアプリケーションで設定した値で更新されます。
この場合、更新件数が0件であっても、 ``BatchOptimisticLockException`` はスローされません。

.. code-block:: java

  @BatchUpdate(ignoreVersion = true)
  int[] update(List<Employee> employees);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

``@BatchUpdate`` の ``suppressOptimisticLockException`` 要素が ``true`` の場合、
``@Versioni`` が注釈されたプロパティがあればバージョン番号は更新条件に含まれ増分もされますが、
更新件数が0件であっても ``BatchOptimisticLockException`` はスローされません。
ただし、エンティティのバージョンプロパティの値は1増分されます。

.. code-block:: java

  @BatchUpdate(suppressOptimisticLockException = true)
  int[] update(List<Employee> employees);

更新対象プロパティ
------------------

updatable
~~~~~~~~~

:doc:`../entity` に ``@Column`` が注釈されたプロパティがある場合、
``@Column`` の ``updatable`` 要素が ``false`` のものは更新対象外です。

exclude
~~~~~~~

``@BatchUpdate`` の ``exclude`` 要素に指定されたプロパティを更新対象外とします。
プロパティがこの要素に指定されていれば、 ``@Column`` の ``updatable`` 要素が
``true`` であっても削除対象外です。

.. code-block:: java

  @BatchUpdate(exclude = {"name", "salary"})
  int[] update(List<Employee> employees);

include
~~~~~~~

``@BatchUpdate`` の ``include`` 要素に指定されたプロパティのみを削除対象とします。
``@BatchUpdate`` の ``include`` 要素と ``exclude``
要素の両方に同じプロパティが指定された場合、そのプロパティは更新対象外になります。
プロパティがこの要素に指定されていても、 ``@Column`` の
``updatable`` 要素が ``false`` であれば更新対象外です。

.. code-block:: java

  @BatchUpdate(include = {"name", "salary"})
  int[] update(List<Employee> employees);

SQLファイルによるバッチ更新
===========================

SQLファイルによるバッチ更新を行うには、
``@BatchUpdate`` の ``sqlFile`` 要素に ``true`` を設定し、
メソッドに対応するSQLファイルを用意します。

.. note::

  SQLファイルによるバッチ更新は、 :ref:`populate` の利用有無によりルールが異なります。

更新カラムリスト生成コメントを使用する場合
-------------------------------------------------

.. code-block:: java

  @BatchUpdate(sqlFile = true)
  int[] update(List<Employee> employees);

  @BatchUpdate
  BatchResult<ImmutableEmployee> update(List<ImmutableEmployee> employees);

パラメータの型は :doc:`../entity` を要素とする ``java.lang.Iterable`` のサブタイプでなければいけません。
指定できるパラメータの数は1つです。
引数は ``null`` であってはいけません。
戻り値の配列の要素の数はパラメータの ``Iterable`` の要素の数と等しくなります。
配列のそれぞれの要素が更新された件数を返します。

たとえば、上記のメソッドに対応するSQLは次のように記述します。

.. code-block:: sql

  update employee set /*%populate*/ id = id where name = /* employees.name */'hoge'

SQLファイル上では、パラメータの名前は ``Iterable`` のサブタイプの要素を指します。

更新対象プロパティの制御に関するルールは、 :ref:`auto-batch-update` と同じです。

更新カラムリスト生成コメントを使用しない場合
-------------------------------------------------

.. code-block:: java

  @BatchUpdate(sqlFile = true)
  int[] update(List<Employee> employees);

  @BatchUpdate
  BatchResult<ImmutableEmployee> update(List<ImmutableEmployee> employees);

パラメータは任意の型を要素とする ``java.lang.Iterable`` のサブタイプでなければいけません。
指定できるパラメータの数は1つです。
引数は ``null`` であってはいけません。
戻り値の配列の要素の数はパラメータの ``Iterable`` の要素の数と等しくなります。
配列のそれぞれの要素が更新された件数を返します。

たとえば、上記のメソッドに対応するSQLは次のように記述します。

.. code-block:: sql

  update employee set name = /* employees.name */'hoge', salary = /* employees.salary */100
  where id = /* employees.id */0

SQLファイル上では、パラメータの名前は ``Iterable`` のサブタイプの要素を指します。

SQLファイルによるバッチ更新では、バージョン番号の自動更新は行われません。
また、 ``@BatchUpdate`` の ``exclude`` 要素、 ``include`` 要素は参照されません。

SQLファイルにおけるバージョン番号と楽観的排他制御
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

次の条件を満たす場合に、楽観的排他制御が行われます。

* パラメータのjava.lang.Iterableのサブタイプの要素が :doc:`../entity` であり、
  :doc:`../entity` に@Versionが注釈されたプロパティがある
* @BatchUpdateのignoreVersion要素がfalseである

ただし、SQLファイルに楽観的排他制御用のSQLを記述するのは、アプリケーション開発者の責任です。
たとえば、下記のSQLのように、
WHERE句でバージョンを番号を指定しSET句でバージョン番号を1だけ増分しなければいけません。

.. code-block:: sql

  update EMPLOYEE set DELETE_FLAG = 1, VERSION = /* employees.version */1 + 1
  where ID = /* employees.id */1 and VERSION = /* employees.version */1

このSQLの更新件数が0件または複数件の場合、楽観的排他制御の失敗を示す
``BatchOptimisticLockException`` がスローされます。
更新件数が1件の場合、 ``BatchOptimisticLockException`` はスローされず、
エンティティのバージョンプロパティの値が1増分されます。

楽観的排他制御が有効であれば、バージョン番号は識別子とともに更新条件に含まれ、
1増分して更新されます。
このときの更新件数が0件または複数件の場合、楽観的排他制御の失敗を示す
``BatchOptimisticLockException`` がスローされます。
一方、更新件数が1件の場合、 ``BatchOptimisticLockException``
はスローされず、エンティティのバージョンプロパティの値が1増分されます。

ignoreVersion
^^^^^^^^^^^^^

``@BatchUpdate`` の ``ignoreVersion`` 要素が ``true`` の場合、
更新件数が0件または複数件であっても、 ``BatchOptimisticLockException`` はスローされません。
また、エンティティのバージョンプロパティの値は変更されません。

.. code-block:: java

  @BatchUpdate(sqlFile = true, ignoreVersion = true)
  int[] update(List<Employee> employees);

suppressOptimisticLockException
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

``@BatchUpdate`` の ``suppressOptimisticLockException`` 要素が ``true`` の場合、
更新件数が0件または複数件であっても ``BatchOptimisticLockException`` はスローされません。
ただし、エンティティのバージョンプロパティの値は1増分されます。

.. code-block:: java

  @BatchUpdate(sqlFile = true, suppressOptimisticLockException = true)
  int[] update(List<Employee> employees);

一意制約違反
============

一意制約違反が発生した場合は、SQLファイルの使用の有無に関係なく
``UniqueConstraintException`` がスローされます。

クエリタイムアウト
==================

``@BatchUpdate`` の ``queryTimeout`` 要素にクエリタイムアウトの秒数を指定できます。

.. code-block:: java

  @BatchUpdate(queryTimeout = 10)
  int[] update(List<Employee> employees);

この設定は、SQLファイルの使用の有無に関係なく適用されます。
``queryTimeout`` 要素に値を指定しない場合、
設定クラスに指定されたクエリタイムアウトが使用されます。

バッチサイズ
============

``@BatchUpdate`` の ``batchSize`` 要素にバッチサイズを指定できます。

.. code-block:: java

  @BatchUpdate(batchSize = 10)
  int[] update(List<Employee> employees);

この指定は、SQLファイルの使用の有無に関係なく適用されます。
``batchSize`` 要素に値を指定しない場合、 :doc:`../config` クラスに指定されたバッチサイズが使用されます。

SQL のログ出力形式
==================

``@BatchUpdate`` の ``sqlLog`` 要素に SQL のログ出力形式を指定できます。

.. code-block:: java

  @BatchUpdate(sqlLog = SqlLogType.RAW)
  int[] update(List<Employee> employees);

``SqlLogType.RAW`` はバインドパラメータ（?）付きの SQL をログ出力することを表します。
