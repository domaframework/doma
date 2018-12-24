==================
Kotlin サポート
==================

.. contents:: 目次
   :depth: 3

Doma は `Kotlin <https://kotlinlang.org/>`_ 1.1.2を実験的にサポートしています。

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

.. code-block:: java

  @Embeddable
  data class Address(val city: String, val street: String)

Daoインタフェース
-------------------

* KotlinではなくJavaで定義する
* 更新処理の戻り値の型は `org.seasar.doma.jdbc.Result` や `org.seasar.doma.jdbc.BatchResult` を使う

.. code-block:: java

  @Dao(config = AppConfig.class)
  public interface PersonDao {
    @Select
    Person selectById(Integer id);
    @Insert
    Result<Person> insert(Person person);
  }

* 更新処理の戻り値を扱う際は `Destructuring Declarations <https://kotlinlang.org/docs/reference/multi-declarations.html>`_ を使う

.. code-block:: java

  val dao: PersonDao = ...
  val person = Person(name = Name("Jhon"), address = Address(city = "Tokyo", street = "Yaesu"))
  val (newPerson, count) = dao.insert(person)


kaptによるビルド
-------------------

Kotlinで記述されたクラスやインタフェースに対して注釈処理をするには `kapt <https://blog.jetbrains.com/kotlin/2016/12/kotlin-1-0-6-is-here/>`_ を実行する必要があります。
kaptは実験的な位置付けにありドキュメントがありません。
Gradleでビルドする際は、確実な注釈処理が行われるように常に `clean build` を実行することを推奨します。

.. code-block:: sh

  ./gradlew clean build

Eclispeを利用する場合設定を適切に行えばJavaの注釈処理は自動で行われますが、kapt（Kotlinの注釈処理）はGradleを実行しない限り行われないことに注意してください。

下記はbuild.gradleの抜粋です。コンパイル時にSQLファイルを参照するために下記の設定に特に注意してください。

.. code-block:: groovy

  // コンパイルより前にSQLファイルを出力先ディレクトリにコピーするために依存関係を逆転する
  compileJava.dependsOn processResources
  
  // SQLファイルなどリソースファイルの出力先ディレクトリをkaptに伝える
  kapt {
      arguments {
          arg("doma.resources.dir", processResources.destinationDir)
      }
  }


JavaとKotlinの混在
-------------------------

kaptの不確実な挙動を避けるため、Domaに関するコードの全てをJavaで書くことは検討に値します。
Domaの利用において、JavaとKotlinの混在は問題ありません。

サンプルプロジェクト
=====================

サンプルコードについては下記のプロジェクトを参照ください。

* `kotlin-sample <https://github.com/domaframework/kotlin-sample>`_
