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
package org.seasar.doma;

/**
 * このフレームワークでルートとなる実行時例外です。
 * <p>
 * このフレームワークで定義される実行時例外はすべてこのクラスのサブタイプとなります。
 * 
 * @author taedium
 * 
 */
public class DomaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** メッセージコード */
    protected final MessageCode messageCode;

    /** メッセージへの引数 */
    protected final Object args;

    /**
     * インスタンスを構築します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param args
     *            メッセージへの引数
     */
    public DomaException(MessageCode messageCode, Object... args) {
        this(messageCode, null, args);
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
    public DomaException(MessageCode messageCode, Throwable cause,
            Object... args) {
        super(messageCode.getMessage(args), cause);
        this.messageCode = messageCode;
        this.args = args;
    }

    /**
     * メッセージコードを返します。
     * 
     * @return メッセージコード
     */
    public MessageCode getMessageCode() {
        return messageCode;
    }

    /**
     * メッセージへの引数を返します。
     * 
     * @return メッセージへの引数
     */
    public Object getArgs() {
        return args;
    }

}
