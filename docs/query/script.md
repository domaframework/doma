# Script

```{contents}
:depth: 4
```

To run a series of static SQL statements,
annotate DAO methods with `@Script`:

```java
@Dao
public interface EmployeeDao {
    @Script
    void createTable();
    ...
}
```

The return type of the method must be `void` and the number of parameters must be zero.

## Script representation

### Scripts in files

#### Encoding

The script files must be saved as UTF-8 encoded.

#### Location

The script files must be located in directories below a “META-INF” directory
which is included in CLASSPATH.

#### Format of file path

The script file path must follow the following format:

> META-INF/*path-format-of-dao-interface*/*dao-method*.script

For example, when the DAO interface name is `aaa.bbb.EmployeeDao`
and the DAO method name is `createTable`, the script file path is as follows:

> META-INF/aaa/bbb/EmployeeDao/createTable.script

#### Dependency on a specific RDBMS

You can specify a dependency on a specific RDBMS by file name.
To do this, put the hyphen "-" and RDBMS name before the extension ".sql".
For example, the file path specific to PostgreSQL is as follows:

> META-INF/aaa/bbb/EmployeeDao/createTable-*postgres*.script

The script files specific to RDBMSs are given priority.
For example, in the environment where PostgreSQL is used,
"META-INF/aaa/bbb/EmployeeDao/createTable-postgres.script"
is chosen instead of "META-INF/aaa/bbb/EmployeeDao/createTable.script".

See also [](../sql.md#dependency-on-a-specific-rdbms).

### Scripts in annotation

You can specify scripts to DAO methods with the `@Sql` annotation:

```java
@Dao
public interface EmployeeDao {
    @Sql("create table employee (id integer, name varchar(200))")
    @Script
    void createTable();
    ...
}
```

See also [](../sql.md#sql-templates-in-annotations).

## Delimiter

There are two kinds of delimiters in scripts:

- statement delimiter
- block delimiter

The statement delimiter is always a semicolon `;`.
The block delimiter is determined by a `Dialect` instance.
The RDBMS block delimiters are as follows:

| RDBMS                      | Dialect          | block delimiter |
| -------------------------- | ---------------- | --------------- |
| DB2                        | Db2Dialect       | @               |
| H2 Database Engine 1.2.126 | H212126Dialect   |                 |
| H2 Database                | H2Dialect        |                 |
| HSQLDB                     | HsqldbDialect    |                 |
| Microsoft SQL Server 2008  | Mssql2008Dialect | GO              |
| Microsoft SQL Server       | MssqlDialect     | GO              |
| MySQL                      | MySqlDialect     | /               |
| Oracle Database            | OracleDialect    | /               |
| PostgreSQL                 | PostgresDialect  | \$\$            |
| SQLite                     | SqliteDialect    |                 |

You can also specify the block delimiter to `@Script`'s `blockDelimiter` element:

```java
@Script(blockDelimiter = "GO")
void createProcedure();
```

The corresponding script file is as follows:

```sql
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[MY_PROCEDURE]
AS
BEGIN
    SET NOCOUNT ON;
END
GO
```

## Stopping on error

Script running will stop when any statement execution fails.
To continue the script running, specify `false` to the `haltOnError` element:

```java
@Script(haltOnError = false)
void createTable();
```

## Example

Following script is valid for Oracle Database:

```sql
/*
 * table creation statement
 */
create table EMPLOYEE (
  ID numeric(5) primary key,  -- identifier is not generated automatically
  NAME varchar2(20)           -- first name only
);

/*
 * insert statement
 */
insert into EMPLOYEE (ID, NAME) values (1, 'SMITH');

/*
 * procedure creation block
 */
create or replace procedure proc
( cur out sys_refcursor,
  employeeid in numeric
) as
begin
  open cur for select * from employee where id > employeeid order by id;
end proc_resultset;
/

/*
 * procedure creation block
 */
create or replace procedure proc2
( cur out sys_refcursor,
  employeeid in numeric
) as
begin
  open cur for select * from employee where id > employeeid order by id;
end proc_resultset;
/
```

You can use both a single line comment `--` and a multi-line comment `/* ... */`.
Each statement must end with a semicolon `;`.
Be careful that a new line doesn't mean the end of a statement.

In this example, the slash `/` is a block delimiter.
The block delimiter must appear at the beginning of a line and be followed by a new line.
