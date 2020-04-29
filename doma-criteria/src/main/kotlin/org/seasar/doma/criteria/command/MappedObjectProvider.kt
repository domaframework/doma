package org.seasar.doma.criteria.command

import java.sql.ResultSet
import org.seasar.doma.criteria.declaration.Row
import org.seasar.doma.def.PropertyDef
import org.seasar.doma.internal.jdbc.command.AbstractObjectProvider
import org.seasar.doma.jdbc.query.Query

class MappedObjectProvider<RESULT>(
    query: Query,
    propDefs: List<PropertyDef<*>>,
    private val mapper: (Row) -> RESULT
) : AbstractObjectProvider<RESULT>() {

    private val jdbcMappingVisitor = query.config.dialect.jdbcMappingVisitor

    private val indexMap = propDefs.mapIndexed { index, propDef ->
        propDef to index + 1
    }.toMap()

    override fun get(resultSet: ResultSet?): RESULT {
        val row = object : Row {
            override fun <PROPERTY> get(propDef: PropertyDef<PROPERTY>): PROPERTY? {
                val index = indexMap[propDef]
                require(index != null) {
                    "The propDef is unknown. " +
                        "Ensure that you specified it to the select function."
                }
                val prop = propDef.asType().createProperty()
                fetch(resultSet, prop, index, jdbcMappingVisitor)
                @Suppress("UNCHECKED_CAST")
                return prop.get() as PROPERTY?
            }
        }
        return mapper(row)
    }
}
