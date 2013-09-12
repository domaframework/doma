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
import java.util.List;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.MappedPropertyNotFoundException;
import org.seasar.doma.jdbc.NoResultException;
import org.seasar.doma.jdbc.NonSingleColumnException;
import org.seasar.doma.jdbc.NonUniqueResultException;
import org.seasar.doma.jdbc.ResultMappingException;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlFileNotFoundException;

/**
 * 検索処理を示します。
 * <p>
 * このアノテーションが注釈されるメソッドは、Daoインタフェースのメンバでなければいけません。 {@code iterate} 要素に
 * {@code true} を指定することで、エンティティを1件ずつ処理することができます。
 * <p>
 * 注釈されるメソッドは、次の制約を満たす必要があります。
 * 
 * <h4>{@code iterate} 要素が {@code false} の場合:</h4>
 * <ul>
 * <li>パラメータは0個以上である。
 * <li>パラメータは任意の型である。ただし、 {@code SelectOptions} は最大でも1つしか使用できない。
 * <li>戻り値の型は次のいずれかである。なお、 型が {@link List} でなくデータが存在しない場合、値は {@code null}
 * となる。ただし、{@link #ensureResult} に {@code true} が指定された場合、データが存在しなければ
 * {@link NoResultException} がスローされる。
 * <table border=1>
 * <tr>
 * <th>戻り値の型</th>
 * <th>データが存在しない場合の値</th>
 * <tr>
 * <td>基本型</td>
 * <td>null</td>
 * </tr>
 * <tr>
 * <td>基本型を実型引数とする {@code List}</td>
 * <td>空の {@code List}</td>
 * </tr>
 * <tr>
 * <td>ドメインクラス</td>
 * <td>null</td>
 * </tr>
 * <tr>
 * <td>ドメインクラスを実型引数とする {@code List}</td>
 * <td>空の {@code List}</td>
 * </tr>
 * <tr>
 * <td>エンティティクラス</td>
 * <td>null</td>
 * </tr>
 * <tr>
 * <td>エンティティクラスを実型引数とする {@code List}</td>
 * <td>空の {@code List}</td>
 * </tr>
 * <tr>
 * <td>{@code Map<String, Object>}</td>
 * <td>null</td>
 * </tr>
 * <tr>
 * <td>{@code Map<String, Object>} を実型引数とする {@code List}</td>
 * <td>空の {@code List}</td>
 * </tr>
 * </table>
 * </ul>
 * 
 * <h4>{@code iterate} 要素が {@code true} の場合:</h4>
 * <ul>
 * <li>パラメータには {@link IterationCallback} 型のものが含まれる。その他のパラメータは任意の型である。ただし、
 * {@code SelectOptions} は最大でも1つしか指定できない。
 * <li>戻り値の型は パラメータで利用する {@code IterationCallback} の１番目の型パラメータと同じ型でなければいけない。
 * {@link #ensureResult} に {@code true} が指定された場合、データが存在しなければ
 * {@link NoResultException} がスローされる。
 * </ul>
 * 
 * <p>
 * &nbsp;
 * <p>
 * {@code SelectOptions} と {@code IterationCallback} は特別な意味を持つパラメータです。
 * <ul>
 * <table border=1>
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
 * <li> {@link NonSingleColumnException} 戻り値の型が、基本型やドメインクラスもしくは基本型やドメインクラスの
 * {@code List} 場合で、かつ結果セットに複数のカラムが含まれている場合
 * <li> {@link NoResultException} {@link #ensureResult} に {@code true}
 * が指定され、結果が0件の場合
 * <li> {@link JdbcException} 上記以外でJDBCに関する例外が発生した場合
 * </ul>
 * 
 * @author taedium
 * @see SelectOptions
 * @see IterationCallback
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

    /**
     * 結果が少なくとも1件以上存在することを保証します。
     * <p>
     * {@code true} の場合に結果が存在しなければ、このアノテーションが注釈されたメソッドから
     * {@link NoResultException} がスローされます。
     */
    boolean ensureResult() default false;

    /**
     * 結果がエンティティやエンティティのリストの場合、エンティティのすべてのプロパティに結果セットのカラムがマッピングされることを保証します。
     * <p>
     * {@code true} の場合、マッピングされないプロパティが存在すれば、このアノテーションが注釈されたメソッドから
     * {@link ResultMappingException} がスローされます。
     * 
     * @since 1.34.0
     */
    boolean ensureResultMapping() default false;

    /**
     * 検索結果を @code{Map<Object, String>} もしくは @code{List<Map<Object, String>>}
     * として取得する場合のマップのキーに対するネーミング規約です。
     * 
     * @since 1.7.0
     */
    MapKeyNamingType mapKeyNaming() default MapKeyNamingType.NONE;
}
