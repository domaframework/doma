# Transaction

```{contents} Contents
:depth: 4
```

Doma provides support for local transactions.
This document explains how to configure and use local transactions in your application.

If you want to use global transactions, use frameworks or application servers
that support JTA (Java Transaction API).

See also [Configuration definition](config.md#configuration-definition).

## Configuration

To use local transactions, the following conditions are required:

- Return a `LocalTransactionDataSource` from the `getDataSource` method in your `Config` implementation
- Create a `LocalTransactionManager` using the `LocalTransactionDataSource` in the constructor
- Use this `LocalTransactionManager` to control database access

There are several ways to create and access the `LocalTransactionManager`,
but the simplest approach is to create it in the constructor of your `Config` implementation class
and make that `Config` implementation a singleton instance.

Here is an example:

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
```

## Usage

The following examples use this DAO interface:

```java
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
```

### Starting and Managing Transactions

You can start a transaction using one of the following methods of `TransactionManager`:

- `required` - Uses an existing transaction if available, or creates a new one if none exists
- `requiresNew` - Always creates a new transaction, suspending any existing transaction
- `notSupported` - Executes without a transaction, suspending any existing transaction

Use a lambda expression to define the code you want to execute within a transaction.

```java
TransactionManager tm = DbConfig.singleton().getTransactionManager();
EmployeeDao dao = new EmployeeDaoImpl(DbConfig.singleton());

tm.required(() -> {
    Employee employee = dao.selectById(1);
    employee.setName("hoge");
    employee.setJobType(JobType.PRESIDENT);
    dao.update(employee);
});
```

The transaction is automatically committed if the lambda expression completes successfully.
If the lambda expression throws an exception, the transaction is automatically rolled back.

### Explicit Rollback

Besides throwing an exception, you can use the `setRollbackOnly` method to explicitly roll back a transaction.

```java
TransactionManager tm = DbConfig.singleton().getTransactionManager();
EmployeeDao dao = new EmployeeDaoImpl(DbConfig.singleton());

tm.required(() -> {
    Employee employee = dao.selectById(1);
    employee.setName("hoge");
    employee.setJobType(JobType.PRESIDENT);
    dao.update(employee);
    // Mark as rollback
    tm.setRollbackOnly();
});
```

### Using Savepoints

Savepoints allow you to roll back specific portions of a transaction while keeping other changes.

```java
TransactionManager tm = DbConfig.singleton().getTransactionManager();
EmployeeDao dao = new EmployeeDaoImpl(DbConfig.singleton());

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
```
