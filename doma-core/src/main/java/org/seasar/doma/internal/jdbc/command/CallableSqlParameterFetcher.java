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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcMappable;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.ListParameter;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.OutParameter;
import org.seasar.doma.jdbc.ResultListParameter;
import org.seasar.doma.jdbc.SingleResultParameter;
import org.seasar.doma.jdbc.SqlParameter;
import org.seasar.doma.jdbc.SqlParameterVisitor;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.query.ModuleQuery;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.Wrapper;

public class CallableSqlParameterFetcher {

  protected final ModuleQuery query;

  public CallableSqlParameterFetcher(ModuleQuery query) {
    assertNotNull(query);
    this.query = query;
  }

  public void fetch(CallableStatement callableStatement, List<? extends SqlParameter> parameters)
      throws SQLException {
    assertNotNull(callableStatement, parameters);
    FetchingVisitor fetchingVisitor = new FetchingVisitor(query, callableStatement);
    for (SqlParameter parameter : parameters) {
      parameter.accept(fetchingVisitor, null);
    }
  }

  protected static class FetchingVisitor implements SqlParameterVisitor<Void, Void, SQLException> {

    protected final ModuleQuery query;

    protected final Dialect dialect;

    protected final JdbcMappingVisitor jdbcMappingVisitor;

    protected final CallableStatement callableStatement;

    protected int index = 1;

    public FetchingVisitor(ModuleQuery query, CallableStatement callableStatement) {
      this.query = query;
      this.dialect = query.getConfig().getDialect();
      this.jdbcMappingVisitor = dialect.getJdbcMappingVisitor();
      this.callableStatement = callableStatement;
    }

    @Override
    public <BASIC> Void visitInParameter(InParameter<BASIC> parameter, Void p) {
      index++;
      return null;
    }

    @Override
    public <BASIC> Void visitOutParameter(OutParameter<BASIC> parameter, Void p)
        throws SQLException {
      fetchOutParameter(parameter);
      parameter.updateReference();
      index++;
      return null;
    }

    @Override
    public <BASIC, INOUT extends InParameter<BASIC> & OutParameter<BASIC>> Void visitInOutParameter(
        INOUT parameter, Void p) throws SQLException {
      fetchOutParameter(parameter);
      parameter.updateReference();
      index++;
      return null;
    }

    @Override
    public <ELEMENT> Void visitListParameter(ListParameter<ELEMENT> parameter, Void p)
        throws SQLException {
      fetchListParameter(parameter);
      return null;
    }

    @Override
    public <BASIC, RESULT> Void visitSingleResultParameter(
        SingleResultParameter<BASIC, RESULT> parameter, Void p) throws SQLException {
      fetchOutParameter(parameter);
      index++;
      return null;
    }

    @Override
    public <ELEMENT> Void visitResultListParameter(ResultListParameter<ELEMENT> parameter, Void p)
        throws SQLException {
      fetchListParameter(parameter);
      return null;
    }

    protected <BASIC> void fetchOutParameter(JdbcMappable<BASIC> parameter) throws SQLException {
      Wrapper<?> wrapper = parameter.getWrapper();
      wrapper.accept(
          jdbcMappingVisitor, new JdbcOutParameterGetter(callableStatement, index), parameter);
    }

    protected <ELEMENT> void fetchListParameter(ListParameter<ELEMENT> parameter)
        throws SQLException {
      ObjectProvider<ELEMENT> provider = parameter.createObjectProvider(query);
      consumeResultSet(parameter.getName(), resultSet -> parameter.add(provider.get(resultSet)));
    }

    protected void consumeResultSet(String parameterName, ResultSetConsumer consumer)
        throws SQLException {
      if (dialect.supportsResultSetReturningAsOutParameter()) {
        JdbcType<ResultSet> resultSetType = dialect.getResultSetType();
        ResultSet resultSet = resultSetType.getValue(callableStatement, index);
        if (resultSet == null) {
          throw new JdbcException(Message.DOMA2137, index, parameterName, query.getQualifiedName());
        }
        try {
          while (resultSet.next()) {
            consumer.accept(resultSet);
          }
        } finally {
          JdbcUtil.close(resultSet, query.getConfig().getJdbcLogger());
        }
        index++;
      } else {
        ResultSet resultSet = callableStatement.getResultSet();
        while (resultSet == null
            && (callableStatement.getMoreResults() || callableStatement.getUpdateCount() > -1)) {
          resultSet = callableStatement.getResultSet();
        }
        if (resultSet == null) {
          throw new JdbcException(Message.DOMA2136, parameterName, query.getQualifiedName());
        }
        try {
          while (resultSet.next()) {
            consumer.accept(resultSet);
          }
        } finally {
          callableStatement.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
        }
      }
    }
  }

  @FunctionalInterface
  protected interface ResultSetConsumer {
    void accept(ResultSet resultSet) throws SQLException;
  }
}
