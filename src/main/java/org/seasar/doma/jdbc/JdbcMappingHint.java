package org.seasar.doma.jdbc;

import java.util.Optional;

/** A hint about mapping. */
public interface JdbcMappingHint {

  /**
   * Returns the holder class if this object is mapped to the holder class.
   *
   * @return the holder class
   */
  Optional<Class<?>> getHolderClass();
}
