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
import org.seasar.doma.domain.AbstractDateDomain;
import org.seasar.doma.domain.AbstractIntegerDomain;
import org.seasar.doma.domain.AbstractStringDomain;
import org.seasar.doma.domain.AbstractTimeDomain;
import org.seasar.doma.domain.AbstractTimestampDomain;
import org.seasar.doma.domain.BuiltInDomainVisitor;
import org.seasar.doma.domain.Domain;

/**
 * @author taedium
 * 
 */
public class SqlLogFormattingVisitor implements
        BuiltInDomainVisitor<String, Void, RuntimeException> {

    protected static final String NULL = "null";

    @Override
    public String visitAbstractBigDecimalDomain(
            AbstractBigDecimalDomain<?> domain, Void p) {
        if (domain.isNull()) {
            return NULL;
        }
        return domain.get().toPlainString();
    }

    @Override
    public String visitAbstractDateDomain(AbstractDateDomain<?> domain, Void p) {
        if (domain.isNull()) {
            return NULL;
        }
        return "'" + domain.get() + "'";
    }

    @Override
    public String visitAbstractIntegerDomain(AbstractIntegerDomain<?> domain,
            Void p) {
        if (domain.isNull()) {
            return NULL;
        }
        return domain.get().toString();
    }

    @Override
    public String visitAbstractStringDomain(AbstractStringDomain<?> domain,
            Void p) {
        if (domain.isNull()) {
            return NULL;
        }
        return "'" + domain.get() + "'";
    }

    @Override
    public String visitAbstractTimeDomain(AbstractTimeDomain<?> domain, Void p) {
        if (domain.isNull()) {
            return NULL;
        }
        return "'" + domain.get() + "'";
    }

    @Override
    public String visitAbstractTimestampDomain(
            AbstractTimestampDomain<?> domain, Void p) {
        if (domain.isNull()) {
            return NULL;
        }
        return "'" + domain.get() + "'";
    }

    @Override
    public String visitUnknownDomain(Domain<?, ?> domain, Void p) {
        if (domain.isNull()) {
            return NULL;
        }
        return domain.toString();
    }

}
