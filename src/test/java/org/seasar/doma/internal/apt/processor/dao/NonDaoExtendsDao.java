package org.seasar.doma.internal.apt.processor.dao;

import java.io.Serializable;
import org.seasar.doma.Dao;

@Dao(config = MyConfig.class)
public interface NonDaoExtendsDao extends Serializable {}
