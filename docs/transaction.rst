==================
Transaction
==================

.. contents:: Contents
   :depth: 3

Doma supports local transaction.
This document explains how to configure and use the local transaction.

If you want to use global transaction, use frameworks or application servers
which support JTA (Java Transaction API).

See also :ref:`config-configuration-definition` .

Configuration
=============

To use local transaction, these conditions are required:

* Return ``LocalTransactionDataSource`` from ``getDataSource`` in ``Config``
* Generate ``LocalTransactionManager`` using the ``LocalTransactionDataSource`` above in the constructor
* Use the ``LocalTransactionManager`` above to control database access

There are several ways to generate and get the ``LocalTransactionManager``,
but the simplest way is to generate it in the constructor of ``Config`` implementaion class
and make the ``Config`` implementaiton class singleton.

Here is an example:

.. code-block:: java

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

Usage
======

We use the following DAO interface in example code:

.. code-block:: java

  @Dao
  public interface EmployeeDao {
      @Sql("select /*%expand*/* from employee where id = /*id*/0")
      @Select
      Employee selectById(Integer id);

      @Update
      int update(Employee employee);

      @Delete
      int delete(Employee employee);
  }


Start and finish transactions
-----------------------------

You can start a transaction with one of following methods of ``TransactionManager``:

* required
* requiresNew
* notSupported

Use a lambda expression to write a process which you want to run in a transaction.

.. code-block:: java

  TransactionManager tm = AppConfig.singleton().getTransactionManager();
  EmployeeDao dao = new EmployeeDaoImpl(AppConfig.singleton());

  tm.required(() -> {
      Employee employee = dao.selectById(1);
      employee.setName("hoge");
      employee.setJobType(JobType.PRESIDENT);
      dao.update(employee);
  });

The transaction is committed if the lambda expression finishes successfully.
The transaction is rolled back if the lambda expression throws an exception.

Explicit rollback
--------------------

Besides throwing an exception, you can use ``setRollbackOnly`` method to rollback a transaction.

.. code-block:: java

  TransactionManager tm = AppConfig.singleton().getTransactionManager();
  EmployeeDao dao = new EmployeeDaoImpl(AppConfig.singleton());

  tm.required(() -> {
      Employee employee = dao.selectById(1);
      employee.setName("hoge");
      employee.setJobType(JobType.PRESIDENT);
      dao.update(employee);
      // Mark as rollback
      tm.setRollbackOnly();
  });

Savepoint
--------------

With a savepoint, you can cancel specific changes in a transaction.

.. code-block:: java

  TransactionManager tm = AppConfig.singleton().getTransactionManager();
  EmployeeDao dao = new EmployeeDaoImpl(AppConfig.singleton());

  tm.required(() -> {
      // Search and update
      Employee employee = dao.selectById(1);
      employee.setName("hoge");
      dao.update(employee);

      // Create a savepoint
      tm.setSavepoint("beforeDelete");

      // Delete
      dao.delete(employee);

      // Rollback to the savepoint (cancel the deletion above)
      tm.rollback("beforeDelete");
  });
