==============
Domain classes
==============

.. contents::
   :depth: 3

A domain class represents a table column and it allows you to handle the column value as a Java object.
In the Doma framework, a **domain** means all the values which a data type may contain.
In short, a domain class is a user defined class that can be map to a column.
The use of the domain classes is optional.

Every domain class is either an internal domain class or an external domain class.

Internal domain classes
=======================

The internal domain class must be annotated with ``@Domain``.
The ``@Domain``'s ``valueType`` element corresponds to a data type of a column.
Specify any type of :doc:`basic` to the ``valueType`` element.

Instantiation with a constructor
--------------------------------

The default value of the ``@Domain``'s ``factoryMethod`` element is ``new``.
The value ``new`` means that the object of annotated class is created with a constructor.

.. code-block:: java

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

.. note::
  In Java 14 and later version, you can annotate `records`_ with ``@Domain``:

  .. code-block:: java

    @Domain(valueType = String.class, accessorMethod = "value")
    public record PhoneNumber(String value) {
      public String getAreaCode() {
        ...
      }
    }

.. _records: https://openjdk.java.net/jeps/359


Instantiation with a static factory method
------------------------------------------

To create the object of annotated class with a static factory method,
specify the method name to the ``@Domain``'s ``factoryMethod`` element.

The method must be static and non-private:

.. code-block:: java

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

With a static factory method, you can apply the ``@Domain`` annotation to enum types:

.. code-block:: java

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

Using type parameters in internal domain classes
------------------------------------------------

All internal domain class declarations have type parameters:

.. code-block:: java

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


When you create the object of annotated class with a static factory method,
the method declaration must have same type parameters that are declared in the class declaration:

.. code-block:: java

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

External domain classes
=======================

This feature allows you to define arbitrary classes as domain classes,
even if the classes can be annotated with the ``@Domain`` annotation.

To define external domain classes, you have to do as follows:

- Create a class that implements ``org.seasar.doma.jdbc.domain.DomainConverter`` and
  annotate ``@ExternalDomain`` to the class
- Create a class that is annotated with ``@DomainConverters``
- Specify the class annotated with ``@ExternalDomain`` to the ``@DomainConverters``'s ``value`` element
- Specify the full qualified name of the class annotated with ``@DomainConverters`` to
  the option of :doc:`annotation-processing`

Suppose, for instance, there is the ``PhoneNumber`` class that you can change:

.. code-block:: java

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

First, to define the ``PhoneNumber`` class as an external domain class, create following class:

.. code-block:: java

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

Then create following class and specify the above class to the ``@DomainConverters``'s ``value`` element:

.. code-block:: java

  @DomainConverters({ PhoneNumberConverter.class })
  public class DomainConvertersProvider {
  }

Finally, specify the full qualified name of the above class to the option of :doc:`annotation-processing`.
If you use Gradle, specify the option in the build script as follows:

.. code-block:: groovy

  compileJava {
      options {
          compilerArgs = ['-Adoma.domain.converters=example.DomainConvertersProvider']
      }
  }

Using type parameters in external domain classes
------------------------------------------------

All external domain class declarations have type parameters:

.. code-block:: java

  public class Identity<T> {

      private final int value;

      public Identity(int value) {
          this.value = value;
      }

      public int getValue() {
          return value;
      }
  }

In the ``DomainConverter`` implementation class,
specify a wildcard ``?`` as type arguments to the external domain class:

.. code-block:: java

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

Example
=======

The Domain classes showed above are used as follows:

.. code-block:: java

  @Entity
  public class Employee {

      @Id
      Identity<Employee> employeeId;

      String employeeName;

      PhoneNumber phoneNumber;

      JobType jobType;

      @Version
      Integer versionNo();

      ...
  }

.. code-block:: java

  @Dao(config = AppConfig.class)
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




















