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
package org.seasar.doma.jdbc.holder;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * ドメインクラスに対応するメタクラスが見つからない場合にスローされる例外です。
 * 
 * @author taedium
 * @since 1.8.0
 */
public class HolderTypeNotFoundException extends JdbcException {

    private static final long serialVersionUID = 1L;

    private final String holderClassName;

    private final String holderTypeClassName;

    /**
     * インスタンスを構築します。
     * 
     * @param cause
     *            原因
     * @param holderClassName
     *            ドメインクラスの名前
     * @param holderTypeClassName
     *            ドメインタイプクラスの名前
     */
    public HolderTypeNotFoundException(Throwable cause, String holderClassName,
            String holderTypeClassName) {
        super(Message.DOMA2202, cause, holderClassName, holderTypeClassName,
                cause);
        this.holderClassName = holderClassName;
        this.holderTypeClassName = holderTypeClassName;
    }

    /**
     * ドメインクラスの名前を返します。
     * 
     * @return ドメインクラスの名前
     */
    public String getHolderClassName() {
        return holderClassName;
    }

    /**
     * ドメインタイプクラスの名前を返します。
     * 
     * @return ドメインタイプクラスの名前
     */
    public String getHolderTypeClassName() {
        return holderTypeClassName;
    }

}
