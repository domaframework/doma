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

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author taedium
 * 
 */
public class DateConverter implements Converter<Date> {

    protected static String DEFAULT_PATTERN = "yyyy-MM-dd";

    @Override
    public Date convert(Object value, String pattern) {
        if (value == null) {
            return null;
        }
        if (Date.class.isInstance(value)) {
            return Date.class.cast(value);
        }
        if (java.util.Date.class.isInstance(value)) {
            java.util.Date date = java.util.Date.class.cast(value);
            return new Date(date.getTime());
        }
        if (String.class.isInstance(value)) {
            String p = pattern != null ? pattern : DEFAULT_PATTERN;
            return parse(String.class.cast(value), p);
        }
        throw new UnsupportedConversionException(value.getClass().getName(),
                Date.class.getName(), value);
    }

    public Date parse(String value, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        java.util.Date date = null;
        try {
            date = dateFormat.parse(value);
        } catch (ParseException e) {
            // TODO
            e.printStackTrace();
        }
        return new Date(date.getTime());
    }

}
