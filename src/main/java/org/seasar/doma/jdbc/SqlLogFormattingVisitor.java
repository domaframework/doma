package org.seasar.doma.jdbc;

import org.seasar.doma.wrapper.Wrapper;
import org.seasar.doma.wrapper.WrapperVisitor;

/**
 * A visitor that converts the {@link Wrapper} values to the SQL log formats.
 *
 * <p>The implementation class must be thread safe.
 */
public interface SqlLogFormattingVisitor
    extends WrapperVisitor<String, SqlLogFormattingFunction, Void, RuntimeException> {}
