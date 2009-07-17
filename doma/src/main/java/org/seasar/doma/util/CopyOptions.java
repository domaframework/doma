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

import java.util.Map;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.converter.Converter;

/**
 * @author taedium
 * 
 */
public class CopyOptions {

    protected String[] includedPropertyNames;

    protected String[] excludedPropertyNames;

    protected Map<String, Converter<?>> converterMapByName;

    protected Map<String, String> patterns;

    protected boolean nullCopied;

    protected boolean emptyStringCopied;

    public CopyOptions include(String... propertyNames) {
        includedPropertyNames = propertyNames;
        return this;
    }

    public CopyOptions exclude(String... propertyNames) {
        excludedPropertyNames = propertyNames;
        return this;
    }

    public CopyOptions copyNull() {
        nullCopied = true;
        return this;
    }

    public CopyOptions copyEmptyString() {
        emptyStringCopied = true;
        return this;
    }

    public CopyOptions converter(Converter<?> converter,
            String... propertyNames) {
        if (converter == null) {
            throw new DomaIllegalArgumentException("converter", converter);
        }
        for (String propertyName : propertyNames) {
            converterMapByName.put(propertyName, converter);
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

    protected Converter<?> getConverter(String propertyName, Class<?> destClass) {
        Converter<?> converter = converterMapByName.get(propertyName);
        if (converter != null) {
            return converter;
        }

        return null;
    }

    protected String getPattern(String propertyName) {
        return patterns.get(propertyName);
    }

    protected boolean isTargetProperty(String propertyName) {
        if (includedPropertyNames != null) {
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
        if (excludedPropertyNames != null) {
            for (String excluded : excludedPropertyNames) {
                if (excluded.equals(propertyName)) {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean isTargetValue(Object value) {
        if (value == null) {
            return nullCopied;
        }
        if ("".equals(value)) {
            return emptyStringCopied;
        }
        return true;
    }
}
