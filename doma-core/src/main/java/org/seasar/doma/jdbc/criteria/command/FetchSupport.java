/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.criteria.command;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.command.JdbcValueGetter;
import org.seasar.doma.jdbc.JdbcMappable;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

@Deprecated(forRemoval = true)
public class FetchSupport {

  private final JdbcMappingVisitor jdbcMappingVisitor;

  public FetchSupport(Query query) {
    Objects.requireNonNull(query);
    this.jdbcMappingVisitor = query.getConfig().getDialect().getJdbcMappingVisitor();
  }

  public Object fetch(ResultSet resultSet, JdbcMappable<?> mappable, int index)
      throws SQLException {
    Wrapper<?> wrapper = mappable.getWrapper();
    JdbcValueGetterProxy proxy = new JdbcValueGetterProxy(new JdbcValueGetter(resultSet, index));
    wrapper.accept(jdbcMappingVisitor, proxy, mappable);
    return proxy.rawValue;
  }

  private static class JdbcValueGetterProxy implements JdbcMappingFunction {
    private final JdbcValueGetter jdbcValueGetter;
    Object rawValue;

    JdbcValueGetterProxy(JdbcValueGetter jdbcValueGetter) {
      Objects.requireNonNull(jdbcValueGetter);
      this.jdbcValueGetter = jdbcValueGetter;
    }

    @Override
    public <R, V> R apply(Wrapper<V> wrapper, JdbcType<V> jdbcType) throws SQLException {
      JdbcTypeProxy<V> proxy = new JdbcTypeProxy<>(jdbcType);
      R result = jdbcValueGetter.apply(wrapper, proxy);
      rawValue = proxy.rawValue;
      return result;
    }
  }

  private static class JdbcTypeProxy<T> implements JdbcType<T> {
    private final JdbcType<T> jdbcType;
    T rawValue;

    JdbcTypeProxy(JdbcType<T> jdbcType) {
      Objects.requireNonNull(jdbcType);
      this.jdbcType = jdbcType;
    }

    @Override
    public T getValue(ResultSet resultSet, int index)
        throws DomaNullPointerException, SQLException {
      T value = jdbcType.getValue(resultSet, index);
      rawValue = value;
      return value;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, T value)
        throws SQLException {
      jdbcType.setValue(preparedStatement, index, value);
    }

    @Override
    public void registerOutParameter(CallableStatement callableStatement, int index)
        throws SQLException {
      jdbcType.registerOutParameter(callableStatement, index);
    }

    @Override
    public T getValue(CallableStatement callableStatement, int index) throws SQLException {
      return jdbcType.getValue(callableStatement, index);
    }

    @Override
    public String convertToLogFormat(T value) {
      return jdbcType.convertToLogFormat(value);
    }
  }
}
