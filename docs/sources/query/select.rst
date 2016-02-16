===============
検索
===============

.. contents:: 目次
   :depth: 3

検索を行うには、 ``@Select`` をDaoのメソッドに注釈します。

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @Select
      List<Employee> selectByDepartmentName(String departmentName);
      ...
  }


検索では、 **SQLファイルが必須** です。
検索系のSQLを自動生成する機能はありません。

.. note::

  エンティティクラスを利用する場合、エンティティクラスは **検索結果に応じて** 作成する必要があります。
  たとえば、EMPLOYEEテーブルに対応するEmployeeエンティティクラスが定義されている場合、
  EMPLOYEEテーブルのカラムを含む結果セットはEmployeeエンティティクラスで受けられますが、
  EMPLOYEEテーブルとDEPARTMENTテーブルを結合して得られる結果セットに対しては、
  Employeeエンティティクラスとは別のクラス（たとえばEmployeeDepartmentクラス）が必要です。

問い合わせ条件
==============

問い合わせ条件にはメソッドのパラメータを使用します。
利用できるパラメータの型は以下のものです。

* :doc:`../basic`
* :doc:`../domain`
* 任意の型
* :doc:`../basic` や :doc:`../domain` や任意の型を要素とするjava.util.Optional
* :doc:`../basic` や :doc:`../domain` を要素とするjava.util.Iterable
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

パラメータの数に制限はありません。
パラメータの型が :doc:`../basic` もしくは :doc:`../domain` の場合、引数を ``null`` にできます。
それ以外の型の場合、引数は ``null`` であってはいけません。

基本型やドメインクラスを使った問い合わせ
----------------------------------------

メソッドやパラメータに :doc:`../basic` や :doc:`../domain` を定義します。

.. code-block:: java

  @Select
  List<Employee> selectByNameAndSalary(String name, Salary salary);

SQLファイルではSQLコメントを使いメソッドのパラメータをSQLにマッピングさせます。
SQLコメントではメソッドのパラメータ名を参照します。

.. code-block:: sql

  select * from employee where employee_name = /* name */'hoge' and salary > /* salary */100

任意の型を使った問い合わせ
--------------------------

メソッドのパラメータに任意の型を使用する場合は、ドット ``.``
でフィールドにアクセスしたりメソッドを呼び出すなどしてSQLにマッピングさせます。

.. code-block:: java

  @Select
  List<Employee> selectByExample(Employee employee);

.. code-block:: sql

  select * from employee where employee_name = /* employee.name */'hoge' and salary > /* employee.getSalary() */100

パラメータは複数指定できます。

.. code-block:: java

  @Select
  List<Employee> selectByEmployeeAndDepartment(Employee employee, Department department);

Iterableを使ったIN句へのマッピング
----------------------------------

``java.lang.Iterable`` のサブタイプは、 IN句を利用した検索を行う場合に使用します。

.. code-block:: java

  @Select
  List<Employee> selectByNames(List<String> names);

.. code-block:: sql

  select * from employee where employee_name in /* names */('aaa','bbb','ccc')

1件検索
========

1件を検索するには、メソッドの戻り値の型を次のいずれかにします。

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* :doc:`../basic` 、 :doc:`../domain` 、 :doc:`../entity` 、 java.util.Map<String, Object>
  のいずれかを要素とするjava.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

.. code-block:: java

  @Select
  Employee selectByNameAndSalary(String name, BigDecimal salary);

戻り値の型が ``Optional`` でなく、かつ、結果が0件のときは ``null`` が返されます。
`検索結果の保証`_ を有効にした場合は、戻り値の型に関係なく結果が0件ならば例外がスローされます。

結果が2件以上存在するときは、 ``NonUniqueResultException`` がスローされます。

複数件検索
==========

複数件を検索するには、メソッドの戻り値の型を ``java.util.List`` にします。
``List`` の要素の型には次のものが使用できます。

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* :doc:`../basic` もしくは :doc:`../domain` のいずれかを要素とするjava.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

.. code-block:: java

  @Select
  List<Employee> selectByNameAndSalary(String name, Salary salary);

結果が0件のときは ``null`` ではなく空のListが返されます。
ただし、 `検索結果の保証`_ を有効にした場合、結果が0件ならば例外がスローされます。

ストリーム検索
==============

全件を一度に ``java.util.List`` で受け取るのではなく ``java.util.stream.Stream`` で扱いたい場合は、ストリーム検索を利用できます。

ストリーム検索には、 ``Stream`` を ``java.util.Function`` へ渡す方法と戻り値で返す方法の2種類があります。

Functionへ渡す方法
---------------------------

``@Select`` の ``strategy`` 要素に ``SelectType.STREAM`` を設定し、
メソッドのパラメータに ``java.util.Function<Stream<TARGET>, RESULT>`` もしくは
``java.util.Function<Stream<TARGET>, RESULT>`` のサブタイプを定義します。

.. code-block:: java

  @Select(strategy = SelectType.STREAM)
  BigDecimal selectByNameAndSalary(String name, BigDecimal salary, Function<Stream<Employee>, BigDecimal> mapper);

呼び出し元はストリームを受け取って結果を返すラムダ式を渡します。

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  BigDecimal result = dao.selectByNameAndSalary(name, salary, stream -> {
      return ...;
  });

``Function<Stream<TARGET>, RESULT>`` の型パラメータ ``TARGET`` は次のいずれかでなければいけません。

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* :doc:`../basic` もしくは :doc:`../domain` のいずれかを要素とするjava.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

型パラメータ ``RESULT`` はDaoのメソッドの戻り値に合わせなければいけません。

`検索結果の保証`_ を有効にした場合、結果が0件ならば例外がスローされます。

戻り値で返す方法
---------------------------

メソッドの戻り値の型を ``java.util.stream.Stream`` にします。
``Stream`` の要素の型には次のものが使用できます。

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* :doc:`../basic` もしくは :doc:`../domain` のいずれかを要素とするjava.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

.. code-block:: java

  @Select
  Stream<Employee> selectByNameAndSalary(String name, BigDecimal salary);

呼び出し元です。

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  try (Stream<Employee> stream = dao.selectByNameAndSalary(name, salary)) {
    ...
  }

`検索結果の保証`_ を有効にした場合、結果が0件ならば例外がスローされます。

.. warning::

  リソースの解放漏れを防ぐためにストリームは必ずクローズしてください。
  ストリームをクローズしないと、 ``java.sql.ResultSet`` 、
  ``java.sql.PreparedStatement`` 、 ``java.sql.Connection`` のクローズが行われません。

.. note::

  戻り値で返す方法はリソース解放漏れのリスクがあるため、特に理由がない限りは、
  Functionへ渡す方法の採用を検討してください。
  注意を促すためにDaoのメソッドに対して警告メッセージを表示します。
  警告を抑制するには以下のように ``@Suppress`` を指定してください。

  .. code-block:: java

    @Select
    @Suppress(messages = { Message.DOMA4274 })
    Stream<Employee> selectByNameAndSalary(String name, BigDecimal salary);

コレクト検索
============

検索結果を ``java.util.Collector`` で処理したい場合は、コレクト検索を利用できます。

コレクト検索を実施するには、 ``@Select`` の ``strategy`` 要素に ``SelectType.COLLECT`` を設定し、
メソッドのパラメータに ``java.stream.Collector<TARGET, ACCUMULATION, RESULT>`` もしくは
``java.stream.Collector<TARGET, ?, RESULT>`` のサブタイプを定義します。

.. code-block:: java

  @Select(strategy = SelectType.COLLECT)
  <RESULT> RESULT selectBySalary(BigDecimal salary, Collector<Employee, ?, RESULT> collector);

呼び出し元は ``Collector`` のインスタンスを渡します。

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  Map<Integer, List<Employee>> result =
      dao.selectBySalary(salary, Collectors.groupingBy(Employee::getDepartmentId));

``Collector<TARGET, ACCUMULATION, RESULT>`` の型パラメータ ``TARGET`` は次のいずれかでなければいけません。

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* :doc:`../basic` もしくは :doc:`../domain` のいずれかを要素とするjava.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

型パラメータ ``RESULT`` はDaoのメソッドの戻り値に合わせなければいけません。

`検索結果の保証`_ を有効にした場合、結果が0件ならば例外がスローされます。

.. note::

  コレクト検索はストリーム検索のFunctionに渡す方法のショートカットです。
  ストリーム検索で得られる ``Stream`` オブジェクトの ``collect`` メソッドを使って同等のことができます。

検索オプションを利用した検索
============================

検索オプションを表す ``SelectOptions`` を使用することで、SELECT文が記述されたSQLファイルをベースにし、
ページング処理や悲観的排他制御用のSQLを自動で生成できます。

``SelectOptions`` は、 `1件検索`_ 、 `複数件検索`_ 、 `ストリーム検索`_
と組み合わせて使用します。

``SelectOptions`` は、Daoのメソッドのパラメータとして定義します。

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @Select
      List<Employee> selectByDepartmentName(String departmentName, SelectOptions options);
      ...
  }

``SelectOptions`` のインスタンスは、staticな ``get`` メソッドにより取得できます。

.. code-block:: java

  SelectOptions options = SelectOptions.get();

ページング
----------

``SelectOptions`` の ``offset`` メソッドで開始位置、 ``limit`` メソッドで取得件数を指定し、
``SelectOptions`` のインスタンスをDaoのメソッドに渡します。

.. code-block:: java

  SelectOptions options = SelectOptions.get().offset(5).limit(10);
  EmployeeDao dao = new EmployeeDaoImpl();
  List<Employee> list = dao.selectByDepartmentName("ACCOUNT", options);

ページングは、ファイルに記述されているオリジナルのSQLを書き換え実行することで実現されています。
オリジナルのSQLは次の条件を満たしていなければいけません。

* SELECT文である
* 最上位のレベルでUNION、EXCEPT、INTERSECT等の集合演算を行っていない（サブクエリで利用している場合は可）
* ページング処理を含んでいない

さらに、データベースの方言によっては特定の条件を満たしていなければいけません。


+------------------+---------------------------------------------------------------+
| Dialect          |    条件                                                       |
+==================+===============================================================+
| Db2Dialect       |    offsetを指定する場合、ORDER BY句を持ちORDER BY句で指定する |
|                  |    カラムすべてをSELECT句に含んでいる                         |
+------------------+---------------------------------------------------------------+
| Mssql2008Dialect |    offsetを指定する場合、ORDER BY句を持ちORDER BY句で指定する |
|                  |    カラムすべてをSELECT句に含んでいる                         |
+------------------+---------------------------------------------------------------+
| MssqlDialect     |    offsetを指定する場合、ORDER BY句を持つ必要があります       |
+------------------+---------------------------------------------------------------+
| StandardDialect  |    ORDER BY句を持ちORDER BY句で指定する                       |
|                  |    カラムすべてをSELECT句に含んでいる                         |
+------------------+---------------------------------------------------------------+

悲観的排他制御
--------------

``SelectOptions`` の ``forUpdate`` メソッドで悲観的排他制御を行うことを示し、
SelectOptionsのインスタンスをDaoのメソッドに渡します。

.. code-block:: java

  SelectOptions options = SelectOptions.get().forUpdate();
  EmployeeDao dao = new EmployeeDaoImpl();
  List<Employee> list = dao.selectByDepartmentName("ACCOUNT", options);

``SelectOptions`` には、ロック対象のテーブルやカラムのエイリアスを指定できる ``forUpdate`` メソッドや、
ロックの取得を待機しない ``forUpdateNowait`` など、名前が *forUpdate*
で始まる悲観的排他制御用のメソッドが用意されています。

悲観的排他制御は、ファイルに記述されているオリジナルのSQLを書き換えて実行しています。
オリジナルのSQLは次の条件を満たしていなければいけません。

* SELECT文である
* 最上位のレベルでUNION、EXCEPT、INTERSECT等の集合演算を行っていない（サブクエリで利用している場合は可）
* 悲観的排他制御の処理を含んでいない

データベースの方言によっては、悲観的排他制御用のメソッドのすべてもしくは一部が使用できません。

+------------------+-----------------------------------------------------------------------------+
| Dialect          |    説明                                                                     |
+==================+=============================================================================+
| Db2Dialect       |    forUpdate()を使用できる                                                  |
+------------------+-----------------------------------------------------------------------------+
| H2Dialect        |    forUpdate()を使用できる                                                  |
+------------------+-----------------------------------------------------------------------------+
| HsqldbDialect    |    forUpdate()を使用できる                                                  |
+------------------+-----------------------------------------------------------------------------+
| Mssql2008Dialect |    forUpdate()とforUpdateNoWait()を使用できる。                             |
|                  |    ただし、オリジナルのSQLのFROM句は1つのテーブルだけから成らねばならない。 |
+------------------+-----------------------------------------------------------------------------+
| MysqlDialect     |    forUpdate()を使用できる                                                  |
+------------------+-----------------------------------------------------------------------------+
| OracleDialect    |    forUpdate()、forUpdate(String... aliases)、                              |
|                  |    forUpdateNowait()、forUpdateNowait(String... aliases)、                  |
|                  |    forUpdateWait(int waitSeconds)、                                         |
|                  |    forUpdateWait(int waitSeconds, String... aliases)を使用できる            |
+------------------+-----------------------------------------------------------------------------+
| PostgresDialect  |    forUpdate()とforUpdate(String... aliases)を使用できる                    |
+------------------+-----------------------------------------------------------------------------+
| StandardDialect  |    悲観的排他制御用のメソッドすべてを使用できない                           |
+------------------+-----------------------------------------------------------------------------+

集計
----

``SelectOptions`` の ``count`` メソッドを呼び出すことで集計件数を取得できるようになります。
通常、ページングのオプションと組み合わせて使用し、ページングで絞り込まない場合の全件数を取得する場合に使います。

.. code-block:: java

  SelectOptions options = SelectOptions.get().offset(5).limit(10).count();
  EmployeeDao dao = new EmployeeDaoImpl();
  List<Employee> list = dao.selectByDepartmentName("ACCOUNT", options);
  long count = options.getCount();

集計件数は、Daoのメソッド呼出し後に ``SelectOptions`` の ``getCount`` メソッドを使って取得します。
メソッド呼び出しの前に ``count`` メソッドを実行していない場合、 ``getCount`` メソッドは ``-1`` を返します。

検索結果の保証
==============

検索結果が1件以上存在することを保証したい場合は、 ``@Select`` の ``ensureResult`` 要素に ``true`` を指定します。

.. code-block:: java

  @Select(ensureResult = true)
  Employee selectById(Integer id);

検索結果が0件ならば ``NoResultException`` がスローされます。

検索結果のマッピングの保証
==========================

エンティティのプロパティすべてに対して漏れなく結果セットのカラムをマッピングすることを保証したい場合は、
``@Select`` の ``ensureResultMapping`` 要素に ``true`` を指定します。

.. code-block:: java

  @Select(ensureResultMapping = true)
  Employee selectById(Integer id);

結果セットのカラムにマッピングされないプロパティが存在する場合 ``ResultMappingException`` がスローされます。

クエリタイムアウト
==================

``@Select`` の ``queryTimeout`` 要素にクエリタイムアウトの秒数を指定できます。

.. code-block:: java

  @Select(queryTimeout = 10)
  List<Employee> selectAll();

値を指定しない場合、 :doc:`../config` に指定されたクエリタイムアウトが使用されます。

フェッチサイズ
==============

``@Select`` の ``fetchSize`` 要素にフェッチサイズを指定できます。

.. code-block:: java

  @Select(fetchSize = 20)
  List<Employee> selectAll();

値を指定しない場合、 :doc:`../config` に指定されたフェッチサイズが使用されます。

最大行数
========

``@Select`` の ``maxRows`` 要素に最大行数を指定できます。

.. code-block:: java

  @Select(maxRows = 100)
  List<Employee> selectAll();

値を指定しない場合、 :doc:`../config` に指定された最大行数が使用されます。

マップのキーのネーミング規約
============================

検索結果を ``java.util.Map<String, Object>`` にマッピングする場合、
``@Select`` の ``mapKeyNaming`` 要素にマップのキーのネーミング規約を指定できます。

.. code-block:: java

  @Select(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  List<Map<String, Object>> selectAll();

``MapKeyNamingType.CAMEL_CASE`` は、カラム名をキャメルケースに変換することを示します。
そのほかにカラム名を大文字や小文字に変換する規約があります。

最終的な変換結果は、ここに指定した値と :doc:`../config` に指定された
``MapKeyNaming`` の実装により決まります。

SQL のログ出力形式
==================

``@Select`` の ``sqlLog`` 要素に SQL のログ出力形式を指定できます。

.. code-block:: java

  @Select(sqlLog = SqlLogType.RAW)
  List<Employee> selectById(Integer id);

``SqlLogType.RAW`` はバインドパラメータ（?）付きの SQL をログ出力することを表します。
