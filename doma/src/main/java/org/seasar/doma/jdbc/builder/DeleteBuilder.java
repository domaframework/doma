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
 * builder.sql(&quot;name = &quot;).param(String.class, &quot;aaa&quot;);
 * builder.sql(&quot;and&quot;);
 * builder.sql(&quot;salary = &quot;).param(int.class, 10);
 * builder.execute();
 * </pre>
 * 
 * <h5>実行されるSQL</h5>
 * 
 * <pre>
 * delete from Emp
 * where
 * name = 'aaa'
 * and
 * salary = 10
 * </pre>
 * 
 * 
 * @author taedium
 * @since 1.8.0
 */
public class DeleteBuilder {

    private final SqlNodeBuilder builder;

    private final SqlDeleteQuery query;

    private final ParameterIndex parameterIndex;

    private DeleteBuilder(Config config) {
        this.builder = new SqlNodeBuilder();
        this.query = new SqlDeleteQuery();
        this.query.setConfig(config);
        this.query.setCallerClassName(getClass().getName());
        this.parameterIndex = new ParameterIndex();
    }

    private DeleteBuilder(SqlNodeBuilder builder, SqlDeleteQuery query,
            ParameterIndex parameterIndex) {
        this.builder = builder;
        this.query = query;
        this.parameterIndex = parameterIndex;
    }

    /**
     * ファクトリメソッドです。
     * 
     * @param config
     *            設定
     * @return UPDATE文を組み立てるビルダー
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
     * @param fragment
     *            SQLの断片
     * @return このインスタンス
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     */
    public DeleteBuilder sql(String fragment) {
        if (fragment == null) {
            throw new DomaNullPointerException("fragment");
        }
        builder.appendSqlWithLineSeparator(fragment);
        return new SubsequentDeleteBuilder(builder, query, parameterIndex);
    }

    /**
     * SQLを切り取ります。
     * <p>
     * {@link #sql(String)}で追加したSQLの断片を切り取ります。
     * 
     * @param length
     *            長さ
     * @return このインスタンス
     */
    public DeleteBuilder cutBackSql(int length) {
        builder.cutBackSql(length);
        return new SubsequentDeleteBuilder(builder, query, parameterIndex);
    }

    /**
     * パラメータを追加します。
     * <p>
     * パラメータの型には、基本型とドメインクラスを指定できます。
     * 
     * @param <P>
     *            パラメータの型
     * @param parameterClass
     *            パラメータのクラス
     * @param parameter
     *            パラメータ
     * @return このインスタンス
     * @throws DomaNullPointerException
     *             {@code parameterClass} が {@code null} の場合
     */
    public <P> DeleteBuilder param(Class<P> parameterClass, P parameter) {
        String parameterName = "p" + parameterIndex.getValue();
        builder.appendParameter(parameterName);
        query.addParameter(parameterName, parameterClass, parameter);
        parameterIndex.increment();
        return new SubsequentDeleteBuilder(builder, query, parameterIndex);
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
        query.setSqlNode(builder.build());
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
     */
    public void callerClassName(String className) {
        query.setCallerClassName(className);
    }

    /**
     * 呼び出し元のメソッド名です。
     * <p>
     * 指定しない場合このSQLを生成するメソッド（{@link #execute()})）の名前が使用されます。
     * 
     * @param methodName
     *            呼び出し元のメソッド名
     */
    public void callerMethodName(String methodName) {
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
        query.setSqlNode(builder.build());
        query.prepare();
        return query.getSql();
    }

    private static class SubsequentDeleteBuilder extends DeleteBuilder {

        private SubsequentDeleteBuilder(SqlNodeBuilder builder,
                SqlDeleteQuery query, ParameterIndex parameterIndex) {
            super(builder, query, parameterIndex);
        }

        @Override
        public DeleteBuilder sql(String fragment) {
            super.builder.appendSql(fragment);
            return this;
        }

    }
}
