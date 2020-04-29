package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.statement.SetOperator
import org.seasar.doma.criteria.statement.SqlDeleteStatement
import org.seasar.doma.criteria.statement.SqlInsertStatement
import org.seasar.doma.criteria.statement.SqlSelectStatement
import org.seasar.doma.criteria.statement.SqlSetStatement
import org.seasar.doma.criteria.statement.SqlUpdateStatement
import org.seasar.doma.criteria.statement.Statement
import org.seasar.doma.def.EntityDef

@Declaration
class SqlDeclaration {
    fun <ENTITY, ENTITY_DEF : EntityDef<ENTITY>, RESULT_ELEMENT> from(
        entityDefProvider: () -> ENTITY_DEF,
        block: SqlSelectDeclaration.(ENTITY_DEF) -> SqlSelectResult<RESULT_ELEMENT>
    ): SqlSelectStatement<ENTITY, ENTITY_DEF, RESULT_ELEMENT> {
        return SqlSelectStatement(entityDefProvider, block)
    }

    val delete = Delete

    object Delete {
        fun <ENTITY, ENTITY_DEF : EntityDef<ENTITY>> from(
            entityDefProvider: () -> ENTITY_DEF,
            block: DeleteDeclaration.(ENTITY_DEF) -> Unit
        ): Statement<Int> {
            return SqlDeleteStatement(entityDefProvider, block)
        }
    }

    val insert = Insert

    object Insert {
        fun <ENTITY, ENTITY_DEF : EntityDef<ENTITY>> into(
            entityDefProvider: () -> ENTITY_DEF,
            block: InsertDeclaration.(ENTITY_DEF) -> Unit
        ): Statement<Int> {
            return SqlInsertStatement(entityDefProvider, block)
        }
    }

    fun <ENTITY, ENTITY_DEF : EntityDef<ENTITY>> update(
        entityDefProvider: () -> ENTITY_DEF,
        block: UpdateDeclaration.(ENTITY_DEF) -> Unit
    ): Statement<Int> {
        return SqlUpdateStatement(entityDefProvider, block)
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
