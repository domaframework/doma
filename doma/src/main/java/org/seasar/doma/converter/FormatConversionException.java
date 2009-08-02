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

import org.seasar.doma.message.DomaMessageCode;

/**
 * フォーマット変換に失敗した場合にスローされる例外です。
 * 
 * @author taedium
 * 
 */
public class FormatConversionException extends ConversionException {

    private static final long serialVersionUID = 1L;

    /** フォーマット対象の値 */
    protected final String value;

    /** フォーマットに使用されるパターン */
    protected final String pattern;

    /**
     * インスタンスを構築します。
     * 
     * @param value
     *            フォーマット対象の値
     * @param pattern
     *            フォーマットに使用されるパターン
     * @param cause
     *            原因
     */
    public FormatConversionException(String value, String pattern,
            Throwable cause) {
        super(DomaMessageCode.DOMA5003, value, pattern, cause, cause);
        this.value = value;
        this.pattern = pattern;
    }

    public String getValue() {
        return value;
    }

    public String getPattern() {
        return pattern;
    }

}
