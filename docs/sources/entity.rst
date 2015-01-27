==================
エンティティクラス
==================

.. contents:: 目次
   :depth: 3

Entity（エンティティ）は、データベースのテーブルやクエリの結果セットに対応します。

エンティティ定義
==================

エンティティクラスは ``@Entity`` を注釈して示します。

.. code-block:: java

  @Entity
  public class Employee {
      ...
  }

エンティティリスナー
---------------------------

エンティティがデータベースに対し挿入、更新、削除される直前/直後に処理を実行したい場合、
``@Entity`` の ``listener`` 要素に ``EntityListener`` の実装クラスを指定できます。

.. code-block:: java

  @Entity(listener = EmployeeEntityListener.class)
  public class Employee {
      ...
  }

``listener`` 要素に何も指定しない場合、エンティティクラスが他のエンティティクラスを継承
しているかどうかで採用する設定が変わります。

* 継承している場合、親エンティティクラスの設定を引き継ぎます
* 継承していない場合、何も行いません（ ``NullEntityListener`` が使用されます）

ネーミング規約
---------------------------

エンティティに対応するテーブル名やプロパティに対応するカラム名を解決するためのネーミング規約
を変更したい場合、 ``naming`` 要素に ``NamingType`` の列挙型を指定できます。

.. code-block:: java

  @Entity(naming = NamingType.SNAKE_UPPER_CASE)
  public class EmployeeInfo {
      ...
  }


``naming`` 要素に何も指定しない場合、エンティティクラスが他のエンティティクラスを継承している
かどうかで採用する設定が変わります。

* 継承している場合、親エンィティクラスの設定を引き継ぎます
* 継承していない場合、何も行いません（ ``NamingType.NONE`` が使用されます）

``NamingType.SNAKE_UPPER_CASE`` は、エンティティ名やプロパティ名を
スネークケース（アンダースコア区切り）の大文字に変換します。
この例の場合、テーブル名はEMPLOYEE_INFOになります。

``naming`` 要素に何も指定しない場合、デフォルトでは、テーブル名にはエンティティクラスの単純名、
カラム名にはプロパティ名が使用されます。

ネーミング規約は、 ``@Table`` や ``@Colum`` の ``name`` 要素が指定されない場合のみ使用されます。
``@Table`` や ``@Colum`` の ``name`` 要素が指定された場合は、 ``name`` 要素
に指定した値が使用され、ネーミング規約は適用されません。

イミュータブルなエンティティ
----------------------------

エンティティをイミュータブルなオブジェクトとして扱いたい場合は
``@Entity`` の ``immutable`` 要素に ``true`` を設定します。

.. code-block:: java

  @Entity(immutable = true)
  public class Employee {
      @Id
      final Integer id;
      final String name;
      @Version
      final Integer version;

      public Employee(Integer id, String name, Integer version) {
          this.id = id;
          this.name = name;
          this.version = version;
      }
  }

永続的なフィールドには ``final`` 修飾子が必須です。

テーブル
------------------

エンティティに対応するテーブル情報を指定するには、 ``@Table`` を使用します。

``name`` 要素でテーブル名を指定できます。

.. code-block:: java

  @Entity
  @Table(name = "EMP")
  public class Employee {
      ...
  }

``@Table`` を使用しない、もしくは ``@Table`` の ``name`` 要素を使用しない場合、
テーブル名は `ネーミング規約`_ により解決されます。

フィールド定義
==================

エンティティクラスのフィールドはデフォルトで永続的です。
つまり、テーブルや結果セットのカラムに対応します。
フィールドの型は次のいずれかでなければいけません。

* :doc:`basic`
* :doc:`domain`
* :doc:`basic` または :doc:`domain` のいずれかを要素とするjava.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

.. code-block:: java

  @Entity
  public class Employee {
      ...
      Integer employeeId;
  }

カラム
------------------

カラム情報を指定するには、 ``@Column`` を使用します。

``name`` 要素でカラム名を指定できます。

.. code-block:: java

  @Column(name = "ENAME")
  String employeeName;

``insertable`` 要素や ``updatable`` 要素で挿入や更新の対象とするかどうかを指定できます。

.. code-block:: java

  @Column(insertable = false, updatable = false)
  String employeeName;

``@Column`` を使用しない、もしくは ``@Column`` の ``name`` 要素を使用しない場合、
カラム名は `ネーミング規約`_ により解決されます。

識別子
------

識別子(主キー)であることを指定するには、 ``@Id`` を使います。

.. code-block:: java

  @Id
  Integer id;

複合主キーの場合は ``@Id`` を複数指定します。

.. code-block:: java

  @Id
  Integer id;

  @Id
  Integer id2;

識別子の自動生成
~~~~~~~~~~~~~~~~

識別子を自動生成するには ``@GeneratedValue`` を注釈して示します。
フィールドの型は以下のいずれかでなければいけません。

* java.lang.Number のサブタイプ
* java.lang.Number のサブタイプを値とする :doc:`domain`
* 上記のいずれかを要素の型とする java.util.Optional
* OptionalInt
* OptionalLong
* OptionalDouble
* 数値のプリミティブ型

IDENTITYを使った識別子の自動生成
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

データベースのIDENTITY自動生成機能を利用する方法です。
RDBMSによってはサポートされていません。
フィールドに対応するカラムの定義でIDENTITY自動生成を有効にしておく必要があります。

.. code-block:: java

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

シーケンスを使った識別子の自動生成
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

データベースのシーケンスを利用する方法です。
RDBMSによってはサポートされていません。

``@SequenceGenerator`` では、シーケンスの名前、割り当てサイズ、初期値等を設定できます。
データベースにあらかじめシーケンスを定義しておく必要がありますが、
その定義は ``@SequenceGenerator`` の定義とあわせておく必要があります。

.. code-block:: java

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequence = "EMPLOYEE_SEQ")
  Integer id;

テーブルを使った識別子の自動採番
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

生成される識別子をテーブルで管理する方法です。
すべてのRDBMSで利用できます。

``@TableGenerator`` では、テーブル名、割り当てサイズ、初期値等を設定できます。
データベースにあらかじめテーブルを定義しておく必要がありますが、
その定義は ``@TableGenerator`` の定義とあわせておく必要があります。
デフォルトでは、 ``ID_GENERATOR`` という名前のテーブルに、文字列型の ``PK`` と数値型の ``VALUE``
という2つのカラムが定義されているものとして動作します（ ``PK`` カラムが主キーです）。
``PK`` カラムにはエンティティクラスごとの一意な名前、 ``VALUE`` カラムには識別子の値が格納されます。
テーブルには、エンティティクラスごとのレコードをあらかじめ登録しておく必要があります。

.. code-block:: java

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  @TableGenerator(pkColumnValue = "EMPLOYEE_ID")
  Integer id;

``@TableGenerator`` の ``pkColumnValue`` 要素には、 識別子を管理するテーブル
（デフォルトでは、 ``ID_GENERATOR`` という名前のテーブル）の主キーの値を指定します。


バージョン
------------------

楽観的排他制御用のバージョンは ``@Version`` を注釈して示します。
フィールドの型は以下のいずれかでなければいけません。

* java.lang.Number のサブタイプ
* java.lang.Number のサブタイプを値とする :doc:`domain`
* 上記のいずれかを要素の型とする java.util.Optional
* OptionalInt
* OptionalLong
* OptionalDouble
* 数値のプリミティブ型

.. code-block:: java

  @Version
  Integer version;

非永続的なフィールド
--------------------------------

非永続的なフィールドは、テーブルや結果セットのカラムに対応しません。

``@Transient`` を注釈して示します。

フィールドの型や可視性に制限はありません。

.. code-block:: java

  @Transient
  List<String> nameList;

取得時の状態を管理するフィールド
--------------------------------------------

取得時の状態とは、エンティティがDaoから取得されときの全プロパティの値です。
取得時の状態を保持しておくことで、更新処理を実行する際、UPDATE文のSET句に変更したフィールドのみを含められます。
取得時の状態を管理するフィールドは、テーブルや結果セットのカラムに対応しません。

``@OriginalStates`` を注釈して示します。

.. code-block:: java

  @OriginalStates
  Employee originalStates;

メソッド定義
==================

メソッドの定義に制限はありません。

フィールドの可視性を ``protected`` やパッケージプライベートにして ``public`` なメソッド経由で
アクセスすることも、メソッドを一切使用せず ``public`` フィールドに直接アクセスすること
もどちらもサポートされています。

利用例
==================

インスタンス化して利用します。

.. code-block:: java

  Employee employee = new Employee();
  employee.setEmployeeId(1);
  employee.setEmployeeName("SMITH");
  employee.setSalary(new BigDecimal(1000));

