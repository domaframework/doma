# SQL templates

```{contents}
:depth: 4
```

## Overview

Doma supports SQL templates known as "two-way SQL".
The term "two-way SQL" indicates that these templates can be used in two ways:

- Building dynamic SQL statements from the templates.
- Executing the templates directly in SQL tools without modification.

Every SQL template must correspond to a DAO method.
For example, suppose you have the pair of an SQL template and a DAO method as follows:

```sql
select * from employee where employee_id = /* employeeId */99
```

```java
Employee selectById(Integer employeeId);
```

The `employeeId` expression enclosed between `/*` and `*/` corresponds to
the method parameter "employeeId" of the DAO.
At runtime, the SQL comment and the following number `/* employeeId */99` are replaced with a bind variable `?`,
and the method parameter "employeeId" is bound to this variable.
The SQL statement generated from the SQL template is as follows:

```sql
select * from employee where employee_id = ?
```

The number `99` in the SQL template is test data that is never used at runtime.
This test data is only useful when you execute the SQL template directly in your database tools.
In other words, you can check whether the SQL template is grammatically correct with your favorite SQL tools.

Each SQL template is represented by either a text file or an annotation.

## SQL templates in files

You can specify SQL templates in text files:

```java
@Dao
public interface EmployeeDao {
  @Select
  Employee selectById(Integer employeeId);

  @Delete(sqlFile = true)
  int deleteByName(Employee employee);
}
```

The `selectById` and `deleteByName` methods above are mapped to their corresponding SQL files.
DAO methods must be annotated with one of the following annotations:

- @Select
- @Insert(sqlFile = true)
- @Update(sqlFile = true)
- @Delete(sqlFile = true)
- @BatchInsert(sqlFile = true)
- @BatchUpdate(sqlFile = true)
- @BatchDelete(sqlFile = true)

### Encoding

All SQL files must be saved with UTF-8 encoding.

### Location

The SQL files must be located in directories below a "META-INF" directory which is included in CLASSPATH.

### Format of file path

SQL file paths must follow this format:

> META-INF/*path-format-of-dao-interface*/*dao-method*.sql

For example, when the DAO interface name is `aaa.bbb.EmployeeDao` and the DAO method name is `selectById`,
the SQL file path is as follows:

> META-INF/aaa/bbb/EmployeeDao/selectById.sql

(dependency-on-a-specific-rdbms)=

#### Dependency on a specific RDBMS

You can specify dependency on a specific RDBMS by file name.
To do this, put the hyphen "-" and RDBMS name before the extension ".sql".
For example, the file path specific to PostgreSQL is as follows:

> META-INF/aaa/bbb/EmployeeDao/selectById-*postgres*.sql

RDBMS-specific SQL files take precedence over generic ones.
For example, in a PostgreSQL environment,
"META-INF/aaa/bbb/EmployeeDao/selectById-postgres.sql"
will be chosen instead of "META-INF/aaa/bbb/EmployeeDao/selectById.sql".

The RDBMS names are stem from dialects:

| RDBMS                | Dialect         | RDBMS Name |
| -------------------- | --------------- | ---------- |
| DB2                  | Db2Dialect      | db2        |
| H2 Database          | H2Dialect       | h2         |
| HSQLDB               | HsqldbDialect   | hsqldb     |
| Microsoft SQL Server | MssqlDialect    | mssql      |
| MySQL                | MySqlDialect    | mysql      |
| Oracle Database      | OracleDialect   | oracle     |
| PostgreSQL           | PostgresDialect | postgres   |
| SQLite               | SqliteDialect   | sqlite     |

(sql-templates-in-annotations)=

## SQL templates in annotations

You can specify SQL templates to DAO methods with the `@Sql` annotation:

```java
@Dao
public interface EmployeeDao {
  @Sql("select * from employee where employee_id = /* employeeId */99")
  @Select
  Employee selectById(Integer employeeId);

  @Sql("delete from employee where employee_name = /* employee.employeeName */'aaa'")
  @Delete
  int deleteByName(Employee employee);
}
```

The `@Sql` annotation must be combined with following annotations:

- @Select
- @Script
- @Insert
- @Update
- @Delete
- @BatchInsert
- @BatchUpdate
- @BatchDelete

(sql-directives)=

## Directives

In SQL templates, the SQL comments following the specific rules are recognised as directives.
Supported directives are as follows:

- [Bind Variable Directive](#bind-variable-directive)
- [Literal Variable Directive](#literal-variable-directive)
- [Embedded Variable Directive](#embedded-variable-directive)
- [Condition Directive](#condition-directive)
- [Loop Directive](#loop-directive)
- [Expansion Directive](#expansion-directive)
- [Population Directive](#population-directive)
- [Parser Level Comment Directive](#parser-level-comment-directive)

:::{note}
See also [Expression language](expression.md) for information of the expression language available in directives.
:::

(bind-variable-directive)=

### Bind variable directive

The bind variable directive is written in the format `/*...*/`.
The expression enclosed between `/*` and `*/` is evaluated, and
its result is passed to a bind variable in the SQL statement.
The directive must be followed by test data, which is never used at runtime.

#### Basic and domain parameters

The parameter whose type is one of [Basic classes](basic.md) and [Domain classes](domain.md)
is recognised as a bind variable.

The following example is the pair of a DAO method and an SQL template:

```java
Employee selectById(Integer employeeId);
```

```sql
select * from employee where employee_id = /* employeeId */99
```

The following SQL statement is generated from the SQL template:

```sql
select * from employee where employee_id = ?
```

#### Parameters in IN clause

Parameters that are either a subtype of `java.lang.Iterable` or an array type are
recognized as bind variables in an IN clause.

The type argument of `java.lang.Iterable` must be one of [Basic classes](basic.md) and [Domain classes](domain.md).
The directives must be followed by test data enclosed between `(` and `)`.

The following example is the pair of a DAO method and an SQL template:

```java
List<Employee> selectByIdList(List<Integer> employeeIdList);
```

```sql
select * from employee where employee_id in /* employeeIdList */(1,2,3)
```

In case that the `employeeIdList` contains five elements,
the following SQL statement is generated from the SQL template:

```sql
select * from employee where employee_id in (?, ?, ?, ?, ?)
```

In case that the `employeeIdList` is empty,
the IN clause is replaced with `in (null)` in runtime:

```sql
select * from employee where employee_id in (null)
```

### Literal variable directive

The literal variable directive is written in the format `/*^...*/`.
The expression enclosed between `/*^` and `*/` is evaluated, and
its result is converted to a literal format that is embedded directly in the SQL statement.
The directive must be followed by test data, which is never used at runtime.

The following example is the pair of a DAO method and an SQL template:

```java
Employee selectByCode(String code);
```

```sql
select * from employee where code = /*^ code */'test'
```

The DAO method is invoked as follows:

```java
EmployeeDao dao = new EmployeeDaoImpl();
List<Employee> list = dao.selectByCode("abc");
```

The generated SQL statement is as follows:

```sql
select * from employee where code = 'abc'
```

:::{note}
Literal variable directives are helpful to avoid bind variables and fix SQL plans.
:::

:::{warning}
Literal variable directives do not escape parameters for SQL injection.
But the directives reject parameters containing the single quotation `'`.
:::

### Embedded variable directive

The embedded variable directive is written in the format `/*#...*/`.
The expression enclosed between `/*#` and `*/` is evaluated, and
its result is embedded directly in the SQL statement.

The following example is the pair of a DAO method and an SQL template:

```java
List<Employee> selectAll(BigDecimal salary, String orderBy);
```

```sql
select * from employee where salary > /* salary */100 /*# orderBy */
```

The DAO method is invoked as follows:

```java
EmployeeDao dao = new EmployeeDaoImpl();
BigDecimal salary = new BigDecimal(1000);
String orderBy = "order by salary asc, employee_name";
List<Employee> list = dao.selectAll(salary, orderBy);
```

The generated SQL statement is as follows:

```sql
select * from employee where salary > ? order by salary asc, employee_name
```

:::{note}
Embedded variable directives are helpful to build SQL fragments such as ORDER BY clause.
:::

:::{warning}
To prevent SQL injection vulnerabilities,
embedded variable directives reject parameters containing any of the following characters or sequences:

- Single quotation mark `'`
- Semicolon `;`
- Double hyphen `--`
- Slash followed by an asterisk `/*`
:::

### Condition directive

Condition directive allows you to build SQL statements conditionally.

#### Synopsis

```sql
/*%if condition*/
  ...
/*%elseif condition2*/
  ...
/*%elseif condition3*/
  ...
/*%else*/
  ...
/*%end*/
```

The expressions `condition`, `condition2`, and `condition3` must evaluate
to either a primitive `boolean` or `java.lang.Boolean` object.

The `elseif` directives and the `else` directive are optional.

#### if

Suppose you have the following SQL template:

```sql
select * from employee where
/*%if employeeId != null */
    employee_id = /* employeeId */99
/*%end*/
```

If the `employeeId` is not `null`, the generated SQL statement is as follows:

```sql
select * from employee where employee_id = ?
```

If the `employeeId` is `null`, the generated SQL statement is as follows:

```sql
select * from employee
```

The SQL keyword `where` is removed automatically.

#### elseif and else

Suppose you have the following SQL template:

```sql
select
  *
from
  employee
where
/*%if employeeId != null */
  employee_id = /* employeeId */9999
/*%elseif department_id != null */
  and
  department_id = /* departmentId */99
/*%else*/
  and
  department_id is null
/*%end*/
```

If the `employeeId != null` is evaluated `true`, the generated SQL statement is as follows:

```sql
select
  *
from
  employee
where
  employee_id = ?
```

If the `employeeId == null && department_id != null` is evaluated `true`,
the generated SQL statement is as follows:

```sql
select
  *
from
  employee
where
  department_id = ?
```

The SQL keyword `and` followed by `department_id` is removed automatically:

If the `employeeId == null && department_id == null` is evaluated `true`,
the generated SQL statement is as follows:

```sql
select
  *
from
  employee
where
  department_id is null
```

The SQL keyword `and` followed by `department_id` is automatically removed.

#### Nested condition directive

You can nest condition directives as follows:

```sql
select * from employee where
/*%if employeeId != null */
  employee_id = /* employeeId */99
  /*%if employeeName != null */
    and
    employee_name = /* employeeName */'hoge'
  /*%else*/
    and
    employee_name is null
  /*%end*/
/*%end*/
```

#### Removal of clauses on the condition directive

The following clauses may become unnecessary when using condition directives:

- WHERE
- HAVING
- ORDER BY
- GROUP BY

In such cases, these clauses are automatically removed.

Suppose you have the following SQL template:

```sql
select * from employee where
/*%if employeeId != null */
    employee_id = /* employeeId */99
/*%end*/
```

If the `employeeId != null` is evaluated `false`,
the generated SQL statement is as follows:

```sql
select * from employee
```

Because the SQL clause `where` followed by `/*%if ...*/` is unnecessary,
it is removed automatically.

#### Removal of AND and OR keywords on the condition directives

AND and OR keywords may become unnecessary when using condition directives.
In such cases, these keywords are automatically removed.

Suppose you have the following SQL template:

```sql
select * from employee where
/*%if employeeId != null */
    employee_id = /* employeeId */99
/*%end*/
and employeeName like 's%'
```

If the `employeeId != null` is evaluated `false`,
the generated SQL statement is as follows:

```sql
select * from employee where employeeName like 's%'
```

Because the SQL keyword `and` following `/*%end*/` is unnecessary,
it is removed automatically.

#### Restriction on condition directive

The `/*%if condition*/` and `/*%end*/` directives must be included in
the same SQL clause and at the same statement level.

The following template is invalid, because `/*%if condition*/` is
in the FROM clause and `/*%end*/` is in the WHERE clause:

```sql
select * from employee /*%if employeeId != null */
where employee_id = /* employeeId */99 /*%end*/
```

The following template is invalid, because `/*%if condition*/` is
in the outer statement and `/*%end*/` is in the inner statement:

```sql
select * from employee
where employee_id in /*%if departmentId != null */(select ...  /*%end*/ ...)
```

### Loop directive

The loop directive allows you to build SQL statements using loops.

#### Synopsis

```sql
/*%for item : sequence*/
  ...
/*%end*/
```

Here, `item` is the loop variable.
The expression `sequence` must evaluate to a subtype of `java.lang.Iterable` or an array type.

Inside the loop (between `/*%for item : sequence*/` and `/*%end*/`),
two additional loop variables are available:

```{eval-rst}

:item_index: The index (0-based number) of the current item in the loop
:item_has_next: Boolean value that tells if the current item is the last in the sequence or not
```

The prefix `item` indicates the name of the loop variable.

#### for and item_has_next

Suppose you have the following SQL template:

```sql
select * from employee where
/*%for name : names */
employee_name like /* name */'hoge'
  /*%if name_has_next */
/*# "or" */
  /*%end */
/*%end*/
```

If the sequence `names` contains three items,
the generated SQL statement is as follows:

```sql
select * from employee where
employee_name like ?
or
employee_name like ?
or
employee_name like ?
```

#### Removal of clauses in the loop directive

The following clauses can become unnecessary in the loop directive:

- WHERE
- HAVING
- ORDER BY
- GROUP BY

In such cases, these clauses are removed automatically.

Suppose you have the following SQL template:

```sql
select * from employee where
/*%for name : names */
employee_name like /* name */'hoge'
  /*%if name_has_next */
/*# "or" */
  /*%end */
/*%end*/
```

If the sequence `names` is empty,
the generated SQL statement is as follows:

```sql
select * from employee
```

Because the SQL clause `where` followed by `/*%for ...*/` is unnecessary,
it is removed automatically.

#### Removal of AND and OR keywords in the loop directive

AND and OR keywords can become unnecessary in the loop directive.
In such cases, these keywords are removed automatically.

Suppose you have the following SQL template:

```sql
select * from employee where
/*%for name : names */
employee_name like /* name */'hoge'
  /*%if name_has_next */
/*# "or" */
  /*%end */
/*%end*/
or
salary > 1000
```

If the sequence `names` is empty,
the generated SQL statement is as follows:

```sql
select * from employee where salary > 1000
```

Because the SQL keyword `or` following `/*%end*/` is unnecessary,
it is removed automatically.

#### Restriction on loop directive

`/*%for ...*/` and `/*%end*/` must be included in
the same SQL clause and at the same statement level.

See also [Restriction on condition directive].

### Expansion directive

The expansion directive automatically generates a column list for the SELECT clause based on an entity definition.

#### Synopsis

```sql
/*%expand alias*/
```

The expression `alias` is optional.
When specified, it must evaluate to a `java.lang.String`.

The directive must be followed by an asterisk `*`.

(expand)=

#### expand

Suppose you have the following SQL template and the entity class mapped to the template:

```sql
select /*%expand*/* from employee
```

```java
@Entity
public class Employee {
    Integer id;
    String name;
    Integer age;
}
```

The generated SQL statement is as follows:

```sql
select id, name, age from employee
```

If you specify an alias to the table, specify same alias to the expansion directive:

```sql
select /*%expand "e" */* from employee e
```

The generated SQL statement is as follows:

```sql
select e.id, e.name, e.age from employee e
```

(populate)=

### Population directive

The population directive automatically generates a column list for
the UPDATE SET clause based on an entity definition.

#### Synopsis

```sql
/*%populate*/
```

#### populate

Suppose you have the following SQL template and the entity class mapped to the template:

```sql
update employee set /*%populate*/ id = id where age < 30
```

```java
@Entity
public class Employee {
    Integer id;
    String name;
    Integer age;
}
```

The generated SQL statement is as follows:

```sql
update employee set id = ?, name = ?, age = ? where age < 30
```

(parser-level-comment)=

### Parser-level comment directive

The parser-level comment directive allows you to include comments in an SQL template.
These comments will be removed when the template is parsed.

#### Synopsis

```sql
/*%! comment */
```

Suppose you have the following SQL template:

```sql
select
  *
from
  employee
where /*%! This comment will be removed */
  employee_id = /* employeeId */99
```

The above SQL template is parsed into the following SQL:

```sql
select
  *
from
  employee
where
  employee_id = ?
```

## Comments

This section explains how to distinguish between directives and normal SQL comments.

### Single line comment

A string beginning with two hyphens `--` is always treated as a single line comment,
never as a directive.

### Multi line comment

If the character following `/*` is not valid as the first character in a Java identifier
and is not one of `%`, `#`, `@`, `"`, or `'`,
then `/*` marks the beginning of a multi-line comment.

The following are examples of multi-line comment beginnings:

- /\*\*...\*/
- /\*+...\*/
- /\*=...\*/
- /\*:...\*/
- /\*;...\*/
- /\*(...\*/
- /\*)...\*/
- /\*&...\*/

On the other hand, the following are examples of directive beginnings:

- /\* ...\*/
- /\*a...\*/
- /\*\$...\*/
- /\*@...\*/
- /\*"...\*/
- /\*'...\*/
- /\*#...\*/
- /\*%...\*/

:::{note}
We recommend always using `/**...*/` for multi-line comments because it's straightforward and clearly distinguishable from directives.
:::

## doma-template module

The doma-template module helps obtain prepared SQL statements from SQL templates.
The module only contains the following classes:

- SqlArgument
- SqlStatement
- SqlTemplate

### Gradle

```kotlin
dependencies {
    implementation("org.seasar.doma:doma-template:{{ doma_version }}")
}
```

### Usage

```java
String sql = "select * from emp where name = /* name */'' and salary = /* salary */0";
SqlStatement statement =
    new SqlTemplate(sql)
        .add("name", String.class, "abc")
        .add("salary", int.class, 1234)
        .execute();
String rawSql = statement.getRawSql(); // select * from emp where name = ? and salary = ?
List<SqlArgument> arguments = statement.getArguments();
```
