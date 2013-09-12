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
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.MappedPropertyNotFoundException;
import org.seasar.doma.jdbc.ResultMappingException;

/**
 * ストアドファンクションの呼び出しを示します。
 * <p>
 * このアノテーションが注釈されるメソッドは、Daoインタフェースのメンバでなければいけません。
 * 
 * 注釈されるメソッドは、次の制約を満たす必要があります。
 * <ul>
 * <li>パラメータは0個以上である。
 * <li>パラメータには、パラメータの種別を示す {@link In} 、 {@link InOut} 、 {@link Out} 、
 * {@link ResultSet} のいずれかのアノテーションが指定される。これらは、ストアドファンクションの定義に合わせて注釈しなければいけない。
 * <ul>
 * <li>{@code In}は、INパラメータを表す。
 * <li> {@code InOut}は、INOUTパラメータを表す。
 * <li> {@code Out}は、 OUTパラメータを表す。
 * <li> {@code ResultSet} は、カーソルのOUTパラメータ、もしくはストアドファンクションが返す結果セットを表す。
 * </ul>
 * <li>戻り値の型には次のいずれかを指定できる。
 * <ul>
 * <li>{@code void}
 * <li>基本型。
 * <li>ドメインクラス。
 * <li>{@link List}。実型引数は、基本型、ドメインクラス、エンティティクラス、もしくは @code{Map<String, Object>}
 * のいずれかでなければならない。ただし、戻り値を{@code List}
 * にできるのは、ストアドファンクションがカーソルをファンクションの実行結果として返す場合のみである。
 * </ul>
 * </ul>
 * 
 * <h5>例:</h5>
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
 * <ul>
 * <li> {@link DomaNullPointerException} パラメータに {@code null}を渡した場合
 * <li> {@link MappedPropertyNotFoundException} {@code ResultSet}
 * を使用していて結果セットに含まれるカラムにマッピングされたプロパティが見つからなかった場合
 * <li> {@link JdbcException} JDBCに関する例外が発生した場合
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
     * カタログ名です。
     */
    String catalog() default "";

    /**
     * スキーマ名です。
     */
    String schema() default "";

    /**
     * ストアドファンクションの名前です。
     * <p>
     * 指定しない場合、注釈されたメソッドの名前が使用されます。
     */
    String name() default "";

    /**
     * クエリタイムアウト（秒）です。
     * <p>
     * 指定しない場合、{@link Config#getQueryTimeout()}が使用されます。
     * 
     * @see Statement#setQueryTimeout(int)
     */
    int queryTimeout() default -1;

    /**
     * 結果セットを @code{Map<Object, String>} もしくは @code{List<Map<Object, String>>}
     * として取得する場合のマップのキーに対するネーミング規約です。
     * 
     * @since 1.7.0
     */
    MapKeyNamingType mapKeyNaming() default MapKeyNamingType.NONE;

    /**
     * 結果がエンティティやエンティティのリストの場合、エンティティのすべてのプロパティに結果セットのカラムがマッピングされることを保証します。
     * <p>
     * {@code true} の場合、マッピングされないプロパティが存在すれば、このアノテーションが注釈されたメソッドから
     * {@link ResultMappingException} がスローされます。
     * 
     * @since 1.34.0
     */
    boolean ensureResultMapping() default false;
}
