==================
設定
==================

Domaに対する設定は、 ``Confing`` インタフェースの実装クラスで表現します。
次の事柄を設定できます。

* データソース
* データソースの名前
* データベースの方言
* ログ出力ライブラリへのアダプタ
* SQLファイルのキャッシュ戦略
* トランザクション属性が ``REQUIRES_NEW`` であるトランザクションとの連動方法
* クラスのロード方法
* 例外メッセージに含めるSQLの種別
* クエリ時に使用するパラメータ（タイムアウト、最大件数、フェッチ件数、バッチサイズ）
* Entitiyに定義されていないカラムが結果セットに含まれていた場合のハンドラ
* ローカルトランザクションマネージャー
* ``Command`` のカスタマイズ
* ``Query`` のカスタマイズ

設定項目
=================

データソース
----------------

設定必須です。

``DataSource`` を ``getDataSource`` メソッドで返してください。

ローカルトランザクションを利用する場合は、 ``LocalTransactionDataSource`` を返してください。

データベースの方言
--------------------------

設定必須です。

``Dialect`` を ``getDialect`` メソッドで返してください。
``Dialect`` はRDBMSの方言を表すインタフェースです。

RDBMSの方言には次のものがあります。

+----------------------------+-----------------------------------------------+--------------------------------------+
| データベース               | 方言クラスの名前                              | 説明                                 |
+============================+===============================================+======================================+
| DB2                        | org.seasar.doma.jdbc.dialect.Db2Dialect       |                                      |
+----------------------------+-----------------------------------------------+--------------------------------------+
| H2 Database Engine 1.2.126 | org.seasar.doma.jdbc.dialect.H212126Dialect   | H2 Database Engine 1.2.126で稼動     |
+----------------------------+-----------------------------------------------+--------------------------------------+
| H2 Database                | org.seasar.doma.jdbc.dialect.H2Dialect        | H2 Database Engine 1.3.171以降に対応 |
+----------------------------+-----------------------------------------------+--------------------------------------+
| HSQLDB                     | org.seasar.doma.jdbc.dialect.HsqldbDialect    |                                      |
+----------------------------+-----------------------------------------------+--------------------------------------+
| Microsoft SQL Server 2008  | org.seasar.doma.jdbc.dialect.Mssql2008Dialect | Microsoft SQL Server 2008に対応      |
+----------------------------+-----------------------------------------------+--------------------------------------+
| Microsoft SQL Server       | org.seasar.doma.jdbc.dialect.MssqlDialect     | Microsoft SQL Server 2012以降に対応  |
+----------------------------+-----------------------------------------------+--------------------------------------+
| MySQL                      | org.seasar.doma.jdbc.dialect.MySqlDialect     |                                      |
+----------------------------+-----------------------------------------------+--------------------------------------+
| Oracle Database            | org.seasar.doma.jdbc.dialect.OracleDialect    |                                      |
+----------------------------+-----------------------------------------------+--------------------------------------+
| PostgreSQL                 | org.seasar.doma.jdbc.dialect.PostgresDialect  |                                      |
+----------------------------+-----------------------------------------------+--------------------------------------+
| SQLite                     | org.seasar.doma.jdbc.dialect.SqliteDialect    |                                      |
+----------------------------+-----------------------------------------------+--------------------------------------+

定義と利用例
==================

シンプル
------------------

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

      private final LocalTransactionManager transactionManager;

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
      public LocalTransactionManager getLocalTransactionManager() {
          return transactionManager;
      }

      public static AppConfig singleton() {
          return CONFIG;
      }
  }

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

* DIコンテナで管理する
* グローバルトランザクションを使う

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

注意点
====================

JDBCドライバのロード
----------------------------------

通常、クラスパスが通っていればJDBCドライバはサービスプロバイダメカニズムにより自動でロードされます。

しかし、たとえばTomcatではWEB-INF/libの下のJDBCドライバを自動でロードしません。
自動でロードされない条件下では、 ``Class.forName`` を使ってJDBCドライバをロードしてください。

``Class.forName`` を実行する場所は、設定クラスのstatic初期化子が1つの候補です。
たとえば、H2 DatabaseのJDBCドライバを明示的にロードする場合には次のようにします。

.. code-block:: java

  public class AppConfig implements Config {
      static {
          try {
              Class.forName("org.h2.Driver");
          } catch (ClassNotFoundException e) {
              throw new RuntimeException(e);
          }
      }
      ...
  }

