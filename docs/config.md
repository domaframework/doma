=============
# Configuration

.. contents::
   :depth: 4

Configurable items must be returned from the methods of a class that implements
the `org.seasar.doma.jdbc.Config` interface.

# Configurable items

## DataSource

Return a JDBC `DataSource` from the `getDataSource` method.
If you need to use local transactions provided by Doma, return a `LocalTransactionDataSource`.

See also: [\1](\1)

```{note}

   This is a required configuration item.

## DataSource's name

Return the DataSource's name from the `getDataSourceName` method.
In environments where multiple DataSources are used, this name is important.
You must assign a unique name to each DataSource.

The default value is the full qualified name of the implementation class of `Config`.

## SQL dialect

Return a `Dialect` from the `getDialect` method.
You must choose an appropriate dialect for the database you are using.

Doma provides the following dialects:

+----------------------------+------------------+--------------------------------------+
| Database                   | Dialect Name     | Description                          |
+============================+==================+======================================+
| DB2                        | Db2Dialect       |                                      |
+----------------------------+------------------+--------------------------------------+
| H2 Database Engine 1.2.126 | H212126Dialect   | H2 Database Engine 1.2.126           |
+----------------------------+------------------+--------------------------------------+
| H2 Database                | H2Dialect        | H2 Database Engine 1.3.171 and above |
+----------------------------+------------------+--------------------------------------+
| HSQLDB                     | HsqldbDialect    |                                      |
+----------------------------+------------------+--------------------------------------+
| Microsoft SQL Server 2008  | Mssql2008Dialect | Microsoft SQL Server 2008            |
+----------------------------+------------------+--------------------------------------+
| Microsoft SQL Server       | MssqlDialect     | Microsoft SQL Server 2012 and above  |
+----------------------------+------------------+--------------------------------------+
| MySQL                      | MySqlDialect     | MySQL 5 and 8                        |
+----------------------------+------------------+--------------------------------------+
| Oracle Database 11g        | Oracle11Dialect  | Oracle Database 11g                  |
+----------------------------+------------------+--------------------------------------+
| Oracle Database            | OracleDialect    | Oracle Database 12g and above        |
+----------------------------+------------------+--------------------------------------+
| PostgreSQL                 | PostgresDialect  |                                      |
+----------------------------+------------------+--------------------------------------+
| SQLite                     | SqliteDialect    |                                      |
+----------------------------+------------------+--------------------------------------+

These dialects are located in the `org.seasar.doma.jdbc.dialect` package.

MysqlDialect supports MySQL 5 by default. To use it as a dialect for MySQL 8, 
instantiate MysqlDialect by specifying the version as follows:

```java

    MysqlDialect dialect = new MysqlDialect(MysqlDialect.MySqlVersion.V8);

```{note}

    This is a required configuration item.

.. _config-logger:

## Logger

Return a `JdbcLogger` from the `getJdbcLogger` method.

Doma provides the following JdbcLogger:

* org.seasar.doma.jdbc.UtilLoggingJdbcLogger

The default JdbcLogger is UtilLoggingJdbcLogger, which uses `java.util.logging`.

## SQL File Repository

Return a `SqlFileRepository` from the `getSqlFileRepository` method.

Doma provides the following SqlFileRepositories:

* org.seasar.doma.jdbc.GreedyCacheSqlFileRepository
* org.seasar.doma.jdbc.NoCacheSqlFileRepository

The default SqlFileRepository is GreedyCacheSqlFileRepository,
which caches the results of SQL parsing without limitation.

## Controlling REQUIRES_NEW transaction

Return a `RequiresNewController` from the `getRequiresNewController` method.
RequiresNewController may begin new transactions to make transaction lock time shorter.

This feature is used only when you use `@TableGenerator`, which generates identities using a database table.

The default RequiresNewController does nothing.

## Loading classes

Return a `ClassHelper` from the `getClassHelper` method.

When the application server or framework you use loads classes in a specific way,
consider creating your own ClassHelper.

The default ClassHelper primarily loads classes using `Class#forName`.

## Choosing SQL format contained in exception messages

Return a `SqlLogType` from the `getExceptionSqlLogType` method.
The default SqlLogType includes the formatted SQL in exception messages.

## Handling duplicate columns

Return a `DuplicateColumnHandler` from the `getDuplicateColumnHandler` method.
In result set mappings, if a duplicate column for an entity class is found,
the `DuplicateColumnHandler` handles this situation.

The default `DuplicateColumnHandler` does nothing.
To throw a `DuplicateColumnException` when duplicates are found, return a `ThrowingDuplicateColumnHandler`.

## Handling unknown columns

Return a `UnknownColumnHandler` from the `getUnknownColumnHandler` method.
In result set mappings, if a column unknown to an entity class is found,
the UnknownColumnHandler handles this situation.

The default UnknownColumnHandler throws an `UnknownColumnException`.

## Naming convention for tables and columns

Return a `Naming` from the `getNaming` method.
The `naming` element of `@Entity` takes precedence over this value.
When you specify explicit values for the name elements of `@Table` and `@Column`,
the naming convention is not applied to them.

The default Naming does nothing.

## Naming convention for keys of java.util.Map

Return a `MapKeyNaming` from the `getMapKeyNaming` method.
The MapKeyNaming is used when the result set is mapped to `java.util.Map<String, Object>`.

The default MapKeyNaming does nothing.

## Local transaction manager

Return a `LocalTransactionManager` from the `getTransactionManager` method.
By default, the `getTransactionManager` method throws `UnsupportedOperationException`.

See also: [\1](\1)

## Adding SQL identifiers to SQLs as comments

Return a `Commenter` from the `getCommenter` method.

Doma provides the following commenter:

* org.seasar.doma.jdbc.CallerCommenter

The default Commenter does nothing.

## Command implementors

Return a `CommandImplementors` from the `getCommandImplementors` method.
For example, the CommandImplementors provides a hook to execute JDBC API.

## Query implementors

Return a `QueryImplementors` from the `getQueryImplementors` method.
For example, the QueryImplementors provides a hook to rewrite SQL statements.

## Query timeout

Return the query timeout (in seconds) from the `getQueryTimeout` method.
This value is used as the default in [\1](\1).

## Max rows

Return the max rows from the `getMaxRows` method.
This value is used as the default in [\1](\1).

## Fetch size

Return the fetch size from the `getFetchSize` method.
This value is used as the default in [\1](\1).

## Batch size

Return the batch size from the `getBatchSize` method.
This value is used as the default in [\1](\1),
[\1](\1) and [\1](\1).

## Providing entity listeners

Return an `EntityListenerProvider` from the `getEntityListenerProvider` method.
When you want to get entity listeners from a dependency injection container,
create your own EntityListenerProvider.

The default EntityListenerProvider gets the entity listener from the supplied provider.

## SQL Builder Settings

Return a `SqlBuilderSettings` from the `getSqlBuilderSettings` method.

`SqlBuilderSettings` controls the following aspects of SQL building:

- Whether to remove block comments from SQL
- Whether to remove line comments from SQL
- Whether to remove blank lines from SQL
- Whether to enable IN list padding

IN list padding is a feature that pads the parameters in an SQL IN clause with the last parameter
when the number of parameters is less than a power of 2.
This feature helps ensure that the same SQL statement is more likely to be generated regardless of the number of parameters,
which can have positive effects on SQL caching and related performance optimizations.

By default, no special controls are applied.

## Statistic Manager

Return a `StatisticManager` from the `getStatisticManager` method.

`StatisticManager` manages statistical information related to SQL execution.
It retains the following information for each SQL statement:

- execution count
- execution maximum time in milliseconds
- execution minimum time in milliseconds
- total execution time in milliseconds
- average execution time in milliseconds

Collection of statistical information is disabled by default.
To enable it, do the following:

```java

    Config config = ...
    config.getStatisticManager().setEnabled(true);

To disable it, call `setEnabled(false)`.

The default implementation collects statistical information indefinitely while enabled.
To prevent memory exhaustion, either call the `clear` method of `StatisticManager` periodically
or create an appropriate implementation class for `StatisticManager`.

# Loading JDBC drivers

.. _service provider: https://docs.oracle.com/javase/8/docs/technotes/guides/jar/jar.html#Service_Provider
.. _tomcat driver: http://tomcat.apache.org/tomcat-7.0-doc/jndi-datasource-examples-howto.html#DriverManager,_the_service_provider_mechanism_and_memory_leaks

All JDBC drivers are loaded automatically by the [service provider](service provider_) mechanism.

```{warning}

  But in the specific environment, the mechanism doesn't work appropriately.
  For example, when you use Apache Tomcat, you will find the case.
  See also: [DriverManager, the service provider mechanism and memory leaks](tomcat driver_)

.. _config-configuration-definition:

# Configuration definition

## Simple definition

The simple definition is appropriate in following cases:

* The configuration instance isn't managed in the dependency injection container
* Local transactions is used

```java

  public class DbConfig implements Config {

      private static final DbConfig CONFIG = new DbConfig();

      private final Dialect dialect;

      private final LocalTransactionDataSource dataSource;

      private final TransactionManager transactionManager;

      private DbConfig() {
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

      public static DbConfig singleton() {
          return CONFIG;
      }
  }

You can use the above `DbConfig` class as follows:

```java

  EmployeeDao dao = new EmployeeDaoImpl(DbConfig.singleton());

The above `EmployeeDao` interface must be annotated with the `@Dao` annotation as follows:

```java

  @Dao
  public interface EmployeeDao {

      @Select
      Employee selectById(Integer id);
  }

## More Simple definition

You can build the configuration more easily by using `org.seasar.doma.jdbc.SimpleConfig`.

`SimpleConfig` determines the `Dialect` based on the connection string and manages transactions using local transactions.

Here is an example of building a `Config` using `SimpleConfig`.

```java

  Config config = SimpleConfig.builder("jdbc:h2:mem:tutorial;DB_CLOSE_DELAY=-1", "sa", null)
    .naming(Naming.SNAKE_UPPER_CASE)
    .queryTimeout(10)
    .build();

You can use the above `config` instance as follows:

```java

  EmployeeDao dao = new EmployeeDaoImpl(config);

```{note}

  `SimpleConfig` is primarily intended for use in sample or test code.

## Advanced definition

The advanced definition is appropriate in following cases:

* The configuration instance is managed as a singleton object in the dependency injection container
* The transaction manager is provided from the application server or framework you use

Suppose the `dialect` and the `dataSource` are injected by the dependency injection container:

```java

  public class DbConfig implements Config {

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

To inject the instance of the above class to your DAO implementation instance,
you have to annotate your DAO interfaces with `@AnnotateWith`:

```java

  @Dao
  @AnnotateWith(annotations = {
      @Annotation(target = AnnotationTarget.CONSTRUCTOR, type = javax.inject.Inject.class) })
  public interface EmployeeDao {

      @Select
      Employee selectById(Integer id);
  }

```java

  @Dao
  @AnnotateWith(annotations = {
      @Annotation(target = AnnotationTarget.CONSTRUCTOR, type = javax.inject.Inject.class)) })
  public interface DepartmentDao {

      @Select
      Department selectById(Integer id);
  }

To avoid annotating your DAO interfaces with `@AnnotateWith` repeatedly,
annotate the arbitrary annotation with it only once:

```java

  @AnnotateWith(annotations = {
      @Annotation(target = AnnotationTarget.CONSTRUCTOR, type = javax.inject.Inject.class)  })
  public @interface InjectConfig {
  }

Then, you can annotate your DAO interfaces with the above `@InjectConfig` annotation:

```java

  @Dao
  @InjectConfig
  public interface EmployeeDao {

      @Select
      Employee selectById(Integer id);
  }

```java

  @Dao
  @InjectConfig
  public interface DepartmentDao {

      @Select
      Department selectById(Integer id);
  }
