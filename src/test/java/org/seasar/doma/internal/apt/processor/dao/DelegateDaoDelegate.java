package org.seasar.doma.internal.apt.processor.dao;

import java.math.BigDecimal;
import org.seasar.doma.jdbc.Config;

/** @author taedium */
public class DelegateDaoDelegate {

  protected Config config;

  public DelegateDaoDelegate(Config config) {
    this.config = config;
  }

  public BigDecimal execute(String aaa, Integer bbb) {
    return null;
  }

  public BigDecimal execute2(String aaa, Integer bbb, String... ccc) {
    return null;
  }

  public void execute3(String aaa, Integer bbb, String... ccc) {}
}
