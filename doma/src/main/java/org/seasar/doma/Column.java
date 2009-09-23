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

import org.seasar.doma.jdbc.NamingConvention;

/**
 * データベースのテーブルのカラムを示します。
 * <p>
 * このアノテーションが注釈されるメソッドは、 {@link Entity} もしくは {@link MappedSuperclass}
 * が注釈されたインタフェースのメンバでなければいけません。
 * <p>
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;Entity
 * public interface Employee {
 * 
 *     &#064;Column(name = &quot;EMPLOYEE_NAME&quot;)
 *     BuiltinStringDomain employeeName();
 * 
 *     &#064;Column(name = &quot;SALARY&quot;)
 *     BuiltinBigDecimalDomain salary();
 * }
 * </pre>
 * 
 * @author taedium
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * カラム名です。
     * <p>
     * 指定しない場合、カラム名は
     * {@link NamingConvention#fromPropertyToColumn(String, org.seasar.doma.jdbc.dialect.Dialect)}
     * によって解決されます。
     */
    String name() default "";

    /**
     * INSERT文に含めるかどうかを示します。
     */
    boolean insertable() default true;

    /**
     * UPDATE文のSET句に含めるかどうかを示します。
     */
    boolean updatable() default true;

}
