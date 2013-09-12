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

import org.seasar.doma.jdbc.ResultMappingException;

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
 * <li>型は {@link List} である。 {@code List} の実型引数は、基本型、 ドメインクラス 、エンティティクラス、もしくは
 * {@code Map<String, Object> } のいずれかでなければならない。
 * </ul>
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 * 
 *     &#064;Procedure
 *     void fetchEmployees(@In Integer departmentId,
 *             &#064;ResultSet List&lt;Employee&gt; employees);
 * }
 * </pre>
 * 
 * <pre>
 * EmployeeDao dao = new EmployeeDaoImpl();
 * List&lt;Employee&gt; employees = new ArrayList&lt;Employee&gt;();
 * dao.fetchEmployees(10, employees);
 * for (Employee e : employees) {
 *     ...
 * }
 * </pre>
 * 
 * @author taedium
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultSet {

    /**
     * 結果がエンティティやエンティティのリストの場合、エンティティのすべてのプロパティに結果セットのカラムがマッピングされることを保証します。
     * <p>
     * {@code true} の場合、マッピングされないプロパティが存在すれば、このアノテーションが注釈されたパラメータを持つメソッドから
     * {@link ResultMappingException} がスローされます。
     * 
     * @since 1.34.0
     */
    boolean ensureResultMapping() default false;
}
