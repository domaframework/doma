# Insert

```{contents}
:depth: 4
```

Annotate a Dao method with `@Insert` to execute insert operations.

```java
@Dao
public interface EmployeeDao {
    @Insert
    int insert(Employee employee);

    @Insert
    Result<ImmutableEmployee> insert(ImmutableEmployee employee);
}
```

By default, the INSERT statement is automatically generated.
You can map to an arbitrary SQL file by setting the `sqlFile` property to `true` in the `@Insert` annotation.

If an entity listener is specified for the entity class parameter, its `preInsert` method is called before executing the insert operation.
Similarly, the `postInsert` method is called after the insert operation completes.

## Return value

### When using the returning property

See {ref}`insert-returning`.

### When not using the returning property

The return value must be an `org.seasar.doma.jdbc.Result` with the entity class as its element if the parameter is an immutable entity class.

The return value must be an `int` representing the update count if the above condition is not satisfied.

## Insert by auto generated SQL

The parameter type must be an entity class.
Only one parameter can be specified.
The parameter must not be null.

```java
@Insert
int insert(Employee employee);

@Insert
Result<ImmutableEmployee> insert(ImmutableEmployee employee);
```

### Identifier

The identifier is automatically generated and set if the [](../entity.md) identifier is annotated with `@GeneratedValue`.

Reference {ref}`identity-auto-generation` for cautionary points.

### Version numbers

If a value that is explicitly set is greater than 0, then that value is used if the [](../entity.md) has a property annotated with `@Version`.

If the value is not set or is less than 0, the value is automatically set to 1.

### Properties of @Insert

#### insertable

A property is excluded from insertion if the entity class has a property annotated with `@Column` and the `insertable` property of `@Column` is set to false.

#### exclude

A property specified in the `exclude` element of `@Insert` is excluded from insertion.
Even if the `insertable` element of `@Column` is set to true, the property is excluded from insertion if it is specified in this element.

```java
@Insert(exclude = {"name", "salary"})
int insert(Employee employee);
```

#### include

A property specified in the `include` element of `@Insert` is included in the insertion.
If the same property is specified in both the `include` element and the `exclude` element of `@Insert`, the property is excluded from insertion.

Even if a property is specified in this element, it is excluded from insertion if the `insertable` element of its `@Column` annotation is set to false.

```java
@Insert(include = {"name", "salary"})
int insert(Employee employee);
```

#### excludeNull

Properties with a value of `null` are excluded from insertion when the `excludeNull` element of `@Insert` is set to true.
When this element is set to true, a property is excluded from insertion if its value is `null`, even if the `insertable` element of its `@Column` annotation is set to true or the property is specified in the `include` element of `@Insert`.

```java
@Insert(excludeNull = true)
int insert(Employee employee);
```

#### duplicateKeyType

This property defines how to handle duplicate keys during an insert operation.

It can take one of three values:

- `DuplicateKeyType.UPDATE`: If a duplicate key is encountered, the existing row in the table will be updated.
- `DuplicateKeyType.IGNORE`: If a duplicate key is encountered, the insert operation will be ignored, and no changes will be made to the table.
- `DuplicateKeyType.EXCEPTION`: If a duplicate key is encountered, an exception will be thrown.

```java
@Insert(duplicateKeyType = DuplicateKeyType.UPDATE)
int insert(Employee employee);
```

#### duplicateKeys

This property represents the keys that should be used to determine if a duplicate key exists. If the duplicate key exists, the operation will use the `duplicateKeyType` strategy to handle the duplicate key.

```java
@Insert(duplicateKeyType = DuplicateKeyType.UPDATE, duplicateKeys = {"employeeNo"})
int insert(Employee employee);
```

:::{note}
This property is only utilized when the `duplicateKeyType` strategy is either `DuplicateKeyType.UPDATE` or `DuplicateKeyType.IGNORE`.
:::

:::{note}
The MySQL dialect does not utilize this property.
:::

(insert-returning)=

#### returning

By specifying `@Returning` in the `returning` property,
you can generate SQL equivalent to the `INSERT .. RETURNING` clause.

```java
@Dao
public interface EmployeeDao {
    @Insert(returning = @Returning)
    Employee insert(Employee employee);

    @Insert(returning = @Returning(include = { "employeeId", "version" }))
    Employee insertReturningIdAndVersion(Employee employee);

    @Insert(returning = @Returning(exclude = { "password" }))
    Employee insertReturningExceptPassword(Employee employee);

    @Insert(returning = @Returning, duplicateKeyType = DuplicateKeyType.IGNORE)
    Optional<Employee> insertOrIgnore(Employee employee);
}
```

You can use the `include` element of `@Returning` to specify which entity properties
(corresponding to database columns) should be returned by the RETURNING clause.
Alternatively, you can use the `exclude` element to specify which properties should not be returned.
If the same entity property is included in both `include` and `exclude` elements, it will not be returned.

The return type must be either an entity class
or an `Optional` containing an entity class as its element.

:::{note}
Only H2 Database, PostgreSQL, SQL Server, and SQLite Dialects support this feature.
:::

## Insert by SQL file

To execute insertion using an SQL file,
set the `sqlFile` element of `@Insert` to `true` and prepare an SQL file that corresponds to the method.

You can use parameters of any type.
There is no limit to the number of parameters that can be specified.
You can set `null` to a parameter if the parameter type is a basic type or domain class.
For other types, the parameter must not be `null`.

```java
@Insert(sqlFile = true)
int insert(Employee employee);

@Insert(sqlFile = true)
Result<ImmutableEmployee> insert(ImmutableEmployee employee);
```

For example, you describe SQL file like below to correspond above method.

```sql
insert into employee (id, name, salary, version)
values (/* employee.id */0,
        /* employee.name */'hoge',
        /* employee.salary */100,
        /* employee.version */0)
```

Automatic identifier setting and automatic version value setting are not performed when inserting via SQL file.

Additionally, the following properties of `@Insert` are not used:

- exclude
- include
- excludeNull
- duplicateKeyType
- duplicateKeys

## Unique constraint violation

A `UniqueConstraintException` is thrown if a unique constraint violation occurs, regardless of whether you use a SQL file or not.

## Query timeout

You can specify second of query timeout to `queryTimeout` element of `@Insert`.

```java
@Insert(queryTimeout = 10)
int insert(Employee employee);
```

This specification is applied regardless of whether an SQL file is used or not.
The query timeout specified in [](../config.md) is used if the `queryTimeout` element is not set.

## SQL log output format

You can specify SQL log output format to `sqlLog` element of `@Insert`.

```java
@Insert(sqlLog = SqlLogType.RAW)
int insert(Employee employee);
```

`SqlLogType.RAW` indicates that the log outputs SQL with bind parameters.
