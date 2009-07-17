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

import java.math.BigDecimal;

/**
 * @author taedium
 * 
 */
public class BigDecimalConverter extends NumberConverter implements
        Converter<BigDecimal> {

    @Override
    public BigDecimal convert(Object value, String pattern) {
        if (value == null) {
            return null;
        }
        if (BigDecimal.class.isInstance(value)) {
            return BigDecimal.class.cast(value);
        }
        if (Number.class.isInstance(value)) {
            return new BigDecimal(value.toString());
        }
        if (String.class.isInstance(value)) {
            return parse(String.class.cast(value), pattern);
        }
        throw new UnsupportedConversionException(value.getClass().getName(),
                BigDecimal.class.getName(), value);
    }

    @Override
    protected BigDecimal parse(String value, String pattern) {
        Number number = super.parse(value, pattern);
        return new BigDecimal(number.doubleValue());
    }

}
