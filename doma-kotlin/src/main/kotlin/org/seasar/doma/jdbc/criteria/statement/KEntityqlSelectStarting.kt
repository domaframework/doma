package org.seasar.doma.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql
import org.seasar.doma.jdbc.criteria.declaration.KJoinDeclaration
import org.seasar.doma.jdbc.criteria.declaration.KOrderByNameDeclaration
import org.seasar.doma.jdbc.criteria.declaration.KWhereDeclaration
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import org.seasar.doma.jdbc.criteria.option.AssociationOption
import org.seasar.doma.jdbc.criteria.option.DistinctOption
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption

class KEntityqlSelectStarting<ENTITY>(private val statement: EntityqlSelectStarting<ENTITY>) : KListable<ENTITY> {

    fun distinct(distinctOption: DistinctOption = DistinctOption.basic()): KEntityqlSelectStarting<ENTITY> {
        statement.distinct(distinctOption)
        return this
    }

    fun innerJoin(
        entityMetamodel: EntityMetamodel<*>,
        block: KJoinDeclaration.() -> Unit
    ): KEntityqlSelectStarting<ENTITY> {
        statement.innerJoin(entityMetamodel) { block(KJoinDeclaration(it)) }
        return this
    }

    fun leftJoin(
        entityMetamodel: EntityMetamodel<*>,
        block: KJoinDeclaration.() -> Unit
    ): KEntityqlSelectStarting<ENTITY> {
        statement.leftJoin(entityMetamodel) { block(KJoinDeclaration(it)) }
        return this
    }

    fun <ENTITY1, ENTITY2> associate(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> Unit
    ): KEntityqlSelectStarting<ENTITY> {
        statement.associate(first, second, associator, AssociationOption.mandatory())
        return this
    }

    fun <ENTITY1, ENTITY2> associate(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> Unit,
        option: AssociationOption
    ): KEntityqlSelectStarting<ENTITY> {
        statement.associate(first, second, associator, option)
        return this
    }

    fun <ENTITY1, ENTITY2> associateWith(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> ENTITY1
    ): KEntityqlSelectStarting<ENTITY> {
        statement.associateWith(first, second, associator)
        return this
    }

    fun <ENTITY1, ENTITY2> associateWith(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> ENTITY1,
        option: AssociationOption
    ): KEntityqlSelectStarting<ENTITY> {
        statement.associateWith(first, second, associator, option)
        return this
    }

    fun where(block: KWhereDeclaration.() -> Unit): KEntityqlSelectStarting<ENTITY> {
        statement.where { block(KWhereDeclaration(it)) }
        return this
    }

    fun orderBy(block: KOrderByNameDeclaration.() -> Unit): KEntityqlSelectStarting<ENTITY> {
        statement.orderBy { block(KOrderByNameDeclaration(it)) }
        return this
    }

    fun limit(limit: Int?): KEntityqlSelectStarting<ENTITY> {
        statement.limit(limit)
        return this
    }

    fun offset(offset: Int?): KEntityqlSelectStarting<ENTITY> {
        statement.offset(offset)
        return this
    }

    fun forUpdate(option: ForUpdateOption = ForUpdateOption.basic()): KEntityqlSelectStarting<ENTITY> {
        statement.forUpdate(option)
        return this
    }

    fun <RESULT> select(entityMetamodel: EntityMetamodel<RESULT>): KEntityqlSelectTerminal<RESULT> {
        val terminal = statement.select(entityMetamodel)
        return KEntityqlSelectTerminal(terminal)
    }

    fun <RESULT> selectTo(
        entityMetamodel: EntityMetamodel<RESULT>,
        vararg propertyMetamodels: PropertyMetamodel<*>
    ): KEntityqlSelectTerminal<RESULT> {
        val terminal = statement.selectTo(entityMetamodel, *propertyMetamodels)
        return KEntityqlSelectTerminal(terminal)
    }

    override fun peek(block: (Sql<*>) -> Unit): KListable<ENTITY> {
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
