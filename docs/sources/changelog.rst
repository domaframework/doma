===========================
Doma 2 における主要な変更点
===========================

.. contents:: 目次
   :depth: 3

Doma 1.36.0 からの変更点の内、主なものを示しています。

全般
====

* Java 8 に対応しました。Java 7 以前では動作しません。
* deprecated な API が削除されました。

設定
====

* ``Config`` の実装クラスをシングルトンとして扱えるように ``@SingletonConfig`` が追加されました。
* トランザクション処理を簡易化するために ``TransactionManager`` が追加されました。
* クエリをカスタマイズ可能にするために ``QueryImplementors`` と ``CommandImplementors`` が追加されました。
* 結果セットに未知のカラムが含まれていた場合の挙動をカスタマイズ可能にするために
  ``UnknownColumnHandler`` が追加されました。
* マップのキーのネーミング規約を一律で解決できるように ``MapKeyNaming`` が追加されました。
* ``AbstractDomaConfing`` が削除されました。
  このクラスが提供していた実装は ``Config`` のデフォルトメソッドで提供されるようになりました。
* ``DefaultClassHelper`` が削除されました。
  このクラスが提供していた実装は ``ClassHelper`` のデフォルトメソッドで提供されるようになりました。
* ``NullRequiresNewController`` が削除されました。
  このクラスが提供していた実装は ``RequiresNewController``
  のデフォルトメソッドで提供されるようになりました。

基本型
======

* ``Object`` 型が基本型と認識されるようになりました。
* ``java.time.LocalDate`` 型が基本型と認識されるようになりました。
* ``java.time.LocalTime`` 型が基本型と認識されるようになりました。
* ``java.time.LocalDateTime`` 型が基本型と認識されるようになりました。

ドメインクラス
==============

* ``@Domain`` を使ってドメインクラスを作った場合、デフォルトでは ``null`` を受け入れなくなりました。
  ``null`` を受け入れるには ``@Domain`` の ``acceptNull`` 要素に ``true`` の指定が必要です。

エンティティクラス
==================

* 実験的な扱いだったイミュータブルなエンティティが正式にサポートされました。
* プロパティに基本型やドメインクラスを要素とする
  ``java.util.Optional`` を定義できるようになりました。
* プロパティに ``java.util.OptionalInt`` を定義できるようになりました。
* プロパティに ``java.util.OptionalLong`` を定義できるようになりました。
* プロパティに ``java.util.OptionalDouble`` を定義できるようになりました。

Daoインタフェース
=================

* パラメータや戻り値の型に ``java.util.Optional`` を定義できるようになりました。
* パラメータや戻り値の型に ``java.util.OptionalInt`` を定義できるようになりました。
* パラメータや戻り値の型に ``java.util.OptionalLong`` を定義できるようになりました。
* パラメータや戻り値の型に ``java.util.OptionalDouble`` を定義できるようになりました。
* ``java.util.stream.Stream`` を使って検索結果を処理できるようになりました。
* ``java.util.stream.Collector`` を使って検索結果を処理できるようになりました。
* ``@Delegate`` が廃止になり、代わりにデフォルトメソッドが使えるようになりました。
* ``IterationCallback`` を使った検索が禁止されました。
  代わりに ``java.util.stream.Stream`` を使った検索をしてください。

SQLに関する変更
===============

* カラムリスト展開コメントが導入されました。

式言語
======

* 組み込み関数 ``contain`` を削除しました。

注釈処理
========

* 注釈処理のオプションの名前に ``doma`` という名前空間を付与しました。
  Doma 以外の注釈処理のオプションとキーが重複することを避けるためです。

ビルド
======

* DomaのjarをMavenのセントラルリポジトリから提供するようにしました。


