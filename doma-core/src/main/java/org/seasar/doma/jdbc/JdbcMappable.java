package org.seasar.doma.jdbc;

import org.seasar.doma.wrapper.Wrapper;

/**
 * An object that is mappable to an SQL type value.
 *
 * @param <BASIC> the basic type
 */
public interface JdbcMappable<BASIC> extends JdbcMappingHint {

  /**
   * Returns the wrapper.
   *
   * @return the wrapper
   */
  Wrapper<BASIC> getWrapper();
}
