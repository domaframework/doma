==================
ドメインクラス
==================

.. contents:: 目次
   :depth: 3

Domain （ドメイン）クラスの定義方法を示します。

Doma では、テーブルのカラムの値を **ドメイン** と呼ばれる Java オブジェクトで扱えます。
ドメインとは値のとり得る範囲、つまり定義域のことです。

ドメインクラスを利用することで、データベース上のカラムの型が同じあっても
アプリケーション上意味が異なるものを別のJavaの型で表現できます。
これにより意味を明確にしプログラミングミスを事前に防ぎやすくなります。
また、ドメインクラスに振る舞いを持たせることでよりわかりやすいプログラミングが可能です。

ドメインクラスの作成と利用は任意です。
ドメインクラスを利用しなくても ``Integer`` や ``String``
など基本型のみでデータアクセスは可能です。

ドメインは、定義の仕方により内部ドメインと外部ドメインに分けられます。

内部ドメイン
======================

ドメインとして扱いたい対象のクラスのソースコードに直接定義を記述します。

内部ドメインを定義するには、クラスに ``@Domain`` を注釈します。

``@Domain`` の ``valueType`` 要素には :doc:`basic` を指定します。

コンストラクタで生成する方法
-----------------------------------------------

``@Domain`` の ``factoryMethod`` 要素のデフォルトの値は ``new`` であり、
非privateなコンストラクタでインスタンスを生成することを示します。
そのため、コンストラクタで生成する場合は ``factoryMethod`` 要素を省略できます。
次の例では、 ``public`` なコンストラクタを持つドメインクラスを作成しています。
このクラスは電話番号を表しています。

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
         // ドメインに固有の振る舞いを記述できる。
         ...
      }
  }

ファクトリメソッドで生成する方法
-----------------------------------------------

コンストラクタをprivateにしファクトリメソッドを使ってインスタンスを生成したい場合は、
staticな非privateなメソッドを定義し ``@Domain`` の ``factoryMethod`` 要素にそのメソッドの名前を指定します。
次の例では、publicなファクトリメソッドをもつドメインクラスを作成しています。
このクラスは電話番号を表しています。

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
         // ドメインに固有の振る舞いを記述できる。
         ...
      }

      public static PhoneNumber of(String value) {
          return new PhoneNumber(value);
      }
  }

次の例では、 ``public`` なファクトリメソッドをもつ列挙型をドメインクラスとして作成しています。
この列挙型は仕事の種別を表しています。

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

型パラメータを利用する方法
-----------------------------------------------

ドメインクラスには任意の数の型パラメータを宣言できます。
次の例では、1つの型パラメータを持ち、さらに ``public`` なコンストラクタを持つ
ドメインクラスを作成しています。
このクラスは識別子を表しています。

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

型パラメータを持ったドメインクラスはファクトリメソッドで生成することも可能です。
この場合、ファクトリメソッドにはクラスの型変数宣言と同等の宣言が必要です。

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

外部ドメイン
======================

ドメインとして扱いたい対象のクラスとは別のクラスに定義を記述します。

外部ドメインは、ソースコードに手を加えられない、 Doma へ依存させたくない、
といった理由がある場合に有効です。
外部ドメインを定義するには、 ``DomainConverter`` の実装クラスに
``@ExternalDomain`` を注釈して示します。

例えば、次のような ``PhoneNumber`` というクラスがありソースコードに手を加えられないとします。

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

上記の ``PhoneNumber`` をドメインクラスとして扱うには、次のようなクラスを作成します。

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

これで外部ドメイン定義は完成ですが、これだけではまだ利用できません。
外部ドメイン定義を ``@DomainConverters`` へ登録します。
``@DomainConverters`` には複数の外部ドメイン定義を登録可能です。

.. code-block:: java

  @DomainConverters({ PhoneNumberConverter.class })
  public class DomainConvertersProvider {
  }

そして最後に、 ``@DomainConverters`` が注釈されたクラスの完全修飾名を
:doc:`annotation-processing` のオプションに指定します。
オプションのkeyは、 ``doma.domain.converters`` です。

型パラメータを利用する方法
----------------------------------------

任意の数の型パラメータを持ったクラスを扱えます。
次の例のような1つの型パラメータを持つクラスがあるとします。
このクラスは識別子を表しています。

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

上記の ``Identity`` をドメインクラスとして扱うには、次のようなクラスを作成します。
``Identity`` の型パラメータにはワイルドカード ``?`` を指定しなければいけません。

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

その他の設定方法については、型パラメータを使用しない場合と同様です。

利用例
==================

ドメインクラスが型パラメータを持つ場合、型パラメータには具体的な型が必要です。
ワイルドカード ``?`` や型変数の指定はサポートされていません。

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




















