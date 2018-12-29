package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * エンベッダブルクラスを示します。エンベッダブルクラスはエンティティに組み込み可能で複数のプロパティをまとめる役割を担います。
 *
 * <p>エンベッダブルクラスにはすべてのプロパティをパラメータに含んだ非privateなコンストラクタが必要です。
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
 * <p>注釈されたインタフェースの実装はスレッドセーフであることを要求されません。
 *
 * <p>
 *
 * @author nakamura-to
 * @since 2.10.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Embeddable {}
