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

import java.util.List;

/**
 * An SQL.
 * 
 * <p>
 * The implementation class is not required to be thread safe.
 * 
 * @param <P>
 *            the parameter type
 */
public interface Sql<P extends SqlParameter> {

    /**
     * Returns the kind of the SQL.
     * 
     * @return the kind of the SQL
     */
    SqlKind getKind();

    /**
     * Returns the raw SQL string.
     * <p>
     * The bind variables are displayed as {@code ?}.
     * 
     * @return the raw SQL string
     */
    String getRawSql();

    /**
     * Returns the formatted SQL string.
     * <p>
     * The bind variables are replaced with the string representations of the
     * parameters.
     * 
     * @return the formatted SQL string
     */
    String getFormattedSql();

    /**
     * Returns the file path that contains this SQL.
     * 
     * @return the file path that contains this SQL、or {@code null} if this SQL
     *         is auto-generated
     */
    String getSqlFilePath();

    /**
     * Returns the parameter list.
     * 
     * @return the parameter list
     */
    List<P> getParameters();

    /**
     * Returns the type of the SQL log.
     * 
     * @return the type of the SQL log
     */
    SqlLogType getSqlLogType();
}
