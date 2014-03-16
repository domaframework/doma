==================
ビルド
==================

.. contents:: 目次
   :depth: 3

ビルドを行う際のポイントは以下のとおりです。

* JavaクラスとSQLファイルの出力先ディレクトリを同じにする
* コンパイルより前にSQLファイルを出力先ディレクトリにコピーする
* 依存関係の設定でdomaへの依存を指定する

Gradle
==================

サンプルのbuild.gradleです。

.. code-block:: groovy

  apply plugin: 'java'

  // JavaクラスとSQLファイルの出力先ディレクトリを同じにする
  processResources.destinationDir = compileJava.destinationDir
  // コンパイルより前にSQLファイルを出力先ディレクトリにコピーするために依存関係を逆転する
  compileJava.dependsOn processResources

  repositories {
      mavenCentral()
  }

  dependencies {
      compile "org.seasar.doma:doma:2.0.0"
  }

Maven
==================

TODO
