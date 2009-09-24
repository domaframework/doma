/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import org.seasar.doma.internal.domain.DomainType;
import org.seasar.doma.internal.domain.DomainTypeFactory;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;

/**
 * @author taedium
 * 
 */
public class DomainIterationHandler<R, V, D> implements ResultSetHandler<R> {

    protected final DomainTypeFactory<V, D> domainTypeFactory;

    protected final IterationCallback<R, D> iterationCallback;

    public DomainIterationHandler(DomainTypeFactory<V, D> domainTypeFactory,
            IterationCallback<R, D> iterationCallback) {
        assertNotNull(domainTypeFactory, iterationCallback);
        this.domainTypeFactory = domainTypeFactory;
        this.iterationCallback = iterationCallback;
    }

    @Override
    public R handle(ResultSet resultSet, Query query) throws SQLException {
        ValueFetcher fetcher = new ValueFetcher(query);
        IterationContext iterationContext = new IterationContext();
        R result = null;
        while (resultSet.next()) {
            DomainType<V, D> domainType = domainTypeFactory.createDomainType();
            fetcher.fetch(resultSet, domainType.getWrapper());
            result = iterationCallback.iterate(domainType.getDomain(),
                    iterationContext);
            if (iterationContext.isExited()) {
                return result;
            }
        }
        return result;
    }

}
