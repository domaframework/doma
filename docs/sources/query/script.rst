==================
スクリプト
==================

.. contents:: 目次
   :depth: 3

SQLスクリプトの実行を行うには、 ``@Script`` をDaoのメソッドに注釈します。

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @Script
      void createTable();
      ...
  }

メソッドの戻り値の型は ``void`` でなければいけません。パラメータの数は0でなければいけません。

また、メソッドに対応するスクリプトファイルが必須です。

スクリプトファイル
==================

スクリプトファイルでは、
``Dialect`` が提供するRDBMS名や区切り文字が使用されます。

+----------------------------+------------------+----------+------------+
| データベース               | Dialectの名前    | RDBMS名  | 区切り文字 |
+============================+==================+==========+============+
| DB2                        | Db2Dialect       | db2      | @          |
+----------------------------+------------------+----------+------------+
| H2 Database Engine 1.2.126 | H212126Dialect   | h2       |            |
+----------------------------+------------------+----------+------------+
| H2 Database                | H2Dialect        | h2       |            |
+----------------------------+------------------+----------+------------+
| HSQLDB                     | HsqldbDialect    | hsqldb   |            |
+----------------------------+------------------+----------+------------+
| Microsoft SQL Server 2008  | Mssql2008Dialect | mssql    | GO         |
+----------------------------+------------------+----------+------------+
| Microsoft SQL Server       | MssqlDialect     | mssql    | GO         |
+----------------------------+------------------+----------+------------+
| MySQL                      | MySqlDialect     | mysql    | /          |
+----------------------------+------------------+----------+------------+
| Oracle Database            | OracleDialect    | oracle   | /          |
+----------------------------+------------------+----------+------------+
| PostgreSQL                 | PostgresDialect  | postgres | $$         |
+----------------------------+------------------+----------+------------+
| SQLite                     | SqliteDialect    | sqlite   |            |
+----------------------------+------------------+----------+------------+

配置場所
--------

スクリプトファイルはクラスパスが通った META-INF ディレクトリ以下に配置しなければいけません。

ファイル名の形式
----------------

ファイル名は、次の形式でなければいけません。

::

  META-INF/Daoのクラスの完全修飾名をディレクトリに変換したもの/Daoのメソッド名.script

例えば、 Daoのクラスが ``aaa.bbb.EmployeeDao`` でマッピングしたいメソッドが
``createTable`` の場合、パス名は次のようになります。

::

  META-INF/aaa/bbb/EmployeeDao/createTable.script

複数のRDBMSに対応する必要があり特定のRDBMSでは別のスクリプトファイルを使いたい場合、
.script の前にハイフン区切りでRDBMS名を入れることで、
優先的に使用するファイルを指示できます。
たとえば、PostgreSQL専用のSQLファイルは次の名前にします。

::

  META-INF/aaa/bbb/EmployeeDao/createTables-postgres.script

この場合、PostgreSQLを使用している場合に限り、
``META-INF/aaa/bbb/EmployeeDao/createTable.script`` よりも
``META-INF/aaa/bbb/EmployeeDao/createTable-postgres.script`` が優先的に使用されます。

エンコーディング
----------------

スクリプトファイルのエンコーディングはUTF-8でなければいけません。

区切り文字
----------

スクリプトファイルの区切り文字には、
ステートメントの区切り文字とブロックの区切り文字の2種類があります。

ステートメントの区切り文字はセミコロン ``;`` です。

ブロックの区切り文字は、 ``Dialect`` が提供する値が使用されます。

ブロックの区切り文字は、アノテーションの ``blockDelimiter``
要素で明示することもできます。
アノテーションで指定した場合、 ``Dialect`` の値よりも優先されます。

.. code-block:: java

  @Script(blockDelimiter = "GO")
  void createTable();

エラー発生時の継続実行
----------------------

デフォルトでは、スクリプト中のどれかのSQLの実行が失敗すれば、
処理はそこで止まります。
しかし、アノテーションの ``haltOnError`` 要素に ``false``
を指定することで、エラー発生時に処理を継続させることができます。

.. code-block:: java

  @Script(haltOnError = false)
  void createTable();

記述例
======

スクリプトファイルは次のように記述できます。
この例は、Oracle Databaseに有効なスクリプトです。

.. code-block:: sql

  /*
   * テーブル定義（SQLステートメント）
   */
  create table EMPLOYEE (
    ID numeric(5) primary key,  -- 識別子
    NAME varchar2(20)           -- 名前
  );

  /*
   * データの追加（SQLステートメント）
   */
  insert into EMPLOYEE (ID, NAME) values (1, 'SMITH');

  /*
   * プロシージャー定義（SQLブロック）
   */
  create or replace procedure proc
  ( cur out sys_refcursor,
    employeeid in numeric
  ) as
  begin
    open cur for select * from employee where id > employeeid order by id;
  end proc_resultset;
  /

  /*
   * プロシージャー定義2（SQLブロック）
   */
  create or replace procedure proc2
  ( cur out sys_refcursor,
    employeeid in numeric
  ) as
  begin
    open cur for select * from employee where id > employeeid order by id;
  end proc_resultset;
  /

コメントは1行コメント ``--`` とブロックコメント ``/* */`` の2種類が使用できます。
コメントは取り除かれてデータベースへ発行されます。

1つのSQLステートメントは複数行に分けて記述できます。
ステートメントはセミコロン ``;`` で区切らなければいけません。
改行はステートメントの区切りとはみなされません。

ストアドプロシージャーなどのブロックの区切りは、 ``Dialect`` のデフォルトの値か、
``@Script`` の ``blockDelimiter`` 要素に指定した値を使用して示せます。
この例では、 ``OracleDialect`` のデフォルトの区切り文字であるスラッシュ
``/`` を使用しています。
ブロックの 区切り文字は行頭に記述し、
区切り文字の後ろには何も記述しないようにしてください。
つまり、区切り文字だけの行としなければいけません。

SQL のログ出力形式
==================

``@Script`` の ``sqlLog`` 要素に SQL のログ出力形式を指定できます。

.. code-block:: java

  @Script(sqlLog = SqlLogType.RAW)
  void createTable();

``SqlLogType.RAW`` はバインドパラメータ（?）付きの SQL をログ出力することを表します。

