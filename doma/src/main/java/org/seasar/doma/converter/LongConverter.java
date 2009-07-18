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
package org.seasar.doma.converter;

/**
 * @author taedium
 * 
 */
public class LongConverter implements Converter<Long> {

    protected static final String DEFAULT_PATTERN = "0";

    protected final ConversionSupport conversionSupport = new ConversionSupport();

    @Override
    public Long convert(Object value, String pattern) {
        if (value == null) {
            return null;
        }
        if (Long.class.isInstance(value)) {
            return Long.class.cast(value);
        }
        if (Number.class.isInstance(value)) {
            Number number = Number.class.cast(value);
            return number.longValue();
        }
        if (String.class.isInstance(value)) {
            Number number = parse(String.class.cast(value), pattern);
            return number.longValue();
        }
        throw new UnsupportedConversionException(value.getClass().getName(),
                Long.class.getName(), value);
    }

    protected Number parse(String value, String pattern) {
        String p = pattern != null ? pattern : DEFAULT_PATTERN;
        return conversionSupport.parseToNumber(value, p);
    }

}
