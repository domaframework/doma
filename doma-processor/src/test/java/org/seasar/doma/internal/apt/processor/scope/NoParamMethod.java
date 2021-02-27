package org.seasar.doma.internal.apt.processor.scope;

import org.seasar.doma.Entity;
import org.seasar.doma.Metamodel;

@Entity(metamodel = @Metamodel(scopes = {NoParamMethodScope.class}))
public class NoParamMethod {}
