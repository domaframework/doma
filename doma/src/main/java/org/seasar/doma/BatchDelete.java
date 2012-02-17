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
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.SqlFileNotFoundException;

/**
 * バッチ削除処理を示します。
 * <p>
 * このアノテーションが注釈されるメソッドは、 Daoインタフェースのメンバでなければいけません。
 * <p>
 * 注釈されるメソッドは次の制約を満たす必要があります。
 * <ul>
 * <li>パラメータを1つだけ受け取る。
 * <li>パラメータの型はエンティティクラスを要素とする {@link Iterable} のサブタイプである。
 * <li>戻り値の型は {@code int[]} である。
 * </ul>
 * 
 * <p>
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
 *     &#064;BatchDelete
 *     int[] delete(List&lt;Employee&gt; employee);
 * }
 * </pre>
 * 
 * 注釈されるメソッドは、次の例外をスローすることがあります。
 * <ul>
 * <li> {@link DomaNullPointerException} パラメータに {@code null} を渡した場合
 * <li> {@link OptimisticLockException} 楽観的排他制御が有効なで更新件数が0件の場合
 * <li> {@link SqlFileNotFoundException} {@code sqlFile} 要素の値が {@code true}
 * で、SQLファイルが見つからなかった場合
 * <li> {@link JdbcException} 上記以外でJDBCに関する例外が発生した場合
 * </ul>
 * 
 * @author taedium
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface BatchDelete {

    /**
     * SQLファイルにマッピングするかどうかを示します。
     */
    boolean sqlFile() default false;

    /**
     * クエリタイムアウト（秒）です。
     * <p>
     * 指定しない場合、{@link Config#getQueryTimeout()}が使用されます。
     * 
     * @see Statement#setQueryTimeout(int)
     */
    int queryTimeout() default -1;

    /**
     * バッチサイズです。
     * <p>
     * 指定しない場合、{@link Config#getBatchSize()}が使用されます。
     * <p>
     * {@link PreparedStatement#executeBatch()} を実行する際のバッチサイズです。
     * バッチ対象の数がバッチサイズを上回る場合、バッチサイズの数だけ {@link PreparedStatement#addBatch()}
     * を呼び出し、 {@link PreparedStatement#executeBatch()} を実行するということを繰り返します。
     * 
     * @see PreparedStatement#addBatch()
     * @since 1.21.0
     */
    int batchSize() default -1;

    /**
     * 楽観的排他制御用のバージョン番号を無視するかどうかを示します。
     * <p>
     * {@code true} の場合、削除条件にバージョン番号を含めません。
     */
    boolean ignoreVersion() default false;

    /**
     * 削除結果が1件でない場合にスローされる {@link OptimisticLockException}を抑制するかどうかを示します。
     * <p>
     * この要素に対する指定は、{@link #sqlFile()} が {@code false} の場合にのみ有効です。
     */
    boolean suppressOptimisticLockException() default false;

}
