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
package org.seasar.doma.jdbc.tx;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * セーブポイントがすでに存在する場合にスローされる例外です。
 * 
 * @author taedium
 * @since 1.1.0
 */
public class SavepointAlreadyExistsException extends JdbcException {

    private static final long serialVersionUID = 1L;

    /** セーブポイントの名前 */
    protected final String savepointName;

    /**
     * インスタンスを構築します。
     * 
     * @param savepointName
     *            セーブポイントの名前
     */
    public SavepointAlreadyExistsException(String savepointName) {
        super(Message.DOMA2059, savepointName);
        this.savepointName = savepointName;
    }

    /**
     * セーブポイントの名前を返します。
     * 
     * @return セーブポイントの名前
     */
    public String getSavepointName() {
        return savepointName;
    }
}
