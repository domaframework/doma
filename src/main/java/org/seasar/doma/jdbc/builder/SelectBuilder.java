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
 * A builder for an SQL SELECT statement.
 * <p>
 * This is not thread safe.
 * 
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
 * <h4>built SQL</h4>
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
     * Creates a new instance.
     * 
     * @param config
     *            the configuration
     * @return a builder
     * @throws DomaNullPointerException
     *             if {@code config} is {@code null}
     */
    public static SelectBuilder newInstance(Config config) {
        if (config == null) {
            throw new DomaNullPointerException("config");
        }
        return new SelectBuilder(config);
    }

    /**
     * Appends an SQL fragment.
     * 
     * @param sql
     *            the SQL fragment
     * @return a builder
     * @throws DomaNullPointerException
     *             if {@code sql} is {@code null}
     */
    public SelectBuilder sql(String sql) {
        if (sql == null) {
            throw new DomaNullPointerException("sql");
        }
        helper.appendSqlWithLineSeparator(sql);
        return new SubsequentSelectBuilder(config, helper, query, paramIndex);
    }

    /**
     * Removes the last SQL fragment or parameter.
     * 
     * @return a builder
     */
    public SelectBuilder removeLast() {
        helper.removeLast();
        return new SubsequentSelectBuilder(config, helper, query, paramIndex);
    }

    /**
     * Appends a parameter.
     * <p>
     * The parameter type must be one of basic types or holder types.
     * 
     * @param <P>
     *            the parameter type
     * @param paramClass
     *            the parameter class
     * @param param
     *            the parameter
     * @return a builder
     * @throws DomaNullPointerException
     *             if {@code paramClass} is {@code null}
     */
    public <P> SelectBuilder param(Class<P> paramClass, P param) {
        if (paramClass == null) {
            throw new DomaNullPointerException("paramClass");
        }
        return appendParam(paramClass, param, false);
    }

    /**
     * Appends a parameter list.
     * <p>
     * The element type of the list must be one of basic types or holder types.
     * 
     * @param <E>
     *            the element type of the list
     * @param elementClass
     *            the element class of the list
     * @param params
     *            the parameter list
     * @return a builder
     * @throws DomaNullPointerException
     *             if {@code elementClass} or {@code params} is {@code null}
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
     * Appends a parameter as literal.
     * <p>
     * The parameter type must be one of basic types or holder types.
     * 
     * @param <P>
     *            the parameter type
     * @param paramClass
     *            the parameter class
     * @param param
     *            the parameter
     * @return a builder
     * @throws DomaNullPointerException
     *             if {@code paramClass} is {@code null}
     */
    public <P> SelectBuilder literal(Class<P> paramClass, P param) {
        if (paramClass == null) {
            throw new DomaNullPointerException("paramClass");
        }
        return appendParam(paramClass, param, true);
    }

    /**
     * Appends a parameter list as literal.
     * <p>
     * The element type of the list must be one of basic types or holder types.
     * 
     * @param <E>
     *            the element type of the list
     * @param elementClass
     *            the element class of the list
     * @param params
     *            the parameter list
     * @return a builder
     * @throws DomaNullPointerException
     *             if {@code elementClass} or {@code params} is {@code null}
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
     * Executes an SQL SELECT statement and returns a single entity result.
     * 
     * @param <RESULT>
     *            the entity type
     * @param resultClass
     *            the entity class
     * @return a single entity or {@code null} if the result is none
     * @throws DomaNullPointerException
     *             if {@code resultClass} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code resultClass} is not entity class
     * @throws UnknownColumnException
     *             if there is the column that is unknown to the entity
     * @throws NoResultException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResult(boolean)} and the result is
     *             none
     * @throws ResultMappingException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResultMapping(boolean)} and all
     *             properties in the entity are not mapped to columns in a
     *             result set
     * @throws NonUniqueResultException
     *             if the number of fetched rows is greater than or equal to 2
     * @throws JdbcException
     *             if a JDBC related error occurs
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
     * Executes an SQL SELECT statement and returns a single entity result as
     * {@link Optional}.
     * 
     * @param <RESULT>
     *            the entity type
     * @param resultClass
     *            the entity class
     * @return a single entity
     * @throws DomaNullPointerException
     *             if {@code resultClass} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code resultClass} is not entity class
     * @throws UnknownColumnException
     *             if there is the column that is unknown to the entity
     * @throws NoResultException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResult(boolean)} and the result is
     *             none
     * @throws ResultMappingException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResultMapping(boolean)} and all
     *             properties in the entity are not mapped to columns in a
     *             result set
     * @throws NonUniqueResultException
     *             if the number of fetched rows is greater than or equal to 2
     * @throws JdbcException
     *             if a JDBC related error occurs
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
     * Executes an SQL SELECT statement and returns a single scalar result.
     * 
     * @param <RESULT>
     *            the basic type or the holder type
     * @param resultClass
     *            the basic class or the holder class
     * @return a single scalar or {@code null} if the result is none
     * @throws DomaNullPointerException
     *             if {@code resultClass} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code resultClass} is not entity class
     * @throws NonSingleColumnException
     *             if there are multiple columns in a result set
     * @throws NoResultException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResult(boolean)} and the result is
     *             none
     * @throws NonUniqueResultException
     *             if the number of fetched rows is greater than or equal to 2
     * @throws JdbcException
     *             if a JDBC related error occurs
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
     * Executes an SQL SELECT statement and returns a single scalar result as
     * {@link Optional}.
     * <p>
     * 
     * @param <RESULT>
     *            the basic type or the holder type
     * @param resultClass
     *            the basic class or the holder class
     * @return a single scalar
     * @throws DomaNullPointerException
     *             if {@code resultClass} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code resultClass} is neither the basic class nor the
     *             holder class
     * @throws NonSingleColumnException
     *             if there are multiple columns in a result set
     * @throws NoResultException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResult(boolean)} and the result is
     *             none
     * @throws NonUniqueResultException
     *             if the number of fetched rows is greater than or equal to 2
     * @throws JdbcException
     *             if a JDBC related error occurs
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
     * Executes an SQL SELECT statement and returns a single map result.
     * 
     * @param mapKeyNamingType
     *            a naming convention for the keys of the map
     * @return a single map or {@code null} if the result is none
     * @throws DomaNullPointerException
     *             if {@code mapKeyNamingType} is {@code null}
     * @throws NoResultException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResult(boolean)} and the result is
     *             none
     * @throws NonUniqueResultException
     *             if the number of fetched rows is greater than or equal to 2
     * @throws JdbcException
     *             if a JDBC related error occurs
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
     * Executes an SQL SELECT statement and returns a single map result as
     * {@link Optional}.
     * 
     * @param mapKeyNamingType
     *            a naming convention for the keys of the map
     * @return a single map
     * @throws DomaNullPointerException
     *             if {@code mapKeyNamingType} is {@code null}
     * @throws NoResultException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResult(boolean)} and the result is
     *             none
     * @throws NonUniqueResultException
     *             if the number of fetched rows is greater than or equal to 2
     * @throws JdbcException
     *             if a JDBC related error occurs
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
     * Executes an SQL SELECT statement and returns the entity results.
     * 
     * @param <ELEMENT>
     *            the entity type
     * @param elementClass
     *            the entity class
     * @return the entity results
     * @throws DomaNullPointerException
     *             if {@code elementClass} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code elementClass} is not entity class
     * @throws UnknownColumnException
     *             if there is the column that is unknown to the entity
     * @throws NoResultException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResult(boolean)} and the result is
     *             none
     * @throws ResultMappingException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResultMapping(boolean)} and all
     *             properties in the entity are not mapped to columns in a
     *             result set
     * @throws JdbcException
     *             if a JDBC related error occurs
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
     * Executes an SQL SELECT statement and returns the scalar results.
     * 
     * @param <ELEMENT>
     *            the basic type or the holder type
     * @param elementClass
     *            the basic class or the holder class
     * @return the scalar results
     * @throws DomaNullPointerException
     *             if {@code elementClass} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code elementClass} is neither the basic class nor the
     *             holder class
     * @throws UnknownColumnException
     *             if there is the column that is unknown to the entity
     * @throws NoResultException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResult(boolean)} and the result is
     *             none
     * @throws ResultMappingException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResultMapping(boolean)} and all
     *             properties in the entity are not mapped to columns in a
     *             result set
     * @throws JdbcException
     *             if a JDBC related error occurs
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
     * Executes an SQL SELECT statement and returns the scalar results that are
     * wrapped with {@link Optional}.
     * 
     * @param <ELEMENT>
     *            the basic type or the holder type
     * @param elementClass
     *            the basic class or the holder class
     * @return the scalar results
     * @throws DomaNullPointerException
     *             if {@code elementClass} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code elementClass} is neither the basic class nor the
     *             holder class
     * @throws UnknownColumnException
     *             if there is the column that is unknown to the entity
     * @throws NoResultException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResult(boolean)} and the result is
     *             none
     * @throws ResultMappingException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResultMapping(boolean)} and all
     *             properties in the entity are not mapped to columns in a
     *             result set
     * @throws JdbcException
     *             if a JDBC related error occurs
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
     * Executes an SQL SELECT statement and returns the map results.
     * 
     * @param mapKeyNamingType
     *            a naming convention for the keys of the map
     * @return the map results
     * @throws DomaNullPointerException
     *             if {@code mapKeyNamingType} is {@code null}
     * @throws JdbcException
     *             if a JDBC related error occurs
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
     * Executes an SQL SELECT statement and returns the stream of entity
     * results.
     * <p>
     * The caller must close the stream.
     * 
     * @param <TARGET>
     *            the entity type
     * @param targetClass
     *            the entity class
     * @return the stream of the entity results
     * @throws DomaNullPointerException
     *             if {@code targetClass} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code targetClass} is not entity class
     * @throws JdbcException
     *             if a JDBC related error occurs
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
     * Executes an SQL SELECT statement, handles the stream of entity values and
     * returns the result.
     * 
     * @param <RESULT>
     *            the result type
     * @param <TARGET>
     *            the entity type
     * @param targetClass
     *            the entity class
     * @param mapper
     *            the mapper function
     * @return the result
     * @throws DomaNullPointerException
     *             if {@code targetClass} or {@code mapper} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code targetClass} is not entity class
     * @throws UnknownColumnException
     *             if there is the column that is unknown to the entity
     * @throws NoResultException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResult(boolean)} and the result is
     *             none
     * @throws ResultMappingException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResultMapping(boolean)} and all
     *             properties in the entity are not mapped to columns in a
     *             result set
     * @throws JdbcException
     *             if a JDBC related error occurs
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
     * Executes an SQL SELECT statement and returns the stream of scalar
     * results.
     * <p>
     * The caller must close the stream.
     * 
     * @param <TARGET>
     *            the basic type or the holder type
     * @param targetClass
     *            the basic class or the holder class
     * @return the stream of scalar results
     * @throws DomaNullPointerException
     *             if {@code targetClass} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code targetClass} is neither the basic class nor the
     *             holder class
     * @throws JdbcException
     *             if a JDBC related error occurs
     */
    public <TARGET> Stream<TARGET> streamScalar(Class<TARGET> targetClass) {
        if (targetClass == null) {
            throw new DomaNullPointerException("targetClass");
        }
        query.setResultStream(true);
        return streamScalarInternal(targetClass, Function.identity());
    }

    /**
     * Executes an SQL SELECT statement, handles the stream of scalar values and
     * returns the result.
     * 
     * @param <RESULT>
     *            the result type
     * @param <TARGET>
     *            the basic type or the holder type
     * @param targetClass
     *            the basic class or the holder class
     * @param mapper
     *            the mapper function
     * @return the result
     * @throws DomaNullPointerException
     *             if {@code targetClass} or {@code mapper} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code targetClass} is neither the basic class nor the
     *             holder class
     * @throws NonSingleColumnException
     *             if there are multiple columns in a result set
     * @throws NoResultException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResult(boolean)} and the result is
     *             none
     * @throws JdbcException
     *             if a JDBC related error occurs
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
     * Executes an SQL SELECT statement and returns the stream of
     * {@code Optional} scalar results.
     * <p>
     * The caller must close the stream.
     * 
     * @param <TARGET>
     *            the basic type or the holder type
     * @param targetClass
     *            the basic class or the holder class
     * @return the stream of {@code Optional} scalar results
     * @throws DomaNullPointerException
     *             if {@code targetClass} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code targetClass} is neither the basic class nor the
     *             holder class
     * @throws JdbcException
     *             if a JDBC related error occurs
     */
    public <TARGET> Stream<Optional<TARGET>> streamOptionalScalar(Class<TARGET> targetClass) {
        if (targetClass == null) {
            throw new DomaNullPointerException("targetClass");
        }
        query.setResultStream(true);
        return streamOptionalScalarInternal(targetClass, Function.identity());
    }

    /**
     * Executes an SQL SELECT statement, handles the stream of {@link Optional}
     * scalar values and returns the result.
     * 
     * @param <RESULT>
     *            the result type
     * @param <TARGET>
     *            the basic type or the holder type
     * @param targetClass
     *            the basic class or the holder class
     * @param mapper
     *            the mapper function
     * @return the result
     * @throws DomaNullPointerException
     *             if {@code targetClass} or {@code mapper} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code targetClass} is neither the basic class nor the
     *             holder class
     * @throws NonSingleColumnException
     *             if there are multiple columns in a result set
     * @throws NoResultException
     *             if you set {@code true} to
     *             {@link SelectBuilder#ensureResult(boolean)} and the result is
     *             none
     * @throws JdbcException
     *             if a JDBC related error occurs
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
     * Executes an SQL SELECT statement and returns the stream of map results.
     * <p>
     * The caller must close the stream.
     * 
     * @param mapKeyNamingType
     *            a naming convention for the keys of the map
     * @return the map results
     * @throws DomaNullPointerException
     *             if {@code mapKeyNamingType} is {@code null}
     * @throws JdbcException
     *             if a JDBC related error occurs
     */
    public Stream<Map<String, Object>> streamMap(MapKeyNamingType mapKeyNamingType) {
        if (mapKeyNamingType == null) {
            throw new DomaNullPointerException("mapKeyNamingType");
        }
        query.setResultStream(true);
        return streamMapInternal(mapKeyNamingType, Function.identity());
    }

    /**
     * Executes an SQL SELECT statement, handles the stream of map values and
     * returns the result.
     * 
     * @param <RESULT>
     *            the result type
     * @param mapKeyNamingType
     *            a naming convention for the keys of the map
     * @param mapper
     *            the mapper function
     * @return the result
     * @throws DomaNullPointerException
     *             if {@code mapKeyNamingType} or {@code mapper} is {@code null}
     * @throws JdbcException
     *             if a JDBC related error occurs
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
     * Whether to ensure that one or more rows are found in a result set.
     * 
     * @param ensureResult
     *            whether to ensure
     */
    public void ensureResult(boolean ensureResult) {
        query.setResultEnsured(ensureResult);
    }

    /**
     * Whether to ensure that all entity properties are mapped to columns of a
     * result set.
     * 
     * @param ensureResultMapping
     *            whether to ensure
     */
    public void ensureResultMapping(boolean ensureResultMapping) {
        query.setResultMappingEnsured(ensureResultMapping);
    }

    /**
     * Set the fetch type.
     * 
     * @param fetchType
     *            the fetch type
     */
    public void fetch(FetchType fetchType) {
        query.setFetchType(fetchType);
    }

    /**
     * Sets the fetch size.
     * <p>
     * If not specified, the value of {@link Config#getFetchSize()} is used.
     * 
     * @param fetchSize
     *            the fetch size
     * @see Statement#setFetchSize(int)
     */
    public void fetchSize(int fetchSize) {
        query.setFetchSize(fetchSize);
    }

    /**
     * Sets the maximum number of rows for a {@code ResultSet} object.
     * <p>
     * If not specified, the value of {@link Config#getMaxRows()} is used.
     * 
     * @param maxRows
     *            the maximum number of rows
     * @see Statement#setMaxRows(int)
     */
    public void maxRows(int maxRows) {
        query.setMaxRows(maxRows);
    }

    /**
     * Sets the query timeout limit in seconds.
     * <p>
     * If not specified, the value of {@link Config#getQueryTimeout()} is used.
     * 
     * @param queryTimeout
     *            the query timeout limit in seconds
     * @see Statement#setQueryTimeout(int)
     */
    public void queryTimeout(int queryTimeout) {
        query.setQueryTimeout(queryTimeout);
    }

    /**
     * Sets the SQL log format.
     * 
     * @param sqlLogType
     *            the SQL log format type
     */
    public void sqlLogType(SqlLogType sqlLogType) {
        if (sqlLogType == null) {
            throw new DomaNullPointerException("sqlLogType");
        }
        query.setSqlLogType(sqlLogType);
    }

    /**
     * Sets the caller class name.
     * <p>
     * If not specified, the class name of this instance is used.
     * 
     * @param className
     *            the caller class name
     * @throws DomaNullPointerException
     *             if {@code className} is {@code null}
     */
    public void callerClassName(String className) {
        if (className == null) {
            throw new DomaNullPointerException("className");
        }
        query.setCallerClassName(className);
    }

    /**
     * Sets the caller method name.
     * 
     * @param methodName
     *            the caller method name
     * @throws DomaNullPointerException
     *             if {@code methodName} is {@code null}
     */
    public void callerMethodName(String methodName) {
        if (methodName == null) {
            throw new DomaNullPointerException("methodName");
        }
        query.setCallerMethodName(methodName);
    }

    /**
     * Sets the options about the SQL SELECT execution.
     * 
     * @param options
     *            the options about the SQL SELECT execution
     * @throws DomaNullPointerException
     *             if {@code options} is {@code null}
     */
    public void options(SelectOptions options) {
        if (options == null) {
            throw new DomaNullPointerException("options");
        }
        query.setOptions(options);
    }

    /**
     * Returns the built SQL.
     * 
     * @return the built SQL
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
