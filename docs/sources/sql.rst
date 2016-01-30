==================
SQL
==================

.. contents:: 目次
   :depth: 3

SQL テンプレート
================

SQL は SQL テンプレートを使用して記述します。

SQL テンプレートの文法は SQL のブロックコメント ``/* */`` をベースにしたもので
あるため1つのテンプレートは次の2つの方法で使用できます。

* Doma でテンプレートの文法を解釈し動的にSQLを組み立てて実行する
* SQL のツールでテンプレートの文法はコメントアウトされたものとして
  静的な SQL を実行する

この特徴は **2-way SQL** と呼ばれることがあります。

SQL テンプレートはファイルに記述してDaoのメソッドにマッピングする必要があります。

たとえば、 SQL ファイルには次のような SQL テンプレートを格納します。

.. code-block:: sql

  select * from employee where employee_id = /* employeeId */99

ここでは、ブロックコメントで囲まれた ``employeeId`` がDaoインタフェースのメソッドのパラメータに対応し、
直後の ``99`` はテスト用のデータになります。
テスト用のデータは、 Doma に解釈されて実行される場合には使用されません。
SQL のツールによる静的な実行時にのみ使用されます。

対応するDaoインタフェースのメソッドは次のとおりです。

.. code-block:: java

  Employee selectById(employeeId);

アノテーション
==============

SQLファイルとDaoのメソッドのマッピングは次のアノテーションで示します。

* @Select
* @Insert(sqlFile = true)
* @Update(sqlFile = true)
* @Delete(sqlFile = true)
* @BatchInsert(sqlFile = true)
* @BatchUpdate(sqlFile = true)
* @BaatchDelete(sqlFile = true)

SQLファイル
===========

エンコーディング
----------------

SQLファイルのエンコーディングはUTF-8でなければいけません。

配置場所
--------

SQLファイルはクラスパスが通った META-INF ディレクトリ以下に配置しなければいけません。

ファイル名の形式
----------------

ファイル名は、次の形式でなければいけません。

::

 META-INF/Daoのクラスの完全修飾名をディレクトリに変換したもの/Daoのメソッド名.sql

例えば、 Daoのクラスが ``aaa.bbb.EmployeeDao`` でマッピングしたいメソッドが
``selectById`` の場合、パス名は次のようになります。

::

  META-INF/aaa/bbb/EmployeeDao/selectById.sql

複数の RDBMS を使用する環境下で特定の RDBMS では別の SQL ファイルを使いたい場合、
拡張子 ``.sql`` の前にハイフン区切りで RDBMS 名を入れることで、
優先的に使用するファイルを指示できます。
たとえば、PostgreSQL専用のSQLファイルは次の名前にします。

::

  META-INF/aaa/bbb/EmployeeDao/selectById-postgres.sql

この例ではPostgreSQLを使用している場合に限り、 ``META-INF/aaa/bbb/EmployeeDao/selectById.sql``
よりも ``META-INF/aaa/bbb/EmployeeDao/selectById-postgres.sql`` が優先的に使用されます。

RDBMS 名は、 ``Dialect`` の ``getName`` メソッドの値が使用されます。
あらかじめ用意された ``Dialect`` についてそれぞれの RDBMS 名を以下の表に示します。

+----------------------------+------------------+----------+
| データベース               | Dialect          | RDBMS 名 |
+============================+==================+==========+
| DB2                        | Db2Dialect       | db2      |
+----------------------------+------------------+----------+
| H2 Database Engine 1.2.126 | H212126Dialect   | h2       |
+----------------------------+------------------+----------+
| H2 Database                | H2Dialect        | h2       |
+----------------------------+------------------+----------+
| HSQLDB                     | HsqldbDialect    | hsqldb   |
+----------------------------+------------------+----------+
| Microsoft SQL Server 2008  | Mssql2008Dialect | mssql    |
+----------------------------+------------------+----------+
| Microsoft SQL Server       | MssqlDialect     | mssql    |
+----------------------------+------------------+----------+
| MySQL                      | MySqlDialect     | mysql    |
+----------------------------+------------------+----------+
| Oracle Database            | OracleDialect    | oracle   |
+----------------------------+------------------+----------+
| PostgreSQL                 | PostgresDialect  | postgres |
+----------------------------+------------------+----------+
| SQLite                     | SqliteDialect    | sqlite   |
+----------------------------+------------------+----------+

SQL コメント
============

SQL コメント中に式を記述することで値のバインディングや条件分岐を行います。
Doma に解釈されるSQLコメントを *式コメント* と呼びます。

式コメントには以下のものがあります。

* `バインド変数コメント`_
* `埋め込み変数コメント`_
* `条件コメント`_
* `繰り返しコメント`_
* `選択カラムリスト展開コメント`_
* `更新カラムリスト生成コメント`_

.. note::

  式コメントに記述できる式の文法については :doc:`expression` を参照してください。

バインド変数コメント
--------------------

バインド変数を示す式コメントを *バインド変数* コメントと呼びます。
バインド変数は、 ``java.sql.PreparedStatement`` を介してSQLに設定されます。

バインド変数は ``/*～*/`` というブロックコメントで囲んで示します。
バインド変数の名前はDaoメソッドのパラメータ名に対応します。
対応するパラメータの型は :doc:`basic` もしくは :doc:`domain` でなければいけません。
バインド変数コメントの直後にはテスト用データを指定する必要があります。
ただし、テスト用データは実行時には使用されません。

基本型もしくはドメインクラス型のパラメータ
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Dao インタフェースのメソッドのパラメータが :doc:`basic` もしくは :doc:`domain` の場合、
このパラメータは1つのバインド変数を表現できます。
バインド変数コメントはバインド変数を埋め込みたい場所に記述し、
バインド変数コメントの直後にはテスト用データを指定しなければいけません。
Dao インタフェースのメソッドと対応する SQL の例は次のとおりです。

.. code-block:: java

   Employee selectById(Integer employeeId);

.. code-block:: sql

   select * from employee where employee_id = /* employeeId */99

Iterable型のパラメータ
~~~~~~~~~~~~~~~~~~~~~~

Dao インタフェースのメソッドのパラメータが ``java.lang.Iterable`` のサブタイプの場合、
このパラメータは、 IN句内の複数のバインド変数を表現できます。
ただし、 ``java.lang.Iterable`` のサブタイプの実型引数は :doc:`basic` もしくは :doc:`domain` でなければいけません。
バインド変数コメントはINキーワードの直後に置き、
バインド変数コメントの直後には括弧つきでテスト用データを指定しなければいけません。
Dao インタフェースのメソッドと対応する SQL の例は次のとおりです。

.. code-block:: java

  List<Employee> selectByIdList(List<Integer> employeeIdList);

.. code-block:: sql

  select * from employee where employee_id in /* employeeIdList */(1,2,3)

``Iterable`` が空であるとき、IN句の括弧内の値は ``null`` になります。

.. code-block:: sql

  select * from employee where employee_id in (null)

任意の型のパラメータ
~~~~~~~~~~~~~~~~~~~~

Dao インタフェースのメソッドのパラメータが :doc:`basic` もしくは :doc:`domain` でない場合、
パラメータは複数のバインド変数コメントに対応します。
バインド変数コメントの中では、ドット ``.`` を使用し任意の型のフィールドやメソッドにアクセスできます。
Dao インタフェースのメソッドと対応する SQL の例は次のとおりです。

``EmployeeDto`` クラスには、 ``employeeName`` フィールドや ``salary`` フィールドが存在するものとします。

.. code-block:: java

  List<Employee> selectByNameAndSalary(EmployeeDto dto);

.. code-block:: sql

  select * from employee
  where
  employee_name = /* dto.employeeName */'abc'
  and
  salary = /* dto.salary */1234

フィールドにアクセスする代わりに ``public`` なメソッドを呼び出すことも可能です。

.. code-block:: sql

  select * from employee
  where
  salary = /* dto.getTaxedSalary() */1234

埋め込み変数コメント
--------------------

埋め込み変数を示す式コメントを埋め込み変数コメントと呼びます。
埋め込み変数の値は SQL を組み立てる際に SQL の一部として直接埋め込まれます。

SQL インジェクションを防ぐため、埋め込み変数の値に以下の値を含めることは禁止しています。

* シングルクォテーション
* セミコロン
* 行コメント
* ブロックコメント

埋め込み変数は ``/*#～*/`` というブロックコメントで示します。
埋め込み変数の名前は Dao メソッドのパラメータ名にマッピングされます。
埋め込み変数は ``ORDER BY`` 句など SQL の一部をプログラムで組み立てたい場合に使用できます。
Dao のメソッドと対応する SQL の例は次のとおりです。

.. code-block:: java

  List<Employee> selectAll(BigDecimal salary, String orderyBy);

.. code-block:: sql

  select * from employee where salary > /* salary */100 /*# orderBy */

Dao の呼び出し例は次の通りです。

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  BigDecimal salary = new BigDecimal(1000);
  String orderBy = "order by salary asc, employee_name";
  List<Employee> list = dao.selectAll(salary, orderBy);

発行される SQL は次のようになります。

.. code-block:: sql

  select * from employee where salary > ? order by salary asc, employee_name

条件コメント
------------

ifとend
~~~~~~~

条件分岐を示す式コメントを条件コメントと呼びます。
構文は次のとおりです。

.. code-block:: sql

  /*%if 条件式*/ ～ /*%end*/

条件式は結果が ``boolean`` もしくは ``java.lang.Boolean`` 型と評価される式でなければいけません。
例を示します。

.. code-block:: sql

  select * from employee where
  /*%if employeeId != null */
      employee_id = /* employeeId */99
  /*%end*/

上記の SQL 文は ``employeeId`` が ``null`` でない場合、 次のような準備された文に変換されます。

.. code-block:: sql

  select * from employee where employee_id = ?

この SQL 文は ``employeeId`` が ``null`` の場合に次のような準備された文に変換されます。

.. code-block:: sql

  select * from employee

``if`` の条件が成り立たない場合に ``if`` の外にある WHERE句が出力されないのは、
`条件コメントにおけるWHEREやHAVINGの自動除去`_ 機能が働いているためです。

条件コメントにおけるWHEREやHAVINGの自動除去
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

条件コメントを使用した場合、条件の前にある ``WHERE`` や ``HAVING`` について自動で出力の要/不要を判定します。
たとえば、次のようなSQLで ``employeeId`` が ``null`` の場合、

.. code-block:: sql

  select * from employee where
  /*%if employeeId != null */
      employee_id = /* employeeId */99
  /*%end*/

``/*%if ～*/`` の前の ``where`` は自動で除去され、次のSQLが生成されます。


.. code-block:: sql

  select * from employee

条件コメントにおけるANDやORの自動除去
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

条件コメントを使用した場合、条件の後ろにつづく ``AND`` や ``OR`` について自動で出力の要/不要を判定します。
たとえば、次のようなSQLで ``employeeId`` が ``null`` の場合、

.. code-block:: sql

  select * from employee where
  /*%if employeeId != null */
      employee_id = /* employeeId */99
  /*%end*/
  and employeeName like 's%'

``/*%end*/`` の後ろの ``and`` は自動で除去され、次の SQL が生成されます。

.. code-block:: sql

  select * from employee where employeeName like 's%'

elseifとelse
~~~~~~~~~~~~

``/*%if 条件式*/`` と ``/*%end*/`` の間では、 ``elseif`` や ``else`` を表す次の構文も使用できます。

* /\*%elseif 条件式\*/
* /\*%else\*/

例を示します。

.. code-block:: sql

  select
    *
  from
    employee
  where
  /*%if employeeId != null */
    employee_id = /* employeeId */9999
  /*%elseif department_id != null */
    and
    department_id = /* departmentId */99
  /*%else*/
    and
    department_id is null
  /*%end*/

上の SQL は、 ``employeeId != null``  が成立するとき実際は次の SQL に変換されます。

.. code-block:: sql

  select
    *
  from
    employee
  where
    employee_id = ?

``employeeId == null && department_id != null`` が成立するとき、実際は次の SQL に変換されます。
``department_id`` の直前の ``AND`` は自動で除去されるため出力されません。

.. code-block:: sql

  select
    *
  from
    employee
  where
    department_id = ?

``employeeId == null && department_id == null`` が成立するとき、実際は次の SQL に変換されます。
``department_id`` の直前の ``AND`` は自動で除去されるため出力されません。

.. code-block:: sql

  select
    *
  from
    employee
  where
    department_id is null

ネストした条件コメント
~~~~~~~~~~~~~~~~~~~~~~

条件コメントはネストさせることができます。

.. code-block:: sql

  select * from employee where
  /*%if employeeId != null */
    employee_id = /* employeeId */99
    /*%if employeeName != null */
      and
      employee_name = /* employeeName */'hoge'
    /*%else*/
      and
      employee_name is null
    /*%end*/
  /*%end*/

条件コメントにおける制約
~~~~~~~~~~~~~~~~~~~~~~~~

条件コメントの ``if`` と ``end`` はSQLの同じ節に含まれなければいけません。
節とは、 SELECT節、FROM節、WHERE節、GROUP BY節、HAVING節、ORDER BY節などです。
次の例では、 ``if`` がFROM節にあり ``end`` がWHERE節にあるため不正です。

.. code-block:: sql

  select * from employee /*%if employeeId != null */
  where employee_id = /* employeeId */99 /*%end*/

また、 ``if`` と ``end`` は同じレベルの文に含まれなければいけません。
次の例では、 ``if`` が括弧の外にありendが括弧の内側にあるので不正です。

.. code-block:: sql

  select * from employee
  where employee_id in /*%if departmentId != null */(...  /*%end*/ ...)

繰り返しコメント
----------------

forとend
~~~~~~~~

繰り返しを示す式コメントを繰り返しコメントと呼びます。
構文は次のとおりです。

::

  /*%for 識別子 : 式*/ ～ /*%end*/

識別子は、繰り返される要素を指す変数です。
式は ``java.lang.Iterable`` 型として評価される式でなければいけません。
例を示します。

.. code-block:: sql

  select * from employee where
  /*%for name : names */
  employee_name like /* name */'hoge'
    /*%if name_has_next */
  /*# "or" */
    /*%end */
  /*%end*/

上記の SQL 文は、 ``names`` が3つの要素からなるリストを表す場合、次のような準備された文に変換されます。

.. code-block:: sql

  select * from employee where
  employee_name like ?
  or
  employee_name like ?
  or
  employee_name like ?

item_has_nextとitem_index
~~~~~~~~~~~~~~~~~~~~~~~~~

``/*%for 識別子 : 式*/`` から ``/*%end*/`` までの内側では次の2つの特別な変数を使用できます。

* item_has_next
* item_index

接頭辞の *item* は識別子を表します。つまり、 ``for`` の識別子が ``name`` の場合
この変数はそれぞれ ``name_has_next`` と ``name_index`` となります。

``item_has_next`` は次の繰り返し要素が存在するかどうかを示す ``boolean`` の値です。

``item_index`` は繰り返しのindexを表す ``int`` の値です。値は0始まりです。

繰り返しコメントにおけるWHEREやHAVINGの自動除去
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

繰り返しコメントを使用した場合、コメントの前にある
``WHERE`` や ``HAVING`` について自動で出力の要/不要を判定します。
たとえば、次のような SQL で ``names`` の ``size`` が ``0`` の場合（繰り返しが行われない場合）、

.. code-block:: sql

  select * from employee where
  /*%for name : names */
  employee_name like /* name */'hoge'
    /*%if name_has_next */
  /*# "or" */
    /*%end */
  /*%end*/

``/*%for ～*/`` の前の ``where`` は自動で除去され、次の SQL が生成されます。

.. code-block:: sql

  select * from employee

繰り返しコメントにおけるANDやORの自動除去
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

繰り返しコメントを使用した場合、コメントの後ろにつづく
``AND`` や ``OR`` について自動で出力の要/不要を判定します。
たとえば、次のような SQL で ``names`` の ``size`` が ``0`` の場合（繰り返しが行われない場合）、

.. code-block:: sql

  select * from employee where
  /*%for name : names */
  employee_name like /* name */'hoge'
    /*%if name_has_next */
  /*# "or" */
    /*%end */
  /*%end*/
  or
  salary > 1000

``/*%end*/`` の後ろの ``or`` は自動で除去され、次のSQLが生成されます。

.. code-block:: sql

  select * from employee where salary > 1000

繰り返しコメントにおける制約
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

繰り返しコメントの ``for`` と ``end`` は SQL の同じ節に含まれなければいけません。
節とは、SELECT節、FROM節、WHERE節、GROUP BY節、HAVING節、ORDER BY節などです。

また、 ``for`` と ``end`` は同じレベルの文に含まれなければいけません。
つまり、括弧の外で ``for`` 、括弧の内側で ``end`` という記述は認められません。

選択カラムリスト展開コメント
----------------------------

expand
~~~~~~

SELECT節のアスタリスク ``*`` を :doc:`entity` の定義を
参照して自動でカラムのリストに展開する式を選択カラムリスト展開コメントと呼びます。
構文は次のとおりです。

::

  /*%expand エイリアス*/

エイリアスは文字列として評価される式でなければいけません。
エイリアスは省略可能です。

このコメントの直後にはアスタリスク ``*`` が必須です。

例を示します。

.. code-block:: sql

  select /*%expand*/* from employee

上記のSQL文の結果が次のような :doc:`entity` にマッピングされているものとします。

.. code-block:: java

   @Entity
   public class Employee {
       Integer id;
       String name;
       Integer age;
   }

このとき、 SQL は以下のように変換されます。

.. code-block:: sql

  select id, name, age from employee

SQL 上でテーブルにエイリアスを指定する場合、
選択カラムリスト展開コメントにも同じエイリアスを指定してください。

.. code-block:: sql

  select /*%expand "e" */* from employee e

このとき、 SQL は以下のように変換されます。

.. code-block:: sql

  select e.id, e.name, e.age from employee e

.. _populate:

更新カラムリスト生成コメント
-----------------------------

populate
~~~~~~~~

UPDATE文のSET節 を :doc:`entity` の定義を
参照して自動で生成する式を更新カラムリスト生成コメントと呼びます。
構文は次のとおりです。

::

  /*%populate*/


例を示します。

.. code-block:: sql

  update employee set /*%populate*/ id = id where age < 30

上記のSQL文への入力が次のような :doc:`entity` にマッピングされているものとします。

.. code-block:: java

   @Entity
   public class Employee {
       Integer id;
       String name;
       Integer age;
   }

このとき、 SQL は以下のように変換されます。

.. code-block:: sql

  update employee set id = ?, name = ?, age = ? where age < 30

更新カラムリスト生成コメントは、 ``/*%populate*/`` からWHERE句までをカラムリストで置き換えます。
つまり、元のSQLにあった ``id = id`` の記述は最終的なSQLからは削除されます。

通常のブロックコメント
----------------------

``/*`` の直後に続く3文字目がJavaの識別子の先頭で使用できない文字
（ただし、空白および式で特別な意味をもつ ``%``、``#``、 ``@``、 ``"``、 ``'`` は除く）の場合、
それは通常のブロックコメントだとみなされます。

たとえば、次の例はすべて通常のブロックコメントです。

.. code-block:: sql

  /**～*/
  /*+～*/
  /*=～*/
  /*:～*/
  /*;～*/
  /*(～*/
  /*)～*/
  /*&～*/

一方、次の例はすべて式コメントだとみなされます。

.. code-block:: sql

  /* ～*/ ...--3文字目が空白であるため式コメントです。
  /*a～*/ ...--3文字目がJavaの識別子の先頭で使用可能な文字であるため式コメントです。
  /*$～*/ ...--3文字目がJavaの識別子の先頭で使用可能な文字であるため式コメントです。
  /*%～*/ ...--3文字目が条件コメントや繰り返しコメントの始まりを表す「%」であるため式コメントです。
  /*#～*/ ...--3文字目が埋め込み変数コメントを表す「#」であるため式コメントです。
  /*@～*/ ...--3文字目が組み込み関数もしくはクラス名を表す「@」であるため式コメントです。
  /*"～*/ ...--3文字目が文字列リテラルの引用符を表す「"」であるため式コメントです。
  /*'～*/ ...--3文字目が文字リテラルの引用符を表す「'」であるため式コメントです。

.. note::

  特に理由がない場合、通常のブロックコメントには
  最初のアスタリスクを2つ重ねる ``/**～*/`` を使用するのがよいでしょう。

通常の行コメント
----------------

``--`` は通常の行コメントだとみなされます。

Domaでは行コメントを特別に解釈することはありません。
