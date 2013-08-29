/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.doma.internal.expr;

import java.sql.Date;
import java.sql.Timestamp;

import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.util.CharSequenceUtil;

/**
 * @author taedium
 * 
 */
public class NullExpressionFunctions implements ExpressionFunctions {

    @Override
    public String escape(String text) {
        return null;
    }

    @Override
    public String escape(String text, char escapeChar) {
        return null;
    }

    @Override
    public String prefix(String text) {
        return text;
    }

    @Override
    public String prefix(String text, char escape) {
        return text;
    }

    @Override
    public String suffix(String text) {
        return text;
    }

    @Override
    public String suffix(String text, char escape) {
        return text;
    }

    @Override
    public String infix(String text) {
        return text;
    }

    @Override
    public String infix(String text, char escapeChar) {
        return text;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String contain(String text) {
        return text;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String contain(String text, char escape) {
        return text;
    }

    @Override
    public java.util.Date roundDownTimePart(java.util.Date date) {
        return date;
    }

    @Override
    public Date roundDownTimePart(Date date) {
        return date;
    }

    @Override
    public Timestamp roundDownTimePart(Timestamp timestamp) {
        return timestamp;
    }

    @Override
    public java.util.Date roundUpTimePart(java.util.Date date) {
        return date;
    }

    @Override
    public Date roundUpTimePart(Date date) {
        return date;
    }

    @Override
    public Timestamp roundUpTimePart(Timestamp timestamp) {
        return timestamp;
    }

    @Override
    public boolean isEmpty(CharSequence charSequence) {
        return CharSequenceUtil.isEmpty(charSequence);
    }

    @Override
    public boolean isNotEmpty(CharSequence charSequence) {
        return CharSequenceUtil.isNotEmpty(charSequence);
    }

    @Override
    public boolean isBlank(CharSequence charSequence) {
        return CharSequenceUtil.isBlank(charSequence);
    }

    @Override
    public boolean isNotBlank(CharSequence charSequence) {
        return CharSequenceUtil.isNotBlank(charSequence);
    }

}
