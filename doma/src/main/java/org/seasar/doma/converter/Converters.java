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
public class Converters {

    public static final StringConverter STRING = new StringConverter();

    public static final BigDecimalConverter BIG_DECIMAL = new BigDecimalConverter();

    public static final BooleanConverter BOOLEAN = new BooleanConverter();

    public static final ByteConverter BYTE = new ByteConverter();

    public static final ShortConverter SHORT = new ShortConverter();

    public static final IntegerConverter INTEGER = new IntegerConverter();

    public static final LongConverter LONG = new LongConverter();

    public static final FloatConverter FLOAT = new FloatConverter();

    public static final DoubleConverter DOUBLE = new DoubleConverter();

    public static final UtilDateConverter UTIL_DATE = new UtilDateConverter();

    public static final DateConverter DATE = new DateConverter();

    public static final TimeConverter TIME = new TimeConverter();

    public static final TimestampConverter TIMESTAMP = new TimestampConverter();
}
