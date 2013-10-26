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
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Entity;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.jdbc.command.EntityIterationHandler;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.MapIterationHandler;
import org.seasar.doma.internal.jdbc.command.MapResultListHandler;
import org.seasar.doma.internal.jdbc.command.MapSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.ScalarIterationHandler;
import org.seasar.doma.internal.jdbc.command.ScalarResultListHandler;
import org.seasar.doma.internal.jdbc.command.ScalarSingleResultHandler;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.internal.jdbc.scalar.ScalarException;
import org.seasar.doma.internal.jdbc.scalar.Scalars;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.MappedPropertyNotFoundException;
import org.seasar.doma.jdbc.NoResultException;
import org.seasar.doma.jdbc.NonSingleColumnException;
import org.seasar.doma.jdbc.NonUniqueResultException;
import org.seasar.doma.jdbc.ResultMappingException;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.EntityTypeFactory;
import org.seasar.doma.jdbc.query.SqlSelectQuery;
import org.seasar.doma.message.Message;

/**
 * SELECT文を組み立て実行するクラスです。
 * <p>
 * このクラスはスレッドセーフではありません。
 * 
 * <h4>例</h4>
 * <h5>Java</h5>
 * 
 * <pre>
 * SelectBuilder builder = SelectBuilder.newInstance(config);
 * builder.sql(&quot;select&quot;);
 * builder.sql(&quot;id&quot;).sql(&quot;,&quot;);
 * builder.sql(&quot;name&quot;).sql(&quot;,&quot;);
 * builder.sql(&quot;salary&quot;);
 * builder.sql(&quot;from Emp&quot;);
 * builder.sql(&quot;where&quot;);
 * builder.sql(&quot;name like &quot;).param(String.class, &quot;S%&quot;);
 * builder.sql(&quot;and&quot;);
 * builder.sql(&quot;age &gt; &quot;).param(int.class, 20);
 * Emp emp = builder.getSingleResult(Emp.class);
 * </pre>
 * 
 * <h5>実行されるSQL</h5>
 * 
 * <pre>
 * select
 * id,
 * name,
 * salary
 * from Emp
 * where
 * name like 'S%'
 * and
 * age > 20
 * </pre>
 * 
 * @author taedium
 * @since 1.8.0
 */
public class SelectBuilder {

    private final Config config;

    private final BuildingHelper helper;

    private final SqlSelectQuery query;

    private final ParamIndex paramIndex;

    private SelectBuilder(Config config) {
        this.config = config;
        this.helper = new BuildingHelper();
        this.query = new SqlSelectQuery();
        this.query.setConfig(config);
        this.query.setCallerClassName(getClass().getName());
        this.paramIndex = new ParamIndex();
    }

    private SelectBuilder(Config config, BuildingHelper builder,
            SqlSelectQuery query, ParamIndex parameterIndex) {
        this.config = config;
        this.helper = builder;
        this.query = query;
        this.paramIndex = parameterIndex;
    }

    /**
     * ファクトリメソッドです。
     * 
     * @param config
     *            設定
     * @return SELECT文を組み立てるビルダー
     * @throws DomaNullPointerException
     *             引数が{@code null} の場合
     */
    public static SelectBuilder newInstance(Config config) {
        if (config == null) {
            throw new DomaNullPointerException("config");
        }
        return new SelectBuilder(config);
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
    public SelectBuilder sql(String sql) {
        if (sql == null) {
            throw new DomaNullPointerException("sql");
        }
        helper.appendSqlWithLineSeparator(sql);
        return new SubsequentSelectBuilder(config, helper, query, paramIndex);
    }

    /**
     * 最後に追加したSQLもしくはパラメータを削除します。
     * 
     * @return このインスタンス
     */
    public SelectBuilder removeLast() {
        helper.removeLast();
        return new SubsequentSelectBuilder(config, helper, query, paramIndex);
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
     *             {@code paramClass} が {@code null} の場合
     */
    public <P> SelectBuilder param(Class<P> paramClass, P param) {
        if (paramClass == null) {
            throw new DomaNullPointerException("paramClass");
        }
        helper.appendParam(new Param(paramClass, param, paramIndex));
        paramIndex.increment();
        return new SubsequentSelectBuilder(config, helper, query, paramIndex);
    }

    /**
     * 1件を返します。
     * <p>
     * 戻り値の型に指定できるのは、エンティティクラス、ドメインクラス、基本型のいずれかです。
     * <p>
     * 検索結果が存在しない場合は {@code null}を返しますが、
     * {@link SelectBuilder#ensureResult(boolean)} に {@code true} を設定することで、
     * {@code null}を返す代わりに{@link NoResultException} をスローできます。
     * 
     * @param <R>
     *            戻り値の型
     * @param resultClass
     *            戻り値のクラス
     * @return 検索結果
     * 
     * @throws DomaNullPointerException
     *             引数が{@code null} の場合
     * @throws DomaIllegalArgumentException
     *             戻り値のクラスがエンティティクラス、ドメインクラス、基本型のいずれでもない場合
     * @throws MappedPropertyNotFoundException
     *             戻り値の型がエンティティクラスで、結果セットに含まれるカラムにマッピングされたプロパティが見つからなかった場合
     * @throws NonSingleColumnException
     *             戻り値の型が基本型やドメインクラスで、かつ結果セットに複数のカラムが含まれている場合
     * @throws NoResultException
     *             {@link SelectBuilder#ensureResult(boolean)} に {@code true}
     *             を設定しており結果が存在しない場合
     * @throws ResultMappingException
     *             {@link SelectBuilder#ensureResultMapping(boolean)} に
     *             {@code true} を設定しており戻り値の型がエンティティクラスやエンティティクラスを要素とする
     *             {@link List} の場合で、マッピングされないエンティティプロパティが存在する場合
     * @throws NonUniqueResultException
     *             結果が2件以上返された場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     */
    public <R> R getEntitySingleResult(Class<R> resultClass) {
        if (resultClass == null) {
            throw new DomaNullPointerException("resultClass");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getSingleResult");
        }
        if (!resultClass.isAnnotationPresent(Entity.class)) {
            // TODO
            throw new RuntimeException("resultClass");
        }
        EntityType<R> entityType = EntityTypeFactory.getEntityType(resultClass,
                config.getClassHelper());
        ResultSetHandler<R> handler = new EntitySingleResultHandler<R>(
                entityType);
        return execute(handler);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <R> R getScalarSingleResult(Class<R> resultClass) {
        if (resultClass == null) {
            throw new DomaNullPointerException("resultClass");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getSingleResult");
        }
        Supplier<Scalar<?, ?>> supplier = createScalarSupplier("resultClass",
                resultClass);
        ResultSetHandler<R> handler = new ScalarSingleResultHandler(supplier);
        return execute(handler);
    }

    /**
     * {@code Map<String, Object>} として1件を返します。
     * <p>
     * 検索結果が存在しない場合は {@code null}を返しますが、
     * {@link SelectBuilder#ensureResult(boolean)} に {@code true} を設定することで、
     * {@code null}を返す代わりに {@link NoResultException} をスローできます。
     * 
     * @param mapKeyNamingType
     *            マップのキーのネーミング規約
     * @return 検索結果
     * 
     * @throws DomaNullPointerException
     *             引数が{@code null} の場合
     * @throws NoResultException
     *             {@link SelectBuilder#ensureResult(boolean)} に {@code true}
     *             を設定しており結果が存在しない場合
     * @throws NonUniqueResultException
     *             結果が2件以上返された場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     * @since 1.17.0
     */
    public Map<String, Object> getMapSingleResult(
            MapKeyNamingType mapKeyNamingType) {
        if (mapKeyNamingType == null) {
            throw new DomaNullPointerException("mapKeyNamingType");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getSingleResult");
        }
        MapSingleResultHandler singleResultHandler = new MapSingleResultHandler(
                mapKeyNamingType);
        return execute(singleResultHandler);
    }

    /**
     * 複数件を返します。
     * <p>
     * 戻り値の型に指定できるのは、エンティティクラス、ドメインクラス、基本型のいずれかです。
     * <p>
     * 検索結果が存在しない場合は空のリストを返します。
     * 
     * @param <R>
     *            戻り値のリストの要素の型
     * @param resultClass
     *            戻り値のリストの要素のクラス
     * @return 検索結果
     * 
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @throws DomaIllegalArgumentException
     *             戻り値のリストの要素のクラスがエンティティクラス、ドメインクラス、基本型のいずれでもない場合
     * @throws MappedPropertyNotFoundException
     *             戻り値のリストの要素の型がエンティティクラスで、
     *             結果セットに含まれるカラムにマッピングされたプロパティが見つからなかった場合
     * @throws NonSingleColumnException
     *             戻り値のリストの要素の型が基本型やドメインクラスで、かつ結果セットに複数のカラムが含まれている場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     */
    public <R> List<R> getEntityResultList(Class<R> resultClass) {
        if (resultClass == null) {
            throw new DomaNullPointerException("resultClass");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getResultList");
        }
        if (!resultClass.isAnnotationPresent(Entity.class)) {
            // TODO
            throw new RuntimeException();
        }
        EntityType<R> entityType = EntityTypeFactory.getEntityType(resultClass,
                config.getClassHelper());
        ResultSetHandler<List<R>> handler = new EntityResultListHandler<R>(
                entityType);
        return execute(handler);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <R> List<R> getScalarResultList(Class<R> resultClass) {
        if (resultClass == null) {
            throw new DomaNullPointerException("resultClass");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getResultList");
        }
        Supplier<Scalar<?, ?>> supplier = createScalarSupplier("resultClass",
                resultClass);
        ResultSetHandler<List<R>> handler = new ScalarResultListHandler(
                supplier);
        return execute(handler);
    }

    /**
     * {@code List<Map<String, Object>>} として 複数件を返します。
     * <p>
     * 検索結果が存在しない場合は空のリストを返します。
     * 
     * @param mapKeyNamingType
     *            マップのキーのネーミング規約
     * @return 検索結果
     * 
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     * @since 1.17.0
     */
    public List<Map<String, Object>> getMapResultList(
            MapKeyNamingType mapKeyNamingType) {
        if (mapKeyNamingType == null) {
            throw new DomaNullPointerException("mapKeyNamingType");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getResultList");
        }
        MapResultListHandler resultListHandler = new MapResultListHandler(
                mapKeyNamingType);
        return execute(resultListHandler);
    }

    /**
     * 処理対象のオブジェクト群を順に1件ずつ処理します。
     * <p>
     * 処理対象の型に指定できるのは、エンティティクラス、ドメインクラス、基本型のいずれかです。
     * 
     * @param <R>
     *            戻り値の型
     * @param <T>
     *            処理対象の型。すなわち、基本型、ドメインクラス、もしくはエンティティクラス
     * @param targetClass
     *            処理対象のクラス
     * @param iterationCallback
     *            コールバック
     * @return 任意の実行結果
     * @throws DomaNullPointerException
     *             引数のいずれかが{@code null} の場合
     * @throws DomaIllegalArgumentException
     *             処理対象のクラスがエンティティクラス、ドメインクラス、基本型のいずれでもない場合
     * @throws MappedPropertyNotFoundException
     *             処理対象の型がエンティティクラスで、結果セットに含まれるカラムにマッピングされたプロパティが見つからなかった場合
     * @throws NonSingleColumnException
     *             処理対象の型が基本型やドメインクラスで、かつ結果セットに複数のカラムが含まれている場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     */
    public <R, T> R iterateAsEntity(Class<T> targetClass,
            IterationCallback<R, T> iterationCallback) {
        if (targetClass == null) {
            throw new DomaNullPointerException("targetClass");
        }
        if (iterationCallback == null) {
            throw new DomaNullPointerException("iterationCallback");
        }
        if (!targetClass.isAnnotationPresent(Entity.class)) {
            // TODO
            throw new RuntimeException();
        }

        if (query.getMethodName() == null) {
            query.setCallerMethodName("iterate");
        }
        EntityType<T> entityType = EntityTypeFactory.getEntityType(targetClass,
                config.getClassHelper());
        ResultSetHandler<R> iterationHandler = new EntityIterationHandler<R, T>(
                entityType, iterationCallback);
        return execute(iterationHandler);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <R, T> R iterateAsScalar(Class<T> targetClass,
            IterationCallback<R, T> iterationCallback) {
        if (targetClass == null) {
            throw new DomaNullPointerException("targetClass");
        }
        if (iterationCallback == null) {
            throw new DomaNullPointerException("iterationCallback");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("iterate");
        }
        Supplier<Scalar<?, ?>> supplier = createScalarSupplier("resultClass",
                targetClass);
        ResultSetHandler<R> iterationHandler = new ScalarIterationHandler(
                supplier, iterationCallback);
        return execute(iterationHandler);
    }

    /**
     * 処理対象のオブジェクト群を {@code Map<String, Object>} として順に1件ずつ処理します。
     * 
     * @param <R>
     *            戻り値の型
     * @param mapKeyNamingType
     *            マップのキーのネーミング規約
     * @param iterationCallback
     *            コールバック
     * @return 任意の実行結果
     * @throws DomaNullPointerException
     *             引数のいずれかが{@code null} の場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     * @since 1.17.0
     */
    public <R> R iterateAsMap(MapKeyNamingType mapKeyNamingType,
            IterationCallback<R, Map<String, Object>> iterationCallback) {
        if (mapKeyNamingType == null) {
            throw new DomaNullPointerException("mapKeyNamingType");
        }
        if (iterationCallback == null) {
            throw new DomaNullPointerException("iterationCallback");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("iterate");
        }
        MapIterationHandler<R> iterationHandler = new MapIterationHandler<R>(
                mapKeyNamingType, iterationCallback);
        return execute(iterationHandler);
    }

    private <R> R execute(ResultSetHandler<R> resultSetHandler) {
        for (Param p : helper.getParams()) {
            query.addParameter(p.name, p.paramClass, p.param);
        }
        query.setSqlNode(helper.getSqlNode());
        query.prepare();
        SelectCommand<R> command = new SelectCommand<R>(query, resultSetHandler);
        R result = command.execute();
        query.complete();
        return result;
    }

    /**
     * 結果が少なくとも1件以上存在することを保証します。
     * 
     * @param ensureResult
     *            結果が少なくとも1件以上存在することを保証する場合 {@code true}
     */
    public void ensureResult(boolean ensureResult) {
        query.setResultEnsured(ensureResult);
    }

    /**
     * 結果のエンティティのすべてのプロパティが結果セットのカラムにマッピングされることを保証します。
     * 
     * @param ensureResultMapping
     *            結果のエンティティのすべてのプロパティが結果セットのカラムにマッピングされることを保証する場合 {@code true}
     * @since 1.34.0
     */
    public void ensureResultMapping(boolean ensureResultMapping) {
        query.setResultMappingEnsured(ensureResultMapping);
    }

    /**
     * フェッチサイズを設定します。
     * <p>
     * 指定しない場合、 {@link Config#getFetchSize()} が使用されます。
     * 
     * @param fetchSize
     *            フェッチサイズ
     * @see Statement#setFetchSize(int)
     */
    public void fetchSize(int fetchSize) {
        query.setFetchSize(fetchSize);
    }

    /**
     * 最大行数の制限値を設定します。
     * <p>
     * 指定しない場合、 {@link Config#getMaxRows()} が使用されます。
     * 
     * @param maxRows
     *            最大行数の制限値
     * @see Statement#setMaxRows(int)
     */
    public void maxRows(int maxRows) {
        query.setMaxRows(maxRows);
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
     * 指定しない場合このSQLを生成するメソッド（{@link #getSingleResult(Class)},
     * {@link #getResultList(Class)},{@link #iterate(Class, IterationCallback)},
     * {@link #getSql()}）の名前が使用されます。
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
     * 検索系SQLを実行する際のオプションを設定します。
     * 
     * @param options
     *            検索系SQLを実行する際のオプション
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     */
    public void options(SelectOptions options) {
        if (options == null) {
            throw new DomaNullPointerException("options");
        }
        query.setOptions(options);
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

    private <T> Supplier<Scalar<?, ?>> createScalarSupplier(
            String parameterName, Class<T> clazz) {
        try {
            return Scalars.wrap(null, clazz, false, config.getClassHelper());
        } catch (ScalarException e) {
            throw new DomaIllegalArgumentException(parameterName,
                    Message.DOMA2204.getMessage(clazz, e));
        }
    }

    private static class SubsequentSelectBuilder extends SelectBuilder {

        private SubsequentSelectBuilder(Config config, BuildingHelper builder,
                SqlSelectQuery query, ParamIndex paramIndex) {
            super(config, builder, query, paramIndex);
        }

        @Override
        public SelectBuilder sql(String fragment) {
            super.helper.appendSql(fragment);
            return this;
        }

    }
}
