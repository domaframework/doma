package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.statement.SetOperator
import org.seasar.doma.criteria.statement.SqlDeleteStatement
import org.seasar.doma.criteria.statement.SqlInsertStatement
import org.seasar.doma.criteria.statement.SqlSelectStatement
import org.seasar.doma.criteria.statement.SqlSetStatement
import org.seasar.doma.criteria.statement.SqlUpdateStatement
import org.seasar.doma.criteria.statement.Statement
import org.seasar.doma.jdbc.entity.EntityType

@Declaration
class SqlDeclaration {
    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>, RESULT_ELEMENT> from(
        entityTypeProvider: () -> ENTITY_TYPE,
        block: SqlSelectDeclaration.(ENTITY_TYPE) -> SqlSelectResult<RESULT_ELEMENT>
    ): SqlSelectStatement<ENTITY, ENTITY_TYPE, RESULT_ELEMENT> {
        return SqlSelectStatement(entityTypeProvider, block)
    }

    val delete = Delete

    object Delete {
        fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> from(
            entityTypeProvider: () -> ENTITY_TYPE,
            block: DeleteDeclaration.(ENTITY_TYPE) -> Unit
        ): Statement<Int> {
            return SqlDeleteStatement(entityTypeProvider, block)
        }
    }

    val insert = Insert

    object Insert {
        fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> into(
            entityTypeProvider: () -> ENTITY_TYPE,
            block: InsertDeclaration.(ENTITY_TYPE) -> Unit
        ): Statement<Int> {
            return SqlInsertStatement(entityTypeProvider, block)
        }
    }

    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> update(
        entityTypeProvider: () -> ENTITY_TYPE,
        block: UpdateDeclaration.(ENTITY_TYPE) -> Unit
    ): Statement<Int> {
        return SqlUpdateStatement(entityTypeProvider, block)
    }

    infix fun <RESULT_ELEMENT> SqlSelectStatement<*, *, RESULT_ELEMENT>.union(
        other: SqlSelectStatement<*, *, RESULT_ELEMENT>
    ): SqlSetStatement<RESULT_ELEMENT> {
        return union(false, SetOperator.Select(this), SetOperator.Select(other))
    }

    infix fun <RESULT_ELEMENT> SqlSelectStatement<*, *, RESULT_ELEMENT>.unionAll(
        other: SqlSelectStatement<*, *, RESULT_ELEMENT>
    ): SqlSetStatement<RESULT_ELEMENT> {
        return union(true, SetOperator.Select(this), SetOperator.Select(other))
    }

    infix fun <RESULT_ELEMENT> SqlSetStatement<RESULT_ELEMENT>.union(
        other: SqlSelectStatement<*, *, RESULT_ELEMENT>
    ): SqlSetStatement<RESULT_ELEMENT> {
        return union(false, this.operator, SetOperator.Select(other))
    }

    infix fun <RESULT_ELEMENT> SqlSetStatement<RESULT_ELEMENT>.unionAll(
        other: SqlSelectStatement<*, *, RESULT_ELEMENT>
    ): SqlSetStatement<RESULT_ELEMENT> {
        return union(true, this.operator, SetOperator.Select(other))
    }

    infix fun <RESULT_ELEMENT> SqlSelectStatement<*, *, RESULT_ELEMENT>.union(
        other: SqlSetStatement<RESULT_ELEMENT>
    ): SqlSetStatement<RESULT_ELEMENT> {
        return union(false, SetOperator.Select(this), other.operator)
    }

    infix fun <RESULT_ELEMENT> SqlSelectStatement<*, *, RESULT_ELEMENT>.unionAll(
        other: SqlSetStatement<RESULT_ELEMENT>
    ): SqlSetStatement<RESULT_ELEMENT> {
        return union(true, SetOperator.Select(this), other.operator)
    }

    private fun <RESULT_ELEMENT> union(
        all: Boolean,
        left: SetOperator<SqlSelectStatement<*, *, RESULT_ELEMENT>>,
        right: SetOperator<SqlSelectStatement<*, *, RESULT_ELEMENT>>
    ): SqlSetStatement<RESULT_ELEMENT> {
        val operator = SetOperator.Union(all, left, right)
        return SqlSetStatement(operator)
    }
}
