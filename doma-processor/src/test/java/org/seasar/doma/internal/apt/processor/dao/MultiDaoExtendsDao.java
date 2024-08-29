package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;

@Dao
public interface MultiDaoExtendsDao extends MyDao, MyDao2 {}
