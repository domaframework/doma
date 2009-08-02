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
 * {@link Boolean} へのコンバーターです。
 * 
 * @author taedium
 * 
 */
public class BooleanConverter implements Converter<Boolean> {

    @Override
    public Boolean convert(Object value, String pattern)
            throws ConversionException {
        if (value == null) {
            return null;
        }
        if (Boolean.class.isInstance(value)) {
            return Boolean.class.cast(value);
        }
        if (Number.class.isInstance(value)) {
            Number number = Number.class.cast(value);
            return number.intValue() == 1;
        }
        if (String.class.isInstance(value)) {
            return Boolean.valueOf(String.class.cast(value));
        }
        throw new UnsupportedConversionException(value.getClass().getName(),
                Boolean.class.getName(), value);
    }

}
