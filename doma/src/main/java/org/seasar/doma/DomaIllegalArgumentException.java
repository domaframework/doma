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
 * 事前条件をもつパラメータへの引数が不正な場合にスローされる例外です。
 * <p>
 * {@link IllegalArgumentException} とは別にこの例外を定義しているのは、 {@literal Doma}
 * のバグによる例外なのか、 {@literal Doma}のAPIの事前条件を満たしていないことによる例外なのかを判別しやすくするためです。
 * 
 * @author taedium
 * 
 */
public class DomaIllegalArgumentException extends DomaException {

    private static final long serialVersionUID = 1L;

    /** 不正な引数のパラメータ名 */
    protected final String parameterName;

    /** 不正な引数であることの説明 */
    protected final String description;

    /**
     * インスタンスを構築します。
     * 
     * @param parameterName
     *            不正な引数のパラメータ名
     * @param description
     *            不正な引数であることの説明
     */
    public DomaIllegalArgumentException(String parameterName, String description) {
        super(Message.DOMA0002, parameterName, description);
        this.parameterName = parameterName;
        this.description = description;
    }

    /**
     * 不正な引数のパラメータ名を返します。
     * 
     * @return 不正な引数のパラメータ名
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * 不正な引数であることの説明を返します。
     * 
     * @return 不正な引数であることの説明
     */
    public String getDescription() {
        return description;
    }

}
