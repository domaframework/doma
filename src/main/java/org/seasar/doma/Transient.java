package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 非永続化対象を示します。
 *
 * <p>注釈されたフィールドは、テーブルのカラムに対応付けされません。一時的な値 を保持するのに適しています。
 *
 * <p>このアノテーションが注釈されるメソッドは、{@link Entity} が注釈されたインタフェースのメンバでなければいけません。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *     ...
 *     &#064;Transient
 *     Integer tempNumber;
 *
 *     ...
 * }
 * </pre>
 *
 * @author taedium
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface Transient {}
