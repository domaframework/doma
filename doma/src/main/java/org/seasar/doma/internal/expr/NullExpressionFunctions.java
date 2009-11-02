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
package org.seasar.doma.internal.expr;

import java.sql.Date;
import java.sql.Timestamp;

import org.seasar.doma.expr.ExpressionFunctions;

/**
 * @author taedium
 * 
 */
public class NullExpressionFunctions implements ExpressionFunctions {

    @Override
    public String startWith(String text) {
        return text;
    }

    @Override
    public String startWith(String text, char escape) {
        return text;
    }

    @Override
    public String endWith(String text) {
        return text;
    }

    @Override
    public String endWith(String text, char escape) {
        return text;
    }

    @Override
    public String contain(String text) {
        return text;
    }

    @Override
    public String contain(String text, char escape) {
        return text;
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
    public Date roundUpTimePart(Date date) {
        return date;
    }

    @Override
    public Timestamp roundUpTimePart(Timestamp timestamp) {
        return timestamp;
    }

}
