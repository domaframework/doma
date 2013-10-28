# 基本型

## 目次

- [概要](#概要)
- [種類](#種類)
- [利用例](#利用例)
 
## 概要
Domaでは、データベースのカラムにマッピング可能なJavaの型を基本型と呼びます。

基本型は、ドメインクラスの値型やエンティティクラスの永続フィールドの型として使用されます。 また、Daoインタフェースのメソッドのパラメータや戻り値にも使用されます。

## 種類

基本型には以下の種類があります。

- プリミティブ型とそのラッパー型（ただし、charとjava.lang.Characterは除く）
- 列挙型
- byte[]
- java.lang.String
- java.math.BigDecimal
- java.math.BigInteger
- java.sql.Date
- java.sql.Time
- java.sql.Timestamp
- java.sql.Array
- java.sql.Blob
- java.sql.Clob
- java.sql.NClob
- java.util.Date

### 日付・時刻型

<dl>
    <dt>java.sql.Date</dt>
	<dd>SQL標準のDATE型 (日付のみ)を表す。</dd>
	<dt>java.sql.Time</dt>
	<dd>
	<dd>SQL標準のTIME型 (時刻のみ)を表す。</dd>
	<dt>java.sql.Timestamp</dt>
	<dd>SQL標準のTIMESTAMP型 (日付と時刻)を表す。RDBMSがサポートしている場合ナノ秒を保持します。</dd>
	<dt>java.util.Date</dt>
	<dd>SQL標準のTIMESTAMP型 (日付と時刻)を表す。ナノ秒を保持しません。</dd>
</dl>

## 利用例

### エンティティクラス

エンティティクラスのフィールドの型での利用例です。

```java
@Entity
public class Employee {

    @Id
    Integer employeeId;

    String employeeName;

    @Version
    Long versionNo;
    
    ...
}
```

### ドメインクラス

ドメインクラスでの利用例です。

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
}
```

### Daoインタフェース

```java
@Dao(config = AppConfig.class)
public interface EmployeeDao {

    @Select
    Employee selectById(Integer employeeId);
    
    @Select
    List<String> selectAllName();
}
```