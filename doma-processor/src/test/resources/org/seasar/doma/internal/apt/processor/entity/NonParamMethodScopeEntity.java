package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Metamodel;

@Entity(metamodel = @Metamodel(scope = {NonParamMethodScope.class}))
class NonParamMethodScopeEntity {}
