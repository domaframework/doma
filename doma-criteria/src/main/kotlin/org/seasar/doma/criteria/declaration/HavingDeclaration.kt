package org.seasar.doma.criteria.declaration

import org.seasar.doma.criteria.context.Criterion
import org.seasar.doma.jdbc.Config

@Declaration
class HavingDeclaration(
    config: Config,
    add: (Criterion) -> Unit
) : ComparisonDeclaration<HavingDeclaration>(config, add, ::HavingDeclaration), AggregateDeclaration
