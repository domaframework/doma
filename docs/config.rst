==================
設定
==================

.. contents:: 目次
   :depth: 3

Domaに対する設定は、 ``Confing`` インタフェースの実装クラスで表現します。

設定項目
=================

設定必須と明示していない項目についてはデフォルトの値が使用されます。

データソース
----------------

``DataSource`` を ``getDataSource`` メソッドで返してください。
ローカルトランザクションを利用する場合は、 ``LocalTransactionDataSource`` を返してください。

.. note::

   この項目は設定必須です。

データソースの名前
------------------

データソース名をあらわす ``String`` を ``getDataSourceName`` メソッドで返してください。
データソース名は、複数のデータソースを利用する環境で重要です。
データソース名はデータソースごとに自動生成される識別子を区別するために使用されます。
複数データソースを利用する場合は、それぞれ異なる名前を返すようにしてください。

デフォルトの実装では、 ``Config`` の実装クラスの完全修飾名が使用されます。

データベースの方言
--------------------------

``Dialect`` を ``getDialect`` メソッドで返してください。
``Dialect`` はRDBMSの方言を表すインタフェースです。
``Dialect`` には次のものがあります。

+----------------------------+------------------+--------------------------------------+
| データベース               | Dialect          | 説明                                 |
+============================+==================+======================================+
| DB2                        | Db2Dialect       |                                      |
+----------------------------+------------------+--------------------------------------+
| H2 Database Engine 1.2.126 | H212126Dialect   | H2 Database Engine 1.2.126で稼動     |
+----------------------------+------------------+--------------------------------------+
| H2 Database                | H2Dialect        | H2 Database Engine 1.3.171以降に対応 |
+----------------------------+------------------+--------------------------------------+
| HSQLDB                     | HsqldbDialect    |                                      |
+----------------------------+------------------+--------------------------------------+
| Microsoft SQL Server 2008  | Mssql2008Dialect | Microsoft SQL Server 2008に対応      |
+----------------------------+------------------+--------------------------------------+
| Microsoft SQL Server       | MssqlDialect     | Microsoft SQL Server 2012以降に対応  |
+----------------------------+------------------+--------------------------------------+
| MySQL                      | MySqlDialect     |                                      |
+----------------------------+------------------+--------------------------------------+
| Oracle Database            | OracleDialect    |                                      |
+----------------------------+------------------+--------------------------------------+
| PostgreSQL                 | PostgresDialect  |                                      |
+----------------------------+------------------+--------------------------------------+
| SQLite                     | SqliteDialect    |                                      |
+----------------------------+------------------+--------------------------------------+

.. note::

   この項目は設定必須です。

ログ出力ライブラリへのアダプタ
------------------------------

``JdbcLogger`` を ``getJdbcLogger`` メソッドで返してください。
``JdbcLogger`` はデータベースアクセスに関するログを扱うインタフェースです。
実装クラスには次のものがあります。

* org.seasar.doma.jdbc.UtilLoggingJdbcLogger

``UtilLoggingJdbcLogger`` は ``java.util.logging`` のロガーを使用する実装で、
デフォルトで使用されます。

SQLファイルのリポジトリ
-----------------------

``SqlFileRepository`` を ``getSqlFileRepository`` メソッドで返してください。
``SqlFileRepository`` は SQL ファイルのリポジトリを扱うインタフェースです。
実装クラスには次のものがあります。

* org.seasar.doma.jdbc.GreedyCacheSqlFileRepository
* org.seasar.doma.jdbc.NoCacheSqlFileRepository

``GreedyCacheSqlFileRepository`` は、読み込んだSQLファイルの内容をパースし、
その結果をメモリが許す限り最大限にキャッシュします。

``NoCacheSqlFileRepository`` は、一切キャッシュを行いません。
毎回、SQLファイルからSQLを読み取りパースします。

メモリの利用に厳しい制限がある環境や、扱うSQLファイルが膨大にある環境では、
適切なキャッシュアルゴリズムをもった実装クラスを作成し使用してください。

デフォルトでは ``GreedyCacheSqlFileRepository`` が使用されます。

REQUIRES_NEW 属性のトランザクションとの連動
-------------------------------------------

``RequiresNewController`` を ``getRequiresNewController`` メソッドで返してください。
``RequiresNewController`` は REQUIRES_NEW の属性をもつトランザクションを
制御するインタフェースです。

このインタフェースは、 ``@TableGenerator`` で、識別子を自動生成する際にしか使われません。
``@TableGenerator`` を利用しない場合は、この設定項目を考慮する必要はありません。
また、 ``@TableGenerator`` を利用する場合であっても、
識別子を採番するための更新ロックが問題にならない程度のトランザクション数であれば、
設定する必要ありません。

デフォルトの実装は何の処理もしません。

クラスのロード方法
------------------

``ClassHelper`` を ``getClassHelper`` メソッドで返してください。
``ClassHelper`` はクラスのロードに関してアプリケーションサーバや
フレームワークの差異を抽象化するインタフェースです。

デフォルトの実装は ``java.lang.Class.forName(name)``  を用いてクラスをロードします。

例外メッセージに含めるSQLの種別
-------------------------------

例外メッセージに含めるSQLのタイプをあらわす ``SqlLogType``
を ``getExceptionSqlLogType`` メソッドで返してください。
この値は、Doma がスローする例外にどのような形式のSQLを含めるかを決定します。

デフォルトの実装では、フォーマットされた SQL を含めます。

未知のカラムのハンドラ
----------------------

``UnknownColumnHandler`` を ``getUnknownColumnHandler`` メソッドで返してください。
``UnknownColumnHandler`` は :doc:`query/select` の結果を :doc:`entity` にマッピングする際、
エンティティクラスにとって未知のカラムが存在する場合に実行されます。

デフォルトでは、 ``UnknownColumnException`` がスローされます。

テーブルやカラムにおけるネーミング規約の制御
--------------------------------------------

``Naming`` を ``getNaming`` メソッドで返してください。

``Naming`` は、 ``@Entity`` の ``name`` 要素に指定された（もしくは指定されない） ``NamingType``
をどのように適用するかについて制御するインタフェースです。
このインタフェースを使うことで、個別のエンティティクラスに ``NamingType`` を指定しなくても
エンティティのクラス名とプロパティ名からデータベースのテーブル名とカラム名を解決できます。

``Naming`` が使用される条件は以下の通りです。

* ``@Table`` や ``@Column`` の ``name`` 要素に値が指定されていない。

一般的なユースケースを実現するための実装は、 ``Naming`` の ``static`` なメンバに定義されています。

デフォルトでは、 ``Naming.NONE`` が使用されます。
この実装は、エンティティクラスに指定された ``NamingType`` を使い、
指定がない場合は何の規約も適用しません。

例えば、指定がない場合にスネークケースの大文字を適用したいというケースでは、
``Naming.SNAKE_UPPER_CASE`` を使用できます。

マップのキーのネーミング規約の制御
----------------------------------

``MapKeyNaming`` を ``getMapKeyNaming`` メソッドで返してください。
``MapKeyNaming`` は検索結果を ``java.util.Map<String, Object>`` にマッピングする場合に実行されます。

デフォルトでは、 ``@Select`` などの ``mapKeyNaming`` 要素に指定された規約を適用します。

ローカルトランザクションマネージャー
------------------------------------

``LocalTransactionManager`` を ``getTransactionManager`` メソッドで返してください。
``getTransactionManager`` メソッドは、デフォルトで
``UnsupportedOperationException`` をスローします。

.. note::

  この項目は設定必須ではありませんが、
  ``org.seasar.doma.jdbc.tx.TransactionManager`` のインタフェースでトランザクションを利用したい場合は設定してください。
  設定方法については :doc:`transaction` を参照してください。

SQLの識別子の追記
------------------------------------

``Commenter`` を ``getCommenter`` メソッドで返してください。
``Commenter`` はSQLの識別子（SQLの発行箇所等を特定するための文字列）をSQLコメントとして追記するためのインタフェースです。

実装クラスには次のものがあります。

* org.seasar.doma.jdbc.CallerCommenter

``CallerCommenter`` は、SQLの呼び出し元のクラス名とメソッド名を識別子として使用します。

デフォルトの実装では、 識別子を追記しません。

Command の実装
--------------

``CommandImplementors`` を ``getCommandImplementors`` メソッドで返してください。
``CommandImplementors`` を実装すると :doc:`query/index` の実行をカスタマイズできます。

たとえば、 JDBC の API を直接呼び出すことができます。

Query の実装
------------

``QueryImplementors`` を ``getQueryImplementors`` メソッドで返してください。
``QueryImplementors`` を実装すると :doc:`query/index` の内容をカスタマイズできます。

たとえば、自動生成される SQL の一部を書き換え可能です。

タイムアウト
------------

クエリタイムアウト（秒）をあらわす ``int`` を ``getQueryTimeout`` メソッドで返してください。
この値はすべての :doc:`query/index` においてデフォルト値として使われます。

最大件数
--------

SELECT時の最大行数をあらわす ``int`` を ``getMaxRows`` メソッドで返します。
この値はすべての :doc:`query/select` においてデフォルト値として使われます。

フェッチサイズ
--------------

SELECT時のフェッチサイズをあらわす ``int`` を ``getFetchSize`` メソッドで返します。
この値はすべての :doc:`query/select` においてデフォルト値として使われます。

バッチサイズ
------------

バッチサイズをあらわす ``int`` を ``getBatchSize`` メソッドで返します。
この値は :doc:`query/batch-insert` 、:doc:`query/batch-update` 、:doc:`query/batch-delete`
においてデフォルト値として使われます。

エンティティリスナーの取得
--------------------------

``EntityListenerProvider`` を ``getEntityListenerProvider`` メソッドで返して下さい。

``EntityListenerProvider`` の ``get`` メソッドは ``EntityListener`` 実装クラスの ``Class`` と ``EntityListener`` 実装クラスのインスタンスを返す ``Supplier``
を引数に取り ``EntityListener`` のインスタンスを返します。
デフォルトの実装では ``Supplier.get`` メソッドを実行して得たインスタンスを返します。

``EntityListener`` 実装クラスのインスタンスをDIコンテナから取得したいなど、
インスタンス取得方法をカスタマイズする場合は ``EntityListenerProvider`` を実装したクラスを作成し、
``getEntityListenerProvider`` メソッドでそのインスタンスを返すよう設定してください。

JDBC ドライバのロード
=====================

.. _service provider: http://docs.oracle.com/javase/7/docs/technotes/guides/jar/jar.html#Service%20Provider
.. _tomcat driver: http://tomcat.apache.org/tomcat-7.0-doc/jndi-datasource-examples-howto.html#DriverManager,_the_service_provider_mechanism_and_memory_leaks

クラスパスが通っていれば JDBC ドライバは
`サービスプロバイダメカニズム <service provider_>`_ により自動でロードされます。

.. warning::

  実行環境によっては、 JDBC ドライバが自動でロードされないことがあります。
  たとえば Tomcat 上では WEB-INF/lib に配置された
  `JDBC ドライバは自動でロードされません <tomcat driver_>`_ 。
  そのような環境においては、その環境に応じた適切は方法を採ってください。
  たとえば Tomcat 上で動作させるためのには、上記のリンク先の指示に従って
  ``ServletContextListener`` を利用したロードとアンロードを行ってください。

定義と利用例
============

シンプル
--------

シンプルな定義は次の場合に適しています。

* DIコンテナで管理しない
* ローカルトランザクションを使用する

実装例です。

.. code-block:: java

  @SingletonConfig
  public class AppConfig implements Config {

      private static final AppConfig CONFIG = new AppConfig();

      private final Dialect dialect;

      private final LocalTransactionDataSource dataSource;

      private final TransactionManager transactionManager;

      private AppConfig() {
          dialect = new H2Dialect();
          dataSource = new LocalTransactionDataSource(
                  "jdbc:h2:mem:tutorial;DB_CLOSE_DELAY=-1", "sa", null);
          transactionManager = new LocalTransactionManager(
                  dataSource.getLocalTransaction(getJdbcLogger()));
      }

      @Override
      public Dialect getDialect() {
          return dialect;
      }

      @Override
      public DataSource getDataSource() {
          return dataSource;
      }

      @Override
      public TransactionManager getTransactionManager() {
          return transactionManager;
      }

      public static AppConfig singleton() {
          return CONFIG;
      }
  }

.. note::

  クラスに ``@SingletonConfig`` を注釈するのを忘れないようにしてください。

利用例です。
定義した設定クラスは、@Daoに指定します。

.. code-block:: java

  @Dao(config = AppConfig.class)
  public interface EmployeeDao {

      @Select
      Employee selectById(Integer id);
  }


アドバンスド
------------------

アドバンスドな定義は次の場合に適しています。

* DIコンテナでシングルトンとして管理する
* DIコンテナやアプリケーションサーバーが提供するトランザクション管理機能を使う

実装例です。
``dialect`` と ``dataSource`` はDIコンテナによってインジェクションされることを想定しています。

.. code-block:: java

  public class AppConfig implements Config {

      private Dialect dialect;

      private DataSource dataSource;

      @Override
      public Dialect getDialect() {
          return dialect;
      }

      public void setDialect(Dialect dialect) {
          this.dialect = dialect;
      }

      @Override
      public DataSource getDataSource() {
          return dataSource;
      }

      public void setDataSource(DataSource dataSource) {
          this.dataSource = dataSource;
      }
  }

利用例です。
定義した設定クラスのインスタンスがDIコンテナによってインジェクトされるようにします。

.. code-block:: java

  @Dao
  @AnnotateWith(annotations = {
      @Annotation(target = AnnotationTarget.CONSTRUCTOR, type = javax.inject.Inject.class),
      @Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = javax.inject.Named.class, elements = "\"config\"") })
  public interface EmployeeDao {

      @Select
      Employee selectById(Integer id);
  }

上記の例では ``@AnnotateWith`` の記述をDaoごとに繰り返し記述する必要があります。
繰り返しを避けたい場合は、任意のアノテーションに一度だけ ``@AnnotateWith`` を記述し、
Daoにはそのアノテーションを注釈してください。

.. code-block:: java
   
  @AnnotateWith(annotations = {
      @Annotation(target = AnnotationTarget.CONSTRUCTOR, type = javax.inject.Inject.class),
      @Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = javax.inject.Named.class, elements = "\"config\"") })
  public @interface InjectConfig {
  }

.. code-block:: java

  @Dao
  @InjectConfig
  public interface EmployeeDao {

      @Select
      Employee selectById(Integer id);
  }


