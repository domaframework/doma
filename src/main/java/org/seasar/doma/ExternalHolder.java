package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.doma.jdbc.holder.HolderConverter;

/**
 * Indicates to handle an arbitrary class as if it was a {@link Holder}
 * annotated class.
 * <p>
 * The annotated class must implement {@link HolderConverter}.
 * <p>
 * In the bellow code, the SalaryConverter class handles the Salary class as a
 * {@link Holder} annotated class:
 * 
 * <pre>
 * &#064;ExternalHolder
 * public class SalaryConverter implements HolderConverter&lt;Salary, BigDecimal&gt; {
 * 
 *     public BigDecimal fromHolderToValue(Salary holder) {
 *         return holder.getValue();
 *     }
 * 
 *     public Salary fromValueToHolder(BigDecimal value) {
 *         return new Salary(value);
 *     }
 * }
 * </pre>
 * <p>
 * The annotated instance is required to be thread safe.
 * 
 * @see Holder
 * @see HolderConverter
 * @see HolderConverters
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExternalHolder {
}
