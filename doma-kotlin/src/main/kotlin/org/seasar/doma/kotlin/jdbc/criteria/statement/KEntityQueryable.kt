package org.seasar.doma.kotlin.jdbc.criteria.statement

import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import org.seasar.doma.jdbc.criteria.option.AssociationOption
import org.seasar.doma.jdbc.criteria.option.DistinctOption
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KJoinDeclaration
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KOrderByNameDeclaration
import org.seasar.doma.kotlin.jdbc.criteria.declaration.KWhereDeclaration

/**
 * Represents a queryable statement for entities.
 *
 * <p>Note: {@link #project(EntityMetamodel)} and {@link #projectTo(EntityMetamodel,
 * PropertyMetamodel[])} will remove duplicate entities from the results.
 *
 * @param <ENTITY> the type of the entity
 */
interface KEntityQueryable<ENTITY> : KListable<ENTITY> {

    fun distinct(distinctOption: DistinctOption = DistinctOption.basic()): KEntityQueryable<ENTITY>

    fun innerJoin(
        entityMetamodel: EntityMetamodel<*>,
        block: KJoinDeclaration.() -> Unit,
    ): KEntityQueryable<ENTITY>

    fun leftJoin(
        entityMetamodel: EntityMetamodel<*>,
        block: KJoinDeclaration.() -> Unit,
    ): KEntityQueryable<ENTITY>

    fun <ENTITY1, ENTITY2> associate(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> Unit,
    ): KEntityQueryable<ENTITY>

    fun <ENTITY1, ENTITY2> associate(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> Unit,
        option: AssociationOption,
    ): KEntityQueryable<ENTITY>

    fun <ENTITY1, ENTITY2> associateWith(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> ENTITY1,
    ): KEntityQueryable<ENTITY>

    fun <ENTITY1, ENTITY2> associateWith(
        first: EntityMetamodel<ENTITY1>,
        second: EntityMetamodel<ENTITY2>,
        associator: (ENTITY1, ENTITY2) -> ENTITY1,
        option: AssociationOption,
    ): KEntityQueryable<ENTITY>

    fun where(block: KWhereDeclaration.() -> Unit): KEntityQueryable<ENTITY>

    fun orderBy(block: KOrderByNameDeclaration.() -> Unit): KEntityQueryable<ENTITY>

    fun limit(limit: Int?): KEntityQueryable<ENTITY>

    fun offset(offset: Int?): KEntityQueryable<ENTITY>

    fun forUpdate(option: ForUpdateOption = ForUpdateOption.basic()): KEntityQueryable<ENTITY>

    fun <RESULT> project(entityMetamodel: EntityMetamodel<RESULT>): KListable<RESULT>

    fun <RESULT> projectTo(
        entityMetamodel: EntityMetamodel<RESULT>,
        vararg propertyMetamodels: PropertyMetamodel<*>,
    ): KListable<RESULT>
}
