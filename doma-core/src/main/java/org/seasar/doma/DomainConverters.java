package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.jdbc.domain.DomainConverter;

/**
 * Indicates an aggregation of {@link DomainConverter} classes.
 *
 * <p>The full qualified name of the annotated class must be specified in annotation processing
 * options. The option key is {@code doma.domain.converters}.
 *
 * <pre>
 * &#064;DomainConverters({ SalaryConverter.class, DayConverter.class, LocationConverter.class })
 * public class DomainConvertersProvider {
 * }
 * </pre>
 *
 * @see DomainConverter
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainConverters {

  /**
   * @return the aggregation of {@code DomainConverter} classes.
   */
  Class<? extends DomainConverter<?, ?>>[] value() default {};
}
