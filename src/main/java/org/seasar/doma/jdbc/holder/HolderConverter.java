package org.seasar.doma.jdbc.holder;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.HolderConverters;

/**
 * A converter between a value holder and a value.
 *
 * <p>You can use an arbitrary type for the value holder. The value's type must be one of the basic
 * types.
 *
 * <p>The implementation class should be annotated with {@link ExternalHolder} and be registered to
 * {@link HolderConverters}.
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
 *
 * @see ExternalHolder
 * @see HolderConverters
 * @param <HOLDER> the holder type
 * @param <BASIC> the basic type
 */
public interface HolderConverter<HOLDER, BASIC> {

  /**
   * Converts the value holder to the value.
   *
   * @param holder the value holder
   * @return the value
   */
  BASIC fromHolderToValue(HOLDER holder);

  /**
   * Converts the value to the value holder
   *
   * @param value the value
   * @return the value holder
   */
  HOLDER fromValueToHolder(BASIC value);
}
