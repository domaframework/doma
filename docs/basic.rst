==================
基本型
==================


種類
==================

Optional型
------------------

日付/時刻型
------------------

利用例
==================

エンティティクラス
------------------

.. code-block:: java
  
  @Entity
  public class Employee {

      @Id
      Integer employeeId;

      String employeeName;

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
