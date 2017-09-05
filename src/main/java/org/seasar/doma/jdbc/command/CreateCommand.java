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
package org.seasar.doma.jdbc.command;

import java.sql.Connection;
import java.sql.SQLException;

import org.seasar.doma.ArrayFactory;
import org.seasar.doma.BlobFactory;
import org.seasar.doma.ClobFactory;
import org.seasar.doma.NClobFactory;
import org.seasar.doma.SQLXMLFactory;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.query.CreateQuery;
import org.seasar.doma.message.Message;

/**
 * A command to create a JDBC object that is mapped to the SQL type.
 * 
 * @param <RESULT>
 *            the result type
 * @see ArrayFactory
 * @see BlobFactory
 * @see ClobFactory
 * @see NClobFactory
 * @see SQLXMLFactory
 */
public class CreateCommand<RESULT> implements Command<RESULT> {

    protected final CreateQuery<RESULT> query;

    public CreateCommand(CreateQuery<RESULT> query) {
        this.query = query;
    }

    @Override
    public RESULT execute() {
        Connection connection = JdbcUtil.getConnection(query.getConfig().getDataSource());
        try {
            return query.create(connection);
        } catch (SQLException e) {
            throw new JdbcException(Message.DOMA2008, e, e);
        } finally {
            JdbcUtil.close(connection, query.getConfig().getJdbcLogger());
        }
    }

}
