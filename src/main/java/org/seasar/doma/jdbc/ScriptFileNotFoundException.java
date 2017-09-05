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
 * Thrown to indicate that a script file is not found.
 */
public class ScriptFileNotFoundException extends JdbcException {

    private static final long serialVersionUID = 1L;

    /** the script file path */
    protected final String path;

    /**
     * Creates an instance.
     * 
     * @param path
     *            the script file path
     */
    public ScriptFileNotFoundException(String path) {
        super(Message.DOMA2012, path);
        this.path = path;
    }

    /**
     * Returns the script file path.
     * 
     * @return the script file path
     */
    public String getPath() {
        return path;
    }

}
