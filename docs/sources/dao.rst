==================
Daoインタフェース
==================

.. contents:: 目次
   :depth: 3

Data Access Object （Dao） はデータベースアクセスのためのインタフェースです。

Dao定義
==================

Daoは ``@Dao`` が注釈されたインタフェースとして定義します。

インタフェースの実装クラスはaptによりコンパイル時に自動生成されます。

クエリ定義
==================

アノテーションを使って :doc:`query/index` を定義できます。

Javaコードで任意のクエリを組み立てるには `デフォルトメソッド`_ の中で :doc:`query-builder/index` を使用してください。

.. _dao-default-method:

デフォルトメソッド
==================

デフォルトメソッドでは任意の処理を記述できます。

``Config.get`` にDaoのインスタンスを渡すとDaoに関連づけられた ``Config`` インスタンスを取得できます。

.. code-block:: java

  @Dao(config = AppConfig.class)
  public interface EmployeeDao {

      default int count() {
          Config config = Config.get(this);
          SelectBuilder builder = SelectBuilder.newInstance(config);
          builder.sql("select count(*) from employee");
          return builder.getScalarSingleResult(int.class);
      }
  }

利用例
==================

コンパイルすると注釈処理により実装クラスが生成されます。
実装クラスをインスタンス化して使用してください。
ただし、設定クラスをDIコンテナで管理する場合、インスタンス化はDIコンテナで制御してください。

.. code-block:: java

  EmployeeDao employeeDao = new EmployeeDaoImpl();
  Employee employee = employeeDao.selectById(1);

デフォルトでは、実装クラスの名前はインタフェースの名前に ``Impl`` をサフィックスしたものになります。
パッケージやサフィックスを変更するには :doc:`annotation-processing` を参照してください。

デフォルトコンストラクタを使用した場合は、 ``@Dao`` の ``config`` 要素に指定した設定により ``DataSource`` が決定されますが、
特定の ``DataSource`` を指定してインスタンス化することも可能です。

.. code-block:: java

  DataSource dataSource = ...;
  EmployeeDao employeeDao = new EmployeeDaoImpl(dataSource);
  Employee employee = employeeDao.selectById(1);

また同様に、 ``Connection`` を指定してインスタンス化することも可能です。

.. code-block:: java

  Connection connection = ...;
  EmployeeDao employeeDao = new EmployeeDaoImpl(connection);
  Employee employee = employeeDao.selectById(1);

Daoインタフェースはエンティティクラスと1対1で結びついているわけではありません。
ひとつのDaoインタフェースで複数のエンティティクラスを扱えます。

.. code-block:: java

  @Dao(config = AppConfig.class)
  public interface MyDao {

      @Select
      Employee selectEmployeeById(int id);

      @Select
      Department selectDepartmentByName(String name);

      @Update
      int updateAddress(Address address);
  }

