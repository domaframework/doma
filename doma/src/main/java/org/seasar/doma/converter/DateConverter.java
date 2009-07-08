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
public class DateConverter extends AbstractConverter<Date> {

    protected String pattern = "yyyy-MM-dd";

    @Override
    public Date doConvert(Object object) {
        if (Date.class.isInstance(object)) {
            return Date.class.cast(object);
        }
        if (java.util.Date.class.isInstance(object)) {
            java.util.Date date = java.util.Date.class.cast(object);
            return new Date(date.getTime());
        }
        if (String.class.isInstance(object)) {
            String s = String.class.cast(object);
            return parse(s);
        }
        // TODO
        throw new IllegalArgumentException();
    }

    protected Date parse(String value) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        java.util.Date date = null;
        try {
            date = df.parse(value);
        } catch (ParseException e) {
            // TODO
            e.printStackTrace();
        }
        return new Date(date.getTime());
    }

}
