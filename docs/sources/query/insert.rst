==================
挿入
==================

.. contents:: 目次
   :depth: 3

挿入を行うには、 ``@Insert`` をDaoのメソッドに注釈します。

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @Insert
      int insert(Employee employee);

      @Insert
      Result<ImmutableEmployee> insert(ImmutableEmployee employee);
  }

デフォルトでは、INSERT文が自動生成されます。
``@Insert`` の ``sqlFile`` 要素に ``true`` を設定することで、任意のSQLファイルにマッピングできます。

パラメータの :doc:`../entity` にエンティティリスナーが指定されている場合、
挿入の実行前にエンティティリスナーの ``preInsert`` メソッドが呼び出されます。
また、挿入の実行後にエンティティリスナーの ``postInsert`` メソッドが呼び出されます。

戻り値
======

パラメータがイミュータブルなエンティティクラスの場合、
戻り値はそのエンティティクラスを要素とする ``org.seasar.doma.jdbc.Result``
でなければいけません。

上記の条件を満たさないない場合、戻り値は更新件数を表す ``int`` でなければいけません。

SQLの自動生成による挿入
=======================

パラメータの型はエンティティクラスでなければいけません。
指定できるパラメータの数は1つです。
引数はnullであってはいけません。

.. code-block:: java

  @Insert
  int insert(Employee employee);

  @Insert
  Result<ImmutableEmployee> insert(ImmutableEmployee employee);

識別子
------

:doc:`../entity` の識別子に、 ``@GeneratedValue`` が注釈されている場合、
識別子が自動的に生成され設定されます。

バージョン番号
--------------

:doc:`../entity` に ``@Version`` が注釈されたプロパティがある場合、
そのプロパティに明示的に ``0`` 以上の値が設定されていればその値を使用します。
もし設定されていないか、 ``0`` 未満の値が設定されていれば ``1`` を自動で設定します。

挿入対象プロパティの制御
~~~~~~~~~~~~~~~~~~~~~~~~

insertable
~~~~~~~~~~

エンティティクラスに ``@Column`` が注釈されたプロパティがある場合、
``@Column`` の ``insertable`` 要素が ``false`` のものは挿入対象外です。

exclude
~~~~~~~

``@Insert`` の ``exclude`` 要素に指定されたプロパティを挿入対象外とします。
プロパティがこの要素に指定されていれば、 ``@Column`` の ``insertable`` 要素が
``true`` であっても挿入対象外です。

.. code-block:: java

  @Insert(exclude = {"name", "salary"})
  int insert(Employee employee);

include
~~~~~~~

``@Insert`` の ``include`` 要素に指定されたプロパティのみを挿入対象とします。
``@Insert`` の ``include`` 要素と ``exclude`` 要素の両方に同じプロパティが指定された場合、
そのプロパティは挿入対象外になります。

プロパティがこの要素に指定されていても、 ``@Column`` の ``insertable`` 要素が
``false`` であれば挿入対象外です。

.. code-block:: java

  @Insert(include = {"name", "salary"})
  int insert(Employee employee);

excludeNull
~~~~~~~~~~~

``@Insert`` の ``excludeNull`` 要素が ``true`` の場合、
値が ``null`` のプロパティを挿入対象外とします。
この要素が ``true`` の場合、 ``@Column`` の ``insertable`` 要素が ``true`` であったり、
``@Insert`` の ``include`` 要素にプロパティが指定されていても、値が ``null`` であれば挿入対象外です。

.. code-block:: java

  @Insert(excludeNull = true)
  int insert(Employee employee);

SQLファイルによる挿入
=====================

SQLファイルによる挿入を行うには、 ``@Insert`` の ``sqlFile`` 要素に ``true`` を設定し、
メソッドに対応するSQLファイルを用意します。

パラメータには任意の型が使用できます。
指定できるパラメータの数に制限はありません。
パラメータの型が基本型もしくはドメインクラスの場合、引数を ``null`` にできます。
それ以外の型の場合、引数は ``null`` であってはいけません。

.. code-block:: java

  @Insert(sqlFile = true)
  int insert(Employee employee);

  @Insert(sqlFile = true)
  Result<ImmutableEmployee> insert(ImmutableEmployee employee);

たとえば、上記のメソッドに対応するSQLは次のように記述します。

.. code-block:: sql

  insert into employee (id, name, salary, version) 
  values (/* employee.id */0, 
          /* employee.name */'hoge', 
          /* employee.salary */100, 
          /* employee.version */0)

SQLファイルによる挿入では、識別子の自動設定やバージョン番号の自動設定は行われません。
また、 ``@Insert`` の ``exclude`` 要素、 ``include`` 要素、 ``excludeNull`` 要素は参照されません。

一意制約違反
============

一意制約違反が発生した場合は、SQLファイルの使用の有無に関係なく
``UniqueConstraintException`` がスローされます。

クエリタイムアウト
==================

``@Insert`` の ``queryTimeout`` 要素にクエリタイムアウトの秒数を指定できます。

.. code-block:: java

  @Insert(queryTimeout = 10)
  int insert(Employee employee);

この指定は、SQLファイルの使用の有無に関係なく適用されます。
``queryTimeout`` 要素に値を指定しない場合、
:doc:`../config` に指定されたクエリタイムアウトが使用されます。

SQL のログ出力形式
==================

``@Insert`` の ``sqlLog`` 要素に SQL のログ出力形式を指定できます。

.. code-block:: java

  @Insert(sqlLog = SqlLogType.RAW)
  int insert(Employee employee);

``SqlLogType.RAW`` はバインドパラメータ（?）付きの SQL をログ出力することを表します。
