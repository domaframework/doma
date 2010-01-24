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

import org.seasar.doma.jdbc.id.BuiltinTableIdGenerator;
import org.seasar.doma.jdbc.id.TableIdGenerator;

/**
 * テーブルを利用する識別子ジェネレータを示します。
 * <p>
 * <p>
 * このアノテーションが注釈されるフィールドは、エンティティクラスのメンバでなければいけません。 このアノテーションは {@link Id} 、
 * {@link GeneratedValue} と併わせて使用しなければいけません。
 * <p>
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;Entity
 * public class Employee {
 * 
 *     &#064;Id
 *     &#064;GeneratedValue(strategy = GenerationType.TABLE)
 *     &#064;TableGenerator(pkColumnValue = &quot;EMPLOYEE_ID&quot;)
 *     Integer id;
 *     
 *     ...
 * }
 * </pre>
 * 
 * @author taedium
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableGenerator {

    /**
     * カタログ名です。
     */
    String catalog() default "";

    /**
     * シーケンス名です。
     */
    String schema() default "";

    /**
     * テーブル名です。
     */
    String table() default "ID_GENERATOR";

    /**
     * 主キーのカラムの名前です。
     */
    String pkColumnName() default "PK";

    /**
     * 生成される識別子を保持するカラムの名前です。
     */
    String valueColumnName() default "VALUE";

    /**
     * 主キーのカラムの値です。
     */
    String pkColumnValue();

    /**
     * 初期値です。
     */
    long initialValue() default 1;

    /**
     * 割り当てサイズです。
     */
    long allocationSize() default 1;

    /**
     * ジェネレータの実装クラスです。
     */
    Class<? extends TableIdGenerator> implementer() default BuiltinTableIdGenerator.class;
}
