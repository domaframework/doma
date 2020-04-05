=============
Configuration
=============

.. contents::
   :depth: 3

The configurable items must be returned from the methods of the implementation class of
the ``org.seasar.doma.jdbc.Confing`` interface.

Configurable items
==================

DataSource
----------

Return a JDBC ``DataSource`` from the ``getDataSource`` method.
If you need local transactions provided by Doma, return a ``LocalTransactionDataSource``.

See also: :doc:`transaction`

.. note::

   Required item

DataSource's name
-----------------

Return a DataSource's name from the ``getDataSourceName`` method.
In the environment where multiple DataSources are used, the name is important.
You have to give an unique name to each DataSource.

The default value is the full qualified name of the implementation class of ``Config``.

SQL dialect
-----------

Return a ``Dialect`` from the  ``getDialect`` method.
You have to choose an appropriate dialect for the database you use.

Doma provides following dialects:

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
| MySQL                      | MySqlDialect     |                                      |
+----------------------------+------------------+--------------------------------------+
| Oracle Database 11g        | Oracle11Dialect  | Oracle Database 11g                  |
+----------------------------+------------------+--------------------------------------+
| Oracle Database            | OracleDialect    | Oracle Database 12g and above        |
+----------------------------+------------------+--------------------------------------+
| PostgreSQL                 | PostgresDialect  |                                      |
+----------------------------+------------------+--------------------------------------+
| SQLite                     | SqliteDialect    |                                      |
+----------------------------+------------------+--------------------------------------+

These dialect are located in the ``org.seasar.doma.jdbc.dialect`` package.

.. note::

   Required item

Logger
------

Return a ``JdbcLogger`` from the ``getJdbcLogger`` method.

Doma provides following JdbcLogger:

* org.seasar.doma.jdbc.UtilLoggingJdbcLogger

The default JdbcLogger is UtilLoggingJdbcLogger which uses ``java.util.logging``.

SQL File Repository
-------------------

Return a ``SqlFileRepository`` from the ``getSqlFileRepository`` method.

Doma provides following SqlFileRepositories:

* org.seasar.doma.jdbc.GreedyCacheSqlFileRepository
* org.seasar.doma.jdbc.NoCacheSqlFileRepository

The default SqlFileRepository is GreedyCacheSqlFileRepository
which caches the result of SQL parsing without limitation.

Controlling REQUIRES_NEW transaction
------------------------------------

Return a ``RequiresNewController`` from the ``getRequiresNewController`` method.
RequiresNewController may begin new transactions to makes transaction locked time shorter.

This feature is used only when you use ``@TableGenerator`` which generates identities with the table.

The default RequiresNewController does nothing.

Loading classes
---------------

Return a ``ClassHelper`` from the ``getClassHelper`` method.

When the application server and framework you use loads classes with their specific way,
consider to create your own ClassHelper.

The default ClassHelper loads classes with ``Class#forName`` mainly.

Choosing SQL format contained in exception messages
---------------------------------------------------

Return a ``SqlLogType`` from the ``getExceptionSqlLogType``.
The default SqlLogType contains the formatted SQL in exception messages.

Handling unknown columns
------------------------

Return a ``UnknownColumnHandler`` from the ``getUnknownColumnHandler`` method.
In result set mappings, if an unknown column to an entity class is found,
the UnknownColumnHandler handles the situation.

The default UnknownColumnHandler throws an ``UnknownColumnException``.

Naming convention for tables and columns
----------------------------------------

Return a ``Naming`` from the ``getNaming`` method.
The naming element of ``@Entity`` have preference over this value.
When you specify explicit value to the name elements of ``@Table`` and ``@Column``,
the naming convention is not applied to them.

The default Naming does nothing.

Naming convention for keys of java.util.Map
-------------------------------------------

Return a ``MapKeyNaming`` from the ``getMapKeyNaming`` method.
The MapKeyNaming is used when the result set is mapped to ``java.util.Map<String, Object>``.

The default MapKeyNaming does nothing.

Local transaction manager
-------------------------

Return a ``LocalTransactionManager`` from the ``getTransactionManager`` method.
The ``getTransactionManager`` method throws ``UnsupportedOperationException`` as default.

See also: :doc:`transaction`

Adding SQL identifiers to the SQLs as a comment
-----------------------------------------------

Return a ``Commenter`` from the ``getCommenter`` method.

Doma provides following commenter:

* org.seasar.doma.jdbc.CallerCommenter

The default Commenter does nothing.

Command implementors
--------------------

Return a ``CommandImplementors`` from the ``getCommandImplementors`` method.
For example, the CommandImplementors provides you a hook to execute JDBC API.

Query implementors
------------------

Return a ``QueryImplementors`` from the ``getQueryImplementors`` method.
For example, the QueryImplementors provides you a hook to rewrite SQL statements.

Query timeout
-------------

Return the query timeout (second) from the ``getQueryTimeout`` method.
This value is used as default in :doc:`query/index`.

Max rows
--------

Return the max rows from the ``getMaxRows`` method.
This value is used as default in :doc:`query/select`.

Fetch size
----------

Return the fetch size from the ``getFetchSize`` method.
This value is used as default in :doc:`query/select`.

Batch size
----------

Return the batch size from the ``getBatchSize`` method.
This value is used as default in :doc:`query/batch-insert`,
:doc:`query/batch-update` and :doc:`query/batch-delete`.

Providing entity listeners
--------------------------

Return a ``EntityListenerProvider`` from the ``getEntityListenerProvider`` method.
When you want to get entity listeners from a dependency injection container,
create your own EntityListenerProvider.

The default EntityListenerProvider get the entity listener from the accepted supplier.

Loading JDBC drivers
====================

.. _service provider: https://docs.oracle.com/javase/8/docs/technotes/guides/jar/jar.html#Service_Provider
.. _tomcat driver: http://tomcat.apache.org/tomcat-7.0-doc/jndi-datasource-examples-howto.html#DriverManager,_the_service_provider_mechanism_and_memory_leaks

All JDBC drivers are loaded automatically by the `service provider <service provider_>`_ mechanism.

.. warning::

  But in the specific environment, the mechanism doesn't work appropriately.
  For example, when you use Apache Tomcat, you will find the case.
  See also: `DriverManager, the service provider mechanism and memory leaks <tomcat driver_>`_

Configuration definition
========================

Simple definition
-----------------

The simple definition is appropriate in following cases:

* The configuration instance isn't managed in the dependency injection container
* Local transactions is used

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

  Remember to annotate the class with ``@SingletonConfig``

Specify the above class to the config element of ``@Dao``.

.. code-block:: java

  @Dao(config = AppConfig.class)
  public interface EmployeeDao {

      @Select
      Employee selectById(Integer id);
  }


Advanced definition
-------------------

The advanced definition is appropriate in following cases:

* The configuration instance is managed as a singleton object in the dependency injection container
* The transaction manager is provided from the application server or framework you use

Suppose the ``dialect`` and the ``dataSource`` are injected by the dependency injection container:

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

To inject the instance of the above class to your DAO implementation instance,
you have to annotate your DAO interfaces with ``@AnnotateWith``:

.. code-block:: java

  @Dao
  @AnnotateWith(annotations = {
      @Annotation(target = AnnotationTarget.CONSTRUCTOR, type = javax.inject.Inject.class),
      @Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = javax.inject.Named.class, elements = "\"config\"") })
  public interface EmployeeDao {

      @Select
      Employee selectById(Integer id);
  }

.. code-block:: java

  @Dao
  @AnnotateWith(annotations = {
      @Annotation(target = AnnotationTarget.CONSTRUCTOR, type = javax.inject.Inject.class),
      @Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = javax.inject.Named.class, elements = "\"config\"") })
  public interface DepartmentDao {

      @Select
      Department selectById(Integer id);
  }

To avoid annotating your DAO interfaces with ``@AnnotateWith`` repeatedly,
annotate the arbitrary annotation with it only once:

.. code-block:: java
   
  @AnnotateWith(annotations = {
      @Annotation(target = AnnotationTarget.CONSTRUCTOR, type = javax.inject.Inject.class),
      @Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = javax.inject.Named.class, elements = "\"config\"") })
  public @interface InjectConfig {
  }

Then, you can annotate your DAO interfaces with the above ``@InjectConfig`` annotation:

.. code-block:: java

  @Dao
  @InjectConfig
  public interface EmployeeDao {

      @Select
      Employee selectById(Integer id);
  }

.. code-block:: java

  @Dao
  @InjectConfig
  public interface DepartmentDao {

      @Select
      Department selectById(Integer id);
  }

