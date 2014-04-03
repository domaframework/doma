==============
リリースノート
==============

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

