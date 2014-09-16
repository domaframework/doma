==================
トランザクション
==================

.. contents:: 目次
   :depth: 3

Domaは、ローカルトランザクションをサポートします。
このドキュメントでは、ローカルトランザクションの設定方法と利用方法について説明します。

グローバルトランザクションを使用したい場合は、JTA（Java Transaction API）
の実装をもつフレームワークやアプリケーションサーバーの機能を利用してください。

設定
====

ローカルトランザクションを実行するには次の条件を満たす必要があります。

* ``Config`` の ``getDataSource`` で ``LocalTransactionDataSource`` を返す
* 上記の ``LocalTransactionDataSource`` をコンストラクタで受けて ``LocalTransactionManager`` を生成する
* 上記の ``LocalTransactionManager`` の管理下でデータベースアクセスを行う

``LocalTransactionManager`` の生成と取得方法はいくつかありますが、最も単純な方法は、
``Config`` の実装クラスのコンストラクタで生成し ``Config`` の実装クラスをシングルトンとすることです。

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

  クラスに ``@SingletonConfig`` を指定することでシングルトンであることを表しています

利用例
======

`設定`_ で示した ``AppConfig`` クラスを以下のようにDaoインタフェースに注釈するものとして例を示します。

.. code-block:: java

  @Dao(config = AppConfig.class)
  public interface EmployeeDao {
      ...
  }

以降のコード例に登場する ``dao`` は上記クラスのインスタンスです。

トランザクションの開始と終了
----------------------------

トランザクションは ``TransactionManager`` の以下のメソッドのいずれかを使って開始します。

* required
* requiresNew
* notSupported

トランザクション内で行う処理はラムダ式として渡します。

.. code-block:: java

  TransactionManager tm = AppConfig.singleton().getTransactionManager();

  tm.required(() -> {
      Employee employee = dao.selectById(1);
      employee.setName("hoge");
      employee.setJobType(JobType.PRESIDENT);
      dao.update(employee);
  });

ラムダ式が正常に終了すればトランザクションはコミットされます。
ラムダ式が例外をスローした場合はトランザクションはロールバックされます。

明示的なロールバック
--------------------

例外をスローする方法以外でトランザクションをロールバックするには ``setRollbackOnly`` メソッドを呼び出します。

.. code-block:: java

  TransactionManager tm = AppConfig.singleton().getTransactionManager();

  tm.required(() -> {
      Employee employee = dao.selectById(1);
      employee.setName("hoge");
      employee.setJobType(JobType.PRESIDENT);
      dao.update(employee);
      // ロールバックするものとしてマークする
      tm.setRollbackOnly();
  });

セーブポイント
--------------

セーブポイントを使用することで、トランザクション中の特定の変更を取り消すことができます。

.. code-block:: java

  TransactionManager tm = AppConfig.singleton().getTransactionManager();

  tm.required(() -> {
      // 検索して更新
      Employee employee = dao.selectById(1);
      employee.setName("hoge");
      dao.update(employee);

      // セーブポイントを作成
      tm.setSavepoint("beforeDelete");

      // 削除
      dao.delete(employee);

      // セーブポイントへ戻る（上で行った削除を取り消す）
      tm.rollback("beforeDelete");
  });

