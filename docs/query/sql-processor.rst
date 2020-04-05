=============
SQL processor
=============

.. contents::
   :depth: 3

SQL processors can handle the SQL statements generated from corresponding SQL templates.
To mark a DAO method as an SQL processor, annotate the method with ``@SqlProcessor``:

.. code-block:: java

  @Config(config = AppConfig.class)
  public interface EmployeeDao {
      @SqlProcessor
      <R> R process(Integer id, BiFunction<Config, PreparedSql, R> handler);
      ...
  }

.. warning::

  Being aware of SQL injection vulnerabilities is essential.
  If it's possible, consider alternative ways other than SQL processors.

Return type
===========

The return type must be the same type as the third type parameter of ``BiFunction``:

.. code-block:: java

  @SqlProcessor
  String process(Integer id, BiFunction<Config, PreparedSql, String> handler);

If the return type is ``void``, the third type parameter of ``BiFunction`` must be ``Void``:

.. code-block:: java

  @SqlProcessor
  void process(Integer id, BiFunction<Config, PreparedSql, Void> handler);

Parameter
=========

Include a parameter whose type is ``BiFunction``.
The ``BiFunction`` parameter accepts a configuration and an SQL statement then processes them.
Parameters other than the ``BiFunction`` parameter are used in the SQL template.

Example
=======

Suppose you want to change the SQL statement generated from an SQL template and execute it:

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
