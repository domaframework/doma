package org.seasar.doma.internal.apt.processor.dao;

import java.math.BigDecimal;
import org.seasar.doma.Dao;

@Dao
public interface DefaultMethodDao {

  default BigDecimal execute(String aaa, Integer bbb) {
    return BigDecimal.ONE;
  }
}
