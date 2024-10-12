package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import org.seasar.doma.jdbc.criteria.option.AssociationOption
import org.seasar.doma.jdbc.criteria.option.DistinctOption
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption
import org.seasar.doma.jdbc.criteria.statement.UnifiedSelectTerminal
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KJoinDeclaration
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KOrderByNameDeclaration
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KWhereDeclaration

class KUnifiedSelectTerminal<ENTITY : Any>(private val statement: UnifiedSelectTerminal<ENTITY>) : KListable<ENTITY> {

    fun distinct(distinctOption: DistinctOption = DistinctOption.basic()): KUnifiedSelectTerminal<ENTITY> {
        statement.distinct(distinctOption)
        return this
    }

    fun innerJoin(
        entityMetamodel: EntityMetamodel<*>,
        block: KJoinDeclaration.() -> Unit,
    ): KUnifiedSelectTerminal<ENTITY> {
        statement.innerJoin(entityMetamodel) { block(KJoinDeclaration(it)) }
        return this
    }

    fun leftJoin(
        entityMetamodel: EntityMetamodel<*>,
        block: KJoinDeclaration.() -> Unit,
    ): KUnifiedSelectTerminal<ENTITY> {
        statement.leftJoin(entityMetamodel) { block(KJoinDeclaration(it)) }
        return this
    }

    fun <ENTITY1, ENTITY2> associate(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> Unit,
    ): KUnifiedSelectTerminal<ENTITY> {
        statement.associate(first, second, associator, AssociationOption.mandatory())
        return this
    }

    fun <ENTITY1, ENTITY2> associate(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> Unit,
        option: AssociationOption,
    ): KUnifiedSelectTerminal<ENTITY> {
        statement.associate(first, second, associator, option)
        return this
    }

    fun <ENTITY1, ENTITY2> associateWith(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> ENTITY1,
    ): KUnifiedSelectTerminal<ENTITY> {
        statement.associateWith(first, second, associator)
        return this
    }

    fun <ENTITY1, ENTITY2> associateWith(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> ENTITY1,
        option: AssociationOption,
    ): KUnifiedSelectTerminal<ENTITY> {
        statement.associateWith(first, second, associator, option)
        return this
    }

    fun where(block: KWhereDeclaration.() -> Unit): KUnifiedSelectTerminal<ENTITY> {
        statement.where { block(KWhereDeclaration(it)) }
        return this
    }

    fun orderBy(block: KOrderByNameDeclaration.() -> Unit): KUnifiedSelectTerminal<ENTITY> {
        statement.orderBy { block(KOrderByNameDeclaration(it)) }
        return this
    }

    fun limit(limit: Int?): KUnifiedSelectTerminal<ENTITY> {
        statement.limit(limit)
        return this
    }

    fun offset(offset: Int?): KUnifiedSelectTerminal<ENTITY> {
        statement.offset(offset)
        return this
    }

    fun forUpdate(option: ForUpdateOption = ForUpdateOption.basic()): KUnifiedSelectTerminal<ENTITY> {
        statement.forUpdate(option)
        return this
    }

    fun <RESULT> project(entityMetamodel: EntityMetamodel<RESULT>): KListable<RESULT> {
        val listable = statement.project(entityMetamodel)
        return object : KListable<RESULT> {
            override fun peek(block: (Sql<*>) -> Unit): KListable<RESULT> {
                listable.peek(block)
                return this
            }

            override fun execute(): List<RESULT> {
                return listable.execute()
            }

            override fun asSql(): Sql<*> {
                return listable.asSql()
            }
        }
    }

    fun <RESULT> projectTo(
        entityMetamodel: EntityMetamodel<RESULT>,
        vararg propertyMetamodels: PropertyMetamodel<*>,
    ): KListable<RESULT> {
        val listable = statement.projectTo(entityMetamodel, *propertyMetamodels)
        return object : KListable<RESULT> {
            override fun peek(block: (Sql<*>) -> Unit): KListable<RESULT> {
                listable.peek(block)
                return this
            }

            override fun execute(): List<RESULT> {
                return listable.execute()
            }

            override fun asSql(): Sql<*> {
                return listable.asSql()
            }
        }
    }

    override fun peek(block: (Sql<*>) -> Unit): KUnifiedSelectTerminal<ENTITY> {
        statement.peek(block)
        return this
    }

    override fun execute(): List<ENTITY> {
        return statement.execute()
    }

    override fun asSql(): Sql<*> {
        return statement.asSql()
    }
}
