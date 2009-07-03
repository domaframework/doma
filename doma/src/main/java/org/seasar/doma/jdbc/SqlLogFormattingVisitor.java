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

import org.seasar.doma.domain.AbstractBigDecimalDomain;
import org.seasar.doma.domain.AbstractBooleanDomain;
import org.seasar.doma.domain.AbstractBytesDomain;
import org.seasar.doma.domain.AbstractDateDomain;
import org.seasar.doma.domain.AbstractIntegerDomain;
import org.seasar.doma.domain.AbstractStringDomain;
import org.seasar.doma.domain.AbstractTimeDomain;
import org.seasar.doma.domain.AbstractTimestampDomain;
import org.seasar.doma.domain.BuiltInDomainVisitor;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.jdbc.domain.AbstractArrayDomain;
import org.seasar.doma.jdbc.domain.AbstractBlobDomain;
import org.seasar.doma.jdbc.domain.AbstractClobDomain;
import org.seasar.doma.jdbc.domain.AbstractNClobDomain;

/**
 * @author taedium
 * 
 */
public class SqlLogFormattingVisitor
        implements
        BuiltInDomainVisitor<String, SqlLogFormattingFunction, RuntimeException> {

    @Override
    public String visitAbstractBigDecimalDomain(
            AbstractBigDecimalDomain<?> domain, SqlLogFormattingFunction p) {
        return p.apply(domain, JdbcTypes.BIGDECIMAL);
    }

    @Override
    public String visitAbstractDateDomain(AbstractDateDomain<?> domain,
            SqlLogFormattingFunction p) {
        return p.apply(domain, JdbcTypes.DATE);
    }

    @Override
    public String visitAbstractIntegerDomain(AbstractIntegerDomain<?> domain,
            SqlLogFormattingFunction p) {
        return p.apply(domain, JdbcTypes.INT);
    }

    @Override
    public String visitAbstractStringDomain(AbstractStringDomain<?> domain,
            SqlLogFormattingFunction p) {
        return p.apply(domain, JdbcTypes.STRING);
    }

    @Override
    public String visitAbstractTimeDomain(AbstractTimeDomain<?> domain,
            SqlLogFormattingFunction p) {
        return p.apply(domain, JdbcTypes.TIME);
    }

    @Override
    public String visitAbstractTimestampDomain(
            AbstractTimestampDomain<?> domain, SqlLogFormattingFunction p) {
        return p.apply(domain, JdbcTypes.TIMESTAMP);
    }

    @Override
    public String visitAbstractArrayDomain(AbstractArrayDomain<?, ?> domain,
            SqlLogFormattingFunction p) throws RuntimeException {
        return p.apply(domain, JdbcTypes.ARRAY);
    }

    @Override
    public String visitAbstractBlobDomain(AbstractBlobDomain<?> domain,
            SqlLogFormattingFunction p) throws RuntimeException {
        return p.apply(domain, JdbcTypes.BLOB);
    }

    @Override
    public String visitAbstractClobDomain(AbstractClobDomain<?> domain,
            SqlLogFormattingFunction p) throws RuntimeException {
        return p.apply(domain, JdbcTypes.CLOB);
    }

    @Override
    public String visitAbstractNClobDomain(AbstractNClobDomain<?> domain,
            SqlLogFormattingFunction p) throws RuntimeException {
        return p.apply(domain, JdbcTypes.NCLOB);
    }

    @Override
    public String visitAbstractBooleanDomain(AbstractBooleanDomain<?> domain,
            SqlLogFormattingFunction p) throws RuntimeException {
        Dialect dialect = p.getConfig().dialect();
        if (dialect.supportsBooleanType()) {
            return p.apply(domain, JdbcTypes.BOOLEAN);
        }
        return p.apply(domain, JdbcTypes.INT_ADAPTIVE_BOOLEAN);
    }

    @Override
    public String visitAbstractBytesDomain(AbstractBytesDomain<?> domain,
            SqlLogFormattingFunction p) throws RuntimeException {
        return p.apply(domain, JdbcTypes.BYTES);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String visitUnknownDomain(Domain<?, ?> domain,
            SqlLogFormattingFunction p) {
        return p.apply((Domain<Object, ?>) domain, JdbcTypes.OBJECT);
    }

}
