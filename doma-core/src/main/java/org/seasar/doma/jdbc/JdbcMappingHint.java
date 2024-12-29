package org.seasar.doma.jdbc;

import java.util.Optional;
import org.seasar.doma.jdbc.type.JdbcType;

/** A hint about mapping. */
public interface JdbcMappingHint {

  /**
   * Returns the domain class if this object is mapped to the domain class.
   *
   * @return the domain class
   */
  Optional<Class<?>> getDomainClass();

  /**
   * Returns the JDBC type if the target value is mapped to the JDBC type.
   *
   * @return the JDBC type
   */
  default Optional<JdbcType<Object>> getJdbcType() {
    return Optional.empty();
  }
}
