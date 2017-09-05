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

import org.seasar.doma.jdbc.id.BuiltinSequenceIdGenerator;
import org.seasar.doma.jdbc.id.SequenceIdGenerator;

/**
 * Indicates an identifier generator that uses a sequence.
 * <p>
 * The annotated field must be a member of an {@link Entity} annotated class.
 * This annotation must be used in conjunction with the {@link Id} annotation
 * and the {@link GeneratedValue} annotation.
 * <p>
 * 
 * <pre>
 * &#064;Entity
 * public class Employee {
 * 
 *     &#064;Id
 *     &#064;GeneratedValue(strategy = GenerationType.SEQUENCE)
 *     &#064;SequenceGenerator(sequence = &quot;EMPLOYEE_SEQ&quot;)
 *     Integer id;
 *     
 *     ...
 * }
 * </pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SequenceGenerator {

    /**
     * The catalog name.
     */
    String catalog() default "";

    /**
     * The schema name.
     */
    String schema() default "";

    /**
     * The sequence name.
     */
    String sequence();

    /**
     * The initial value.
     */
    long initialValue() default 1;

    /**
     * The allocation size.
     */
    long allocationSize() default 1;

    /**
     * The implementation class of the {@link SequenceIdGenerator} interface.
     */
    Class<? extends SequenceIdGenerator> implementer() default BuiltinSequenceIdGenerator.class;
}
