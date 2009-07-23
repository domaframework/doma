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
package org.seasar.doma.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.FieldUtil;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public class FieldAccessBeanWrapper implements BeanWrapper {

    protected final Object bean;

    protected final Class<?> beanClass;

    protected final List<BeanPropertyWrapper> propertyWrappers;

    protected final Map<String, BeanPropertyWrapper> propertyWrapperMap;

    public FieldAccessBeanWrapper(Object bean) {
        if (bean == null) {
            throw new DomaIllegalArgumentException("obj", bean);
        }
        this.bean = bean;
        this.beanClass = bean.getClass();
        this.propertyWrapperMap = createPropertyWrapperMap(beanClass);
        this.propertyWrappers = Collections
                .unmodifiableList(new ArrayList<BeanPropertyWrapper>(
                        propertyWrapperMap.values()));
    }

    @Override
    public BeanPropertyWrapper getBeanPropertyWrapper(String name) {
        return propertyWrapperMap.get(name);
    }

    @Override
    public List<BeanPropertyWrapper> getBeanPropertyWrappers() {
        return propertyWrappers;
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    protected LinkedHashMap<String, BeanPropertyWrapper> createPropertyWrapperMap(
            Class<?> beanClass) {
        LinkedHashMap<String, BeanPropertyWrapper> result = new LinkedHashMap<String, BeanPropertyWrapper>();
        for (Class<?> clazz = beanClass; clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            for (Field field : beanClass.getFields()) {
                BeanPropertyWrapper propertyWrapper = new FieldAccessPropertyWrapper(
                        field);
                String name = propertyWrapper.getName();
                if (result.containsKey(name)) {
                    continue;
                }
                result.put(name, propertyWrapper);
            }
        }
        return result;
    }

    protected class FieldAccessPropertyWrapper implements BeanPropertyWrapper {

        protected final Field field;

        public FieldAccessPropertyWrapper(Field field) {
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
                return FieldUtil.get(field, bean);
            } catch (WrapException e) {
                Throwable cause = e.getCause();
                throw new BeanException(DomaMessageCode.DOMA6001, beanClass, field
                        .getName(), cause, cause);
            }
        }

        @Override
        public void setValue(Object value) {
            try {
                FieldUtil.set(field, bean, value);
            } catch (WrapException e) {
                Throwable cause = e.getCause();
                throw new BeanException(DomaMessageCode.DOMA6002, beanClass, field
                        .getName(), cause, cause);
            }
        }

    }
}
