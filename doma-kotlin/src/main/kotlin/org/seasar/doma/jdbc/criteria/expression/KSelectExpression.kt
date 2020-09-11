package org.seasar.doma.jdbc.criteria.expression

import org.seasar.doma.jdbc.criteria.declaration.KSubSelectFromDeclaration
import org.seasar.doma.jdbc.criteria.declaration.SubSelectFromDeclaration
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel

class KSelectExpression {

    class Declaration {
        fun <ENTITY> from(entityMetamodel: EntityMetamodel<ENTITY>): KSubSelectFromDeclaration<ENTITY> {
            val declaration = SubSelectFromDeclaration<ENTITY>(entityMetamodel)
            return KSubSelectFromDeclaration(declaration)
        }
    }
}
