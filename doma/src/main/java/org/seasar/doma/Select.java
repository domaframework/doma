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
import java.sql.Statement;
import java.util.List;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.MappedPropertyNotFoundException;
import org.seasar.doma.jdbc.NonUniqueResultException;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlFileNotFoundException;

/**
 * 検索処理を示します。
 * <p>
 * このアノテーションが注釈されるメソッドは、{@link Dao} が注釈されたインタフェースのメンバでなければいけません。 {@code iterate}
 * 要素に {@code false} を指定することで、エンティティを1件ずつ処理することができます。
 * <p>
 * 注釈されるメソッドは、次の制約を満たす必要があります。
 * 
 * <h4>{@code iterate} 要素が {@code false} の場合:</h4>
 * <ul>
 * <li>パラメータは0個以上である。
 * <li>パラメータは基本型、 {@link Domain} が注釈されたクラス、{@link Entity} が注釈されたクラス、もしくは
 * {@link SelectOptions} である。ただし、 {@code SelectOptions} は最大でも1つしか使用できない。
 * <li>戻り値の型は次のいずれかである。なお、 型が {@link List} でなくデータが存在しない場合、値は {@code null} となる。
 * <table border=1>
 * <tr>
 * <th>戻り値の型</th>
 * <th>データが存在しない場合の値</th>
 * <tr>
 * <td>基本型</td>
 * <td>null</td>
 * </tr>
 * <tr>
 * <td>基本型を要素とする {@code List}</td>
 * <td>空の {@code List}</td>
 * </tr>
 * <tr>
 * <td>{@code Domain} が注釈されたクラス</td>
 * <td>null</td>
 * </tr>
 * <tr>
 * <td>{@code Domain} が注釈されたクラスを要素とする {@code List}</td>
 * <td>空の {@code List}</td>
 * </tr>
 * <tr>
 * <td>{@code Entity} が注釈されたクラス</td>
 * <td>null</td>
 * </tr>
 * <tr>
 * <td>{@code Entity} が注釈されたクラスを要素とする {@code List}</td>
 * <td>空の {@code List}</td>
 * </tr>
 * </table>
 * </ul>
 * 
 * <h4>{@code iterate} 要素が {@code true} の場合:</h4>
 * <ul>
 * <li>パラメータは {@link IterationCallback} 型のものが必須である。そのほか、基本型、{@code Domain}
 * が注釈されたクラス、 {@code Entity} が注釈されたクラス、もしくは {@code SelectOptions} を指定できる。ただし、
 * {@code SelectOptions} は最大でも1つしか指定できない。
 * <li>戻り値の型は パラメータで利用する {@link IterationCallback} の型パラメータと同じ型でなければいけない。
 * </ul>
 * 
 * <p>
 * &nbsp;
 * <p>
 * 注釈されるメソッドに定義できるパラメータは型ごとに意味が異なります。
 * <ul>
 * <table border=1>
 * <tr>
 * <th>パラメータの型</th>
 * <th>役割</th>
 * <tr>
 * <td>基本型</td>
 * <td>SQLにバインドする単一の値です。</td>
 * </tr>
 * <tr>
 * <td>{@code Domain} が注釈されたクラス</td>
 * <td>SQLにバインドする単一の値です。</td>
 * </tr>
 * <tr>
 * <td>{@code Entity} が注釈されたクラス</td>
 * <td>SQLにバインドする値の集合です。</td>
 * </tr>
 * <tr>
 * <td>{@code SelectOptions}</td>
 * <td>SELECT文の実行に関するオプション（ページングや悲観的排他制御など）です。</td>
 * </tr>
 * <tr>
 * <td>{@code IterationCallback}</td>
 * <td>検索結果にマッピングされたインスタンスを1件ずつ処理するコールバックです。</td>
 * </tr>
 * </table>
 * </ul>
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;Entity
 * public class Employee {
 *     ...
 * }
 * 
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 * 
 *     &#064;Select
 *     String selectNameById(Integer id);
 *     
 *     &#064;Select
 *     List&lt;Employee&gt; selectNamesByAgeAndSalary(Integer age, BigDecimal salary);
 * 
 *     &#064;Select
 *     Employee selectById(Integer id);
 *     
 *     &#064;Select
 *     List&lt;Employee&gt; selectByExample(Employee example);
 *     
 *     &#064;Select(iterate = true)
 *     &lt;R&gt; R selectSalary(Integer departmentId, IterationCallback&lt;R, BigDecimal&gt; callback);
 * }
 * </pre>
 * 
 * 注釈されるメソッドは、次の例外をスローすることがあります。
 * <ul>
 * <li> {@link DomaNullPointerException} パラメータに {@code null} を渡した場合
 * <li> {@link SqlFileNotFoundException} SQLファイルが見つからなかった場合
 * <li> {@link MappedPropertyNotFoundException}
 * 結果セットに含まれるカラムにマッピングされたプロパティが見つからなかった場合
 * <li> {@link NonUniqueResultException} 戻り値の型が {@code List}
 * でない場合で、かつ結果が2件以上返された場合
 * <li> {@link JdbcException} 上記以外でJDBCに関する例外が発生した場合
 * </ul>
 * 
 * @author taedium
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface Select {

    /**
     * クエリタイムアウト（秒）です。
     * <p>
     * 指定しない場合、 {@link Config#getQueryTimeout()} が使用されます。
     * 
     * @see Statement#setQueryTimeout(int)
     */
    int queryTimeout() default -1;

    /**
     * フェッチサイズです。
     * <p>
     * 指定しない場合、 {@link Config#getFetchSize()} が使用されます。
     * 
     * @see Statement#setFetchSize(int)
     */
    int fetchSize() default -1;

    /**
     * 最大行数の制限値です。
     * <p>
     * 指定しない場合、 {@link Config#getMaxRows()} が使用されます。
     * 
     * @see Statement#setMaxRows(int)
     */
    int maxRows() default -1;

    /**
     * 結果のインスタンスを1件ずつ処理するかどうかを示します。
     * <p>
     * {@code true} の場合、注釈されたメソッドのパラメータに {@link IterationCallback}
     * 型のパラメータを含める必要があります。
     */
    boolean iterate() default false;
}
