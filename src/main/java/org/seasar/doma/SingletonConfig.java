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
 * {@link Config} の実装クラスがシングルトンであることを示します。
 * <p>
 * このアノテーションが注釈されたクラスは、自身のシングルトンを提供する static なメソッドを持たなければいけません。
 * 
 * @author nakamura-to
 * @since 2.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SingletonConfig {

    /**
     * シングルトンを提供するメソッドの名前です。
     * <p>
     * 対応するメソッドは次の条件を満たす必要があります。
     * <ul>
     * <li>修飾子として public static を持つ</li>
     * <li>戻り値の型はこのアノテーションが注釈されたクラス</li>
     * <li>パラメータの数は0</li>
     * </ul>
     * 
     * @return シングルトンを提供するメソッドの名前
     */
    String method() default "singleton";
}
