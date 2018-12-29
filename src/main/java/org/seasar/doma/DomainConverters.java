package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.jdbc.domain.DomainConverter;

/**
 * {@link DomainConverter} を複数登録します。
 *
 * <p>このアノテーションの{@code value} 要素に指定される {@code DomainConverter} のクラスには {@link ExternalDomain}
 * が注釈されていないければいけません。
 *
 * <p>このアノテーションが注釈されたクラスの完全修飾名は、注釈処理のオプションに登録する必要があります。オプションのキーは {@code doma.domain.converters} です。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;DomainConverters({ SalaryConverter.class, DayConverter.class,
 *         LocationConverter.class })
 * public class DomainConvertersProvider {
 * }
 * </pre>
 *
 * @author taedium
 * @since 1.25.0
 * @see DomainConverter
 * @see ExternalDomain
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainConverters {

  /**
   * {@code DomainConverter} のクラスの配列を返します。
   *
   * @return {@code DomainConverter} のクラスの配列
   */
  Class<? extends DomainConverter<?, ?>>[] value() default {};
}
