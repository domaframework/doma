==============
# Domain classes

.. contents::
   :depth: 4

A domain class represents a table column and allows you to handle column values as Java objects.
In the Doma framework, a **domain** refers to all the values that a data type may contain.
In essence, a domain class is a user-defined class that can be mapped to a database column.
Using domain classes is optional but recommended for type safety.

Every domain class is either an internal domain class or an external domain class.

# Internal domain classes

Internal domain classes must be annotated with `@Domain`.
The `valueType` element of the `@Domain` annotation specifies the data type of the corresponding database column.
You must specify a type from [\1](\1) for the `valueType` element.

## Instantiation with a constructor

The default value of the `factoryMethod` element in the `@Domain` annotation is `new`.
The value `new` indicates that instances of the annotated class will be created using a constructor.

```java

  @Domain(valueType = String.class)
  public class PhoneNumber {

      private final String value;

      public PhoneNumber(String value) {
          this.value = value;
      }

      public String getValue() {
          return value;
      }

      public String getAreaCode() {
         ...
      }
  }

```{note}

  You can use `@DataType` instead of `@Domain` for records.
  The information corresponding to the `valueType` element of `@Domain`
  is resolved from the type of the constructor parameter.

```java

    @DataType
    public record PhoneNumber(String value) {
      public String getAreaCode() {
        ...
      }
    }


## Instantiation with a static factory method

To create instances using a static factory method,
specify the method name in the `factoryMethod` element of the `@Domain` annotation.

The method must be static and non-private:

```java

  @Domain(valueType = String.class, factoryMethod = "of")
  public class PhoneNumber {

      private final String value;

      private PhoneNumber(String value) {
          this.value = value;
      }

      public String getValue() {
          return value;
      }

      public String getAreaCode() {
         ...
      }

      public static PhoneNumber of(String value) {
          return new PhoneNumber(value);
      }
  }

With a static factory method, you can apply the `@Domain` annotation to enum types:

```java

  @Domain(valueType = String.class, factoryMethod = "of")
  public enum JobType {
      SALESMAN("10"),
      MANAGER("20"),
      ANALYST("30"),
      PRESIDENT("40"),
      CLERK("50");

      private final String value;

      private JobType(String value) {
          this.value = value;
      }

      public static JobType of(String value) {
          for (JobType jobType : JobType.values()) {
              if (jobType.value.equals(value)) {
                  return jobType;
              }
          }
          throw new IllegalArgumentException(value);
      }

      public String getValue() {
          return value;
      }
  }

## Using type parameters in internal domain classes

Internal domain classes can include type parameters as shown below:

```java

  @Domain(valueType = int.class)
  public class Identity<T> {

      private final int value;

      public Identity(int value) {
          this.value = value;
      }

      public int getValue() {
          return value;
      }
  }


When creating instances using a static factory method,
the method declaration must include the same type parameters as those declared in the class:

```java

  @Domain(valueType = int.class, factoryMethod = "of")
  public class Identity<T> {

      private final int value;

      private Identity(int value) {
          this.value = value;
      }

      public int getValue() {
          return value;
      }

      public static <T> Identity<T> of(int value) {
          return new Identity<T>(value);
      }
  }

# External domain classes

This feature allows you to define any class as a domain class,
even if you cannot annotate the class with the `@Domain` annotation.

To define external domain classes, you must create a class that implements
`org.seasar.doma.jdbc.domain.DomainConverter` and annotate it with `@ExternalDomain`.

Consider, for example, the following `PhoneNumber` class that you cannot modify directly:

```java

  public class PhoneNumber {

      private final String value;

      public PhoneNumber(String value) {
          this.value = value;
      }

      public String getValue() {
          return value;
      }

      public String getAreaCode() {
         ...
      }
  }

To define the `PhoneNumber` class as an external domain class, create the following converter class:

```java

  @ExternalDomain
  public class PhoneNumberConverter implements DomainConverter<PhoneNumber, String> {

      public String fromDomainToValue(PhoneNumber domain) {
          return domain.getValue();
      }

      public PhoneNumber fromValueToDomain(String value) {
          if (value == null) {
              return null;
          }
          return new PhoneNumber(value);
      }
  }

## Using type parameters in external domain classes

External domain classes can also use type parameters, as shown below:

```java

  public class Identity<T> {

      private final int value;

      public Identity(int value) {
          this.value = value;
      }

      public int getValue() {
          return value;
      }
  }

In the `DomainConverter` implementation class,
use a wildcard `?` as the type argument when referring to the external domain class:

```java

  @ExternalDomain
  public class IdentityConverter implements DomainConverter<Identity<?>, String> {

      public String fromDomainToValue(Identity<?> domain) {
          return domain.getValue();
      }

      @SuppressWarnings("rawtypes")
      public Identity<?> fromValueToDomain(String value) {
          if (value == null) {
              return null;
          }
          return new Identity(value);
      }
  }

## Direct mapping of external domain classes to the database

All external domain classes can be directly mapped to any database type.

Here's an example of mapping `java.util.UUID` to PostgreSQL's UUID type.

First, create an implementation of `org.seasar.doma.jdbc.type.JdbcType` to handle the mapping:

```java

    class PostgresUUIDJdbcType extends AbstractJdbcType<UUID> {

      protected PostgresUUIDJdbcType() {
        super(Types.OTHER);
      }

      @Override
      protected UUID doGetValue(ResultSet resultSet, int index) throws SQLException {
        String value = resultSet.getString(index);
        return value == null ? null : UUID.fromString(value);
      }

      @Override
      protected void doSetValue(PreparedStatement preparedStatement, int index, UUID value)
          throws SQLException {
        preparedStatement.setObject(index, value, type);
      }

      @Override
      protected UUID doGetValue(CallableStatement callableStatement, int index) throws SQLException {
        String value = callableStatement.getString(index);
        return value == null ? null : UUID.fromString(value);
      }

      @Override
      protected String doConvertToLogFormat(UUID value) {
        return value.toString();
      }
    }

Then, create a class that extends `org.seasar.doma.it.domain.JdbcTypeProvider`.
In the `getJdbcType` method, return an instance of the `JdbcType` implementation created above:

```java

    @ExternalDomain
    public class PostgresUUIDConverter extends JdbcTypeProvider<UUID> {

      private static final PostgresUUIDJdbcType jdbcType = new PostgresUUIDJdbcType();

      @Override
      public JdbcType<UUID> getJdbcType() {
        return jdbcType;
      }
    }

Remember to annotate this class with `@ExternalDomain`.

# Example

The Domain classes shown above are used as follows:

```java

  @Entity
  public class Employee {

      @Id
      Identity<Employee> employeeId;

      String employeeName;

      PhoneNumber phoneNumber;

      JobType jobType;

      @Version
      Integer versionNo;

      ...
  }

```java

  @Dao
  public interface EmployeeDao {

      @Select
      Employee selectById(Identity<Employee> employeeId);

      @Select
      Employee selectByPhoneNumber(PhoneNumber phoneNumber);

      @Select
      List<PhoneNumber> selectAllPhoneNumber();

      @Select
      Employee selectByJobType(JobType jobType);

      @Select
      List<JobType> selectAllJobTypes();
  }
