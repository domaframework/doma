==============
リリースノート
==============

2.0-beta-4: 2014-05-04
======================

* Pluggable Annotation Processing API の Visitor を Java 8 用のものへバージョンアップしました
* 空の ``java.util.Iterable`` を IN 句にバインドする場合は SQL の ``null`` として扱うようにしました
* ``java.sql.SQLXML`` に対応しました
* ``LocalTransaction`` で指定したセーブポイント「以降」を削除すべき箇所で「以前」を削除している不具合を修正しました
* ``LocalTransaction`` でセーブポイント削除時のログが間違っている不具合を修正しました
* Entity のプロパティの型を byte 配列にすると注釈処理に失敗する不具合を修正しました

2.0-beta-3: 2014-04-03
======================

* 検索結果を ``java.util.stream.Collector`` で処理できるようにしました。
* ``LocalTransactionManager`` から ``TransactionManager`` インタフェースを抽出しました。
* ``Config`` で指定した設定が一部無視される不具合を修正しました。
* マップのネーミング規約を一律制御するためのインタフェース ``MapKeyNaming`` を追加しました。
* ``java.time.LocalDate`` 、 ``java.time.LocalTime`` 、 ``java.time.LocalDateTime``
  を基本型として使用できるようにしました。
* ``JdbcLogger`` の実装の差し替えを容易にするために ``AbstractJdbcLogger`` を追加しました。
* ``SelectStrategyType`` の名前を ``SelectType`` に変更しました。

