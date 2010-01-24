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

/**
 * 委譲を示します。
 * <p>
 * このアノテーションが注釈されるメソッドは、 Daoインタフェースのメンバでなければいけません。
 * <p>
 * {@code to} 要素に指定されたクラスのインスタンスは、注釈されたメソッドが実行されるたびにインスタンス化されます。
 * インスタンス化後、注釈されたメソッドと同じシグニチャのメソッドが実行されます。
 * 
 * <p>
 * {@code to} 要素に指定されるクラスは次の制約を満たさなければいけません。
 * <ul>
 * <li> {@link Config} 型の1つのパラメータ、もしくは{@code Config}
 * 型と呼び出し元のDaoインタフェースの2つのパラメータを受け取る {@code public} なコンストラクタをもつ。
 * <li>注釈されたメソッドと同じシグニチャのメソッドをもつ。
 * <li>スレッドセーフである。
 * </ul>
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 * 
 *     &#064;Delegate(to = EmployeeDaoDelegate.class)
 *     int execute(Employee employee);
 * }
 * </pre>
 * <p>
 * コンストラクタのパラメータが{@link Config} 型
 * 
 * <pre>
 * public class EmployeeDaoDelegate {
 * 
 *     private final Config config;
 * 
 *     public EmployeeDaoDelegate(Config config) {
 *         this.config = config;
 *     }
 * 
 *     public int execute(Employee employee) {
 *         ...
 *     }
 * }
 * </pre>
 * <p>
 * コンストラクタのパラメータが{@link Config} 型とDaoインタフェース
 * 
 * <pre>
 * public class EmployeeDaoDelegate {
 * 
 *     private final Config config;
 * 
 *     private final EmployeeDao dao;
 * 
 *     public EmployeeDaoDelegate(Config config, EmployeeDao dao) {
 *         this.config = config;
 *         this.dao = dao;
 *     }
 * 
 *     public int execute(Employee employee) {
 *         ...
 *     }
 * }
 * </pre>
 * 
 * @author taedium
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface Delegate {

    /**
     * 委譲先のクラスです。
     * <p>
     * 指定できるクラスには制約があります。
     */
    Class<?> to();
}
