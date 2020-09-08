package org.seasar.doma.jdbc.criteria.declaration

import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel

class KInsertSelectDeclaration {

    fun <ENTITY> from(entityMetamodel: EntityMetamodel<ENTITY>): KSubSelectFromDeclaration<ENTITY> {
        val declaration = SubSelectFromDeclaration<ENTITY>(entityMetamodel)
        return KSubSelectFromDeclaration(declaration)
    }
}