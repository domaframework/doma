=============
Release notes
=============

v2.29.0: 2020-04-05
======================

* `GH353 <https://github.com/domaframework/doma/pull/353>`_
  Remove a resource bundle
* `GH352 <https://github.com/domaframework/doma/pull/352>`_
  Escape a single quotation to make format success
* `GH351 <https://github.com/domaframework/doma/pull/351>`_
  Update sample build.gradle to make it work on Gradle 6.2.2 without any warnings
* `GH350 <https://github.com/domaframework/doma/pull/350>`_
  Enable incremental annotation processing for the DataTypeProcessor
* `GH349 <https://github.com/domaframework/doma/pull/349>`_
  Upgrade to Gradle 6.2.2

v2.28.0: 2020-03-18
======================

* `GH347 <https://github.com/domaframework/doma/pull/347>`_
  Stored functions return null when they must return the basic type of list
* `GH346 <https://github.com/domaframework/doma/pull/346>`_
  Add the DataType annotation
* `GH344 <https://github.com/domaframework/doma/pull/344>`_
  Support records
* `GH343 <https://github.com/domaframework/doma/pull/343>`_
  Fix some mistakes in document

v2.27.1: 2020-02-07
======================

* `GH341 <https://github.com/domaframework/doma/pull/341>`_
  Fix broken link in document
* `GH340 <https://github.com/domaframework/doma/pull/340>`_
  Fixed that schema name was not used

v2.27.0: 2020-01-25
======================

* `GH338 <https://github.com/domaframework/doma/pull/338>`_
  Reuse CharBuffer instances 
* `GH332 <https://github.com/domaframework/doma/pull/332>`_
  Needs default constructor to AbstractDao to use quarkus

v2.26.0: 2019-12-29
======================

* `GH330 <https://github.com/domaframework/doma/pull/330>`_
  Clarify support for Java 13
* `GH329 <https://github.com/domaframework/doma/pull/329>`_
  Replace table aliases in the ORDER BY clause

v2.25.1: 2019-08-25
======================

* `GH324 <https://github.com/domaframework/doma/pull/324>`_
  Fix null dereference

v2.25.0: 2019-08-25
======================

* `GH322 <https://github.com/domaframework/doma/pull/322>`_
  Describe settings for IntelliJ IDEA Community Edition 2019.2.1
* `GH321 <https://github.com/domaframework/doma/pull/321>`_
  Support domain mapping using ExternalDomain of Java Array
* `GH314 <https://github.com/domaframework/doma/pull/314>`_
  Upgrade google-java-format to 1.7
* `GH313 <https://github.com/domaframework/doma/pull/313>`_
  Upgrade spotless-plugin-gradle to 3.20.0
* `GH312 <https://github.com/domaframework/doma/pull/312>`_
  Clarify support for Java 12
* `GH311 <https://github.com/domaframework/doma/pull/311>`_
  Introduce wrapper classes dedicated to primitive types
* `GH310 <https://github.com/domaframework/doma/pull/310>`_
  Upgrade JUnit to 5.4.0

v2.24.0: 2019-02-23
======================

* `GH308 <https://github.com/domaframework/doma/pull/308>`_
  Implement toString methods to make debug easy
* `GH307 <https://github.com/domaframework/doma/pull/307>`_
  Polish the org.seasar.doma.internal package
* `GH306 <https://github.com/domaframework/doma/pull/306>`_
  Remove Japanese comments 
* `GH305 <https://github.com/domaframework/doma/pull/305>`_
  Register the option "doma.config.path" to suppress a warning message
* `GH304 <https://github.com/domaframework/doma/pull/304>`_
  Simplify the DefaultPropertyType class
* `GH303 <https://github.com/domaframework/doma/pull/303>`_
  Simplify generators
* `GH301 <https://github.com/domaframework/doma/pull/301>`_
  Fix compile error in procedure and function methods
* `GH300 <https://github.com/domaframework/doma/pull/300>`_
  Support bound type parameters in domain classes
* `GH299 <https://github.com/domaframework/doma/pull/299>`_
  Migrate from JUnit 3 to JUnit 5
* `GH298 <https://github.com/domaframework/doma/pull/398>`_
  Polish build script 

v2.23.0: 2019-02-03
======================

* `GH294 <https://github.com/domaframework/doma/pull/294>`_
  Support array types for bind and literal variable directives
* `GH276 <https://github.com/domaframework/doma/pull/276>`_
  Support array types for loop directives
* `GH286 <https://github.com/domaframework/doma/pull/286>`_
  Support automatic removal of unnecessary ORDER BY and GROUP BY clauses
* `GH295 <https://github.com/domaframework/doma/pull/295>`_
  Polish messages
* `GH284 <https://github.com/domaframework/doma/pull/284>`_
  Preserve the auto-commit state of a JDBC connection
* `GH283 <https://github.com/domaframework/doma/pull/283>`_
  Run tests in each annotation processor instance
* `GH296 <https://github.com/domaframework/doma/pull/296>`_
  `GH293 <https://github.com/domaframework/doma/pull/293>`_
  `GH292 <https://github.com/domaframework/doma/pull/292>`_
  `GH291 <https://github.com/domaframework/doma/pull/291>`_
  `GH290 <https://github.com/domaframework/doma/pull/290>`_
  `GH289 <https://github.com/domaframework/doma/pull/289>`_
  `GH288 <https://github.com/domaframework/doma/pull/288>`_
  `GH287 <https://github.com/domaframework/doma/pull/287>`_
  `GH285 <https://github.com/domaframework/doma/pull/285>`_
  `GH282 <https://github.com/domaframework/doma/pull/282>`_
  Polish documents

v2.22.0: 2019-01-20
======================

* `GH278 <https://github.com/domaframework/doma/pull/278>`_
  Remove unused Dockerfile
* `GH272 <https://github.com/domaframework/doma/pull/272>`_
  Add experimental Sql annotation
* `GH279 <https://github.com/domaframework/doma/pull/279>`_
  `GH277 <https://github.com/domaframework/doma/pull/277>`_
  `GH274 <https://github.com/domaframework/doma/pull/274>`_
  `GH273 <https://github.com/domaframework/doma/pull/273>`_
  `GH272 <https://github.com/domaframework/doma/pull/272>`_
  `GH270 <https://github.com/domaframework/doma/pull/270>`_
  `GH269 <https://github.com/domaframework/doma/pull/269>`_
  Translate documents from Japanese into English

v2.21.0: 2019-01-06
======================

* `GH267 <https://github.com/domaframework/doma/pull/267>`_
  Change the description of Doma
* `GH266 <https://github.com/domaframework/doma/pull/266>`_
  Remove descriptions of Doma 3
* `GH265 <https://github.com/domaframework/doma/pull/265>`_
  Polish annotation processing
* `GH264 <https://github.com/domaframework/doma/pull/264>`_
  Improve build script example
* `GH263 <https://github.com/domaframework/doma/pull/263>`_
  Translate resource bundle messages from Japanese into English
* `GH262 <https://github.com/domaframework/doma/pull/262>`_
  Translate javadoc comments from Japanese into English
* `GH261 <https://github.com/domaframework/doma/pull/261>`_
  Change the sphinx langulage option
* `GH260 <https://github.com/domaframework/doma/pull/260>`_
  Translate development.rst
* `GH259 <https://github.com/domaframework/doma/pull/259>`_
  Format with google-java-format 1.6
* `GH258 <https://github.com/domaframework/doma/pull/258>`_
  Translate docs from Japanese into English
* `GH257 <https://github.com/domaframework/doma/pull/258>`_
  Use Sphinx 1.8.2

v2.20.0: 2018-12-23
======================

* `GH255 <https://github.com/domaframework/doma/pull/255>`_
  Support Gradle incremental annotation processing
* `GH254 <https://github.com/domaframework/doma/pull/254>`_
  Specify supported Java versions
* `GH253 <https://github.com/domaframework/doma/pull/253>`_
  Explain how to write gradle build script in Gradle 5.0
* `GH252 <https://github.com/domaframework/doma/pull/252>`_
  Polish Gradle configuration
* `GH251 <https://github.com/domaframework/doma/pull/251>`_
  Use Gradle 5.0 
* `GH248 <https://github.com/domaframework/doma/pull/248>`_
  Fix mistake in abbreviation for JRE
* `GH247 <https://github.com/domaframework/doma/pull/247>`_
  Load a class with Class.forName when the context class loader fails to load the class
* `GH245 <https://github.com/domaframework/doma/pull/245>`_
  Revise wrong descriptions about `@Update.excludeNull`, `@BatchUpdate.include` and `@BatchUpdate.exclude`

v2.19.3: 2018-09-02
======================

* `GH242 <https://github.com/domaframework/doma/pull/242>`_
  Remove wrapper task
* `GH241 <https://github.com/domaframework/doma/pull/241>`_
  Upgrade Gradle version
* `GH240 <https://github.com/domaframework/doma/pull/240>`_
  Refactor gradle script for eclipse
* `GH239 <https://github.com/domaframework/doma/pull/239>`_
  Resolve an external domain class by traversing class hierarchy
* `GH225 <https://github.com/domaframework/doma/pull/225>`_
  Add documents for IntelliJ IDEA
* `GH223 <https://github.com/domaframework/doma/pull/223>`_
  Fix Javadoc comment for Update#includeUnchanged

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
