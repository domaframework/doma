/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.jdbc.id.BuiltinTableIdGenerator;
import org.seasar.doma.jdbc.id.TableIdGenerator;

/**
 * テーブルを利用する識別子ジェネレータを示します。
 *
 * <p>このアノテーションが注釈されるフィールドは、エンティティクラスのメンバでなければいけません。 このアノテーションは {@link Id} 、 {@link GeneratedValue}
 * と併わせて使用しなければいけません。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *
 *     &#064;Id
 *     &#064;GeneratedValue(strategy = GenerationType.TABLE)
 *     &#064;TableGenerator(pkColumnValue = &quot;EMPLOYEE_ID&quot;)
 *     Integer id;
 *
 *     ...
 * }
 * </pre>
 *
 * @author taedium
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableGenerator {

  /**
   * カタログ名を返します。
   *
   * @return カタログ名
   */
  String catalog() default "";

  /**
   * シーケンス名を返します。
   *
   * @return シーケンス名
   */
  String schema() default "";

  /**
   * テーブル名を返します。
   *
   * @return テーブル名
   */
  String table() default "ID_GENERATOR";

  /**
   * 主キーのカラムの名前を返します。
   *
   * @return 主キーのカラムの名前
   */
  String pkColumnName() default "PK";

  /**
   * 生成される識別子を保持するカラムの名前を返します。
   *
   * @return 生成される識別子を保持するカラムの名前
   */
  String valueColumnName() default "VALUE";

  /**
   * 主キーのカラムの値を返します。
   *
   * @return 主キーのカラムの値
   */
  String pkColumnValue();

  /**
   * 初期値を返します。
   *
   * @return 初期値
   */
  long initialValue() default 1;

  /**
   * 割り当てサイズを返します。
   *
   * @return 割り当てサイズ
   */
  long allocationSize() default 1;

  /**
   * ジェネレータの実装クラスを返します。
   *
   * @return ジェネレータの実装クラス
   */
  Class<? extends TableIdGenerator> implementer() default BuiltinTableIdGenerator.class;
}
