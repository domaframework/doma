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
package org.seasar.doma.it.helper.sqlfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.it.helper.logging.Logger;
import org.seasar.doma.jdbc.SqlKind;

/**
 * {@link SqlExecutionContext}の実装クラスです。
 * 
 * @author taedium
 */
public class SqlExecutionContext {

    /** {@link RuntimeException}のリスト */
    protected List<RuntimeException> exceptionList = new ArrayList<RuntimeException>();

    /** データソース */
    protected DataSource dataSource;

    /** コネクション */
    protected Connection connection;

    /** エラー発生時に処理を即座に中断する場合{@code true}、中断しない場合{@code false} */
    protected boolean haltOnError;

    /** ステートメント */
    protected Statement statement;

    /** 準備されたステートメント */
    protected PreparedStatement preparedStatement;

    /**
     * @param dataSource
     *            データソース
     * @param haltOnError
     *            エラー発生時に処理を即座に中断する場合{@code true}、中断しない場合{@code false}
     */
    public SqlExecutionContext(DataSource dataSource, boolean haltOnError) {
        if (dataSource == null) {
            throw new NullPointerException("dataSource");
        }
        this.dataSource = dataSource;
        this.haltOnError = haltOnError;
        openConnection();
    }

    /**
     * ステートメントを返します。
     * 
     * @return ステートメント
     */
    public Statement getStatement() {
        if (statement != null) {
            return statement;
        }
        statement = JdbcUtil.createStatement(connection);
        return statement;
    }

    /**
     * 準備されたステートメントを返します。
     * 
     * @param sql
     *            SQL
     * @return 準備されたステートメント
     */
    public PreparedStatement getPreparedStatement(String sql) {
        if (connection != null && preparedStatement != null) {
            JdbcUtil.close(preparedStatement, null);
        }
        preparedStatement = JdbcUtil.prepareStatement(connection,
                new PreparedSql(SqlKind.SELECT, sql, sql, null, Collections
                        .<PreparedSqlParameter> emptyList()));
        return preparedStatement;
    }

    /**
     * 例外のリストを返します。
     * 
     * @return 例外のリスト
     */
    public List<RuntimeException> getExceptionList() {
        return Collections.unmodifiableList(exceptionList);
    }

    /**
     * 例外を追加します。
     * 
     * @param exception
     *            例外
     */
    public void addException(RuntimeException exception) {
        closeStatements();
        closeConnection();
        if (haltOnError) {
            throw exception;
        }
        exceptionList.add(exception);
        openConnection();
    }

    /**
     * 例外を通知します。
     */
    public void notifyException() {
        closeStatements();
        closeConnection();
        openConnection();
    }

    /**
     * 破棄します。
     */
    public void destroy() {
        if (connection == null) {
            return;
        }
        closeStatements();
        closeConnection();
        exceptionList.clear();
    }

    /**
     * ステートメントをクローズします。
     */
    protected void closeStatements() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ignore) {
                Logger.debug(ignore);
            }
            statement = null;
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ignore) {
                Logger.debug(ignore);
            }
            preparedStatement = null;
        }
    }

    /**
     * コネクションをクローズします。
     */
    protected void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignore) {
                Logger.debug(ignore);
            }
            connection = null;
        }
    }

    /**
     * コネクションをオープンします。
     */
    protected void openConnection() {
        connection = JdbcUtil.getConnection(dataSource);
    }
}
