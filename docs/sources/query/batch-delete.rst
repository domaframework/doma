==================
バッチ削除
==================

.. contents:: 目次
   :depth: 3

バッチ削除を行うには、 ``@BatchDelete`` をDaoのメソッドに注釈します。

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @BatchDelete
      int[] delete(List<Employee> employees);
      ...
  }

デフォルトでは、DELETE文が自動生成されます。
``@BatchDelete`` の ``sqlFile`` に ``true`` を設定することで、任意のSQLファイルにマッピングできます。

パラメータの要素である :doc:`../entity` にエンティティリスナーが指定されている場合、
削除の実行前にエンティティリスナーの ``preDelete``
メソッドがエンティティごとに呼び出されます。
また、削除の実行後にエンティティリスナーの ``postDelete``
メソッドがエンティティごとに呼び出されます。

戻り値
======

戻り値は各更新処理の更新件数を表す ``int[]`` でなければいけません。

SQLの自動生成によるバッチ削除
=============================

パラメータの型は :doc:`../entity` を要素とする ``java.lang.Iterable``
のサブタイプでなければいけません。
指定できるパラメータの数は1つです。
引数は ``null`` であってはいけません。
戻り値の配列の要素の数はパラメータの ``Iterable`` の要素の数と等しくなります。
配列のそれぞれの要素が更新された件数を返します。

SQL自動生成におけるバージョン番号と楽観的排他制御
-------------------------------------------------

次の条件を満たす場合に、楽観的排他制御が行われます。

* パラメータのjava.lang.Iterableのサブタイプの要素である
  :doc:`../entity` に@Versionが注釈されたプロパティがある
* @BatchDeleteのignoreVersion要素がfalseである

楽観的排他制御が有効であれば、バージョン番号は識別子とともに削除条件に含まれます。
この場合、削除件数が0件であれば、楽観的排他制御の失敗を示す
``BatchOptimisticLockException`` がスローされます。

ignoreVersion
~~~~~~~~~~~~~

``@BatchDelete`` の ``ignoreVersion`` 要素が ``true`` の場合、
バージョン番号は削除条件には含まれません。
削除件数が0件であっても ``BatchOptimisticLockException`` はスローされません。

.. code-block:: java

  @BatchDelete(ignoreVersion = true)
  int[] delete(List<Employee> employees);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

``@BatchDelete`` の ``suppressOptimisticLockException`` 要素が ``true`` の場合、
バージョン番号は削除条件に含まれますが、
削除件数が0件であっても ``BatchOptimisticLockException`` はスローされません。

.. code-block:: java

  @BatchDelete(suppressOptimisticLockException = true)
  int[] delete(List<Employee> employees);

SQLファイルによるバッチ削除
===========================

SQLファイルによるバッチ削除を行うには、 ``@BatchDelete`` の ``sqlFile`` 要素に
``true`` を設定し、 メソッドに対応するSQLファイルを用意します。

.. code-block:: java

  @BatchDelete(sqlFile = true)
  int[] delete(List<Employee> employees);

パラメータは任意の型を要素とする ``java.lang.Iterable`` のサブタイプでなければいけません。
指定できるパラメータの数は1つです。
引数は ``null`` であってはいけません。
戻り値の配列の要素の数はパラメータの ``Iterable`` の要素の数と等しくなります。
配列のそれぞれの要素が更新された件数を返します。

たとえば、上記のメソッドに対応するSQLは次のように記述します。

.. code-block:: sql

  delete from employee where name = /* employees.name */'hoge'

SQLファイル上では、パラメータの名前は ``java.lang.Iterable`` のサブタイプの要素を指します。

SQLファイルにおけるバージョン番号と楽観的排他制御
-------------------------------------------------

次の条件を満たす場合に、楽観的排他制御が行われます。

* パラメータの ``java.lang.Iterable`` のサブタイプの要素が :doc:`../entity` であり、
  :doc:`../entity` に@Versionが注釈されたプロパティがある
* @BatchDeleteのignoreVersion要素がfalseである

ただし、SQLファイルに楽観的排他制御用のSQLを記述するのは、アプリケーション開発者の責任です。
たとえば、下記のSQLのように、WHERE句でバージョンを番号を指定しなければいけません。

.. code-block:: sql

  delete from EMPLOYEE where ID = /* employees.id */1 and VERSION = /* employees.version */1

このSQLの削除件数が0件または複数件の場合、
楽観的排他制御の失敗を示す ``BatchOptimisticLockException`` がスローされます。

ignoreVersion
~~~~~~~~~~~~~

``@BatchDelete`` の ``ignoreVersion``
要素が ``true`` の場合、削除件数が0件または複数件であっても
``BatchOptimisticLockException`` はスローされません。

.. code-block:: java

  @BatchDelete(sqlFile = true, ignoreVersion = true)
  int[] delete(List<Employee> employees);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

``@BatchDelete`` の ``suppressOptimisticLockException``
要素が ``true`` の場合、削除件数が0件または複数件であっても
``BatchOptimisticLockException`` はスローされません。

.. code-block:: java

  @BatchDelete(sqlFile = true, suppressOptimisticLockException = true)
  int[] delete(List<Employee> employees);

クエリタイムアウト
==================

``@BatchDelete`` の ``queryTimeout`` 要素にクエリタイムアウトの秒数を指定できます。

.. code-block:: java

  @BatchDelete(queryTimeout = 10)
  int[] delete(List<Employee> employees);

この指定は、SQLファイルの使用の有無に関係なく適用されます。
``queryTimeout`` 要素に値を指定しない場合、
:doc:`../config` に指定されたクエリタイムアウトが使用されます。

バッチサイズ
============

``@BatchDelete`` の ``batchSize`` 要素にバッチサイズを指定できます。

.. code-block:: java

  @BatchDelete(batchSize = 10)
  int[] delete(List<Employee> employees);

この設定は、SQLファイルの使用の有無に関係なく適用されます。
``batchSize`` 要素に値を指定しない場合、 :doc:`../config` に指定されたバッチサイズが使用されます。


SQL のログ出力形式
==================

``@BatchDelete`` の ``sqlLog`` 要素に SQL のログ出力形式を指定できます。

.. code-block:: java

  @BatchDelete(sqlLog = SqlLogType.RAW)
  int[] delete(List<Employee> employees);

``SqlLogType.RAW`` はバインドパラメータ（?）付きの SQL をログ出力することを表します。
