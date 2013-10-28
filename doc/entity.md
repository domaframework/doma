# エンティティクラス

## 目次

- [概要](#概要)
- [エンティティ定義](#エンティティ定義)
- [フィールド定義](#フィールド定義)
- [メソッド定義](#メソッド定義)
- [利用例](#利用例)

## 概要

Entity（エンティティ）クラスは、データベースのテーブルやクエリの結果セットに対応します。

このページで説明するアノテーションはすべてorg.seasar.domaパッケージに属します。

## エンティティ定義

エンティティクラスは @Entityが注釈されたクラスとして定義します。 クラスはトップレベルのクラスでなければいけません（他のクラスやインタフェースにネストされていてはいけません）。 エンティティクラスは 非privateなデフォルトコンストラクタ（引数なしのコンストラクタ）を持たなければいけません。

```java
@Entity
public class Employee {
    ...
}
```

### エンティティリスナー

エンティティがデータベースに対し挿入、更新、削除される直前/直後に処理を実行したい場合、 listener 要素に org.seasar.doma.jdbc.entity.EntityListener の実装クラスを指定できます。

```java
@Entity(listener = EmployeeEntityListener.class)
public class Employee {
    ...
}
```

listener 要素に何も指定しない場合、エンティティクラスが他のエンティティクラスを継承しているかどうかで採用する設定が変わります。

- 継承している場合、親エンティティクラスの設定を引き継ぎます
- 継承していない場合、何も行いません（org.seasar.doma.jdbc.entity.NullEntityListener が使用されます）

EntityListenerは次のように実装できます。

```java
public class EmployeeEntityListener implements EntityListener<Employee> {
    @Override
    public void preDelete(Employee entity, PreDeleteContext<Employee> context) {
        ...
    }
    @Override
    public void preInsert(Employee entity, PreInsertContext<Employee> context) {
        ...
    }
    @Override
    public void preUpdate(Employee entity, PreUpdateContext<Employee> context) {
        ...
    }
    @Override
    public void postDelete(Employee entity, PostDeleteContext<Employee> context) {
        ...
    }
    @Override
    public void postInsert(Employee entity, PostInsertContext<Employee> context) {
        ...
    }
    @Override
    public void postUpdate(Employee entity, PostUpdateContext<Employee> context) {
        ...
    }
}
```

EntityListenerのメソッドが呼び出される条件は次のどちらかです。

- Daoメソッドに、@Insert、@Delete、@Updateのいずれかが注釈され、パラメータにエンティティを含む
- Daoメソッドに、@BatchInsert、@BatchDelete、@BatchUpdateのいずれかが注釈され、パラメータにエンティティのリストを含む

### ネーミング規約

エンティティに対応するテーブル名やプロパティに対応するカラム名を解決するためのネーミング規約を変更したい場合、 naming 要素に org.seasar.doma.jdbc.entity.NamingType の列挙型を指定できます。

naming 要素に何も指定しない場合、エンティティクラスが他のエンティティクラスを継承しているかどうかで採用する設定が変わります。

- 継承している場合、親エンティティクラスの設定を引き継ぎます
- 継承していない場合、何も行いません（org.seasar.doma.jdbc.entity.NamingType.NONE が使用されます）

NamingType.SNAKE_UPPER_CASEは、エンティティ名やプロパティ名をスネークケース（アンダースコア区切り）の大文字に変換します。この例の場合、テーブル名はEMPLOYEE_INFOになります。

naming要素に何も指定しない場合、デフォルトでは、テーブル名にはエンティティクラスの単純名、カラム名にはプロパティ名が使用されます。

ネーミング規約は、@Tableや@Columのname要素が指定されない場合のみ使用されます。 @Tableや@Columのname要素が指定された場合は、name要素に指定した値が使用され、ネーミング規約は適用されません。

### テーブル

エンティティに対応するテーブル情報を指定するには、 @Table を使用します。

name要素でテーブル名を指定できます。

```java
@Entity
@Table(name = "EMP")
public class Employee {
    ...
}
```

catalog要素や schema要素 でカタログやスキーマを指定できます。

```java
@Entity
@Table(catalog = "CATALOG", schema ="SCHEMA", name = "EMP")
public class Employee {
    ...
}
```

@Tableを使用しない、もしくは @Tableのname要素を使用しない場合、 テーブル名は、ネーミング規約により解決されます。

### 継承

任意のクラスを継承できます。

ただし、親クラスのフィールドが永続フィールドとみなされたりDomaのアノテーションが解釈されたりするのは、親クラスが@Entityで注釈されている場合だけです。

## フィールド定義

### 永続的なフィールド

永続的なフィールドは、テーブルや結果セットのカラムに対応します。

フィールドの型は、基本型もしくはドメインクラスでなければいけません。

```java
@Entity
public class Employee {
    ...
    Integer employeeId;
}
```

### カラム

プロパティに対応するカラム情報を指定するには、@Columnを使用します。

name要素でカラム名を指定できます。

```java
@Column(name = "ENAME")
String employeeName;
```

insertable要素や updatable要素 で挿入や更新の対象とするかどうかを指定できます。

```java
@Column(insertable = false, updatable = false)
String employeeName;
```

@Columnを使用しない、もしくは@Columnの name要素を使用しない場合、 カラム名は、ネーミング規約により解決されます。

### 識別子

識別子(主キー)であることを指定するには、@Idを使います。

```java
@Id
Integer id;
```

複合主キーの場合は @Id を複数指定します。

```java
@Id
Integer id;

@Id
Integer id2;
```

識別子を自動生成する場合は、@GeneratedValueを使用し、 生成方法をstrategy要素に指定します。 このアノテーションが注釈されるフィールドの型は数値のプリミティブ型、 、Numberのサブタイプ、もしくはそれらを基本型とするドメイン型でなければいけません。

strategy 要素に指定できる方法は、次の3つです。

- GenerationType.IDENTITY
- GenerationType.SEQUENCE
- GenerationType.TABLE

#### GenerationType.IDENTITY

データベースのIDENTITY自動生成機能を利用する方法です。RDBMSによってはサポートされていません。 フィールドに対応するカラムの定義でIDENTITY自動生成を有効にしておく必要があります。

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
Integer id;
```

#### GenerationType.SEQUENCE

データベースのシーケンスを利用する方法です。RDBMSによってはサポートされていません。

GenerationType.SEQUENCEを使用するには、 @SequenceGeneratorを併記します。 @SequenceGeneratorでは、シーケンスの名前、割り当てサイズ、初期値等を設定できます。 データベースにあらかじめシーケンスを定義しておく必要がありますが、その定義は@SequenceGeneratorの定義とあわせておく必要があります。

```java
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE)
@SequenceGenerator(sequence = "EMPLOYEE_SEQ")
Integer id;
```

#### GenerationType.TABLE

生成される識別子をテーブルで管理する方法です。すべてのRDBMSで利用できます。

GenerationType.TABLEを使用するには、 @TableGeneratorを併記します。 @TableGeneratorでは、テーブル名、割り当てサイズ、初期値等を設定できます。 データベースにあらかじめテーブルを定義しておく必要がありますが、その定義は@TableGeneratorの定義とあわせておく必要があります。 デフォルトでは、「ID_GENERATOR」という名前のテーブルに、文字列型の「PK」と数値型の「VALUE」という2つのカラムが定義されているものとして動作します（「PK」カラムが主キーです）。 「PK」カラムにはエンティティクラスごとの一意な名前、「VALUE」カラムには識別子の値が格納されます。 テーブルには、エンティティクラスごとのレコードをあらかじめ登録しておく必要があります。

```java
@Id
@GeneratedValue(strategy = GenerationType.TABLE)
@TableGenerator(pkColumnValue = "EMPLOYEE_ID")
Integer id;
```

### バージョン

楽観的排他制御用のバージョンは@Versionを注釈して示します。

フィールドの型は数値のプリミティブ型、 Numberのサブタイプ、もしくはそれらを基本型とするドメイン型でなければいけません。

```java
@Version
Integer version;
```

### 非永続的なフィールド

非永続的なフィールドは、テーブルや結果セットのカラムに対応しません。

@Transientを注釈して示します。 フィールドの型や可視性に制限はありません。

```java
@Transient
BigDecimal tempSalary;
```

```java
@Transient
List<String> nameList;
```

### 取得時の状態を管理するフィールド

取得時の状態とは、エンティティがDaoから取得されときの全プロパティの値です。 取得時の状態を保持しておくことで、Daoインタフェースの@Updateが注釈されたメソッドを介して更新処理を実行する際、 UPDATE文のSET句に変更したフィールドのみを含めるようにすることが可能です。 取得時の状態を管理するフィールドは、テーブルや結果セットのカラムに対応しません。

@OriginalStatesを注釈して示します。 フィールドの型はフィールドが属するエンティティクラスと同じ型でなければいけません。 @OriginalStatesが注釈されたフィールドはアプリケーションで初期化したりアクセスしたりしてはいけません。

```java
@OriginalStates
Employee originalStates;
```

@OriginalStatesが注釈されたフィールドを持つエンティティの利用法は次のとおりです。

```java
@Entity
public class Employee {

   @Id
   Integer id;

   String name;

   Integer age;

   @OriginalStates
   Employee originalStates;

   ...
}
```

```java
EmployeeDao dao = new EmployeeDaoImpl();
Employee employee =dao.selectById(1);
emoloyee.setAge(employee.getAge() + 1);
dao.update(employee);
```

この例では、UPDATE文のSET句にAGEカラムだけが含まれます。

## メソッド定義

メソッドの定義に制限はありません。 フィールドの可視性をprotectedやパッケージプライベートにしてpublicなメソッド経由でアクセスすることも、 メソッドを一切使用せずpublicフィールドに直接アクセスすることもどちらもサポートされています。

## 利用例

エンティティはインスタンス化して使用します。

```java
Employee employee = new Employee();
employee.setEmployeeId(1);
employee.setEmployeeName("SMITH");
employee.setSalary(new BigDecimal(1000));
```

エンティティはテーブルに1対1で対応させなければいけないわけではありません。 たとえば、EMPLOYEEテーブルとDEPARTMENTテーブルを結合した結果もエンティティにマッピングできます。 そのエンティティクラスをEmpDeptDtoとした場合のエンティティクラスの定義とDaoインタフェースの定義は次のとおりです。

```java
@Entity
public class EmpDeptDto {
    String employeeName;
    String departmentName;
}
```

```java
@Dao(config = AppConfig.class)
public interface EmployeeDao {
    @Select
    EmpDeptDto selectByDepartmentId(Integer departmentId);
}
```