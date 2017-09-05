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
 * Indicates that a generated code that implements the annotated interface is
 * annotated with some annotations.
 * <p>
 * This annotation is mainly intended to inject a {@link Config} instance to a
 * DAO implementation's constructor. Don't use {@link Dao#config()} with this
 * annotation.
 * <p>
 * There are 2 ways to use this annotation:
 * <ul>
 * <li>annotate directly
 * <li>annotate indirectly
 * </ul>
 * <p>
 * annotate directly:
 * 
 * <pre>
 * &#64;Dao
 * &#64;AnnotateWith(annotations = {
 *   &#64;Annotation(target = AnnotationTarget.CONSTRUCTOR, type = javax.inject.Inject.class),
 *   &#64;Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = javax.inject.Named.class, elements = "\"config\"") })
 * public interface EmployeeDao {
 *    ...
 * }
 * </pre>
 * 
 * annotate indirectly:
 * 
 * <pre>
 * &#64;AnnotateWith(annotations = {
 *         &#64;Annotation(target = AnnotationTarget.CONSTRUCTOR, type = javax.inject.Inject.class),
 *         &#64;Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = javax.inject.Named.class, elements = "\"config\"") })
 * public @interface InjectConfig {
 * }
 * </pre>
 * 
 * <pre>
 * &#64;Dao
 * &#64;InjectConfig
 * public interface EmployeeDao {
 *    ...
 * }
 * </pre>
 */
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotateWith {

    /**
     * The {@link Annotation} array.
     */
    Annotation[] annotations();
}
