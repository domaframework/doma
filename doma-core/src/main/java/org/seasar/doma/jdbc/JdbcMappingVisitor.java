package org.seasar.doma.jdbc;

import java.sql.SQLException;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;
import org.seasar.doma.wrapper.WrapperVisitor;

/**
 * A visitor that maps between {@link Wrapper} and {@link JdbcType}.
 *
 * <p>The implementation class must be thread safe.
 */
public interface JdbcMappingVisitor
    extends WrapperVisitor<Void, JdbcMappingFunction, JdbcMappingHint, SQLException> {}
