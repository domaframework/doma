# SQL processor

```{contents}
:depth: 4
```

SQL processors allow you to handle SQL statements that are generated from corresponding SQL templates.
To designate a DAO method as an SQL processor, annotate it with `@SqlProcessor`:

```java
@Dao
public interface EmployeeDao {
    @SqlProcessor
    <R> R process(Integer id, BiFunction<Config, PreparedSql, R> handler);
    ...
}
```

:::{warning}
It is essential to be aware of SQL injection vulnerabilities when using this feature.
Whenever possible, consider using alternatives to SQL processors for better security.
:::

## Return type

The return type must be the same type as the third type parameter of `BiFunction`:

```java
@SqlProcessor
String process(Integer id, BiFunction<Config, PreparedSql, String> handler);
```

If the return type is `void`, the third type parameter of `BiFunction` must be `Void`:

```java
@SqlProcessor
void process(Integer id, BiFunction<Config, PreparedSql, Void> handler);
```

## Parameter

Include a parameter whose type is `BiFunction`.
The `BiFunction` parameter accepts a configuration and an SQL statement then processes them.
Parameters other than the `BiFunction` parameter are used in the SQL template.

## Example

Suppose you want to change the SQL statement generated from an SQL template and execute it:

```java
EmployeeDao dao = ...
dao.process(1, (config, preparedSql) -> {
  String sql = preparedSql.getRawSql();
  String anotherSql = createAnotherSql(sql);
  DataSource dataSource = config.getDataSource()
  Connection connection = dataSource.getConnection();
  PreparedStatement statement = connection.prepareStatement(anotherSql);
  return statement.execute();
});
```

```sql
select * from employee where id = /*^ id */0
```
