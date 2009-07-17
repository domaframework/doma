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

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.seasar.doma.DomaIllegalArgumentException;

/**
 * @author taedium
 * 
 */
public class FieldAccessBean implements Bean {

    protected final Object bean;

    protected final Class<?> beanClass;

    protected final Collection<BeanProperty> propertyWrappers;

    protected final Map<String, BeanProperty> propertyWrapperMap;

    public FieldAccessBean(Object bean) {
        if (bean == null) {
            throw new DomaIllegalArgumentException("bean", bean);
        }
        this.bean = bean;
        this.beanClass = bean.getClass();
        this.propertyWrapperMap = createPropertyWrapperMap(bean, beanClass);
        this.propertyWrappers = Collections
                .unmodifiableCollection(propertyWrapperMap.values());
    }

    @Override
    public BeanProperty getBeanProperty(String name) {
        return propertyWrapperMap.get(name);
    }

    @Override
    public Collection<BeanProperty> getBeanProperties() {
        return propertyWrappers;
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    protected LinkedHashMap<String, BeanProperty> createPropertyWrapperMap(
            Object bean, Class<?> beanClass) {
        LinkedHashMap<String, BeanProperty> result = new LinkedHashMap<String, BeanProperty>();
        for (Class<?> clazz = beanClass; clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            for (Field field : beanClass.getFields()) {
                BeanProperty propertyWrapper = new FieldAccessPropertyWrapper(
                        bean, field);
                String name = propertyWrapper.getName();
                if (result.containsKey(name)) {
                    continue;
                }
                result.put(name, propertyWrapper);
            }
        }
        return result;
    }

    protected static class FieldAccessPropertyWrapper implements BeanProperty {

        protected final Object bean;

        protected final Field field;

        public FieldAccessPropertyWrapper(Object bean, Field field) {
            this.bean = bean;
            this.field = field;
        }

        @Override
        public String getName() {
            return field.getName();
        }

        @Override
        public Class<?> getPropertyClass() {
            return field.getType();
        }

        @Override
        public Object getValue() {
            try {
                return field.get(bean);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void setValue(Object value) {
            try {
                field.set(bean, value);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }
}
