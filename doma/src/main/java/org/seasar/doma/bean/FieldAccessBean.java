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
import org.seasar.doma.internal.util.Fields;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class FieldAccessBean implements Bean {

    protected final Object obj;

    protected final Class<?> beanClass;

    protected final List<BeanProperty> propertyWrappers;

    protected final Map<String, BeanProperty> propertyWrapperMap;

    public FieldAccessBean(Object obj) {
        if (obj == null) {
            throw new DomaIllegalArgumentException("obj", obj);
        }
        this.obj = obj;
        this.beanClass = obj.getClass();
        this.propertyWrapperMap = createPropertyWrapperMap(beanClass);
        this.propertyWrappers = Collections
                .unmodifiableList(new ArrayList<BeanProperty>(
                        propertyWrapperMap.values()));
    }

    @Override
    public BeanProperty getBeanProperty(String name) {
        return propertyWrapperMap.get(name);
    }

    @Override
    public List<BeanProperty> getBeanProperties() {
        return propertyWrappers;
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    protected LinkedHashMap<String, BeanProperty> createPropertyWrapperMap(
            Class<?> beanClass) {
        LinkedHashMap<String, BeanProperty> result = new LinkedHashMap<String, BeanProperty>();
        for (Class<?> clazz = beanClass; clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            for (Field field : beanClass.getFields()) {
                BeanProperty propertyWrapper = new FieldAccessPropertyWrapper(
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

    protected class FieldAccessPropertyWrapper implements BeanProperty {

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
                return Fields.get(field, obj);
            } catch (WrapException e) {
                Throwable cause = e.getCause();
                throw new BeanException(MessageCode.DOMA6001, beanClass, field
                        .getName(), cause, cause);
            }
        }

        @Override
        public void setValue(Object value) {
            try {
                Fields.set(field, obj, value);
            } catch (WrapException e) {
                Throwable cause = e.getCause();
                throw new BeanException(MessageCode.DOMA6002, beanClass, field
                        .getName(), cause, cause);
            }
        }

    }
}
