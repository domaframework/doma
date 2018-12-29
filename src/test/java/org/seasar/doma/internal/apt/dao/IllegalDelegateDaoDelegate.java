package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.jdbc.Config;

public class IllegalDelegateDaoDelegate {

  public IllegalDelegateDaoDelegate(Config config) {}

  public long illegalReturnType() {
    return 0;
  }

  public int illegalParameterType(String aaa, Integer bbb) {
    return 0;
  }
}
