package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * エンティティの識別子を示します。
 *
 * <p>このアノテーションが注釈されるフィールドは、エンティティクラスのメンバでなければいけません。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *
 *     &#064;Id
 *     &#064;Column(name = &quot;ID&quot;)
 *     String id;
 *     ...
 * }
 * </pre>
 *
 * 識別子を自動生成する場合は、 {@link GeneratedValue} を合わせて注釈します。
 *
 * @author taedium
 * @see GeneratedValue
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface Id {}
