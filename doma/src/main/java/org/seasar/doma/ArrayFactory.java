/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import org.seasar.doma.domain.Domain;
import org.seasar.doma.jdbc.JdbcException;

/**
 * {@link Array} のインスタンスを生成することを示します。
 * <p>
 * このアノテーションが注釈されるメソッドは、 {@link Dao} が注釈されたインタフェースのメンバでなければいけません。
 * 
 * 注釈されるメソッドは、次の制約を満たす必要があります。
 * <li>パラメータを1つだけ受け取る。
 * <li>パラメータの型は配列である。この配列は {@link Connection#createArrayOf(String, Object[])}
 * の2番目のパラメータに渡される。
 * <li>戻り値の型は {@code Array} を値とする {@link Domain} の実装クラスである。
 * <li>戻り値の型は、 配列の要素の型を型パラメータとして受け取る。
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 * 
 *     &#064;ArrayFactory(typeName = &quot;integer&quot;)
 *     ArrayDomain&lt;Integer&gt; createIntegerArray(Integer[] elements);
 * }
 * </pre>
 * 
 * 注釈されるメソッドは、次の例外をスローすることがあります。
 * <ul>
 * <li> {@link DomaNullPointerException} パラメータに {@code null}を渡した場合
 * <li> {@link JdbcException} JDBCに関する例外が発生した場合
 * </ul>
 * 
 * @author taedium
 * @see Connection#createArrayOf(String, Object[])
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Query
public @interface ArrayFactory {

    /**
     * 配列の要素がマッピングされる型のSQL名です。
     * <p>
     * この値は、 {@link Connection#createArrayOf(String, Object[])} の最初のパラメータに渡されます。
     */
    String typeName();
}
