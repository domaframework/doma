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
package org.seasar.doma.internal.wrapper;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.Wrapper;

/**
 * {@link Wrapper} に関する例外を表します。
 * 
 * @author taedium
 * 
 */
public class WrapperException extends DomaException {

    private static final long serialVersionUID = 1L;

    /**
     * インスタンスを構築します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param args
     *            メッセージへの引数
     */
    public WrapperException(Message messageCode, Object... args) {
        super(messageCode, args);
    }

    /**
     * この例外の原因となった {@link Throwable} を指定してインスタンスを構築します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param cause
     *            原因
     * @param args
     *            メッセージへの引数
     */
    public WrapperException(Message messageCode, Throwable cause,
            Object... args) {
        super(messageCode, cause, args);
    }

}
