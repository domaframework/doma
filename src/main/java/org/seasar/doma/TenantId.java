package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * テナントの識別子を示します。
 *
 * <p>このアノテーションが注釈されるフィールドは、エンティティインタフェースのメンバでなければいけません。
 *
 * <p>SQLが生成されるタイプのクエリにおいて、注釈されたフィールドにマップされたカラムは検索条件としてWHERE句に含まれます。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *     ...
 *
 *     &#064;TenantId
 *     String tenantId;
 * }
 * </pre>
 *
 * @author nakamura
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface TenantId {}
