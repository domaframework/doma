==================
削除
==================

.. contents:: 目次
   :depth: 3

削除を行うには、 ``@Delete`` をDaoのメソッドに注釈します。

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @Delete
      int delete(Employee employee);
  }

デフォルトでは、DELETE文が自動生成されます。
``@Delete`` の ``sqlFile`` に ``true`` を設定することで、任意のSQLファイルにマッピングできます。

パラメータのエンティティクラスにエンティティリスナーが指定されている場合、
削除の実行前にエンティティリスナーの ``preDelete`` メソッドが呼び出されます。
また、削除の実行後にエンティティリスナーの ``postDelete`` メソッドを呼び出されます。

戻り値
======

戻り値は更新件数を表す ``int`` でなければいけません。

SQLの自動生成による削除
=============================

パラメータの型はエンティティクラスでなければいけません。
指定できるパラメータの数は1つです。
引数は ``null`` であってはいけません。

.. code-block:: java

  @Delete
  int delete(Employee employee);

  @Delete
  Result<ImmutableEmployee> delete(ImmutableEmployee employee);

SQL自動生成におけるバージョン番号と楽観的排他制御
-------------------------------------------------

次の条件を満たす場合に、楽観的排他制御が行われます。

* パラメータのエンティティクラスに@Versionが注釈されたプロパティがある
* @DeleteのignoreVersion要素がfalseである

楽観的排他制御が有効であれば、バージョン番号は識別子とともに削除条件に含まれます。
この場合、削除件数が0件であれば、楽観的排他制御の失敗を示す ``OptimisticLockException`` がスローされます。

ignoreVersion
~~~~~~~~~~~~~

``@Delete`` の ``ignoreVersion`` 要素が ``true`` の場合、 バージョン番号は削除条件に含まれません。
この場合、削除件数が0件であっても、 ``OptimisticLockException`` はスローされません。

.. code-block:: java

  @Delete(includeVersion = true)
  int delete(Employee employee);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

``@Delete`` の ``suppressOptimisticLockException`` 要素が ``true`` の場合、
バージョン番号は削除条件に含まれます。
しかし、この場合、削除件数が0件であっても、 ``OptimisticLockException`` はスローされません。

.. code-block:: java

  @Delete(suppressOptimisticLockException = true)
  int delete(Employee employee);

SQLファイルによる削除
===========================

SQLファイルによる削除を行うには、 ``@Delete`` の ``sqlFile`` 要素に ``true`` を設定し、
メソッドに対応するSQLファイルを用意します。

パラメータには任意の型が使用できます。
指定できるパラメータの数に制限はありません。
パラメータの型が基本型もしくはドメインクラスの場合、引数を ``null`` にできます。
それ以外の型の場合、引数は ``null`` であってはいけません。

エンティティにエンティティリスナーが指定されていても、エンティティリスナーのメソッドは呼び出しません。

.. code-block:: java

  @Delete(sqlFile = true)
  int delete(Employee employee);

たとえば、上記のメソッドに対応するSQLは次のように記述します。

.. code-block:: sql

  delete from employee where name = /* employee.name */'hoge'

SQLファイルにおけるバージョン番号と楽観的排他制御
-------------------------------------------------

次の条件を満たす場合に、楽観的排他制御が行われます。

* パラメータにエンティティクラスを含む
* パラメータの内、左から数えて最初に登場するエンティティクラスに@Versionが注釈されたプロパティがある
* @DeleteのignoreVersion要素がfalseである
* @DeleteのsuppressOptimisticLockException要素がfalseである

ただし、SQLファイルに楽観的排他制御用のSQLを記述するのは、アプリケーション開発者の責任です。
たとえば、下記のSQLのように、WHERE句でバージョンを番号を指定しなければいけません。

.. code-block:: sql

  delete from EMPLOYEE where ID = /* employee.id */1 and VERSION = /* employee.version */1

このSQLの削除件数が0件の場合、楽観的排他制御の失敗を示す ``OptimisticLockException`` がスローされます。
削除件数が0件でない場合、 ``OptimisticLockException`` はスローされません。

ignoreVersion
~~~~~~~~~~~~~

``@Delete`` の ``ignoreVersion`` 要素が ``true`` の場合、
削除件数が0件であっても、 ``OptimisticLockException`` はスローされません。

.. code-block:: java

  @Delete(sqlFile = true, includeVersion = true)
  int delete(Employee employee);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

``@Delete`` の ``suppressOptimisticLockException`` 要素が ``true`` の場合、
削除件数が0件であっても、 ``OptimisticLockException`` はスローされません。

.. code-block:: java

  @Delete(sqlFile = true, suppressOptimisticLockException = true)
  int delete(Employee employee);

クエリタイムアウト
==================


``@Delete`` の ``queryTimeout`` 要素にクエリタイムアウトの秒数を指定できます。

.. code-block:: java

  @Delete(queryTimeout = 10)
  int delete(Employee employee);

この指定は、SQLファイルの使用の有無に関係なく適用されます。
``queryTimeout`` 要素に値を指定しない場合、 :doc:`../config` に指定されたクエリタイムアウトが使用されます。

SQL のログ出力形式
==================

``@Delete`` の ``sqlLog`` 要素に SQL のログ出力形式を指定できます。

.. code-block:: java

  @Delete(sqlLog = SqlLogType.RAW)
  int delete(Employee employee);

``SqlLogType.RAW`` はバインドパラメータ（?）付きの SQL をログ出力することを表します。
