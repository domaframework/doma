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
package org.seasar.doma;

import org.seasar.doma.message.Message;

/**
 * {@code null} でないことを期待されたパラメータへの引数が {@code null} の場合にスローされる例外です。
 * <p>
 * {@link NullPointerException} とは別にこの例外を定義しているのは、 {@literal Doma}のバグによる例外なのか、
 * {@literal Doma}のAPIの事前条件を満たしていないことによる例外なのかを判別しやすくするためです。
 * 
 * @author taedium
 * 
 */
public class DomaNullPointerException extends DomaException {

    private static final long serialVersionUID = 1L;

    /** {@code null} であるパラメータの名前 */
    protected final String parameterName;

    /**
     * インスタンスを構築します。
     * 
     * @param parameterName
     *            {@code null} であるパラメータの名前
     */
    public DomaNullPointerException(String parameterName) {
        super(Message.DOMA0001, parameterName);
        this.parameterName = parameterName;
    }

    /**
     * {@code null} であるパラメータの名前を返します。
     * 
     * @return {@code null} であるパラメータの名前
     */
    public String getParameterName() {
        return parameterName;
    }

}
