package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.doma.jdbc.holder.HolderConverter;

/**
 * Indicates an aggregation of {@link HolderConverter} classes.
 * <p>
 * The full qualified name of the annotated class must be specified in
 * annotation processing options. The option key is
 * {@code doma.holder.converters}.
 * <p>
 * 
 * <pre>
 * &#064;HolderConverters({ SalaryConverter.class, DayConverter.class, LocationConverter.class })
 * public class HolderConvertersProvider {
 * }
 * </pre>
 * 
 * @see HolderConverter
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HolderConverters {

    /**
     * The aggregation of {@code HolderConverter} classes.
     */
    Class<? extends HolderConverter<?, ?>>[] value() default {};
}
