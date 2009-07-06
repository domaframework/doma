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
package org.seasar.doma.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.seasar.doma.domain.AbstractBigDecimalDomain;
import org.seasar.doma.domain.AbstractBigDecimalDomainVisitor;
import org.seasar.doma.domain.AbstractBooleanDomain;
import org.seasar.doma.domain.AbstractBooleanDomainVisitor;
import org.seasar.doma.domain.AbstractDateDomain;
import org.seasar.doma.domain.AbstractDateDomainVisitor;
import org.seasar.doma.domain.AbstractDoubleDomain;
import org.seasar.doma.domain.AbstractDoubleDomainVisitor;
import org.seasar.doma.domain.AbstractIntegerDomain;
import org.seasar.doma.domain.AbstractIntegerDomainVisitor;
import org.seasar.doma.domain.AbstractStringDomain;
import org.seasar.doma.domain.AbstractStringDomainVisitor;
import org.seasar.doma.domain.AbstractTimeDomain;
import org.seasar.doma.domain.AbstractTimeDomainVisitor;
import org.seasar.doma.domain.AbstractTimestampDomain;
import org.seasar.doma.domain.AbstractTimestampDomainVisitor;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.domain.DomainVisitor;
import org.seasar.doma.internal.jdbc.Entity;
import org.seasar.doma.internal.jdbc.Property;
import org.seasar.doma.internal.util.Classes;

/**
 * @author taedium
 * 
 */
public class EntityConvertor {

    protected final Convertor convertor;

    protected final DomainVisitor<Void, Object, RuntimeException> settingVisitor;

    public EntityConvertor() {
        this.convertor = createConvertor();
        this.settingVisitor = createValueSettingVisitor();
    }

    public <I, E extends Entity<I>> I toEntity(Object bean, Class<E> entityClass)
            throws Exception {
        ObjectWrapper objectWrapper = createObjectWrapper(bean);
        E entity = Classes.newInstance(entityClass);
        for (Property<?> p : entity.__getProperties()) {
            Object value = objectWrapper.get(p.getName());
            p.getDomain().accept(settingVisitor, value);
        }
        return entity.__asInterface();
    }

    public <T> T toBean(Object entity, Class<T> beanClazz) throws Exception {
        T bean = Classes.newInstance(beanClazz);
        ObjectWrapper objectWrapper = createObjectWrapper(bean);
        Entity<?> e = Entity.class.cast(entity);
        for (Property<?> p : e.__getProperties()) {
            Object value = p.getDomain().get();
            if (value == null) {
                continue;
            }
            objectWrapper.set(p.getName(), value);
        }
        return bean;
    }

    protected Convertor createConvertor() {
        return new Convertor();
    }

    protected DomainVisitor<Void, Object, RuntimeException> createValueSettingVisitor() {
        return new ValueSettingVisitor();
    }

    protected ObjectWrapper createObjectWrapper(Object object) {
        return new FieldAccessObjectWrapper(object);
    }

    public class ValueSettingVisitor implements
            DomainVisitor<Void, Object, RuntimeException>,
            AbstractBigDecimalDomainVisitor<Void, Object, RuntimeException>,
            AbstractDateDomainVisitor<Void, Object, RuntimeException>,
            AbstractIntegerDomainVisitor<Void, Object, RuntimeException>,
            AbstractStringDomainVisitor<Void, Object, RuntimeException>,
            AbstractTimeDomainVisitor<Void, Object, RuntimeException>,
            AbstractTimestampDomainVisitor<Void, Object, RuntimeException>,
            AbstractBooleanDomainVisitor<Void, Object, RuntimeException>,
            AbstractDoubleDomainVisitor<Void, Object, RuntimeException> {

        @Override
        public Void visitAbstractTimestampDomain(
                AbstractTimestampDomain<?> domain, Object p)
                throws RuntimeException {
            Timestamp value = convertor.toTimestamp(p);
            domain.set(value);
            return null;
        }

        @Override
        public Void visitAbstractDoubleDomain(AbstractDoubleDomain<?> domain,
                Object p) throws RuntimeException {
            Double value = convertor.toDouble(p);
            domain.set(value);
            return null;
        }

        @Override
        public Void visitAbstractBigDecimalDomain(
                AbstractBigDecimalDomain<?> domain, Object p)
                throws RuntimeException {
            BigDecimal value = convertor.toBigDecimal(p);
            domain.set(value);
            return null;
        }

        @Override
        public Void visitAbstractDateDomain(AbstractDateDomain<?> domain,
                Object p) throws RuntimeException {
            Date value = convertor.toDate(p);
            domain.set(value);
            return null;
        }

        @Override
        public Void visitAbstractTimeDomain(AbstractTimeDomain<?> domain,
                Object p) throws RuntimeException {
            Time value = convertor.toTime(p);
            domain.set(value);
            return null;
        }

        @Override
        public Void visitAbstractIntegerDomain(AbstractIntegerDomain<?> domain,
                Object p) throws RuntimeException {
            Integer value = convertor.toInteger(p);
            domain.set(value);
            return null;
        }

        @Override
        public Void visitAbstractBooleanDomain(AbstractBooleanDomain<?> domain,
                Object p) throws RuntimeException {
            Boolean value = convertor.toBoolean(p);
            domain.set(value);
            return null;
        }

        @Override
        public Void visitAbstractStringDomain(AbstractStringDomain<?> domain,
                Object p) throws RuntimeException {
            String value = convertor.toString(p);
            domain.set(value);
            return null;
        }

        @Override
        public Void visitUnknownDomain(Domain<?, ?> domain, Object p)
                throws RuntimeException {
            return null;
        }

    }
}
