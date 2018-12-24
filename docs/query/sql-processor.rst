==================
SQLプロセッサ
==================

.. contents:: 目次
   :depth: 3

SQLテンプレートで組み立てられたSQLをアプリケーションで扱うには、 ``@SqlProcessor`` をDaoのメソッドに注釈します。

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @SqlProcessor
      <R> R process(Integer id, BiFunction<Config, PreparedSql, R> handler);
      ...
  }

メソッドに対応する :doc:`../sql` が必須です。

.. warning::

  SQLプロセッサを使ってSQLを組み立て実行する場合、潜在的には常にSQLインジェクションのリスクがあります。
  まずは、他のクエリもしくはクエリビルダを使う方法を検討してください。
  また、SQLプロセッサでは信頼できない値をSQLの組み立てに使わないように注意してください。

戻り値
==================

メソッドの戻り値は任意の型にできます。
ただし、 ``BiFunction`` 型のパラメータの3番目の型パラメータと合わせる必要があります。

なお、戻り値の型を ``void`` にする場合、 ``BiFunction`` 型のパラメータの3番目の型パラメータには ``Void`` を指定します。

.. code-block:: java

  @SqlProcessor
  void process(Integer id, BiFunction<Config, PreparedSql, Void> handler);

パラメータ
==================

 ``BiFunction`` 型のパラメータを1つのみ含める必要があります。
 ``BiFunction`` 型のパラメータはSQLテンプレート処理後のSQLを処理するために使われます。

その他のパラメータはSQLテンプレートで参照できます。
基本的には、 :doc:`select` の問い合わせ条件に指定できるのと同じ型を使用できます。 

利用例
==================

例えば、SQLテンプレートで処理したSQLをさらに変形し直接実行することができます。（この例では例外処理を省略しています。）

.. code-block:: java

  EmployeeDao dao = ...
  dao.process(1, (config, preparedSql) -> {
    String sql = preparedSql.getRawSql();
    String anotherSql = createAnotherSql(sql);
    DataSource dataSource = config.getDataSource()
    Connection connection = dataSource.getConnection();
    PreparedStatement statement = connection.prepareStatement(anotherSql);
    return statement.execute();
  });

.. code-block:: sql

  select * from employee where id = /*^ id */0
