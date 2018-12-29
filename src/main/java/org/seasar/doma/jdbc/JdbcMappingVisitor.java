package org.seasar.doma.jdbc;

import java.sql.SQLException;
import org.seasar.doma.wrapper.Wrapper;
import org.seasar.doma.wrapper.WrapperVisitor;

/**
 * {@link Wrapper} をJDBCの型とマッピングする {@link WrapperVisitor} の拡張です。
 *
 * <p>このインタフェースの実装はスレッドセーフでなければいけません。
 *
 * @author taedium
 */
public interface JdbcMappingVisitor
    extends WrapperVisitor<Void, JdbcMappingFunction, JdbcMappingHint, SQLException> {}
