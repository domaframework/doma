# Entity classes

```{contents}
:depth: 4
```

Entity classes correspond to database tables or query result sets.

## Entity definition

The following code snippet shows how to define an entity:

```java
@Entity
public class Employee {
    ...
}
```

An entity class can inherit from another entity class.

The following code snippet shows how to inherit from another entity class:

```java
@Entity
public class SkilledEmployee extends Employee {
    ...
}
```

:::{note}
You can annotate [records] with `@Entity`:

```java
@Entity
public record Employee(...) {
}
```

In this case, the entity is recognized as [immutable](#immutable)
even though the `immutable` property of `@Entity` is `false`.
:::

### Entity listeners

Entity listeners execute before and after Doma issues database modification statements - INSERT, DELETE, and UPDATE.

The following code snippet shows how to define an entity listener:

```java
public class EmployeeEntityListener implements EntityListener<Employee> {
    ...
}
```

To use the entity listener, specify it in the `listener` property of the `@Entity` annotation:

```java
@Entity(listener = EmployeeEntityListener.class)
public class Employee {
    ...
}
```

An entity subclass inherits its parent's entity listener.

(naming-convention)=

### Naming convention

Naming conventions define the mapping between:

- database tables and Java entity classes
- database columns and Java entity fields

The following code snippet shows how to apply the naming convention to an entity:

```java
@Entity(naming = NamingType.SNAKE_UPPER_CASE)
public class EmployeeInfo {
    ...
}
```

When the `name` property of the `@Table` or `@Column` annotation is explicitly specified,
the naming convention is ignored for that specific element.

An entity subclass inherits its parent's naming convention.

(immutable)=

### Immutable

An entity class can be immutable.

The following code snippet shows how to define an immutable entity:

```java
@Entity(immutable = true)
public class Employee {
    @Id
    final Integer id;
    final String name;
    @Version
    final Integer version;

    public Employee(Integer id, String name, Integer version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }
    ...
}
```

The `immutable` property of the `@Entity` annotation must be set to `true`.
All persistent fields must be declared as `final`.

An entity subclass inherits its parent's immutable property.

### Table

You can specify the corresponding table name with the `@Table` annotation:

```java
@Entity
@Table(name = "EMP")
public class Employee {
    ...
}
```

If the `@Table` annotation is not specified, the table name is determined by the [](#naming-convention) .

## Field definition

By default, all fields in an entity class are persistent and correspond to database columns or result set columns.

The field type must be one of the following:

- [](basic.md)
- [](domain.md)
- [](embeddable.md)
- java.util.Optional, whose element is either [](basic.md) or [](domain.md)
- java.util.OptionalInt
- java.util.OptionalLong
- java.util.OptionalDouble

The following code snippet shows how to define a field:

```java
@Entity
public class Employee {
    ...
    Integer employeeId;
}
```

### Column

You can specify the corresponding column name with the `@Column` annotation:

```java
@Column(name = "ENAME")
String employeeName;
```

To exclude fields from INSERT or UPDATE statements, set the `insertable` or `updatable`
properties to `false` within the `@Column` annotation:

```java
@Column(insertable = false, updatable = false)
String employeeName;
```

If the `@Column` annotation is not specified, the column name is determined by the [](#naming-convention) .

:::{note}
When the field type is [](embeddable.md), you cannot apply the `@Column` annotation to the field.
:::

### Id

The database primary keys are represented with the `@Id` annotation:

```java
@Id
Integer id;
```

For composite primary keys, apply the `@Id` annotation to multiple fields:

```java
@Id
Integer id;

@Id
Integer id2;
```

:::{note}
When the field type is [](embeddable.md), you cannot apply the `@Id` annotation to the field.
:::

(identity-auto-generation)=

#### Id generation

You can instruct Doma to generate id values automatically using the `@GeneratedValue` annotation.

The field type must be one of the following:

- the subclass of java.lang.Number
- [](domain.md), whose value type is the subclass of java.lang.Number
- java.util.Optional, whose element is either above types
- OptionalInt
- OptionalLong
- OptionalDouble
- the primitive types for number

:::{note}
The generated values are assigned to the field only when the field is either `null` or has a value less than `0`.
If you use one of the primitive types as the field type,
initialize the field with a value less than `0`, such as `-1`.
:::

#### Id generation by IDENTITY

To generate values using the RDBMS IDENTITY function, set the `strategy` property of the `@GeneratedValue` annotation to `GenerationType.IDENTITY`:

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
Integer id;
```

You must first define the database primary key as IDENTITY in your database schema.

:::{warning}
Not all RDBMS systems support the IDENTITY function.
:::

#### Id generation by SEQUENCE

To generate values using the RDBMS SEQUENCE, set the `strategy` property of the `@GeneratedValue` annotation to `GenerationType.SEQUENCE`.
And use the `@SequenceGenerator` annotation:

```java
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE)
@SequenceGenerator(sequence = "EMPLOYEE_SEQ")
Integer id;
```

In advance, define the SEQUENCE in the database.
The SEQUENCE definitions (such as name, allocation size, and initial size) must
match the properties specified in the `@SequenceGenerator` annotation.

:::{warning}
Not all RDBMS systems support SEQUENCES.
:::

#### Id generation by TABLE

To generate values using the RDBMS TABLE, set the `strategy` property of the `@GeneratedValue` annotation to `GenerationType.TABLE`.
And use the `@TableGenerator` annotation:

```java
@Id
@GeneratedValue(strategy = GenerationType.TABLE)
@TableGenerator(pkColumnValue = "EMPLOYEE_ID")
Integer id;
```

In advance, define the TABLE in the database.
The TABLE's definition must correspond to the properties within the `@TableGenerator` annotation.
For example, the DDL should look like this:

```sql
CREATE TABLE ID_GENERATOR(PK VARCHAR(20) NOT NULL PRIMARY KEY, VALUE INTEGER NOT NULL);
```

You can change the table name and the column names using the properties within the `@TableGenerator` annotation.

(entity-version)=

### Version

The version fields for optimistic locking are represented with the `@Version` annotation.

The field type must be one of the following:

- the subclass of java.lang.Number
- [](domain.md), whose value type is the subclass of java.lang.Number
- java.util.Optional, whose element is either above types
- OptionalInt
- OptionalLong
- OptionalDouble
- the primitive types for number

```java
@Version
Integer version;
```

:::{note}
When the field type is [](embeddable.md), you cannot apply the `@Version` annotation to the field.
:::

### Tenant Id

The tenant id fields are represented with the `@TenantId` annotation.
The column corresponding to the annotated field is included in the WHERE clause of UPDATE and DELETE statements.

```java
@TenantId
String tenantId;
```

:::{note}
When the field type is [](embeddable.md), you cannot apply the `@TenantId` annotation to the field.
:::

### Transient

If an entity has fields that you don't want to persist, you can annotate them using `@Transient`:

```java
@Transient
List<String> nameList;
```

### Association

Use the `@Association` annotation for fields that represent associations between entities:

```java
@Association
Address address;
```

```java
@Association
List<Employee> assistants;
```

Fields annotated with `@Association` are not persisted in the database.
Instead, this annotation is used to define entity relationships within an aggregate.

This annotation should be used in conjunction with the aggregate strategy,
ensuring that related entities are treated as a single unit of consistency.
For more details, see [](aggregate-strategy.md).

### OriginalStates

If you want to include only changed values in UPDATE statements,
you can define fields annotated with `@OriginalStates`.
The fields can hold the original values that were fetched from the database.

Doma uses these values to determine which fields have changed in the application and
includes only the modified values in UPDATE statements.

The following code snippet shows how to define original states:

```java
@OriginalStates
Employee originalStates;
```

The field type must be the same as the entity type.

## Method definition

There are no limitations in the use of methods.

## Example

Instantiate the `Employee` entity class and use its instance:

```java
Employee employee = new Employee();
employee.setEmployeeId(1);
employee.setEmployeeName("SMITH");
employee.setSalary(new BigDecimal(1000));
```

[records]: https://openjdk.java.net/jeps/359
