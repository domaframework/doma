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

import java.util.function.Function;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.BatchModifyCommand;
import org.seasar.doma.jdbc.query.SqlBatchModifyQuery;
import org.seasar.doma.message.Message;

/**
 * バッチ更新で利用される、SQL文を組み立てるクラスです。
 *
 * @author bakenezumi
 * @since 2.14.0
 */
public abstract class BatchBuilder<Q extends SqlBatchModifyQuery> {

    final BatchBuildingHelper helper;

    final Q query;

    final ParamIndex paramIndex;

    final Map<Integer, String> paramNameMap;

    BatchBuilder(Q query) {
        this.helper = new BatchBuildingHelper();
        this.query = query;
        this.paramIndex = new ParamIndex();
        paramNameMap = new HashMap<>();
    }

    BatchBuilder(BatchBuildingHelper builder, Q query,
                 ParamIndex paramIndex, Map<Integer, String> paramNameMap) {
        this.helper = builder;
        this.query = query;
        this.paramIndex = paramIndex;
        this.paramNameMap = paramNameMap;
    }

    static BatchBuilder newInstance(SqlBatchModifyQuery query) {
        if (query == null) {
            throw new DomaNullPointerException("query");
        }
        if (query.getClassName() == null) {
            query.setCallerClassName(BatchBuilder.class.getName());
        }
        return new InitialBatchBuilder(query);
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
    public abstract BatchBuilder sql(String sql);

    /**
     * 最後に追加したSQLもしくはパラメータを削除します。
     *
     * @return このインスタンス
     */
    public abstract BatchBuilder removeLast();

    /**
     * パラメータを追加します。
     * <p>
     * パラメータの型には、基本型とドメインクラスを指定できます。
     *
     * @param <P>
     *            パラメータの型
     * @param paramClass
     *            パラメータの要素のクラス
     * @param param
     *            パラメータ
     * @return このインスタンス
     * @throws DomaNullPointerException
     *             {@code paramClass} が {@code null} の場合
     */
    public <P> BatchBuilder param(Class<P> paramClass, P param) {
        if (paramClass == null) {
            throw new DomaNullPointerException("paramClass");
        }
        return appendParam(paramClass, param, false);
    }

    /**
     * リテラルとしてパラメータを追加します。
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
     *             {@code paramClass} が {@code null} の場合
     */
    public <P> BatchBuilder literal(Class<P> paramClass, P param) {
        if (paramClass == null) {
            throw new DomaNullPointerException("paramClass");
        }
        return appendParam(paramClass, param, true);
    }

    abstract <P> BatchBuilder appendParam(Class<P> paramClass, P param, boolean literal);

    BatchBuilder fixSql() {
        return new FixedBatchBuilder(helper, query, paramNameMap);
    }

    private void prepare() {
        query.clearParameters();
        for (BatchParam p : helper.getParams()) {
            query.addParameter(p.name, p.paramClass, p.params);
        }
        query.setSqlNode(helper.getSqlNode());
        query.prepare();
    }

    int[] execute(Function<Q, BatchModifyCommand> commandBuilder) {
        if (query.getMethodName() == null) {
            query.setCallerMethodName("execute");
        }
        prepare();
        BatchModifyCommand command = commandBuilder.apply(query);
        int[] result = command.execute();
        query.complete();
        return result;
    }

    List<? extends Sql<?>> getSqls() {
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getSqls");
        }
        prepare();
        return query.getSqls();
    }

    private static class InitialBatchBuilder<Q extends SqlBatchModifyQuery> extends BatchBuilder<Q> {

        private InitialBatchBuilder(Q query) {
            super(query);
        }

        private InitialBatchBuilder(BatchBuildingHelper builder, Q query,
                                    ParamIndex paramIndex, Map<Integer, String> paramNameMap) {
            super(builder, query, paramIndex, paramNameMap);
        }

        @Override
        public BatchBuilder sql(String sql) {
            if (sql == null) {
                throw new DomaNullPointerException("sql");
            }
            helper.appendSqlWithLineSeparator(sql);
            return new SubsequentBatchBuilder(helper, query, paramIndex, paramNameMap);
        }

        @Override
        public BatchBuilder removeLast() {
            helper.removeLast();
            return new SubsequentBatchBuilder(helper, query, paramIndex, paramNameMap);
        }

        @Override
        <P> BatchBuilder appendParam(Class<P> paramClass, P param, boolean literal) {
            BatchParam batchParam = new BatchParam(paramClass, paramIndex, literal);
            batchParam.add(param);
            helper.appendParam(batchParam);
            paramNameMap.put(paramIndex.getValue(), batchParam.name);
            paramIndex.increment();
            return new SubsequentBatchBuilder(helper, query, paramIndex, paramNameMap);
        }

    }

    private static class SubsequentBatchBuilder<Q extends SqlBatchModifyQuery> extends InitialBatchBuilder<Q> {

        SubsequentBatchBuilder(BatchBuildingHelper builder,
                               Q query, ParamIndex paramIndex,
                               Map<Integer, String> paramNameMap) {
            super(builder, query, paramIndex, paramNameMap);
        }

        @Override
        public BatchBuilder sql(String sql) {
            if (sql == null) {
                throw new DomaNullPointerException("sql");
            }
            super.helper.appendSql(sql);
            return this;
        }
    }

    private static class FixedBatchBuilder<Q extends SqlBatchModifyQuery> extends BatchBuilder<Q> {

        private FixedBatchBuilder(BatchBuildingHelper builder, Q query, Map<Integer, String> paramNameMap) {
            super(builder, query, new ParamIndex(), paramNameMap);
        }

        @Override
        public BatchBuilder sql(String sql) {
            return this;
        }

        @Override
        public BatchBuilder removeLast() {
            return this;
        }

        @Override
        <P> BatchBuilder appendParam(Class<P> paramClass, P param, boolean literal) {
            final String paramName = paramNameMap.get(paramIndex.getValue());
            if (paramName == null) {
                throw new JdbcException(Message.DOMA2231);
            }
            BatchParam batchParam = helper.getParam(paramName);
            if (paramClass != batchParam.paramClass) {
                // BatchParamの初期値が型:Object、値:nullの場合に限り型の上書きを許可
                if (batchParam.paramClass == Object.class) {
                    batchParam = new BatchParam(batchParam, paramClass);
                    helper.modifyParam(batchParam);
                } else if (param != null || paramClass != Object.class) {
                    // 型違いは型:Object、値:nullの場合のみ許可
                    throw new JdbcException(Message.DOMA2229);
                }
            }
            if (literal != batchParam.literal) {
                throw new JdbcException(Message.DOMA2230);
            }
            batchParam.add(param);
            paramIndex.increment();
            return this;
        }

    }

}