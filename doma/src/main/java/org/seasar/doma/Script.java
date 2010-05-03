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

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.ScriptException;
import org.seasar.doma.jdbc.ScriptFileNotFoundException;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * SQLスクリプトの実行を示します。
 * <p>
 * このアノテーションが注釈されるメソッドは、Daoインタフェースのメンバでなければいけません。
 * <p>
 * 注釈されるメソッドは次の制約を満たす必要があります。
 * <ul>
 * <li>パラメータは受け取らない。
 * <li>戻り値の型は {@code void} である。
 * </ul>
 * <p>
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 * 
 *     &#064;Script
 *     void createTables();
 * }
 * </pre>
 * 
 * 注釈されるメソッドは、次の例外をスローすることがあります。
 * <ul>
 * <li> {@link ScriptFileNotFoundException} スクリプトファイルが見つからなかった場合
 * <li> {@link ScriptException} スクリプトファイルの実行中に例外が発生した場合
 * <li> {@link JdbcException} 上記以外でJDBCに関する例外が発生した場合
 * </ul>
 * 
 * @author taedium
 * @since 1.7.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface Script {

    /**
     * SQLのブロックの区切り文字です。
     * <p>
     * SQLのブロックとはステートメントの集合です。一般的に、プロシージャーやトリガーの定義の終了を示すために使用されます。
     * <p>
     * 空文字が指定されている場合、 {@link Dialect#getScriptBlockDelimiter()} の値が使用されます。
     */
    String blockDelimiter() default "";

    /**
     * スクリプトの実行中にエラーが発生した場合、即座に処理を終了するかどうかを示します。
     */
    boolean haltOnError() default true;
}
