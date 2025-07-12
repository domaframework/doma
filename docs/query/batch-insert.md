# Batch insert

```{contents}
:depth: 4
```

Annotate a Dao method with `@BatchInsert` to execute batch insert operations.

```java
@Dao
public interface EmployeeDao {
    @BatchInsert
    int[] insert(List<Employee> employees);

    @BatchInsert
    BatchResult<ImmutableEmployee> insert(List<ImmutableEmployee> employees);
}
```

By default, the INSERT statement is automatically generated.
You can map to an arbitrary SQL file by setting the `sqlFile` property to `true` in the `@BatchInsert` annotation.

If an entity listener is specified for the entity class, its `preInsert` method is called for each entity before executing the insert operation.
Similarly, the `postInsert` method is called for each entity after the insert operation completes.

## Return value

If the elements of the parameter (which must be an `Iterable` subtype) are immutable entity classes, the return value must be `org.seasar.doma.jdbc.BatchResult` with the entity class as its element type.

If the above condition is not met, the return value must be `int[]`, where each element represents the number of rows affected by each insert operation.

## Batch insert by auto generated SQL

The parameter type must be a `java.lang.Iterable` subtype that has [Entity classes](../entity.md) as its element type.
Only one parameter can be specified.
The parameter must not be `null`.
The return value array's element count equals the `Iterable` element count.
Each element in the array represents the insert count for the corresponding operation.

### Identifier

If the identifier in [Entity classes](../entity.md) is annotated with `@GeneratedValue`, the identifier is automatically generated and set.

Reference [Id generation](../entity.md#id-generation) for cautionary points.

If you don't use auto-generated keys in your application, you can enable the `ignoreGeneratedKeys` flag.
This flag may improve performance.

```java
@BatchInsert(ignoreGeneratedKeys = true)
int[] insert(List<Employee> entities);
```

### Version number

If a value that is explicitly set is greater than `0`, then that value is used if the [Entity classes](../entity.md) has a property annotated with `@Version`.
If the value is not set or is less than `0`, the value is automatically set to `1`.

### Properties of @BatchInsert

#### insertable

A property is excluded from insertion if the entity class has a property annotated with `@Column` and the `insertable` property of the `@Column` annotation is set to `false`.

#### exclude

A property specified in the `exclude` property of the `@BatchInsert` annotation is excluded from the insertion operation.
Even if the `insertable` property of the `@Column` annotation is set to `true`, the property is excluded from the insertion operation if it is specified in this element.

```java
@BatchInsert(exclude = {"name", "salary"})
int[] insert(List<Employee> employees);
```

#### include

Only properties specified in the `include` property of the `@BatchInsert` annotation are included in the insertion operation.
If the same property is specified in both the `include` property and the `exclude` property of the `@BatchInsert` annotation, it is excluded from the insertion operation.
Even if a property is specified in this element, it is excluded from the insertion operation if the `insertable` property of its `@Column` annotation is set to `false`.

```java
@BatchInsert(include = {"name", "salary"})
int[] insert(List<Employee> employees);
```

#### duplicateKeyType

This property defines the strategy for handling duplicate keys during an insert operation.

It can take one of three values:

- `DuplicateKeyType.UPDATE`: If a duplicate key is encountered, the existing row in the table will be updated.
- `DuplicateKeyType.IGNORE`: If a duplicate key is encountered, the insert operation will be ignored, and no changes will be made to the table.
- `DuplicateKeyType.EXCEPTION`: If a duplicate key is encountered, an exception will be thrown.

```java
@BatchInsert(duplicateKeyType = DuplicateKeyType.UPDATE)
int[] insert(List<Employee> employees);
```

#### duplicateKeys

This property represents the keys that should be used to determine if a duplicate key exists. If the duplicate key exists, the operation will use the `duplicateKeyType` strategy to handle the duplicate key.

```java
@BatchInsert(duplicateKeyType = DuplicateKeyType.UPDATE, duplicateKeys = {"employeeNo"})
int[] insert(List<Employee> employees);
```

:::{note}
This property is only utilized when the `duplicateKeyType` strategy is either `DuplicateKeyType.UPDATE` or `DuplicateKeyType.IGNORE`.
:::

:::{note}
The MySQL dialect does not utilize this property.
:::

## Batch insert by SQL file

To execute batch insertion using an SQL file,
set the `sqlFile` property of the `@BatchInsert` annotation to `true` and prepare an SQL file that corresponds to the method.

```java
@BatchInsert(sqlFile = true)
int[] insert(List<Employee> employees);

@BatchInsert(sqlFile = true)
BatchResult<ImmutableEmployee> insert(List<ImmutableEmployee> employees);
```

The parameter type must be a `java.lang.Iterable` subtype that has [Entity classes](../entity.md) as its element type.
Only one parameter can be specified.
The parameter must not be `null`.
The return value array's element count equals the `Iterable` element count.
Each element in the array represents the insert count for the corresponding operation.

If an entity listener is specified for the [Entity classes](../entity.md), its methods are not called when using SQL files.

For example, you would write SQL like the following to correspond to the above method:

```sql
insert into employee (id, name, salary, version)
values (/* employees.id */0, /* employees.name */'hoge', /* employees.salary */100, /* employees.version */0)
```

The parameter name in the SQL file refers to the element of the `java.lang.Iterable` subtype.

Automatic identifier setting and automatic version number setting are not performed when inserting via SQL file.

Additionally, the following properties of `@BatchInsert` are not used:

- exclude
- include
- duplicateKeyType
- duplicateKeys

## Unique constraint violation

`UniqueConstraintException` is thrown if a unique constraint violation occurs, regardless of whether an SQL file is used or not.

## Query timeout

You can specify the query timeout in seconds using the `queryTimeout` property of the `@BatchInsert` annotation.

```java
@BatchInsert(queryTimeout = 10)
int[] insert(List<Employee> employees);
```

This specification is applied regardless of whether an SQL file is used or not.
The query timeout specified in the config class is used if the `queryTimeout` property is not set.

## Batch size

You can specify the batch size using the `batchSize` property of the `@BatchInsert` annotation.

```java
@BatchInsert(batchSize = 10)
int[] insert(List<Employee> employees);
```

This setting applies regardless of whether you use a SQL file or not.
If you do not specify a value for the `batchSize` property, the batch size configured in the [Configuration](../config.md) class is used.

## SQL log output format

You can specify the SQL log output format using the `sqlLog` property of the `@BatchInsert` annotation.

```java
@BatchInsert(sqlLog = SqlLogType.RAW)
int insert(Employee employee);
```

`SqlLogType.RAW` indicates that the log outputs SQL with bind parameters.
