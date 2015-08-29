======
式言語
======

.. contents:: 目次
   :depth: 3

:doc:`sql` 中の式コメントには簡易な式を記述できます。
文法はJavaとほとんど同じです。
ただし、Javaで可能なことすべてができるわけではありません。

.. note::

  特に大きな違いは、 ``java.util.Optional`` などのオプショナルな型の扱い方にあります。
  式の中では、 ``Optional`` 型の値は常にその要素の型の値に自動変換されます。
  たとえば、 ``Optional<String>`` 型の値は ``String`` 型の値として扱われます。
  したがって、 ``Optional`` 型のメソッドを呼び出したり、
  ``Optional`` 型をパラメータとするメソッドの呼び出しはできません。

  値の存在の有無を確認する場合は、 ``/*%if optional.isPresent() */`` とする替わりに
  ``/*%if optional != null */`` としてください。

  ``java.util.OptionalInt`` 、 ``java.util.OptionalDouble`` 、 ``java.util.OptionalLong``
  についても同様です。

リテラル
========

以下のリテラルが用意されています。

+----------+----------------------+
| リテラル | 型                   |
+==========+======================+
| null     | void                 |
+----------+----------------------+
| true     | boolean              |
+----------+----------------------+
| false    | boolean              |
+----------+----------------------+
| 10       | int                  |
+----------+----------------------+
| 10L      | long                 |
+----------+----------------------+
| 0.123F   | float                |
+----------+----------------------+
| 0.123D   | double               |
+----------+----------------------+
| 0.123B   | java.math.BigDecimal |
+----------+----------------------+
| 'a'      | char                 |
+----------+----------------------+
| "a"      | java.lang.String     |
+----------+----------------------+

数値の型は、リテラルの最後に ``L`` や ``F`` などのサフィックスを付与して区別します。 
サフィックスは大文字でなければいけません。

.. code-block:: sql

  select * from employee where 
  /*%if employeeName != null && employeeName.length() > 10 */
      employee_name = /* employeeName */'smith'
  /*%end*/

比較演算子
==========

以下の比較演算子を使用できます。

+--------+------------------------+
| 演算子 |   説明                 |
+========+========================+
| ==     |   等値演算子           |
+--------+------------------------+
| !=     |   不等値演算子         |
+--------+------------------------+
| <      |   小なり演算子         |
+--------+------------------------+
| <=     |   小なりイコール演算子 |
+--------+------------------------+
| >      |   大なり演算子         |
+--------+------------------------+
| >=     |   大なりイコール演算子 |
+--------+------------------------+

比較演算子を利用するには、 被演算子が ``java.lang.Comparable`` を実装している必要があります。

``<`` 、 ``<=`` 、 ``>`` 、 ``>=`` では、被演算子が ``null`` であってはいけません。

.. code-block:: sql

  select * from employee where 
  /*%if employeeName.indexOf("s") > -1 */
      employee_name = /* employeeName */'smith'
  /*%end*/

論理演算子
==========

以下の論理演算子を使用できます。

========= ====================
演算子    説明
========= ====================
!         論理否定演算子
&&        論理積演算子
||        論理和演算子
========= ====================

括弧を使って、演算子が適用される優先度を制御できます。

.. code-block:: sql

  select * from employee where 
  /*%if (departmentId == null || managerId == null) and employee_name != null */
      employee_name = /* employeeName */'smith'
  /*%end*/

算術演算子
==========

以下の算術演算子を使用できます。

+--------+---------------+
| 演算子 |    説明       |
+========+===============+
| \+     |    加算演算子 |
+--------+---------------+
| \-     |    減算演算子 |
+--------+---------------+
| \*     |    乗算演算子 |
+--------+---------------+
| /      |    除算演算子 |
+--------+---------------+
| %      |    剰余演算子 |
+--------+---------------+

被演算子は数値型でなければいけません。

.. code-block:: sql

  select * from employee where 
      salary = /* salary + 1000 */0

連結演算子
==============

連結演算子 ``+`` を使って文字を連結できます。

被演算子は次のいずれかの型でなければいけません。

* java.lang.String
* java.lang.Character
* char

.. code-block:: sql

  select * from employee where 
     employee_name like /* employeeName + "_" */'smith'

インスタンスメソッドの呼び出し
==============================

ドット ``.`` で区切ってメソッド名を指定することでインスタンスメソッドを実行可能です。
実行可能なメソッドは可視性がpublicなものだけに限られます。

.. code-block:: sql

  select * from employee where 
  /*%if employeeName.startsWith("s") */
      employee_name = /* employeeName */'smith'
  /*%end*/

引数がない場合はメソッド名の後ろに ``()`` を指定します。

.. code-block:: sql

  select * from employee where 
  /*%if employeeName.length() > 10 */ 
      employee_name = /* employeeName */'smith'
  /*%end*/

インスタンスフィールドへのアクセス
==================================

ドット ``.`` で区切ってフィールド名を指定することでインスタンスフィールドにアクセスできます。
可視性はprivateであってもアクセス可能です。

.. code-block:: sql

  select * from employee where 
      employee_name = /* employee.employeeName */'smith'

staticメソッドの呼び出し
========================

``@`` で囲まれたクラスの完全修飾名にメソッドを続けることでstaticメソッドを実行可能です。
実行可能なメソッドは可視性がpublicなものだけに限られます。

.. code-block:: sql

  select * from employee where 
  /*%if @java.util.regex.Pattern@matches("^[a-z]*$", employeeName) */
      employee_name = /* employeeName */'smith'
  /*%end*/

staticフィールドへのアクセス
============================

``@`` で囲まれたクラスの完全修飾名にフィールドを続けることでstaticフィールドにアクセスできます。
可視性はprivateであってもアクセス可能です。

.. code-block:: sql

  select * from employee where 
  /*%if employeeName.length() < @java.lang.Byte@MAX_VALUE */
    employee_name = /* employeeName */'smith'
  /*%end*/

組み込み関数の使用
==================

組み込み関数は、主に、SQLにバインドする前にバインド変数の値を変更するためのユーティリティです。

たとえば、LIKE句で前方一致検索を行う場合に次のように記述できます。

.. code-block:: sql

  select * from employee where 
      employee_name like /* @prefix(employee.employeeName) */'smith' escape '$'

ここでは、 ``@prefix(employee.employeeName)`` というように、 ``employee.employeeName`` 
を ``@prefix`` 関数に渡しています。
``@prefix`` 関数は、パラメータで受け取る文字列を前方一致検索用の文字列に変換します。
また、特別な意味を持つ文字をエスケープします。
たとえば ``employee.employeeName`` の値が ``ABC`` である場合、 値は ``ABC%`` に変換されます。
もし、 ``employee.employeeName`` の値が ``AB%C`` というように ``%`` を含んでいる場合、
``%`` はデフォルトのエスケープシーケンス ``$`` でエスケープされ、値は ``AB$%C%`` に変換されます。

使用可能な関数のシグネチャは以下のとおりです。

String @escape(String text, char escapeChar = '$')
  LIKE演算のためのエスケープを行うことを示します。
  戻り値は入力値をエスケープした文字列です。
  ``escapeChar`` が指定されない場合、デフォルトのエスケープ文字 ``$`` が使用されます。
  引数に ``null`` を渡した場合、 ``null`` を返します。

String @prefix(String prefix, char escapeChar = '$')
  前方一致検索を行うことを示します。
  戻り値は入力値をエスケープしワイルドカードを後ろに付与した文字列です。
  ``escapeChar`` が指定されない場合、デフォルトのエスケープ文字 ``$`` が使用されます。
  引数に ``null`` を渡した場合、 ``null`` を返します。

String @infix(String infix, char escapeChar = '$')
  中間一致検索を行うことを示します。
  戻り値は入力値をエスケープしワイルドカードを前と後ろに付与した文字列です。
  ``escapeChar`` が指定されない場合、デフォルトのエスケープ文字 ``$`` が使用されます。
  引数に ``null`` を渡した場合、 ``null`` を返します。

String @suffix(String suffix, char escapeChar = '$')
  後方一致検索を行うことを示します。
  戻り値は入力値をエスケープしワイルドカードを前に付与した文字列です。
  ``escapeChar`` が指定されない場合、デフォルトのエスケープ文字 ``$`` が使用されます。
  引数に ``null`` を渡した場合、 ``null`` を返します。

java.util.Date @roundDownTimePart(java.util.Date date)
  時刻部分を切り捨てることを示します。
  戻り値は時刻部分が切り捨てられた新しい日付です。
  引数に ``null`` を渡した場合、 ``null`` を返します。

java.sql.Date @roundDownTimePart(java.sql.Date date)
  時刻部分を切り捨てることを示します。
  戻り値は時刻部分が切り捨てられた新しい日付です。
  引数に ``null`` を渡した場合、 ``null`` を返します。

java.sql.Timestamp @roundDownTimePart(java.sql.Timestamp timestamp)
  時刻部分を切り捨てることを示します。
  戻り値は時刻部分が切り捨てられた新しいタイムスタンプです。
  引数に ``null`` を渡した場合、 ``null`` を返します。

java.util.Date @roundUpTimePart(java.util.Date date)
  時刻部分を切り上げることを示します。
  戻り値は時刻部分が切り上げられた新しい日付です。
  引数に ``null`` を渡した場合、 ``null`` を返します。

java.sql.Date @roundUpTimePart(java.sql.Date date)
  時刻部分を切り上げることを示します。
  戻り値は時刻部分が切り上げられた新しい日付です。
  引数に ``null`` を渡した場合、 ``null`` を返します。

java.sql.Timestamp @roundUpTimePart(java.sql.Timestamp timestamp)
  時刻部分を切り上げることを示します。
  戻り値は時刻部分が切り上げられた新しいタイムスタンプです。
  引数に ``null`` を渡した場合、 ``null`` を返します。

boolean @isEmpty(CharSequence charSequence)
  文字シーケンスが ``null`` 、もしくは文字シーケンスの長さが ``0`` の場合 ``true`` を返します。

boolean @isNotEmpty(CharSequence charSequence)
  文字シーケンスが ``null`` でない、かつ文字シーケンスの長さが ``0`` でない場合 ``true`` を返します。

boolean @isBlank(CharSequence charSequence)
  文字シーケンスが ``null`` 、もしくは文字シーケンスの長さが ``0`` 、
  もしくは文字シーケンスが空白だけから形成される場合 trueを返します。

boolean @isNotBlank(CharSequence charSequence)
  文字シーケンスが ``null`` でない、かつ文字シーケンスの長さが ``0`` でない、
  かつ文字シーケンスが空白だけで形成されない場合 ``true`` を返します。

これらの関数は、 ``org.seasar.doma.expr.ExpressionFunctions`` のメソッドに対応しています。

カスタム関数の使用
==================

関数を独自に定義し使用できます。

独自に定義した関数（カスタム関数）を使用するには次の設定が必要です。

* 関数は、 ``org.seasar.doma.expr.ExpressionFunctions`` を実装したクラスのメソッドとして定義する。
* メソッドはpublicなインスタンスメソッドとする。
* 作成したクラスは :doc:`annotation-processing` のオプションで登録する。
  オプションのキーは ``doma.expr.functions`` である。
* 作成したクラスのインスタンスを設定クラスのRDBMSの方言で使用する
  （Domaが提供するRDBMSの方言の実装はコンストラクタで ``ExpressionFunctions`` を受け取ることが可能）。

カスタム関数を呼び出すには、組み込み関数と同じように関数名の先頭に ``@`` をつけます。
たとえば、 ``myfunc`` という関数の呼び出しは次のように記述できます。

.. code-block:: sql

  select * from employee where 
      employee_name = /* @myfunc(employee.employeeName) */'smith'

