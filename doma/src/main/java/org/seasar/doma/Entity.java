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
 * エンティティクラスを示します。エンティティクラスのインスタンスは、テーブルもしくは結果セットのレコードを表現します。
 * <p>
 * ミュータブルなエンティティクラスは、 次の制約を満たす必要があります。
 * <ul>
 * <li>トップレベルのクラスである。
 * <li>引数なしの 非 {@code private} なコンストラクタを持つ。
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
 * <p>
 * イミュータブルなエンティティクラスは、 次の制約を満たす必要があります。
 * <ul>
 * <li>トップレベルのクラスである。
 * <li>非 {@code private} なコンストラクタを持ち、コンストラクタのパラメータの型と名前は永続フィールドに対応する。
 * </ul>
 * 
 * <h5>例:</h5>
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
     * この要素に値を指定しない場合、エンティティクラスが他のエンティティクラスを継承しているかどうかで採用する設定が変わります。
     * <ul>
     * <li>継承している場合、親エンティティクラスの設定を引き継ぎます</li>
     * <li>継承していない場合、デフォルトの設定を使用します</li>
     * </ul>
     * <p>
     * リスナーは、エンティティクラスごとに1つだけインスタンス化されます。
     */
    @SuppressWarnings("rawtypes")
    Class<? extends EntityListener> listener() default NullEntityListener.class;

    /**
     * ネーミング規約です。
     * <p>
     * この要素に値を指定しない場合、エンティティクラスが他のエンティティクラスを継承しているかどうかで採用する設定が変わります。
     * <ul>
     * <li>継承している場合、親エンティティクラスの設定を引き継ぎます</li>
     * <li>継承していない場合、デフォルトの設定を使用します</li>
     * </ul>
     */
    NamingType naming() default NamingType.NONE;

    /**
     * イミュータブルかどうかです。
     * <p>
     * この機能はEXPERIMETALな機能です。破壊的な仕様変更が行われる可能性があります。
     * <p>
     * この要素に値を指定しない場合、エンティティクラスが他のエンティティクラスを継承しているかどうかで採用する設定が変わります。
     * <ul>
     * <li>継承している場合、親エンティティクラスの設定を引き継ぎます</li>
     * <li>継承していない場合、デフォルトの設定を使用します</li>
     * </ul>
     * <p>
     * ただし、エンティティクラスの継承階層で {@code true} と {@code false} の混在はできません。
     * 
     * @return イミュータブルの場合 {@code true}
     * @since 1.34.0
     */
    boolean immutable() default false;
}
