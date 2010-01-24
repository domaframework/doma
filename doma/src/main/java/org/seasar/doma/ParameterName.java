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
 * パラメータの名前を示します。
 * <p>
 * クラスファイルにパラメータ名を保持させたい場合に使用します。 {@literal Annotation Plaggable API}
 * では、ソースファイルからパラメータ名を取得できるため
 * 、このアノテーションを利用する機会は稀です。ソースファイルではなく、クラスファイルを処理しなければいけない場合に使用されることを想定しています。
 * 
 * @author taedium
 * 
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterName {

    String value() default "";
}
