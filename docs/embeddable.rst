=========================
エンベッダブルクラス
=========================

.. contents:: 目次
   :depth: 3

Embeddable（エンベッダブル）は、データベースのテーブルやクエリの結果セット複数カラムをグループ化します。

エンベッダブル定義
=========================

エンベッダブルクラスは ``@Enbeddable`` を注釈して示します。
コンストラクタには永続的なフィールドに対応するパラメータが必要です。

.. code-block:: java

  @Embeddalbe
  public class Address {

      final String city;

      final String street;

      @Column(name = "ZIP_CODE")
      final String zip;

      public Address(String city, String street, String zip) {
          this.city = city;
          this.street = street;
          this.zip = zip;
      }
  }

エンベッダブルクラスは :doc:`../entity` のフィールドとして使用します。

.. code-block:: java

  @Entity
  public class Employee {
      @Id
      Integer id;

      Address address;
  }

テーブルや結果セットとのマッピングにおいて、上記のクラス定義は下記のクラス定義と同等です。

.. code-block:: java

  @Entity
  public class Employee {
      @Id
      Integer id;

      String city;

      String street;

      @Column(name = "ZIP_CODE")
      String zip;
  }


ネーミング規約
---------------------------

ネーミング規約は、エンベッダブルクラスを保有する :doc:`../entity` から引き継ぎます。

フィールド定義
==================

エンベッダブルクラスのフィールドはデフォルトで永続的です。
つまり、テーブルや結果セットのカラムに対応します。
フィールドの型は次のいずれかでなければいけません。

* :doc:`basic`
* :doc:`domain`
* :doc:`basic` または :doc:`domain` のいずれかを要素とするjava.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

.. code-block:: java

  @Embeddalbe
  public class Address {
      ...
      String street;
  }

カラム
------------------

カラム情報を指定するには、 ``@Column`` を使用します。

.. code-block:: java

  @Column(name = "ZIP_CODE")
  final String zip;

識別子
------

エンベッダブルクラスには識別子(主キー)を定義できません。

バージョン
------------------

エンベッダブルクラスには楽観的排他制御用のバージョンを定義できません。

非永続的なフィールド
--------------------------------

非永続的なフィールドは、``@Transient`` を注釈して示します。

取得時の状態を管理するフィールド
--------------------------------------------

エンベッダブルクラスには取得時の状態を管理するフィールドを定義できません。

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

  Employee employee = new Employee(); // エンティティ
  Address address = new Address("Tokyo", "Yaesu", "103-0028"); // エンベッダブル
  employee.setAddress(address);

