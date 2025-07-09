=============
Basic classes
=============

.. contents::
   :depth: 4

Overview
========

In Doma, "Basic classes" are Java types that can be mapped directly to database column types.

List of basic classes
=====================

* primitive types except ``char``
* wrapper classes for the above primitive types
* enum types
* byte[]
* java.lang.String
* java.lang.Object
* java.math.BigDecimal
* java.math.BigInteger
* java.time.LocalDate
* java.time.LocalTime
* java.time.LocalDateTime
* java.sql.Date
* java.sql.Time
* java.sql.Timestamp
* java.sql.Array
* java.sql.Blob
* java.sql.Clob
* java.sql.SQLXML
* java.util.Date

Differences between temporal classes
------------------------------------

:java.time.LocalDate:
  Represents SQL DATE

:java.time.LocalTime:
  Represents SQL TIME

:java.time.LocalDateTime:
  Represents SQL TIMESTAMP and can store nanoseconds if the RDBMS supports it

:java.sql.Date:
  Represents SQL DATE

:java.sql.Time:
  Represents SQL TIME

:java.sql.Timestamp:
  Represents SQL TIMESTAMP and can store nanoseconds if the RDBMS supports it

:java.util.Date:
  Represents SQL TIMESTAMP but does not store nanoseconds

Examples
========

Using in entity class
---------------------

.. code-block:: java

  @Entity
  public class Employee {

      @Id
      Integer employeeId;

      Optional<String> employeeName;

      @Version
      Long versionNo;

      ...
  }


Using in domain class
---------------------

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
  }

Using in DAO interface
----------------------

.. code-block:: java

  @Dao
  public interface EmployeeDao {

      @Select
      Employee selectById(Integer employeeId);

      @Select
      List<String> selectAllName();
  }
