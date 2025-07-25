# Embeddable classes

```{contents}
:depth: 4
```

Embeddable classes group properties for entity classes.

## Embeddable definition

Here is an example of how to define an embeddable class:

```java
@Embeddable
public class Address {

    final String city;

    final String street;

    @Column(name = "ZIP_CODE")
    final String zip;

    public Address(String city, String street, String zip) {
        this.city = city;
        this.street = street;
        this.zip = zip;
    }
}
```

You can apply the `@Embeddable` annotation to both classes and records:

```java
@Embeddable
public record Address(
  String city,
  String street,
  @Column(name = "ZIP_CODE")String zip) {
}
```

The embeddable class is used as the entity field type:

```java
@Entity
public class Employee {
    @Id
    Integer id;

    Address address;
}
```

The above entity definition is equivalent to following one:

```java
@Entity
public class Employee {
    @Id
    Integer id;

    String city;

    String street;

    @Column(name = "ZIP_CODE")
    String zip;
}
```

### Naming convention

The naming convention is inherited from the enclosing entity class.

## Field definition

By default, all fields are persistent and correspond to database columns or result set columns.

The field type must be one of the following:

- [Basic classes](basic.md)
- [Domain classes](domain.md)
- [Embeddable classes](embeddable.md) (nested embeddables)
- java.util.Optional, whose element is either [Basic classes](basic.md), [Domain classes](domain.md), or [Embeddable classes](embeddable.md)
- java.util.OptionalInt
- java.util.OptionalLong
- java.util.OptionalDouble

```java
@Embeddable
public class Address {
    String street;
}
```

### Column

You can specify the corresponding column name with the `@Column` annotation:

```java
@Column(name = "ZIP_CODE")
final String zip;
```

### Transient

If an embeddable has fields that you don't want to persist, you can annotate them using `@Transient`:

### Nested embeddable classes

Embeddable classes can contain other embeddable classes as fields, allowing for nested composition:

```java
@Embeddable
public class Address {
    String street;
    String city;
    String zipCode;
}

@Embeddable
public class ContactInfo {
    String email;
    String phone;
    Address address; // Nested embeddable
}

@Entity
public class Customer {
    @Id
    Integer id;
    String name;
    ContactInfo contactInfo; // Contains nested Address
}
```

This creates a hierarchical structure where the `Customer` entity contains `ContactInfo`, which in turn contains `Address`. 
All fields from nested embeddables are flattened into the entity's table structure.

### Optional embeddable classes

Embeddable classes can be wrapped in `java.util.Optional` to indicate that the entire embeddable group may be null:

```java
@Entity
public class Employee {
    @Id
    Integer id;
    String name;
    Optional<Address> homeAddress;    // Optional embeddable
    Optional<ContactInfo> emergencyContact; // Optional nested embeddable
}
```

When an Optional embeddable is null, all corresponding database columns will be null. 
Conversely, when all columns corresponding to an Optional embeddable are null in the database, the Optional field will be empty (Optional.empty()). 

## Method definition

There are no limitations in the use of methods.

## Using @Embedded annotation

You can use the `@Embedded` annotation to embed the same embeddable type multiple times within a single entity with different column name prefixes.

### Basic usage

```java
@Embeddable
public record Address(String street, String city, String zipCode) {}

@Entity(naming = NamingType.SNAKE_LOWER_CASE)
public class Customer {
    @Id
    Integer customerId;

    @Embedded(prefix = "billing_")
    Address billingAddress;

    @Embedded(prefix = "shipping_")
    Address shippingAddress;
}
```

This will generate the following columns in the SQL statements:

- `billing_street`
- `billing_city`
- `billing_zip_code`
- `shipping_street`
- `shipping_city`
- `shipping_zip_code`

### Prefix behavior

The `prefix` attribute controls how column names are generated:

- Column names are generated by combining the prefix with the embeddable field column name
- The prefix is added as-is to the field column name
- If no prefix is specified, the behavior remains the same as using the embeddable field column name directly

```java
@Entity(naming = NamingType.SNAKE_LOWER_CASE)
public class Order {
    @Id
    Integer orderId;

    // Without prefix - generates columns: street, city, zip_code
    Address address;

    // With prefix - generates columns: delivery_street, delivery_city, delivery_zip_code
    @Embedded(prefix = "delivery_")
    Address deliveryAddress;
}
```

### Column overrides

You can use the `columnOverrides` attribute along with `@ColumnOverride` annotations to have fine-grained control over individual column mappings:

```java
@Entity
public class Customer {
    @Id
    Integer id;

    @Embedded(columnOverrides = {
        @ColumnOverride(name = "street", column = @Column(name = "BILLING_STREET")),
        @ColumnOverride(name = "city", column = @Column(name = "BILLING_CITY"))
    })
    Address billingAddress;

    @Embedded(columnOverrides = {
        @ColumnOverride(name = "street", column = @Column(name = "SHIP_STREET", insertable = false)),
        @ColumnOverride(name = "city", column = @Column(name = "SHIP_CITY", updatable = false))
    })
    Address shippingAddress;
}
```

The `@ColumnOverride` annotation allows you to:

- Specify a custom column name for a specific embeddable field
- Override column attributes such as `insertable`, `updatable`, and `quote`
- Take precedence over any `prefix` attribute when both are specified

:::{note}
When both `prefix` and `columnOverrides` are used, the `@ColumnOverride` settings take precedence for the specified fields.
:::

## Example

```java
void doSomething() {
    Employee employee = new Employee(); // Entity
    Address address = new Address("Tokyo", "Yaesu", "103-0028"); // Embeddable
    employee.setAddress(address);
}
```
