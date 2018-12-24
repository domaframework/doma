============================
ストアドプロシージャー
============================

.. contents:: 目次
   :depth: 3

ストアドプロシージャーを呼び出すには、 ``@Procedure`` をDaoのメソッドに注釈します。

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @Procedure
      void execute(@In Integer id, @InOut Reference<BigDecimal> salary);
      ...
  }

パラメータには、パラメータの種別を示す ``@In`` 、 ``@InOut`` 、 ``@Out`` 、
``@ResultSet`` のいずれかのアノテーションが必須です。
パラメータは複数指定できます。

戻り値
======

戻り値は ``void`` でなければいけません。

プロシージャー名
================

デフォルトではメソッド名がプロシージャー名になります。
``@Procedure`` の ``name`` 要素に値を指定した場合は、その値がプロシージャー名になります。

.. code-block:: java

  @Procedure(name = "calculateSalary")
  void execute(@In Integer id, @InOut Reference<BigDecimal> salary);

``@Procedure`` の ``catalog`` 要素や ``schema`` 要素にカタログ名やスキーマ名を指定できます。
このときプロシージャーの名前は ``catalog`` 要素、 ``schema`` 要素、
``name`` 要素（指定されていなければメソッド名）をピリオドで連結したものになります。

.. code-block:: java

  @Procedure(catlog = "CATALOG", schema ="SCHEMA", name = "calculateSalary")
  void execute(@In Integer id, @InOut Reference<BigDecimal> salary);

パラメータ
==========

ストアドプロシージャーのパラメータとDaoメソッドのパラメータの並び順は合わせなければいけません。

INパラメータ
------------

INパラメータは、 ``@In`` をメソッドのパラメータに注釈して示します。
指定可能なパラメータの型は以下の通りです。

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../basic` または :doc:`../domain` を要素とするjava.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

パラメータの型が基本型もしくはドメインクラスの場合、引数を ``null`` にできます。
それ以外の型の場合、引数は ``null`` であってはいけません。

.. code-block:: java

  @Procedure
  void execute(@In Integer id);

次のように使用します。

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  dao.execute(1);

INOUTパラメータ
---------------

INOUTパラメータは、 ``@InOut`` をメソッドのパラメータに注釈して示します。
注釈されるパラメータの型は ``org.seasar.doma.jdbc.Reference`` でなければいけません。
``Reference`` の型パラメータに指定できる型は以下の通りです。

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../basic` または :doc:`../domain` を要素とするjava.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

引数は ``null`` であってはいけません。

.. code-block:: java

  @Procedure
  void execute(@InOut Reference<BigDecimal> salary);

次のように使用します。

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  BigDecimal in = new BigDecimal(100);
  Reference<BigDecimal> ref = new Reference<BigDecimal>(in);
  dao.execute(ref);
  BigDecimal out = ref.get();

OUTパラメータ
-------------

OUTパラメータは、 ``@Out`` をメソッドのパラメータに注釈して示します。
注釈されるパラメータの型は ``org.seasar.doma.jdbc.Reference`` でなければいけません。
``Reference`` の型パラメータに指定できる型は以下の通りです。

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../basic` または :doc:`../domain` を要素とするjava.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

引数は ``null`` であってはいけません。

.. code-block:: java

  @Procedure
  void execute(@Out Reference<BigDecimal> salary);

次のように使用します。

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  Reference<BigDecimal> ref = new Reference<BigDecimal>();
  dao.execute(ref);
  BigDecimal out = ref.get();

カーソルのOUTパラメータもしくは結果セット
-----------------------------------------

カーソルのOUTパラメータ、もしくはストアドプロシージャーが返す結果セットは、
``@ResultSet`` をメソッドのパラメータに注釈して示します。
注釈されるパラメータの型は、以下の型を要素とする ``java.util.List`` でなければいけません。

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* :doc:`../basic` または :doc:`../domain` を要素とするjava.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

引数は ``null`` であってはいけません。

.. code-block:: java

  @Procedure
  void execute(@ResultSet List<Employee> employees);

次のように使用します。

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  List<Employee> employees = new ArrayList<Employee>();
  dao.execute(employees);
  for (Employee e : employees) {
      ...
  }

``@ResultSet`` が注釈された ``java.util.List`` の型パラメータが
:doc:`../entity` であり、かつ、エンティティのプロパティすべてに対して
漏れなく結果セットのカラムをマッピングすることを保証したい場合は、
``@ResultSet`` の ``ensureResultMapping`` 要素に ``true`` を指定します。

.. code-block:: java

  @Procedure
  void execute(@ResultSet(ensureResultMapping = true) List<Employee> employee);

結果セットのカラムにマッピングされないプロパティが存在する場合
``ResultMappingException`` がスローされます。

マップのキーのネーミング規約
============================

結果セットを ``java.util.Map<String, Object>`` にマッピングする場合、
``@Procedure`` の ``mapKeyNaming`` 要素にマップのキーのネーミング規約を指定できます。

.. code-block:: java

  @Procedure(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  void execute(@ResultSet List<Map<String, Object>> employees);

``MapKeyNamingType.CAMEL_CASE`` は、カラム名をキャメルケースに変換することを示します。
そのほかに、カラム名をを大文字や小文字に変換する規約があります。

最終的な変換結果は、ここに指定した値と :doc:`../config` に指定された
``MapKeyNaming`` の実装により決まります。

SQL のログ出力形式
==================

``@Procedure`` の ``sqlLog`` 要素に SQL のログ出力形式を指定できます。

.. code-block:: java

  @Procedure(sqlLog = SqlLogType.RAW)
  void execute(@In Integer id);

``SqlLogType.RAW`` はバインドパラメータ（?）付きの SQL をログ出力することを表します。

