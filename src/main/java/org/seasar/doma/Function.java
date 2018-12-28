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
import java.sql.Statement;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.ResultMappingException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UnknownColumnException;

/**
 * ストアドファンクションの呼び出しを示します。
 *
 * <p>このアノテーションが注釈されるメソッドは、Daoインタフェースのメンバでなければいけません。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 *
 *     &#064;Function
 *     BigDecimal getSalary(@In Integer id, @Out Reference&lt;String&gt; name);
 * }
 * </pre>
 *
 * 注釈されるメソッドは、次の例外をスローすることがあります。
 *
 * <ul>
 *   <li>{@link DomaNullPointerException} パラメータに {@code null}を渡した場合
 *   <li>{@link UnknownColumnException} {@code ResultSet}
 *       を使用していて結果セットに含まれるカラムにマッピングされたプロパティが見つからなかった場合
 *   <li>{@link JdbcException} JDBCに関する例外が発生した場合
 * </ul>
 *
 * @author taedium
 * @see In
 * @see InOut
 * @see Out
 * @see ResultSet
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface Function {

  /**
   * カタログ名を返します。
   *
   * @return カタログ名
   */
  String catalog() default "";

  /**
   * スキーマ名を返します。
   *
   * @return スキーマ名
   */
  String schema() default "";

  /**
   * ストアドファンクションの名前を返します。
   *
   * <p>指定しない場合、注釈されたメソッドの名前が使用されます。
   *
   * @return ストアドファンクションの名前
   */
  String name() default "";

  /**
   * カタログ、スキーマ、ストアドファンクション名を引用符で囲むかどうかを返します。
   *
   * @return 引用符で囲むかどうか。
   */
  boolean quote() default false;

  /**
   * クエリタイムアウト（秒）を返します。
   *
   * <p>指定しない場合、{@link Config#getQueryTimeout()}が使用されます。
   *
   * @return クエリタイムアウト（秒）
   * @see Statement#setQueryTimeout(int)
   */
  int queryTimeout() default -1;

  /**
   * 結果セットを {@code Map<Object, String>} もしくは {@code List<Map<Object, String>>}
   * として取得する場合のマップのキーに対するネーミング規約を返します。
   *
   * @return ネーミング規約
   * @since 1.7.0
   */
  MapKeyNamingType mapKeyNaming() default MapKeyNamingType.NONE;

  /**
   * 結果がエンティティやエンティティのリストの場合、 エンティティのすべてのプロパティに結果セットのカラムがマッピングされることを保証するかどうかを返します。
   *
   * <p>{@code true} の場合、マッピングされないプロパティが存在すれば、このアノテーションが注釈されたメソッドから {@link ResultMappingException}
   * がスローされます。
   *
   * @return マッピングされることを保証するかどうか
   * @since 1.34.0
   */
  boolean ensureResultMapping() default false;

  /**
   * SQLのログの出力形式を返します。
   *
   * @return SQLログの出力形式
   * @since 2.0.0
   */
  SqlLogType sqlLog() default SqlLogType.FORMATTED;
}
