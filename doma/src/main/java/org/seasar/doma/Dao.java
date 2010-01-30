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

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ConfigProxy;

/**
 * Daoインタフェースであることを示します。
 * <p>
 * このアノテーションは、トップレベルのインタフェースに指定できます。Daoインタフェースは他のインタフェースを拡張できません。
 * 
 * <p>
 * インタフェースのメンバメソッドには、メタアノテーション {@link DaoMethod}
 * でマークされたアノテーションのいずれかを指定しなければいけません。
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 * 
 *     &#064;Insert
 *     int insert(Employee employee);
 * }
 * </pre>
 * 
 * 注釈されたインタフェースの実装はスレッドセーフでなければいけません。
 * 
 * @author taedium
 * @see ArrayFactory
 * @see BatchDelete
 * @see BatchInsert
 * @see BatchUpdate
 * @see BlobFactory
 * @see ClobFactory
 * @see Delegate
 * @see Delete
 * @see Function
 * @see Insert
 * @see NClobFactory
 * @see Procedure
 * @see Select
 * @see Update
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dao {

    /**
     * Daoを実行する際の設定（ {@literal JDBC} の接続情報や {@literal RDBMS} の方言等）です。
     * <p>
     * ここに指定するクラスは、引数なしのpublicなコンストラクタを持つ具象クラスでなければいけません。 ただし、例外として、
     * {@link AnnotateWith} と併用する場合は、{@link ConfigProxy} が指定されたとみなされます。 {@code
     * AnnotateWith} と併用する場合は、何も指定しないか、明示的に {@code ConfigProxy} を指定してください。
     * <p>
     * ここに指定するクラスは、Daoインタフェースの実装クラスがインスタンス化されるごとにインスタンス化されます。
     */
    Class<? extends Config> config() default Config.class;

}
