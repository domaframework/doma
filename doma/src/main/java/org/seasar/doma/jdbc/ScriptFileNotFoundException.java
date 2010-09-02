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
 * スクリプトファイルが見つからない場合にスローされる例外です。
 * 
 * @author taedium
 * @since 1.7.0
 */
public class ScriptFileNotFoundException extends JdbcException {

    private static final long serialVersionUID = 1L;

    /** 見つからないスクリプトファイルのパス */
    protected final String path;

    /**
     * スクリプトファイルのパスを指定してインスタンスを構築します。
     * 
     * @param path
     *            見つからないスクリプトファイルのパス
     */
    public ScriptFileNotFoundException(String path) {
        super(Message.DOMA2012, path);
        this.path = path;
    }

    /**
     * 見つからないスクリプトファイルのパスを返します。
     * 
     * @return 見つからないスクリプトファイルのパス
     */
    public String getPath() {
        return path;
    }

}
