==================
Lombok サポート
==================

.. contents:: 目次
   :depth: 3

Doma は `Lombok <https://projectlombok.org/>`_ のバージョン 1.16.12 以上をサポートしています。

.. note::

  IDE として Eclipse を利用する場合はバージョン 4.5 以上を使ってください。
  4.5 未満ではアノテーションプロッサで取得されるクラスに定義されたフィールドなどの並びがソースコードに記載されている順序と異なり、
  正しく動作しないためです。

Lombok サポートの概要
================================

Lombok と Doma は共に JSR 269 で規定されたアノテーションプロセッサを提供していますが、
Lombok と Doma を同時に利用する場合はアノテーションプロセッサの処理順序が問題になることがあります。
例えば、 Lombok のアノテーションプロセッサがコンストラクタを生成し、
Doma のアノテーションプロセッサがそのコンストラクタを読み取る場合などです。
仮に、Doma のアノテーションプロセッサが先に実行されると単にコンストラクタが定義されていないものとして動作し、
コンパイルに失敗します。

アノテーションプロセッサの処理順序を指定できればこの問題は解決するのですが、
残念ながら処理順序が保証されないことが仕様に記載されており、実際に問題解決の仕組みは提供されていません。

Doma では、この問題に対応するために、Lombok のアノテーションの有無を認識して処理順序に依存にしない振る舞いをするようにしています。
先ほどの例で言えば、 ``@lombok.Value`` などコンストラクタを生成するLombokのアノテーションが存在する場合は
実際にはコンストラクタを読み取らなくてもコンストラクタが存在するものとして動作するということです。

Lombok 利用のベストプラクティス
================================

Lombok のアノテーションを用いたクラスの定義について推奨する方法を記載します。

エンティティクラス
-------------------

immutable（イミュータブル）
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

イミュータブルなエンティティクラスを定義する場合は下記の点に注意します。

* ``@Entity`` の ``immutable`` 要素に ``true`` を設定する
* ``@lombok.Value`` もしくは ``@lombok.AllArgsConstructor`` を注釈する
* ``@lombok.AllArgsConstructor`` を選んだ場合、getterを生成するためには ``@lombok.Getter`` を注釈する

.. code-block:: java

  @Entity(immutable = true)
  @Value
  public class Employee {
    @Id
    Integer id;
    String name;
    Age age;
  }

.. code-block:: java

  @Entity(immutable = true)
  @AllArgsConstructor
  @Getter
  public class Worker {
    @Id
    private final Integer id;
    private final String name;
    private final Age age;
  }

mutable（ミュータブル）
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

ミュータブルなエンティティクラスを定義する場合は下記の点に注意します。

* デフォルトコンストラクタを定義する（デフォルトコンストラクタの生成を抑制しない）
* getter/setterを生成する場合は ``@lombok.Data`` もしくは ``@lombok.Getter`` / ``@lombok.Setter`` を注釈する

.. code-block:: java

  @Entity
  @Data
  public class Person {
    @Id
    private Integer id;
    private String name;
    private Age age;
  }

.. code-block:: java

  @Entity
  @Getter
  @Setter
  public class Businessman {
    @Id
    private Integer id;
    private String name;
    private Age age;
  }

ドメインクラス
-------------------

ドメインクラスを定義する場合は下記の点に注意します。

* ``@lombok.Value`` を注釈する
* インスタンスフィールドは1つだけ定義し名前は ``value`` にする

.. code-block:: java

  @Domain(valueType = Integer.class)
  @Value
  public class Age {
    Integer value;
  }

エンベッダブルクラス
----------------------

エンベッダブルクラスを定義する場合は下記の点に注意します。

* ``@lombok.Value`` もしくは ``@lombok.AllArgsConstructor`` を注釈する
* ``@lombok.AllArgsConstructor`` を選んだ場合、getterを生成するためには ``@lombok.Getter`` を注釈する

.. code-block:: java

  @Embeddable
  @Value
  public class Address {
    String street;
    String city;
  }

.. code-block:: java

  @Embeddable
  @AllArgsConstructor
  @Getter
  public class Location {
    private final String street;
    private final String city;
  }
