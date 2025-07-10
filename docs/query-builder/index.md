==============
# Query builders

.. contents::
   :depth: 3

The package `org.seasar.doma.jdbc.builder` provides SQL builders.

When it is difficult to build a SQL statement with [\1](\1) or [\1](\1),
consider to use the SQL builders in [\1](\1).

# Search

```java

  SelectBuilder builder = SelectBuilder.newInstance(config);
  builder.sql("select");
  builder.sql("id").sql(",");
  builder.sql("name").sql(",");
  builder.sql("salary");
  builder.sql("from Emp");
  builder.sql("where");
  builder.sql("job_type = ").literal(String.class, "fulltime");
  builder.sql("and");
  builder.sql("name like ").param(String.class, "S%");
  builder.sql("and");
  builder.sql("age in (").params(Integer.class, Arrays.asList(20, 30, 40)).sql(")");
  List<Emp> employees = builder.getEntityResultList(Emp.class);

You can get result of the SQL execution in various ways.

## Single record search

* getScalarSingleResult
* getOptionalScalarSingleResult
* getEntitySingleResult
* getOptionalEntitySingleResult
* getMapSingleResult
* getOptionalMapSingleResult

## Multiple records search

* getScalarResultList
* getOptionalScalarResultList
* getEntityResultList
* getMapResultList

## Stream search

* streamAsScalar
* streamAsOptionalScalar
* streamAsEntity
* streamAsMap

# Insert

```java

  InsertBuilder builder = InsertBuilder.newInstance(config);
  builder.sql("insert into Emp");
  builder.sql("(name, salary)");
  builder.sql("values (");
  builder.param(String.class, "SMITH").sql(", ");
  builder.param(BigDecimal.class, new BigDecimal(1000)).sql(")");
  builder.execute();

# Update

```java

  UpdateBuilder builder = UpdateBuilder.newInstance(config);
  builder.sql("update Emp");
  builder.sql("set");
  builder.sql("name = ").param(String.class, "SMIHT").sql(",");
  builder.sql("salary = ").param(BigDecimal.class, new BigDecimal("1000"));
  builder.sql("where");
  builder.sql("id = ").param(int.class, 10);
  builder.execute();

# Delete

```java

  DeleteBuilder builder = DeleteBuilder.newInstance(config);
  builder.sql("delete from Emp");
  builder.sql("where");
  builder.sql("name = ").param(String.class, "SMITH");
  builder.sql("and");
  builder.sql("salary = ").param(BigDecimal.class, new BigDecimal(1000));
  builder.execute();
