package org.seasar.doma.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.seasar.doma.DomaNullPointerException;

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

/**
 * 変換をサポートするクラスです。
 * 
 * @author taedium
 * 
 */
public class ConversionSupport {

    /**
     * 文字列をパースし {@code BigDecimal} に変換します。
     * 
     * @param value
     *            文字列
     * @param pattern
     *            パターン
     * @return 変換された数値
     * @throws ConversionException
     *             パースに失敗した場合
     */
    public BigDecimal parseToBigDecimal(String value, String pattern) {
        if (value == null) {
            throw new DomaNullPointerException("value");
        }
        if (pattern == null) {
            throw new DomaNullPointerException("pattern");
        }
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        decimalFormat.setParseBigDecimal(true);
        try {
            Number number = decimalFormat.parse(value);
            return BigDecimal.class.cast(number);
        } catch (ParseException e) {
            throw new org.seasar.doma.converter.ParseConversionException(value,
                    pattern, e);
        }
    }

    /**
     * 文字列をパースし日付に変換します。
     * 
     * @param value
     *            文字列
     * @param pattern
     *            パターン
     * @return 変換された日付
     * @throws ConversionException
     *             パースに失敗した場合
     */
    public Date parseToDate(String value, String pattern) {
        if (value == null) {
            throw new DomaNullPointerException("value");
        }
        if (pattern == null) {
            throw new DomaNullPointerException("pattern");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            throw new org.seasar.doma.converter.ParseConversionException(value,
                    pattern, e);
        }
    }

    /**
     * 数値から文字列にフォーマットします。
     * 
     * @param value
     *            数値
     * @param pattern
     *            パターン
     * @return フォーマットされた文字列
     * @throws ConversionException
     *             フォーマットに失敗した場合
     */
    public String formatFromNumber(Number value, String pattern) {
        if (value == null) {
            throw new DomaNullPointerException("value");
        }
        if (pattern == null) {
            throw new DomaNullPointerException("pattern");
        }
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        try {
            return decimalFormat.format(value);
        } catch (IllegalArgumentException e) {
            throw new FormatConversionException(value.toString(), pattern, e);
        }
    }

    /**
     * 日付から文字列にフォーマットします。
     * 
     * @param value
     *            日付
     * @param pattern
     *            パターン
     * @return フォーマットされた文字列
     * @throws ConversionException
     *             フォーマットに失敗した場合
     */
    public String formatFromDate(Date value, String pattern) {
        if (value == null) {
            throw new DomaNullPointerException("value");
        }
        if (pattern == null) {
            throw new DomaNullPointerException("pattern");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.format(value);
        } catch (IllegalArgumentException e) {
            throw new FormatConversionException(value.toString(), pattern, e);
        }
    }
}
