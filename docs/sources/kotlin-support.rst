==================
Kotlin サポート
==================

.. contents:: 目次
   :depth: 3

Doma は `Kotlin <https://kotlinlang.org/>`_ を実験的にサポートしています。

Kotlin利用のベストプラクティス
================================

クラスの定義やビルドに関する事柄について推奨する方法を記載します。

エンティティクラス
-------------------

* Data Classで定義する
* イミュータブルで定義する（ `@Entity` の `immutable` 要素に `true` を設定する）
* コンストラクタは1つだけ定義する
* コンストラクタ以外でプロパティを定義しない
* コンストラクタで定義するプロパティには `val` を使用する
* 継承は使わない

.. code-block:: java

  @Entity(immutable = true)
  data class Person(
        @Id
        @GeneratedValue(strategy = org.seasar.doma.GenerationType.IDENTITY)
        val id: Int? = null,
        val name: Name,
        val address: Address)

ドメインクラス
-------------------

* Data Classで定義する
* コンストラクタは1つだけ定義する
* コンストラクタで定義するプロパティの名前は `value` にする
* コンストラクタで定義するプロパティには `val` を使用する

.. code-block:: java

  @Domain(valueType = String::class)
  data class Name(val value: String)

エンベッダブルクラス
----------------------

* Data Classで定義する
* コンストラクタは1つだけ定義する
* コンストラクタ以外でプロパティを定義しない
* コンストラクタで定義するプロパティには `val` を使用する
* 継承は使わない

.. code-block:: java

  @Embeddable
  data class Address(val city: String, val street: String)

Daoインタフェース
-------------------

* SQLファイルとマッピングする場合は `@ParameterName` を使ってメソッドのパラメータに名前をつける
* 更新処理の戻り値の型は `org.seasar.doma.jdbc.Result` や `org.seasar.doma.jdbc.BatchResult` を使う

.. code-block:: java

  @Dao(config = AppConfig::class)
  interface PersonDao {
    @Select
    fun selectById(@ParameterName("id") id: Int): Person
    @Insert
    fun insert(person: Person): Result<Person>
  }

* 更新処理の戻り値を扱う際は `Destructuring Declarations <https://kotlinlang.org/docs/reference/multi-declarations.html>`_ を使う

.. code-block:: java

  val dao: PersonDao = ...
  val person = Person(name = Name("Jhon"), address = Address(city = "Tokyo", street = "Yaesu"))
  val (newPerson, count) = dao.insert(person)


kaptによるビルド
-------------------

Kotlin で記述されたクラスやインタフェースに対して注釈処理をするには `kapt <http://blog.jetbrains.com/kotlin/2015/06/better-annotation-processing-supporting-stubs-in-kapt/>`_ を実行する必要があります。
2016年5月現在、Kotlin 1.0.2のkaptは機能が不足しておりかつ動作が不安定です。また、ドキュメントがありません。
Daoインタフェースで `@ParameterName` を用いて明示的に名前をつけることを推奨するのも kapt の不具合に由来します。
不安定な挙動を避けるため、Gradleでビルドする際、常に `clean build` を実行することを推奨します。

.. code-block:: sh

  ./gradlew clean build

Eclispeを利用する場合設定を適切に行えばJavaの注釈処理は自動で行われますが、kapt（Kotlinの注釈処理）はGradleを実行しない限り行われないことに注意してください。

JavaとKotlinの混在
-------------------------

kaptの不具合を避けるため、Domaに関するコードの全てもしくは一部をJavaで書くことは検討に値します。
特に、DaoについてはJavaで書いても良いかもしれません。
JavaとKotlinで記述量にほとんど差がなく、 `@ParameterName` を使う必要性を避けられるためです。
Domaの利用において、JavaとKotlinの混在は特に問題ありません。

サンプルプロジェクト
=====================

サンプルコードについては下記のプロジェクトを参照ください。

* `kotlin-sample <https://github.com/domaframework/kotlin-sample>`_
