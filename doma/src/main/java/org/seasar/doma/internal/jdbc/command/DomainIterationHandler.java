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

import org.seasar.doma.domain.Wrapper;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.DomaMessageCode;


/**
 * @author taedium
 * 
 */
public class DomainIterationHandler<R, D extends Wrapper<?, ?>> implements
        ResultSetHandler<R> {

    protected final Class<D> domainClass;

    protected final IterationCallback<R, D> iterationCallback;

    public DomainIterationHandler(Class<D> domainClass,
            IterationCallback<R, D> iterationCallback) {
        assertNotNull(domainClass);
        this.domainClass = domainClass;
        this.iterationCallback = iterationCallback;
    }

    @Override
    public R handle(ResultSet resultSet, Query query) throws SQLException {
        DomainFetcher fetcher = new DomainFetcher(query);
        IterationContext iterationContext = new IterationContext();
        R result = null;
        while (resultSet.next()) {
            D domain = null;
            try {
                domain = ClassUtil.newInstance(domainClass);
            } catch (WrapException e) {
                Throwable cause = e.getCause();
                throw new JdbcException(DomaMessageCode.DOMA2006, cause,
                        domainClass.getName(), cause);
            }
            fetcher.fetch(resultSet, domain);
            result = iterationCallback.iterate(domain, iterationContext);
            if (iterationContext.isExited()) {
                return result;
            }
        }
        return result;
    }

}
