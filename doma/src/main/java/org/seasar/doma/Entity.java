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

import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.NamingConventionType;
import org.seasar.doma.jdbc.entity.NullEntityListener;

/**
 * テーブルもしくは結果セットを示します。
 * <p>
 * 注釈されたクラスは、 次の制約を満たす必要があります。
 * <ul>
 * <li>トップレベルのクラスである。
 * <li>非 {@code private} のクラスである。
 * <li>引数なしの 非 {@code private} なコンストラクタを持つ。
 * </ul>
 * <p>
 * 注釈されたクラスのメンバフィールドは、 次の制約を満たす必要があります。
 * <ul>
 * <li>非 {@code private} である。
 * </ul>
 * 
 * <h5>例:</h5>
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
 * <p>
 * 注釈されたインタフェースの実装はスレッドセーフであることを要求されません。
 * <p>
 * 
 * @author taedium
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
     * リスナーです。
     * <p>
     * 指定しない場合、デフォルトのリスナーが設定されます。 ここに指定したクラスは、エンティティクラスごとに1つだけインスタンス化されます。
     */
    Class<? extends EntityListener<?>> listener() default NullEntityListener.class;

    /**
     * ネーミング規約です。
     * <p>
     * 指定しない場合、デフォルトのネーミング規約が設定されます。
     */
    NamingConventionType namingConvention() default NamingConventionType.NONE;
}
