package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.jdbc.domain.DomainConverter;

/**
 * 任意のクラスをドメインクラスとして扱うことを示します。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;ExternalDomain
 * public class SalaryConverter implements DomainConverter&lt;Salary, BigDecimal&gt; {
 *
 *     public BigDecimal fromDomainToValue(Salary domain) {
 *         return domain.getValue();
 *     }
 *
 *     public Salary fromValueToDomain(BigDecimal value) {
 *         return new Salary(value);
 *     }
 * }
 * </pre>
 *
 * 注釈されたクラスはスレッドセーフでなければいけません。
 *
 * @author taedium
 * @since 1.25.0
 * @see DomainConverter
 * @see DomainConverters
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExternalDomain {}
