package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;

@Dao(config = MyConfig.class)
public interface MultiDaoExtendsDao extends MyDao, MyDao2 {}
