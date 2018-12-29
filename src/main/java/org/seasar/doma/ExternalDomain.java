package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.jdbc.domain.DomainConverter;

/**
 * Indicates to handle an arbitrary class as if it was a {@link Domain} annotated class.
 *
 * <p>The annotated class must implement {@link DomainConverter}.
 *
 * <p>In the bellow code, the SalaryConverter class handles the Salary class as a {@link Domain}
 * annotated class:
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
 * <p>The annotated instance is required to be thread safe.
 *
 * @see DomainConverter
 * @see DomainConverters
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExternalDomain {}
