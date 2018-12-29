package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 楽観的排他制御で使用されるバージョンを示します。
 *
 * <p>このアノテーションが注釈されるフィールドは、エンティティインタフェースのメンバでなければいけません。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *     ...
 *
 *     &#064;Version
 *     &#064;Column(name = &quot;VERSION_NO&quot;)
 *     int versionNo;
 * }
 * </pre>
 *
 * @author taedium
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface Version {}
