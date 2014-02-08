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
package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * Daoクラスのメソッドが見つからない場合にスローされる例外です。
 * 
 * @author taedium
 * @since 1.27.0
 */
public class DaoMethodNotFoundException extends JdbcException {

    private static final long serialVersionUID = 1L;

    /** クラスの名前 */
    protected final String className;

    /** メソッドのシグネチャ */
    protected final String signature;

    /**
     * クラスの名前とメソッドのシグネチャを指定してインスタンスを構築します。
     * 
     * @param cause
     *            原因
     * @param className
     *            クラス名
     * @param signature
     *            メソッドのシグネチャ
     */
    public DaoMethodNotFoundException(Throwable cause, String className,
            String signature) {
        super(Message.DOMA2215, cause, className, signature, cause);
        this.className = className;
        this.signature = signature;
    }

    /**
     * クラスの名前を返します。
     * 
     * @return クラスの名前
     */
    public String getClassName() {
        return className;
    }

    /**
     * メソッドのシグネチャを返します。
     * 
     * @return メソッドのシグネチャ
     */
    public String getSignature() {
        return signature;
    }
}
