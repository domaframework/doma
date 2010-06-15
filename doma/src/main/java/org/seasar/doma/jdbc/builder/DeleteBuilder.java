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
package org.seasar.doma.jdbc.builder;

import java.sql.Statement;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.command.DeleteCommand;
import org.seasar.doma.internal.jdbc.query.SqlDeleteQuery;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Sql;

/**
 * DELETE文を組み立て実行するクラスです。
 * <p>
 * このクラスはスレッドセーフではありません。
 * 
 * <h4>例</h4>
 * <h5>Java</h5>
 * 
 * <pre>
 * DeleteBuilder builder = DeleteBuilder.newInstance(config);
 * builder.sql(&quot;delete from Emp&quot;);
 * builder.sql(&quot;where&quot;);
 * builder.sql(&quot;name = &quot;).param(String.class, &quot;SMITH&quot;);
 * builder.sql(&quot;and&quot;);
 * builder.sql(&quot;salary = &quot;).param(BigDecimal.class, new BigDecimal(1000));
 * builder.execute();
 * </pre>
 * 
 * <h5>実行されるSQL</h5>
 * 
 * <pre>
 * delete from Emp
 * where
 * name = 'SMITH'
 * and
 * salary = 1000
 * </pre>
 * 
 * 
 * @author taedium
 * @since 1.8.0
 */
public class DeleteBuilder {

    private final BuildingHelper helper;

    private final SqlDeleteQuery query;

    private final ParamIndex paramIndex;

    private DeleteBuilder(Config config) {
        this.helper = new BuildingHelper();
        this.query = new SqlDeleteQuery();
        this.query.setConfig(config);
        this.query.setCallerClassName(getClass().getName());
        this.paramIndex = new ParamIndex();
    }

    private DeleteBuilder(BuildingHelper builder, SqlDeleteQuery query,
            ParamIndex parameterIndex) {
        this.helper = builder;
        this.query = query;
        this.paramIndex = parameterIndex;
    }

    /**
     * ファクトリメソッドです。
     * 
     * @param config
     *            設定
     * @return DELETE文を組み立てるビルダー
     * @throws DomaNullPointerException
     *             引数が{@code null} の場合
     */
    public static DeleteBuilder newInstance(Config config) {
        if (config == null) {
            throw new DomaNullPointerException("config");
        }
        return new DeleteBuilder(config);
    }

    /**
     * SQLの断片を追加します。
     * 
     * @param sql
     *            SQLの断片
     * @return このインスタンス
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     */
    public DeleteBuilder sql(String sql) {
        if (sql == null) {
            throw new DomaNullPointerException("sql");
        }
        helper.appendSqlWithLineSeparator(sql);
        return new SubsequentDeleteBuilder(helper, query, paramIndex);
    }

    /**
     * 最後に追加したSQLもしくはパラメータを削除します。
     * 
     * @return このインスタンス
     */
    public DeleteBuilder removeLast() {
        helper.removeLast();
        return new SubsequentDeleteBuilder(helper, query, paramIndex);
    }

    /**
     * パラメータを追加します。
     * <p>
     * パラメータの型には、基本型とドメインクラスを指定できます。
     * 
     * @param <P>
     *            パラメータの型
     * @param paramClass
     *            パラメータのクラス
     * @param param
     *            パラメータ
     * @return このインスタンス
     * @throws DomaNullPointerException
     *             {@code parameterClass} が {@code null} の場合
     */
    public <P> DeleteBuilder param(Class<P> paramClass, P param) {
        if (paramClass == null) {
            throw new DomaNullPointerException("paramClass");
        }
        helper.appendParam(new Param(paramClass, param, paramIndex));
        paramIndex.increment();
        return new SubsequentDeleteBuilder(helper, query, paramIndex);
    }

    /**
     * SQLを実行します。
     * 
     * @return 更新件数
     * @throws JdbcException
     *             JDBCに関する例外が発生した場合
     */
    public int execute() {
        if (query.getMethodName() == null) {
            query.setCallerMethodName("execute");
        }
        for (Param p : helper.getParams()) {
            query.addParameter(p.name, p.paramClass, p.param);
        }
        query.setSqlNode(helper.getSqlNode());
        query.prepare();
        DeleteCommand command = new DeleteCommand(query);
        int result = command.execute();
        query.complete();
        return result;
    }

    /**
     * クエリタイムアウト（秒）を設定します。
     * <p>
     * 指定しない場合、 {@link Config#getQueryTimeout()} が使用されます。
     * 
     * @param queryTimeout
     *            クエリタイムアウト（秒）
     * @see Statement#setQueryTimeout(int)
     */
    public void queryTimeout(int queryTimeout) {
        query.setQueryTimeout(queryTimeout);
    }

    /**
     * 呼び出し元のクラス名です。
     * <p>
     * 指定しない場合このクラスの名前が使用されます。
     * 
     * @param className
     *            呼び出し元のクラス名
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     */
    public void callerClassName(String className) {
        if (className == null) {
            throw new DomaNullPointerException("className");
        }
        query.setCallerClassName(className);
    }

    /**
     * 呼び出し元のメソッド名です。
     * <p>
     * 指定しない場合このSQLを生成するメソッド（{@link #execute()})）の名前が使用されます。
     * 
     * @param methodName
     *            呼び出し元のメソッド名
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     */
    public void callerMethodName(String methodName) {
        if (methodName == null) {
            throw new DomaNullPointerException("methodName");
        }
        query.setCallerMethodName(methodName);
    }

    /**
     * 組み立てられたSQLを返します。
     * 
     * @return 組み立てられたSQL
     */
    public Sql<?> getSql() {
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getSql");
        }
        query.setSqlNode(helper.getSqlNode());
        query.prepare();
        return query.getSql();
    }

    private static class SubsequentDeleteBuilder extends DeleteBuilder {

        private SubsequentDeleteBuilder(BuildingHelper builder,
                SqlDeleteQuery query, ParamIndex parameterIndex) {
            super(builder, query, parameterIndex);
        }

        @Override
        public DeleteBuilder sql(String sql) {
            if (sql == null) {
                throw new DomaNullPointerException("sql");
            }
            super.helper.appendSql(sql);
            return this;
        }

    }
}
