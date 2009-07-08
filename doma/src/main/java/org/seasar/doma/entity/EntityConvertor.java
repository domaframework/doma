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
import java.util.Map;

import org.seasar.doma.converter.Converter;
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

    protected Map<Class<?>, Converter<?>> converterMapByClass;

    protected Map<String, Converter<?>> converterMapByName;

    public EntityConvertor() {
    }

    public <I, E extends Entity<I>> I toEntity(Object bean, Class<E> entityClass)
            throws Exception {
        DomainVisitor<Void, BeanPropertyAccessor, RuntimeException> settingVisitor = new ValueSettingVisitor();
        E entity = Classes.newInstance(entityClass);
        for (Property<?> p : entity.__getProperties()) {
            BeanPropertyAccessor accessor = createBeanPropertyAccessor(bean, p
                    .getName());
            p.getDomain().accept(settingVisitor, accessor);
        }
        return entity.__asInterface();
    }

    public <T> T toBean(Object entity, Class<T> beanClazz) throws Exception {
        T bean = Classes.newInstance(beanClazz);
        Entity<?> e = Entity.class.cast(entity);
        for (Property<?> p : e.__getProperties()) {
            BeanPropertyAccessor accessor = new FieldPropertyAccessor(bean, p
                    .getName());
            Object value = p.getDomain().get();
            if (value == null) {
                continue;
            }
            Converter<?> converter = converterMapByName.get(p.getName());
            if (converter == null) {
                converter = converterMapByClass.get(value.getClass());
            }
            if (converter == null) {
                continue;
            }
            accessor.setValue(converter.convert(value));
        }
        return bean;
    }

    protected BeanPropertyAccessor createBeanPropertyAccessor(Object bean,
            String name) {
        return new FieldPropertyAccessor(bean, name);
    }

    public class ValueSettingVisitor
            implements
            DomainVisitor<Void, BeanPropertyAccessor, RuntimeException>,
            AbstractBigDecimalDomainVisitor<Void, BeanPropertyAccessor, RuntimeException>,
            AbstractDateDomainVisitor<Void, BeanPropertyAccessor, RuntimeException>,
            AbstractIntegerDomainVisitor<Void, BeanPropertyAccessor, RuntimeException>,
            AbstractStringDomainVisitor<Void, BeanPropertyAccessor, RuntimeException>,
            AbstractTimeDomainVisitor<Void, BeanPropertyAccessor, RuntimeException>,
            AbstractTimestampDomainVisitor<Void, BeanPropertyAccessor, RuntimeException>,
            AbstractBooleanDomainVisitor<Void, BeanPropertyAccessor, RuntimeException>,
            AbstractDoubleDomainVisitor<Void, BeanPropertyAccessor, RuntimeException> {

        protected Converter<?> getConverter(BeanPropertyAccessor p) {
            if (converterMapByName.containsKey(p.getName())) {
                return converterMapByName.get(p.getName());
            }
            if (converterMapByClass.containsKey(p.getPropertyClass())) {
                return converterMapByClass.get(p.getPropertyClass());
            }
            return null;
        }

        protected Object getValue(BeanPropertyAccessor p) {
            Converter<?> converter = getConverter(p);
            if (p == null) {
                return null;
            }
            return converter.convert(p.getValue());
        }

        @Override
        public Void visitAbstractTimestampDomain(
                AbstractTimestampDomain<?> domain, BeanPropertyAccessor p)
                throws RuntimeException {
            Object value = getValue(p);
            if (Timestamp.class.isInstance(value)) {
                domain.set(Timestamp.class.cast(value));
            }
            return null;
        }

        @Override
        public Void visitAbstractDoubleDomain(AbstractDoubleDomain<?> domain,
                BeanPropertyAccessor p) throws RuntimeException {
            Object value = getValue(p);
            if (Double.class.isInstance(value)) {
                domain.set(Double.class.cast(value));
            }
            return null;
        }

        @Override
        public Void visitAbstractBigDecimalDomain(
                AbstractBigDecimalDomain<?> domain, BeanPropertyAccessor p)
                throws RuntimeException {
            Object value = getValue(p);
            if (BigDecimal.class.isInstance(value)) {
                domain.set(BigDecimal.class.cast(value));
            }
            return null;
        }

        @Override
        public Void visitAbstractDateDomain(AbstractDateDomain<?> domain,
                BeanPropertyAccessor p) throws RuntimeException {
            Object value = getValue(p);
            if (Date.class.isInstance(value)) {
                domain.set(Date.class.cast(value));
            }
            return null;
        }

        @Override
        public Void visitAbstractTimeDomain(AbstractTimeDomain<?> domain,
                BeanPropertyAccessor p) throws RuntimeException {
            Object value = getValue(p);
            if (Time.class.isInstance(value)) {
                domain.set(Time.class.cast(value));
            }
            return null;
        }

        @Override
        public Void visitAbstractIntegerDomain(AbstractIntegerDomain<?> domain,
                BeanPropertyAccessor p) throws RuntimeException {
            Object value = getValue(p);
            if (Integer.class.isInstance(value)) {
                domain.set(Integer.class.cast(value));
            }
            return null;
        }

        @Override
        public Void visitAbstractBooleanDomain(AbstractBooleanDomain<?> domain,
                BeanPropertyAccessor p) throws RuntimeException {
            Object value = getValue(p);
            if (Boolean.class.isInstance(value)) {
                domain.set(Boolean.class.cast(value));
            }
            return null;
        }

        @Override
        public Void visitAbstractStringDomain(AbstractStringDomain<?> domain,
                BeanPropertyAccessor p) throws RuntimeException {
            Object value = getValue(p);
            if (String.class.isInstance(value)) {
                domain.set(String.class.cast(value));
            }
            return null;
        }

        @Override
        public Void visitUnknownDomain(Domain<?, ?> domain,
                BeanPropertyAccessor p) throws RuntimeException {
            return null;
        }

    }
}
