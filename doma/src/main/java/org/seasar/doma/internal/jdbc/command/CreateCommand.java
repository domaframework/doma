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
package org.seasar.doma.internal.jdbc.command;

import java.sql.Connection;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.query.CreateQuery;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.internal.message.DomaMessageCode;
import org.seasar.doma.jdbc.JdbcException;

/**
 * @author taedium
 * 
 */
public class CreateCommand<R> implements Command<R, CreateQuery<R>> {

    protected final CreateQuery<R> query;

    public CreateCommand(CreateQuery<R> query) {
        this.query = query;
    }

    @Override
    public R execute() {
        Connection connection = JdbcUtil.getConnection(query.getConfig()
                .getDataSource());
        try {
            R result = query.create(connection);
            query.complete();
            return result;
        } catch (SQLException e) {
            throw new JdbcException(DomaMessageCode.DOMA2008, e, e);
        } finally {
            JdbcUtil.close(connection, query.getConfig().getJdbcLogger());
        }
    }

}
