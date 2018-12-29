package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;

@Dao(config = MyConfig.class)
public interface NotOnlyDefaultMethodsExtendsDao extends NotOnlyDefaultMethodsChild<String> {}
