==================
バッチ挿入
==================

.. contents:: 目次
   :depth: 3

バッチ挿入を行うには、 ``@BatchInsert`` をDaoのメソッドに注釈します。

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @BatchInsert
      int[] insert(List<Employee> employees);

      @BatchInsert
      BatchResult<ImmutableEmployee> insert(List<ImmutableEmployee> employees);
  }

デフォルトでは、INSERT文が自動生成されます。
``@BatchInsert`` の ``sqlFile`` に ``true`` を設定することで、任意のSQLファイルにマッピングできます。

パラメータの要素のに :doc:`../entity` が指定されている場合、
挿入の実行前にエンティティリスナーの ``preInsert`` メソッドをエンティティごとに呼び出します。
また、挿入の実行後にエンティティリスナーの ``postInsert`` メソッドをエンティティごとに呼び出します。

戻り値
======

パラメータ ``Iterable`` のサブタイプの要素がイミュータブルなエンティティクラスの場合、
戻り値はそのエンティティクラスを要素とする ``org.seasar.doma.BatchResult``
でなければいけません。

上記の条件を満たさないない場合、戻り値は各更新処理の更新件数を表す ``int[]`` でなければいけません。

SQLの自動生成によるバッチ挿入
=============================

パラメータの型は :doc:`../entity` 要素とする ``java.lang.Iterable`` のサブタイプでなければいけません。
指定できるパラメータの数は1つです。
引数は ``null`` であってはいけません。
戻り値の配列の要素の数はパラメータの ``Iterable`` の要素の数と等しくなります。
配列のそれぞれの要素が更新された件数を返します。

識別子
------

:doc:`../entity` の識別子に、 ``@GeneratedValue`` が注釈されている場合、
識別子が自動的に生成され設定されます。

バージョン番号
--------------

:doc:`../entity` に ``@Version`` が注釈されたプロパティがある場合、
そのプロパティに明示的に ``0`` 以上の値が設定されていればその値を使用します。
もし設定されていないか、 ``0`` 未満の値が設定されていれば ``1`` を自動で設定します。

挿入対象プロパティ
------------------

insertable
~~~~~~~~~~

:doc:`../entity` に ``@Column`` が注釈されたプロパティがある場合、
``@BatchInsert`` の ``insertable`` 要素が ``false`` のものは挿入対象外です。

exclude
~~~~~~~

``@BatchInsert`` の ``exclude`` 要素に指定されたプロパティを挿入対象外とします。
プロパティがこの要素に指定されていれば、 ``@Column`` の ``insertable`` 要素が ``true`` であっても挿入対象外です。

.. code-block:: java

  @BatchInsert(exclude = {"name", "salary"})
  int[] insert(List<Employee> employees);

include
~~~~~~~

``@BatchInsert`` の ``include`` 要素に指定されたプロパティのみを挿入対象とします。
``@BatchInsert`` の ``include`` 要素と ``exclude`` 要素の両方に同じプロパティが指定された場合、
そのプロパティは挿入対象外になります。
プロパティがこの要素に指定されていても、 ``@Column`` の ``insertable`` 要素が ``false`` であれば挿入対象外です。

.. code-block:: java

  @BatchInsert(include = {"name", "salary"})
  int[] insert(List<Employee> employees);

SQLファイルによるバッチ挿入
===========================

SQLファイルによるバッチ挿入を行うには、 ``@BatchInsert`` の ``sqlFile`` 要素に ``true`` を設定し、
メソッドに対応するSQLファイルを用意します。

.. code-block:: java

  @BatchInsert(sqlFile = true)
  int[] insert(List<Employee> employees);

  @BatchInsert(sqlFile = true)
  BatchResult<ImmutableEmployee> insert(List<ImmutableEmployee> employees);

パラメータは任意の型を要素とする ``java.lang.Iterable`` のサブタイプでなければいけません。
指定できるパラメータの数は1つです。
引数は ``null`` であってはいけません。
戻り値の配列の要素の数はパラメータの ``Iterable`` の要素の数と等しくなります。
配列のそれぞれの要素が更新された件数を返します。

:doc:`../entity` にエンティティリスナーが指定されていても、エンティティリスナーのメソッドは呼び出しません。

たとえば、上記のメソッドに対応するSQLは次のように記述します。

.. code-block:: sql

  insert into employee (id, name, salary, version) 
  values (/* employees.id */0, /* employees.name */'hoge', /* employees.salary */100, /* employees.version */0)

SQLファイル上では、パラメータの名前は ``java.lang.Iterable`` のサブタイプの要素を指します。

SQLファイルによるバッチ挿入では、識別子の自動設定やバージョン番号の自動設定は行われません。
また、 ``@BatchInsert`` の ``exclude`` 要素、 ``include`` 要素は参照されません。

一意制約違反
============

一意制約違反が発生した場合は、SQLファイルの使用の有無に関係なく ``UniqueConstraintException`` がスローされます。

クエリタイムアウト
==================

``@BatchInsert`` の ``queryTimeout`` 要素にクエリタイムアウトの秒数を指定できます。

.. code-block:: java

  @BatchInsert(queryTimeout = 10)
  int[] insert(List<Employee> employees);

この指定は、SQLファイルの使用の有無に関係なく適用されます。
``queryTimeout`` 要素に値を指定しない場合、  :doc:`../config` に指定されたクエリタイムアウトが使用されます。

バッチサイズ
============

``@BatchInsert`` の ``batchSize`` 要素にバッチサイズを指定できます。

.. code-block:: java

  @BatchInsert(batchSize = 10)
  int[] insert(List<Employee> employees);

この指定は、SQLファイルの使用の有無に関係なく適用されます。
``batchSize`` 要素に値を指定しない場合、  :doc:`../config` に指定されたバッチサイズが使用されます。

SQL のログ出力形式
==================

``@BatchInsert`` の ``sqlLog`` 要素に SQL のログ出力形式を指定できます。

.. code-block:: java

  @BatchInsert(sqlLog = SqlLogType.RAW)
  int insert(Employee employee);

``SqlLogType.RAW`` はバインドパラメータ（?）付きの SQL をログ出力することを表します。
