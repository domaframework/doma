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

import org.seasar.doma.jdbc.Reference;

/**
 * Indicates an OUT parameter of stored functions or stored procedures.
 * <p>
 * The annotated parameter type must be {@link Reference} and it must be one of
 * parameters of the method that is annotated with {@link Function} or
 * {@link Procedure}.
 * <p>
 * 
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 * 
 *     &#064;Procedure
 *     void updateSalary(&#064;In Integer id, &#064;Out Reference&lt;BigDecimal&gt; salary);
 * }
 * </pre>
 * 
 * <pre>
 * EmployeeDao dao = new EmployeeDaoImpl();
 * Reference&lt;BigDecimal&gt; salaryRef = new Reference&lt;BigDecimal&gt;();
 * dao.updateSalary(1, salaryRef);
 * BigDecimal salary = salaryRef.get();
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Out {
}
