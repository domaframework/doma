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
import java.util.List;

import org.seasar.doma.jdbc.ResultMappingException;

/**
 * Indicates a result set that is fetch by stored functions or stored
 * procedures.
 * <p>
 * The annotated parameter type must be {@link List} and it must be one of
 * parameters of the method that is annotated with {@link Function} or
 * {@link Procedure}.
 * <p>
 * 
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 * 
 *     &#064;Procedure
 *     void fetchEmployees(@In Integer departmentId, &#064;ResultSet List&lt;Employee&gt; employees);
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
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultSet {

    /**
     * Whether to ensure that all entity properties are mapped to columns of a
     * result set.
     * <p>
     * This value is used only if the result set is fetched as an entity or a
     * entity list.
     * <p>
     * If {@code true} and there are some unmapped properties、
     * {@link ResultMappingException} is thrown from the annotated method.
     */
    boolean ensureResultMapping() default false;
}
