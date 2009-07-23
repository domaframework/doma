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

import java.util.HashMap;
import java.util.Map;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.bean.BeanWrapperFactory;
import org.seasar.doma.converter.Converter;

/**
 * @author taedium
 * 
 */
public class CopyOptions {

    protected static final String[] EMPTY_STRINGS = new String[] {};

    protected final Map<String, Converter<?>> converterMap = new HashMap<String, Converter<?>>();

    protected final Map<String, String> patterns = new HashMap<String, String>();

    protected String[] includedPropertyNames = EMPTY_STRINGS;

    protected String[] excludedPropertyNames = EMPTY_STRINGS;

    protected boolean nullIncluded;

    protected boolean emptyStringIncluded;

    protected boolean whitespaceIncluded = true;

    protected BeanWrapperFactory beanWrapperFactory;

    public CopyOptions include(String... propertyNames) {
        includedPropertyNames = propertyNames;
        return this;
    }

    public CopyOptions exclude(String... propertyNames) {
        excludedPropertyNames = propertyNames;
        return this;
    }

    public CopyOptions includeNull() {
        nullIncluded = true;
        return this;
    }

    public CopyOptions includeEmptyString() {
        emptyStringIncluded = true;
        return this;
    }

    public CopyOptions excludeWhitespace() {
        whitespaceIncluded = false;
        return this;
    }

    public CopyOptions converter(Converter<?> converter,
            String... propertyNames) {
        if (converter == null) {
            throw new DomaIllegalArgumentException("converter", converter);
        }
        for (String propertyName : propertyNames) {
            converterMap.put(propertyName, converter);
        }
        return this;
    }

    public CopyOptions pattern(String pattern, String... propertyNames) {
        if (pattern == null) {
            throw new DomaIllegalArgumentException("pattern", pattern);
        }
        for (String propertyName : propertyNames) {
            patterns.put(propertyName, pattern);
        }
        return this;
    }

    public CopyOptions beanWrapperFactory(BeanWrapperFactory beanWrapperFactory) {
        this.beanWrapperFactory = beanWrapperFactory;
        return this;
    }

    public Converter<?> getConverter(String propertyName) {
        return converterMap.get(propertyName);
    }

    public String getPattern(String propertyName) {
        return patterns.get(propertyName);
    }

    public BeanWrapperFactory getBeanFactory() {
        return beanWrapperFactory;
    }

    public boolean isTargetProperty(String propertyName) {
        if (includedPropertyNames.length > 0) {
            for (String included : includedPropertyNames) {
                if (included.equals(propertyName)) {
                    for (String excluded : excludedPropertyNames) {
                        if (excluded.equals(propertyName)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        if (excludedPropertyNames.length > 0) {
            for (String excluded : excludedPropertyNames) {
                if (excluded.equals(propertyName)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isTargetValue(Object value) {
        if (value == null) {
            return nullIncluded;
        }
        if ("".equals(value)) {
            return emptyStringIncluded;
        }
        if (String.class.isInstance(value)) {
            String s = String.class.cast(value);
            if (s.trim().isEmpty()) {
                return whitespaceIncluded;
            }
        }
        return true;
    }
}
