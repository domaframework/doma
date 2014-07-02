==============
リリースノート
==============

2.0.0: 2014-07-02
======================

* ``UnitOfWork`` を削除しました

2.0-beta-5: 2014-06-07
======================

* ``List<Optional<Emp>>`` や ``List<Optional<Map<String, Object>>>`` を戻り値とする
  Dao メソッドは注釈処理でコンパイルエラーにしました
* Entity 更新後に OriginalStates へ変更が反映されない問題を修正しました
* エンティティの識別子の値がすでに設定されている場合は自動生成処理を実行しないようにしました
* カラムリスト展開コメント で DOMA4257 エラーになる問題を修正しました
* SQLのログ出力方法をアノテーションで制御できるようにしました
* Dao から出力されるログのメッセージを詳細化しました
* ``UtilLoggingJdbcLogger`` のロガーの名前をクラスの完全修飾名に変更しました
* SQL実行時にSQLファイルのパスがログに出力されない問題を修正しました

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

