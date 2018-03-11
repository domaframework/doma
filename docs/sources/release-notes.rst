==============
リリースノート
==============

v2.19.2: 2018-03-11
======================

* `GH220 <https://github.com/domaframework/doma/pull/220>`_
  Fix broken local transaction

v2.19.1: 2018-01-08
======================

* `GH216 <https://github.com/domaframework/doma/pull/216>`_
  Document that the auto-generated value is set only if the identity field is either null or less than 0
* `GH215 <https://github.com/domaframework/doma/pull/215>`_
  Fix TypeDeclaration#removeOverriddenMethodDeclarations to prevent IllegalStateException

v2.19.0: 2017-11-19
======================

* `GH211 <https://github.com/domaframework/doma/pull/211>`_
  Use `CharSequence` as parameter in expression functions
* `GH210 <https://github.com/domaframework/doma/pull/210>`_
  Support private methods in DAO classes

v2.18.0: 2017-10-28
======================

* `GH208 <https://github.com/domaframework/doma/pull/208>`_
  Use `javax.annotation.processing.Generated` on JDK 9
* `GH207 <https://github.com/domaframework/doma/pull/207>`_
  If the result of the expression is `Long`, it is evaluated as `Float`.
* `GH206 <https://github.com/domaframework/doma/pull/206>`_
  Fix a wrong annotation parameter in a Docs example. 
* `GH205 <https://github.com/domaframework/doma/pull/205>`_
  Remove full-width `％` and `＿` from wild cards in Oracle dialect

v2.17.0: 2017-09-09
======================

* `GH203 <https://github.com/domaframework/doma/pull/203>`_
  Add `@TenantId` to support the partitioned approach of multi-tenancy
* `GH202 <https://github.com/domaframework/doma/pull/202>`_
  Update url
* `GH200 <https://github.com/domaframework/doma/pull/200>`_
  Fix typo
* `GH199 <https://github.com/domaframework/doma/pull/199>`_
  Fix the use of the wrong class `java.lang.reflect.Modifier`

v2.16.1: 2017-05-14
======================

* `GH196 <https://github.com/domaframework/doma/pull/196>`_
  Kotlin 1.1.2 を実験的にサポート
* `GH195 <https://github.com/domaframework/doma/pull/195>`_
  Oracle 12c 以降でIDENTITYを使った識別子の自動生成をサポート
* `GH194 <https://github.com/domaframework/doma/pull/194>`_
  SelectBuilder に params メソッドと literals メソッドを追加 

v2.16.0: 2017-02-19
======================

* `GH191 <https://github.com/domaframework/doma/pull/191>`_
  設定ファイルから注釈処理のオプションを読み込めるように

v2.15.0: 2017-02-05
======================

* `GH184 <https://github.com/domaframework/doma/pull/184>`_
  Long name serial sequence
* `GH187 <https://github.com/domaframework/doma/pull/187>`_
  Eclipse 4.6.2 で検出されたワーニングを修正
* `GH188 <https://github.com/domaframework/doma/pull/188>`_
  Dao においてデフォルトメソッドのみが定義されたインタフェースを継承可能に変更

v2.14.0: 2017-01-14
======================

* `GH183 <https://github.com/domaframework/doma/pull/183>`_
  BatchUpdateExecutor, BatchDeleteExecutor, MapBatchInsertBuilder を追加
* `GH182 <https://github.com/domaframework/doma/pull/182>`_
  エンベッダブルクラスにプロパティを定義しない場合に生成されるコードがコンパイルエラーになっていたのを修正
* `GH181 <https://github.com/domaframework/doma/pull/181>`_
  SQLテンプレートで組み立てられたSQLを扱うための `@SqlProcessor` を追加
* `GH180 <https://github.com/domaframework/doma/pull/180>`_
  Lombok をサポート
* `GH179 <https://github.com/domaframework/doma/pull/179>`_
  StandardExpressionFunctions#escapeがescapeCharを使用していない
* `GH177 <https://github.com/domaframework/doma/pull/177>`_
  Kotlin 1.0.6対応
* `GH176 <https://github.com/domaframework/doma/pull/176>`_
  BatchInsertExecutorを追加
* `GH175 <https://github.com/domaframework/doma/pull/175>`_
  組み込み関数の LocalDate, LocalDateTime 対応
* `GH174 <https://github.com/domaframework/doma/pull/174>`_
  Mapをパラメータとして自動でInsert文を組み立てるMapInsertBuilderを追加

v2.13.0: 2016-11-13
======================

* `GH170 <https://github.com/domaframework/doma/pull/170>`_
  識別子をプリミティブ型にする場合の注意点を記載
* `GH167 <https://github.com/domaframework/doma/pull/167>`_
  Doma 2 における主要な変更点に記載されたクラス名の間違いを修正


v2.12.1: 2016-08-06
======================

* `GH165 <https://github.com/domaframework/doma/pull/165>`_
  エンティティクラスの継承構造が2段階の場合に無限ループが発生する不具合を修正

v2.12.0: 2016-07-14
======================

* `GH161 <https://github.com/domaframework/doma/pull/161>`_
  SQLファイルのキャッシュを削除するためのメソッドを追加
* `GH160 <https://github.com/domaframework/doma/pull/160>`_
  エンベッダブルクラスをネストした型として定義することをサポート
* `GH159 <https://github.com/domaframework/doma/pull/159>`_
  エンティティクラスをネストした型として定義することをサポート
* `GH158 <https://github.com/domaframework/doma/pull/158>`_
  ドキュメントのコピーライトの日付を更新
* `GH156 <https://github.com/domaframework/doma/pull/156>`_
  SQLファイルの存在チェックでパスの大文字小文字を区別するように修正
* `GH155 <https://github.com/domaframework/doma/pull/155>`_
  fix typo

v2.11.0: 2016-06-18
======================

* `GH153 <https://github.com/domaframework/doma/pull/153>`_
  クエリビルダでリテラルの埋め込みをサポート
* `GH151 <https://github.com/domaframework/doma/pull/151>`_
  リテラル変数コメントの直後のテスト用リテラルに対するチェックを修正
* `GH150 <https://github.com/domaframework/doma/pull/150>`_
  リテラル変数コメントの機能を追加

v2.10.0: 2016-05-28
======================

* `GH146 <https://github.com/domaframework/doma/pull/146>`_
  Embeddable なオブジェクトが null の場合に更新系の処理が失敗する不具合を修正
* `GH145 <https://github.com/domaframework/doma/pull/145>`_
  Kotlin のサポートについてドキュメントを追加
* `GH142 <https://github.com/domaframework/doma/pull/142>`_
  エンベッダブルクラスのドキュメントを追加
* `GH141 <https://github.com/domaframework/doma/pull/141>`_
  エンティティクラスが継承をしている場合の親プロパティへのアクセス方法を簡易化
* `GH140 <https://github.com/domaframework/doma/pull/140>`_
  プリミティブ型のプロパティにnullがアサインされる場合に例外が発生していた不具合をデフォルト値が設定されるように修正
* `GH139 <https://github.com/domaframework/doma/pull/139>`_
  `@Embeddable` をサポート
* `GH138 <https://github.com/domaframework/doma/pull/138>`_
  Kotlin でイミュータブルなエンティティを定義する際 `@ParameterName` を使用する必要性を除去

v2.9.0: 2016-05-16
======================

* `GH136 <https://github.com/domaframework/doma/pull/136>`_
  更新結果を表すクラスで Kotlin の Destructuring Declarations に対応
* `GH135 <https://github.com/domaframework/doma/pull/135>`_
  注釈処理で出力するメッセージに、クラス、メソッド、フィード名など出力元の情報を追加
* `GH134 <https://github.com/domaframework/doma/pull/134>`_
  `@Dao` に Singleton をフィールドで提供するタイプの Config を指定することをサポート
* `GH133 <https://github.com/domaframework/doma/pull/133>`_
  kapt 1.0.1-2の実験的なサポート
* `GH132 <https://github.com/domaframework/doma/pull/132>`_
  Switching remote URLs from SSH to HTTPS
* `GH131 <https://github.com/domaframework/doma/pull/131>`_
  無名パッケージに配置したクラスの注釈処理に失敗する不具合を修正
* `GH130 <https://github.com/domaframework/doma/pull/130>`_
  テストコードの改善

v2.8.0: 2016-04-16
======================

* `GH127 <https://github.com/domaframework/doma/pull/127>`_
  ドメインクラスをネストした型として定義することをサポート

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
