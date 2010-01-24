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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * アノテーションを示します。
 * 
 * @author taedium
 * @see AnnotateWith
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Annotation {

    /** 注釈する対象 */
    AnnotationTarget target();

    /** アノテーションの型 */
    Class<? extends java.lang.annotation.Annotation> type();

    /**
     * アノテーションの要素
     * <p>
     * 「要素名 = 値」 形式で文字列を記述します。 複数存在する場合はカンマで区切ります
     */
    String elements() default "";
}
