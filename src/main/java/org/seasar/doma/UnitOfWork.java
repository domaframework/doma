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
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.doma.jdbc.tx.TransactionAttribute;
import org.seasar.doma.jdbc.tx.TransactionIsolationLevel;

/**
 * ローカルトランザクションを開始することを示します。
 * 
 * @author nakamura-to
 * @since 2.0.0
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface UnitOfWork {

    /**
     * トランザクション属性です。
     * 
     * @return トランザクション属性
     */
    TransactionAttribute attribute() default TransactionAttribute.REQURED;

    /**
     * トランザクション分離レベルです。
     * 
     * @return トランザクション分離レベル
     */
    TransactionIsolationLevel isolationLevel() default TransactionIsolationLevel.DEFAULT;
}
