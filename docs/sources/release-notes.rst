==============
リリースノート
==============

v2.7.0: 2016-02-27
======================

* `GH125 <https://github.com/domaframework/doma/pull/125>`_
  SelectBuilder のデフォルトの FetchType を Lazy に設定
* `GH124 <https://github.com/domaframework/doma/pull/124>`_
  間違った警告メッセージを修正
* `GH122 <https://github.com/domaframework/doma/pull/122>`_
  検索用メソッドの戻り値の型を Stream とすることを認めた
* `GH121 <https://github.com/domaframework/doma/pull/121>`_
  includeの説明が間違っていたのを修正

v2.6.2: 2016-02-11
======================

* `GH118 <https://github.com/domaframework/doma/pull/118>`_
  SQLコメント カラムリスト展開コメント リンク修正
* `GH117 <https://github.com/domaframework/doma/pull/117>`_
  リンクに関連したJavadoc生成オプションを追加しました
* `GH116 <https://github.com/domaframework/doma/pull/116>`_
  クエリビルダのgetSql()の呼び出しでエラーが発生しないように修正
* `GH115 <https://github.com/domaframework/doma/pull/115>`_
  Spring Boot DevToolsに対応

v2.6.1: 2016-01-11
======================

* `GH111 <https://github.com/domaframework/doma/pull/111>`_
  Revert "注釈処理で生成されるコードが冗長なジェネリクスを含む問題を修正"

v2.6.0: 2015-11-21
======================

* `GH107 <https://github.com/domaframework/doma/pull/107>`_
  注釈処理で生成されるコードが冗長な型引数を含む問題を修正
* `GH105 <https://github.com/domaframework/doma/pull/105>`_
  Fix cause position in UniqueConstraintException's constructor

v2.5.1: 2015-11-01
======================

* `GH102 <https://github.com/domaframework/doma/pull/102>`_
  UnknownColumnHandler の handle() を空実装にすると NullPointerException が発生する問題を修正

v2.5.0: 2015-10-10
======================

* `GH99 <https://github.com/domaframework/doma/pull/99>`_
  バッチ更新においてパフォーマンスが悪くなる問題をデータベースのIDENTITYを事前に予約することで解決

v2.4.1: 2015-09-12
======================

* `GH96 <https://github.com/domaframework/doma/pull/96>`_
  埋め込み変数コメントの展開後にスペースを挿入しない

v2.4.0: 2015-08-14
======================

* `GH93 <https://github.com/domaframework/doma/pull/93>`_
  JdbcMappingHint#getDomainClass() がドメインクラスを返さない問題を修正
* `GH89 <https://github.com/domaframework/doma/pull/89>`_
  PortableObjectTypeをジェネリクスにして、String等をvalueTypeとして指定できるように
* `GH88 <https://github.com/domaframework/doma/pull/88>`_
  JdbcLoggerのメソッドのtypoを修正。 Failuer -> Failure
* `GH87 <https://github.com/domaframework/doma/pull/87>`_
  StandardExpressionFunctionsのサブクラスにpublicなコンストラクタを追加
* `GH86 <https://github.com/domaframework/doma/pull/86>`_
  Version number spec is different from the document
* `GH84 <https://github.com/domaframework/doma/pull/84>`_
  populate を使ったメソッドで DOMA4122 が出る問題を修正
* `GH81 <https://github.com/domaframework/doma/pull/81>`_
  リソースバンドルが取得できない場合はデフォルトのリソースバンドルにフォールバックする

v2.3.1: 2015-05-30
======================

* `GH79 <https://github.com/domaframework/doma/pull/79>`_
  SQLファイルを使った更新がスキップされる問題を修正

v2.3.0: 2015-05-23
======================

* `GH75 <https://github.com/domaframework/doma/pull/75>`_
  SQLファイルでUPDATE文のSET句を自動生成
* `GH74 <https://github.com/domaframework/doma/pull/74>`_
  PostgresDialectでID生成エラーが発生する問題を修正

v2.2.0: 2015-03-28
======================

* `GH71 <https://github.com/domaframework/doma/pull/71>`_
  インターフェースにも@Domainで注釈できるようにしました
* `GH70 <https://github.com/domaframework/doma/pull/70>`_
  EntityListenerの取得はEntityListenerProviderを介するようにしました
* `GH67 <https://github.com/domaframework/doma/pull/67>`_
  SQL Server の OPTION 句が存在するとページングが正しく実行されない問題を修正しました
* `GH66 <https://github.com/domaframework/doma/pull/66>`_
  ネーミング規約の適用をコンパイル時から実行時に変更
* `GH64 <https://github.com/domaframework/doma/pull/64>`_
  イミュータブルなエンティティの取得でNullPointerException が発生するバグを修正しました
* `GH61 <https://github.com/domaframework/doma/pull/61>`_
  SQL Server 2012 から追加された OFFSET-FETCH をページング処理に使う
* `GH60 <https://github.com/domaframework/doma/pull/60>`_
  Mssql2008Dialect の getName() が返す値を変更しました
* `GH59 <https://github.com/domaframework/doma/pull/59>`_
  Windows環境でテストが失敗する問題を修正
* `GH58 <https://github.com/domaframework/doma/pull/58>`_
  StringUtilのfromCamelCaseToSnakeCaseで、カラム名に数字が含まれている場合意図している結果にならない

v2.1.0: 2014-12-30
======================

* `GH51 <https://github.com/domaframework/doma/issues/51>`_
  LocalTransactionManager#notSupported()が新規のトランザクションを作成する不具合を修正しました
* `GH50 <https://github.com/domaframework/doma/pull/50>`_
  SQLコメントを使ってSQLに識別子を追記できるようにしました
* `GH49 <https://github.com/domaframework/doma/pull/49>`_
  Gradleのプロジェクトの名前を"doma"に設定しました
* `GH48 <https://github.com/domaframework/doma/pull/48>`_
  `/*%expand` と `*/` の間のスペースはaliasとみなさないようにしました

v2.0.1: 2014-08-06
======================

* ``DomainConverter`` の第2型引数に ``byte[]`` を指定すると注釈処理でコンパイル
  エラーになる問題を修正しました

v2.0.0: 2014-07-02
======================

* ``UnitOfWork`` を削除しました

v2.0-beta-5: 2014-06-07
========================

* ``List<Optional<Emp>>`` や ``List<Optional<Map<String, Object>>>`` を戻り値とする
  Dao メソッドは注釈処理でコンパイルエラーにしました
* Entity 更新後に OriginalStates へ変更が反映されない問題を修正しました
* エンティティの識別子の値がすでに設定されている場合は自動生成処理を実行しないようにしました
* カラムリスト展開コメント で DOMA4257 エラーになる問題を修正しました
* SQLのログ出力方法をアノテーションで制御できるようにしました
* Dao から出力されるログのメッセージを詳細化しました
* ``UtilLoggingJdbcLogger`` のロガーの名前をクラスの完全修飾名に変更しました
* SQL実行時にSQLファイルのパスがログに出力されない問題を修正しました

v2.0-beta-4: 2014-05-04
========================

* Pluggable Annotation Processing API の Visitor を Java 8 用のものへバージョンアップしました
* 空の ``java.util.Iterable`` を IN 句にバインドする場合は SQL の ``null`` として扱うようにしました
* ``java.sql.SQLXML`` に対応しました
* ``LocalTransaction`` で指定したセーブポイント「以降」を削除すべき箇所で「以前」を削除している不具合を修正しました
* ``LocalTransaction`` でセーブポイント削除時のログが間違っている不具合を修正しました
* Entity のプロパティの型を byte 配列にすると注釈処理に失敗する不具合を修正しました

v2.0-beta-3: 2014-04-03
========================

* 検索結果を ``java.util.stream.Collector`` で処理できるようにしました。
* ``LocalTransactionManager`` から ``TransactionManager`` インタフェースを抽出しました。
* ``Config`` で指定した設定が一部無視される不具合を修正しました。
* マップのネーミング規約を一律制御するためのインタフェース ``MapKeyNaming`` を追加しました。
* ``java.time.LocalDate`` 、 ``java.time.LocalTime`` 、 ``java.time.LocalDateTime``
  を基本型として使用できるようにしました。
* ``JdbcLogger`` の実装の差し替えを容易にするために ``AbstractJdbcLogger`` を追加しました。
* ``SelectStrategyType`` の名前を ``SelectType`` に変更しました。
