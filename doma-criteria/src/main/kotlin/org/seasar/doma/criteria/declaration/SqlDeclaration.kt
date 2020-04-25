package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.statement.SqlDeleteStatement
import org.seasar.doma.criteria.statement.SqlInsertStatement
import org.seasar.doma.criteria.statement.SqlSelectStatement
import org.seasar.doma.criteria.statement.SqlUpdateStatement
import org.seasar.doma.criteria.statement.Statement
import org.seasar.doma.jdbc.entity.EntityType

@Declaration
class SqlDeclaration {
    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>, RESULT> from(
        entityTypeProvider: () -> ENTITY_TYPE,
        block: SqlSelectDeclaration.(ENTITY_TYPE) -> SqlSelectResult<RESULT>
    ): Statement<List<RESULT>> {
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
}
