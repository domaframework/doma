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
package org.seasar.doma.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.converter.BigDecimalConverter;
import org.seasar.doma.converter.Converter;
import org.seasar.doma.converter.DateConverter;
import org.seasar.doma.converter.IntegerConverter;
import org.seasar.doma.converter.StringConverter;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.jdbc.Entity;
import org.seasar.doma.internal.jdbc.Property;

/**
 * @author taedium
 * 
 */
public class StandardCopier implements Copier {

    protected static Map<Class<?>, Converter<?>> converterMapByClass;
    static {
        converterMapByClass.put(BigDecimal.class, new BigDecimalConverter());
        converterMapByClass.put(Date.class, new DateConverter());
        converterMapByClass.put(Integer.class, new IntegerConverter());
        converterMapByClass.put(String.class, new StringConverter());
    }

    @Override
    public void copy(Object src, Object dest, CopyOptions copyOptions) {
        if (src == null) {
            throw new DomaIllegalArgumentException("src", src);
        }
        if (dest == null) {
            throw new DomaIllegalArgumentException("dest", dest);
        }
        if (Entity.class.isInstance(src)) {
            Entity<?> srcEntity = Entity.class.cast(src);
            if (Entity.class.isInstance(dest)) {
                copyFromEntityToEntity(srcEntity, Entity.class.cast(dest), copyOptions);
            } else {
                copyFromEntityToBean(srcEntity, createBean(dest), copyOptions);
            }
        } else {
            Bean srcBean = createBean(src);
            if (Entity.class.isInstance(dest)) {
                copyFromBeanToEntity(srcBean, Entity.class.cast(dest), copyOptions);
            } else {
                copyFromBeanToBean(srcBean, createBean(dest), copyOptions);
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
        if (Entity.class.isInstance(src)) {
            copyFromEntityToMap(Entity.class.cast(src), dest, copyOptions);
        } else {
            copyFromBeanToMap(createBean(src), dest, copyOptions);
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
        if (Entity.class.isInstance(dest)) {
            copyFromMapToEntity(src, Entity.class.cast(src), copyOptions);
        } else {
            copyFromMapToBean(src, createBean(dest), copyOptions);
        }
    }

    protected void copyFromEntityToEntity(Entity<?> src, Entity<?> dest,
            CopyOptions copyOptions) {
        for (Property<?> srcProperty : src.__getProperties()) {
            if (!copyOptions.isTargetProperty(srcProperty.getName())) {
                continue;
            }
            Property<?> destProperty = dest.__getPropertyByName(srcProperty
                    .getName());
            if (destProperty == null) {
                continue;
            }
            Domain<?, ?> destDomain = destProperty.getDomain();
            Converter<?> converter = getConverter(destProperty.getName(), destDomain
                    .getValueClass(), copyOptions);
            if (converter == null) {
                continue;
            }
            String pattern = copyOptions.getPattern(destProperty.getName());
            Object value = converter
                    .convert(srcProperty.getDomain().get(), pattern);
            destDomain.setByReflection(value);
        }
    }

    protected void copyFromEntityToMap(Entity<?> src, Map<String, Object> dest,
            CopyOptions copyOptions) {
        for (Property<?> srcProperty : src.__getProperties()) {
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
        for (Property<?> srcProperty : src.__getProperties()) {
            if (!copyOptions.isTargetProperty(srcProperty.getName())) {
                continue;
            }
            if (!copyOptions.isTargetValue(srcProperty.getDomain().get())) {
                continue;
            }
            BeanProperty destProperty = dest.getBeanProperty(srcProperty
                    .getName());
            if (destProperty == null) {
                continue;
            }
            Converter<?> converter = getConverter(destProperty.getName(), destProperty
                    .getClass(), copyOptions);
            if (converter == null) {
                continue;
            }
            String pattern = copyOptions.getPattern(destProperty.getName());
            Object value = converter
                    .convert(srcProperty.getDomain().get(), pattern);
            destProperty.setValue(value);
        }
    }

    protected void copyFromBeanToEntity(Bean src, Entity<?> dest,
            CopyOptions copyOptions) {
        for (BeanProperty srcProperty : src.getBeanProperties()) {
            if (!copyOptions.isTargetProperty(srcProperty.getName())) {
                continue;
            }
            if (!copyOptions.isTargetValue(srcProperty.getValue())) {
                continue;
            }
            Property<?> destProperty = dest.__getPropertyByName(srcProperty
                    .getName());
            if (destProperty == null) {
                continue;
            }
            Domain<?, ?> destDomain = destProperty.getDomain();
            Converter<?> converter = getConverter(destProperty.getName(), destDomain
                    .getValueClass(), copyOptions);
            if (converter == null) {
                continue;
            }
            String pattern = copyOptions.getPattern(destProperty.getName());
            Object value = converter.convert(srcProperty.getValue(), pattern);
            destDomain.setByReflection(value);
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
        for (BeanProperty srcProperty : src.getBeanProperties()) {
            if (!copyOptions.isTargetProperty(srcProperty.getName())) {
                continue;
            }
            if (!copyOptions.isTargetValue(srcProperty.getValue())) {
                continue;
            }
            BeanProperty destProperty = dest.getBeanProperty(srcProperty
                    .getName());
            if (destProperty == null) {
                continue;
            }
            Converter<?> converter = getConverter(destProperty.getName(), destProperty
                    .getPropertyClass(), copyOptions);
            if (converter == null) {
                continue;
            }
            String pattern = copyOptions.getPattern(destProperty.getName());
            Object value = converter.convert(srcProperty.getValue(), pattern);
            destProperty.setValue(value);
        }
    }

    protected void copyFromMapToEntity(Map<String, Object> src, Entity<?> dest,
            CopyOptions copyOptions) {
        for (Entry<String, Object> srcEntry : src.entrySet()) {
            if (!copyOptions.isTargetProperty(srcEntry.getKey())) {
                continue;
            }
            if (!copyOptions.isTargetValue(srcEntry.getValue())) {
                continue;
            }
            Property<?> destProperty = dest.__getPropertyByName(srcEntry
                    .getKey());
            if (destProperty == null) {
                continue;
            }
            Domain<?, ?> destDomain = destProperty.getDomain();
            Converter<?> converter = getConverter(destProperty.getName(), destDomain
                    .getValueClass(), copyOptions);
            if (converter == null) {
                continue;
            }
            String pattern = copyOptions.getPattern(destProperty.getName());
            Object value = converter.convert(srcEntry.getValue(), pattern);
            destDomain.setByReflection(value);
        }
    }

    protected void copyFromMapToBean(Map<String, Object> src, Bean dest,
            CopyOptions copyOptions) {
        for (Entry<String, Object> srcEntry : src.entrySet()) {
            if (!copyOptions.isTargetProperty(srcEntry.getKey())) {
                continue;
            }
            if (!copyOptions.isTargetValue(srcEntry.getValue())) {
                continue;
            }
            BeanProperty destProperty = dest.getBeanProperty(srcEntry.getKey());
            if (destProperty == null) {
                continue;
            }
            Converter<?> converter = getConverter(destProperty.getName(), destProperty
                    .getPropertyClass(), copyOptions);
            if (converter == null) {
                continue;
            }
            String pattern = copyOptions.getPattern(destProperty.getName());
            Object value = converter.convert(srcEntry.getValue(), pattern);
            destProperty.setValue(value);
        }
    }

    protected Converter<?> getConverter(String name, Class<?> destClass,
            CopyOptions copyOptions) {
        Converter<?> converter = copyOptions.getConverter(name, destClass);
        if (converter == null) {
            converter = converterMapByClass.get(destClass);
        }

        String pattern = copyOptions.getPattern(name);
        if (pattern != null) {

        }

        return converter;
    }

    protected Bean createBean(Object obj) {
        return new FieldAccessBean(obj);
    }

}
