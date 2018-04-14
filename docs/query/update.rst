==================
更新
==================

.. contents:: 目次
   :depth: 3

更新を行うには、 ``@Update`` をDaoのメソッドに注釈します。

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @Update
      int update(Employee employee);

      @Update
      Result<ImmutableEmployee> update(ImmutableEmployee employee);
  }

デフォルトでは、UPDATE文が自動生成されます。
``@Update`` の ``sqlFile`` にtrueを設定することで、任意のSQLファイルにマッピングできます。

パラメータのエンティティクラスにエンティティリスナーが指定されている場合、
更新の実行前にエンティティリスナーの ``preUpdate`` メソッドを呼び出されます。
また、更新の実行後にエンティティリスナーの ``postUpdate`` メソッドを呼び出されます。

戻り値
======

パラメータがイミュータブルなエンティティクラスの場合、
戻り値はそのエンティティクラスを要素とする ``org.seasar.doma.Result``
でなければいけません。

上記の条件を満たさないない場合、戻り値は更新件数を表す ``int`` でなければいけません。

.. _auto-update:

SQLの自動生成による更新
=======================

パラメータの型はエンティティクラスでなければいけません。
指定できるパラメータの数は1つです。
引数は ``null`` であってはいけません。

.. code-block:: java

  @Update
  int update(Employee employee);

  @Update
  Result<ImmutableEmployee> update(ImmutableEmployee employee);

SQL自動生成におけるバージョン番号と楽観的排他制御
-------------------------------------------------

次の条件を満たす場合に、楽観的排他制御が行われます。

* パラメータのエンティティクラスに@Versionが注釈されたプロパティがある
* @UpdateのignoreVersion要素がfalseである

楽観的排他制御が有効であれば、バージョン番号は識別子とともに更新条件に含まれ、
1増分して更新されます。
このときの更新件数が0件の場合、楽観的排他制御の失敗を示す
``OptimisticLockException`` がスローされます。
また、更新件数が0件でない場合、 ``OptimisticLockException`` はスローされず、
エンティティのバージョンプロパティの値が1増分されます。

ignoreVersion
~~~~~~~~~~~~~

``@Update`` の ``ignoreVersion`` 要素がtrueの場合、
バージョン番号は更新条件には含まれず、UPDATE文のSET句に含まれます。
バージョン番号はアプリケーションで設定した値で更新されます。
この場合、更新件数が0件であっても、 ``OptimisticLockException`` はスローされません。

.. code-block:: java

  @Update(ignoreVersion = true)
  int update(Employee employee);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

``@Update`` の ``suppressOptimisticLockException`` 要素が ``true`` の場合、
``@Version`` が注釈されたプロパティがあればバージョン番号は更新条件に含まれ増分もされますが、
更新件数が0件であっても ``OptimisticLockException`` はスローされません。
ただし、エンティティのバージョンプロパティの値は1増分されます。

.. code-block:: java

  @Update(suppressOptimisticLockException = true)
  int update(Employee employee);

更新対象プロパティの制御
------------------------

updatable
~~~~~~~~~

エンティティクラスに ``@Column`` が注釈されたプロパティがある場合、
``@Column`` の ``updatable`` 要素がfalseのものは更新対象外です。

exclude
~~~~~~~

``@Update`` の ``exclude`` 要素に指定されたプロパティを更新対象外とします。
プロパティがこの要素に指定されていれば、 ``@Column`` の ``updatable``
要素が ``true`` であっても更新対象外です。

.. code-block:: java

  @Update(exclude = {"name", "salary"})
  int update(Employee employee);

include
~~~~~~~

``@Update`` の ``include`` 要素に指定されたプロパティのみを更新対象とします。
``@Update`` の ``include`` 要素と ``exclude`` 要素の両方に
同じプロパティが指定された場合、そのプロパティは更新対象外になります。
プロパティがこの要素に指定されていても、 ``@Column`` の ``updatable``
要素が ``false`` であれば更新対象外です。

.. code-block:: java

  @Update(include = {"name", "salary"})
  int update(Employee employee);

excludeNull
~~~~~~~~~~~

``@Update`` の ``excludeNull`` 要素が ``true`` の場合、
値が ``null`` のプロパティを削除対象外とします。
この要素が ``true`` の場合、 ``@Column`` の ``updatable`` 要素が ``true`` であったり、
``@Update`` の ``include`` 要素にプロパティが指定されていても、
値が ``null`` であれば更新対象外です。

.. code-block:: java

  @Update(excludeNull = true)
  int update(Employee employee);

includeUnchanged
~~~~~~~~~~~~~~~~

この要素は、更新対象のエンティティクラスに
``@OriginalStates`` が注釈されたプロパティがある場合にのみ有効です。

この要素がtrueの場合、エンティティの全プロパティが更新対象となります。
つまり、全プロパティに対応するカラムがUPDATE文のSET句に含まれます。

この要素が ``false`` の場合、
エンティティが取得されてから実際に変更されたプロパティのみが更新対象になります。
つまり、変更されたプロパティに対応するカラムのみがUPDATE文のSET句に含まれます。

.. code-block:: java

  @Update(includeUnchanged = true)
  int update(Employee employee);

SQLファイルによる更新
=====================

SQLファイルによる更新を行うには、 ``@Update`` の ``sqlFile`` 要素に ``true`` を設定し、
メソッドに対応するSQLファイルを用意します。

.. note::

  SQLファイルによる更新は、 :ref:`populate` の利用有無によりルールが異なります。

更新カラムリスト生成コメントを使用する場合
-------------------------------------------------

最初のパラメータの型はエンティティクラスでなければいけません。
指定できるパラメータの数に制限はありません。
パラメータの型が基本型もしくはドメインクラスの場合、引数を ``null`` にできます。
それ以外の型の場合、引数は ``null`` であってはいけません。

.. code-block:: java

  @Update(sqlFile = true)
  int update(Employee employee, BigDecimal salary);

  @Update(sqlFile = true)
  Result<ImmutableEmployee> update(ImmutableEmployee employee, , BigDecimal salary);

たとえば、上記のメソッドに対応するSQLは次のように記述します。

.. code-block:: sql

  update employee set /*%populate*/ id = id where salary > /* salary */0

更新対象プロパティの制御に関するルールは、 :ref:`auto-update` と同じです。

更新カラムリスト生成コメントを使用しない場合
-------------------------------------------------

パラメータには任意の型が使用できます。
指定できるパラメータの数に制限はありません。
パラメータの型が基本型もしくはドメインクラスの場合、引数を ``null`` にできます。
それ以外の型の場合、引数は ``null`` であってはいけません。

.. code-block:: java

  @Update(sqlFile = true)
  int update(Employee employee);

  @Update(sqlFile = true)
  Result<ImmutableEmployee> update(ImmutableEmployee employee);

たとえば、上記のメソッドに対応するSQLは次のように記述します。

.. code-block:: sql

  update employee set name = /* employee.name */'hoge', salary = /* employee.salary */100
  where id = /* employee.id */0

SQLファイルによる更新では、
``@Update`` の ``exclude`` 要素、 ``include`` 要素、  ``excludeNull`` 要素、
``includeUnchanged`` 要素は参照されません。

SQLファイルにおけるバージョン番号と楽観的排他制御
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

次の条件を満たす場合に、楽観的排他制御が行われます。

* パラメータにエンティティクラスを含む
* パラメータの内、左から数えて最初に登場するエンティティクラスに@Versionが注釈されたプロパティがある
* @UpdateのignoreVersion要素がfalseである

ただし、SQLファイルに楽観的排他制御用のSQLを記述するのは、アプリケーション開発者の責任です。
たとえば、下記のSQLのように、
WHERE句でバージョンを番号を指定しSET句でバージョン番号を1だけ増分しなければいけません。

.. code-block:: sql

  update EMPLOYEE set DELETE_FLAG = 1, VERSION = /* employee.version */1 + 1
  where ID = /* employee.id */1 and VERSION = /* employee.version */1

このSQLの更新件数が0件の場合、楽観的排他制御の失敗を示す
``OptimisticLockException`` がスローされます。
更新件数が0件でない場合、 ``OptimisticLockException`` はスローされず、
エンティティのバージョンプロパティの値が1増分されます。

ignoreVersion
^^^^^^^^^^^^^

``@Update`` の ``ignoreVersion`` 要素が ``true`` の場合、
更新件数が0件であっても、 ``OptimisticLockException`` はスローされません。
また、エンティティのバージョンプロパティの値は変更されません。

.. code-block:: java

  @Update(sqlFile = true, ignoreVersion = true)
  int update(Employee employee);

suppressOptimisticLockException
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

``@Update`` の ``suppressOptimisticLockException`` 要素が ``true`` の場合、
更新件数が0件であっても、 ``OptimisticLockException`` はスローされません。
ただし、エンティティのバージョンプロパティの値は1増分されます。

.. code-block:: java

  @Update(sqlFile = true, suppressOptimisticLockException = true)
  int update(Employee employee);

一意制約違反
============

一意制約違反が発生した場合は、SQLファイルの使用の有無に関係なく
``UniqueConstraintException`` がスローされます。

クエリタイムアウト
==================

``@Update`` の ``queryTimeout`` 要素にクエリタイムアウトの秒数を指定できます。

.. code-block:: java

  @Update(queryTimeout = 10)
  int update(Employee employee);

この指定はSQLファイルの使用の有無に関係なく適用されます。
``queryTimeout`` 要素に値を指定しない場合、
:doc:`../config` に指定されたクエリタイムアウトが使用されます。

SQL のログ出力形式
==================

``@Update`` の ``sqlLog`` 要素に SQL のログ出力形式を指定できます。

.. code-block:: java

  @Update(sqlLog = SqlLogType.RAW)
  int update(Employee employee);

``SqlLogType.RAW`` はバインドパラメータ（?）付きの SQL をログ出力することを表します。
