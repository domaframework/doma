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
 * エンベッダブルクラスを示します。エンベッダブルクラスはエンティティに組み込み可能で複数のプロパティをまとめる役割を担います。
 * <p>
 * エンベッダブルクラスにはすべてのプロパティをパラメータに含んだ非privateなコンストラクタが必要です。
 * 
 * <pre>
 * &#064;Embeddable
 * public class Address {
 * 
 *     &#064;Column(name = &quot;CITY&quot;)
 *     private final String city;
 * 
 *     &#064;Column(name = &quot;STREET&quot;)
 *     private final String street;
 *     
 *     public Address(String city, String street) {
 *         this.city = city;
 *         this.street = street;
 *     }
 *     ...
 * }
 * </pre>
 * 
 * <pre>
 * &#064;Entity
 * public class Employee {
 * 
 *     &#064;Id
 *     &#064;Column(name = &quot;ID&quot;)
 *     Integer id;
 * 
 *     Address address;
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
 * @author nakamura-to
 * @since 2.10.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Embeddable {
}
