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
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Entity;
import org.seasar.doma.FetchType;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.EntityStreamHandler;
import org.seasar.doma.internal.jdbc.command.MapResultListHandler;
import org.seasar.doma.internal.jdbc.command.MapSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.MapStreamHandler;
import org.seasar.doma.internal.jdbc.command.OptionalEntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.OptionalMapSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.ScalarResultListHandler;
import org.seasar.doma.internal.jdbc.command.ScalarSingleResultHandler;
import org.seasar.doma.internal.jdbc.command.ScalarStreamHandler;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.internal.jdbc.scalar.ScalarException;
import org.seasar.doma.internal.jdbc.scalar.Scalars;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.NoResultException;
import org.seasar.doma.jdbc.NonSingleColumnException;
import org.seasar.doma.jdbc.NonUniqueResultException;
import org.seasar.doma.jdbc.ResultMappingException;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UnknownColumnException;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.EntityDescFactory;
import org.seasar.doma.jdbc.query.SqlSelectQuery;
import org.seasar.doma.message.Message;

/**
 * SELECT文を組み立て実行するクラスです。
 * <p>
 * このクラスはスレッドセーフではありません。
 * 
 * <h3>例</h3>
 * <h4>Java</h4>
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
 * Emp emp = builder.getEntitySingleResult(Emp.class);
 * </pre>
 * 
 * <h4>実行されるSQL</h4>
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
 * age &gt; 20
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
        this.query.setFetchType(FetchType.LAZY);
        this.query.setSqlLogType(SqlLogType.FORMATTED);
        this.paramIndex = new ParamIndex();
    }

    private SelectBuilder(Config config, BuildingHelper builder, SqlSelectQuery query,
            ParamIndex parameterIndex) {
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
        return appendParam(paramClass, param, false);
    }

    /**
     * パラメータのリストを追加します。
     * <p>
     * パラメータのリストの要素の型には、基本型とドメインクラスを指定できます。
     * 
     * @param <E>
     *            リストの要素の型
     * @param elementClass
     *            リストの要素のクラス
     * @param params
     *            パラメータのリスト
     * 
     * @return クエリビルダ
     * @throws DomaNullPointerException
     *             {@code elementClass} もしくは {@code params} が {@code null} の場合
     */
    public <E> SelectBuilder params(Class<E> elementClass, List<E> params) {
        if (elementClass == null) {
            throw new DomaNullPointerException("elementClass");
        }
        if (params == null) {
            throw new DomaNullPointerException("params");
        }
        return appendParams(elementClass, params, false);
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
    public <P> SelectBuilder literal(Class<P> paramClass, P param) {
        if (paramClass == null) {
            throw new DomaNullPointerException("paramClass");
        }
        return appendParam(paramClass, param, true);
    }

    /**
     * リテラルとしてパラメータのリストを追加します。
     * <p>
     * パラメータのリストの要素の型には、基本型とドメインクラスを指定できます。
     * 
     * @param <E>
     *            リストの要素の型
     * @param elementClass
     *            リストの要素のクラス
     * @param params
     *            パラメータのリスト
     * @return クエリビルダ
     * @throws DomaNullPointerException
     *             {@code elementClass} もしくは {@code params} が {@code null} の場合
     */
    public <E> SelectBuilder literals(Class<E> elementClass, List<E> params) {
        if (elementClass == null) {
            throw new DomaNullPointerException("elementClass");
        }
        if (params == null) {
            throw new DomaNullPointerException("params");
        }
        return appendParams(elementClass, params, true);
    }

    private <P> SelectBuilder appendParam(Class<P> paramClass, P param, boolean literal) {
        helper.appendParam(new Param(paramClass, param, paramIndex, literal));
        paramIndex.increment();
        return new SubsequentSelectBuilder(config, helper, query, paramIndex);
    }

    private <E> SelectBuilder appendParams(Class<E> elementClass, List<E> params, boolean literal) {
        SelectBuilder builder = this;
        int index = 0;
        for (E param : params) {
            builder = builder.appendParam(elementClass, param, literal).sql(", ");
            index++;
        }
        if (index == 0) {
            builder = builder.sql("null");
        } else {
            builder = builder.removeLast();
        }
        return builder;
    }

    /**
     * エンティティのインスタンスを1件返します。
     * <p>
     * 検索結果が存在しない場合は {@code null}を返しますが、
     * {@link SelectBuilder#ensureResult(boolean)} に {@code true} を設定することで、
     * {@code null}を返す代わりに{@link NoResultException} をスローできます。
     * 
     * @param <RESULT>
     *            エンティティの型
     * @param resultClass
     *            エンティティクラス
     * @return 検索結果
     * 
     * @throws DomaNullPointerException
     *             引数が{@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code resultClass} がエンティティクラスでない場合
     * @throws UnknownColumnException
     *             結果セットに含まれるカラムにマッピングされたプロパティが見つからなかった場合
     * @throws NoResultException
     *             {@link SelectBuilder#ensureResult(boolean)} に {@code true}
     *             を設定しており結果が存在しない場合
     * @throws ResultMappingException
     *             {@link SelectBuilder#ensureResultMapping(boolean)} に
     *             {@code true} を設定しており、マッピングされないエンティティプロパティが存在する場合
     * @throws NonUniqueResultException
     *             結果が2件以上返された場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     * @since 2.0.0
     */
    public <RESULT> RESULT getEntitySingleResult(Class<RESULT> resultClass) {
        if (resultClass == null) {
            throw new DomaNullPointerException("resultClass");
        }
        if (!resultClass.isAnnotationPresent(Entity.class)) {
            throw new DomaIllegalArgumentException("resultClass",
                    Message.DOMA2219.getMessage(resultClass));
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getEntitySingleResult");
        }
        EntityDesc<RESULT> entityDesc = EntityDescFactory.getEntityDesc(resultClass,
                config.getClassHelper());
        query.setEntityDesc(entityDesc);
        EntitySingleResultHandler<RESULT> handler = new EntitySingleResultHandler<>(entityDesc);
        return execute(handler);
    }

    /**
     * エンティティのインスタンスを {@link Optional} でラップして1件返します。
     * <p>
     * 検索結果が存在しない場合は {@code Optional}を返しますが、
     * {@link SelectBuilder#ensureResult(boolean)} に {@code true} を設定することで、
     * {@code null}を返す代わりに{@link NoResultException} をスローできます。
     * 
     * @param <RESULT>
     *            エンティティの型
     * @param resultClass
     *            エンティティクラス
     * @return 検索結果
     * @throws DomaNullPointerException
     *             引数が{@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code resultClass} がエンティティクラスでない場合
     * @throws UnknownColumnException
     *             結果セットに含まれるカラムにマッピングされたプロパティが見つからなかった場合
     * @throws NoResultException
     *             {@link SelectBuilder#ensureResult(boolean)} に {@code true}
     *             を設定しており結果が存在しない場合
     * @throws ResultMappingException
     *             {@link SelectBuilder#ensureResultMapping(boolean)} に
     *             {@code true} を設定しており、マッピングされないエンティティプロパティが存在する場合
     * @throws NonUniqueResultException
     *             結果が2件以上返された場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     * @since 2.0.0
     */
    public <RESULT> Optional<RESULT> getOptionalEntitySingleResult(Class<RESULT> resultClass) {
        if (resultClass == null) {
            throw new DomaNullPointerException("resultClass");
        }
        if (!resultClass.isAnnotationPresent(Entity.class)) {
            throw new DomaIllegalArgumentException("resultClass",
                    Message.DOMA2219.getMessage(resultClass));
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getOptionalEntitySingleResult");
        }
        EntityDesc<RESULT> entityDesc = EntityDescFactory.getEntityDesc(resultClass,
                config.getClassHelper());
        query.setEntityDesc(entityDesc);
        OptionalEntitySingleResultHandler<RESULT> handler = new OptionalEntitySingleResultHandler<>(
                entityDesc);
        return execute(handler);
    }

    /**
     * 基本型もしくはドメイン型のインスタンスを1件を返します。
     * <p>
     * 検索結果が存在しない場合は {@code null}を返しますが、
     * {@link SelectBuilder#ensureResult(boolean)} に {@code true} を設定することで、
     * {@code null}を返す代わりに{@link NoResultException} をスローできます。
     * 
     * @param <RESULT>
     *            基本型もしくはドメイン型
     * @param resultClass
     *            基本型もしくはドメイン型のクラス
     * @return 検索結果
     * @throws DomaNullPointerException
     *             引数が{@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code resultClass} が基本型もしくはドメイン型のクラスでない場合
     * @throws NonSingleColumnException
     *             結果セットに複数のカラムが含まれている場合
     * @throws NoResultException
     *             {@link SelectBuilder#ensureResult(boolean)} に {@code true}
     *             を設定しており結果が存在しない場合
     * @throws NonUniqueResultException
     *             結果が2件以上返された場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     * @since 2.0.0
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <RESULT> RESULT getScalarSingleResult(Class<RESULT> resultClass) {
        if (resultClass == null) {
            throw new DomaNullPointerException("resultClass");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getScalarSingleResult");
        }
        Supplier<Scalar<?, ?>> supplier = createScalarSupplier("resultClass", resultClass, false);
        ResultSetHandler<RESULT> handler = new ScalarSingleResultHandler(supplier);
        return execute(handler);
    }

    /**
     * 基本型もしくはドメイン型のインスタンスを {@link Optional} でラップして1件を返します。
     * <p>
     * 検索結果が存在しない場合は {@code Optional}を返しますが、
     * {@link SelectBuilder#ensureResult(boolean)} に {@code true} を設定することで、
     * {@code null}を返す代わりに{@link NoResultException} をスローできます。
     * 
     * @param <RESULT>
     *            基本型もしくはドメイン型
     * @param resultClass
     *            基本型もしくはドメイン型のクラス
     * @return 検索結果
     * @throws DomaNullPointerException
     *             引数が{@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code resultClass} が基本型もしくはドメイン型のクラスでない場合
     * @throws NonSingleColumnException
     *             結果セットに複数のカラムが含まれている場合
     * @throws NoResultException
     *             {@link SelectBuilder#ensureResult(boolean)} に {@code true}
     *             を設定しており結果が存在しない場合
     * @throws NonUniqueResultException
     *             結果が2件以上返された場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     * @since 2.0.0
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <RESULT> Optional<RESULT> getOptionalScalarSingleResult(Class<RESULT> resultClass) {
        if (resultClass == null) {
            throw new DomaNullPointerException("resultClass");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getOptionalScalarSingleResult");
        }
        Supplier<Scalar<?, ?>> supplier = createScalarSupplier("resultClass", resultClass, true);
        ResultSetHandler<Optional<RESULT>> handler = new ScalarSingleResultHandler(supplier);
        return execute(handler);
    }

    /**
     * {@code Map<String, Object>} のインスタンスを1件返します。
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
     * @since 2.0.0
     */
    public Map<String, Object> getMapSingleResult(MapKeyNamingType mapKeyNamingType) {
        if (mapKeyNamingType == null) {
            throw new DomaNullPointerException("mapKeyNamingType");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getMapSingleResult");
        }
        MapSingleResultHandler handler = new MapSingleResultHandler(mapKeyNamingType);
        return execute(handler);
    }

    /**
     * {@code Map<String, Object>} のインスタンスを {@link Optional} でラップして1件返します。
     * <p>
     * 検索結果が存在しない場合は {@code Optional}を返しますが、
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
     * @since 2.0.0
     */
    public Optional<Map<String, Object>> getOptionalMapSingleResult(
            MapKeyNamingType mapKeyNamingType) {
        if (mapKeyNamingType == null) {
            throw new DomaNullPointerException("mapKeyNamingType");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getOptionalMapSingleResult");
        }
        OptionalMapSingleResultHandler handler = new OptionalMapSingleResultHandler(
                mapKeyNamingType);
        return execute(handler);
    }

    /**
     * エンティティの複数件を返します。
     * <p>
     * 検索結果が存在しない場合は空のリストを返します。
     * 
     * @param <ELEMENT>
     *            エンティティ型
     * @param elementClass
     *            エンティティ型のクラス
     * @return 検索結果
     * 
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code elementClass} がエンティティクラスでない場合
     * @throws UnknownColumnException
     *             結果セットに含まれるカラムにマッピングされたプロパティが見つからなかった場合
     * @throws NoResultException
     *             {@link SelectBuilder#ensureResult(boolean)} に {@code true}
     *             を設定しており結果が存在しない場合
     * @throws ResultMappingException
     *             {@link SelectBuilder#ensureResultMapping(boolean)} に
     *             {@code true} を設定しており、マッピングされないエンティティプロパティが存在する場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     * @since 2.0.0
     */
    public <ELEMENT> List<ELEMENT> getEntityResultList(Class<ELEMENT> elementClass) {
        if (elementClass == null) {
            throw new DomaNullPointerException("elementClass");
        }
        if (!elementClass.isAnnotationPresent(Entity.class)) {
            throw new DomaIllegalArgumentException("elementClass",
                    Message.DOMA2219.getMessage(elementClass));
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getEntityResultList");
        }
        EntityDesc<ELEMENT> entityDesc = EntityDescFactory.getEntityDesc(elementClass,
                config.getClassHelper());
        query.setEntityDesc(entityDesc);
        ResultSetHandler<List<ELEMENT>> handler = new EntityResultListHandler<ELEMENT>(entityDesc);
        return execute(handler);
    }

    /**
     * 基本型もしくはドメイン型のインスタンスを複数件返します。
     * <p>
     * 検索結果が存在しない場合は空のリストを返します。
     * 
     * @param <ELEMENT>
     *            基本型もしくはドメイン型
     * @param elementClass
     *            基本型もしくはドメイン型のクラス
     * @return 検索結果
     * 
     * @throws DomaNullPointerException
     *             引数が{@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code resultClass} が基本型もしくはドメイン型のクラスでない場合
     * @throws NonSingleColumnException
     *             結果セットに複数のカラムが含まれている場合
     * @throws NoResultException
     *             {@link SelectBuilder#ensureResult(boolean)} に {@code true}
     *             を設定しており結果が存在しない場合
     * @throws NonUniqueResultException
     *             結果が2件以上返された場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     * @since 2.0.0
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <ELEMENT> List<ELEMENT> getScalarResultList(Class<ELEMENT> elementClass) {
        if (elementClass == null) {
            throw new DomaNullPointerException("elementClass");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getScalarResultList");
        }
        Supplier<Scalar<?, ?>> supplier = createScalarSupplier("elementClass", elementClass, false);
        ResultSetHandler<List<ELEMENT>> handler = new ScalarResultListHandler(supplier);
        return execute(handler);
    }

    /**
     * 基本型もしくはドメイン型のインスタンスを {@link Optional} でラップして複数件返します。
     * <p>
     * 検索結果が存在しない場合は空のリストを返します。
     * 
     * @param <ELEMENT>
     *            基本型もしくはドメイン型
     * @param elementClass
     *            基本型もしくはドメイン型のクラス
     * @return 検索結果
     * 
     * @throws DomaNullPointerException
     *             引数が{@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code resultClass} が基本型もしくはドメイン型のクラスでない場合
     * @throws NonSingleColumnException
     *             結果セットに複数のカラムが含まれている場合
     * @throws NoResultException
     *             {@link SelectBuilder#ensureResult(boolean)} に {@code true}
     *             を設定しており結果が存在しない場合
     * @throws NonUniqueResultException
     *             結果が2件以上返された場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     * @since 2.0.0
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <ELEMENT> List<Optional<ELEMENT>> getOptionalScalarResultList(
            Class<ELEMENT> elementClass) {
        if (elementClass == null) {
            throw new DomaNullPointerException("elementClass");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getOptionalScalarResultList");
        }
        Supplier<Scalar<?, ?>> supplier = createScalarSupplier("elementClass", elementClass, true);
        ResultSetHandler<List<Optional<ELEMENT>>> handler = new ScalarResultListHandler(supplier);
        return execute(handler);
    }

    /**
     * {@code List<Map<String, Object>>} のインスタンスを複数件返します。
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
     * @since 2.0.0
     */
    public List<Map<String, Object>> getMapResultList(MapKeyNamingType mapKeyNamingType) {
        if (mapKeyNamingType == null) {
            throw new DomaNullPointerException("mapKeyNamingType");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("getMapResultList");
        }
        MapResultListHandler handler = new MapResultListHandler(mapKeyNamingType);
        return execute(handler);
    }

    /**
     * エンティティのストリームを返します。
     * <p>
     * ストリームはアプリケーションでクローズしなければいけません。
     * 
     * @param <TARGET>
     *            エンティティ型
     * @param targetClass
     *            エンティティ型のクラス
     * @return エンティティのストリーム
     * @throws DomaNullPointerException
     *             引数が{@code null} の場合
     * @throws DomaIllegalArgumentException
     *             処理対象のクラスがエンティティ型でない場合
     * @throws JdbcException
     *             JDBCに関する例外が発生した場合
     * @since 2.7.0
     */
    public <TARGET> Stream<TARGET> streamEntity(Class<TARGET> targetClass) {
        if (targetClass == null) {
            throw new DomaNullPointerException("targetClass");
        }
        if (!targetClass.isAnnotationPresent(Entity.class)) {
            throw new DomaIllegalArgumentException("targetClass",
                    Message.DOMA2219.getMessage(targetClass));
        }
        query.setResultStream(true);
        return streamEntityInternal(targetClass, Function.identity());
    }

    /**
     * エンティティのインスタンスをストリームで処理します。
     * 
     * @param <RESULT>
     *            戻り値の型
     * @param <TARGET>
     *            エンティティ型
     * @param targetClass
     *            エンティティ型のクラス
     * @param mapper
     *            マッパー
     * @return 任意の実行結果
     * @throws DomaNullPointerException
     *             引数のいずれかが{@code null} の場合
     * @throws DomaIllegalArgumentException
     *             処理対象のクラスがエンティティ型でない場合
     * @throws UnknownColumnException
     *             結果セットに含まれるカラムにマッピングされたプロパティが見つからなかった場合
     * @throws NoResultException
     *             {@link SelectBuilder#ensureResult(boolean)} に {@code true}
     *             を設定しており結果が存在しない場合
     * @throws ResultMappingException
     *             {@link SelectBuilder#ensureResultMapping(boolean)} に
     *             {@code true} を設定しており、マッピングされないエンティティプロパティが存在する場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     * @since 2.0.0
     */
    public <TARGET, RESULT> RESULT streamEntity(Class<TARGET> targetClass,
            Function<Stream<TARGET>, RESULT> mapper) {
        if (targetClass == null) {
            throw new DomaNullPointerException("targetClass");
        }
        if (!targetClass.isAnnotationPresent(Entity.class)) {
            throw new DomaIllegalArgumentException("targetClass",
                    Message.DOMA2219.getMessage(targetClass));
        }
        if (mapper == null) {
            throw new DomaNullPointerException("mapper");
        }
        return streamEntityInternal(targetClass, mapper);
    }

    protected <TARGET, RESULT> RESULT streamEntityInternal(Class<TARGET> targetClass,
            Function<Stream<TARGET>, RESULT> mapper) {
        if (query.getMethodName() == null) {
            query.setCallerMethodName("streamEntity");
        }
        EntityDesc<TARGET> entityDesc = EntityDescFactory.getEntityDesc(targetClass,
                config.getClassHelper());
        query.setEntityDesc(entityDesc);
        ResultSetHandler<RESULT> handler = new EntityStreamHandler<>(entityDesc, mapper);
        return execute(handler);
    }

    /**
     * 基本型もしくはドメイン型のストリームを返します。
     * <p>
     * ストリームはアプリケーションでクローズしなければいけません。
     * 
     * @param <TARGET>
     *            基本型もしくはドメイン型
     * @param targetClass
     *            基本型もしくはドメイン型のクラス
     * @return 基本型もしくはドメイン型のストリーム
     * @throws DomaNullPointerException
     *             引数のいずれかが{@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code targetClass} が基本型もしくはドメイン型のクラスでない場合
     * @throws JdbcException
     *             JDBCに関する例外が発生した場合
     * @since 2.7.0
     */
    public <TARGET> Stream<TARGET> streamScalar(Class<TARGET> targetClass) {
        if (targetClass == null) {
            throw new DomaNullPointerException("targetClass");
        }
        query.setResultStream(true);
        return streamScalarInternal(targetClass, Function.identity());
    }

    /**
     * 結果セットを基本型もしくはドメイン型のインスタンスをストリームで処理します。
     * 
     * @param <RESULT>
     *            戻り値の型
     * @param <TARGET>
     *            基本型もしくはドメイン型
     * @param targetClass
     *            基本型もしくはドメイン型のクラス
     * @param mapper
     *            マッパー
     * @return 任意の実行結果
     * @throws DomaNullPointerException
     *             引数のいずれかが{@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code targetClass} が基本型もしくはドメイン型のクラスでない場合
     * @throws NonSingleColumnException
     *             結果セットに複数のカラムが含まれている場合
     * @throws NoResultException
     *             {@link SelectBuilder#ensureResult(boolean)} に {@code true}
     *             を設定しており結果が存在しない場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     * @since 2.0.0
     */
    public <RESULT, TARGET> RESULT streamScalar(Class<TARGET> targetClass,
            Function<Stream<TARGET>, RESULT> mapper) {
        if (targetClass == null) {
            throw new DomaNullPointerException("targetClass");
        }
        if (mapper == null) {
            throw new DomaNullPointerException("mapper");
        }
        return streamScalarInternal(targetClass, mapper);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <RESULT, TARGET> RESULT streamScalarInternal(Class<TARGET> targetClass,
            Function<Stream<TARGET>, RESULT> mapper) {
        if (query.getMethodName() == null) {
            query.setCallerMethodName("streamScalar");
        }
        Supplier<Scalar<?, ?>> supplier = createScalarSupplier("targetClass", targetClass, false);
        ResultSetHandler<RESULT> handler = new ScalarStreamHandler(supplier, mapper);
        return execute(handler);
    }

    /**
     * 基本型もしくはドメイン型をラップした {@code Optional} のストリームを返します。
     * <p>
     * ストリームはアプリケーションでクローズしなければいけません。
     * 
     * @param <TARGET>
     *            基本型もしくはドメイン型
     * @param targetClass
     *            基本型もしくはドメイン型のクラス
     * @return {@code Optional} のストリーム
     * @throws DomaNullPointerException
     *             引数のいずれかが{@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code targetClass} が基本型もしくはドメイン型のクラスでない場合
     * @throws JdbcException
     *             JDBCに関する例外が発生した場合
     * @since 2.7.0
     */
    public <TARGET> Stream<Optional<TARGET>> streamOptionalScalar(Class<TARGET> targetClass) {
        if (targetClass == null) {
            throw new DomaNullPointerException("targetClass");
        }
        query.setResultStream(true);
        return streamOptionalScalarInternal(targetClass, Function.identity());
    }

    /**
     * 結果セットを基本型もしくはドメイン型のインスタンスを {@link Optional} でラップしてストリームで処理します。
     * <p>
     * ストリームはアプリケーションでクローズしなければいけません。
     * 
     * @param <RESULT>
     *            戻り値の型
     * @param <TARGET>
     *            基本型もしくはドメイン型
     * @param targetClass
     *            基本型もしくはドメイン型のクラス
     * @param mapper
     *            マッパー
     * @return 任意の実行結果
     * @throws DomaNullPointerException
     *             引数のいずれかが{@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code targetClass} が基本型もしくはドメイン型のクラスでない場合
     * @throws NonSingleColumnException
     *             結果セットに複数のカラムが含まれている場合
     * @throws NoResultException
     *             {@link SelectBuilder#ensureResult(boolean)} に {@code true}
     *             を設定しており結果が存在しない場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     * @since 2.0.0
     */
    public <RESULT, TARGET> RESULT streamOptionalScalar(Class<TARGET> targetClass,
            Function<Stream<Optional<TARGET>>, RESULT> mapper) {
        if (targetClass == null) {
            throw new DomaNullPointerException("targetClass");
        }
        if (mapper == null) {
            throw new DomaNullPointerException("mapper");
        }
        return streamOptionalScalarInternal(targetClass, mapper);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <RESULT, TARGET> RESULT streamOptionalScalarInternal(Class<TARGET> targetClass,
            Function<Stream<Optional<TARGET>>, RESULT> mapper) {
        if (query.getMethodName() == null) {
            query.setCallerMethodName("streamOptionalScalar");
        }
        Supplier<Scalar<?, ?>> supplier = createScalarSupplier("targetClass", targetClass, true);
        ResultSetHandler<RESULT> handler = new ScalarStreamHandler(supplier, mapper);
        return execute(handler);
    }

    /**
     * {@code Map<String, Object>} のストリームを返します。
     * <p>
     * ストリームはアプリケーションでクローズしなければいけません。
     * 
     * @param mapKeyNamingType
     *            マップのキーのネーミング規約
     * @return {@code Map<String, Object>} のストリーム
     * @throws DomaNullPointerException
     *             引数のいずれかが{@code null} の場合
     * @throws JdbcException
     *             JDBCに関する例外が発生した場合
     * @since 2.7.0
     */
    public Stream<Map<String, Object>> streamMap(MapKeyNamingType mapKeyNamingType) {
        if (mapKeyNamingType == null) {
            throw new DomaNullPointerException("mapKeyNamingType");
        }
        query.setResultStream(true);
        return streamMapInternal(mapKeyNamingType, Function.identity());
    }

    /**
     * {@code Map<String, Object>} のインスタンスをストリームで処理します。
     * 
     * @param <RESULT>
     *            戻り値の型
     * @param mapKeyNamingType
     *            マップのキーのネーミング規約
     * @param mapper
     *            マッパー
     * @return 任意の実行結果
     * @throws DomaNullPointerException
     *             引数のいずれかが{@code null} の場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     * @since 2.0.0
     */
    public <RESULT> RESULT streamMap(MapKeyNamingType mapKeyNamingType,
            Function<Stream<Map<String, Object>>, RESULT> mapper) {
        if (mapKeyNamingType == null) {
            throw new DomaNullPointerException("mapKeyNamingType");
        }
        if (mapper == null) {
            throw new DomaNullPointerException("mapper");
        }
        return streamMapInternal(mapKeyNamingType, mapper);
    }

    protected <RESULT> RESULT streamMapInternal(MapKeyNamingType mapKeyNamingType,
            Function<Stream<Map<String, Object>>, RESULT> mapper) {
        if (query.getMethodName() == null) {
            query.setCallerMethodName("streamMap");
        }
        MapStreamHandler<RESULT> handler = new MapStreamHandler<>(mapKeyNamingType, mapper);
        return execute(handler);
    }

    private <RESULT> RESULT execute(ResultSetHandler<RESULT> resultSetHandler) {
        prepare();
        SelectCommand<RESULT> command = new SelectCommand<RESULT>(query, resultSetHandler);
        RESULT result = command.execute();
        query.complete();
        return result;
    }

    private void prepare() {
        query.clearParameters();
        for (Param p : helper.getParams()) {
            query.addParameter(p.name, p.paramClass, p.param);
        }
        query.setSqlNode(helper.getSqlNode());
        query.prepare();
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
     * フェッチのタイプを設定します。
     * 
     * @param fetchType
     *            フェッチのタイプ
     */
    public void fetch(FetchType fetchType) {
        query.setFetchType(fetchType);
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
     * SQLのログの出力形式を設定します。
     * 
     * @param sqlLogType
     *            SQLのログの出力形式
     */
    public void sqlLogType(SqlLogType sqlLogType) {
        if (sqlLogType == null) {
            throw new DomaNullPointerException("sqlLogType");
        }
        query.setSqlLogType(sqlLogType);
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
        prepare();
        return query.getSql();
    }

    private Supplier<Scalar<?, ?>> createScalarSupplier(String parameterName, Class<?> clazz,
            boolean optional) {
        try {
            return Scalars.wrap(null, clazz, optional, config.getClassHelper());
        } catch (ScalarException e) {
            throw new DomaIllegalArgumentException(parameterName,
                    Message.DOMA2204.getMessage(clazz, e));
        }
    }

    private static class SubsequentSelectBuilder extends SelectBuilder {

        private SubsequentSelectBuilder(Config config, BuildingHelper builder, SqlSelectQuery query,
                ParamIndex paramIndex) {
            super(config, builder, query, paramIndex);
        }

        @Override
        public SelectBuilder sql(String fragment) {
            super.helper.appendSql(fragment);
            return this;
        }

    }
}
