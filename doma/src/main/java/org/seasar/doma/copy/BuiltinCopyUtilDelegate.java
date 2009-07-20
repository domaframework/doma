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
package org.seasar.doma.copy;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.bean.Bean;
import org.seasar.doma.bean.BeanFactory;
import org.seasar.doma.bean.BeanProperty;
import org.seasar.doma.bean.FieldAccessBeanFactory;
import org.seasar.doma.converter.ConversionException;
import org.seasar.doma.converter.Converter;
import org.seasar.doma.converter.Converters;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.domain.DomainUtil;
import org.seasar.doma.entity.Entity;
import org.seasar.doma.entity.EntityProperty;
import org.seasar.doma.internal.util.ClasseUtil;

/**
 * @author taedium
 * 
 */
public class BuiltinCopyUtilDelegate implements CopyUtilDelegate {

    protected final static BeanFactory DEFAULT_BEAN_FACTORY = new FieldAccessBeanFactory();

    protected final Map<Class<?>, Converter<?>> converterMap = new HashMap<Class<?>, Converter<?>>();

    protected final BeanFactory beanFactory;

    public BuiltinCopyUtilDelegate() {
        this(DEFAULT_BEAN_FACTORY);
    }

    protected BuiltinCopyUtilDelegate(BeanFactory beanFactory) {
        if (beanFactory == null) {
            throw new DomaIllegalArgumentException("beanFactory", beanFactory);
        }
        this.beanFactory = beanFactory;
        converterMap.put(String.class, Converters.STRING);
        converterMap.put(BigDecimal.class, Converters.BIG_DECIMAL);
        converterMap.put(Boolean.class, Converters.BOOLEAN);
        converterMap.put(Byte.class, Converters.BYTE);
        converterMap.put(Short.class, Converters.SHORT);
        converterMap.put(Integer.class, Converters.INTEGER);
        converterMap.put(Long.class, Converters.LONG);
        converterMap.put(Float.class, Converters.FLOAT);
        converterMap.put(Double.class, Converters.DOUBLE);
        converterMap.put(java.util.Date.class, Converters.UTIL_DATE);
        converterMap.put(Date.class, Converters.DATE);
        converterMap.put(Time.class, Converters.TIME);
        converterMap.put(Timestamp.class, Converters.TIMESTAMP);
    }

    @Override
    public void copy(Object src, Object dest, CopyOptions copyOptions) {
        if (src == null) {
            throw new DomaIllegalArgumentException("src", src);
        }
        if (dest == null) {
            throw new DomaIllegalArgumentException("dest", dest);
        }
        if (copyOptions == null) {
            throw new DomaIllegalArgumentException("copyOptions", copyOptions);
        }
        if (Entity.class.isInstance(src)) {
            Entity<?> srcEntity = Entity.class.cast(src);
            if (Entity.class.isInstance(dest)) {
                copyFromEntityToEntity(srcEntity, Entity.class.cast(dest), copyOptions);
            } else {
                copyFromEntityToBean(srcEntity, createBean(dest, copyOptions), copyOptions);
            }
        } else {
            Bean srcBean = createBean(src, copyOptions);
            if (Entity.class.isInstance(dest)) {
                copyFromBeanToEntity(srcBean, Entity.class.cast(dest), copyOptions);
            } else {
                copyFromBeanToBean(srcBean, createBean(dest, copyOptions), copyOptions);
            }
        }
    }

    @Override
    public void copy(Object src, Map<String, Object> dest,
            CopyOptions copyOptions) {
        if (src == null) {
            throw new DomaIllegalArgumentException("src", src);
        }
        if (dest == null) {
            throw new DomaIllegalArgumentException("dest", dest);
        }
        if (copyOptions == null) {
            throw new DomaIllegalArgumentException("copyOptions", copyOptions);
        }
        if (Entity.class.isInstance(src)) {
            copyFromEntityToMap(Entity.class.cast(src), dest, copyOptions);
        } else {
            copyFromBeanToMap(createBean(src, copyOptions), dest, copyOptions);
        }
    }

    @Override
    public void copy(Map<String, Object> src, Object dest,
            CopyOptions copyOptions) {
        if (src == null) {
            throw new DomaIllegalArgumentException("src", src);
        }
        if (dest == null) {
            throw new DomaIllegalArgumentException("dest", dest);
        }
        if (copyOptions == null) {
            throw new DomaIllegalArgumentException("copyOptions", copyOptions);
        }
        if (Entity.class.isInstance(dest)) {
            copyFromMapToEntity(src, Entity.class.cast(dest), copyOptions);
        } else {
            copyFromMapToBean(src, createBean(dest, copyOptions), copyOptions);
        }
    }

    protected void copyFromEntityToEntity(Entity<?> src, Entity<?> dest,
            CopyOptions copyOptions) {
        Class<?> srcClass = src.getClass();
        for (EntityProperty<?> srcProperty : src.__getEntityProperties()) {
            if (!copyOptions.isTargetProperty(srcProperty.getName())) {
                continue;
            }
            copyToEntityProperty(srcClass, srcProperty.getName(), srcProperty
                    .getDomain().get(), dest, copyOptions);
        }
    }

    protected void copyFromEntityToMap(Entity<?> src, Map<String, Object> dest,
            CopyOptions copyOptions) {
        for (EntityProperty<?> srcProperty : src.__getEntityProperties()) {
            if (!copyOptions.isTargetProperty(srcProperty.getName())) {
                continue;
            }
            if (!copyOptions.isTargetValue(srcProperty.getDomain().get())) {
                continue;
            }
            dest.put(srcProperty.getName(), srcProperty.getDomain().get());
        }
    }

    protected void copyFromEntityToBean(Entity<?> src, Bean dest,
            CopyOptions copyOptions) {
        Class<?> srcClass = src.getClass();
        for (EntityProperty<?> srcProperty : src.__getEntityProperties()) {
            if (!copyOptions.isTargetProperty(srcProperty.getName())) {
                continue;
            }
            if (!copyOptions.isTargetValue(srcProperty.getDomain().get())) {
                continue;
            }
            copyToBeanProperty(srcClass, srcProperty.getName(), srcProperty
                    .getDomain().get(), dest, copyOptions);
        }
    }

    protected void copyFromBeanToEntity(Bean src, Entity<?> dest,
            CopyOptions copyOptions) {
        Class<?> srcClass = src.getBeanClass();
        for (BeanProperty srcProperty : src.getBeanProperties()) {
            if (!copyOptions.isTargetProperty(srcProperty.getName())) {
                continue;
            }
            if (!copyOptions.isTargetValue(srcProperty.getValue())) {
                continue;
            }
            copyToEntityProperty(srcClass, srcProperty.getName(), srcProperty
                    .getValue(), dest, copyOptions);
        }
    }

    protected void copyFromBeanToMap(Bean src, Map<String, Object> dest,
            CopyOptions copyOptions) {
        for (BeanProperty srcProperty : src.getBeanProperties()) {
            if (!copyOptions.isTargetProperty(srcProperty.getName())) {
                continue;
            }
            if (!copyOptions.isTargetValue(srcProperty.getValue())) {
                continue;
            }
            dest.put(srcProperty.getName(), srcProperty.getValue());
        }
    }

    protected void copyFromBeanToBean(Bean src, Bean dest,
            CopyOptions copyOptions) {
        Class<?> srcClass = src.getBeanClass();
        for (BeanProperty srcProperty : src.getBeanProperties()) {
            if (!copyOptions.isTargetProperty(srcProperty.getName())) {
                continue;
            }
            if (!copyOptions.isTargetValue(srcProperty.getValue())) {
                continue;
            }
            copyToBeanProperty(srcClass, srcProperty.getName(), srcProperty
                    .getValue(), dest, copyOptions);
        }
    }

    protected void copyFromMapToEntity(Map<String, Object> src, Entity<?> dest,
            CopyOptions copyOptions) {
        Class<?> srcClass = src.getClass();
        for (Entry<String, Object> srcEntry : src.entrySet()) {
            if (!copyOptions.isTargetProperty(srcEntry.getKey())) {
                continue;
            }
            if (!copyOptions.isTargetValue(srcEntry.getValue())) {
                continue;
            }
            copyToEntityProperty(srcClass, srcEntry.getKey(), srcEntry
                    .getValue(), dest, copyOptions);
        }
    }

    protected void copyFromMapToBean(Map<String, Object> src, Bean dest,
            CopyOptions copyOptions) {
        Class<?> srcClass = src.getClass();
        for (Entry<String, Object> srcEntry : src.entrySet()) {
            if (!copyOptions.isTargetProperty(srcEntry.getKey())) {
                continue;
            }
            if (!copyOptions.isTargetValue(srcEntry.getValue())) {
                continue;
            }
            copyToBeanProperty(srcClass, srcEntry.getKey(), srcEntry.getValue(), dest, copyOptions);
        }
    }

    protected void copyToEntityProperty(Class<?> srcClass,
            String srcPropertyName, Object srcPropertyValue, Entity<?> dest,
            CopyOptions copyOptions) {
        EntityProperty<?> destProperty = dest
                .__getEntityProperty(srcPropertyName);
        if (destProperty == null) {
            return;
        }
        Domain<?, ?> destDomain = destProperty.getDomain();
        Converter<?> converter = findConverter(destProperty.getName(), destDomain
                .getValueClass(), copyOptions);
        if (converter == null) {
            return;
        }
        Object destValue = convert(converter, srcClass, srcPropertyName, srcPropertyValue, copyOptions);
        DomainUtil.set(destDomain, destValue);
    }

    protected void copyToBeanProperty(Class<?> srcClass,
            String srcPropertyName, Object srcPropertyValue, Bean dest,
            CopyOptions copyOptions) {
        BeanProperty destProperty = dest.getBeanProperty(srcPropertyName);
        if (destProperty == null) {
            return;
        }
        Converter<?> converter = findConverter(destProperty.getName(), destProperty
                .getPropertyClass(), copyOptions);
        if (converter == null) {
            return;
        }
        Object destValue = convert(converter, srcClass, srcPropertyName, srcPropertyValue, copyOptions);
        destProperty.setValue(destValue);
    }

    protected Object convert(Converter<?> converter, Class<?> srcClass,
            String srcPropertyName, Object srcPropertyValue,
            CopyOptions copyOptions) {
        String pattern = copyOptions.getPattern(srcPropertyName);
        try {
            return converter.convert(srcPropertyValue, pattern);
        } catch (ConversionException e) {
            throw new PropertyCopyException(srcClass.getName(),
                    srcPropertyName, srcPropertyValue, e);
        }
    }

    protected Converter<?> findConverter(String name, Class<?> destClass,
            CopyOptions copyOptions) {
        Converter<?> converter = copyOptions.getConverter(name);
        if (converter == null) {
            Class<?> wrapperClass = ClasseUtil
                    .getWrapperClassIfPrimitive(destClass);
            converter = converterMap.get(wrapperClass);
        }
        return converter;
    }

    protected Bean createBean(Object obj, CopyOptions copyOptions) {
        BeanFactory beanFactory = copyOptions.getBeanFactory();
        if (beanFactory == null) {
            beanFactory = this.beanFactory;
        }
        return beanFactory.create(obj);
    }

}
