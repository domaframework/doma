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

import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.Methods;


/**
 * @author taedium
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    static class Default {

        private static final Column column;
        static {
            try {
                column = Methods
                        .getMethod(Default.class, "get", new Class<?>[] {})
                        .getAnnotation(Column.class);
            } catch (WrapException e) {
                throw new DomaUnexpectedException(e.getCause());
            }
        }

        @Column
        public static Column get() {
            return column;
        }
    }

    String name() default "";

    boolean insertable() default true;

    boolean updatable() default true;

}
