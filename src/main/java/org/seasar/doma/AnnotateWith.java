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
 * Daoインタフェースの実装クラスのソースコードにアノテーションを注釈することを示します。
 * <p>
 * このアノテーションには2種類の使い方があります
 * <ul>
 * <li>Daoインタフェースに直接的に注釈する方法。
 * <li>Daoインタフェースに間接的に注釈する方法。この方法では、任意のアノテーションに{@code AnnotateWith}
 * を注釈し、そのアノテーションをDaoインタフェースに注釈する。
 * </ul>
 * <p>
 * このアノテーションを直接的であれ間接的であれDaoインタフェースに注釈する場合、{@link Dao#config()} に値を設定してはいけません。
 * 
 * @author taedium
 * 
 */
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotateWith {

    /**
     * アノテーションの配列を返します。
     * 
     * @return アノテーションの配列
     */
    Annotation[] annotations();
}
