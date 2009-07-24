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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.MethodUtil;

/**
 * @author taedium
 * 
 */
public class MethodAccessBeanWrapper implements BeanWrapper {

    protected final Object bean;

    protected final Class<?> beanClass;

    protected final List<BeanPropertyWrapper> propertyWrappers;

    protected final Map<String, BeanPropertyWrapper> propertyWrapperMap;

    public MethodAccessBeanWrapper(Object bean) {
        if (bean == null) {
            throw new DomaIllegalArgumentException("bean", bean);
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
        BeanInfo beanInfo = getBeanInfo(beanClass);
        for (PropertyDescriptor propertyDescriptor : beanInfo
                .getPropertyDescriptors()) {
            BeanPropertyWrapper propertyWrapper = new MethodAccessPropertyWrapper(
                    propertyDescriptor);
            String name = propertyWrapper.getName();
            result.put(name, propertyWrapper);
        }
        return result;
    }

    protected BeanInfo getBeanInfo(Class<?> beanClass) {
        try {
            return Introspector.getBeanInfo(beanClass);
        } catch (java.beans.IntrospectionException e) {
            throw new IntrospectionException(e);
        }
    }

    protected class MethodAccessPropertyWrapper implements BeanPropertyWrapper {

        protected final PropertyDescriptor propertyDescriptor;

        protected final Method readMethod;

        protected final Method writeMethod;

        public MethodAccessPropertyWrapper(PropertyDescriptor propertyDescriptor) {
            this.propertyDescriptor = propertyDescriptor;
            this.readMethod = propertyDescriptor.getReadMethod();
            this.writeMethod = propertyDescriptor.getWriteMethod();
        }

        @Override
        public String getName() {
            return propertyDescriptor.getName();
        }

        @Override
        public Class<?> getPropertyClass() {
            return propertyDescriptor.getPropertyType();
        }

        @Override
        public boolean isValueGettable() {
            return readMethod != null;
        }

        @Override
        public Object getValue() {
            try {
                return MethodUtil.invoke(readMethod, bean);
            } catch (WrapException e) {
                Throwable cause = e.getCause();
                throw new PropertyReadAccessException(beanClass.getName(),
                        propertyDescriptor.getName(), cause);
            }
        }

        @Override
        public boolean isValueSettable() {
            return writeMethod != null;
        }

        @Override
        public void setValue(Object value) {
            try {
                MethodUtil.invoke(writeMethod, bean, value);
            } catch (WrapException e) {
                Throwable cause = e.getCause();
                throw new PropertyWriteAccessException(beanClass.getName(),
                        propertyDescriptor.getName(), cause);
            }
        }

    }

}
