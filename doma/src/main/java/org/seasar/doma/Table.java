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

/**
 * データベースのテーブルを示します。
 * <p>
 * このアノテーションは、エンティティクラスに対して有効です。
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;Entity
 * &#064;Table(name = &quot;EMP&quot;)
 * public class Employee {
 *     ...
 * }
 * </pre>
 * 
 * @author taedium
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * カタログ名です。
     */
    String catalog() default "";

    /**
     * スキーマ名です。
     */
    String schema() default "";

    /**
     * テーブル名です。
     * <p>
     * 指定しない場合、テーブル名は {@link Entity#naming()} に指定した列挙型 によって解決されます。
     */
    String name() default "";
}
