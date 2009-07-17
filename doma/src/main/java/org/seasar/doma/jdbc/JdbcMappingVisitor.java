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
package org.seasar.doma.jdbc;

import java.sql.SQLException;

import org.seasar.doma.domain.AbstractArrayDomain;
import org.seasar.doma.domain.AbstractBigDecimalDomain;
import org.seasar.doma.domain.AbstractBlobDomain;
import org.seasar.doma.domain.AbstractBooleanDomain;
import org.seasar.doma.domain.AbstractBytesDomain;
import org.seasar.doma.domain.AbstractClobDomain;
import org.seasar.doma.domain.AbstractDateDomain;
import org.seasar.doma.domain.AbstractDoubleDomain;
import org.seasar.doma.domain.AbstractIntegerDomain;
import org.seasar.doma.domain.AbstractNClobDomain;
import org.seasar.doma.domain.AbstractStringDomain;
import org.seasar.doma.domain.AbstractTimeDomain;
import org.seasar.doma.domain.AbstractTimestampDomain;
import org.seasar.doma.domain.BuiltInDomainVisitor;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.type.JdbcTypes;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class JdbcMappingVisitor implements
        BuiltInDomainVisitor<Void, JdbcMappingFunction, SQLException> {

    @Override
    public Void visitAbstractStringDomain(AbstractStringDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        return p.apply(domain, JdbcTypes.STRING);
    }

    @Override
    public Void visitAbstractBigDecimalDomain(
            AbstractBigDecimalDomain<?> domain, JdbcMappingFunction p)
            throws SQLException {
        return p.apply(domain, JdbcTypes.BIGDECIMAL);
    }

    @Override
    public Void visitAbstractDateDomain(AbstractDateDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        return p.apply(domain, JdbcTypes.DATE);
    }

    @Override
    public Void visitAbstractIntegerDomain(AbstractIntegerDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        return p.apply(domain, JdbcTypes.INT);
    }

    @Override
    public Void visitAbstractTimestampDomain(AbstractTimestampDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        return p.apply(domain, JdbcTypes.TIMESTAMP);
    }

    @Override
    public Void visitAbstractTimeDomain(AbstractTimeDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        return p.apply(domain, JdbcTypes.TIME);
    }

    @Override
    public Void visitAbstractArrayDomain(AbstractArrayDomain<?, ?> domain,
            JdbcMappingFunction p) throws SQLException {
        return p.apply(domain, JdbcTypes.ARRAY);
    }

    @Override
    public Void visitAbstractBlobDomain(AbstractBlobDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        return p.apply(domain, JdbcTypes.BLOB);
    }

    @Override
    public Void visitAbstractClobDomain(AbstractClobDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        return p.apply(domain, JdbcTypes.CLOB);
    }

    @Override
    public Void visitAbstractNClobDomain(AbstractNClobDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        return p.apply(domain, JdbcTypes.NCLOB);
    }

    @Override
    public Void visitAbstractBooleanDomain(AbstractBooleanDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        Dialect dialect = p.getConfig().dialect();
        if (dialect.supportsBooleanType()) {
            return p.apply(domain, JdbcTypes.BOOLEAN);
        }
        return p.apply(domain, JdbcTypes.INT_ADAPTIVE_BOOLEAN);
    }

    @Override
    public Void visitAbstractBytesDomain(AbstractBytesDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        return p.apply(domain, JdbcTypes.BYTES);
    }

    @Override
    public Void visitAbstractDoubleDomain(AbstractDoubleDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        return p.apply(domain, JdbcTypes.DOUBLE);
    }

    @Override
    public Void visitUnknownDomain(Domain<?, ?> domain, JdbcMappingFunction p)
            throws SQLException {
        throw new JdbcException(MessageCode.DOMA2019, domain.getClass()
                .getName());
    }
}
