package org.seasar.doma.jdbc.domain;

import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;

/**
 * A description for a domain type.
 *
 * <p>The implementation class is not required to be thread safe.
 *
 * @param <BASIC> the basic type
 * @param <DOMAIN> the domain type
 */
public interface DomainType<BASIC, DOMAIN> {

  /**
   * Returns the basic class.
   *
   * <p>This method may return a primitive type, so the return type is not {@code Class<BASIC>} but
   * {@code Class<?>}.
   *
   * @return the basic class
   */
  Class<?> getBasicClass();

  /**
   * Returns the domain class.
   *
   * @return the domain class
   */
  Class<DOMAIN> getDomainClass();

  /**
   * Create a scalar.
   *
   * @return the scalar
   */
  Scalar<BASIC, DOMAIN> createScalar();

  /**
   * Create a scalar that has the initial value.
   *
   * @param value the initial value
   * @return the scalar
   */
  Scalar<BASIC, DOMAIN> createScalar(DOMAIN value);

  /**
   * Create an optional scalar.
   *
   * @return the optional scalar
   */
  Scalar<BASIC, Optional<DOMAIN>> createOptionalScalar();

  /**
   * Create an optional scalar that has the initial value.
   *
   * @param value the initial value
   * @return the optional scalar
   */
  Scalar<BASIC, Optional<DOMAIN>> createOptionalScalar(DOMAIN value);

  default Supplier<Scalar<BASIC, DOMAIN>> createScalarSupplier() {
    return this::createScalar;
  }

  default Supplier<Scalar<BASIC, Optional<DOMAIN>>> createOptionalScalarSupplier() {
    return this::createOptionalScalar;
  }
}
