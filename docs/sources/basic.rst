==================
基本型
==================

.. contents:: 目次
   :depth: 3

Domaでは、データベースのカラムにマッピング可能なJavaの型を基本型と呼びます。

種類
==================

基本型には以下の種類があります。

* プリミティブ型とそのラッパー型（ただし ``char`` と ``java.lang.Character`` は除く）
* 列挙型
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

日付/時刻型
------------------

日付と時刻の型の違いについて説明します。

java.time.LocalDate
  SQL標準のDATE型 (日付のみ)を表します。

java.time.LocalTime
  SQL標準のTIME型 (時刻のみ)を表します。

java.time.LocalDateTime
  SQL標準のTIMESTAMP型 (日付と時刻)を表します。RDBMSがサポートしている場合ナノ秒を保持します。

java.sql.Date
  SQL標準のDATE型 (日付のみ)を表します。

java.sql.Time
  SQL標準のTIME型 (時刻のみ)を表します。

java.sql.Timestamp
  SQL標準のTIMESTAMP型 (日付と時刻)を表します。RDBMSがサポートしている場合ナノ秒を保持します。

java.util.Date
  SQL標準のTIMESTAMP型 (日付と時刻)を表します。ナノ秒を保持しません。

利用例
==================

エンティティクラス
------------------

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


ドメインクラス
------------------

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

Daoクラス
------------------

.. code-block:: java

  @Dao(config = AppConfig.class)
  public interface EmployeeDao {

      @Select
      Employee selectById(Integer employeeId);

      @Select
      List<String> selectAllName();
  }
