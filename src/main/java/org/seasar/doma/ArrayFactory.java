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
import java.sql.Array;
import java.sql.Connection;
import org.seasar.doma.jdbc.JdbcException;

/**
 * {@link Array} のインスタンスを生成することを示します。
 *
 * <p>このアノテーションが注釈されるメソッドは、 Daoインタフェースのメンバでなければいけません。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 *
 *     &#064;ArrayFactory(typeName = &quot;integer&quot;)
 *     Array createIntegerArray(Integer[] elements);
 * }
 * </pre>
 *
 * 注釈されるメソッドは、次の例外をスローすることがあります。
 *
 * <ul>
 *   <li>{@link DomaNullPointerException} パラメータに {@code null}を渡した場合
 *   <li>{@link JdbcException} JDBCに関する例外が発生した場合
 * </ul>
 *
 * @author taedium
 * @see Connection#createArrayOf(String, Object[])
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface ArrayFactory {

  /**
   * 配列の要素がマッピングされる型のSQL名を返します。。
   *
   * <p>この値は、 {@link Connection#createArrayOf(String, Object[])} の最初のパラメータに渡されます。
   *
   * @return 配列の要素がマッピングされる型のSQL名
   */
  String typeName();
}
