# Multi-row insert

```{contents}
:depth: 4
```

Annotate the DAO method with `@MultiInsert` to execute a multi-row insert.

```java
@Dao
public interface EmployeeDao {
    @MultiInsert
    int insert(List<Employee> employees);

    @MultiInsert
    MultiResult<ImmutableEmployee> insert(List<ImmutableEmployee> employees);
}
```

By using multi-row insert, you can issue SQL statements such as the following:

```sql
insert into EMPLOYEE (EMPLOYEE_ID, EMPLOYEE_NO, EMPLOYEE_NAME, AGE, VERSION)
values (?, ?, ?, ?, ?), (?, ?, ?, ?, ?)
```

If an entity listener is specified for the entity class, its `preInsert` method is called for each entity before executing the insert operation.
Similarly, the `postInsert` method is called for each entity after the insert operation completes.

:::{note}
The databases that support this feature are:

- H2
- MySQL
- PostgreSQL
- SQL Server
- Oracle Database

However, in the case of SQL Server and Oracle, this feature cannot be executed on tables with an auto-increment primary key.
:::

## Return type

### When using the returning property

See [Returning](#returning).

### When not using the returning property

If the type argument of the `Iterable` parameter is an immutable entity class, the return type must be `org.seasar.doma.jdbc.MultiResult` with that entity class as an element.

If the type argument of the `Iterable` parameter is a mutable entity class, the return type must be `int` that represents updated count.

## Parameter type

The parameter type must be a subtype of `java.lang.Iterable` that has the entity class as its element.

The parameter must not be `null`.

## Automatically generated values

During the execution of a multi-insert, automatically generated values will be set to the entity properties.

### Identifier

See [Id generation](../entity.md#id-generation).

### Version number

If the application does not set a value to the version property or sets a value less than `0`, the value `1` will be ultimately set to that property.

If the application explicitly sets a value greater than `0` to the version property, automatic generation will not occur.

See also [Version](../entity.md#version).

## Properties of @MultiInsert

### exclude

Entity properties specified in the `exclude` property of `@MultiInsert` will be excluded from the insert targets, even if they are set as `insertable` in the
`@Column` annotation.

```java
@MultiInsert(exclude = {"name", "salary"})
int insert(List<Employee> employees);
```

### include

Only the entity properties specified in the `include` property of `@MultiInsert` will be included in the insert targets.

If the same entity property is specified in both the `exclude` and `include` properties, that entity property will not be included in the insert targets.

Entity properties with `insertable` set to `false` in the `@Column` annotation will not be included in the insert targets, even if they are specified in the `include` property.

```java
@MultiInsert(include = {"name", "salary"})
int insert(List<Employee> employees);
```

### duplicateKeyType

This property defines the strategy for handling duplicate keys during an insert operation.

It can take one of three values:

- `DuplicateKeyType.UPDATE`: If a duplicate key is encountered, the existing row in the table will be updated.
- `DuplicateKeyType.IGNORE`: If a duplicate key is encountered, the insert operation will be ignored, and no changes will be made to the table.
- `DuplicateKeyType.EXCEPTION`: If a duplicate key is encountered, an exception will be thrown.

```java
@MultiInsert(duplicateKeyType = DuplicateKeyType.UPDATE)
int insert(List<Employee> employees);
```

### duplicateKeys

This property represents the keys that should be used to determine if a duplicate key exists. If the duplicate key exists, the operation will use the `duplicateKeyType` strategy to handle the duplicate key.

```java
@MultiInsert(duplicateKeyType = DuplicateKeyType.UPDATE, duplicateKeys = {"employeeNo"})
int insert(List<Employee> employees);
```

:::{note}
This property is only utilized when the `duplicateKeyType` strategy is either `DuplicateKeyType.UPDATE` or `DuplicateKeyType.IGNORE`.
:::

:::{note}
The MySQL dialect does not utilize this property.
:::

(multi-row-insert-returning)=

### returning

By specifying `@Returning` in the `returning` property,
you can generate SQL equivalent to the `INSERT .. RETURNING` clause.

```java
@Dao
public interface EmployeeDao {
    @MultiInsert(returning = @Returning)
    List<Employee> insert(List<Employee> employees);

    @MultiInsert(returning = @Returning(include = { "employeeId", "version" }))
    List<Employee> insertReturningIdAndVersion(List<Employee> employees);

    @MultiInsert(returning = @Returning(exclude = { "password" }))
    List<Employee> insertReturningExceptPassword(List<Employee> employees);
}
```

You can use the `include` element of `@Returning` to specify which entity properties
(corresponding to database columns) should be returned by the RETURNING clause.
Alternatively, you can use the `exclude` element to specify which properties should not be returned.
If the same entity property is included in both `include` and `exclude` elements, it will not be returned.

The return type must be a `List` of entity instances.

:::{note}
Only H2 Database, PostgreSQL, SQL Server, and SQLite Dialects support this feature.
:::

## Unique constraint violation

An `org.seasar.doma.jdbc.UniqueConstraintException` is thrown if a unique constraint violation occurs.

## Query timeout

You can specify seconds of query timeout to `queryTimeout` property within `@MultiInsert` annotation.

```java
@MultiInsert(queryTimeout = 10)
int insert(List<Employee> employees);
```

If no value is set for the `queryTimeout` property, the query timeout specified in the config class is used.

## SQL log output format

You can specify SQL log output format to `sqlLog` property within `@MultiInsert` annotation.

```java
@MultiInsert(sqlLog = SqlLogType.RAW)
int insert(List<Employee> employees);
```

`SqlLogType.RAW` outputs the SQL statement with its binding parameters in the log.
