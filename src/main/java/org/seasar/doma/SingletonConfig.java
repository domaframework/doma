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
 * Indicates that the annotated class is an implementation class of the
 * {@link Config} interface and it is a singleton.
 * <p>
 * The annotated class must have a static method that returns a singleton. The
 * all constructors of the class must be private.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SingletonConfig {

    /**
     * The static method name that returns a singleton.
     * <p>
     * The method must meet following requirements:
     * <ul>
     * <li>public and static</li>
     * <li>the return type is same as the annotated class</li>
     * <li>has no parameter</li>
     * </ul>
     */
    String method() default "singleton";
}
