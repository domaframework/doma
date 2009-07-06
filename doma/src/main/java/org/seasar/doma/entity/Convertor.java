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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author taedium
 * 
 */
public class Convertor {

    public Boolean toBoolean(Object value) {
        if (value == null) {
            return null;
        }
        return BooleanConvertor.toBoolean(value);
    }

    public String toString(Boolean value) {
        if (value == null) {
            return null;
        }
        return BooleanConvertor.toString(value);
    }

    protected static class BooleanConvertor {

        public static Boolean toBoolean(Object value) {
            if (Boolean.class.isInstance(value)) {
                return Boolean.class.cast(value);
            }
            if (String.class.isInstance(value)) {
                String s = String.class.cast(value);
                if (s.isEmpty()) {
                    return null;
                }
                return Boolean.valueOf(s);
            }
            // TODO
            throw new IllegalArgumentException();
        }

        public static String toString(Boolean value) {
            return value.toString();
        }
    }

    public Byte toByte(Object value) {
        if (value == null) {
            return null;
        }
        return ByteConvertor.toByte(value);
    }

    public String toString(Byte value) {
        if (value == null) {
            return null;
        }
        return ByteConvertor.toString(value);
    }

    protected static class ByteConvertor {

        public static Byte toByte(Object value) {
            if (Byte.class.isInstance(value)) {
                return Byte.class.cast(value);
            }
            if (Number.class.isInstance(value)) {
                Number number = Number.class.cast(value);
                return number.byteValue();
            }
            if (String.class.isInstance(value)) {
                String s = String.class.cast(value);
                if (s.isEmpty()) {
                    return null;
                }
                return Byte.valueOf(s);
            }
            // TODO
            throw new IllegalArgumentException();
        }

        public static String toString(Byte value) {
            return value.toString();
        }
    }

    public Short toShort(Object value) {
        if (value == null) {
            return null;
        }
        return ShortConvertor.toShort(value);
    }

    public String toString(Short value) {
        if (value == null) {
            return null;
        }
        return ShortConvertor.toString(value);
    }

    protected static class ShortConvertor {

        public static Short toShort(Object value) {
            if (Short.class.isInstance(value)) {
                return Short.class.cast(value);
            }
            if (Number.class.isInstance(value)) {
                Number number = Number.class.cast(value);
                return number.shortValue();
            }
            if (String.class.isInstance(value)) {
                String s = String.class.cast(value);
                if (s.isEmpty()) {
                    return null;
                }
                return Short.valueOf(s);
            }
            // TODO
            throw new IllegalArgumentException();
        }

        public static String toString(Short value) {
            return value.toString();
        }
    }

    public Integer toInteger(Object value) {
        if (value == null) {
            return null;
        }
        return IntegerConvertor.toInteger(value);
    }

    public String toString(Integer value) {
        if (value == null) {
            return null;
        }
        return IntegerConvertor.toString(value);
    }

    protected static class IntegerConvertor {

        public static Integer toInteger(Object value) {
            if (Integer.class.isInstance(value)) {
                return Integer.class.cast(value);
            }
            if (Number.class.isInstance(value)) {
                Number number = Number.class.cast(value);
                return number.intValue();
            }
            if (String.class.isInstance(value)) {
                String s = String.class.cast(value);
                return Integer.valueOf(s);
            }
            // TODO
            throw new IllegalArgumentException();
        }

        public static String toString(Integer value) {
            return value.toString();
        }
    }

    public Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        return LongConvertor.toLong(value);
    }

    public String toString(Long value) {
        if (value == null) {
            return null;
        }
        return LongConvertor.toString(value);
    }

    protected static class LongConvertor {

        public static Long toLong(Object value) {
            if (Long.class.isInstance(value)) {
                return Long.class.cast(value);
            }
            if (Number.class.isInstance(value)) {
                Number number = Number.class.cast(value);
                return number.longValue();
            }
            if (String.class.isInstance(value)) {
                String s = String.class.cast(value);
                return Long.valueOf(s);
            }
            // TODO
            throw new IllegalArgumentException();
        }

        public static String toString(Long value) {
            return value.toString();
        }
    }

    public Float toFloat(Object value) {
        if (value == null) {
            return null;
        }
        return FloatConvertor.toFloat(value);
    }

    public String toString(Float value) {
        if (value == null) {
            return null;
        }
        return FloatConvertor.toString(value);
    }

    protected static class FloatConvertor {

        public static Float toFloat(Object value) {
            if (Float.class.isInstance(value)) {
                return Float.class.cast(value);
            }
            if (Number.class.isInstance(value)) {
                Number number = Number.class.cast(value);
                return number.floatValue();
            }
            if (String.class.isInstance(value)) {
                String s = String.class.cast(value);
                return Float.valueOf(s);
            }
            // TODO
            throw new IllegalArgumentException();
        }

        public static String toString(Float value) {
            return value.toString();
        }
    }

    public Double toDouble(Object value) {
        if (value == null) {
            return null;
        }
        return DoubleConvertor.toDouble(value);
    }

    public String toString(Double value) {
        if (value == null) {
            return null;
        }
        return DoubleConvertor.toString(value);
    }

    protected static class DoubleConvertor {

        public static Double toDouble(Object value) {
            if (Double.class.isInstance(value)) {
                return Double.class.cast(value);
            }
            if (Number.class.isInstance(value)) {
                Number number = Number.class.cast(value);
                return number.doubleValue();
            }
            if (String.class.isInstance(value)) {
                String s = String.class.cast(value);
                return Double.valueOf(s);
            }
            // TODO
            throw new IllegalArgumentException();
        }

        public static String toString(Double value) {
            return value.toString();
        }
    }

    public BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        return BigDecimalConvertor.toBigDecimal(value);
    }

    public String toString(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return BigDecimalConvertor.toString(value);
    }

    protected static class BigDecimalConvertor {

        public static BigDecimal toBigDecimal(Object value) {
            if (BigDecimal.class.isInstance(value)) {
                return BigDecimal.class.cast(value);
            }
            if (Number.class.isInstance(value)) {
                return new BigDecimal(value.toString());
            }
            if (String.class.isInstance(value)) {
                String s = String.class.cast(value);
                if (s.isEmpty()) {
                    return null;
                }
                return new BigDecimal(s);
            }
            // TODO
            throw new IllegalArgumentException();
        }

        public static String toString(BigDecimal value) {
            return value.toPlainString();
        }
    }

    public Date toDate(Object value) {
        if (value == null) {
            return null;
        }
        return DateConvertor.toDate(value);
    }

    public String toString(Date value) {
        if (value == null) {
            return null;
        }
        return DateConvertor.toString(value);
    }

    protected static class DateConvertor {

        public static Date toDate(Object value) {
            if (Date.class.isInstance(value)) {
                return Date.class.cast(value);
            }
            if (java.util.Date.class.isInstance(value)) {
                java.util.Date date = java.util.Date.class.cast(value);
                return new Date(date.getTime());
            }
            if (String.class.isInstance(value)) {
                String s = String.class.cast(value);
                if (s.isEmpty()) {
                    return null;
                }
                return toDate(s, "yyyy-MM-dd");
            }
            // TODO
            throw new IllegalArgumentException();
        }

        public static Date toDate(String value, String pattern) {
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

        public static String toString(Date value) {
            return value.toString();
        }

        public static String toString(Date value, String pattern) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(value);
        }
    }

    public Time toTime(Object value) {
        if (value == null) {
            return null;
        }
        return TimeConvertor.toTime(value);
    }

    public String toString(Time value) {
        if (value == null) {
            return null;
        }
        return TimeConvertor.toString(value);
    }

    protected static class TimeConvertor {

        public static Time toTime(Object value) {
            if (Time.class.isInstance(value)) {
                return Time.class.cast(value);
            }
            if (java.util.Date.class.isInstance(value)) {
                java.util.Date date = java.util.Date.class.cast(value);
                return new Time(date.getTime());
            }
            if (String.class.isInstance(value)) {
                String s = String.class.cast(value);
                if (s.isEmpty()) {
                    return null;
                }
                return toTime(s, "hh:mm:dd");
            }
            // TODO
            throw new IllegalArgumentException();
        }

        public static Time toTime(String value, String pattern) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            java.util.Date date = null;
            try {
                date = df.parse(value);
            } catch (ParseException e) {
                // TODO
                e.printStackTrace();
            }
            return new Time(date.getTime());
        }

        public static String toString(Time value) {
            return value.toString();
        }

        public static String toString(Time value, String pattern) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(value);
        }
    }

    public Timestamp toTimestamp(Object value) {
        if (value == null) {
            return null;
        }
        return TimestampConvertor.toTimestamp(value);
    }

    public String toString(Timestamp value) {
        if (value == null) {
            return null;
        }
        return TimestampConvertor.toString(value);
    }

    protected static class TimestampConvertor {

        public static Timestamp toTimestamp(Object value) {
            if (Timestamp.class.isInstance(value)) {
                return Timestamp.class.cast(value);
            }
            if (java.util.Date.class.isInstance(value)) {
                java.util.Date date = java.util.Date.class.cast(value);
                return new Timestamp(date.getTime());
            }
            if (String.class.isInstance(value)) {
                String s = String.class.cast(value);
                if (s.isEmpty()) {
                    return null;
                }
                return toTimestamp(s, "yyyy-MM-dd hh:mm:dd");
            }
            // TODO
            throw new IllegalArgumentException();
        }

        public static Timestamp toTimestamp(String value, String pattern) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            java.util.Date date = null;
            try {
                date = df.parse(value);
            } catch (ParseException e) {
                // TODO
                e.printStackTrace();
            }
            return new Timestamp(date.getTime());
        }

        public static String toString(Timestamp value) {
            return value.toString();
        }

        public static String toString(Timestamp value, String pattern) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(value);
        }
    }

    public String toString(Object value) {
        if (value == null) {
            return null;
        }
        return StringConvertor.toString(value);
    }

    protected static class StringConvertor {

        public static String toString(Object value) {
            return value.toString();
        }

    }
}
