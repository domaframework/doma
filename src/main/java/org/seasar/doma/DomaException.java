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

import org.seasar.doma.message.MessageResource;

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

    /** メッセージリソース */
    protected final MessageResource messageResource;

    /** メッセージへの引数 */
    protected final Object args;

    /**
     * インスタンスを構築します。
     * 
     * @param messageResource
     *            メッセージリソース
     * @param args
     *            メッセージへの引数
     */
    public DomaException(MessageResource messageResource, Object... args) {
        this(messageResource, null, args);
    }

    /**
     * この例外の原因となった {@link Throwable} を指定してインスタンスを構築します。
     * 
     * @param messageResource
     *            メッセージリソース
     * @param cause
     *            原因
     * @param args
     *            メッセージへの引数
     */
    public DomaException(MessageResource messageResource, Throwable cause,
            Object... args) {
        super(messageResource.getMessage(args), cause);
        this.messageResource = messageResource;
        this.args = args;
    }

    /**
     * メッセージリソースを返します。
     * 
     * @return メッセージリソース
     */
    public MessageResource getMessageResource() {
        return messageResource;
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
