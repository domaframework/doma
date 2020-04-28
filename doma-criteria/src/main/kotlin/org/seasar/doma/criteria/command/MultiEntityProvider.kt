package org.seasar.doma.criteria.command

import java.sql.ResultSet
import org.seasar.doma.def.EntityDef
import org.seasar.doma.internal.jdbc.command.JdbcValueGetter
import org.seasar.doma.jdbc.JdbcMappable
import org.seasar.doma.jdbc.JdbcMappingFunction
import org.seasar.doma.jdbc.ObjectProvider
import org.seasar.doma.jdbc.query.Query
import org.seasar.doma.jdbc.type.JdbcType
import org.seasar.doma.wrapper.Wrapper

class MultiEntityProvider(private val entityDefs: List<EntityDef<Any>>, query: Query) : ObjectProvider<MultiEntity> {

    private val jdbcMappingVisitor = query.config.dialect.jdbcMappingVisitor

    override fun get(resultSet: ResultSet): MultiEntity {
        val multiEntity = MultiEntity()
        var index = 1
        for (entityDef in entityDefs) {
            val entityType = entityDef.asType()
            val triples = entityType.entityPropertyTypes.map { propType ->
                val (prop, rawValue) = propType.createProperty().let { prop ->
                    val rawValue = fetch(resultSet, prop, index++)
                    prop to rawValue
                }
                Triple(propType, prop, rawValue)
            }
            if (triples.all { it.third == null }) {
                continue
            }
            val key = if (entityType.idPropertyTypes.isEmpty()) {
                EntityKey(listOf(Any()))
            } else {
                triples.filter { it.first.isId }.map { it.second.wrapper.get() }.let { EntityKey(it) }
            }
            val data = triples.map { (propType, prop) -> propType.name to prop }.toMap().let { EntityData(it) }
            multiEntity.keyDataMap[entityDef] = key to data
        }
        return multiEntity
    }

    private fun fetch(resultSet: ResultSet, mappable: JdbcMappable<*>, index: Int): Any? {
        val wrapper = mappable.wrapper
        val proxy = JdbcValueGetterProxy(JdbcValueGetter(resultSet, index))
        wrapper.accept(jdbcMappingVisitor, proxy, mappable)
        return proxy.rawValue
    }
}

internal class JdbcValueGetterProxy(private val jdbcValueGetter: JdbcValueGetter) : JdbcMappingFunction by jdbcValueGetter {
    var rawValue: Any? = null

    override fun <R : Any?, V : Any?> apply(wrapper: Wrapper<V>, jdbcType: JdbcType<V>): R {
        val proxy = JdbcTypeProxy(jdbcType)
        return jdbcValueGetter.apply<R, V>(wrapper, proxy).also {
            rawValue = proxy.rawValue
        }
    }
}

internal class JdbcTypeProxy<T>(private val jdbcType: JdbcType<T>) : JdbcType<T> by jdbcType {
    var rawValue: T? = null

    override fun getValue(resultSet: ResultSet, index: Int): T? {
        return jdbcType.getValue(resultSet, index).also {
            rawValue = it
        }
    }
}
