package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;

@Dao(config = MyConfig.class)
public interface MultiDaoExtendsDao extends MyDao, MyDao2 {}
