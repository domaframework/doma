package org.seasar.doma.criteria.statement

import org.seasar.doma.criteria.command.MappedObjectIterationHandler
import org.seasar.doma.criteria.query.SelectQuery
import org.seasar.doma.criteria.query.UnionBuilder
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.command.Command
import org.seasar.doma.jdbc.command.SelectCommand

class SqlSetStatement<RESULT_ELEMENT>(
    val operator: SetOperator<SqlSelectStatement<*, *, RESULT_ELEMENT>>
) : AbstractStatement<List<RESULT_ELEMENT>>() {

    override fun commandAndSql(config: Config): Pair<Command<List<RESULT_ELEMENT>>, PreparedSql> {
        val results = operator.map { it.selectResult(config) }
        val (propTypes, mapper) = results.toList().map { it.propTypes to it.mapper }.first()
        val contexts = results.map { it.context }
        val builder = UnionBuilder(config, contexts)
        val sql = builder.build()
        val query = SelectQuery(config, sql)
        val handler = MappedObjectIterationHandler(propTypes, mapper)
        val command = SelectCommand(query, handler)
        return command to sql
    }
}
