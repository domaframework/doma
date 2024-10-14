package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import org.seasar.doma.jdbc.criteria.option.AssociationOption
import org.seasar.doma.jdbc.criteria.option.DistinctOption
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption
import org.seasar.doma.jdbc.criteria.statement.EntityQueryable
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KJoinDeclaration
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KOrderByNameDeclaration
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KWhereDeclaration

class KUnifiedSelectTerminal<ENTITY : Any>(private val statement: EntityQueryable<ENTITY>) : KEntityQueryable<ENTITY> {

    override fun distinct(distinctOption: DistinctOption): KEntityQueryable<ENTITY> {
        statement.distinct(distinctOption)
        return this
    }

    override fun innerJoin(
        entityMetamodel: EntityMetamodel<*>,
        block: KJoinDeclaration.() -> Unit,
    ): KEntityQueryable<ENTITY> {
        statement.innerJoin(entityMetamodel) { block(KJoinDeclaration(it)) }
        return this
    }

    override fun leftJoin(
        entityMetamodel: EntityMetamodel<*>,
        block: KJoinDeclaration.() -> Unit,
    ): KEntityQueryable<ENTITY> {
        statement.leftJoin(entityMetamodel) { block(KJoinDeclaration(it)) }
        return this
    }

    override fun <ENTITY1, ENTITY2> associate(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> Unit,
    ): KEntityQueryable<ENTITY> {
        statement.associate(first, second, associator, AssociationOption.mandatory())
        return this
    }

    override fun <ENTITY1, ENTITY2> associate(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> Unit,
        option: AssociationOption,
    ): KEntityQueryable<ENTITY> {
        statement.associate(first, second, associator, option)
        return this
    }

    override fun <ENTITY1, ENTITY2> associateWith(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> ENTITY1,
    ): KEntityQueryable<ENTITY> {
        statement.associateWith(first, second, associator)
        return this
    }

    override fun <ENTITY1, ENTITY2> associateWith(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> ENTITY1,
        option: AssociationOption,
    ): KEntityQueryable<ENTITY> {
        statement.associateWith(first, second, associator, option)
        return this
    }

    override fun where(block: KWhereDeclaration.() -> Unit): KEntityQueryable<ENTITY> {
        statement.where { block(KWhereDeclaration(it)) }
        return this
    }

    override fun orderBy(block: KOrderByNameDeclaration.() -> Unit): KEntityQueryable<ENTITY> {
        statement.orderBy { block(KOrderByNameDeclaration(it)) }
        return this
    }

    override fun limit(limit: Int?): KEntityQueryable<ENTITY> {
        statement.limit(limit)
        return this
    }

    override fun offset(offset: Int?): KEntityQueryable<ENTITY> {
        statement.offset(offset)
        return this
    }

    override fun forUpdate(option: ForUpdateOption): KEntityQueryable<ENTITY> {
        statement.forUpdate(option)
        return this
    }

    override fun <RESULT> project(entityMetamodel: EntityMetamodel<RESULT>): KListable<RESULT> {
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

    override fun <RESULT> projectTo(
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
