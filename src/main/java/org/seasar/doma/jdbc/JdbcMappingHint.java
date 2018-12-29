package org.seasar.doma.jdbc;

import java.util.Optional;

/** A hint about mapping. */
public interface JdbcMappingHint {

  /**
   * Returns the domain class if this object is mapped to the domain class.
   *
   * @return the domain class
   */
  Optional<Class<?>> getDomainClass();
}
