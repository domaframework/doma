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

import java.lang.reflect.Field;

/**
 * @author taedium
 * 
 */
public class FieldPropertyAccessor implements BeanPropertyAccessor {

    protected final Object bean;

    protected final String name;

    protected Field field;

    public FieldPropertyAccessor(Object bean, String name) {
        this.bean = bean;
        this.name = name;
        try {
            field = bean.getClass().getField(name);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        field.setAccessible(true);
    }

    @Override
    public String getName() {
        return name;
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

    @Override
    public Class<?> getPropertyClass() {
        return field.getType();
    }

}
