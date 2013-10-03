Doma
========================================

Doma is a Database access framework. 

Doma uses [Annotation Processing Tool][apt] to generate source code and validate sql mappings in compile time.

Versions
--------

<dl>
	<dt>Doma 1</dt>
	<dd>
	安定版。Java 6 以上で動作する。<br />
	http://doma.seasar.org/</dd>
	<dt>Doma 2</dt>
	<dd>
	開発版。Java 8 で動作する。<br />
	2.0-beta-1 ブランチで開発中</dd>
</dl>

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
	<dt>doma-gen</dt>
	<dd>
	データベースのメタデータからコードを生成するツール<br />
	https://github.com/seasarorg/doma-gen
	</dd>
	<dt>doma-tools</dt>
	<dd>
	Dao クラスと SQL ファイル の相互遷移を可能にする Eclipse プラグイン<br />
	https://github.com/seasarorg/doma-tools
	</dd>
	<dt>doma-samples</dt>
	<dd>
	サンプル<br />
	https://github.com/seasarorg/doma-samples
	</dd>
</dl>

License
-------

Apache License, Version 2.0

  [apt]: http://docs.oracle.com/javase/6/docs/technotes/guides/apt/index.html
