package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;

@Dao(config = MyConfig.class)
public interface StaticMethodDao {

  static String hello(String name) {
    return "hello " + name;
  }
}
