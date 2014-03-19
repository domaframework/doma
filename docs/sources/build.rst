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

Doma の SNAPSHOT を参照したい場合は、
repositories を次のように書き換えてください。

.. code-block:: groovy

  repositories {
      mavenCentral()
      maven {url 'https://oss.sonatype.org/content/repositories/snapshots/'}
  }

Doma の SNAPSHOT は `Travis-CI <https://travis-ci.org/domaframework/doma>`_
でビルドが成功されるたびに作成されリポジトリに配置されます。

Maven
==================

TODO
