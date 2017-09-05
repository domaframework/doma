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
 * Indicates a database table.
 * <p>
 * This annotation must be used in conjunction with the {@link Entity}
 * annotation.
 * <p>
 * 
 * <pre>
 * &#064;Entity
 * &#064;Table(name = &quot;EMP&quot;)
 * public class Employee {
 *     ...
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * The catalog name.
     */
    String catalog() default "";

    /**
     * The schema name.
     */
    String schema() default "";

    /**
     * The table name.
     * <p>
     * If not specified, the table name is resolved by {@link Entity#naming()}.
     */
    String name() default "";

    /**
     * Whether quotation marks are used for the catalog name, the schema name
     * and the table name.
     */
    boolean quote() default false;
}
