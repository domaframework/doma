# ドメインクラス

## 目次

- [概要](#概要)
- [ドメイン定義](#ドメイン定義)
- [外部ドメイン定義](#外部ドメイン定義)
- [利用例](#利用例)

## 概要

Domain（ドメイン）クラスの定義方法を示します。 ドメインとは、値のとり得る範囲（定義域）のことです。 Domaでは、テーブルのカラムの値を、ドメインと呼ばれるJavaオブジェクトで扱います。

ドメインクラスを利用することで、データベース上のカラムの型が同じであってもアプリケーション上意味が異なるものを別のJavaの型で表現できます。 これにより、意味を明確にしプログラミングミスを事前に防ぎやすくなります。 また、ドメインクラスに振る舞いを持たせることで、よりわかりやすいプログラミングが可能です。

ドメインクラスの作成と利用は任意です。 ドメインクラスを利用しなくてもIntegerやStringなど基本型のみでデータアクセスが可能です。

## ドメイン定義

ドメインクラスは @Domainを注釈して示します。

@DomainのvalueType要素には、ドメインクラスで扱う基本型を指定します。この基本型が、データベースのカラムの型とのマッピングに使用されます。

accessorMethod要素には、valueType要素に指定した型を返すアクセッサメソッドの名前を指定します。デフォルト値は"getValue"です。

factoryMethod要素には、valueType要素に指定した型をパラメータとし受け取りドメインクラスの型を返すファクトリメソッドの名前を指定します。デフォルト値は"new"であり、これはコンストラクタにより生成することを示します。

acceptNull要素には、ドメインクラスのコンストラクタもしくはファクトリメソッドでnullを受け入れるかどうかを示します。

任意ですが、ドメインクラスは不変オブジェクトとして作成することを推奨します。 クラスには、任意のメソッドを持たせることができます。

列挙型は非privateなコンストラクタを定義できないため、列挙型に対してはファクトリメソッドを用いた方法を使用する必要があります。

### コンストラクタで生成する方法

@DomainのfactoryMethod要素のデフォルトの値はnewであり、 非privateなコンストラクタでインスタンスを生成することを示します。そのため、コンストラクタで生成する場合はfactoryMethod要素を省略できます。

次の例では、 publicなコンストラクタを持つドメインクラスを作成しています。 このクラスは電話番号を表しています。

```java
package example.domain;

import org.seasar.doma.Domain;

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
       // ドメインに固有の振る舞いを記述できる。
       ...
    }
}
```

### ファクトリメソッドで生成する方法

コンストラクタをprivateにし、ファクトリメソッドを使ってインスタンスを生成したい場合は、 staticな非privateなメソッドを定義し@DomainのfactoryMethod要素にそのメソッドの名前を指定します。

次の例では、publicな ファクトリメソッドをもつドメインクラスを作成しています。 このクラスは電話番号を表しています。

```java
package example.domain;

import org.seasar.doma.Domain;

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
       // ドメインに固有の振る舞いを記述できる。
       ...
    }
    
    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }
}
```

次の例では、publicな ファクトリメソッドをもつ列挙型をドメインクラスとして作成しています。 この列挙型は仕事の種別を表しています。

```java
package example.domain;

import org.seasar.doma.Domain;

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
```

### 型パラメータの利用

ドメインクラスには任意の数の型パラメータを宣言できます。

次の例では、1つの型パラメータを持ち、さらにpublicなコンストラクタを持つドメインクラスを作成しています。 このクラスは識別子を表しています。

```java
package example.domain;

import org.seasar.doma.Domain;

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
```

型パラメータを持ったドメインクラスはファクトリメソッドで生成することも可能です。 この場合、ファクトリメソッドにはクラスの型変数宣言と同等の宣言が必要です。

```java
package example.domain;

import org.seasar.doma.Domain;

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
```

## 外部ドメイン定義

ソースコードに手を加えられないなどの理由で任意の型をドメインクラスとして扱いたい場合は、外部ドメイン定義が使えます。

外部ドメイン定義は、 DomainConverter の実装クラスに @ExternalDomain を注釈して示します。

DomainConverterの第1型パラメータにはドメインクラスとして扱いたい型、第2型パラメータには 基本型 を指定します。

例えば、次のようなPhoneNumberというクラスがあり、ソースコードに手を加えられないとします。

```java
package sample;

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
```

上記のPhoneNumberをドメインクラスとして扱うには、次のようなクラスを作成します。

```java
package example.domain;

import sample.PhoneNumber;

@ExternalDomain
public class PhoneNumberConverter implements DomainConverter<PhoneNumber, String> {

    public String fromDomainToValue(PhoneNumber domain) {
        return domain.getValue();
    }

    public PhoneNumber fromValueToDomain(String value) {
        return new PhoneNumber(value);
    }
}
```

これで外部ドメイン定義は完成ですが、これだけではまだ利用できません。 外部ドメインの定義は注釈処理のオプションで指定する必要があります。

注釈処理のオプションで指定する前段階として、外部ドメイン定義を @DomainConverters で登録します。 @DomainConverters には複数の外部ドメイン定義を登録可能です。

```java
package example.domain;

@DomainConverters({ PhoneNumberConverter.class })
public class DomainConvertersProvider {
}
```

そして、 @DomainConverters が注釈されたクラスの完全修飾名を注釈処理のオプションに指定します。 オプションのkeyは、「doma.domain.converters」です。 オプションの指定の仕方については [注釈処理のオプション](apt-option.md) を参照してください。

### 外部ドメイン定義で型パラメータの利用

外部ドメイン定義では、任意の数の型パラメータを持ったクラスを扱えます。

次の例のような1つの型パラメータを持つクラスがあるとします。 このクラスは識別子を表しています。

```java
package sample;

public class Identity<T> {

    private final int value;

    public Identity(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
```

上記のIdentityをドメインクラスとして扱うには、次のようなクラスを作成します。 Identityの型パラメータにはワイルドカード（?）を指定しなければいけません。

```
package example.domain;

import sample.Identity;

@ExternalDomain
public class IdentityConverter implements DomainConverter<Identity<?>, String> {

    public String fromDomainToValue(Identity<?> domain) {
        return domain.getValue();
    }

    @SuppressWarnings("rawtypes")
    public Identity<?> fromValueToDomain(String value) {
        return new Identity(value);
    }
}
```

その他の設定方法については、型パラメータを使用しない場合と同様です。

## 利用例

ドメインクラスが型パラメータを持つ場合、型パラメータには具体的な型が必要です。 ワイルドカード（?）や型変数の指定はサポートされていません。

### エンティティクラス

エンティティクラスのフィールドの型での利用例です。

```java
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
```

### Daoインタフェース

Daoインタフェースのメソッドのパラメータや戻り値での利用例です。

```java
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
```