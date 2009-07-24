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

import org.seasar.doma.DomaMessageCode;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.NonUniqueResultException;


/**
 * @author taedium
 * 
 */
public class DomainSingleResultHandler<D extends Domain<?, ?>> implements
        ResultSetHandler<D> {

    protected final Class<D> domainClass;

    public DomainSingleResultHandler(Class<D> domainClass) {
        assertNotNull(domainClass);
        this.domainClass = domainClass;
    }

    @Override
    public D handle(ResultSet resultSet, Query query) throws SQLException {
        DomainFetcher fetcher = new DomainFetcher(query);
        D domain = null;
        if (resultSet.next()) {
            try {
                domain = ClassUtil.newInstance(domainClass);
            } catch (WrapException e) {
                Throwable cause = e.getCause();
                throw new JdbcException(DomaMessageCode.DOMA2006, cause,
                        domainClass.getName(), cause);
            }
            fetcher.fetch(resultSet, domain);
            if (resultSet.next()) {
                throw new NonUniqueResultException(query.getSql());
            }
        }
        return domain;
    }

}
