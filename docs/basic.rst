=============
Basic classes
=============

.. contents::
   :depth: 3

Overview
========

The Java types can be mapped to database column types are called "Basic classes" in Doma.

List of basic classes
=====================

* primitive types except ``char``
* wrapper class for above primitive types
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
  represents SQL DATE

:java.time.LocalTime:
  represents SQL TIME

:java.time.LocalDateTime:
  represents SQL TIMESTAMP and may hold nanoseconds if RDBMS supports it

:java.sql.Date:
  represents SQL DATE

:java.sql.Time:
  represents SQL TIME

:java.sql.Timestamp:
  represents SQL TIMESTAMP and may hold nanoseconds if RDBMS supports it

:java.util.Date:
  represents SQL TIMESTAMP and doesn't hold nanoseconds

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

  @Dao(config = AppConfig.class)
  public interface EmployeeDao {

      @Select
      Employee selectById(Integer employeeId);

      @Select
      List<String> selectAllName();
  }
