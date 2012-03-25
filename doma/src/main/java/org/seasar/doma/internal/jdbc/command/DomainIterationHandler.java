/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.query.SelectQuery;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;
import org.seasar.doma.jdbc.NoResultException;
import org.seasar.doma.jdbc.PostIterationCallback;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.jdbc.domain.DomainWrapper;

/**
 * @author taedium
 * 
 */
public class DomainIterationHandler<R, D> implements ResultSetHandler<R> {

    protected final DomainType<?, D> domainType;

    protected final IterationCallback<R, D> iterationCallback;

    public DomainIterationHandler(DomainType<?, D> domainType,
            IterationCallback<R, D> iterationCallback) {
        assertNotNull(domainType, iterationCallback);
        this.domainType = domainType;
        this.iterationCallback = iterationCallback;
    }

    @Override
    public R handle(ResultSet resultSet, SelectQuery query) throws SQLException {
        BasicFetcher fetcher = new BasicFetcher(query);
        IterationContext iterationContext = new IterationContext();
        boolean existent = false;
        R result = null;
        while (resultSet.next()) {
            existent = true;
            DomainWrapper<?, D> wrapper = domainType.getWrapper(null);
            fetcher.fetch(resultSet, wrapper);
            result = iterationCallback.iterate(wrapper.getDomain(),
                    iterationContext);
            if (iterationContext.isExited()) {
                return result;
            }
        }
        if (query.isResultEnsured() && !existent) {
            Sql<?> sql = query.getSql();
            throw new NoResultException(query.getConfig()
                    .getExceptionSqlLogType(), sql);
        }
        if (iterationCallback instanceof PostIterationCallback) {
            result = ((PostIterationCallback<R, D>) iterationCallback)
                    .postIterate(result, iterationContext);
        }
        return result;
    }

}
