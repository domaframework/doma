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

import org.seasar.doma.domain.Wrapper;

/**
 * ストアドファンクションやストアドプロシージャーから返される結果セットにマッピングされることを示します。
 * <p>
 * {@code ResultSet}
 * をカーソルとしてOUTパラメータで返すRDBMSにおいては、注釈されたパラメータは実質的にOUTパラメータとして扱われます
 * 。そうでないRDBMSにおいては、IN、INOUT、OUTのいずれのパラメータにもみなされません。
 * {@link Statement#getResultSet()}で取得される結果セットにマッピングされます。
 * <p>
 * このアノテーションが注釈されるパラメータは、 {@link Function} もしくは {@link Procedure}
 * が注釈されたメソッドのパラメータでなければいけません。
 * 
 * 注釈されるパラメータは、次の制約を満たす必要があります。
 * <ul>
 * <li>型は {@link Wrapper} の実装クラスを要素にもつ {@link List}、もしくは {@link Entity}
 * が注釈されたインタフェースを要素にもつ {@link List} である。
 * </ul>
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 * 
 *     &#064;Procedure
 *     void fetchEmployees(@In BuiltinIntegerDomain departmentId,
 *             &#064;ResultSet List&lt;Employee&gt; employees);
 * }
 * </pre>
 * 
 * @author taedium
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultSet {
}
