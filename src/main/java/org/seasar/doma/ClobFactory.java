package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Clob;
import java.sql.Connection;
import org.seasar.doma.jdbc.JdbcException;

/**
 * {@link Clob} のインスタンスを生成することを示します。
 *
 * <p>このアノテーションが注釈されるメソッドは、Daoインタフェースのメンバでなければいけません。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;Dao(config = AppConfig.class)
 * public interface EmployeeDao {
 *
 *     &#064;ClobFactory
 *     Clob createClob();
 * }
 * </pre>
 *
 * 注釈されるメソッドは、次の例外をスローすることがあります。
 *
 * <ul>
 *   <li>{@link DomaNullPointerException} パラメータに {@code null}を渡した場合
 *   <li>{@link JdbcException} JDBCに関する例外が発生した場合
 * </ul>
 *
 * @author taedium
 * @see Connection#createClob()
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DaoMethod
public @interface ClobFactory {}
