=============================
# Aggregate strategies

.. contents::
   :depth: 4

The aggregate strategy defines how to construct an entity aggregate from an arbitrary SELECT statement.
It provides a structured way to map relational query results into hierarchical entity structures by specifying
how entities should be linked together.

# Aggregate strategy definition

An aggregate strategy is defined by annotating an interface with `@AggregateStrategy`.
This annotation specifies how an entity aggregate is reconstructed from a query result.

```java

    @AggregateStrategy(root = Department.class, tableAlias = "d")
    interface DepartmentAggregateStrategy {
        ...
    }

- The `root` element specifies the entity class that serves as the root of the aggregate.
- The `tableAlias` element defines the alias for the table corresponding to the root entity class.
  This alias must be used in the SELECT statement to correctly map query results to entity properties.

# Association linker definition

An aggregate strategy must contain at least one field of type `BiConsumer` or `BiFunction`,
annotated with `@AssociationLinker`.
These functions are responsible for dynamically associating two entity instances.
Use a `BiFunction` when associating immutable entities.
For mutable entities, you may use either a `BiConsumer` or a `BiFunction`.

```java

    @AggregateStrategy(root = Department.class, tableAlias = "d")
    interface DepartmentAggregateStrategy {
      @AssociationLinker(propertyPath = "employees", tableAlias = "e")
      BiConsumer<Department, Employee> employees =
          (d, e) -> {
            d.getEmployees().add(e);
            e.setDepartment(d);
          };

      @AssociationLinker(propertyPath = "employees.address", tableAlias = "a")
      BiFunction<Employee, Address, Employee> address =
          (e, a) -> {
            e.setAddress(a);
            return e;
          };
    }

- The first type parameter of a `BiConsumer` or `BiFunction` represents the type of the property owner,
  and the second type parameter represents the type of the property.
  The third type parameter of a `BiFunction` must be the same as the first one and represents the type of
  the entity after the association is applied.
- The `propertyPath` element specifies the name of the target property as a dot-separated path from the root entity class.
- The `tableAlias` element specifies the alias for the table corresponding to the entity class used as the second
  type parameter of the `BiConsumer` or `BiFunction`. This alias must be used in the SELECT statement.

# Example

The `DepartmentAggregateStrategy` described above is based on the following entity definitions:

```java

    @Entity(naming = NamingType.SNAKE_LOWER_CASE)
    public class Department {
      @Id Integer id;
      String name;
      @Association List<Employee> employees = new ArrayList<>();

      // getter, setter
    }

    @Entity(naming = NamingType.SNAKE_LOWER_CASE)
    public class Employee {
      @Id Integer id;
      String name;
      Integer departmentId;
      Integer addressId;
      @Association Department department;
      @Association Address address;

      // getter, setter
    }

    @Entity(naming = NamingType.SNAKE_LOWER_CASE)
    public class Address {
      @Id Integer id;
      String street;

      // getter, setter
    }

In entity classes, association properties must be annotated with `@Association`.
These properties can be linked using `@AssociationLinker`.

## Using an aggregate strategy

`DepartmentAggregateStrategy` is used by specifying it in the `aggregateStrategy` element of `@Select`:

```java

    @Dao
    interface DepartmentDao {
      @Select(aggregateStrategy = DepartmentAggregateStrategy.class)
      Department selectById(Integer id);
    }

The `selectById` method requires a SELECT statement like the following:

```sql

    select
        d.id as d_id,
        d.name as d_name,
        a.id as a_id,
        a.street as a_street,
        e.id as e_id,
        e.name as e_name,
        e.department_id as e_department_id,
        e.address_id as e_address_id
    from
        department d
        left outer join
        employee e on (d.id = e.department_id)
        left outer join
        address a on (e.address_id = a.id)
    where
        d.id = /* id */0

```{note}

    The SELECT list must include the IDs of all entities that form the aggregate.

### Column aliasing rules

- Table aliases must match those defined in `DepartmentAggregateStrategy`.
- Column aliases must begin with the table alias followed by an underscore (`_`).
  For example, `d.id` should be aliased as `d_id` and `e.id` as `e_id`.

## Using the expansion directive

By using the [\1](\1), the above SELECT statement can be written more concisely:

```sql

    select
        /*%expand*/*
    from
        department d
        left outer join
        employee e on (d.id = e.department_id)
        left outer join
        address a on (e.address_id = a.id)
    where
        d.id = /* id */0


### How expansion works

- The `/*%expand*/*` directive automatically expands into a column list following predefined aliasing rules.
- By default, all columns from all tables are included in the result set.

To selectively expand only specific tables, pass a comma-separated list of table aliases:

```sql

    select
        /*%expand "e, d" */*,
        a.id as a_id,
        a.street as a_street
    from
        department d
        left outer join
        employee e on (d.id = e.department_id)
        left outer join
        address a on (e.address_id = a.id)
    where
        d.id = /* id */0

- Here, only columns from tables `e` (`employee`) and `d` (`department`) are expanded.
- The columns from table `a` (`address`) are explicitly specified.

