package org.seasar.doma.jdbc;

import org.seasar.doma.wrapper.Wrapper;

/** @author nakamura-to */
public interface JdbcMappable<BASIC> extends JdbcMappingHint {

  Wrapper<BASIC> getWrapper();
}
