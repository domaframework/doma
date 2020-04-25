package org.seasar.doma.criteria

import org.seasar.doma.jdbc.entity.EntityPropertyType
import org.seasar.doma.jdbc.entity.EntityType

@Declaration
class SqlDeclaration {
    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>, RESULT> from(
        entityTypeProvider: () -> ENTITY_TYPE,
        block: SqlSelectDeclaration.(ENTITY_TYPE) -> Pair<List<EntityPropertyType<*, *>>, (Row) -> RESULT>
    ): Statement<List<RESULT>> {
        return SelectSqlStatement(entityTypeProvider, block)
    }

    val delete = Delete

    object Delete {
        fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> from(
            entityTypeProvider: () -> ENTITY_TYPE,
            block: DeleteDeclaration.(ENTITY_TYPE) -> Unit
        ): Statement<Int> {
            return DeleteSqlStatement(entityTypeProvider, block)
        }
    }

    val insert = Insert

    object Insert {
        fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> into(
            entityTypeProvider: () -> ENTITY_TYPE,
            block: InsertDeclaration.(ENTITY_TYPE) -> Unit
        ): Statement<Int> {
            return InsertSqlStatement(entityTypeProvider, block)
        }
    }

    fun <ENTITY, ENTITY_TYPE : EntityType<ENTITY>> update(
        entityTypeProvider: () -> ENTITY_TYPE,
        block: UpdateDeclaration.(ENTITY_TYPE) -> Unit
    ): Statement<Int> {
        return UpdateSqlStatement(entityTypeProvider, block)
    }
}
