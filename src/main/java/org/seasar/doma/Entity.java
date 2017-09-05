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

import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.NullEntityListener;

/**
 * Indicates an entity class.
 * <p>
 * The entity class represents a database relation (table or SQL result set). An
 * instance of the class represents a row.
 * <p>
 * The entity class can be defined as either mutable or immutable.
 * 
 * The mutable entity:
 * 
 * <pre>
 * &#064;Entity
 * public class Employee {
 * 
 *     &#064;Id
 *     &#064;Column(name = &quot;ID&quot;)
 *     Integer id;
 * 
 *     &#064;Column(name = &quot;EMPLOYEE_NAME&quot;)
 *     String employeeName;
 * 
 *     &#064;Version
 *     &#064;Column(name = &quot;VERSION&quot;)
 *     int version;
 *     
 *     ...
 * }
 * </pre>
 * 
 * The immutable entity:
 * 
 * <pre>
 * &#064;Entity(immutable = true)
 * public class Employee {
 * 
 *     &#064;Id
 *     &#064;Column(name = &quot;ID&quot;)
 *     final Integer id;
 * 
 *     &#064;Column(name = &quot;EMPLOYEE_NAME&quot;)
 *     final String employeeName;
 * 
 *     &#064;Version
 *     &#064;Column(name = &quot;VERSION&quot;)
 *     final int version;
 * 
 *     public Employee(Integer id, String employeeName, int version) {
 *         this.id = id;
 *         this.employeeName = employeeName;
 *         this.version = version;
 *     }
 *     ...
 * }
 * </pre>
 * <p>
 * The entity instance is not required to be thread safe.
 * 
 * @see Table
 * @see Column
 * @see Id
 * @see Transient
 * @see Version
 * @see OriginalStates
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {

    /**
     * The entity listener class.
     * <p>
     * If not specified and the entity class inherits another entity class, this
     * value is inherited from the parent entity class.
     * <p>
     * An instance of the entity lister class is instantiated only once per
     * entity class.
     */
    @SuppressWarnings("rawtypes")
    Class<? extends EntityListener> listener() default NullEntityListener.class;

    /**
     * The naming convention that maps the entity class to the database table.
     * <p>
     * If not specified and the entity class inherits another entity class, this
     * value is inherited from the parent entity class.
     */
    NamingType naming() default NamingType.NONE;

    /**
     * Whether the entity class is immutable.
     * <p>
     * If not specified and the entity class inherits another entity class, this
     * value is inherited from the parent entity class. The values must be
     * consistent in the hierarchy.
     */
    boolean immutable() default false;
}
