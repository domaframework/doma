# Unified Criteria API

```{contents}
:depth: 4
```

## Introduction

The Unified Criteria API provides a clear and intuitive interface
by integrating the Entityql and NativeSql DSLs from the [Classic Criteria API](criteria-api.md).

The following entity classes are used in the examples below:

```java
@Entity(metamodel = @Metamodel)
public class Employee {

  @Id private Integer employeeId;
  private Integer employeeNo;
  private String employeeName;
  private Integer managerId;
  private LocalDate hiredate;
  private Salary salary;
  private Integer departmentId;
  private Integer addressId;
  @Version private Integer version;
  @OriginalStates private Employee states;
  @Association private Department department;
  @Association private Employee manager;
  @Association private Address address;

  // getters and setters
}
```

```java
@Entity(metamodel = @Metamodel)
public class Department {

  @Id private Integer departmentId;
  private Integer departmentNo;
  private String departmentName;
  private String location;
  @Version private Integer version;
  @OriginalStates private Department originalStates;
  @Association private List<Employee> employeeList = new ArrayList<>();

  // getters and setters
}
```

```java
@Entity(immutable = true, metamodel = @Metamodel)
@Table(name = "EMPLOYEE")
public class Emp {

  @Id private final Integer employeeId;
  private final Integer employeeNo;
  private final String employeeName;
  private final Integer managerId;
  private final LocalDate hiredate;
  private final Salary salary;
  private final Integer departmentId;
  private final Integer addressId;
  @Version private final Integer version;
  @Association private final Dept department;
  @Association private final Emp manager;

  // constructor and getters
}
```

```java
@Entity(immutable = true, metamodel = @Metamodel)
@Table(name = "DEPARTMENT")
public class Dept {

  @Id private final Integer departmentId;
  private final Integer departmentNo;
  private final String departmentName;
  private final String location;
  @Version private final Integer version;

  // constructor and getters
}
```

The `@Entity(metamodel = @Metamodel)` annotation on these classes instructs Doma's annotation processor
to generate corresponding metamodel classes that enable type-safe query creation.

In our examples, the generated metamodel classes are `Employee_`, `Department_`, `Emp_`, and `Dept_`.

You can customize the metamodel names using the elements of the `Metamodel` annotation.

To bulk customize all metamodels, you can use annotation processor options.
See [Annotation Processing](annotation-processing.md) and refer to the following options:

- doma.metamodel.enabled
- doma.metamodel.prefix
- doma.metamodel.suffix

### Query DSL

The Unified Criteria API is essentially the Query DSL.

The Query DSL can execute entity queries and associations.
The entry point is the `org.seasar.doma.jdbc.criteria.QueryDsl` class.
This class includes the following methods:

- from
- insert
- delete
- update

Instantiate the `QueryDsl` class as follows:

```java
QueryDsl queryDsl = new QueryDsl(config);
```

For example, to query `Employee` and `Department` entities and associate them, use:

```java
Employee_ e = new Employee_();
Department_ d = new Department_();

List<Employee> list =
    queryDsl
        .from(e)
        .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
        .where(c -> c.eq(d.departmentName, "SALES"))
        .associate(
            e,
            d,
            (employee, department) -> {
              employee.setDepartment(department);
              department.getEmployeeList().add(employee);
            })
        .fetch();
```

The query above generates the following SQL statement:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NO, t0_.EMPLOYEE_NAME, t0_.MANAGER_ID,
t0_.HIREDATE, t0_.SALARY, t0_.DEPARTMENT_ID, t0_.ADDRESS_ID, t0_.VERSION,
t1_.DEPARTMENT_ID, t1_.DEPARTMENT_NO, t1_.DEPARTMENT_NAME, t1_.LOCATION, t1_.VERSION
from EMPLOYEE t0_ inner join DEPARTMENT t1_ on (t0_.DEPARTMENT_ID = t1_.DEPARTMENT_ID)
where t1_.DEPARTMENT_NAME = ?
```

:::{note}
In Kotlin, use `org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl` instead of `QueryDsl`.
`KQueryDsl` is included in the doma-kotlin module.
See [Kotlin-specific Criteria API](kotlin-support.md#kotlin-specific-criteria-api).
:::

## Select Statement

### Select Settings

We support the following settings:

- allowEmptyWhere
- comment
- fetchSize
- maxRows
- queryTimeout
- sqlLogType

All of these settings are optional and can be applied as follows:

```java
Employee_ e = new Employee_();

List<Employee> list = queryDsl.from(e, settings -> {
  settings.setAllowEmptyWhere(false);
  settings.setComment("all employees");
  settings.setFetchSize(100);
  settings.setMaxRows(100);
  settings.setSqlLogType(SqlLogType.RAW);
  settings.setQueryTimeout(1000);
}).fetch();
```

### Fetching

The Query DSL provides the following data-fetching methods:

- fetch
- fetchOne
- fetchOptional
- stream

```java
Employee_ e = new Employee_();

// The fetch method returns results as a list.
List<Employee> list = queryDsl.from(e).fetch();

// The fetchOne method returns a single result, possibly null.
Employee employee = queryDsl.from(e).where(c -> c.eq(e.employeeId, 1)).fetchOne();

// The fetchOptional method returns a single result as an Optional object.
Optional<Employee> optional = queryDsl.from(e).where(c -> c.eq(e.employeeId, 1)).fetchOptional();

// The stream method returns results as a stream.
Stream<Employee> stream = queryDsl.from(e).stream();
```

### Streaming

The Query DSL supports the following stream-handling methods:

- mapStream
- collect
- openStream

```java
Employee_ e = new Employee_();

// mapStream allows processing of a stream.
Map<Integer, List<Employee>> map = queryDsl
    .from(e)
    .mapStream(stream -> stream.collect(groupingBy(Employee::getDepartmentId)));

// collect is a shorthand for mapStream.
Map<Integer, List<Employee>> map2 = queryDsl.from(e).collect(groupingBy(Employee::getDepartmentId));

// openStream returns a stream. You MUST close the stream explicitly.
try (Stream<Employee> stream = queryDsl.from(e).openStream()) {
    stream.forEach(employee -> {
        // do something
    });
}
```

These methods provide efficient processing for large result sets.

### Select Expression

#### Entity Selection

By default, the result entity type is the same as the type specified in the `from` method:

```java
Employee_ e = new Employee_();
Department_ d = new Department_();

List<Employee> list = queryDsl
    .from(e)
    .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
    .fetch();
```

The above query generates the following SQL statement:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NO, t0_.EMPLOYEE_NAME, t0_.MANAGER_ID,
t0_.HIREDATE, t0_.SALARY, t0_.DEPARTMENT_ID, t0_.ADDRESS_ID, t0_.VERSION
from EMPLOYEE t0_
inner join DEPARTMENT t1_ on (t0_.DEPARTMENT_ID = t1_.DEPARTMENT_ID)
```

To choose a joined entity type as the result entity type, use `project` or `select`:

```java
Employee_ e = new Employee_();
Department_ d = new Department_();

List<Department> list = queryDsl
    .from(e)
    .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
    .project(d)
    .fetch();
```

This query generates the following SQL:

```sql
select t1_.DEPARTMENT_ID, t1_.DEPARTMENT_NO, t1_.DEPARTMENT_NAME, t1_.LOCATION, t1_.VERSION
from EMPLOYEE t0_
inner join DEPARTMENT t1_ on (t0_.DEPARTMENT_ID = t1_.DEPARTMENT_ID)
```

:::{note}
The `project` method removes duplicate entities, while `select` does not.
If you call neither method, duplicates are removed by default.
:::

#### Multiple Entity Selection

Specify multiple entity types and fetch them as tuples:

```java
Employee_ e = new Employee_();
Department_ d = new Department_();

List<Tuple2<Department, Employee>> list = queryDsl
    .from(d)
    .leftJoin(e, on -> on.eq(d.departmentId, e.departmentId))
    .where(c -> c.eq(d.departmentId, 4))
    .select(d, e)
    .fetch();
```

This query generates:

```sql
select t0_.DEPARTMENT_ID, t0_.DEPARTMENT_NO, t0_.DEPARTMENT_NAME, t0_.LOCATION,
t0_.VERSION, t1_.EMPLOYEE_ID, t1_.EMPLOYEE_NO, t1_.EMPLOYEE_NAME, t1_.MANAGER_ID,
t1_.HIREDATE, t1_.SALARY, t1_.DEPARTMENT_ID, t1_.ADDRESS_ID, t1_.VERSION
from DEPARTMENT t0_ left outer join EMPLOYEE t1_ on (t0_.DEPARTMENT_ID = t1_.DEPARTMENT_ID)
where t0_.DEPARTMENT_ID = ?
```

In the tuple, an entity is null if all its properties are null.

:::{note}
The `select` method does not remove duplicates.
:::

#### Column Projection

To project columns, use `select`. For one column:

```java
Employee_ e = new Employee_();

List<String> list = queryDsl.from(e).select(e.employeeName).fetch();
```

This generates:

```sql
select t0_.EMPLOYEE_NAME from EMPLOYEE t0_
```

For multiple columns:

```java
Employee_ e = new Employee_();

List<Tuple2<String, Integer>> list = queryDsl
    .from(e)
    .select(e.employeeName, e.employeeNo)
    .fetch();
```

This generates:

```sql
select t0_.EMPLOYEE_NAME, t0_.EMPLOYEE_NO from EMPLOYEE t0_
```

Columns up to 9 are held in `Tuple2` to `Tuple9`. Beyond that, they are held in `Row`.

Use `selectAsRow` for a `Row` list:

```java
Employee_ e = new Employee_();

List<Row> list = queryDsl.from(e).selectAsRow(e.employeeName, e.employeeNo).fetch();
```

#### Column Projection and Mapping

To project columns and map them to an entity, use the `projectTo` or `selectTo` methods:

```java
Employee_ e = new Employee_();

List<Employee> list = queryDsl.from(e).selectTo(e, e.employeeName).fetch();
```

This query generates:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NAME from EMPLOYEE t0_
```

Note that the SQL select clause includes the primary key "EMPLOYEE_ID". The `projectTo` and `selectTo` methods always include the entity's ID properties, even if they aren't explicitly specified.

:::{note}
The `projectTo` method removes duplicate entity IDs from the results, while `selectTo` does not.
:::

(query-dsl-where)=

### Where Expression

The following operators and predicates are supported:

- eq - (=)
- ne - (\<>)
- ge - (>=)
- gt - (>)
- le - (\<=)
- lt - (\<)
- isNull - (is null)
- isNotNull - (is not null)
- like
- notLike - (not like)
- between
- in
- notIn - (not in)
- exists
- notExists - (not exists)

:::{note}
If the right-hand operand is `null`, the WHERE or HAVING clause will exclude the operator. See [WhereDeclaration] and [HavingDeclaration] javadoc for details.
:::

We also support utility operators:

- eqOrIsNull - ("=" or "is null")
- neOrIsNotNull - ("\<>" or "is not null")

Additionally, the following logical operators are supported:

- and
- or
- not

```java
Employee_ e = new Employee_();

List<Employee> list = queryDsl
    .from(e)
    .where(c -> {
        c.eq(e.departmentId, 2);
        c.isNotNull(e.managerId);
        c.or(() -> {
            c.gt(e.salary, new Salary("1000"));
            c.lt(e.salary, new Salary("2000"));
        });
    })
    .fetch();
```

This generates:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NO, t0_.EMPLOYEE_NAME, t0_.MANAGER_ID, t0_.HIREDATE,
t0_.SALARY, t0_.DEPARTMENT_ID, t0_.ADDRESS_ID, t0_.VERSION
from EMPLOYEE t0_
where t0_.DEPARTMENT_ID = ? and t0_.MANAGER_ID is not null or (t0_.SALARY > ? and t0_.SALARY < ?)
```

Subqueries can be written as follows:

```java
Employee_ e = new Employee_();
Employee_ e2 = new Employee_();

List<Employee> list = queryDsl
    .from(e)
    .where(c -> c.in(e.employeeId, c.from(e2).select(e2.managerId)))
    .orderBy(c -> c.asc(e.employeeId))
    .fetch();
```

The above query generates:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NO, t0_.EMPLOYEE_NAME, t0_.MANAGER_ID, t0_.HIREDATE,
t0_.SALARY, t0_.DEPARTMENT_ID, t0_.ADDRESS_ID, t0_.VERSION
from EMPLOYEE t0_
where t0_.EMPLOYEE_ID in (select t1_.MANAGER_ID from EMPLOYEE t1_)
order by t0_.EMPLOYEE_ID asc
```

#### Dynamic Where Expression

A WHERE expression uses only evaluated operators to build a WHERE clause. When no operators are evaluated in the expression, the statement omits the WHERE clause.

For example, with a conditional expression:

```java
Employee_ e = new Employee_();

List<Employee> list = queryDsl
    .from(e)
    .where(c -> {
        c.eq(e.departmentId, 1);
        if (enableNameCondition) {
            c.like(e.employeeName, name);
        }
    })
    .fetch();
```

If `enableNameCondition` is `false`, the `like` expression is ignored, generating:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NO, t0_.EMPLOYEE_NAME, t0_.MANAGER_ID, t0_.HIREDATE,
t0_.SALARY, t0_.DEPARTMENT_ID, t0_.ADDRESS_ID, t0_.VERSION
from EMPLOYEE t0_ where t0_.DEPARTMENT_ID = ?
```

### Join Expression

We support the following join expressions:

- innerJoin - (inner join)
- leftJoin - (left outer join)

Example for innerJoin:

```java
Employee_ e = new Employee_();
Department_ d = new Department_();

List<Employee> list = queryDsl
    .from(e)
    .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
    .fetch();
```

This generates:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NO, t0_.EMPLOYEE_NAME, t0_.MANAGER_ID, t0_.HIREDATE,
t0_.SALARY, t0_.DEPARTMENT_ID, t0_.ADDRESS_ID, t0_.VERSION
from EMPLOYEE t0_
inner join DEPARTMENT t1_ on (t0_.DEPARTMENT_ID = t1_.DEPARTMENT_ID)
```

Example for leftJoin:

```java
Employee_ e = new Employee_();
Department_ d = new Department_();

List<Employee> list = queryDsl
    .from(e)
    .leftJoin(d, on -> on.eq(e.departmentId, d.departmentId))
    .fetch();
```

This generates:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NO, t0_.EMPLOYEE_NAME, t0_.MANAGER_ID, t0_.HIREDATE,
t0_.SALARY, t0_.DEPARTMENT_ID, t0_.ADDRESS_ID, t0_.VERSION
from EMPLOYEE t0_
left outer join DEPARTMENT t1_ on (t0_.DEPARTMENT_ID = t1_.DEPARTMENT_ID)
```

(query-dsl-associate)=
#### Association

You can associate entities using the `associate` operation in conjunction with a join expression:

```java
Employee_ e = new Employee_();
Department_ d = new Department_();

List<Employee> list = queryDsl
    .from(e)
    .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
    .where(c -> c.eq(d.departmentName, "SALES"))
    .associate(
        e,
        d,
        (employee, department) -> {
          employee.setDepartment(department);
          department.getEmployeeList().add(employee);
        })
    .fetch();
```

This query generates:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NO, t0_.EMPLOYEE_NAME, t0_.MANAGER_ID,
t0_.HIREDATE, t0_.SALARY, t0_.DEPARTMENT_ID, t0_.ADDRESS_ID, t0_.VERSION,
t1_.DEPARTMENT_ID, t1_.DEPARTMENT_NO, t1_.DEPARTMENT_NAME, t1_.LOCATION, t1_.VERSION
from EMPLOYEE t0_ inner join DEPARTMENT t1_ on (t0_.DEPARTMENT_ID = t1_.DEPARTMENT_ID)
where t1_.DEPARTMENT_NAME = ?
```

Associating Multiple Entities:

```java
Employee_ e = new Employee_();
Department_ d = new Department_();
Address_ a = new Address_();

List<Employee> list = queryDsl
    .from(e)
    .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
    .innerJoin(a, on -> on.eq(e.addressId, a.addressId))
    .where(c -> c.eq(d.departmentName, "SALES"))
    .associate(
        e,
        d,
        (employee, department) -> {
          employee.setDepartment(department);
          department.getEmployeeList().add(employee);
        })
    .associate(e, a, Employee::setAddress)
    .fetch();
```

#### Associating Immutable Entities

To associate immutable entities, use the `associateWith` operation with a join expression:

```java
Emp_ e = new Emp_();
Emp_ m = new Emp_();
Dept_ d = new Dept_();

List<Emp> list = queryDsl
    .from(e)
    .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
    .leftJoin(m, on -> on.eq(e.managerId, m.employeeId))
    .where(c -> c.eq(d.departmentName, "SALES"))
    .associateWith(e, d, Emp::withDept)
    .associateWith(e, m, Emp::withManager)
    .fetch();
```

This query generates:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NO, t0_.EMPLOYEE_NAME, t0_.MANAGER_ID, t0_.HIREDATE,
t0_.SALARY, t0_.DEPARTMENT_ID, t0_.ADDRESS_ID, t0_.VERSION,
t1_.DEPARTMENT_ID, t1_.DEPARTMENT_NO, t1_.DEPARTMENT_NAME, t1_.LOCATION, t1_.VERSION,
t2_.EMPLOYEE_ID, t2_.EMPLOYEE_NO, t2_.EMPLOYEE_NAME, t2_.MANAGER_ID, t2_.HIREDATE,
t2_.SALARY, t2_.DEPARTMENT_ID, t2_.ADDRESS_ID, t2_.VERSION
from EMPLOYEE t0_
inner join DEPARTMENT t1_ on (t0_.DEPARTMENT_ID = t1_.DEPARTMENT_ID)
left outer join EMPLOYEE t2_ on (t0_.MANAGER_ID = t2_.EMPLOYEE_ID)
where t1_.DEPARTMENT_NAME = ?
```

#### Dynamic Join Expression

A join expression uses only evaluated operators to build a JOIN clause. When no operators are evaluated, the JOIN clause is omitted.

For example, with a conditional join:

```java
Employee_ e = new Employee_();
Employee_ e2 = new Employee_();

List<Employee> list = queryDsl
    .from(e)
    .innerJoin(e2, on -> {
        if (join) {
            on.eq(e.managerId, e2.employeeId);
        }
    })
    .fetch();
```

If `join` is `false`, the `on` expression is ignored, generating:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NO, t0_.EMPLOYEE_NAME, t0_.MANAGER_ID, t0_.HIREDATE,
t0_.SALARY, t0_.DEPARTMENT_ID, t0_.ADDRESS_ID, t0_.VERSION
from EMPLOYEE t0_
```

#### Dynamic Association

With dynamic join expressions, associations can be made optional. Use `AssociationOption.optional()` in the `associate` method:

```java
Employee_ e = new Employee_();
Department_ d = new Department_();

List<Employee> list = queryDsl
    .from(e)
    .innerJoin(d, on -> {
        if (join) {
            on.eq(e.departmentId, d.departmentId);
        }
    })
    .associate(
        e,
        d,
        (employee, department) -> {
          employee.setDepartment(department);
          department.getEmployeeList().add(employee);
        },
        AssociationOption.optional())
    .fetch();
```

### Aggregate Functions

The following aggregate functions are supported:

- avg(property)
- avgAsDouble(property)
- count()
- count(property)
- countDistinct(property)
- max(property)
- min(property)
- sum(property)

These functions are defined in the `org.seasar.doma.jdbc.criteria.expression.Expressions` class and can be used with static imports.

For example, to pass the `sum` function to the select method:

```java
Employee_ e = new Employee_();

Salary salary = queryDsl.from(e).select(sum(e.salary)).fetchOne();
```

This generates:

```sql
select sum(t0_.SALARY) from EMPLOYEE t0_
```

### Group By Expression

Group by expressions allow for grouping results based on specified columns:

```java
Employee_ e = new Employee_();

List<Tuple2<Integer, Long>> list = queryDsl
    .from(e)
    .groupBy(e.departmentId)
    .select(e.departmentId, count())
    .fetch();
```

The above code generates:

```sql
select t0_.DEPARTMENT_ID, count(*) from EMPLOYEE t0_ group by t0_.DEPARTMENT_ID
```

When a group by expression is not specified, the expression is inferred from the select expression automatically. Thus, the following code issues the same SQL as above:

```java
Employee_ e = new Employee_();

List<Tuple2<Integer, Long>> list = queryDsl.from(e).select(e.departmentId, count()).fetch();
```

### Having Expression

The following operators are supported in having expressions:

- eq - (=)
- ne - (\<>)
- ge - (>=)
- gt - (>)
- le - (\<=)
- lt - (\<)

Logical operators are also supported:

- and
- or
- not

```java
Employee_ e = new Employee_();
Department_ d = new Department_();

List<Tuple2<Long, String>> list = queryDsl
    .from(e)
    .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
    .having(c -> c.gt(count(), 3L))
    .orderBy(c -> c.asc(count()))
    .select(count(), d.departmentName)
    .fetch();
```

The above query generates:

```sql
select count(*), t1_.DEPARTMENT_NAME
from EMPLOYEE t0_
inner join DEPARTMENT t1_ on (t0_.DEPARTMENT_ID = t1_.DEPARTMENT_ID)
group by t1_.DEPARTMENT_NAME having count(*) > ?
order by count(*) asc
```

#### Dynamic Having Expression

A having expression includes only evaluated operators, omitting the HAVING clause if no operators are evaluated.

For instance, a conditional expression in a having clause:

```java
Employee_ e = new Employee_();
Department_ d = new Department_();

List<Tuple2<Long, String>> list = queryDsl
    .from(e)
    .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
    .groupBy(d.departmentName)
    .having(c -> {
        if (countCondition) {
            c.gt(count(), 3L);
        }
    })
    .select(count(), d.departmentName)
    .fetch();
```

If `countCondition` is `false`, the `having` clause is ignored in the SQL statement.

### Order By Expression

Supported ordering operations are:

- asc
- desc

```java
Employee_ e = new Employee_();

List<Employee> list = queryDsl
    .from(e)
    .orderBy(c -> {
        c.asc(e.departmentId);
        c.desc(e.salary);
    })
    .fetch();
```

The query above generates:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NO, t0_.EMPLOYEE_NAME, t0_.MANAGER_ID, t0_.HIREDATE,
t0_.SALARY, t0_.DEPARTMENT_ID, t0_.ADDRESS_ID, t0_.VERSION
from EMPLOYEE t0_
order by t0_.DEPARTMENT_ID asc, t0_.SALARY desc
```

#### Dynamic Order By Expression

Order by expressions use only evaluated operators to build the ORDER BY clause. When no operators are evaluated, the ORDER BY clause is omitted.

### Distinct Expression

To select distinct rows, use `distinct()`:

```java
List<Department> list = queryDsl
    .from(d)
    .distinct()
    .leftJoin(e, on -> on.eq(d.departmentId, e.departmentId))
    .fetch();
```

This query generates:

```sql
select distinct t0_.DEPARTMENT_ID, t0_.DEPARTMENT_NO, t0_.DEPARTMENT_NAME,
t0_.LOCATION, t0_.VERSION
from DEPARTMENT t0_
left outer join EMPLOYEE t1_ on (t0_.DEPARTMENT_ID = t1_.DEPARTMENT_ID)
```

### Limit and Offset Expression

To limit the number of rows and specify an offset:

```java
Employee_ e = new Employee_();

List<Employee> list = queryDsl
    .from(e)
    .limit(5)
    .offset(3)
    .orderBy(c -> c.asc(e.employeeNo))
    .fetch();
```

This generates:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NO, t0_.EMPLOYEE_NAME, t0_.MANAGER_ID, t0_.HIREDATE,
t0_.SALARY, t0_.DEPARTMENT_ID, t0_.ADDRESS_ID, t0_.VERSION
from EMPLOYEE t0_
order by t0_.EMPLOYEE_NO asc
offset 3 rows fetch first 5 rows only
```

#### Dynamic Limit and Offset Expression

Limit and offset expressions include only non-null values in the SQL. If either value is null, the corresponding FETCH FIRST or OFFSET clause is omitted.

### For Update Expression

The `forUpdate` method allows row locking in SQL:

```java
Employee_ e = new Employee_();

List<Employee> list = queryDsl
    .from(e)
    .where(c -> c.eq(e.employeeId, 1))
    .forUpdate()
    .fetch();
```

The query above generates:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NO, t0_.EMPLOYEE_NAME, t0_.MANAGER_ID, t0_.HIREDATE,
t0_.SALARY, t0_.DEPARTMENT_ID, t0_.ADDRESS_ID, t0_.VERSION
from EMPLOYEE t0_
where t0_.EMPLOYEE_ID = ?
for update
```

### Union Expression

Supported union operations include:

- union
- unionAll - (union all)

```java
Employee_ e = new Employee_();
Department_ d = new Department_();

List<Tuple2<Integer, String>> list = queryDsl
    .from(e)
    .select(e.employeeId, e.employeeName)
    .union(queryDsl.from(d)
        .select(d.departmentId, d.departmentName))
    .fetch();
```

This generates:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NAME from EMPLOYEE t0_
union
select t0_.DEPARTMENT_ID, t0_.DEPARTMENT_NAME from DEPARTMENT t0_
```

Using order by with an index in union queries:

```java
List<Tuple2<Integer, String>> list = queryDsl
    .from(e)
    .select(e.employeeId, e.employeeName)
    .union(queryDsl.from(d)
        .select(d.departmentId, d.departmentName))
    .orderBy(c -> c.asc(2))
    .fetch();
```

### Derived Table Expression

Subqueries using derived tables are supported. A corresponding entity class for the derived table is required.

Define the entity class for the derived table as follows:

```java
@Entity(metamodel = @Metamodel)
public class NameAndAmount {
  private String name;
  private Integer amount;

  public NameAndAmount() {}

  public NameAndAmount(String accounting, BigDecimal bigDecimal) {
    this.name = accounting;
    this.amount = bigDecimal.intValue();
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public Integer getAmount() { return amount; }
  public void setAmount(Integer amount) { this.amount = amount; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NameAndAmount that = (NameAndAmount) o;
    return Objects.equals(name, that.name) && Objects.equals(amount, that.amount);
  }

  @Override
  public int hashCode() { return Objects.hash(name, amount); }
}
```

A subquery using a derived table can be written as follows:

```java
Department_ d = new Department_();
Employee_ e = new Employee_();
NameAndAmount_ t = new NameAndAmount_();

SetOperand<?> subquery = queryDsl
    .from(e)
    .innerJoin(d, c -> c.eq(e.departmentId, d.departmentId))
    .groupBy(d.departmentName)
    .select(d.departmentName, Expressions.sum(e.salary));

List<NameAndAmount> list = queryDsl
    .from(t, subquery)
    .orderBy(c -> c.asc(t.name))
    .fetch();
```

This generates:

```sql
select
    t0_.NAME,
    t0_.AMOUNT
from
    (
        select
            t2_.DEPARTMENT_NAME AS NAME,
            sum(t1_.SALARY) AS AMOUNT
        from
            EMPLOYEE t1_
        inner join
            DEPARTMENT t2_ on (t1_.DEPARTMENT_ID = t2_.DEPARTMENT_ID)
        group by
            t2_.DEPARTMENT_NAME
    ) t0_
order by
    t0_.NAME asc
```

### Common Table Expression

Common Table Expressions (CTEs) are supported.
To use a CTE, a corresponding entity class must be defined.

Define the entity class for the CTE as follows:

```java
@Entity(metamodel = @Metamodel)
public record AverageSalary(Salary salary) {}
```

A query using the CTE can be written as follows:

```java
var a = new AverageSalary_();
var e = new Employee_();

var cteQuery =
    dsl.from(e)
        .select(Expressions.avg(e.salary));

var list =
    dsl.with(a, cteQuery)
        .from(e)
        .innerJoin(a, on -> on.ge(e.salary, a.salary))
        .select(e.employeeId, e.employeeName, e.salary)
        .fetch();
```

The above query generates the following SQL:

```sql
with AVERAGE_SALARY(SALARY) as (
    select
        avg(t0_.SALARY)
    from
        EMPLOYEE t0_
)
select
    t0_.EMPLOYEE_ID,
    t0_.EMPLOYEE_NAME,
    t0_.SALARY from EMPLOYEE t0_
inner join
    AVERAGE_SALARY t1_ on (t0_.SALARY >= t1_.SALARY)
```

## Delete Statement

The delete statement follows the same rules as the [Where Expression](#where-expression).

### Delete Settings

The following settings are supported:

- allowEmptyWhere
- batchSize
- comment
- ignoreVersion
- queryTimeout
- sqlLogType
- suppressOptimisticLockException

All are optional and can be applied as follows:

```java
Employee_ e = new Employee_();

int count = queryDsl.delete(e, settings -> {
  settings.setAllowEmptyWhere(true);
  settings.setBatchSize(20);
  settings.setComment("delete all");
  settings.setIgnoreVersion(true);
  settings.setQueryTimeout(1000);
  settings.setSqlLogType(SqlLogType.RAW);
  settings.setSuppressOptimisticLockException(true);
})
.where(c -> {})
.execute();
```

:::{note}
To allow a delete statement with an empty WHERE clause, enable the `allowEmptyWhere` setting.
:::

### Delete Record by Entity

```java
Employee_ e = new Employee_();

Employee employee = queryDsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();

Result<Employee> result = queryDsl.delete(e).single(employee).execute();
```

This generates:

```sql
delete from EMPLOYEE where EMPLOYEE_ID = ? and VERSION = ?
```

Batch Delete is also supported:

```java
List<Employee> employees = queryDsl.from(e).where(c -> c.in(e.employeeId, Arrays.asList(5, 6))).fetch();

BatchResult<Employee> result = queryDsl.delete(e).batch(employees).execute();
```

Exceptions thrown by the execute method include:

- OptimisticLockException: if the entity has a version property and an update count is 0

### Delete Record by Entity and Retrieve the Deleted Record

By calling the `returning` method, you can delete an entity and retrieve the deleted entity at the same time:

```java
Department result = queryDsl.delete(d).single(department).returning().fetchOne();
```

This generates the following SQL:

```sql
delete from DEPARTMENT where DEPARTMENT_ID = ? and VERSION = ?
returning DEPARTMENT_ID, DEPARTMENT_NO, DEPARTMENT_NAME, LOCATION, VERSION
```

You can also specify which properties to return in the `returning` method.

To receive the result as an `Optional`, use the `fetchOptional` method instead of `fetchOne`.

:::{note}
Only H2 Database, PostgreSQL, SQL Server, and SQLite Dialects support this feature.
:::

### Delete Records by Where Expression

To delete by a condition:

```java
int count = queryDsl.delete(e).where(c -> c.ge(e.salary, new Salary("2000"))).execute();
```

This generates:

```sql
delete from EMPLOYEE t0_ where t0_.SALARY >= ?
```

To delete all records, use the `all` method:

```java
int count = queryDsl.delete(e).all().execute();
```

## Insert Statement

If a unique constraint violation occurs during the execution of an insert statement,
a `UniqueConstraintException` will be thrown.

### Insert Settings

Supported insert settings include:

- comment
- queryTimeout
- sqlLogType
- batchSize
- excludeNull
- include
- exclude
- ignoreGeneratedKeys

All are optional and can be applied as follows:

```java
Department_ d = new Department_();

int count = queryDsl.insert(d, settings -> {
    settings.setComment("insert department");
    settings.setQueryTimeout(1000);
    settings.setSqlLogType(SqlLogType.RAW);
    settings.setBatchSize(20);
    settings.excludeNull(true);
})
.values(c -> {
    c.value(d.departmentId, 99);
    c.value(d.departmentNo, 99);
    c.value(d.departmentName, "aaa");
    c.value(d.location, "bbb");
    c.value(d.version, 1);
})
.execute();
```

You can specify excluded columns:

```java
Department department = ...;

Result<Department> result = queryDsl.insert(d, settings ->
    settings.exclude(d.departmentName, d.location)
).single(department).execute();
```

### Insert Record with Entity

#### single

Inserting a single entity:

```java
Department department = new Department();
department.setDepartmentId(99);
department.setDepartmentNo(99);
department.setDepartmentName("aaa");
department.setLocation("bbb");

Result<Department> result = queryDsl.insert(d).single(department).execute();
```

This generates:

```sql
insert into DEPARTMENT (DEPARTMENT_ID, DEPARTMENT_NO, DEPARTMENT_NAME, LOCATION, VERSION)
values (?, ?, ?, ?, ?)
```

Functionality equivalent to `INSERT ... ON CONFLICT` is supported.

Use the `onDuplicateKeyUpdate` method to update the existing record when a duplicate key is found:

```java
Result<Department> = queryDsl
    .insert(d)
    .single(department)
    .onDuplicateKeyUpdate()
    .execute();
```

Use the `onDuplicateKeyIgnore` method when you want to do nothing in case of a duplicate key:

```java
Result<Department> = queryDsl
    .insert(d)
    .single(department)
    .onDuplicateKeyIgnore()
    .execute();
```

#### batch

Batch Insert is also supported:

```java
Department department = ...;
Department department2 = ...;
List<Department> departments = Arrays.asList(department, department2);

BatchResult<Department> result = queryDsl.insert(d).batch(departments).execute();
```

Functionality equivalent to `INSERT ... ON CONFLICT` is supported.

Use the `onDuplicateKeyUpdate` method to update existing records when duplicate keys are found:

```java
BatchResult<Department> = queryDsl
    .insert(d)
    .batch(departments)
    .onDuplicateKeyUpdate()
    .execute();
```

Use the `onDuplicateKeyIgnore` method to skip the insert operation when a duplicate key is found:

```java
BatchResult<Department> = queryDsl
    .insert(d)
    .batch(departments)
    .onDuplicateKeyIgnore()
    .execute();
```

#### multi

Multi-row Insert is also supported:

```java
MultiResult<Department> result = queryDsl.insert(d).multi(departments).execute();
```

This generates:

```sql
insert into DEPARTMENT (DEPARTMENT_ID, DEPARTMENT_NO, DEPARTMENT_NAME, LOCATION, VERSION)
values (?, ?, ?, ?, ?), (?, ?, ?, ?, ?)
```

Functionality equivalent to `INSERT ... ON CONFLICT` is supported.

Use the `onDuplicateKeyUpdate` method to update existing records when duplicate keys are found:

```java
MultiResult<Department> = queryDsl
    .insert(d)
    .multi(departments)
    .onDuplicateKeyUpdate()
    .execute();
```

Use the `onDuplicateKeyIgnore` method to skip insert operations when duplicate keys are found:

```java
MultiResult<Department> = queryDsl
    .insert(d)
    .multi(departments)
    .onDuplicateKeyIgnore()
    .execute();
```

### Insert Record with Entity and Retrieve the Inserted Record

By calling the `returning` method, you can insert an entity and retrieve the inserted entity at the same time:

```java
Department result = queryDsl.insert(d).single(department).returning().fetchOne();
```

This generates the following SQL:

```sql
insert into DEPARTMENT (DEPARTMENT_ID, DEPARTMENT_NO, DEPARTMENT_NAME, LOCATION, VERSION)
values (?, ?, ?, ?, ?) returning DEPARTMENT_ID, DEPARTMENT_NO, DEPARTMENT_NAME, LOCATION, VERSION
```

You can also specify which properties to return in the `returning` method.

To receive the result as an `Optional`, use the `fetchOptional` method instead of `fetchOne`.

The `returning` method is also supported for multi-row inserts.
In that case, the `fetch` method returns a List of inserted entities:

```java
List<Department> results = queryDsl.insert(d).multi(departmentList).returning().fetch();
```

:::{note}
Only H2 Database, PostgreSQL, SQL Server, and SQLite Dialects support this feature.
:::

### Insert Record with Specified Values

Inserting records by specifying values:

```java
int count = queryDsl.insert(d)
    .values(c -> {
        c.value(d.departmentId, 99);
        c.value(d.departmentNo, 99);
        c.value(d.departmentName, "aaa");
        c.value(d.location, "bbb");
        c.value(d.version, 1);
    })
    .execute();
```

This generates:

```sql
insert into DEPARTMENT (DEPARTMENT_ID, DEPARTMENT_NO, DEPARTMENT_NAME, LOCATION, VERSION)
values (?, ?, ?, ?, ?)
```

We also support the INSERT SELECT syntax:

```java
Department_ da = new Department_("DEPARTMENT_ARCHIVE");
Department_ d = new Department_();

int count = queryDsl.insert(da)
    .select(c -> c.from(d).where(cc -> cc.in(d.departmentId, Arrays.asList(1, 2))))
    .execute();
```

This generates:

```sql
insert into DEPARTMENT_ARCHIVE (DEPARTMENT_ID, DEPARTMENT_NO, DEPARTMENT_NAME,
LOCATION, VERSION) select t0_.DEPARTMENT_ID, t0_.DEPARTMENT_NO, t0_.DEPARTMENT_NAME,
t0_.LOCATION, t0_.VERSION from DEPARTMENT t0_ where t0_.DEPARTMENT_ID in (?, ?)
```

Functionality equivalent to `INSERT ... ON CONFLICT` is supported.

Use the `onDuplicateKeyUpdate` method when you want to perform an update in case of a duplicate key:

```java
int count = queryDsl
    .insert(d)
    .values(c -> {
        c.value(d.departmentId, 1);
        c.value(d.departmentNo, 60);
        c.value(d.departmentName, "DEVELOPMENT");
        c.value(d.location, "KYOTO");
        c.value(d.version, 2);
    })
    .onDuplicateKeyUpdate()
    .keys(d.departmentId)
    .set(c -> {
        c.value(d.departmentName, c.excluded(d.departmentName));
        c.value(d.location, "KYOTO");
        c.value(d.version, 3);
    })
    .execute();
```

Use the `onDuplicateKeyIgnore` method to skip insert operations when duplicate keys are found:

```java
int count = queryDsl
    .insert(d)
    .values(c -> {
        c.value(d.departmentId, 1);
        c.value(d.departmentNo, 60);
        c.value(d.departmentName, "DEVELOPMENT");
        c.value(d.location, "KYOTO");
        c.value(d.version, 2);
    })
    .onDuplicateKeyIgnore()
    .keys(d.departmentId)
    .execute();
```

## Update Statement

If a unique constraint violation occurs during the execution of an update statement,
a `UniqueConstraintException` will be thrown.

The update statement follows the same specifications as the [Where Expression](#where-expression).

### Update Settings

The following settings are supported:

- allowEmptyWhere
- batchSize
- comment
- ignoreVersion
- queryTimeout
- sqlLogType
- suppressOptimisticLockException
- excludeNull
- include
- exclude

All are optional and can be applied as follows:

```java
Employee_ e = new Employee_();

int count = queryDsl.update(e, settings -> {
  settings.setAllowEmptyWhere(true);
  settings.setBatchSize(20);
  settings.setComment("update all");
  settings.setIgnoreVersion(true);
  settings.setQueryTimeout(1000);
  settings.setSqlLogType(SqlLogType.RAW);
  settings.setSuppressOptimisticLockException(true);
  settings.excludeNull(true);
}).set(c -> {
  c.value(e.employeeName, "aaa");
}).execute();
```

You can also specify excluded columns:

```java
Employee employee = ...;

Result<Employee> result = queryDsl.update(e, settings ->
    settings.exclude(e.hiredate, e.salary)
).single(employee).execute();
```

:::{note}
To perform an update without a WHERE clause, enable the `allowEmptyWhere` setting.
:::

### Update Record by Entity

Updating a single entity:

```java
Employee employee = queryDsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
employee.setEmployeeName("aaa");
employee.setSalary(new Salary("2000"));

Result<Employee> result = queryDsl.update(e).single(employee).execute();
```

This generates:

```sql
update EMPLOYEE set EMPLOYEE_NAME = ?, SALARY = ?, VERSION = ? + 1
where EMPLOYEE_ID = ? and VERSION = ?
```

Batch Update is also supported:

```java
Employee employee = ...;
Employee employee2 = ...;
List<Employee> employees = Arrays.asList(employee, employee2);

BatchResult<Employee> result = queryDsl.update(e).batch(employees).execute();
```

Exceptions from the execute method may include:

- OptimisticLockException: if the entity has a version property and the update count is 0

### Update Record by Entity and Retrieve the Updated Record

By calling the `returning` method, you can update an entity and retrieve the updated entity at the same time:

```java
Department result = queryDsl.update(d).single(department).returning().fetchOne();
```

This generates the following SQL:

```sql
update DEPARTMENT set DEPARTMENT_NO = ?, DEPARTMENT_NAME = ?, LOCATION = ?, VERSION = ? + 1
where DEPARTMENT_ID = ? and VERSION = ?
returning DEPARTMENT_ID, DEPARTMENT_NO, DEPARTMENT_NAME, LOCATION, VERSION
```

You can also specify which properties to return in the `returning` method.

To receive the result as an `Optional`, use the `fetchOptional` method instead of `fetchOne`.

:::{note}
Only H2 Database, PostgreSQL, SQL Server, and SQLite Dialects support this feature.
:::

### Update Records by Where Expression

To update records based on a condition:

```java
int count = queryDsl.update(e)
    .set(c -> c.value(e.departmentId, 3))
    .where(c -> {
        c.isNotNull(e.managerId);
        c.ge(e.salary, new Salary("2000"));
    })
    .execute();
```

This generates:

```sql
update EMPLOYEE t0_ set t0_.DEPARTMENT_ID = ?
where t0_.MANAGER_ID is not null and t0_.SALARY >= ?
```

## Property Expressions

All property expression methods are in the `org.seasar.doma.jdbc.criteria.expression.Expressions` class and can be used with static imports.

### Arithmetic Expressions

The following methods are available for arithmetic expressions:

- add - (+)
- sub - (-)
- mul - (\*)
- div - (/)
- mod - (%)

Example of using the `add` method:

```java
int count = queryDsl.update(e)
    .set(c -> c.value(e.version, add(e.version, 10)))
    .where(c -> c.eq(e.employeeId, 1))
    .execute();
```

This generates:

```sql
update EMPLOYEE t0_
set t0_.VERSION = (t0_.VERSION + ?)
where t0_.EMPLOYEE_ID = ?
```

### String Functions

The following string functions are provided:

- concat
- lower
- upper
- trim
- ltrim
- rtrim

Example using `concat`:

```java
int count = queryDsl.update(e)
    .set(c -> c.value(e.employeeName, concat("[", concat(e.employeeName, "]"))))
    .where(c -> c.eq(e.employeeId, 1))
    .execute();
```

This generates:

```sql
update EMPLOYEE t0_
set t0_.EMPLOYEE_NAME = concat(?, concat(t0_.EMPLOYEE_NAME, ?))
where t0_.EMPLOYEE_ID = ?
```

### Literal Expression

The `literal` method supports all basic data types.

Example of using `literal`:

```java
Employee employee = queryDsl.from(e)
    .where(c -> c.eq(e.employeeId, literal(1)))
    .fetchOne();
```

This generates:

```sql
select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NO, t0_.EMPLOYEE_NAME, t0_.MANAGER_ID, t0_.HIREDATE,
t0_.SALARY, t0_.DEPARTMENT_ID, t0_.ADDRESS_ID, t0_.VERSION
from EMPLOYEE t0_
where t0_.EMPLOYEE_ID = 1
```

:::{note}
Note that literal expressions are directly embedded in the SQL rather than being treated as bind variables.
:::

### Case Expression

The following method is supported for case expressions:

- when

Example of using `when`:

```java
List<String> list = queryDsl
    .from(e)
    .select(
        when(c -> {
            c.eq(e.employeeName, literal("SMITH"), lower(e.employeeName));
            c.eq(e.employeeName, literal("KING"), lower(e.employeeName));
        }, literal("_")))
    .fetch();
```

This generates:

```sql
select case
        when t0_.EMPLOYEE_NAME = 'SMITH' then lower(t0_.EMPLOYEE_NAME)
        when t0_.EMPLOYEE_NAME = 'KING' then lower(t0_.EMPLOYEE_NAME)
        else '_' end
from EMPLOYEE t0_
```

### Subquery Select Expression

The `select` method supports subquery select expressions.

Example usage:

```java
Employee_ e = new Employee_();
Employee_ e2 = new Employee_();
Department_ d = new Department_();

SelectExpression<Salary> subSelect = select(c ->
    c.from(e2)
     .innerJoin(d, on -> on.eq(e2.departmentId, d.departmentId))
     .where(cc -> cc.eq(e.departmentId, d.departmentId))
     .groupBy(d.departmentId)
     .select(max(e2.salary))
);

int count = queryDsl.update(e)
    .set(c -> c.value(e.salary, subSelect))
    .where(c -> c.eq(e.employeeId, 1))
    .execute();
```

This generates:

```sql
update EMPLOYEE t0_
set t0_.SALARY = (
    select max(t1_.SALARY)
    from EMPLOYEE t1_
    inner join DEPARTMENT t2_ on (t1_.DEPARTMENT_ID = t2_.DEPARTMENT_ID)
    where t0_.DEPARTMENT_ID = t2_.DEPARTMENT_ID
    group by t2_.DEPARTMENT_ID
)
where t0_.EMPLOYEE_ID = ?
```

### User-Defined Expressions

You can define user-defined expressions using `Expressions.userDefined`.

Example of defining a custom `replace` function:

```java
UserDefinedExpression<String> replace(PropertyMetamodel<String> expression, PropertyMetamodel<String> from, PropertyMetamodel<String> to) {
    return Expressions.userDefined(expression, "replace", from, to, c -> {
        c.appendSql("replace(");
        c.appendExpression(expression);
        c.appendSql(", ");
        c.appendExpression(from);
        c.appendSql(", ");
        c.appendExpression(to);
        c.appendSql(")");
    });
}
```

Using the custom `replace` function in a query:

```java
List<String> list = queryDsl
    .from(d)
    .select(replace(d.location, Expressions.literal("NEW"), Expressions.literal("new")))
    .fetch();
```

This generates:

```sql
select replace(t0_.LOCATION, 'NEW', 'new') from DEPARTMENT t0_
```

## Scopes

Scopes allow you to specify commonly used query conditions.

To define a scope, create a class with a method annotated with `@Scope`:

```java
public class DepartmentScope {
    @Scope
    public Consumer<WhereDeclaration> onlyTokyo(Department_ d) {
        return c -> c.eq(d.location, "Tokyo");
    }
}
```

To enable the scope, specify the scope class in the `scopes` element of `@Metamodel`:

```java
@Entity(metamodel = @Metamodel(scopes = { DepartmentScope.class }))
public class Department { ... }
```

Now `Department_` includes the `onlyTokyo` method, which can be used as follows:

```java
List<Department> list = queryDsl.from(d).where(d.onlyTokyo()).fetch();
```

This generates:

```sql
select t0_.DEPARTMENT_ID, t0_.DEPARTMENT_NO, t0_.DEPARTMENT_NAME, t0_.LOCATION, t0_.VERSION from DEPARTMENT t0_
where t0_.LOCATION = ?
```

To combine other query conditions with scopes, use the `andThen` method:

```java
List<Department> list = queryDsl
    .from(d)
    .where(d.onlyTokyo().andThen(c -> c.gt(d.departmentNo, 50)))
    .fetch();
```

Defining multiple scopes within a class:

```java
public class DepartmentScope {
    @Scope
    public Consumer<WhereDeclaration> onlyTokyo(Department_ d) {
        return c -> c.eq(d.location, "Tokyo");
    }

    @Scope
    public Consumer<WhereDeclaration> locationStartsWith(Department_ d, String prefix) {
        return c -> c.like(d.location, prefix, LikeOption.prefix());
    }

    @Scope
    public Consumer<OrderByNameDeclaration> sortByNo(Department_ d) {
        return c -> c.asc(d.departmentNo);
    }
}
```

## Tips

### Execution in DAO

It can be useful to execute DSLs within a default method of the DAO interface.
To obtain a `config` object, call `Config.get(this)` within the default method:

```java
@Dao
public interface EmployeeDao {

  default Optional<Employee> selectById(Integer id) {
    QueryDsl queryDsl = new QueryDsl(Config.get(this));

    Employee_ e = new Employee_();
    return queryDsl.from(e).where(c -> c.eq(e.employeeId, id)).fetchOptional();
  }
}
```

You can also use `QueryDsl.of(this)` as a shortcut for `new QueryDsl(Config.get(this))`.

```java
@Dao
public interface EmployeeDao {

  default Optional<Employee> selectById(Integer id) {
    Employee_ e = new Employee_();
    return QueryDsl.of(this).from(e).where(c -> c.eq(e.employeeId, id)).fetchOptional();
  }
}
```

### Overwriting the Table Name

A metamodel constructor can accept a qualified table name, which allows the metamodel to overwrite its default table name.

This feature is useful for working with two tables that share the same structure:

```java
Department_ da = new Department_("DEPARTMENT_ARCHIVE");
Department_ d = new Department_();

int count = queryDsl
    .insert(da)
    .select(c -> c.from(d))
    .execute();
```

This generates:

```sql
insert into DEPARTMENT_ARCHIVE (DEPARTMENT_ID, DEPARTMENT_NO, DEPARTMENT_NAME,
LOCATION, VERSION) select t0_.DEPARTMENT_ID, t0_.DEPARTMENT_NO, t0_.DEPARTMENT_NAME,
t0_.LOCATION, t0_.VERSION from DEPARTMENT t0_
```

### User-Defined Operators

User-defined operators can be implemented as methods in a class that accepts a `UserDefinedCriteriaContext` instance via its constructor.

```java
record MyExtension(UserDefinedCriteriaContext context) {
  public void regexp(PropertyMetamodel<String> propertyMetamodel, String regexp) {
    context.add(
        (b) -> {
          b.appendExpression(propertyMetamodel);
          b.appendSql(" ~ ");
          b.appendParameter(propertyMetamodel, regexp);
        });
  }
}
```

The class above can be used in the WHERE, JOIN, or HAVING clause of a query as follows:

```java
var d = new Department_();
var list =
    queryDsl.from(d)
        .where(c -> c.extension(MyExtension::new, (ext) -> {
            ext.regexp(d.departmentName, "A");
        }))
        .orderBy(c -> c.asc(d.departmentId))
        .select()
        .fetch();
```

The query above is translated into the following SQL:

```sql
select t0_.DEPARTMENT_ID, t0_.DEPARTMENT_NO, t0_.DEPARTMENT_NAME, t0_.LOCATION, t0_.VERSION from DEPARTMENT t0_
where t0_.DEPARTMENT_NAME ~ ?
order by t0_.DEPARTMENT_ID asc
```

## Debugging

To inspect the SQL statement generated by DSLs, use the `asSql` method:

```java
Department_ d = new Department_();

Listable<Department> stmt = queryDsl.from(d).where(c -> c.eq(d.departmentName, "SALES"));

Sql<?> sql = stmt.asSql();
System.out.printf("Raw SQL      : %s\n", sql.getRawSql());
System.out.printf("Formatted SQL: %s\n", sql.getFormattedSql());
```

The code above outputs the following:

```sh
Raw SQL      : select t0_.DEPARTMENT_ID, t0_.DEPARTMENT_NO, t0_.DEPARTMENT_NAME, t0_.LOCATION, t0_.VERSION from DEPARTMENT t0_ where t0_.DEPARTMENT_NAME = ?
Formatted SQL: select t0_.DEPARTMENT_ID, t0_.DEPARTMENT_NO, t0_.DEPARTMENT_NAME, t0_.LOCATION, t0_.VERSION from DEPARTMENT t0_ where t0_.DEPARTMENT_NAME = 'SALES'
```

The `asSql` method does not execute the SQL statement against the database; instead, it only constructs the SQL statement and returns it as an `Sql` object.

You can also obtain the `Sql` object by using the `peek` method:

```java
List<String> locations = queryDsl
    .from(d)
    .peek(System.out::println)
    .where(c -> c.eq(d.departmentName, "SALES"))
    .peek(System.out::println)
    .orderBy(c -> c.asc(d.location))
    .peek(sql -> System.out.println(sql.getFormattedSql()))
    .select(d.location)
    .peek(sql -> System.out.println(sql.getFormattedSql()))
    .fetch();
```

The code above outputs SQL statements at various stages of the query:

```sql
select t0_.DEPARTMENT_ID, t0_.DEPARTMENT_NO, t0_.DEPARTMENT_NAME, t0_.LOCATION, t0_.VERSION from DEPARTMENT t0_
select t0_.DEPARTMENT_ID, t0_.DEPARTMENT_NO, t0_.DEPARTMENT_NAME, t0_.LOCATION, t0_.VERSION from DEPARTMENT t0_ where t0_.DEPARTMENT_NAME = ?
select t0_.DEPARTMENT_ID, t0_.DEPARTMENT_NO, t0_.DEPARTMENT_NAME, t0_.LOCATION, t0_.VERSION from DEPARTMENT t0_ where t0_.DEPARTMENT_NAME = 'SALES' order by t0_.LOCATION asc
select t0_.LOCATION from DEPARTMENT t0_ where t0_.DEPARTMENT_NAME = 'SALES' order by t0_.LOCATION asc
```

## Sample Projects

You can refer to the following sample projects for additional guidance:

- [simple-examples](https://github.com/domaframework/simple-examples)
- [kotlin-sample](https://github.com/domaframework/kotlin-sample)

[havingdeclaration]: https://www.javadoc.io/doc/org.seasar.doma/doma-core/latest/org/seasar/doma/jdbc/criteria/declaration/HavingDeclaration.html
[wheredeclaration]: https://www.javadoc.io/doc/org.seasar.doma/doma-core/latest/org/seasar/doma/jdbc/criteria/declaration/WhereDeclaration.html
