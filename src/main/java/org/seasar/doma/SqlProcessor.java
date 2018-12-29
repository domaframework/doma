package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SQLテンプレートで組み立てられたSQLを扱うことを示します。
 *
 * <p>このアノテーションが注釈されるメソッドは、Daoインタフェースのメンバでなければいけません。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 *     &#64;SqlProcessor
 *     &lt;R&gt; R process(Integer id, BiFunction&lt;Config, PreparedSql, R&gt; handler);
 * }
 * </pre>
 *
 * @author nakamura
 * @since 2.14.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface SqlProcessor {}
