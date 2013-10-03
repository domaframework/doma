Doma - Domain Oriented MApping Framework
========================================

Doma is a Database access framework in Java. 

Doma uses [Annotation Processing Tool][apt] to generate source code and validate sql mappings in compile time.

Versions
--------

Doma 1 は安定版です。Java 6 以上で動作します。
- http://doma.seasar.org/

Doma 2 は開発版です。Java 8 で動作します。以下のブランチで開発中です。 
- 2.0-beta-1 branch

Projects
--------

<dl>
	<dt>doma</dt>
	<dd>フレームワーク本体</dd>
	<dt>doma-it</dt>
	<dd>各種 RDBMS を使った結合テスト用プロジェクト</dd>
</dl>

Related Repositories
--------------------

<dl>
	<dt>[doma-gen](https://github.com/seasarorg/doma-gen)</dt>
	<dd>データベースのメタデータからコードを生成ツール</dd>
		<dt>[doma-tools](https://github.com/seasarorg/doma-tools)</dt>
	<dd>Dao クラスと SQL ファイル の相互遷移を可能にする Eclipse プラグイン</dd>
		<dt>[doma-samples](https://github.com/seasarorg/doma-samples)</dt>
	<dd>サンプル</dd>
</dl>

License
-------

Apache License, Version 2.0

  [apt]: http://docs.oracle.com/javase/6/docs/technotes/guides/apt/index.html
