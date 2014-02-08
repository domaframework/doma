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
 * SQLファイルが見つからない場合にスローされる例外です。
 * 
 * @author taedium
 * 
 */
public class SqlFileNotFoundException extends JdbcException {

    private static final long serialVersionUID = 1L;

    /** 見つからないSQLファイルのパス */
    protected final String path;

    /**
     * SQLファイルのパスを指定してインスタンスを構築します。
     * 
     * @param path
     *            見つからないSQLファイルのパス
     */
    public SqlFileNotFoundException(String path) {
        super(Message.DOMA2011, path);
        this.path = path;
    }

    /**
     * 見つからないSQLファイルのパスを返します。
     * 
     * @return 見つからないSQLファイルのパス
     */
    public String getPath() {
        return path;
    }

}
